package net.devdome.paperplayer_old.ui.activity;

import android.app.ActivityManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.graphics.Palette;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.transition.Explode;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import net.devdome.paperplayer_old.Constants;
import net.devdome.paperplayer_old.R;
import net.devdome.paperplayer_old.adapter.ViewPagerAdapter;
import net.devdome.paperplayer_old.playback.PlayerService;
import net.devdome.paperplayer_old.ui.fragment.AlbumsFragment;
import net.devdome.paperplayer_old.ui.fragment.ArtistsFragment;
import net.devdome.paperplayer_old.ui.fragment.SongsFragment;
import net.devdome.paperplayer_old.ui.widget.PlayPauseButton;

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
                Boolean isPlaying = intent.getBooleanExtra(Constants.KEY_IS_PLAYING, false);
                loadArt(intent.getLongExtra(Constants.SONG_ALBUM_ID, 0));
                if (isPlaying) {
                    fab.setVisibility(View.GONE);
                    miniPlayerSongName.setText(intent.getStringExtra(Constants.SONG_NAME));
                    miniPlayerSongArtist.setText(intent.getStringExtra(Constants.SONG_ARTIST));
                    playPauseButton.pause();
                } else {
                    playPauseButton.play();
                    miniPlayerSongName.setText(intent.getStringExtra(Constants.SONG_NAME));
                    miniPlayerSongArtist.setText(intent.getStringExtra(Constants.SONG_ARTIST));
                }
                if (intent.getStringExtra(Constants.SONG_NAME) == null) {
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

        changeColorScheme(miniAlbumArt);
    }

    private void changeColorScheme(ImageView imgView) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        if (!preferences.getBoolean(getString(R.string.key_dynamic_theme), false)) {
            return;
        }

        try {
            Bitmap bitmap = ((BitmapDrawable) imgView.getDrawable()).getBitmap();
            new Palette.Builder(bitmap).generate(new Palette.PaletteAsyncListener() {
                @Override
                public void onGenerated(Palette palette) {
                    Palette.Swatch swatch = palette.getDarkVibrantSwatch();

                    int bgColor = ContextCompat.getColor(MainActivity.this, R.color.colorPrimary);
                    if (swatch == null) {
                        swatch = palette.getDarkMutedSwatch();
                    }
                    if (swatch != null) {
                        bgColor = swatch.getRgb();
                    }

                    ((RelativeLayout) findViewById(R.id.layout_mini_player)).setBackgroundColor(bgColor);
                    if (Build.VERSION.SDK_INT >= 21) {
                        ActivityManager.TaskDescription taskDescription = new
                                ActivityManager.TaskDescription(null, null, bgColor);
                        setTaskDescription(taskDescription);
                        getWindow().setStatusBarColor(bgColor);
                    }

                    ((AppBarLayout) findViewById(R.id.app_bar)).setBackgroundColor(bgColor);
                }
            });
        } catch (Exception e) {
            Log.e(TAG, e.getMessage());
            int bgColor = ContextCompat.getColor(MainActivity.this, R.color.colorPrimary);
            ((RelativeLayout) findViewById(R.id.layout_mini_player)).setBackgroundColor(bgColor);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        registerReceiver(playerStateReceiver, new IntentFilter(NowPlayingActivity.ACTION_GET_PLAY_STATE));
        Intent i = new Intent(Constants.ACTION_REQUEST_PLAY_STATE);
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
        startService(new Intent(this, PlayerService.class));
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        initViewPager();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setExitTransition(new Explode());
        }

        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent();
                i.setAction(Constants.ACTION_PLAY_ALL);
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
                stopMusic.setAction(Constants.ACTION_PAUSE);
                sendBroadcast(stopMusic);
            }
        });
        cardPlaying.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent();
                i.setAction(Constants.ACTION_VIEW_NOW_PLAYING);
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
