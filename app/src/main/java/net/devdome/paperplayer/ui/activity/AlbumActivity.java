package net.devdome.paperplayer.ui.activity;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.transition.Fade;
import android.transition.Transition;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import net.devdome.paperplayer.R;
import net.devdome.paperplayer.adapter.AlbumSongsAdapter;
import net.devdome.paperplayer.adapter.AlbumsAdapter;
import net.devdome.paperplayer.data.Mood;
import net.devdome.paperplayer.data.model.Song;
import net.devdome.paperplayer.playback.PlayerServiceOld;

import java.io.File;
import java.util.ArrayList;

public class AlbumActivity extends AppCompatActivity {


    private String artPath;
    private Cursor musicCursor;
    private ImageView artwork;
    private TextView tvYear;
    private FloatingActionButton fab;
    private RecyclerView rvSongs;
    private AlbumSongsAdapter adapter;
    private long albumId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_album);
        overridePendingTransition(0, 0);
        albumId = getIntent().getLongExtra(AlbumsAdapter.EXTRA_ALBUM_ID, 0);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_close_24dp);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setTitle(getIntent().getStringExtra(AlbumsAdapter.EXTRA_ALBUM_NAME));

        rvSongs = (RecyclerView) findViewById(R.id.rv_songs);
        rvSongs.setLayoutManager(new LinearLayoutManager(this));

        artwork = (ImageView) findViewById(R.id.album_art);
        tvYear = (TextView) findViewById(R.id.year);
        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                playAlbum(albumId, 0);
            }
        });

        controlEnterAnimation();
        initAlbum();
    }

    private void playAlbum(long albumId, long startWith) {
        Intent i = new Intent(PlayerServiceOld.ACTION_PLAY_ALBUM);
        i.putExtra(PlayerServiceOld.SONG_ALBUM_ID, albumId);
        i.putExtra(PlayerServiceOld.PLAY_START_WITH, startWith);
        sendBroadcast(i);
    }

    private void initAlbum() {
        final String where = MediaStore.Audio.Albums._ID + "=?";
        musicCursor = getContentResolver().query(MediaStore.Audio.Albums.EXTERNAL_CONTENT_URI, new String[]{MediaStore.Audio.Albums._ID,
                MediaStore.Audio.Albums.ALBUM_ART, MediaStore.Audio.Albums.LAST_YEAR}, where, new String[]{String.valueOf(albumId)}, null);

        if (musicCursor != null) {
            if (musicCursor.moveToFirst()) {
                artPath = musicCursor.getString(musicCursor.getColumnIndex(MediaStore.Audio.Albums.ALBUM_ART));
                int year = musicCursor.getInt(musicCursor.getColumnIndex(MediaStore.Audio.Albums.LAST_YEAR));
                tvYear.setText(Integer.toString(year));
                try {
                    Picasso.with(this).load(new File(artPath))
                            .config(Bitmap.Config.RGB_565)
                            .error(R.drawable.default_artwork_dark)
                            .into(artwork);
                } catch (NullPointerException e) {
                    Picasso.with(this).load(R.drawable.default_artwork_dark).into(artwork);
                }
            }
            musicCursor.close();
        }
        new GetSongs().execute();
    }


    private void controlExitAnimation() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setExitTransition(new Fade());

            getWindow().getExitTransition().addListener(new Transition.TransitionListener() {
                @Override
                public void onTransitionStart(Transition transition) {
                    fab.hide();
                }

                @Override
                public void onTransitionEnd(Transition transition) {

                }

                @Override
                public void onTransitionCancel(Transition transition) {

                }

                @Override
                public void onTransitionPause(Transition transition) {

                }

                @Override
                public void onTransitionResume(Transition transition) {

                }
            });

        } else {
            fab.hide();
        }
    }

    private void controlEnterAnimation() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setEnterTransition(new Fade());
        }


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().getEnterTransition().addListener(new Transition.TransitionListener() {
                @Override
                public void onTransitionStart(Transition transition) {

                }

                @Override
                public void onTransitionEnd(Transition transition) {
                    fab.show();
                }

                @Override
                public void onTransitionCancel(Transition transition) {

                }

                @Override
                public void onTransitionPause(Transition transition) {

                }

                @Override
                public void onTransitionResume(Transition transition) {

                }
            });

        } else {
            fab.show();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            fab.hide(new FloatingActionButton.OnVisibilityChangedListener() {
                @Override
                public void onHidden(FloatingActionButton fab) {
                    super.onHidden(fab);
                    onBackPressed();
                }
            });
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        controlExitAnimation();
    }

    private class GetSongs extends AsyncTask<Void, Void, ArrayList<Song>> {

        @Override
        protected void onPostExecute(ArrayList<Song> songList) {
            super.onPostExecute(songList);
            if (songList != null) {
                adapter = new AlbumSongsAdapter(AlbumActivity.this, songList);
                rvSongs.setAdapter(adapter);
            }
        }

        @Override
        protected ArrayList<Song> doInBackground(Void... params) {
            final ArrayList<Song> songList = new ArrayList<>();

            final String where = MediaStore.Audio.Media.ALBUM_ID + "=?";
            final String orderBy = MediaStore.Audio.Media._ID;
            musicCursor = getContentResolver().query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
                    null, where, new String[]{String.valueOf(albumId)}, orderBy);
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
                int trackNumberColumn = musicCursor.getColumnIndex
                        (MediaStore.Audio.Media.TRACK);
                int i = 1;

                do {
                    Song song = new Song(musicCursor.getLong(idColumn),
                            musicCursor.getString(titleColumn),
                            musicCursor.getString(artistColumn),
                            musicCursor.getString(pathColumn), false,
                            musicCursor.getLong(albumIdColumn),
                            musicCursor.getString(albumColumn), i, Mood.UNKNOWN);
                    song.setTrackNumber(musicCursor.getLong(trackNumberColumn));
                    songList.add(song);
                    i++;
                }
                while (musicCursor.moveToNext());
                musicCursor.close();
                return songList;
            }
            return null;
        }
    }
}

