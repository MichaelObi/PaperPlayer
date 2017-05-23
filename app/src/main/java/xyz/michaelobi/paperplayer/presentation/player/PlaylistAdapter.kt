package xyz.michaelobi.paperplayer.presentation.player

import android.support.v7.widget.RecyclerView
import android.support.v7.widget.helper.ItemTouchHelper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import xyz.michaelobi.paperplayer.R
import xyz.michaelobi.paperplayer.injection.Injector
import xyz.michaelobi.paperplayer.playback.queue.QueueItem

/**
 * PaperPlayer
 * Michael Obi
 * 22 05 2017 11:19 PM
 */

class PlaylistAdapter : RecyclerView.Adapter<PlaylistAdapter.ViewHolder>() {

    internal var itemTouchHelper: ItemTouchHelper? = null
    private val queue: List<QueueItem>

    init {
        val queueManager = Injector.provideQueueManager()
        queue = queueManager.queue
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.card_playlist_item, parent, false)
        return ViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.title.text = queue[position].song.title
        holder.artist.text = queue[position].song.artist
    }

    override fun getItemCount(): Int {
        return queue.size
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var title: TextView = itemView.findViewById(R.id.song_item_title) as TextView
        var artist: TextView = itemView.findViewById(R.id.song_item_artist) as TextView
    }
}
