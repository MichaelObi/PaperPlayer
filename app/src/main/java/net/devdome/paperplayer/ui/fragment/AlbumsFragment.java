package net.devdome.paperplayer.ui.fragment;

import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import net.devdome.paperplayer.R;
import net.devdome.paperplayer.SpacesItemDecoration;
import net.devdome.paperplayer.adapter.AlbumsAdapter;
import net.devdome.paperplayer.data.model.Album;

import java.util.ArrayList;

public class AlbumsFragment extends Fragment {

    Cursor musicCursor;
    View view;
    AlbumsAdapter adapter;
    RecyclerView rv;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_albums, container, false);
        view = v;

        rv = (RecyclerView) v.findViewById(R.id.rv_albums);
        GridLayoutManager layoutManager = new GridLayoutManager(getActivity(), 2);
        rv.setLayoutManager(layoutManager);
        rv.setHasFixedSize(true);
        rv.addItemDecoration(new SpacesItemDecoration(2, 2));
        new GetAlbums().execute();

        return v;
    }


    private class GetAlbums extends AsyncTask<Void, Void, ArrayList> {

        @Override
        protected void onPostExecute(ArrayList albumList) {
            super.onPostExecute(albumList);
            if (albumList != null) {
                adapter = new AlbumsAdapter(view.getContext(), albumList);
                rv.setAdapter(adapter);
            }
        }

        @Override
        protected ArrayList doInBackground(Void... params) {
            final ArrayList<Album> albums = new ArrayList<>();

            final String orderBy = MediaStore.Audio.Albums.ALBUM;
            musicCursor = getActivity().getContentResolver().query(MediaStore.Audio.Albums.EXTERNAL_CONTENT_URI,
                    null, null, null, orderBy);
            if (musicCursor != null && musicCursor.moveToFirst()) {
                int titleColumn = musicCursor.getColumnIndex
                        (MediaStore.Audio.Albums.ALBUM);
                int idColumn = musicCursor.getColumnIndex
                        (android.provider.MediaStore.Audio.Albums._ID);
                int artistColumn = musicCursor.getColumnIndex
                        (android.provider.MediaStore.Audio.Albums.ARTIST);
                int numOfSongsColumn = musicCursor.getColumnIndex
                        (android.provider.MediaStore.Audio.Albums.NUMBER_OF_SONGS);
                int albumArtColumn = musicCursor.getColumnIndex
                        (android.provider.MediaStore.Audio.Albums.ALBUM_ART);

                do {
                    albums.add(new Album(musicCursor.getLong(idColumn),
                            musicCursor.getString(titleColumn),
                            musicCursor.getString(artistColumn),
                            false, musicCursor.getString(albumArtColumn),
                            musicCursor.getInt(numOfSongsColumn)));
                }
                while (musicCursor.moveToNext());
                return albums;
            }
            return null;
        }
    }
}
