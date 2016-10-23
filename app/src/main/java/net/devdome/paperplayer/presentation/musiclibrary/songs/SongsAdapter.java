package net.devdome.paperplayer.presentation.musiclibrary.songs;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import net.devdome.paperplayer.R;
import net.devdome.paperplayer.data.model.Song;

import java.util.List;

/**
 * PaperPlayer
 * Michael Obi
 * 23 10 2016 11:02 AM
 */

class SongsAdapter extends RecyclerView.Adapter<SongViewHolder> {
    private static final String TAG = "SongsAdapter";
    private final Context context;
    private List<Song> songs;

    public SongsAdapter(List<Song> songs, Context context) {
        super();
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

    }

    @Override
    public int getItemCount() {
        if (songs == null) {
            return 0;
        }
        return songs.size();
    }

    void setSongs(List<Song> songs) {
        this.songs = songs;
        notifyDataSetChanged();
    }
}

class SongViewHolder extends RecyclerView.ViewHolder {
    final TextView title;
    final TextView artist;
//    final ImageView menu;

    public SongViewHolder(View itemView) {
        super(itemView);

        title = (TextView) itemView.findViewById(R.id.song_item_name);
        artist = (TextView) itemView.findViewById(R.id.song_item_desc);
//        menu = (ImageView) itemView.findViewById(R.id.song_item_menu);
    }
}
