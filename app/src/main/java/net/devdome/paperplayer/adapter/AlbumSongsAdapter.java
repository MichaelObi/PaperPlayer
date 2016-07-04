package net.devdome.paperplayer.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import net.devdome.paperplayer.R;
import net.devdome.paperplayer.data.model.Song;
import net.devdome.paperplayer.playback.PlayerService;

import java.util.ArrayList;
import java.util.List;

public class AlbumSongsAdapter extends RecyclerView.Adapter<AlbumSongsAdapter.SongsViewHolder> {

    private final List<Song> songs;
    private final Context context;


    public AlbumSongsAdapter(Context context, ArrayList<Song> songList) {
        this.context = context;
        songs = songList;
    }

    @Override
    public SongsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).
                inflate(R.layout.list_item_album_songs, parent, false);
        return new SongsViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(SongsViewHolder holder, final int position) {
        holder.title.setText(songs.get(position).getName());
        holder.artist.setText(songs.get(position).getArtist());
        holder.track.setText(String.valueOf(songs.get(position).getTrackNumber()));

        holder.view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent a = new Intent();
                a.setAction(PlayerService.ACTION_PLAY_ALBUM);
                a.putExtra(PlayerService.SONG_ALBUM_ID, songs.get(position).getAlbumId());
                a.putExtra(PlayerService.PLAY_START_WITH, songs.get(position).getId());
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
        TextView track;

        public SongsViewHolder(View itemView) {
            super(itemView);
            view = itemView;
            track = (TextView) itemView.findViewById(R.id.album_song_track);
            title = (TextView) itemView.findViewById(R.id.album_song_item_name);
            artist = (TextView) itemView.findViewById(R.id.album_song_item_artist);
            menu = (ImageView) itemView.findViewById(R.id.album_song_item_menu);
        }
    }
}
