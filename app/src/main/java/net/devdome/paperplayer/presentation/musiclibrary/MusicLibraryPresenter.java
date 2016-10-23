package net.devdome.paperplayer.presentation.musiclibrary;

import net.devdome.paperplayer.mvp.BasePresenter;

/**
 * PaperPlayer
 * Michael Obi
 * 15 10 2016 3:45 PM
 */

class MusicLibraryPresenter extends BasePresenter<MusicLibraryContract.View> implements
        MusicLibraryContract.Presenter {

    @Override
    public void attachView(MusicLibraryContract.View view) {
        super.attachView(view);
        initialize();
    }

    public void initialize() {
        checkViewAttached();
        getView().initializeViewPager();
    }
}
