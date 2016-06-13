package net.devdome.paperplayer.ui.activity;

import android.database.Cursor;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.transition.Fade;
import android.transition.Transition;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import net.devdome.paperplayer.R;
import net.devdome.paperplayer.adapter.AlbumSongsAdapter;
import net.devdome.paperplayer.adapter.AlbumsAdapter;
import net.devdome.paperplayer.data.Mood;
import net.devdome.paperplayer.data.model.Song;

import java.io.File;
import java.util.ArrayList;

public class AlbumActivity extends AppCompatActivity {


    String artPath;
    Cursor musicCursor;
    ImageView artwork;
    FloatingActionButton fab;
    RecyclerView rvSongs;
    AlbumSongsAdapter adapter;
    long albumId;

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

        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        controlEnterAnimation();
        initAlbum();
    }

    private void initAlbum() {
        final String where = MediaStore.Audio.Albums._ID + "=?";
        musicCursor = getContentResolver().query(MediaStore.Audio.Albums.EXTERNAL_CONTENT_URI, new String[]{MediaStore.Audio.Albums._ID,
                MediaStore.Audio.Albums.ALBUM_ART}, where, new String[]{String.valueOf(albumId)}, null);

        if (musicCursor != null) {
            if (musicCursor.moveToFirst()) {
                artPath = musicCursor.getString(musicCursor.getColumnIndex(MediaStore.Audio.Albums.ALBUM_ART));
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
                    if (Build.VERSION.SDK_INT >= 21) {
                        Animation zoomIn = AnimationUtils.loadAnimation(AlbumActivity.this, R.anim.zoom_in);
                        zoomIn.setAnimationListener(new Animation.AnimationListener() {
                            @Override
                            public void onAnimationStart(Animation animation) {
                            }

                            @Override
                            public void onAnimationEnd(Animation animation) {
                                fab.setVisibility(View.VISIBLE);
                            }

                            @Override
                            public void onAnimationRepeat(Animation animation) {
                            }
                        });
                        fab.setAnimation(zoomIn);
                        fab.animate();
                    } else {
                        fab.setVisibility(View.VISIBLE);
                    }
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
            fab.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public class GetSongs extends AsyncTask<Void, Void, ArrayList> {

        @Override
        protected void onPostExecute(ArrayList songList) {
            super.onPostExecute(songList);
            if (songList != null) {
                adapter = new AlbumSongsAdapter(AlbumActivity.this, songList);
                rvSongs.setAdapter(adapter);
            }
        }

        @Override
        protected ArrayList doInBackground(Void... params) {
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
                musicCursor.close();
                return songList;
            }
            return null;
        }
    }
}

