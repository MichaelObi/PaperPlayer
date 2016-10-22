package net.devdome.paperplayer.presentation.musiclibrary.songs;

import net.devdome.paperplayer.data.MusicLibraryRepoContract;
import net.devdome.paperplayer.data.model.Song;
import net.devdome.paperplayer.mvp.BasePresenter;

import java.util.List;

import rx.Scheduler;
import rx.Subscriber;

/**
 * PaperPlayer
 * Michael Obi
 * 15 10 2016 3:45 PM
 */

class SongsPresenter extends BasePresenter<SongsContract.View> implements
        SongsContract.Presenter {

    private final Scheduler ioScheduler;
    private final Scheduler mainScheduler;

    private MusicLibraryRepoContract musicLibraryRepo;

    SongsPresenter(MusicLibraryRepoContract musicLibraryRepo, Scheduler ioScheduler, Scheduler mainScheduler) {
        this.ioScheduler = ioScheduler;
        this.mainScheduler = mainScheduler;
        this.musicLibraryRepo = musicLibraryRepo;
    }

    @Override
    public void getAllSongs() {
        checkViewAttached();
        getView().showLoading();
        addSubscription(musicLibraryRepo.fetchAllSongs().subscribeOn(ioScheduler).observeOn
                (mainScheduler).subscribe(new Subscriber<List<Song>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                getView().hideLoading();
                getView().showError(e.getMessage());
            }

            @Override
            public void onNext(List<Song> songs) {
                getView().hideLoading();
                getView().showSongsList(songs);
            }
        }));


    }
}
