package net.devdome.paperplayer_old.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.util.Pair;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import net.devdome.paperplayer_old.R;
import net.devdome.paperplayer_old.data.model.Album;
import net.devdome.paperplayer_old.ui.activity.AlbumActivity;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class AlbumsAdapter<A> extends RecyclerView.Adapter<AlbumsAdapter.AlbumViewHolder> {

    public static final String EXTRA_ALBUM_ID = "album_id";
    public static final String EXTRA_ALBUM_NAME = "album_name";
    private static final String TAG = "Albums Adapter";
    private final List<Album> albums;
    private final Context context;

    public AlbumsAdapter(Context context, ArrayList<Album> albums) {
        this.context = context;
        this.albums = albums;
    }

    @Override
    public AlbumViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).
                inflate(R.layout.card_album_grid, parent, false);
        return new AlbumViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final AlbumViewHolder holder, int position) {
        holder.name.setText(albums.get(position).getName());
        holder.artist.setText(albums.get(position).getArtist());
        try {
            Picasso.with(context).load(new File(albums.get(position).getArtString()))
                    .error(R.drawable.default_artwork_dark)
                    .config(Bitmap.Config.ARGB_8888)
                    .resize(200, 200)
                    .into(holder.albumArt);
        } catch (Exception e) {
            e.printStackTrace();
            holder.albumArt.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.default_artwork_dark));
        }
        holder.view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, AlbumActivity.class);
                intent.putExtra(EXTRA_ALBUM_ID, albums.get(holder.getAdapterPosition()).getId());
                intent.putExtra(EXTRA_ALBUM_NAME, albums.get(holder.getAdapterPosition()).getName());
                ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation((Activity) context, new Pair<>(holder.view, context.getString(R.string.album_art)));
                ActivityCompat.startActivity((Activity) context, intent, options.toBundle());
            }
        });
    }

    @Override
    public int getItemCount() {
        return albums.size();
    }


    public static class AlbumViewHolder extends RecyclerView.ViewHolder {
        TextView name;
        TextView artist;
        ImageView albumArt;
        View view, details;

        public AlbumViewHolder(View itemView) {
            super(itemView);
            view = itemView;
            albumArt = (ImageView) itemView.findViewById(R.id.album_art);
            name = (TextView) itemView.findViewById(R.id.album_name);
            artist = (TextView) itemView.findViewById(R.id.album_artist);
            details = itemView.findViewById(R.id.details_holder);
//            menu = (ImageView) itemView.findViewById(R.id.album_menu);

        }
    }
}
