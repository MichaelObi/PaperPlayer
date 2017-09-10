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

package xyz.michaelobi.paperplayer.presentation.musiclibrary.fragment.songs;

import android.content.Context;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import xyz.michaelobi.paperplayer.R;
import xyz.michaelobi.paperplayer.data.model.Song;
import xyz.michaelobi.paperplayer.event.EventBus;
import xyz.michaelobi.paperplayer.injection.Injector;
import xyz.michaelobi.paperplayer.playback.events.action.PlayAllSongs;

import java.util.List;

/**
 * PaperPlayer Michael Obi 23 10 2016 11:02 AM
 */

public class SongsAdapter extends RecyclerView.Adapter<SongsAdapter.SongViewHolder> {
    private final Context context;
    private List<Song> songs;

    private EventBus bus;

    public SongsAdapter(List<Song> songs, Context context) {
        super();
        bus = Injector.provideEventBus();
        this.songs = songs;
        this.context = context;
    }


    @Override
    public SongViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_song, parent,
                false);
        return new SongViewHolder(v);
    }

    @Override
    public void onBindViewHolder(SongViewHolder holder, int position) {
        Song song = songs.get(position);
        holder.title.setText(song.getTitle());
        holder.artist.setText(song.getArtist());
        holder.menu.setOnClickListener(v -> {
            PopupMenu popup = new PopupMenu(context, v);
            MenuInflater inflater = popup.getMenuInflater();
            inflater.inflate(R.menu.song_actions, popup.getMenu());
            popup.show();
            popup.setOnMenuItemClickListener(menuItem -> {
                switch (menuItem.getItemId()) {
                    default:
                        Toast.makeText(context, "Not yet implemented.", Toast.LENGTH_SHORT).show();
                }
                return true;
            });
        });
    }

    @Override
    public int getItemCount() {
        if (songs == null) {
            return 0;
        }
        return songs.size();
    }

    public void setSongs(List<Song> songs) {
        this.songs = songs;
        notifyDataSetChanged();
    }



    class SongViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        final TextView title;
        final TextView artist;
        final ImageView menu;

        SongViewHolder(View itemView) {
            super(itemView);
            title = (TextView) itemView.findViewById(R.id.song_item_name);
            artist = (TextView) itemView.findViewById(R.id.song_item_desc);
            menu = (ImageView) itemView.findViewById(R.id.song_item_menu);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
           bus.post(new PlayAllSongs(songs.get(getAdapterPosition()).getId()));
        }
    }
}