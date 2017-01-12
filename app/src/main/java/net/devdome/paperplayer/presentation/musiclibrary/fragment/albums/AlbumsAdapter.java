package net.devdome.paperplayer.presentation.musiclibrary.fragment.albums;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import net.devdome.paperplayer.R;
import net.devdome.paperplayer.data.model.Album;
import net.devdome.paperplayer.presentation.album.AlbumActivity;

import java.util.List;

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


class AlbumsViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    final ImageView albumArt;
    final TextView name;
    final TextView artist;

    AlbumsViewHolder(View itemView) {
        super(itemView);
        itemView.setOnClickListener(this);
        albumArt = (ImageView) itemView.findViewById(R.id.album_art);
        name = (TextView) itemView.findViewById(R.id.album_name);
        artist = (TextView) itemView.findViewById(R.id.album_artist);
    }

    @Override
    public void onClick(View view) {
        Context c = view.getContext();
        c.startActivity(new Intent(c, AlbumActivity.class));
    }
}