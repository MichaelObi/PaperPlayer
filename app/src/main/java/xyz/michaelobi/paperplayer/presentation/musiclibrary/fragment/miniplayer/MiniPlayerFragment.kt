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

package xyz.michaelobi.paperplayer.presentation.musiclibrary.fragment.miniplayer

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import xyz.michaelobi.paperplayer.R
import xyz.michaelobi.paperplayer.event.RxBus
import xyz.michaelobi.paperplayer.injection.Injector
import xyz.michaelobi.paperplayer.playback.events.PlaybackState
import xyz.michaelobi.paperplayer.playback.events.action.TogglePlayback
import xyz.michaelobi.paperplayer.presentation.player.PlayerActivity
import xyz.michaelobi.paperplayer.widget.PlayPauseButton

/**
 * PaperPlayer
 * Michael Obi
 * 08 04 2017 10:34 AM
 */
class MiniPlayerFragment : Fragment(), MiniPlayerContract.View, View.OnClickListener {

    lateinit var presenter: MiniPlayerPresenter
    var miniPlayerSongName: TextView? = null
    var miniPlayerSongArtist: TextView? = null
    var miniAlbumArt: ImageView? = null
    var playPauseButton: PlayPauseButton? = null
    var miniPlayer: View? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view: View = inflater.inflate(R.layout.mini_player, container, false)
        presenter = MiniPlayerPresenter(Injector.provideMusicRepository(activity))
        presenter.attachView(this)
        miniPlayerSongName = view.findViewById(R.id.song_name_mini) as TextView?
        miniPlayer = view.findViewById(R.id.mini_player)
        miniPlayerSongArtist = view.findViewById(R.id.song_artist_mini) as TextView?
        miniAlbumArt = view.findViewById(R.id.album_art_mini) as ImageView?
        playPauseButton = view.findViewById(R.id.play_pause) as PlayPauseButton?
        playPauseButton?.setOnClickListener(this)
        miniPlayer?.setOnClickListener(this)
        return view
    }

    override fun onResume() {
        super.onResume()
        presenter.initialize()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        presenter.detachView()
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.play_pause -> presenter.onPlayButtonClicked()
            else -> {
                val intent = Intent(activity, PlayerActivity::class.java)
                startActivity(intent)
            }
        }
    }

    override fun updateSongArt(uri: String?) {
        if (uri.isNullOrEmpty()) {
            val defaultAlbumArt: Drawable? = ContextCompat.getDrawable(requireActivity(), R.drawable
                    .default_artwork_dark)
            miniAlbumArt?.setImageDrawable(defaultAlbumArt)
            return
        }
        val options = BitmapFactory.Options()
        options.inJustDecodeBounds = false
        options.inPreferredConfig = Bitmap.Config.ARGB_8888
        options.inDither = true
        val artWork = BitmapDrawable(resources, BitmapFactory.decodeFile(uri, options))
        miniAlbumArt?.setImageDrawable(artWork)
    }

    override fun updateTitleAndArtist(playbackState: PlaybackState) {
        val song = playbackState.song
        miniPlayerSongName?.text = song?.title
        miniPlayerSongArtist?.text = song?.artist
    }

    override fun updatePlayPauseButton(isPlaying: Boolean) {
        if (isPlaying) playPauseButton?.pause() else playPauseButton?.play()
    }
}