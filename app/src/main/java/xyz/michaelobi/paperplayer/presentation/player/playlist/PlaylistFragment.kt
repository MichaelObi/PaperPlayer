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
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.appcompat.widget.Toolbar
import android.view.View
import android.widget.Toast
import xyz.michaelobi.paperplayer.R
import xyz.michaelobi.paperplayer.injection.Injector
import xyz.michaelobi.paperplayer.playback.queue.QueueItem
import xyz.michaelobi.paperplayer.playback.queue.QueueManager


/**
 * PaperPlayer
 * Michael Obi
 * 22 05 2017 1:04 AM
 */
class PlaylistFragment : com.google.android.material.bottomsheet.BottomSheetDialogFragment(), PlaylistView {
    lateinit var playlistAdapter: PlaylistAdapter
    lateinit var rvPlaylist: androidx.recyclerview.widget.RecyclerView
    val queueManager: QueueManager = Injector.provideQueueManager()
    val presenter = PlaylistPresenter()

    override fun showList(items: MutableList<QueueItem>) {
        setUpPlaylist()
        playlistAdapter.setQueueItems(items)
    }

    override fun showLoading() {}

    override fun hideLoading() {}

    override fun showError(message: String) {
        Toast.makeText(activity, "An error occurred", Toast.LENGTH_SHORT).show()
    }

    override fun setupDialog(dialog: Dialog?, style: Int) {
        val view = View.inflate(activity, R.layout.bottomsheet_playlist, null)
        dialog?.setContentView(view)
        val toolbar = view.findViewById(R.id.toolbar) as Toolbar
        rvPlaylist = view.findViewById(R.id.rv_playlist) as androidx.recyclerview.widget.RecyclerView
        toolbar.setNavigationOnClickListener { dismiss() }
        toolbar.setTitle(R.string.playlist)
        val params = (view.parent as View).layoutParams as androidx.coordinatorlayout.widget.CoordinatorLayout.LayoutParams
        val behavior = params.behavior
        (behavior as com.google.android.material.bottomsheet.BottomSheetBehavior<*>).state = com.google.android.material.bottomsheet.BottomSheetBehavior.STATE_EXPANDED
        behavior.setBottomSheetCallback(object : com.google.android.material.bottomsheet.BottomSheetBehavior.BottomSheetCallback() {
            override fun onStateChanged(bottomSheet: View, newState: Int) {
                when (newState) {
                    com.google.android.material.bottomsheet.BottomSheetBehavior.STATE_COLLAPSED, com.google.android.material.bottomsheet.BottomSheetBehavior.STATE_HIDDEN -> {
                        dismiss()
                    }
                }
            }

            override fun onSlide(bottomSheet: View, slideOffset: Float) {}
        })
        presenter.attachView(this)
        presenter.getAll()
    }

    private fun setUpPlaylist() {
        playlistAdapter = PlaylistAdapter()
        val currentPlayingIndex = queueManager.currentIndex
        val linearLayoutManager = androidx.recyclerview.widget.LinearLayoutManager(activity)
        linearLayoutManager.scrollToPositionWithOffset(currentPlayingIndex, 20)

        with(rvPlaylist) {
            setHasFixedSize(true)
            layoutManager = linearLayoutManager
            adapter = playlistAdapter
        }
    }


}