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

package xyz.michaelobi.paperplayer.presentation.musiclibrary.fragment.songs

import android.support.v7.widget.PopupMenu
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import xyz.michaelobi.paperplayer.R
import xyz.michaelobi.paperplayer.data.model.Song
import xyz.michaelobi.paperplayer.event.RxBus
import xyz.michaelobi.paperplayer.injection.Injector
import xyz.michaelobi.paperplayer.playback.events.action.PlayAllSongs

/**
 * PaperPlayer Michael Obi 23 10 2016 11:02 AM
 */

class SongsAdapter(private val songs: MutableList<Song> = mutableListOf()) :
        RecyclerView.Adapter<SongsAdapter.SongViewHolder>() {

    private val bus: RxBus = Injector.provideEventBus()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SongViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.card_song, parent,
                false)
        return SongViewHolder(v)
    }

    override fun onBindViewHolder(holder: SongViewHolder, position: Int) {
        val (_, title, _, artist) = songs[position]
        holder.title.text = title
        holder.artist.text = artist
        holder.menu.setOnClickListener { v ->
            val popup = PopupMenu(v.context, v)
            val inflater = popup.menuInflater
            inflater.inflate(R.menu.song_actions, popup.menu)
            popup.show()
            popup.setOnMenuItemClickListener { _ ->
                Toast.makeText(v.context, "Not yet implemented.", Toast.LENGTH_SHORT).show()
                true
            }
        }
    }

    override fun getItemCount(): Int {
        return songs.size
    }

    fun setSongs(songs: List<Song>) {
        with(this.songs) {
            clear()
            addAll(songs)
        }
        notifyDataSetChanged()
    }


    inner class SongViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener {
        val title: TextView = itemView.findViewById(R.id.song_item_name)
        val artist: TextView = itemView.findViewById(R.id.song_item_desc)
        val menu: ImageView = itemView.findViewById(R.id.song_item_menu)

        init {
            itemView.setOnClickListener(this)
        }

        override fun onClick(view: View) {
            bus.post(PlayAllSongs(songs[adapterPosition].id))
        }
    }
}