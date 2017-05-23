package xyz.michaelobi.paperplayer.presentation.player

import android.app.Dialog
import android.support.design.widget.BottomSheetBehavior
import android.support.design.widget.BottomSheetDialogFragment
import android.support.design.widget.CoordinatorLayout
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.Toolbar
import android.view.View
import xyz.michaelobi.paperplayer.R
import xyz.michaelobi.paperplayer.injection.Injector
import xyz.michaelobi.paperplayer.playback.queue.QueueManager


/**
 * PaperPlayer
 * Michael Obi
 * 22 05 2017 1:04 AM
 */
class PlaylistFragment : BottomSheetDialogFragment() {
    val queueManager: QueueManager = Injector.provideQueueManager()


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
        val toolbar = view.findViewById(R.id.toolbar) as Toolbar
        toolbar.title = getString(R.string.playlist)
        toolbar.setNavigationOnClickListener({ dismiss() })
        setUpPlaylist(view)
    }

    private fun setUpPlaylist(view: View) {
        val rvPlaylist = view.findViewById(R.id.rv_playlist) as RecyclerView
        val layoutManager = LinearLayoutManager(activity)
        val currentPlayingIndex = queueManager.currentIndex
        layoutManager.scrollToPositionWithOffset(currentPlayingIndex, 20)
        layoutManager.supportsPredictiveItemAnimations()
        rvPlaylist.layoutManager = layoutManager
        rvPlaylist.adapter = PlaylistAdapter()
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