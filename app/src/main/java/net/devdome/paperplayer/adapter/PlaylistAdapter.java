package net.devdome.paperplayer.adapter;


import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import net.devdome.paperplayer.R;
import net.devdome.paperplayer.playback.PlaylistManager;

import java.util.ArrayList;

public class PlaylistAdapter extends RecyclerView.Adapter<PlaylistAdapter.ViewHolder> {

    private ArrayList<PlaylistManager.PlaylistItem> playlist;

    public PlaylistAdapter() {
        PlaylistManager playlistManager = PlaylistManager.getInstance();
        playlist = playlistManager.getCurrentPlaylist();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_playlist_item, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.title.setText(playlist.get(position).getSong().getName());
        holder.artist.setText(playlist.get(position).getSong().getArtist());

    }

    @Override
    public int getItemCount() {
        return playlist.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        View itemView;
        TextView title, artist;

        public ViewHolder(View itemView) {
            super(itemView);
            this.itemView = itemView;
            title = (TextView) itemView.findViewById(R.id.song_item_title);
            artist = (TextView) itemView.findViewById(R.id.song_item_artist);
        }
    }
}
