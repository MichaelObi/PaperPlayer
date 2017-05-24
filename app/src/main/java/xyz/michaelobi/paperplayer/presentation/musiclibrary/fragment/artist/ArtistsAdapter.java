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

package xyz.michaelobi.paperplayer.presentation.musiclibrary.fragment.artist;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import xyz.michaelobi.paperplayer.R;
import xyz.michaelobi.paperplayer.data.model.Artist;

import java.util.List;
import java.util.Locale;

/**
 * PaperPlayer Michael Obi 21 01 2017 8:46 AM
 */
public class ArtistsAdapter extends RecyclerView.Adapter<ArtistsAdapter.ArtistViewHolder> {
    private final Context context;
    private List<Artist> artists;

    public ArtistsAdapter(Context context) {
        this.context = context;
    }

    @Override
    public ArtistsAdapter.ArtistViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(xyz.michaelobi.paperplayer.R.layout.card_artist_list, parent,
                false);
        return new ArtistViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ArtistsAdapter.ArtistViewHolder holder, int position) {
        holder.name.setText(artists.get(position).getName());
        holder.noOfRecords.setText(String.format(Locale.getDefault(), "%d songs, %d Albums", artists.get(position).getNumOfSongs(), artists.get(position).getNumOfAlbums()));
    }

    @Override
    public int getItemCount() {
        return artists.size();
    }

    public void setArtists(List<Artist> artists) {
        this.artists = artists;
    }

    public class ArtistViewHolder extends RecyclerView.ViewHolder {
        final TextView name;
        final TextView noOfRecords;
        final ImageView artistImg;

        public ArtistViewHolder(View itemView) {
            super(itemView);
            this.name = (TextView) itemView.findViewById(xyz.michaelobi.paperplayer.R.id.artist_name);
            this.artistImg = (ImageView) itemView.findViewById(xyz.michaelobi.paperplayer.R.id.artist_img);
            noOfRecords = (TextView) itemView.findViewById(xyz.michaelobi.paperplayer.R.id.artist_content_count);
        }
    }
}
