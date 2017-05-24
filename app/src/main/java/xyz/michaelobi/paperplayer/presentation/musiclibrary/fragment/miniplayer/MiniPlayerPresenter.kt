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

import android.util.Log
import xyz.michaelobi.paperplayer.data.MusicRepositoryInterface
import xyz.michaelobi.paperplayer.data.model.Album
import xyz.michaelobi.paperplayer.event.EventBus
import xyz.michaelobi.paperplayer.injection.Injector
import xyz.michaelobi.paperplayer.mvp.BasePresenter
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
        bus.observable(PlaybackState::class.java)?.subscribe {
            playbackState ->
            run {
                getView()?.updatePlayPauseButton(playbackState.playing)
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