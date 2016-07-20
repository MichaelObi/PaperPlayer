package net.devdome.paperplayer.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.util.Pair;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import net.devdome.paperplayer.R;
import net.devdome.paperplayer.data.model.Artist;
import net.devdome.paperplayer.ui.activity.AlbumActivity;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class ArtistsAdapter extends RecyclerView.Adapter<ArtistsAdapter.ArtistViewHolder> {

    public static final String EXTRA_ARTIST_ID = "artist_id";
    public static final String EXTRA_ARTIST_NAME = "artist_name";
    private static final String TAG = "Artists Adapter";
    private final List<Artist> artists;
    private final Context context;

    public ArtistsAdapter(Context context, ArrayList<Artist> artists) {
        this.context = context;
        this.artists = artists;
    }

    @Override
    public ArtistViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).
                inflate(R.layout.card_artist_list, parent, false);
        return new ArtistViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final ArtistViewHolder holder, final int position) {
        holder.name.setText(artists.get(position).getName());
        String strSongs = artists.get(position).getNumOfSongs() > 1 ? "songs" : "song";
        holder.contentCount.setText(String.format(Locale.getDefault(), "%d %s", artists.get(position).getNumOfSongs(), strSongs));
//        try {
//            Picasso.with(context).load(new File(artists.get(position).getArtString()))
//                    .error(R.drawable.default_artwork_dark)
//                    .config(Bitmap.Config.RGB_565)
//                    .resize(200, 200)
//                    .into(holder.artistImage);
//        } catch (Exception e) {
//            e.printStackTrace();
//            holder.albumArt.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.default_artwork_dark));
//        }
        holder.view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, AlbumActivity.class);
                intent.putExtra(EXTRA_ARTIST_ID, artists.get(holder.getAdapterPosition()).getId());
                intent.putExtra(EXTRA_ARTIST_NAME, artists.get(holder.getAdapterPosition()).getName());
                ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation((Activity) context, new Pair<View, String>(holder.view, context.getString(R.string.album_art)));
                ActivityCompat.startActivity((Activity) context, intent, options.toBundle());
            }
        });
    }

    @Override
    public int getItemCount() {
        return artists.size();
    }


    static class ArtistViewHolder extends RecyclerView.ViewHolder {
        TextView name;
        TextView contentCount;
        ImageView artistImage;
        View view;

        ArtistViewHolder(View itemView) {
            super(itemView);
            view = itemView;
            artistImage = (ImageView) itemView.findViewById(R.id.artist_img);
            name = (TextView) itemView.findViewById(R.id.artist_name);
            contentCount = (TextView) itemView.findViewById(R.id.artist_content_count);
        }
    }
}
