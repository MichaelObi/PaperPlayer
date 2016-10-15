package net.devdome.paperplayer_old.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import net.devdome.paperplayer_old.Constants;
import net.devdome.paperplayer_old.R;
import net.devdome.paperplayer_old.data.model.Song;
import net.devdome.paperplayer_old.playback.QueueManager;

import java.util.ArrayList;
import java.util.List;

public class SongsAdapter extends RecyclerView.Adapter<SongsAdapter.SongsViewHolder> {

    private static final String TAG = ".SongsAdapter";
    private final List<Song> songs;
    private final Context context;
    SongActionSelectedListener actionSelectedListener;

    public SongsAdapter(Context context, ArrayList<Song> songList) {
        this.context = context;
        songs = songList;
    }

    void setSongActionSelectedListener(SongActionSelectedListener listener) {
        this.actionSelectedListener = listener;
    }

    @Override
    public SongsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).
                inflate(R.layout.card_song, parent, false);
        return new SongsViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final SongsViewHolder holder, final int position) {
        holder.title.setText(songs.get(position).getName());
        holder.artist.setText(songs.get(position).getArtist());
        holder.menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupMenu popup = new PopupMenu(context, v);
                MenuInflater inflater = popup.getMenuInflater();
                inflater.inflate(R.menu.song_actions, popup.getMenu());
                popup.show();
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem menuItem) {
                        switch (menuItem.getItemId()) {
                            case R.id.action_play_next:
                                Song song = songs.get(holder.getAdapterPosition());
                                QueueManager queueManager = QueueManager.getInstance();
                                queueManager.setUpNext(song);
                                break;
                            default:
                                Toast.makeText(context, "Not yet implemented.", Toast.LENGTH_SHORT).show();
                        }
                        return true;
                    }
                });
            }
        });
        holder.view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent a = new Intent();
                a.setAction(Constants.ACTION_PLAY_ALL);
                Log.e(TAG, "Song Id: " + songs.get(holder.getAdapterPosition()).getId());
                a.putExtra(Constants.KEY_PLAY_START_WITH, songs.get(holder.getAdapterPosition()).getId());
                context.sendBroadcast(a);
            }
        });
    }

    @Override
    public int getItemCount() {
        return songs.size();
    }


    public interface SongActionSelectedListener {
        void onActionSelected();
    }

    public static class SongsViewHolder extends RecyclerView.ViewHolder {

        TextView title;
        TextView artist;
        ImageView menu;
        View view;

        public SongsViewHolder(View itemView) {
            super(itemView);
            view = itemView;
            title = (TextView) itemView.findViewById(R.id.song_item_name);
            artist = (TextView) itemView.findViewById(R.id.song_item_desc);
            menu = (ImageView) itemView.findViewById(R.id.song_item_menu);

        }
    }
}
