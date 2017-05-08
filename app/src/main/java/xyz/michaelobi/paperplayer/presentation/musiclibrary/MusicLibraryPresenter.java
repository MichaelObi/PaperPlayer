package xyz.michaelobi.paperplayer.presentation.musiclibrary;

import xyz.michaelobi.paperplayer.event.EventBus;
import xyz.michaelobi.paperplayer.injection.Injector;
import xyz.michaelobi.paperplayer.mvp.BasePresenter;
import xyz.michaelobi.paperplayer.playback.events.action.TogglePlayback;

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
