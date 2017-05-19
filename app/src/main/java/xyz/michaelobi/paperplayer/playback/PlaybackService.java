package xyz.michaelobi.paperplayer.playback;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.IBinder;
import android.os.PowerManager;
import android.util.Log;

import java.io.IOException;

import xyz.michaelobi.paperplayer.data.MusicRepositoryInterface;
import xyz.michaelobi.paperplayer.event.EventBus;
import xyz.michaelobi.paperplayer.injection.Injector;
import xyz.michaelobi.paperplayer.playback.events.PlaybackPaused;
import xyz.michaelobi.paperplayer.playback.events.PlaybackStarted;
import xyz.michaelobi.paperplayer.playback.events.PlaybackState;
import xyz.michaelobi.paperplayer.playback.events.action.NextSong;
import xyz.michaelobi.paperplayer.playback.events.action.PlayAllSongs;
import xyz.michaelobi.paperplayer.playback.events.action.PreviousSong;
import xyz.michaelobi.paperplayer.playback.events.action.RequestPlaybackState;
import xyz.michaelobi.paperplayer.playback.events.action.Seek;
import xyz.michaelobi.paperplayer.playback.events.action.TogglePlayback;
import xyz.michaelobi.paperplayer.playback.queue.QueueManager;

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

    // TODO: Remove all PlaybackStarted and PlaybackStopped  events in favor of PlaybackState

    public PlaybackService() {
        eventBus = Injector.provideEventBus();
        queueManager = Injector.provideQueueManager();
        musicRepository = Injector.provideMusicRepository(this);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        player = new MediaPlayer();
        player.setOnCompletionListener(this);
        player.setOnPreparedListener(this);
        player.setOnErrorListener(this);
        player.setWakeMode(getApplicationContext(), PowerManager.PARTIAL_WAKE_LOCK);
        player.setAudioStreamType(AudioManager.STREAM_MUSIC);
        registerEvents();
        musicRepository.getAllSongs().subscribe(songs -> queueManager.setQueue(songs, 0));
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        eventBus.post(new RequestPlaybackState());
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
            PlaybackState playbackState = new PlaybackState(queueManager.getCurrentSong(),
                    player.isPlaying(), player.getDuration(), player.getCurrentPosition());
            eventBus.post(new PlaybackPaused(playbackState));
        }
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

        eventBus.observable(TogglePlayback.class)
                .subscribe(event -> {
                    if (player.isPlaying()) {
                        pauseMusic();
                        return;
                    }
                    playMusic();
                });

        eventBus.observable(RequestPlaybackState.class)
                .subscribe(requestPlaybackState -> {
                    Log.d(TAG, "requestPlaybackState called");
                    PlaybackState playbackState = new PlaybackState(queueManager.getCurrentSong(),
                            player.isPlaying(), player.getDuration(), player.getCurrentPosition());
                    if (player.isPlaying()) {
                        eventBus.post(new PlaybackStarted(playbackState));
                        eventBus.post(playbackState);
                        return;
                    }
                    if (queueManager.hasSongs()) {
                        eventBus.post(new PlaybackPaused(playbackState));
                        eventBus.post(playbackState);
                    }
                });
        eventBus.observable(NextSong.class).subscribe(nextSong -> playNextSong());

        eventBus.observable(PreviousSong.class).subscribe(nextSong -> playPreviousSong());

        eventBus.observable(Seek.class).subscribe(seek -> {
            songSeek = seek.getSeekTo();
            player.seekTo(songSeek);
            eventBus.post(new RequestPlaybackState());
        }, e -> Log.e(TAG, e.getMessage()));
    }

    private void playPreviousSong() {
        pauseMusic();
        if (player.getCurrentPosition() > 3) {
            songSeek = 0;
            if (queueManager.previous() != null) {
                playMusic();
            }
        } else {
            songSeek = 0;
            player.seekTo(songSeek);
        }
        eventBus.post(RequestPlaybackState.class);
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
                if (queueManager.hasSongs()) {
                    Uri uri = Uri.parse(queueManager.getCurrentSong().getSongUri());
                    player.setDataSource(this, uri);
                    player.prepareAsync();
                }
            } catch (IOException e) {
                Log.e(TAG, e.getMessage(), e);
            }
        }
    }

    @Override
    public void onCompletion(MediaPlayer mediaPlayer) {
        Log.d(TAG, "Song Play complete");
        playNextSong();
    }

    private void playNextSong() {
        pauseMusic();
        if (queueManager.next() != null) {
            songSeek = 0;
            playMusic();
        }
        eventBus.post(RequestPlaybackState.class);
    }

    @Override
    public boolean onError(MediaPlayer mediaPlayer, int what, int extra) {
        return true;
    }

    @Override
    public void onPrepared(MediaPlayer mediaPlayer) {
        if (requestAudioFocus()) {
            mediaPlayer.seekTo(songSeek);
            mediaPlayer.start();
            PlaybackState playbackState = new PlaybackState(queueManager.getCurrentSong(),
                    mediaPlayer.isPlaying(), mediaPlayer.getDuration(), mediaPlayer.getCurrentPosition());
            eventBus.post(new PlaybackStarted(playbackState));
            eventBus.post(new RequestPlaybackState());
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        eventBus.cleanup();
        player.release();
    }
}