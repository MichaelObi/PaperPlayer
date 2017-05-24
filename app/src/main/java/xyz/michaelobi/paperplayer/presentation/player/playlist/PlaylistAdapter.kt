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

import android.support.v7.widget.RecyclerView
import android.support.v7.widget.helper.ItemTouchHelper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import xyz.michaelobi.paperplayer.R
import xyz.michaelobi.paperplayer.injection.Injector
import xyz.michaelobi.paperplayer.playback.queue.QueueItem

/**
 * PaperPlayer
 * Michael Obi
 * 22 05 2017 11:19 PM
 */

class PlaylistAdapter : android.support.v7.widget.RecyclerView.Adapter<PlaylistAdapter.ViewHolder>() {

    internal var itemTouchHelper: android.support.v7.widget.helper.ItemTouchHelper? = null
    private val queue: List<xyz.michaelobi.paperplayer.playback.queue.QueueItem>

    init {
        val queueManager = xyz.michaelobi.paperplayer.injection.Injector.provideQueueManager()
        queue = queueManager.queue
    }

    override fun onCreateViewHolder(parent: android.view.ViewGroup, viewType: Int): xyz.michaelobi.paperplayer.presentation.player.playlist.PlaylistAdapter.ViewHolder {
        val itemView = android.view.LayoutInflater.from(parent.context).inflate(xyz.michaelobi.paperplayer.R.layout.card_playlist_item, parent, false)
        return xyz.michaelobi.paperplayer.presentation.player.playlist.PlaylistAdapter.ViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: xyz.michaelobi.paperplayer.presentation.player.playlist.PlaylistAdapter.ViewHolder, position: Int) {
        holder.title.text = queue[position].song.title
        holder.artist.text = queue[position].song.artist
    }

    override fun getItemCount(): Int {
        return queue.size
    }

    class ViewHolder(itemView: android.view.View) : android.support.v7.widget.RecyclerView.ViewHolder(itemView) {
        var title: android.widget.TextView = itemView.findViewById(xyz.michaelobi.paperplayer.R.id.song_item_title) as android.widget.TextView
        var artist: android.widget.TextView = itemView.findViewById(xyz.michaelobi.paperplayer.R.id.song_item_artist) as android.widget.TextView
        var art: android.widget.ImageView = itemView.findViewById(xyz.michaelobi.paperplayer.R.id.album_art) as android.widget.ImageView
    }
}
