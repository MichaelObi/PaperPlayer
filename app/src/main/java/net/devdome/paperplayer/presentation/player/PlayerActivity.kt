package net.devdome.paperplayer.presentation.player

import android.databinding.DataBindingUtil
import android.graphics.Bitmap
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.widget.SeekBar
import com.squareup.picasso.Picasso
import net.devdome.paperplayer.R
import net.devdome.paperplayer.databinding.PlayerActivityBinding
import net.devdome.paperplayer.injection.Injector
import net.devdome.paperplayer.playback.events.PlaybackState
import net.devdome.paperplayer.playback.events.action.Seek
import java.io.File
import java.util.*


/**
 * PaperPlayer
 * Michael Obi
 * 06 05 2017 11:12 PM
 */

class PlayerActivity : AppCompatActivity(), PlayerContract.View, SeekBar.OnSeekBarChangeListener {
    private val TAG: String = ".PlayerActivity"
    private lateinit var viewBinding: PlayerActivityBinding
    private var presenter: PlayerContract.Presenter? = null
    private var timer: Timer? = null
    private val eventBus = Injector.provideEventBus()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewBinding = DataBindingUtil.setContentView(this, R.layout.player_activity)
        presenter = PlayerPresenter(Injector.provideMusicRepository(this))
        presenter?.attachView(this)
        viewBinding.presenter = presenter
        viewBinding.playerSeekbar.setOnSeekBarChangeListener(this)
    }

    override fun onDestroy() {
        super.onDestroy()
        presenter?.detachView()
    }

    override fun updatePlaybackState(playbackState: PlaybackState) {
        updateSeeker(playbackState)
        updateCurrentTime(playbackState.currentDuration)
        if (playbackState.playing) {
            viewBinding.playPause?.pause()
        } else {
            viewBinding.playPause?.play()
        }
    }


    override fun updateSeeker(playbackState: PlaybackState) {
        viewBinding.playerSeekbar?.max = playbackState.songDuration
        viewBinding.playerSeekbar?.progress = playbackState.currentDuration
        val strDuration = String.format("%s:%s", String.format("%02d", playbackState.songDuration / 1000 / 60), String
                .format("%02d", playbackState.songDuration / 1000 % 60))
        viewBinding.duration?.text = strDuration
        timer?.cancel()
        timer = Timer()
        timer?.schedule(object : TimerTask() {
            override fun run() {
                runOnUiThread {
                    if (playbackState.playing) {
                        val progress = viewBinding.playerSeekbar?.progress
                        if (progress != null) {
                            if (progress < playbackState.songDuration) {
                                viewBinding.playerSeekbar?.progress = progress.plus(100)
                            } else {
                                viewBinding.playerSeekbar?.progress = 100
                            }
                            updateCurrentTime(progress)
                        }
                    }
                }
            }
        }, 0, 100)
    }

    private fun updateCurrentTime(progress: Int) {
        val min = String.format("%02d", progress / 1000 / 60)
        val sec = String.format("%02d", progress / 1000 % 60)
        viewBinding.currentTime?.text = String.format("%s:%s", min, sec)
    }

    override fun updateTitleAndArtist(playbackState: PlaybackState) {
        val song = playbackState.song
        viewBinding.songName?.text = song.title
        viewBinding.albumArtist?.text = song.artist
    }

    override fun updateSongArt(uri: String?) {
        try {
            Picasso.with(this).load(File(uri))
                    .error(R.drawable.default_artwork_dark)
                    .config(Bitmap.Config.ARGB_8888)
                    .into(viewBinding.albumArt)
        } catch (e: Exception) {
            Log.e(TAG, e.message, e)
            viewBinding.albumArt?.setImageDrawable(ContextCompat.getDrawable(
                    this, R.drawable.default_artwork_dark))
        }
    }

    override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
        if (fromUser) {
            eventBus.post(Seek(progress))
        } else {
            if (progress == seekBar?.max) {
                seekBar.progress = 100
            }
        }
    }

    override fun onStartTrackingTouch(seekBar: SeekBar?) {}

    override fun onStopTrackingTouch(seekBar: SeekBar?) {}
}
