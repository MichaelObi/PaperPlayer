package net.devdome.paperplayer.playback;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.IBinder;
import android.os.PowerManager;
import android.util.Log;

import net.devdome.paperplayer.data.MusicRepositoryInterface;
import net.devdome.paperplayer.event.EventBus;
import net.devdome.paperplayer.injection.Injector;
import net.devdome.paperplayer.playback.events.PlaybackPaused;
import net.devdome.paperplayer.playback.events.PlaybackStarted;
import net.devdome.paperplayer.playback.events.action.PausePlayback;
import net.devdome.paperplayer.playback.events.action.PlayAllSongs;
import net.devdome.paperplayer.playback.queue.QueueManager;

/**
 * PaperPlayer Michael Obi 11 01 2017 10:46 PM
 */
public class PlaybackService extends Service implements MediaPlayer.OnErrorListener, AudioManager
        .OnAudioFocusChangeListener, MediaPlayer.OnCompletionListener, MediaPlayer
        .OnPreparedListener {
    private static final String TAG = "PlaybackService";
    QueueManager queueManager;
    MusicRepositoryInterface musicRepository;
    private EventBus eventBus;
    private MediaPlayer player;
    private int songSeek = 0;

    public PlaybackService() {
        eventBus = Injector.provideEventBus();
        queueManager = Injector.provideQueueManager();
        musicRepository = Injector.provideMusicRepository(this);
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        registerEvents();
        player = new MediaPlayer();
        player.setOnCompletionListener(this);
        player.setOnPreparedListener(this);
        player.setOnErrorListener(this);
        player.setWakeMode(getApplicationContext(), PowerManager.PARTIAL_WAKE_LOCK);
        player.setAudioStreamType(AudioManager.STREAM_MUSIC);
        return START_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onAudioFocusChange(int focusChange) {
        switch (focusChange) {
            case AudioManager.AUDIOFOCUS_LOSS:
                stopMusic();
                break;
            case AudioManager.AUDIOFOCUS_GAIN:
                playMusic();
                player.setVolume(1.0f, 1.0f);
                break;
            case AudioManager.AUDIOFOCUS_LOSS_TRANSIENT:
                pauseMusic();
                break;
            case AudioManager.AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK:
                player.setVolume(0.2f, 0.2f);
                break;
            default:
        }
    }

    private void pauseMusic() {
        if (player != null) {
            if (player.isPlaying()) {
                songSeek = player.getCurrentPosition();
                player.pause();
            }
        }
        eventBus.post(new PlaybackPaused());
    }

    private boolean requestAudioFocus() {
        AudioManager am = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        int result = am.requestAudioFocus(this, AudioManager.STREAM_MUSIC, AudioManager.AUDIOFOCUS_GAIN);
        return result == AudioManager.AUDIOFOCUS_REQUEST_GRANTED;
    }

    private void abandonAudioFocus() {
        AudioManager audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        audioManager.abandonAudioFocus(this);
    }

    private void registerEvents() {
        eventBus.observable(PlayAllSongs.class)
                .subscribe(event -> musicRepository.getAllSongs()
                        .subscribe(songs -> {
                            stopMusic();
                            queueManager.setQueue(songs, event.getStartSongId());
                            playMusic();
                        }, error -> Log.e(TAG, error.getMessage()), () -> Log.d(TAG, "Get all songs complete")), error -> Log.e(TAG, error.getMessage()));
        eventBus.observable(PausePlayback.class)
                .subscribe(event -> pauseMusic());
    }

    private void stopMusic() {
        pauseMusic();
        songSeek = 0;
        abandonAudioFocus();
    }

    private void playMusic() {
        if (!player.isPlaying()) {
            player.reset();
            try {
                Uri uri = Uri.parse(queueManager.getCurrentSong().getSongUri());
                player.setDataSource(this, uri);
            } catch (Exception e) {
                Log.e(TAG, "playMusic: Cannot set song Uri", e);
            }
            player.prepareAsync();
        }
    }

    @Override
    public void onCompletion(MediaPlayer mediaPlayer) {
        Log.d(TAG, "Song Play complete");
        eventBus.post(new PlaybackPaused());
    }

    @Override
    public boolean onError(MediaPlayer mediaPlayer, int i, int i1) {
        return false;
    }

    @Override
    public void onPrepared(MediaPlayer mediaPlayer) {
        if (requestAudioFocus()) {
            player.start();
            player.seekTo(songSeek);
        }
        player.start();
        eventBus.post(new PlaybackStarted(queueManager.getCurrentSong()));
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        eventBus.cleanup();
        player.release();
    }
}