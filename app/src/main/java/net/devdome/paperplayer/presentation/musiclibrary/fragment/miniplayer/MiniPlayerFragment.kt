package net.devdome.paperplayer.presentation.musiclibrary.fragment.miniplayer

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
import net.devdome.paperplayer.R
import net.devdome.paperplayer.event.EventBus
import net.devdome.paperplayer.injection.Injector
import net.devdome.paperplayer.playback.events.PlaybackState
import net.devdome.paperplayer.playback.events.action.TogglePlayback
import net.devdome.paperplayer.widget.PlayPauseButton
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers


/**
 * PaperPlayer
 * Michael Obi
 * 08 04 2017 10:34 AM
 */
class MiniPlayerFragment : Fragment(), MiniPlayerContract.View, View.OnClickListener {

    var bus: EventBus? = Injector.provideEventBus()
    var presenter: MiniPlayerPresenter? = null
    var miniPlayerSongName: TextView? = null
    var miniPlayerSongArtist: TextView? = null
    var miniAlbumArt: ImageView? = null
    var playPauseButton: PlayPauseButton? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        presenter = MiniPlayerPresenter(Injector.provideMusicRepository(activity), Schedulers.io(),
                AndroidSchedulers.mainThread())

    }

    override fun onPause() {
        super.onPause()
        presenter?.detachView()
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view: View = inflater!!.inflate(R.layout.mini_player, container, false)
        presenter?.attachView(this)
        miniPlayerSongName = view.findViewById(R.id.song_name_mini) as TextView?
        miniPlayerSongArtist = view.findViewById(R.id.song_artist_mini) as TextView?
        miniAlbumArt = view.findViewById(R.id.album_art_mini) as ImageView?
        playPauseButton = view.findViewById(R.id.play_pause) as PlayPauseButton?
        playPauseButton?.setOnClickListener(this)
        return view
    }

    override fun onResume() {
        super.onResume()
        presenter?.attachView(this)
        presenter?.initialize()
    }

    override fun onDestroy() {
        super.onDestroy()
        bus = null
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.play_pause -> bus?.post(TogglePlayback())
            else -> {
                TODO("Not Implemented - Now Playing")
            }
        }
    }

    override fun updateSongArt(uri: String?) {
        if (uri.isNullOrEmpty()) {
            val defaultAlbumArt: Drawable = ContextCompat.getDrawable(activity, R.drawable.default_artwork_dark)
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
        miniPlayerSongName?.text = song.title
        miniPlayerSongArtist?.text = song.artist
    }

    override fun updatePlayPauseButton(isPlaying: Boolean) {
        if (isPlaying) {
            playPauseButton?.pause()
        } else {
            playPauseButton?.play()
        }
    }

}