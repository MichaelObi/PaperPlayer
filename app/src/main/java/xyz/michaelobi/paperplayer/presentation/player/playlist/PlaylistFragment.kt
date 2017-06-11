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

import android.app.Dialog
import android.support.design.widget.BottomSheetBehavior
import android.support.design.widget.BottomSheetDialogFragment
import android.support.design.widget.CoordinatorLayout
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import android.widget.Toast
import kotlinx.android.synthetic.main.bottomsheet_playlist.*
import xyz.michaelobi.paperplayer.R
import xyz.michaelobi.paperplayer.injection.Injector
import xyz.michaelobi.paperplayer.mvp.ListViewContract
import xyz.michaelobi.paperplayer.playback.queue.QueueItem
import xyz.michaelobi.paperplayer.playback.queue.QueueManager


/**
 * PaperPlayer
 * Michael Obi
 * 22 05 2017 1:04 AM
 */
class PlaylistFragment : BottomSheetDialogFragment(), ListViewContract.View<QueueItem> {
    lateinit var playlistAdapter: PlaylistAdapter
    val queueManager: QueueManager = Injector.provideQueueManager()
    val presenter: ListViewContract.Presenter = PlaylistPresenter()

    override fun showList(items: MutableList<QueueItem>) {
        playlistAdapter = PlaylistAdapter(items)
        activity.rv_playlist.adapter = playlistAdapter
    }

    override fun showLoading() {}

    override fun hideLoading() {}

    override fun showError(message: String?) {
        Toast.makeText(activity, "An error occurred", Toast.LENGTH_SHORT).show()
    }

    override fun setupDialog(dialog: Dialog?, style: Int) {
        super.setupDialog(dialog, style)
        val view = View.inflate(activity, R.layout.bottomsheet_playlist, null)
        dialog?.setContentView(view)
        val params = (view.parent as View).layoutParams as CoordinatorLayout.LayoutParams
        val behavior = params.behavior
        if (behavior != null) {
            (behavior as BottomSheetBehavior<*>).state = BottomSheetBehavior.STATE_EXPANDED
            behavior.setBottomSheetCallback(bottomSheetBehaviorCallback)
        }
        activity.toolbar.title = getString(R.string.playlist)
        activity.toolbar.setNavigationOnClickListener { dismiss() }
        setUpPlaylist()
        presenter.attachView(this)
        presenter.getAll()
    }

    private fun setUpPlaylist() {
        val currentPlayingIndex = queueManager.currentIndex
        val linearLayoutManager = LinearLayoutManager(activity)
        linearLayoutManager.scrollToPositionWithOffset(currentPlayingIndex, 20)
        with(activity.rv_playlist) {
            setHasFixedSize(true)
            layoutManager = linearLayoutManager
        }
    }

    private val bottomSheetBehaviorCallback = object : BottomSheetBehavior.BottomSheetCallback() {
        override fun onStateChanged(bottomSheet: View, newState: Int) {
            if (newState == BottomSheetBehavior.STATE_COLLAPSED) {
                dismiss()
            }
        }

        override fun onSlide(bottomSheet: View, slideOffset: Float) {}
    }
}