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

package xyz.michaelobi.paperplayer.presentation.musiclibrary.fragment.albums;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

import xyz.michaelobi.paperplayer.R;
import xyz.michaelobi.paperplayer.data.model.Album;
import xyz.michaelobi.paperplayer.presentation.album.AlbumActivity;

/**
 * PaperPlayer
 * Michael Obi
 * 23 10 2016 4:01 PM
 */

class AlbumsAdapter extends RecyclerView.Adapter<AlbumsViewHolder> {
    private static final String TAG = "AlbumsAdapter";
    private final Context context;
    private List<Album> albums;


    AlbumsAdapter(List<Album> albums, Context context) {
        this.albums = albums;
        this.context = context;
    }

    @Override
    public AlbumsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.card_album_grid, parent, false);
        return new AlbumsViewHolder(v);
    }

    @Override
    public void onBindViewHolder(AlbumsViewHolder holder, int position) {
        holder.name.setText(albums.get(position).getTitle());
        holder.artist.setText(albums.get(position).getArtist());
        Log.i(TAG, albums.get(position).toString());
        Picasso.with(context).load(albums.get(position).getArt())
                .error(R.drawable.default_artwork_dark)
                .placeholder(R.drawable.default_artwork_dark)
                .config(Bitmap.Config.ARGB_8888)
                .resize(200, 200)
                .into(holder.albumArt);
        holder.itemView.setOnClickListener(v -> {
            Context c = v.getContext();
            Intent intent = new Intent(c, AlbumActivity.class);
            intent.putExtra(AlbumActivity.KEY_ALBUM_ID, albums.get(position).getId());
            c.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        if (albums == null) {
            return 0;
        }

        return this.albums.size();
    }

    public void setAlbums(List<Album> albums) {
        this.albums = albums;
        notifyDataSetChanged();
    }
}


class AlbumsViewHolder extends RecyclerView.ViewHolder {

    final ImageView albumArt;
    final TextView name;
    final TextView artist;

    AlbumsViewHolder(View itemView) {
        super(itemView);

        albumArt = (ImageView) itemView.findViewById(R.id.album_art);
        name = (TextView) itemView.findViewById(R.id.album_name);
        artist = (TextView) itemView.findViewById(R.id.album_artist);
    }
}