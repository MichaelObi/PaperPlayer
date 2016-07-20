package net.devdome.paperplayer.playback;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.IBinder;
import android.os.PowerManager;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat.Action;
import android.support.v4.media.MediaMetadataCompat;
import android.support.v4.media.session.MediaSessionCompat;
import android.support.v7.app.NotificationCompat;
import android.util.Log;

import net.devdome.paperplayer.Constants;
import net.devdome.paperplayer.R;
import net.devdome.paperplayer.data.model.Song;
import net.devdome.paperplayer.playback.actions.ActionListenerContract;
import net.devdome.paperplayer.ui.activity.NowPlayingActivity;
import net.devdome.paperplayer.utils.MediaUtils;

import java.util.HashMap;
import java.util.Map;

public class PlayerService extends Service implements MediaPlayer.OnErrorListener, AudioManager
        .OnAudioFocusChangeListener, MediaPlayer.OnCompletionListener, MediaPlayer
        .OnPreparedListener {

    private static final String TAG = "PlayerService";
    private MediaSessionCompat session;
    private MediaPlayer player;
    private int pausedSongSeek = 0;
    private LibraryManager libraryManager;
    private QueueManager queueManager;
    private Map<String, ActionListenerContract> actions = new HashMap<>();

    public PlayerService() {
        super();
        actions.put(Constants.ACTION_PLAY_ALL, new PlayAllAction());
        actions.put(Constants.ACTION_VIEW_NOW_PLAYING, new ViewNowPlayingAction());
        actions.put(Constants.ACTION_SEEK_GET, new SeekGetAction());
        actions.put(Constants.ACTION_SEEK_TO, new SeekToAction());
        actions.put(Constants.ACTION_PAUSE, new PauseAction());
        actions.put(Constants.ACTION_PLAY_ALBUM, new PlayAlbumAction());
        actions.put(Constants.ACTION_NEXT, new NextSongAction());
        actions.put(Constants.ACTION_REQUEST_PLAY_STATE, new RequestStateAction());
        actions.put(Constants.ACTION_PREVIOUS, new PreviousSongAction());
        actions.put(Constants.ACTION_SHUFFLE, new ShuffleAction());
        actions.put(Constants.ACTION_REPEAT, new RepeatAction());
    }

    private void updateCurrentPlaying() {
        Intent sendDetails = new Intent();
        Song song = getCurrentSong();
        sendDetails.putExtra(Constants.SONG_NAME, song.getName());
        sendDetails.putExtra(Constants.SONG_ARTIST, song.getArtist());
        sendDetails.putExtra(Constants.SONG_ALBUM_NAME, song.getAlbumName());
        sendDetails.putExtra(Constants.SONG_ALBUM_ID, song.getAlbumId());
        sendDetails.putExtra(Constants.SONG_PATH, song.getPath());
        sendDetails.putExtra(Constants.SONG_ART, song.getArt());

        try {
            sendDetails.putExtra(Constants.SONG_DURATION, player.getDuration());
            sendDetails.putExtra(Constants.SONG_CURRENT_TIME, player.getCurrentPosition());
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
        sendDetails.setAction(NowPlayingActivity.ACTION_GET_PLAYING_DETAIL);
        sendBroadcast(sendDetails);
    }

    private void pause() {
        if (player != null) {
            if (player.isPlaying()) {
                pausedSongSeek = player.getCurrentPosition();
                player.pause();
                abandonAudioFocus();
            } else {
                if (queueManager.getCurrentQueue().size() > 0) {
                    player.start();
                    player.seekTo(pausedSongSeek);
                    setUpNotification();
                }
            }
            new RequestStateAction().execute(new Intent(), this);
        }

    }

    private void playMusic() {
        Log.d(TAG, "playMusic() called");
        if (player == null) initPlayer();
        player.reset();
        setPlayerDataSource();
        player.prepareAsync();
    }

    private void setPlayerDataSource() {
        Uri uri = Uri.parse(getCurrentSong().getPath());
        try {
            player.setDataSource(getApplicationContext(), uri);
        } catch (Exception e) {
            Log.e(TAG, "playMusic: Cannot set song Uri", e);
        }
    }

    private Song getCurrentSong() {
        return queueManager.getCurrentSong();
    }

    private void playNext() {
        playNext(false);
    }

    private void playNext(boolean userInvoked) {
        pausedSongSeek = 0;
        if (queueManager.next(userInvoked)) { // If playback should continue
            playMusic();
        }
    }

    private void playPrevious() {
        queueManager.previous();
        pausedSongSeek = 0;
        playMusic();
    }

    private void release() {
        if (player != null) {
            player.release();
            player = null;
        }
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        IntentFilter filter = new IntentFilter();
        filter.addAction(Constants.ACTION_PLAY_ALL);
        filter.addAction(Constants.ACTION_PLAY_ALBUM);
        filter.addAction(Constants.ACTION_VIEW_NOW_PLAYING);
        filter.addAction(Constants.ACTION_PAUSE);
        filter.addAction(Constants.ACTION_SEEK_GET);
        filter.addAction(Constants.ACTION_SEEK_TO);
        filter.addAction(Constants.ACTION_NEXT);
        filter.addAction(Constants.ACTION_PREVIOUS);
        filter.addAction(Constants.ACTION_REQUEST_PLAY_STATE);
        filter.addAction(Constants.ACTION_SHUFFLE);
        filter.addAction(Constants.ACTION_REPEAT);
        registerReceiver(new PlayerReceiver(), filter);

        new RequestStateAction().execute(null, this);
        return START_STICKY;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        release();
        session.release();
    }

    @Override
    public void onCreate() {
        super.onCreate();

        libraryManager = new LibraryManager(this);

        queueManager = QueueManager.getInstance();

        queueManager.generateQueue(libraryManager.getAllSongs());

        session = new MediaSessionCompat(getApplicationContext(), Constants.MEDIA_SESSION_TAG);
    }

    private void initPlayer() {
        player = new MediaPlayer();
        player.setOnPreparedListener(this);
        player.setOnCompletionListener(this);
        player.setWakeMode(getApplicationContext(), PowerManager.PARTIAL_WAKE_LOCK);
        player.setAudioStreamType(AudioManager.STREAM_MUSIC);
    }

    private boolean requestAudioFocus() {
//        return true;
        AudioManager audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        int result = audioManager.requestAudioFocus(this, AudioManager.STREAM_MUSIC,
                AudioManager.AUDIOFOCUS_GAIN);
        return result == AudioManager.AUDIOFOCUS_REQUEST_GRANTED;
    }

    @Override
    public void onAudioFocusChange(int focusChange) {
        return;
        /*switch (focusChange) {
            case AudioManager.AUDIOFOCUS_GAIN:
                if (!player.isPlaying()) {
                    playMusic();
                }
                player.setVolume(1.0f, 1.0f);
                break;
            case AudioManager.AUDIOFOCUS_LOSS_TRANSIENT:
                pause();
                break;
            case AudioManager.AUDIOFOCUS_LOSS:
                pause();
                setUpNotification();
                break;
            case AudioManager.AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK:
                if (player.isPlaying()) player.setVolume(0.1f, 0.1f);
                break;
        }*/
    }

    private void abandonAudioFocus() {
        AudioManager audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        audioManager.abandonAudioFocus(this);
        setUpNotification();
        stopForeground(false);
    }

    @Override
    public void onCompletion(MediaPlayer mp) {
        if (mp.getCurrentPosition() > 0) {
            playNext();
        }
    }

    @Override
    public void onPrepared(MediaPlayer mp) {
        if (requestAudioFocus()) {
            mp.start();
            mp.seekTo(pausedSongSeek);
            updateCurrentPlaying();
            new RequestStateAction().execute(new Intent(), this);
            setUpNotification();
        }
    }

    private void setUpNotification() {
        Intent intent = new Intent(Constants.ACTION_VIEW_NOW_PLAYING);
        PendingIntent pi = PendingIntent.getBroadcast(getApplicationContext(), 0,
                intent, PendingIntent.FLAG_UPDATE_CURRENT);

        Bitmap art = MediaUtils.getSongArt(this, queueManager.getCurrentQueue().get
                (queueManager.getCurrentIndex()).getSong().getAlbumId());

        MediaMetadataCompat metadata = new MediaMetadataCompat.Builder()
                .putBitmap(MediaMetadataCompat.METADATA_KEY_ART, art)
                .build();
        session.setMetadata(metadata);

        // Previous Button
        PendingIntent piPrevous = PendingIntent.getBroadcast(this, 70022, new Intent(Constants
                .ACTION_PREVIOUS), 0);
        Action actionPrevious = new Action(R.drawable.ic_skip_previous_24dp, getString(R.string
                .previous), piPrevous);

        int icon = R.drawable.ic_play_arrow_24dp;
        if (player.isPlaying()) {
            icon = R.drawable.ic_pause_24dp;
        }
        PendingIntent piPlayPause = PendingIntent.getBroadcast(this, 70022, new Intent(Constants
                .ACTION_PAUSE), 0);
        Action actionPlayPause = new Action(icon, getString(R.string.pause), piPlayPause);

        PendingIntent piNext = PendingIntent.getBroadcast(this, 70022, new Intent(Constants
                .ACTION_NEXT), 0);
        Action actionNext = new Action(R.drawable.ic_skip_next_24dp, getString(R.string.next),
                piNext);

        Notification notification = new NotificationCompat.Builder(getApplicationContext())
                .setSmallIcon(player.isPlaying() ? R.drawable.ic_play_arrow_24dp : R.drawable
                        .ic_pause_24dp)
                .setContentTitle(queueManager.getCurrentQueue().get(queueManager
                        .getCurrentIndex()).getSong().getName())
                .setContentText(String.format("%s", queueManager.getCurrentQueue().get
                        (queueManager.getCurrentIndex()).getSong().getArtist()))
                .setSubText(queueManager.getCurrentQueue().get(queueManager
                        .getCurrentIndex()).getSong().getAlbumName())
                .setStyle(
                        new NotificationCompat.MediaStyle().setMediaSession(session
                                .getSessionToken())
                )
                .setPriority(NotificationCompat.PRIORITY_MAX)
                .addAction(actionPrevious).addAction(actionPlayPause).addAction(actionNext)
                .setContentIntent(pi)
                .setLargeIcon(art)
                .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
                .setOngoing(player.isPlaying())
                .build();
        startForeground(Constants.NOTIFICATION_ID, notification);
    }

    @Override
    public boolean onError(MediaPlayer mp, int what, int extra) {
        return false;
    }

    private class RepeatAction implements ActionListenerContract {
        @Override
        public void execute(Intent intent, Context context) {
            queueManager.setRepeatState(intent.getStringExtra(Constants.KEY_REPEAT_STATE_EXTRA));
        }
    }

    private class PlayerReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            ActionListenerContract action = actions.get(intent.getAction());
            if (action == null) {
                throw new IllegalStateException("Unknown callback for action: " + intent
                        .getAction());
            }
            action.execute(intent, context);
        }
    }

    private class PlayAllAction implements ActionListenerContract {
        @Override
        public void execute(final Intent intent, final Context context) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    queueManager.generateQueue(libraryManager.getAllSongs(), intent
                            .getLongExtra(Constants.KEY_PLAY_START_WITH, 0));
                    pausedSongSeek = 0;
                    playMusic();
                }
            }).run();
        }
    }

    private class PauseAction implements ActionListenerContract {
        @Override
        public void execute(Intent intent, Context context) {
            pause();
        }
    }

    private class PlayAlbumAction implements ActionListenerContract {
        @Override
        public void execute(final Intent intent, Context context) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    queueManager.generateQueue(libraryManager.getAlbumSongs(intent
                            .getLongExtra(Constants.SONG_ALBUM_ID, 0)), intent.getLongExtra
                            (Constants.KEY_PLAY_START_WITH, 0));
                    pausedSongSeek = 0;
                    playMusic();
                }
            }).run();
        }
    }

    private class SeekToAction implements ActionListenerContract {
        @Override
        public void execute(Intent intent, Context context) {
            try {
                if (!player.isPlaying()) {
                    pausedSongSeek = intent.getIntExtra(Constants.KEY_SEEK_CHANGE, 0);
                }
                player.seekTo(intent.getIntExtra(Constants.KEY_SEEK_CHANGE, 0));
            } catch (NullPointerException e) {
                e.printStackTrace();
                pausedSongSeek = intent.getIntExtra(Constants.KEY_SEEK_CHANGE, 0);
                playMusic();
                pausedSongSeek = 0;
            }
        }
    }

    private class SeekGetAction implements ActionListenerContract {
        @Override
        public void execute(Intent intent, Context context) {
            Intent sIntent = new Intent();
            sIntent.setAction(NowPlayingActivity.ACTION_GET_SEEK_VALUE);
            if (player != null && player.isPlaying()) {
                sIntent.putExtra(Constants.KEY_IS_PLAYING, true);
            }
            if (player != null) {
                sIntent.putExtra(Constants.KEY_SONG_SEEK_VALUE, player.getCurrentPosition());
            }
            sendBroadcast(sIntent);
        }
    }

    private class NextSongAction implements ActionListenerContract {
        @Override
        public void execute(Intent intent, Context context) {
            playNext(true);
        }
    }

    private class PreviousSongAction implements ActionListenerContract {
        @Override
        public void execute(Intent intent, Context context) {
            playPrevious();
        }
    }

    private class ViewNowPlayingAction implements ActionListenerContract {
        @Override
        public void execute(Intent intent, final Context context) {
            if (queueManager.getCurrentQueue().size() > 0) {
                Intent i = new Intent(PlayerService.this, NowPlayingActivity.class);
                Song song = getCurrentSong();
                i.putExtra(Constants.SONG_NAME, song.getName());
                i.putExtra(Constants.SONG_ARTIST, song.getArtist());
                i.putExtra(Constants.SONG_ALBUM_NAME, song.getAlbumName());
                i.putExtra(Constants.SONG_ALBUM_ID, song.getAlbumId());
                i.putExtra(Constants.SONG_PATH, song.getPath());
                i.putExtra(Constants.SONG_ART, song.getArt());
                i.putExtra(Constants.SONG_ID, song.getId());
                i.putExtra(Constants.SONG_CURRENT_TIME, player.getCurrentPosition());
                i.putExtra(Constants.SONG_DURATION, player.getDuration());
                i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(i);
            }
        }
    }

    private class RequestStateAction implements ActionListenerContract {
        @Override
        public void execute(@Nullable Intent intent, Context context) {
            intent = new Intent(NowPlayingActivity.ACTION_GET_PLAY_STATE);
            if (queueManager.getCurrentQueue().size() > 0) {
                Song song = getCurrentSong();
                intent.putExtra(Constants.SONG_NAME, song.getName());
                intent.putExtra(Constants.SONG_ARTIST, song.getArtist());
                intent.putExtra(Constants.SONG_ALBUM_NAME, song.getAlbumName());
                intent.putExtra(Constants.SONG_ALBUM_ID, song.getAlbumId());
                intent.putExtra(Constants.SONG_PATH, song.getPath());
                intent.putExtra(Constants.SONG_ART, song.getArt());

                try {
                    intent.putExtra(Constants.KEY_IS_PLAYING, player.isPlaying());
                    intent.putExtra(Constants.SONG_DURATION, player.getDuration());
                    intent.putExtra(Constants.SONG_CURRENT_TIME, player.getCurrentPosition());
                } catch (NullPointerException e) {
                    e.printStackTrace();
                }
            }
            sendBroadcast(intent);
        }
    }

    private class ShuffleAction implements ActionListenerContract {
        @Override
        public void execute(Intent intent, Context context) {
            queueManager.shuffleQueue();
        }
    }
}
