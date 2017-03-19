package net.devdome.paperplayer.presentation.musiclibrary.fragment.songs;

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

import net.devdome.paperplayer.R;
import net.devdome.paperplayer.data.model.Song;
import net.devdome.paperplayer.event.EventBus;
import net.devdome.paperplayer.injection.Injector;
import net.devdome.paperplayer.playback.events.action.PlayAllSongs;

import java.util.List;

/**
 * PaperPlayer Michael Obi 23 10 2016 11:02 AM
 */

class SongsAdapter extends RecyclerView.Adapter<SongsAdapter.SongViewHolder> {
    private final Context context;
    private List<Song> songs;

    private EventBus bus;

    SongsAdapter(List<Song> songs, Context context) {
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

    void setSongs(List<Song> songs) {
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