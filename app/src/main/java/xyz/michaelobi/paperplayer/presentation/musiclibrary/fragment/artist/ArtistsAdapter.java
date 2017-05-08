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
