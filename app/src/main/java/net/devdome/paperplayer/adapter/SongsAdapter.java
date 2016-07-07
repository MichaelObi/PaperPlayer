package net.devdome.paperplayer.adapter;

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

import net.devdome.paperplayer.Constants;
import net.devdome.paperplayer.R;
import net.devdome.paperplayer.data.model.Song;

import java.util.ArrayList;
import java.util.List;

public class SongsAdapter extends RecyclerView.Adapter<SongsAdapter.SongsViewHolder> implements PopupMenu.OnMenuItemClickListener {

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
    public void onBindViewHolder(SongsViewHolder holder, final int position) {
        holder.title.setText(songs.get(position).getName());
        holder.artist.setText(songs.get(position).getArtist());
        holder.menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupMenu popup = new PopupMenu(context, v);
                MenuInflater inflater = popup.getMenuInflater();
                inflater.inflate(R.menu.song_actions, popup.getMenu());
                popup.show();
                popup.setOnMenuItemClickListener(SongsAdapter.this);
            }
        });
        holder.view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent a = new Intent();
                a.setAction(Constants.ACTION_PLAY_ALL);
                Log.e(TAG, "Song Id: " + songs.get(position).getId());
                a.putExtra(Constants.KEY_PLAY_START_WITH, songs.get(position).getId());
                context.sendBroadcast(a);
            }
        });
    }

    @Override
    public int getItemCount() {
        return songs.size();
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        return true;
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
