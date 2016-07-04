package net.devdome.paperplayer.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import net.devdome.paperplayer.R;
import net.devdome.paperplayer.data.model.Song;
import net.devdome.paperplayer.playback.PlayerService;
import net.devdome.paperplayer.playback.PlayerServiceOld;

import java.util.ArrayList;
import java.util.List;

public class SongsAdapter extends RecyclerView.Adapter<SongsAdapter.SongsViewHolder> {

    private static final String TAG = ".SongsAdapter";
    private final List<Song> songs;
    private final Context context;

    public SongsAdapter(Context context, ArrayList<Song> songList) {
        this.context = context;
        songs = songList;
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
        holder.view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent a = new Intent();
                a.setAction(PlayerService.ACTION_PLAY_ALL);
                Log.e(TAG, "Song Id: " + songs.get(position).getId());
                a.putExtra(PlayerServiceOld.PLAY_START_WITH, songs.get(position).getId());
                context.sendBroadcast(a);
            }
        });
    }

    @Override
    public int getItemCount() {
        return songs.size();
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
