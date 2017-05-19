package xyz.michaelobi.paperplayer.presentation.musiclibrary.fragment.miniplayer

import android.util.Log
import xyz.michaelobi.paperplayer.data.MusicRepositoryInterface
import xyz.michaelobi.paperplayer.data.model.Album
import xyz.michaelobi.paperplayer.event.EventBus
import xyz.michaelobi.paperplayer.injection.Injector
import xyz.michaelobi.paperplayer.mvp.BasePresenter
import xyz.michaelobi.paperplayer.playback.events.PlaybackPaused
import xyz.michaelobi.paperplayer.playback.events.PlaybackStarted
import xyz.michaelobi.paperplayer.playback.events.PlaybackState
import xyz.michaelobi.paperplayer.playback.events.action.RequestPlaybackState
import xyz.michaelobi.paperplayer.playback.events.action.TogglePlayback
import rx.Scheduler
import rx.Subscriber

/**
 * PaperPlayer
 * Michael Obi
 * 08 04 2017 10:50 AM
 */
class MiniPlayerPresenter(val musicRepository: MusicRepositoryInterface, val ioScheduler: Scheduler, val mainScheduler: Scheduler) : BasePresenter<MiniPlayerContract.View>(), MiniPlayerContract.Presenter {

    private var bus: EventBus = Injector.provideEventBus()

    override fun onPlayerStateUpdate() {
    }

    override fun initialize() {
        checkViewAttached()
        bus.observable(PlaybackPaused::class.java)?.subscribe { getView()?.updatePlayPauseButton(false) }
        bus.observable(PlaybackStarted::class.java)?.subscribe { getView()?.updatePlayPauseButton(true) }
        bus.observable(PlaybackState::class.java)?.subscribe {
            playbackState ->
            run {
                getView()?.updateTitleAndArtist(playbackState)
                playbackState.song?.albumId?.let { getAlbumArtUri(it) }
            }
        }
        bus.post(RequestPlaybackState())
    }

    fun getAlbumArtUri(albumId: Long) {

        addSubscription(musicRepository.getAlbum(albumId).subscribe(object : Subscriber<Album>() {
            override fun onCompleted() {}

            override fun onError(e: Throwable) {
                Log.e(Companion.TAG, e.localizedMessage, e)
                getView()?.updateSongArt(null)
            }

            override fun onNext(album: Album) {
                getView()?.updateSongArt(album.artPath)
            }
        }))
    }

    override fun onPlayButtonClicked() = bus.post(TogglePlayback())

    companion object {
        val TAG: String = ".MiniPlayerPresenter"
    }
}