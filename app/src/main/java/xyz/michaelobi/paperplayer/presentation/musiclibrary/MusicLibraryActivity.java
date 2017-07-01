/*
 * MIT License
 *
 * Copyright (c) 2017 MIchael Obi
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package xyz.michaelobi.paperplayer.presentation.musiclibrary;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.CompositePermissionListener;
import com.karumi.dexter.listener.single.DialogOnDeniedPermissionListener;
import com.karumi.dexter.listener.single.PermissionListener;

import xyz.michaelobi.paperplayer.R;
import xyz.michaelobi.paperplayer.event.EventBus;
import xyz.michaelobi.paperplayer.injection.Injector;
import xyz.michaelobi.paperplayer.playback.PlaybackService;
import xyz.michaelobi.paperplayer.playback.events.PlaybackState;
import xyz.michaelobi.paperplayer.playback.events.action.RequestPlaybackState;
import xyz.michaelobi.paperplayer.presentation.musiclibrary.fragment.albums.AlbumsFragment;
import xyz.michaelobi.paperplayer.presentation.musiclibrary.fragment.artist.ArtistsFragment;
import xyz.michaelobi.paperplayer.presentation.musiclibrary.fragment.songs.SongsFragment;

/**
 * PaperPlayer
 * Michael Obi
 * 15 10 2016 3:36 PM
 */

public class MusicLibraryActivity extends AppCompatActivity implements MusicLibraryContract.View,
        View.OnClickListener, PermissionListener {

    MusicLibraryContract.Presenter musicLibraryPresenter;

    private EventBus bus = Injector.provideEventBus();

    private FloatingActionButton fab;

    @Override
    protected void onResume() {
        super.onResume();
        setUpEventBus();
    }


    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        setTheme(R.style.AppTheme_NoActionBar);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        musicLibraryPresenter = new MusicLibraryPresenter();
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(this);
        PermissionListener listener = new CompositePermissionListener(this,
                DialogOnDeniedPermissionListener.Builder
                        .withContext(this)
                        .withTitle("Storage permission")
                        .withMessage("Permission to access your storage is needed to play music")
                        .withButtonText(android.R.string.ok)
                        .withIcon(R.mipmap.ic_player)
                        .build());

        Dexter.withActivity(this)
                .withPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
                .withListener(listener)
                .check();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        bus = null;
    }

    private void setUpEventBus() {
        bus.observe(PlaybackState.class)
                .subscribe(playbackState -> {
                    if (playbackState.getPlaying()) {
                        fab.setImageResource(R.drawable.ic_play_arrow_white_24dp);
                    } else {
                        fab.setImageResource(R.drawable.ic_pause_white_24dp);
                    }
                });
      bus.post(new RequestPlaybackState());
    }

    @Override
    public void initializeViewPager() {
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
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.fab:
                musicLibraryPresenter.onFabClick();
                break;
            default:
        }
    }

    @Override
    public void onPermissionGranted(PermissionGrantedResponse response) {
        musicLibraryPresenter.attachView(this);
        getApplicationContext().startService(new Intent(getApplicationContext(), PlaybackService.class));
    }

    @Override
    public void onPermissionDenied(PermissionDeniedResponse response) {
        finish();
    }

    @Override
    public void onPermissionRationaleShouldBeShown(PermissionRequest permission, PermissionToken token) {
        token.continuePermissionRequest();
    }
}
