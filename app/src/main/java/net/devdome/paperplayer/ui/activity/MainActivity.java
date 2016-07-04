package net.devdome.paperplayer.ui.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import net.devdome.paperplayer.R;
import net.devdome.paperplayer.adapter.ViewPagerAdapter;
import net.devdome.paperplayer.playback.PlayerService;
import net.devdome.paperplayer.playback.PlayerServiceOld;
import net.devdome.paperplayer.ui.fragment.AlbumsFragment;
import net.devdome.paperplayer.ui.fragment.ArtistsFragment;
import net.devdome.paperplayer.ui.fragment.SongsFragment;
import net.devdome.paperplayer.ui.widget.PlayPauseButton;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    static {
//        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
    }

    private FloatingActionButton fab;
    private TextView miniPlayerSongName, miniPlayerSongArtist;
    private ImageView miniAlbumArt;
    private CardView cardPlaying;
    private PlayPauseButton playPauseButton;
    private BroadcastReceiver playerStateReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals(NowPlayingActivity.ACTION_GET_PLAY_STATE)) {
                Boolean isPlaying = intent.getBooleanExtra(PlayerServiceOld.IS_PLAYING, false);
                loadArt(intent.getLongExtra(PlayerServiceOld.SONG_ALBUM_ID, 0));
                if (isPlaying) {
                    fab.setVisibility(View.GONE);
                    miniPlayerSongName.setText(intent.getStringExtra(PlayerServiceOld.SONG_NAME));
                    miniPlayerSongArtist.setText(intent.getStringExtra(PlayerServiceOld.SONG_ARTIST));
                    playPauseButton.pause();
                } else {
                    playPauseButton.play();
                    miniPlayerSongName.setText(intent.getStringExtra(PlayerServiceOld.SONG_NAME));
                    miniPlayerSongArtist.setText(intent.getStringExtra(PlayerServiceOld.SONG_ARTIST));
                }
                if (intent.getStringExtra(PlayerService.SONG_NAME) == null) {
                    fab.setVisibility(View.VISIBLE);
                }
            }
        }
    };

    private void loadArt(long albumId) {
        String songArtPath = "";
        Cursor cursor = getContentResolver().query(MediaStore.Audio.Albums.EXTERNAL_CONTENT_URI,
                new String[]{MediaStore.Audio.Albums._ID, MediaStore.Audio.Albums.ALBUM_ART},
                MediaStore.Audio.Albums._ID + "=?",
                new String[]{String.valueOf(albumId)},
                null);
        if (cursor.moveToFirst()) {
            songArtPath = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Albums.ALBUM_ART));
        }
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = false;
        options.inPreferredConfig = Bitmap.Config.ARGB_8888;
        options.inDither = true;
        Drawable artWork = new BitmapDrawable(getResources(), BitmapFactory.decodeFile(songArtPath, options));
        ((ImageView) findViewById(R.id.album_art_mini)).setImageDrawable(artWork);
        cursor.close();

    }

    @Override
    protected void onResume() {
        super.onResume();
        registerReceiver(playerStateReceiver, new IntentFilter(NowPlayingActivity.ACTION_GET_PLAY_STATE));
        Intent i = new Intent(PlayerServiceOld.ACTION_REQUEST_PLAY_STATE);
        sendBroadcast(i);
    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(playerStateReceiver);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        initViewPager();
        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent();
                i.setAction(PlayerServiceOld.ACTION_PLAY_ALL);
                sendBroadcast(i);
            }
        });
        cardPlaying = (CardView) findViewById(R.id.cv_mini_player);
        miniPlayerSongName = (TextView) findViewById(R.id.song_name_mini);
        miniPlayerSongArtist = (TextView) findViewById(R.id.song_artist_mini);
        miniAlbumArt = (ImageView) findViewById(R.id.album_art_mini);
        playPauseButton = (PlayPauseButton) findViewById(R.id.play_pause);

        playPauseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent stopMusic = new Intent();
                stopMusic.setAction(PlayerServiceOld.ACTION_PAUSE);
                sendBroadcast(stopMusic);
            }
        });
        cardPlaying.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent();
                i.setAction(PlayerServiceOld.ACTION_VIEW_NOW_PLAYING);
                sendBroadcast(i);
            }
        });
    }

    private void initViewPager() {
        ViewPager mViewPager = (ViewPager) findViewById(R.id.container);
        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());
        viewPagerAdapter.addFragment(new SongsFragment(), getResources().getString(R.string.songs));
        viewPagerAdapter.addFragment(new AlbumsFragment(), getResources().getString(R.string.albums));
        viewPagerAdapter.addFragment(new ArtistsFragment(), getResources().getString(R.string.artists));
        if (mViewPager != null) {
            mViewPager.setAdapter(viewPagerAdapter);
            mViewPager.setCurrentItem(0);
        }
        TabLayout tabLayout = (TabLayout) findViewById(R.id.main_tablayout);
        if (tabLayout != null) {
            tabLayout.setTabMode(TabLayout.MODE_FIXED);
            tabLayout.setupWithViewPager(mViewPager);
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            startActivity(new Intent(this, SettingsActivity.class));
        }
        return true;
    }
}
