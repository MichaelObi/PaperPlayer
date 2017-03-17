package net.devdome.paperplayer.presentation.musiclibrary.fragment.songs;

import android.util.Log;

import net.devdome.paperplayer.data.MusicRepositoryInterface;
import net.devdome.paperplayer.data.model.Song;
import net.devdome.paperplayer.mvp.BasePresenter;
import net.devdome.paperplayer.presentation.musiclibrary.fragment.FragmentsContract;

import java.util.List;

import rx.Scheduler;
import rx.Subscriber;

/**
 * PaperPlayer
 * Michael Obi
 * 15 10 2016 3:45 PM
 */

public class SongsPresenter extends BasePresenter<FragmentsContract.View> implements
        FragmentsContract.Presenter {

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
