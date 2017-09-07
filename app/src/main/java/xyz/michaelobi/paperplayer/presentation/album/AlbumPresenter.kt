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

package xyz.michaelobi.paperplayer.presentation.album

import android.util.Log
import rx.Scheduler
import rx.Subscriber
import xyz.michaelobi.paperplayer.data.MusicRepositoryInterface
import xyz.michaelobi.paperplayer.data.model.Album
import xyz.michaelobi.paperplayer.mvp.BasePresenter

/**
 * PaperPlayer
 * Michael Obi
 * 06 09 2017 9:49 PM
 */
class AlbumPresenter(private val musicRepository: MusicRepositoryInterface,
                     private val ioScheduler: Scheduler, private val mainScheduler: Scheduler) :
        BasePresenter<AlbumContract.View>(), AlbumContract.Presenter {

    override fun loadAlbum(albumId: Long) {

        addSubscription(musicRepository.getAlbum(albumId)
                .subscribeOn(ioScheduler)
                .observeOn(mainScheduler)
                .subscribe(
                        object : Subscriber<Album>() {
                            override fun onCompleted() {

                            }

                            override fun onError(e: Throwable) {
//                                view.hideLoading()
                                Log.e(TAG, e.localizedMessage, e)
                                e.message?.let {
                                    view.showError(it)
                                }
                            }

                            override fun onNext(album: Album) {
//                                view.hideLoading()
                                album.art?.let {
                                    view.showAlbumArt(it)
                                }

                            }
                        }
                )
        )
    }

    companion object {
        const val TAG = ".AlbumPresenter"
    }

}