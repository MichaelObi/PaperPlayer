package net.devdome.paperplayer.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import net.devdome.paperplayer.R;
import net.devdome.paperplayer.adapter.ViewPagerAdapter;
import net.devdome.paperplayer.playback.PlayerService;
import net.devdome.paperplayer.ui.fragment.AlbumsFragment;
import net.devdome.paperplayer.ui.fragment.SongsFragment;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    /**
     * The {@link ViewPager} that will host the section contents.
     */
    FloatingActionButton fab;
    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide fragments for each of the
     * sections. We use a {@link FragmentPagerAdapter} derivative, which will keep every loaded
     * fragment in memory. If this becomes too memory intensive, it may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    private ViewPagerAdapter viewPagerAdapter;
    private ViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        startService(new Intent(this, PlayerService.class));

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        initViewPager();
        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                fab.hide();

//                Intent i = new Intent(MainActivity.this, NowPlayingActivity.class);
//                i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                startActivity(i);

                Intent i = new Intent();
                i.setAction(PlayerService.ACTION_VIEW_NOW_PLAYING);
                sendBroadcast(i);

            }
        });

    }


    private void initViewPager() {
        mViewPager = (ViewPager) findViewById(R.id.container);
        viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());
        viewPagerAdapter.addFragment(new SongsFragment(), getResources().getString(R.string.songs));
        viewPagerAdapter.addFragment(new AlbumsFragment(), getResources().getString(R.string.albums));
        viewPagerAdapter.addFragment(new SongsFragment(), getResources().getString(R.string.artists));
        viewPagerAdapter.addFragment(new SongsFragment(), getResources().getString(R.string.playlists));
        mViewPager.setAdapter(viewPagerAdapter);
        mViewPager.setCurrentItem(0);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.main_tablayout);
        tabLayout.setTabMode(TabLayout.MODE_SCROLLABLE);
        tabLayout.setupWithViewPager(mViewPager);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
