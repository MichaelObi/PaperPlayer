package net.devdome.paperplayer.presentation.musiclibrary;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import net.devdome.paperplayer.R;
import net.devdome.paperplayer.presentation.musiclibrary.fragment.albums.AlbumsFragment;
import net.devdome.paperplayer.presentation.musiclibrary.fragment.artist.ArtistsFragment;
import net.devdome.paperplayer.presentation.musiclibrary.fragment.songs.SongsFragment;

/**
 * PaperPlayer
 * Michael Obi
 * 15 10 2016 3:36 PM
 */

public class MusicLibraryActivity extends AppCompatActivity implements MusicLibraryContract.View {

    MusicLibraryContract.Presenter musicLibraryPresenter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        musicLibraryPresenter = new MusicLibraryPresenter();
        musicLibraryPresenter.attachView(this);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
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
}
