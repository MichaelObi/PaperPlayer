package xyz.michaelobi.paperplayer.presentation.musiclibrary;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * PaperPlayer
 * Michael Obi
 * 19 10 2016 5:34 PM
 */

class ViewPagerAdapter extends FragmentPagerAdapter {

    private List<Fragment> fragments = new ArrayList<>();
    private List<String> fragmentTitles = new ArrayList<>();

    ViewPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        return fragments.get(position);
    }

    @Override
    public int getCount() {
        return fragments.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return fragmentTitles.get(position);
    }

    void setFragments(List<Fragment> fragments) {
        this.fragments = fragments;
    }

    void addFragment(Fragment fragment, String title) {
        fragments.add(fragment);
        fragmentTitles.add(title);
    }
}
