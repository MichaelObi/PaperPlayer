package net.devdome.paperplayer_old.adapter;


import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import net.devdome.paperplayer_old.R;
import net.devdome.paperplayer_old.playback.QueueManager;

import java.util.ArrayList;

public class PlaylistAdapter extends RecyclerView.Adapter<PlaylistAdapter.ViewHolder> {

    ItemTouchHelper itemTouchHelper;
    private ArrayList<QueueManager.QueueItem> queue;

    public PlaylistAdapter() {
        QueueManager queueManager = QueueManager.getInstance();
        queue = queueManager.getCurrentQueue();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_playlist_item, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.title.setText(queue.get(position).getSong().getName());
        holder.artist.setText(queue.get(position).getSong().getArtist());
    }

    @Override
    public int getItemCount() {
        return queue.size();
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
