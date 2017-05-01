package net.devdome.paperplayer.presentation.musiclibrary;

import net.devdome.paperplayer.event.EventBus;
import net.devdome.paperplayer.injection.Injector;
import net.devdome.paperplayer.mvp.BasePresenter;
import net.devdome.paperplayer.playback.events.action.TogglePlayback;

/**
 * PaperPlayer
 * Michael Obi
 * 15 10 2016 3:45 PM
 */

class MusicLibraryPresenter extends BasePresenter<MusicLibraryContract.View> implements
        MusicLibraryContract.Presenter {
    private EventBus bus;

    public MusicLibraryPresenter() {
        bus = Injector.provideEventBus();
    }

    @Override
    public void attachView(MusicLibraryContract.View view) {
        super.attachView(view);
        initialize();
    }

    public void initialize() {
        checkViewAttached();
        getViewOrThrow().initializeViewPager();
    }

    @Override
    public void onFabClick() {
        bus.post(new TogglePlayback());
    }
}
