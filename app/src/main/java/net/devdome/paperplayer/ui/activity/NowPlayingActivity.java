package net.devdome.paperplayer.ui.activity;

import android.app.ActivityManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.graphics.Palette;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.annimon.paperstyle.PaperSeekBar;

import net.devdome.paperplayer.R;
import net.devdome.paperplayer.playback.PlayerService;

import java.util.Timer;
import java.util.TimerTask;

public class NowPlayingActivity extends AppCompatActivity {

    public static final String ACTION_GET_PLAY_STATE = "get_play_state";
    public static final String ACTION_GET_SEEK_VALUE = "get_seek_value";
    public static final String ACTION_GET_PLAYING_LIST = "get_playing_list";
    public static final String ACTION_GET_PLAYING_DETAIL = "get_playing_detail";
    private static final String TAG = "NowPlayingActivity";
    public static int mainColor;
    PaperSeekBar seekBar;
    ImageView albumArt;
    Timer timer;
    boolean musicPlaying;
    ImageButton playButton, nextButton;
    String strDuration;
    private TextView tvSongName, tvSongAlbumArtist, tvCurrentTimeHolder, tvDuration;
    private int duration, currentDuration;
    private String songPath, songName, songArtist, albumName, songArtPath;
    private long albumId;
    BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals(ACTION_GET_SEEK_VALUE)) {
                seekBar.setProgress(intent.getIntExtra(PlayerService.SONG_SEEK_VALUE, 0));
                currentDuration = intent.getIntExtra(PlayerService.SONG_SEEK_VALUE, 0);
                if (intent.getBooleanExtra(PlayerService.IS_PLAYING, false)) {
                    playButton.setImageResource(R.drawable.ic_pause_24dp);
                    musicPlaying = true;

                } else {
                    playButton.setImageResource(R.drawable.ic_play_arrow_24dp);
                    musicPlaying = false;
                }
            } else if (intent.getAction().equals(ACTION_GET_PLAY_STATE)) {
                if (intent.getBooleanExtra(PlayerService.IS_PLAYING, false)) {
                    playButton.setImageResource(R.drawable.ic_pause_24dp);
                    musicPlaying = true;
                } else {
                    playButton.setImageResource(R.drawable.ic_play_arrow_24dp);
                    musicPlaying = false;
                }
            } else if (intent.getAction().equals(ACTION_GET_PLAYING_DETAIL)) {
                songPath = intent.getStringExtra(PlayerService.SONG_PATH);
                songName = intent.getStringExtra(PlayerService.SONG_NAME);
                songArtist = intent.getStringExtra(PlayerService.SONG_ARTIST);
                albumId = intent.getLongExtra(PlayerService.SONG_ALBUM_ID, 0);
                albumName = intent.getStringExtra(PlayerService.SONG_ALBUM_NAME);
                duration = intent.getIntExtra(PlayerService.SONG_DURATION, 0);
                currentDuration = 0;
                musicPlaying = true;
                updateView();
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        overridePendingTransition(R.anim.slide_in_up, R.anim.fade_back);
        setContentView(R.layout.activity_player);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        IntentFilter filter = new IntentFilter();
        filter.addAction(ACTION_GET_SEEK_VALUE);
        filter.addAction(ACTION_GET_PLAY_STATE);
        filter.addAction(ACTION_GET_PLAYING_LIST);
        filter.addAction(ACTION_GET_PLAYING_DETAIL);
        registerReceiver(receiver, filter);


        seekBar = (PaperSeekBar) findViewById(R.id.player_seekbar);
        tvSongName = (TextView) findViewById(R.id.song_name);
        tvSongAlbumArtist = (TextView) findViewById(R.id.album_artist);
        albumArt = (ImageView) findViewById(R.id.album_art);
        playButton = (ImageButton) findViewById(R.id.btn_play_pause);
        nextButton = (ImageButton) findViewById(R.id.btn_next);
        tvCurrentTimeHolder = (TextView) findViewById(R.id.current_time);
        tvDuration = (TextView) findViewById(R.id.duration);

        songPath = getIntent().getStringExtra(PlayerService.SONG_PATH);
        songName = getIntent().getStringExtra(PlayerService.SONG_NAME);
        songArtist = getIntent().getStringExtra(PlayerService.SONG_ARTIST);
        albumId = getIntent().getLongExtra(PlayerService.SONG_ALBUM_ID, 0);
        albumName = getIntent().getStringExtra(PlayerService.SONG_ALBUM_NAME);
        duration = getIntent().getIntExtra(PlayerService.SONG_DURATION, 0);
        currentDuration = getIntent().getIntExtra(PlayerService.SONG_CURRENT_TIME, 0);
        setupUI();

        updateView();

    }

    @Override
    protected void onResume() {
        super.onResume();
        Intent i = new Intent();
        i.setAction(PlayerService.ACTION_SEEK_GET);
        sendBroadcast(i);
    }

    private void loadArt() {
        songArtPath = "";
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
        ((ImageView) findViewById(R.id.album_art)).setImageDrawable(artWork);
        cursor.close();

    }

    private void setupUI() {
        playButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent stopMusic = new Intent();
                stopMusic.setAction(PlayerService.ACTION_PAUSE);
                sendBroadcast(stopMusic);
            }
        });

        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent nextSong = new Intent();
                nextSong.setAction(PlayerService.ACTION_NEXT);
                sendBroadcast(nextSong);
            }
        });
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (fromUser) {
                    Intent i = new Intent(PlayerService.ACTION_SEEK_TO);
                    i.putExtra(PlayerService.SEEK_CHANGE, progress);
                    sendBroadcast(i);
                    musicPlaying = true;
                } else {
                    if (progress == duration) {
                        seekBar.setProgress(100);
                        musicPlaying = false;
                    }
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }


    private void updateSeeker() {
        Log.e(TAG, "Duration: " + duration);
        seekBar.setMax(duration);
        seekBar.setProgress(currentDuration);
        strDuration = String.format("%s:%s", String.format("%02d", ((duration / 1000) / 60)), String.format("%02d", ((duration / 1000) % 60)));
        tvDuration.setText(strDuration);
        musicPlaying = true;
        if (timer != null) timer.cancel();
        timer = new Timer();
        timer.schedule(new TimerTask() {

            @Override
            public void run() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (musicPlaying) {
                            int progress = seekBar.getProgress();
                            if (progress < duration) {
                                seekBar.setProgress(progress + 100);
                            } else {
                                seekBar.setProgress(100);
                            }
                            new ChangeSeekDetailUpdater(progress, tvCurrentTimeHolder).execute();
                        }
                    }
                });
            }
        }, 0, 100);
    }

    private void updateView() {
        tvSongName.setText(songName);
        tvSongAlbumArtist.setText(String.format("%s — %s", songArtist, albumName));

        updateSeeker();

//        loadArt();
//        setTheme();
    }

    private void setTheme() {
        if (songArtPath != null) {
            try {
                new Palette.Builder(((BitmapDrawable) albumArt.getDrawable()).getBitmap()).generate(new Palette.PaletteAsyncListener() {
                    @Override
                    public void onGenerated(Palette palette) {
                        Palette.Swatch darkVibrantSwatch = palette.getDarkVibrantSwatch();
                        LinearLayout detailsLayout = (LinearLayout) findViewById(R.id.song_details);

                        Palette.Swatch secondarySwatch = palette.getLightVibrantSwatch();
                        if (secondarySwatch == null) {
                            secondarySwatch = palette.getMutedSwatch();
                        }
                        if (secondarySwatch != null) {
                            seekBar.setColor(secondarySwatch.getRgb());
                        }
                        if (darkVibrantSwatch != null) {
                            mainColor = darkVibrantSwatch.getRgb();
                            detailsLayout.setBackgroundColor(mainColor);
                            if (Build.VERSION.SDK_INT >= 21) {
                                ActivityManager.TaskDescription taskDescription = new
                                        ActivityManager.TaskDescription(getResources().getString(R.string.app_name),
                                        BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher),
                                        darkVibrantSwatch.getRgb());
                                setTaskDescription(taskDescription);
                            }

                        } else {
                            mainColor = Color.parseColor("#37474f");
                        }
                    }
                });
            } catch (Exception e) {
                Log.e(TAG, e.getMessage());
                mainColor = Color.parseColor("#37474f");
                albumArt.setImageResource(R.drawable.default_artwork_dark);
            }
        } else {
            mainColor = Color.parseColor("#37474f");
            albumArt.setImageResource(R.drawable.default_artwork_dark);
        }
    }

    @Override
    protected void onPause() {
        overridePendingTransition(R.anim.fade_forward, R.anim.slide_out_down);

        super.onPause();
    }

    @Override
    protected void onDestroy() {
        unregisterReceiver(receiver);
        super.onDestroy();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    private class ChangeSeekDetailUpdater extends AsyncTask<Void, Void, Void> {

        private final TextView currentTime;
        private int mSeekBarProgress;
        private String min, sec;

        public ChangeSeekDetailUpdater(int seekBar, TextView currentTime) {
            this.mSeekBarProgress = seekBar;
            this.currentTime = currentTime;
        }

        @Override
        protected Void doInBackground(Void... params) {
            min = String.format("%02d", ((mSeekBarProgress / 1000) / 60));
            sec = String.format("%02d", ((mSeekBarProgress / 1000) % 60));
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            currentTime.setText(String.format("%s:%s", min, sec));
        }
    }
}
