package net.devdome.paperplayer_old.ui.fragment;

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

import net.devdome.paperplayer_old.R;
import net.devdome.paperplayer_old.adapter.ArtistsAdapter;
import net.devdome.paperplayer_old.data.model.Artist;

import java.util.ArrayList;

public class ArtistsFragment extends Fragment {

    private View view;
    private RecyclerView rv;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_artists, container, false);
        view = v;

        rv = (RecyclerView) v.findViewById(R.id.rv_artists);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        rv.setLayoutManager(layoutManager);
        rv.setHasFixedSize(true);
        new GetArtists().execute();

        return v;
    }


    private class GetArtists extends AsyncTask<Void, Void, ArrayList<Artist>> {

        @Override
        protected void onPostExecute(ArrayList<Artist> artistList) {
            super.onPostExecute(artistList);
            if (artistList != null) {
                ArtistsAdapter adapter = new ArtistsAdapter(view.getContext(), artistList);
                rv.setAdapter(adapter);
            }
        }

        @Override
        protected ArrayList<Artist> doInBackground(Void... params) {
            final ArrayList<Artist> artists = new ArrayList<>();

            final String orderBy = MediaStore.Audio.Artists.ARTIST;
            Cursor musicCursor = getActivity().getContentResolver().query(MediaStore.Audio.Artists.EXTERNAL_CONTENT_URI,
                    null, null, null, orderBy);
            if (musicCursor != null && musicCursor.moveToFirst()) {
                int idColumn = musicCursor.getColumnIndex
                        (MediaStore.Audio.Artists._ID);
                int artistColumn = musicCursor.getColumnIndex
                        (MediaStore.Audio.Artists.ARTIST);
                int numOfSongsColumn = musicCursor.getColumnIndex
                        (MediaStore.Audio.Artists.NUMBER_OF_TRACKS);
                int numOfAlbumsColumn = musicCursor.getColumnIndex
                        (MediaStore.Audio.Artists.NUMBER_OF_ALBUMS);
                int artistKeyColumn = musicCursor.getColumnIndex
                        (MediaStore.Audio.Artists.ARTIST_KEY);

                do {
                    artists.add(new Artist(musicCursor.getLong(idColumn),
                            musicCursor.getString(artistColumn),
                            musicCursor.getInt(numOfAlbumsColumn),
                            musicCursor.getInt(numOfSongsColumn),
                            musicCursor.getString(artistKeyColumn)));
                }
                while (musicCursor.moveToNext());
                musicCursor.close();
                return artists;
            }
            return null;
        }
    }
}
