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

import android.util.Log
import rx.Subscriber
import xyz.michaelobi.paperplayer.data.MusicRepositoryInterface
import xyz.michaelobi.paperplayer.data.model.Album
import xyz.michaelobi.paperplayer.injection.Injector
import xyz.michaelobi.paperplayer.mvp.BasePresenter
import xyz.michaelobi.paperplayer.playback.events.PlaybackState
import xyz.michaelobi.paperplayer.playback.events.RepeatState
import xyz.michaelobi.paperplayer.playback.events.ShuffleState
import xyz.michaelobi.paperplayer.playback.events.action.*
import xyz.michaelobi.paperplayer.presentation.musiclibrary.fragment.miniplayer.MiniPlayerPresenter

/**
 * PaperPlayer
 * Michael Obi
 * 07 05 2017 7:54 AM
 */

class PlayerPresenter(val musicRepository: MusicRepositoryInterface) : BasePresenter<PlayerContract.View>(), PlayerContract.Presenter {

    internal var bus = Injector.provideEventBus()

    override fun attachView(view: PlayerContract.View) {
        super.attachView(view)
        bus.observe(PlaybackState::class.java).subscribe {
            playbackState ->
            run {
                getView().updatePlaybackState(playbackState)
                getView().updateTitleAndArtist(playbackState)
                getView().updateSeeker(playbackState)
                playbackState.song?.albumId?.let { getAlbumArtUri(it) }
            }
        }
        bus.observe(ShuffleState::class.java).subscribe {
            shuffleState ->
            run {
                getView()?.setShuffled(shuffleState.isShuffled)
            }
        }
        bus.observe(RepeatState::class.java).subscribe {
            repeatState ->
            run {
                getView().setRepeatState(repeatType = repeatState.repeatType)
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

    override fun toggleShuffle() {
        bus.post(ToggleShuffle())
    }

    override fun toggleRepeat() {
        bus.post(ToggleRepeat())
    }

    private fun getAlbumArtUri(albumId: Long) {
        addSubscription(musicRepository.getAlbum(albumId).subscribe(object : Subscriber<Album>() {
            override fun onCompleted() {}

            override fun onError(e: Throwable) {
                Log.e(MiniPlayerPresenter.TAG, e.localizedMessage, e)
                view.updateSongArt(null)
            }

            override fun onNext(album: Album) {
                getView()?.updateSongArt(album.artPath)
            }
        }))
    }
}
