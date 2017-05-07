package net.devdome.paperplayer.presentation.player

import android.databinding.DataBindingUtil
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.util.Log
import com.squareup.picasso.Picasso
import net.devdome.paperplayer.R
import net.devdome.paperplayer.databinding.PlayerActivityBinding
import net.devdome.paperplayer.injection.Injector
import net.devdome.paperplayer.playback.events.PlaybackState
import java.io.File


/**
 * PaperPlayer
 * Michael Obi
 * 06 05 2017 11:12 PM
 */

class PlayerActivity : AppCompatActivity(), PlayerContract.View {
    private val TAG: String = ".PlayerActivity"
    private var viewBinding: PlayerActivityBinding? = null
    private var presenter: PlayerContract.Presenter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewBinding = DataBindingUtil.setContentView(this, R.layout.player_activity)
        presenter = PlayerPresenter(Injector.provideMusicRepository(this))
        presenter?.attachView(this)
    }

    override fun onDestroy() {
        super.onDestroy()
        presenter?.detachView()
    }

    override fun updatePlayPauseButton(isPlaying: Boolean) {
        if (isPlaying) {
            viewBinding?.playPause?.pause()
        } else {
            viewBinding?.playPause?.play()
        }
    }

    override fun updateTitleAndArtist(playbackState: PlaybackState) {
        val song = playbackState.song
        viewBinding?.songName?.text = song.title
        viewBinding?.albumArtist?.text = song.artist
    }

    override fun updateSongArt(uri: String?) {
        try {
            Picasso.with(this).load(File(uri))
                    .error(R.drawable.default_artwork_dark)
                    .config(Bitmap.Config.ARGB_8888)
                    .into(viewBinding?.albumArt)
        } catch (e: Exception) {
            Log.e(TAG, e.message, e)
            viewBinding?.albumArt?.setImageDrawable(ContextCompat.getDrawable(
                    this, R.drawable.default_artwork_dark))
        }
    }
}
