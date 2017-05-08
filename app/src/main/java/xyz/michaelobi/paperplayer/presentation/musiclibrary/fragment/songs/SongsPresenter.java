package xyz.michaelobi.paperplayer.presentation.musiclibrary.fragment.songs;

import android.util.Log;

import xyz.michaelobi.paperplayer.data.MusicRepositoryInterface;
import xyz.michaelobi.paperplayer.data.model.Song;
import xyz.michaelobi.paperplayer.mvp.BasePresenter;
import xyz.michaelobi.paperplayer.presentation.musiclibrary.fragment.LibraryFragmentsContract;

import java.util.List;

import rx.Scheduler;
import rx.Subscriber;
import xyz.michaelobi.paperplayer.data.MusicRepositoryInterface;
import xyz.michaelobi.paperplayer.data.model.Song;
import xyz.michaelobi.paperplayer.mvp.BasePresenter;
import xyz.michaelobi.paperplayer.presentation.musiclibrary.fragment.LibraryFragmentsContract;

/**
 * PaperPlayer
 * Michael Obi
 * 15 10 2016 3:45 PM
 */

public class SongsPresenter extends BasePresenter<LibraryFragmentsContract.View> implements
        LibraryFragmentsContract.Presenter {

    private static final String TAG = "AlbumsPresenter";
    private final Scheduler ioScheduler;
    private final Scheduler mainScheduler;

    private MusicRepositoryInterface musicRepository;

    public SongsPresenter(MusicRepositoryInterface musicRepository, Scheduler ioScheduler, Scheduler
            mainScheduler) {
        super();
        this.ioScheduler = ioScheduler;
        this.mainScheduler = mainScheduler;
        this.musicRepository = musicRepository;
    }

    @Override
    public void getAll() {
        checkViewAttached();
        getView().showLoading();
        addSubscription(musicRepository.getAllSongs()
                .subscribeOn(ioScheduler)
                .observeOn(mainScheduler)
                .subscribe(
                        new Subscriber<List<Song>>() {
                            @Override
                            public void onCompleted() {

                            }

                            @Override
                            public void onError(Throwable e) {
                                getView().hideLoading();
                                Log.e(TAG, e.getLocalizedMessage(), e);
                                getView().showError(e.getMessage());
                            }

                            @Override
                            public void onNext(List<Song> songs) {
                                getView().hideLoading();
                                getView().showList(songs);
                            }
                        }
                )
        );
    }
}
