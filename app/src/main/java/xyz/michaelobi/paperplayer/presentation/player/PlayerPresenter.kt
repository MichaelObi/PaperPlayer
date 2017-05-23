package xyz.michaelobi.paperplayer.presentation.player

import android.util.Log
import xyz.michaelobi.paperplayer.data.MusicRepositoryInterface
import xyz.michaelobi.paperplayer.data.model.Album
import xyz.michaelobi.paperplayer.injection.Injector
import xyz.michaelobi.paperplayer.mvp.BasePresenter
import xyz.michaelobi.paperplayer.playback.events.PlaybackState
import xyz.michaelobi.paperplayer.playback.events.action.NextSong
import xyz.michaelobi.paperplayer.playback.events.action.PreviousSong
import xyz.michaelobi.paperplayer.playback.events.action.RequestPlaybackState
import xyz.michaelobi.paperplayer.playback.events.action.TogglePlayback
import xyz.michaelobi.paperplayer.presentation.musiclibrary.fragment.miniplayer.MiniPlayerPresenter
import rx.Subscriber

/**
 * PaperPlayer
 * Michael Obi
 * 07 05 2017 7:54 AM
 */

class PlayerPresenter(val musicRepository: MusicRepositoryInterface) : BasePresenter<PlayerContract.View>(), PlayerContract.Presenter {

    internal var bus = Injector.provideEventBus()

    override fun attachView(view: PlayerContract.View) {
        super.attachView(view)
        bus.observable(PlaybackState::class.java)?.subscribe {
            playbackState ->
            run {
                getView()?.updatePlaybackState(playbackState)
                getView()?.updateTitleAndArtist(playbackState)
                getView()?.updateSeeker(playbackState)
                playbackState.song?.albumId?.let { getAlbumArtUri(it) }
            }
        }
        bus.post(RequestPlaybackState())
    }

    override fun detachView() {
        bus = null
        super.detachView()
    }

    override fun next() {
        bus.post(NextSong())
    }

    override fun previous() {
        bus.post(PreviousSong())
    }

    override fun playPauseToggle() {
        bus.post(TogglePlayback())
    }

    private fun getAlbumArtUri(albumId: Long) {
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
