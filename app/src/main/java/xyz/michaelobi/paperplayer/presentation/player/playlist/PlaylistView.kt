package xyz.michaelobi.paperplayer.presentation.player.playlist

import xyz.michaelobi.paperplayer.mvp.DataView
import xyz.michaelobi.paperplayer.playback.queue.QueueItem

/**
 * PaperPlayer
 * Michael Obi
 * 30/04/2018, 9:32 PM
 */
interface PlaylistView : DataView {
    fun showList(items: MutableList<QueueItem>)
}