package net.devdome.paperplayer.presentation.musiclibrary.fragment.albums;

import android.util.Log;

import net.devdome.paperplayer.data.MusicRepositoryInterface;
import net.devdome.paperplayer.data.model.Album;
import net.devdome.paperplayer.mvp.BasePresenter;
import net.devdome.paperplayer.presentation.musiclibrary.fragment.LibraryFragmentsContract;

import java.util.List;

import rx.Scheduler;
import rx.Subscriber;

/**
 * PaperPlayer
 * Michael Obi
 * 15 10 2016 3:45 PM
 */

public class AlbumsPresenter extends BasePresenter<LibraryFragmentsContract.View> implements
        LibraryFragmentsContract.Presenter {

    private static final String TAG = "AlbumsPresenter";
    private final Scheduler ioScheduler;
    private final Scheduler mainScheduler;

    private MusicRepositoryInterface musicRepository;

    public AlbumsPresenter(MusicRepositoryInterface musicRepository, Scheduler ioScheduler, Scheduler
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
        addSubscription(musicRepository.getAllAlbums()
                .subscribeOn(ioScheduler)
                .observeOn(mainScheduler)
                .subscribe(
                        new Subscriber<List<Album>>() {
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
                            public void onNext(List<Album> albums) {
                                getView().hideLoading();
                                getView().showList(albums);
                            }
                        }
                )
        );
    }
}
