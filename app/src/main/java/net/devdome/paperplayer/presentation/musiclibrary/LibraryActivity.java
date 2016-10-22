package net.devdome.paperplayer.presentation.musiclibrary;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import net.devdome.paperplayer.R;
import net.devdome.paperplayer.presentation.musiclibrary.songs.SongsFragment;

import javax.inject.Inject;

/**
 * PaperPlayer
 * Michael Obi
 * 15 10 2016 3:36 PM
 */

public class LibraryActivity extends AppCompatActivity implements MusicLibraryContract.View {

    @Inject
    MusicLibraryContract.Presenter musicLibraryPresenter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        DaggerLibraryComponent.builder()
                .build()
                .inject(this);
        musicLibraryPresenter.attachView(this);
        musicLibraryPresenter.initialize();
    }

    @Override
    public void initializeViewPager() {
        ViewPager mViewPager = (ViewPager) findViewById(R.id.container);
        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());
        viewPagerAdapter.addFragment(new SongsFragment(), getResources().getString(R.string.songs));
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
