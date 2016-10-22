package net.devdome.paperplayer.presentation.musiclibrary;

import net.devdome.paperplayer.mvp.BasePresenter;

/**
 * PaperPlayer
 * Michael Obi
 * 15 10 2016 3:45 PM
 */

class MusicLibraryPresenter extends BasePresenter<MusicLibraryContract.View> implements
        MusicLibraryContract.Presenter {

    public void initialize() {
        checkViewAttached();
        getView().initializeViewPager();
    }
}
