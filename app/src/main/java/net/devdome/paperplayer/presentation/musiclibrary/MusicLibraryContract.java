package net.devdome.paperplayer.presentation.musiclibrary;

import android.support.v4.app.Fragment;

import net.devdome.paperplayer.mvp.Mvp;

/**
 * PaperPlayer
 * Michael Obi
 * 15 10 2016 3:39 PM
 */

interface MusicLibraryContract {
    interface View extends Mvp.View {

    }

    interface Presenter extends Mvp.Presenter<View> {
        void attachFragment(Fragment fragment);

        void detachFragment();
    }

}
