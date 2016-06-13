package net.devdome.paperplayer.ui.fragment;

import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import net.devdome.paperplayer.R;
import net.devdome.paperplayer.adapter.SongsAdapter;
import net.devdome.paperplayer.data.Mood;
import net.devdome.paperplayer.data.model.Song;

import java.util.ArrayList;

public class SongsFragment extends Fragment {

    Cursor musicCursor;
    View view;
    SongsAdapter adapter;
    RecyclerView rv;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_songs, container, false);
        view = v;

        rv = (RecyclerView) v.findViewById(R.id.rv_songs);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        rv.setLayoutManager(layoutManager);
        rv.setHasFixedSize(true);

        new GetSongs().execute();

        return v;
    }


    public class GetSongs extends AsyncTask<Void, Void, ArrayList> {

        @Override
        protected void onPostExecute(ArrayList songList) {
            super.onPostExecute(songList);
            if (songList != null) {
                adapter = new SongsAdapter(view.getContext(), songList);
                rv.setAdapter(adapter);
            }
        }

        @Override
        protected ArrayList doInBackground(Void... params) {
            final ArrayList<Song> songList = new ArrayList<>();

            final String where = MediaStore.Audio.Media.IS_MUSIC + "=1";
            final String orderBy = MediaStore.Audio.Media.TITLE;
            musicCursor = getActivity().getContentResolver().query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
                    null, where, null, orderBy);
            if (musicCursor != null && musicCursor.moveToFirst()) {
                int titleColumn = musicCursor.getColumnIndex
                        (android.provider.MediaStore.Audio.Media.TITLE);
                int idColumn = musicCursor.getColumnIndex
                        (android.provider.MediaStore.Audio.Media._ID);
                int artistColumn = musicCursor.getColumnIndex
                        (android.provider.MediaStore.Audio.Media.ARTIST);
                int pathColumn = musicCursor.getColumnIndex
                        (MediaStore.Audio.Media.DATA);
                int albumIdColumn = musicCursor.getColumnIndex
                        (MediaStore.Audio.Media.ALBUM_ID);
                int albumColumn = musicCursor.getColumnIndex
                        (MediaStore.Audio.Media.ALBUM);
                int i = 1;
                do {
                    songList.add(new Song(musicCursor.getLong(idColumn),
                            musicCursor.getString(titleColumn),
                            musicCursor.getString(artistColumn),
                            musicCursor.getString(pathColumn), false,
                            musicCursor.getLong(albumIdColumn),
                            musicCursor.getString(albumColumn), i, Mood.UNKNOWN));
                    i++;
                }
                while (musicCursor.moveToNext());
                return songList;
            }
            return null;
        }
    }
}
