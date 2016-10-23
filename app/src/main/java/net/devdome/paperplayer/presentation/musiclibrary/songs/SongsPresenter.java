package net.devdome.paperplayer.presentation.musiclibrary.songs;

import android.util.Log;

import net.devdome.paperplayer.data.library.LibraryManager;
import net.devdome.paperplayer.data.model.SongList;
import net.devdome.paperplayer.injection.Injector;
import net.devdome.paperplayer.mvp.BasePresenter;

import rx.Scheduler;
import rx.Subscriber;

/**
 * PaperPlayer
 * Michael Obi
 * 15 10 2016 3:45 PM
 */

class SongsPresenter extends BasePresenter<SongsContract.View> implements
        SongsContract.Presenter {

    private static final String TAG = "SongsPresenter";
    private final Scheduler ioScheduler;
    private final Scheduler mainScheduler;

    LibraryManager libraryManager;

    public SongsPresenter(Scheduler ioScheduler, Scheduler mainScheduler) {
        super();
        this.ioScheduler = ioScheduler;
        this.mainScheduler = mainScheduler;
        libraryManager = Injector.provideLibraryManager();
    }

    @Override
    public void getAllSongs() {
        checkViewAttached();
        getView().showLoading();
        addSubscription(libraryManager.fetchAllSongs()
                .subscribeOn(ioScheduler)
                .observeOn(mainScheduler)
                .subscribe(
                        new Subscriber<SongList>() {
                            @Override
                            public void onCompleted() {

                            }

                            @Override
                            public void onError(Throwable e) {
                                getView().hideLoading();
                                getView().showError(e.getMessage());
                            }

                            @Override
                            public void onNext(SongList songs) {
                                Log.d(TAG, "Song Count = " + songs.getSongs().size());
                                getView().hideLoading();
                                getView().showSongsList(songs.getSongs());
                            }
                        }
                )
        );
    }
}
