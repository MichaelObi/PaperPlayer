package net.devdome.paperplayer.presentation.musiclibrary.fragment.albums;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import net.devdome.paperplayer.data.model.Album;

import java.util.List;

/**
 * PaperPlayer
 * Michael Obi
 * 23 10 2016 4:01 PM
 */

class AlbumsAdapter extends RecyclerView.Adapter<AlbumsViewHolder> {
    private final Context context;
    private List<Album> albumList;

    public AlbumsAdapter(List<Album> albumList, Context context) {

        this.albumList = albumList;
        this.context = context;
    }

    @Override
    public AlbumsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(AlbumsViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        if (albumList == null) {
            return 0;
        }

        return this.albumList.size();
    }

    public void setAlbumList(List<Album> albumList) {
        this.albumList = albumList;
    }
}


class AlbumsViewHolder extends RecyclerView.ViewHolder {

    public AlbumsViewHolder(View itemView) {
        super(itemView);
    }
}