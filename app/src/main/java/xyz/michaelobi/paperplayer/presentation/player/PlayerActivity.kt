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

package xyz.michaelobi.paperplayer.presentation.player

import android.databinding.DataBindingUtil
import android.graphics.Bitmap
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.SeekBar
import com.squareup.picasso.Picasso
import xyz.michaelobi.paperplayer.R
import xyz.michaelobi.paperplayer.databinding.PlayerActivityBinding
import xyz.michaelobi.paperplayer.injection.Injector
import xyz.michaelobi.paperplayer.playback.events.PlaybackState
import xyz.michaelobi.paperplayer.playback.events.action.Seek
import xyz.michaelobi.paperplayer.presentation.player.playlist.PlaylistFragment
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

        val toolbar = findViewById(R.id.toolbar) as Toolbar
        setSupportActionBar(toolbar)
        supportActionBar?.setHomeButtonEnabled(true)
        supportActionBar?.setDisplayShowTitleEnabled(false)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_player, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when {
            item.itemId == android.R.id.home -> {
                onBackPressed()
            }
            else -> when {
                item.itemId == R.id.action_playlist -> {
                    val playlistFragment = PlaylistFragment()
                    val args = Bundle()
                    playlistFragment.arguments = args
                    playlistFragment.show(supportFragmentManager, playlistFragment.tag)
                }
            }
        }
        return true
    }

    override fun onDestroy() {
        super.onDestroy()
        presenter?.detachView()
    }

    override fun updatePlaybackState(playbackState: PlaybackState) {
        updateSeeker(playbackState)
        updateCurrentTime(playbackState.currentDuration)
        if (playbackState.playing) viewBinding.playPause?.pause() else viewBinding.playPause?.play()
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
        viewBinding.songName?.text = song?.title
        viewBinding.albumArtist?.text = song?.artist
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
