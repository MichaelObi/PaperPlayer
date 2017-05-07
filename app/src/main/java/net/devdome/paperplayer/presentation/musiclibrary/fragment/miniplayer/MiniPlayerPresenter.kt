package net.devdome.paperplayer.presentation.musiclibrary.fragment.miniplayer

import android.util.Log
import net.devdome.paperplayer.data.MusicRepositoryInterface
import net.devdome.paperplayer.data.model.Album
import net.devdome.paperplayer.event.EventBus
import net.devdome.paperplayer.injection.Injector
import net.devdome.paperplayer.mvp.BasePresenter
import net.devdome.paperplayer.playback.events.PlaybackPaused
import net.devdome.paperplayer.playback.events.PlaybackStarted
import net.devdome.paperplayer.playback.events.PlaybackState
import net.devdome.paperplayer.playback.events.action.RequestPlaybackState
import net.devdome.paperplayer.playback.events.action.TogglePlayback
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
                getAlbumArtUri(playbackState.song.albumId)
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