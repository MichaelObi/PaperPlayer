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
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.widget.Toast;

import net.devdome.paperplayer.data.model.Song;
import net.devdome.paperplayer.ui.activity.NowPlayingActivity;

import java.util.ArrayList;

public class PlayerService extends Service implements AudioManager.OnAudioFocusChangeListener, MediaPlayer.OnCompletionListener, MediaPlayer.OnPreparedListener {

    // Song Detail Keys
    public static final String SONG_ID = "song_id";
    public static final String SONG_PATH = "song_path";
    public static final String SONG_NAME = "song_name";
    public static final String SONG_ALBUM_NAME = "song_album_name";
    public static final String SONG_ART = "song_art";
    public static final String SONG_ALBUM_ID = "song_album_id";
    public static final String SONG_ARTIST = "song_artist";
    public static final String SONG_DURATION = "song_duration";
    public static final String SONG_CURRENT_TIME = "song_current_time";


    public static final int NOTIFICATION_ID = 107;
    public static final String ACTION_PAUSE = "pause";
    public static final String ACTION_NEXT = "next_song";
    public static final String ACTION_SEEK_TO = "player_seek_to_song";
    public static final String ACTION_PLAY_ALL = "play_all";
    public static final String ACTION_VIEW_NOW_PLAYING = "view_now_playing";
    public static final String PLAY_START_WITH = "start_with";
    public static final String SEEK_CHANGE = "change_seek";
    public static final String SONG_SEEK_VALUE = "song_seek_value";
    public static final String IS_PLAYING = "is_playing";
    public static final String ACTION_SEEK_GET = "action_get_seek";
    public static final String ACTION_PLAY_ALBUM = "play_album";
    private static final String TAG = "PlayerService";
    LocalBroadcastManager broadcastManager;
    MediaPlayer player;
    ArrayList<Song> songs = new ArrayList<>();
    int songPosition;
    int pausedSongSeek = 0;
    LibraryManager libraryManager;
    BroadcastReceiver playerReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(final Context context, final Intent intent) {
            libraryManager = new LibraryManager(context);
            String action = intent.getAction();
            switch (action) {
                case ACTION_PLAY_ALL:
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            songs = libraryManager.getAllSongs();
                            songPosition = intent.getIntExtra(PLAY_START_WITH, 0);
                            pausedSongSeek = 0;
                            playMusic();
                        }
                    }).run();
                    break;
                case ACTION_VIEW_NOW_PLAYING:

                    if (songs.size() < 1) {
                        Toast.makeText(context, "Nothing's playing right now.", Toast.LENGTH_SHORT).show();
                        break;
                    }
                    Intent i = new Intent(PlayerService.this, NowPlayingActivity.class);
                    i.putExtra(SONG_NAME, songs.get(songPosition).getName());
                    i.putExtra(SONG_ARTIST, songs.get(songPosition).getArtist());
                    i.putExtra(SONG_ALBUM_NAME, songs.get(songPosition).getAlbumName());
                    i.putExtra(SONG_ALBUM_ID, songs.get(songPosition).getAlbumId());
                    i.putExtra(SONG_PATH, songs.get(songPosition).getPath());
                    i.putExtra(SONG_ART, songs.get(songPosition).getArt());
                    i.putExtra(SONG_ID, songs.get(songPosition).getId());
                    i.putExtra(SONG_CURRENT_TIME, player.getCurrentPosition());
                    i.putExtra(SONG_DURATION, player.getDuration());


                    i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(i);

                    break;
                case ACTION_SEEK_GET:
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
                    break;
                case ACTION_PAUSE:
                    pause();
                    break;
                case ACTION_SEEK_TO:
                    try {
                        player.seekTo(intent.getIntExtra(SEEK_CHANGE, 0));
                    } catch (NullPointerException e) {
                        e.printStackTrace();
                        pausedSongSeek = intent.getIntExtra(SEEK_CHANGE, 0);
                        playMusic();
                        pausedSongSeek = 0;
                    }
                    break;
                case ACTION_PLAY_ALBUM:
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            songs = libraryManager.getAlbumSongs(intent.getLongExtra(PlayerService.SONG_ALBUM_ID, 0));
                            songPosition = intent.getIntExtra(PLAY_START_WITH, 0);
                            pausedSongSeek = 0;
                            playMusic();
                        }
                    }).run();
                    break;
                default:
            }
        }
    };

    public void updateCurrentPlaying() {
        Intent sendDetails = new Intent();
        sendDetails.putExtra(SONG_NAME, songs.get(songPosition).getName());
        sendDetails.putExtra(SONG_ARTIST, songs.get(songPosition).getArtist());
        sendDetails.putExtra(SONG_ALBUM_NAME, songs.get(songPosition).getAlbumName());
        sendDetails.putExtra(SONG_ALBUM_ID, songs.get(songPosition).getAlbumId());
        sendDetails.putExtra(SONG_PATH, songs.get(songPosition).getPath());
        sendDetails.putExtra(SONG_ART, songs.get(songPosition).getArt());

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
                player.start();
                player.seekTo(pausedSongSeek);
            }
            Intent i = new Intent(NowPlayingActivity.ACTION_GET_PLAY_STATE);
            if (player != null && player.isPlaying())
                i.putExtra(IS_PLAYING, true);
            sendBroadcast(i);
        }
    }

    private void playMusic() {
        player.reset();
        Uri uri = Uri.parse(songs.get(songPosition).getPath());
        try {
            player.setDataSource(getApplicationContext(), uri);
        } catch (Exception e) {
            Log.e(TAG, "playMusic: Cannot set song Uri", e);
        }
        player.prepareAsync();
    }

    private void playNext() {
        songPosition++;
        if (songPosition >= songs.size()) {
            songPosition = 0;
        }
        pausedSongSeek = 0;
        playMusic();
    }

    private void stopMusic() {
        if (player != null) {
            player.stop();
//            setNotificationPlayer(true);
        }
    }

    public void release() {
        player.release();
        player = null;
    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        broadcastManager = LocalBroadcastManager.getInstance(this);

        IntentFilter filter = new IntentFilter();
        filter.addAction(ACTION_PLAY_ALL);
        filter.addAction(ACTION_PLAY_ALBUM);
        filter.addAction(ACTION_VIEW_NOW_PLAYING);
        filter.addAction(ACTION_PAUSE);
        filter.addAction(ACTION_SEEK_GET);
        filter.addAction(ACTION_SEEK_TO);

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
        release();
        super.onDestroy();
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
        if (result != AudioManager.AUDIOFOCUS_REQUEST_GRANTED) {
            return false;
        }
        return true;

    }

    @Override
    public void onAudioFocusChange(int focusChange) {
        switch (focusChange) {
            case AudioManager.AUDIOFOCUS_GAIN:
//                playMusic();
                break;
            case AudioManager.AUDIOFOCUS_LOSS_TRANSIENT:
                pause();
                break;
            case AudioManager.AUDIOFOCUS_LOSS:
                pause();
                release();
                break;

        }
    }

    @Override
    public void onCompletion(MediaPlayer mp) {
        if (mp.getCurrentPosition() > 0) {
            playNext();
            updateCurrentPlaying();
        }
    }

    @Override
    public void onPrepared(MediaPlayer mp) {
        mp.start();
        mp.seekTo(pausedSongSeek);
    }
}
