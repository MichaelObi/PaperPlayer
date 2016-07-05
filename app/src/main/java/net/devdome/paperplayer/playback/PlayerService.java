package net.devdome.paperplayer.playback;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.IBinder;
import android.os.PowerManager;
import android.support.annotation.Nullable;
import android.util.Log;

import net.devdome.paperplayer.data.model.Song;
import net.devdome.paperplayer.ui.activity.NowPlayingActivity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class PlayerService extends Service implements MediaPlayer.OnErrorListener, AudioManager.OnAudioFocusChangeListener, MediaPlayer.OnCompletionListener, MediaPlayer.OnPreparedListener {

    public static final int NOTIFICATION_ID = 107;
    // Song Properties Keys
    public static final String SONG_ID = "song_id";
    public static final String SONG_PATH = "song_path";
    public static final String SONG_NAME = "song_name";
    public static final String SONG_ALBUM_NAME = "song_album_name";
    public static final String SONG_ART = "song_art";
    public static final String SONG_ALBUM_ID = "song_album_id";
    public static final String SONG_ARTIST = "song_artist";
    public static final String SONG_DURATION = "song_duration";
    public static final String SONG_CURRENT_TIME = "song_current_time";
    // Action Keys
    public static final String ACTION_PAUSE = "pause";
    public static final String ACTION_NEXT = "next_song";
    public static final String ACTION_SEEK_TO = "player_seek_to_song";
    public static final String ACTION_PLAY_ALL = "play_all";
    public static final String ACTION_VIEW_NOW_PLAYING = "view_now_playing";
    public static final String ACTION_SEEK_GET = "action_get_seek";
    public static final String ACTION_PLAY_ALBUM = "play_album";
    public static final String ACTION_REQUEST_PLAY_STATE = "request_play_state";
    public static final String ACTION_PREVIOUS = "previous_song";
    // Song ID of track to start with
    public static final String PLAY_START_WITH = "start_with";

    // Property Keys
    public static final String SEEK_CHANGE = "change_seek";
    public static final String SONG_SEEK_VALUE = "song_seek_value";
    public static final String IS_PLAYING = "is_playing";
    public static final String EXTRA_IS_PLAYING = "is_playing";
    public static final String KEY_SHUFFLE = "shuffle";
    private static final String TAG = "PlayerServiceOld";
    private MediaPlayer player;
    private ArrayList<PlaylistManager.PlaylistItem> playlist = new ArrayList<>();
    private int currentPlaylistPosition;
    private int pausedSongSeek = 0;
    private LibraryManager libraryManager;
    private PlaylistManager playlistManager;
    private Map<String, ActionListenerContract> actions = new HashMap<>();

    private BroadcastReceiver playerReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(final Context context, final Intent intent) {
            libraryManager = new LibraryManager(context);
            ActionListenerContract action = actions.get(intent.getAction());
            if (action == null) {
                throw new IllegalStateException("Unknown callback for action: " + intent.getAction());
            }
            action.execute(intent, context);
        }
    };

    public PlayerService() {
        super();
        playlistManager = PlaylistManager.getInstance();
        actions.put(ACTION_PLAY_ALL, new PlayAllAction());
        actions.put(ACTION_VIEW_NOW_PLAYING, new ViewNowPlayingAction());
        actions.put(ACTION_SEEK_GET, new SeekGetAction());
        actions.put(ACTION_SEEK_TO, new SeekToAction());
        actions.put(ACTION_PAUSE, new PauseAction());
        actions.put(ACTION_PLAY_ALBUM, new PlayAlbumAction());
        actions.put(ACTION_NEXT, new NextSongAction());
        actions.put(ACTION_REQUEST_PLAY_STATE, new RequestStateAction());
        actions.put(ACTION_PREVIOUS, new PreviousSongAction());
    }


    private void updateCurrentPlaying() {
        Intent sendDetails = new Intent();
        Song song = getCurrentSong();
        sendDetails.putExtra(SONG_NAME, song.getName());
        sendDetails.putExtra(SONG_ARTIST, song.getArtist());
        sendDetails.putExtra(SONG_ALBUM_NAME, song.getAlbumName());
        sendDetails.putExtra(SONG_ALBUM_ID, song.getAlbumId());
        sendDetails.putExtra(SONG_PATH, song.getPath());
        sendDetails.putExtra(SONG_ART, song.getArt());

        try {
            sendDetails.putExtra(SONG_DURATION, player.getDuration());
            sendDetails.putExtra(SONG_CURRENT_TIME, player.getCurrentPosition());
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
            } else {
                if (playlist.size() > 0) {
                    player.start();
                    player.seekTo(pausedSongSeek);
                }
            }
            new RequestStateAction().execute(new Intent(), this);
        }
    }

    private void playMusic() {
        if (player == null) initPlayer();

        player.reset();
        Uri uri = Uri.parse(getCurrentSong().getPath());
        try {
            player.setDataSource(getApplicationContext(), uri);
        } catch (Exception e) {
            Log.e(TAG, "playMusic: Cannot set song Uri", e);
        }
        player.prepareAsync();
    }

    private Song getCurrentSong() {
        return playlist.get(currentPlaylistPosition).getSong();
    }

    private void playNext() {
        currentPlaylistPosition++;
        if (currentPlaylistPosition >= playlist.size()) {
            currentPlaylistPosition = 0;
        }
        pausedSongSeek = 0;
        playMusic();
    }

    private void playPrevious() {
        currentPlaylistPosition--;
        if (currentPlaylistPosition < 0) {
            currentPlaylistPosition = 0;
        }
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
        filter.addAction(ACTION_PLAY_ALL);
        filter.addAction(ACTION_PLAY_ALBUM);
        filter.addAction(ACTION_VIEW_NOW_PLAYING);
        filter.addAction(ACTION_PAUSE);
        filter.addAction(ACTION_SEEK_GET);
        filter.addAction(ACTION_SEEK_TO);
        filter.addAction(ACTION_NEXT);
        filter.addAction(ACTION_PREVIOUS);
        filter.addAction(ACTION_REQUEST_PLAY_STATE);
        registerReceiver(playerReceiver, filter);
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
    }

    @Override
    public void onCreate() {
        super.onCreate();
        initPlayer();
    }

    private void initPlayer() {
        player = new MediaPlayer();
        player.setOnCompletionListener(this);
        player.setOnPreparedListener(this);
        player.setWakeMode(getApplicationContext(), PowerManager.PARTIAL_WAKE_LOCK);
        player.setAudioStreamType(AudioManager.STREAM_MUSIC);
    }

    private boolean requestAudioFocus() {
        AudioManager audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        int result = audioManager.requestAudioFocus(this, AudioManager.STREAM_MUSIC,
                AudioManager.AUDIOFOCUS_GAIN);
        return result == AudioManager.AUDIOFOCUS_REQUEST_GRANTED;
    }

    @Override
    public void onAudioFocusChange(int focusChange) {
        switch (focusChange) {
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
                break;
            case AudioManager.AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK:
                if (player.isPlaying()) player.setVolume(0.1f, 0.1f);
                break;
        }
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
        }
    }

    @Override
    public boolean onError(MediaPlayer mp, int what, int extra) {
        return false;
    }

    private class PlayAllAction implements ActionListenerContract {
        @Override
        public void execute(final Intent intent, final Context context) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    playlist = playlistManager.generatePlaylist(libraryManager.getAllSongs(), intent.getLongExtra(PLAY_START_WITH, 0));
                    currentPlaylistPosition = playlistManager.getStartIndex();
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
                    playlist = playlistManager.generatePlaylist(libraryManager.getAlbumSongs(intent.getLongExtra(PlayerService.SONG_ALBUM_ID, 0)), intent.getLongExtra(PLAY_START_WITH, 0));
                    pausedSongSeek = 0;
                    currentPlaylistPosition = playlistManager.getStartIndex();
                    playMusic();
                }
            }).run();
        }
    }

    private class SeekToAction implements ActionListenerContract {
        @Override
        public void execute(Intent intent, Context context) {
            try {
                player.seekTo(intent.getIntExtra(SEEK_CHANGE, 0));
            } catch (NullPointerException e) {
                e.printStackTrace();
                pausedSongSeek = intent.getIntExtra(SEEK_CHANGE, 0);
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
                sIntent.putExtra(IS_PLAYING, true);
            }
            if (player != null) {
                sIntent.putExtra(SONG_SEEK_VALUE, player.getCurrentPosition());
            }
            sendBroadcast(sIntent);
            //TODO: updatePlaylist();
        }
    }

    private class NextSongAction implements ActionListenerContract {
        @Override
        public void execute(Intent intent, Context context) {
            playNext();
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
            if (playlist.size() > 0) {
                Intent i = new Intent(PlayerService.this, NowPlayingActivity.class);
                Song song = getCurrentSong();
                i.putExtra(SONG_NAME, song.getName());
                i.putExtra(SONG_ARTIST, song.getArtist());
                i.putExtra(SONG_ALBUM_NAME, song.getAlbumName());
                i.putExtra(SONG_ALBUM_ID, song.getAlbumId());
                i.putExtra(SONG_PATH, song.getPath());
                i.putExtra(SONG_ART, song.getArt());
                i.putExtra(SONG_ID, song.getId());
                i.putExtra(SONG_CURRENT_TIME, player.getCurrentPosition());
                i.putExtra(SONG_DURATION, player.getDuration());
                i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(i);
            }
        }
    }

    private class RequestStateAction implements ActionListenerContract {
        @Override
        public void execute(Intent intent, Context context) {
            intent = new Intent(NowPlayingActivity.ACTION_GET_PLAY_STATE);
            if (player != null && player.isPlaying()) {
                intent.putExtra(IS_PLAYING, true);
            }
            if (playlist.size() > 0) {
                Song song = getCurrentSong();
                intent.putExtra(SONG_NAME, song.getName());
                intent.putExtra(SONG_ARTIST, song.getArtist());
                intent.putExtra(SONG_ALBUM_NAME, song.getAlbumName());
                intent.putExtra(SONG_ALBUM_ID, song.getAlbumId());
                intent.putExtra(SONG_PATH, song.getPath());
                intent.putExtra(SONG_ART, song.getArt());

                try {
                    intent.putExtra(SONG_DURATION, player.getDuration());
                    intent.putExtra(SONG_CURRENT_TIME, player.getCurrentPosition());
                } catch (NullPointerException e) {
                    e.printStackTrace();
                }
            }
            sendBroadcast(intent);
        }
    }
}
