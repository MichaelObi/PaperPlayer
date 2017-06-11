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

package xyz.michaelobi.paperplayer.presentation.player.playlist

import android.util.Log
import rx.Subscriber
import rx.android.schedulers.AndroidSchedulers
import xyz.michaelobi.paperplayer.injection.Injector
import xyz.michaelobi.paperplayer.mvp.BasePresenter
import xyz.michaelobi.paperplayer.mvp.ListViewContract
import xyz.michaelobi.paperplayer.playback.queue.QueueItem
import xyz.michaelobi.paperplayer.playback.queue.QueueManager

/**
 * PaperPlayer
 * Michael Obi
 * 29 05 2017 10:54 AM
 */
class PlaylistPresenter : BasePresenter<ListViewContract.View<Any>>(), ListViewContract.Presenter {

    val queueManager: QueueManager = Injector.provideQueueManager()

    override fun getAll() {
        checkViewAttached()
        getView()!!.showLoading()
        addSubscription(queueManager.queue.subscribeOn(AndroidSchedulers.mainThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : Subscriber<List<QueueItem>>() {
                    override fun onCompleted() {}

                    override fun onError(e: Throwable) {
                        getView()!!.hideLoading()
                        Log.e(TAG, e.localizedMessage, e)
                        getView()?.showError(e.message)
                    }

                    override fun onNext(queueItems: List<QueueItem>) {
                        getView()?.hideLoading()
                        getView()?.showList(queueItems)
                    }
                }))
    }

    companion object {
        val TAG = ".PlaylistPresenter"
    }

}