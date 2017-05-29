/*
 * MIT License
 *
 * Copyright (c) 2017 MIchael Obi
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package xyz.michaelobi.paperplayer.playback

import android.app.Service
import android.content.Context
import android.content.Intent
import android.media.AudioManager
import android.media.MediaPlayer
import android.net.Uri
import android.os.IBinder
import android.os.PowerManager
import android.util.Log

import java.io.IOException

import xyz.michaelobi.paperplayer.data.MusicRepositoryInterface
import xyz.michaelobi.paperplayer.event.EventBus
import xyz.michaelobi.paperplayer.injection.Injector
import xyz.michaelobi.paperplayer.playback.events.PlaybackState
import xyz.michaelobi.paperplayer.playback.events.action.NextSong
import xyz.michaelobi.paperplayer.playback.events.action.PlayAllSongs
import xyz.michaelobi.paperplayer.playback.events.action.PreviousSong
import xyz.michaelobi.paperplayer.playback.events.action.RequestPlaybackState
import xyz.michaelobi.paperplayer.playback.events.action.Seek
import xyz.michaelobi.paperplayer.playback.events.action.TogglePlayback
import xyz.michaelobi.paperplayer.playback.queue.QueueManager

/**
 * PaperPlayer Michael Obi 11 01 2017 10:46 PM
 */
class PlaybackService : Service(), MediaPlayer.OnErrorListener, AudioManager.OnAudioFocusChangeListener, MediaPlayer.OnCompletionListener, MediaPlayer.OnPreparedListener {
    internal var queueManager: QueueManager
    internal var musicRepository: MusicRepositoryInterface
    private val eventBus: EventBus
    private var player: MediaPlayer? = null
    private var songSeek = 0

    init {
        eventBus = Injector.provideEventBus()
        queueManager = Injector.provideQueueManager()
        musicRepository = Injector.provideMusicRepository(this)
    }

    override fun onCreate() {
        super.onCreate()
        player = MediaPlayer()
        player!!.setOnCompletionListener(this)
        player!!.setOnPreparedListener(this)
        player!!.setOnErrorListener(this)
        player!!.setWakeMode(applicationContext, PowerManager.PARTIAL_WAKE_LOCK)
        player!!.setAudioStreamType(AudioManager.STREAM_MUSIC)
        registerEvents()
        musicRepository.allSongs.subscribe { songs -> queueManager.setQueue(songs, 0) }
    }

    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {
        eventBus.post(RequestPlaybackState())
        return Service.START_STICKY
    }

    override fun onBind(intent: Intent): IBinder? {
        return null
    }

    override fun onAudioFocusChange(focusChange: Int) {
        when (focusChange) {
            AudioManager.AUDIOFOCUS_LOSS -> stopMusic()
            AudioManager.AUDIOFOCUS_GAIN -> {
                playMusic()
                player!!.setVolume(1.0f, 1.0f)
            }
            AudioManager.AUDIOFOCUS_LOSS_TRANSIENT -> pauseMusic()
            AudioManager.AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK -> player!!.setVolume(0.2f, 0.2f)
        }
    }

    private fun pauseMusic() {
        if (player != null) {
            if (player!!.isPlaying) {
                songSeek = player!!.currentPosition
                player!!.pause()
            }
            val playbackState = PlaybackState(queueManager.currentSong,
                    player!!.isPlaying, player!!.duration, player!!.currentPosition)
            eventBus.post(playbackState)
        }
    }

    private fun requestAudioFocus(): Boolean {
        val am = getSystemService(Context.AUDIO_SERVICE) as AudioManager
        val result = am.requestAudioFocus(this, AudioManager.STREAM_MUSIC, AudioManager.AUDIOFOCUS_GAIN)
        return result == AudioManager.AUDIOFOCUS_REQUEST_GRANTED
    }

    private fun abandonAudioFocus() {
        val audioManager = getSystemService(Context.AUDIO_SERVICE) as AudioManager
        audioManager.abandonAudioFocus(this)
    }

    private fun registerEvents() {
        eventBus.observable(PlayAllSongs::class.java)
                .subscribe({ event ->
                    musicRepository.allSongs
                            .subscribe({ songs ->
                                stopMusic()
                                queueManager.setQueue(songs, event.getStartSongId())
                                playMusic()
                            }, { error -> Log.e(TAG, error.message) }) { Log.d(TAG, "Get all songs complete") }
                }) { error -> Log.e(TAG, error.message) }

        eventBus.observable(TogglePlayback::class.java)
                .subscribe { event ->
                    if (player!!.isPlaying) {
                        pauseMusic()
                        return@eventBus.observable(TogglePlayback.class)
                                .subscribe
                    }
                    playMusic()
                }

        eventBus.observable(RequestPlaybackState::class.java)
                .subscribe { requestPlaybackState ->
                    Log.d(TAG, "requestPlaybackState called")
                    val duration = if (player!!.isPlaying) player!!.duration else 0
                    val playbackState = PlaybackState(queueManager.currentSong,
                            player!!.isPlaying, duration, player!!.currentPosition)
                    if (queueManager.hasSongs()) {
                        eventBus.post(playbackState)
                    }
                }
        eventBus.observable(NextSong::class.java).subscribe { nextSong -> playNextSong() }

        eventBus.observable(PreviousSong::class.java).subscribe { nextSong -> playPreviousSong() }

        eventBus.observable(Seek::class.java).subscribe({ seek ->
            songSeek = seek.seekTo
            player!!.seekTo(songSeek)
            eventBus.post(RequestPlaybackState())
        }) { e -> Log.e(TAG, e.message) }
    }

    private fun playPreviousSong() {
        pauseMusic()
        if (player!!.currentPosition > 3) {
            songSeek = 0
            if (queueManager.previous() != null) {
                playMusic()
            }
        } else {
            songSeek = 0
            player!!.seekTo(songSeek)
        }
        eventBus.post(RequestPlaybackState::class.java)
    }

    private fun stopMusic() {
        pauseMusic()
        songSeek = 0
        abandonAudioFocus()
    }

    private fun playMusic() {
        if (!player!!.isPlaying) {
            player!!.reset()
            try {
                if (queueManager.hasSongs()) {
                    val uri = Uri.parse(queueManager.currentSong.songUri)
                    player!!.setDataSource(this, uri)
                    player!!.prepareAsync()
                }
            } catch (e: IOException) {
                Log.e(TAG, e.message, e)
            }

        }
    }

    override fun onCompletion(mediaPlayer: MediaPlayer) {
        Log.d(TAG, "Song Play complete")
        playNextSong()
    }

    private fun playNextSong() {
        pauseMusic()
        if (queueManager.next() != null) {
            songSeek = 0
            playMusic()
        }
        eventBus.post(RequestPlaybackState::class.java)
    }

    override fun onError(mediaPlayer: MediaPlayer, what: Int, extra: Int): Boolean {
        return true
    }

    override fun onPrepared(mediaPlayer: MediaPlayer) {
        if (requestAudioFocus()) {
            mediaPlayer.seekTo(songSeek)
            mediaPlayer.start()
            val playbackState = PlaybackState(queueManager.currentSong,
                    mediaPlayer.isPlaying, mediaPlayer.duration, mediaPlayer.currentPosition)
            eventBus.post(playbackState) //TODO: Check the implication of this
            eventBus.post(RequestPlaybackState())
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        eventBus.cleanup()
        player!!.release()
    }

    companion object {
        private val TAG = "PlaybackService"
    }
}