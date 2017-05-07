package net.devdome.paperplayer.presentation.player

import android.util.Log
import net.devdome.paperplayer.data.MusicRepositoryInterface
import net.devdome.paperplayer.data.model.Album
import net.devdome.paperplayer.injection.Injector
import net.devdome.paperplayer.mvp.BasePresenter
import net.devdome.paperplayer.playback.events.PlaybackPaused
import net.devdome.paperplayer.playback.events.PlaybackStarted
import net.devdome.paperplayer.playback.events.PlaybackState
import net.devdome.paperplayer.playback.events.action.RequestPlaybackState
import net.devdome.paperplayer.playback.events.action.TogglePlayback
import net.devdome.paperplayer.presentation.musiclibrary.fragment.miniplayer.MiniPlayerPresenter
import rx.Subscriber

/**
 * PaperPlayer
 * Michael Obi
 * 07 05 2017 7:54 AM
 */

class PlayerPresenter(val musicRepository: MusicRepositoryInterface) : BasePresenter<PlayerContract.View>(), PlayerContract.Presenter {
    override fun playPauseToggle() {
        bus.post(TogglePlayback())
    }

    internal var bus = Injector.provideEventBus()

    override fun attachView(view: PlayerContract.View) {
        super.attachView(view)
        bus.observable(PlaybackPaused::class.java)?.subscribe { getView()?.updatePlayPauseButton(false) }
        bus.observable(PlaybackStarted::class.java)?.subscribe { getView()?.updatePlayPauseButton(true) }
        bus.observable(PlaybackState::class.java)?.subscribe {
            playbackState ->
            run {
                getView()?.updateTitleAndArtist(playbackState)
                getAlbumArtUri(playbackState.song.albumId)
            }
        }
        bus.post(RequestPlaybackState())
    }

    override fun detachView() {
        bus = null
        super.detachView()
    }

    fun getAlbumArtUri(albumId: Long) {
        addSubscription(musicRepository.getAlbum(albumId).subscribe(object : Subscriber<Album>() {
            override fun onCompleted() {}

            override fun onError(e: Throwable) {
                Log.e(MiniPlayerPresenter.TAG, e.localizedMessage, e)
                getView()?.updateSongArt(null)
            }

            override fun onNext(album: Album) {
                getView()?.updateSongArt(album.artPath)
            }
        }))
    }

}
