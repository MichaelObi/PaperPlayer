package net.devdome.paperplayer.presentation.musiclibrary.fragment.artist;

import android.util.Log;

import net.devdome.paperplayer.data.MusicRepositoryInterface;
import net.devdome.paperplayer.data.model.Artist;
import net.devdome.paperplayer.mvp.BasePresenter;
import net.devdome.paperplayer.presentation.musiclibrary.fragment.FragmentsContract;

import java.util.List;

import rx.Scheduler;
import rx.Subscriber;

/**
 * PaperPlayer Michael Obi 15 10 2016 3:45 PM
 */

public class ArtistsPresenter extends BasePresenter<FragmentsContract.View> implements
        FragmentsContract.Presenter {

    private static final String TAG = "ArtistsPresenter";
    private final Scheduler ioScheduler;
    private final Scheduler mainScheduler;

    private MusicRepositoryInterface musicRepository;

    public ArtistsPresenter(MusicRepositoryInterface musicRepository, Scheduler ioScheduler, Scheduler
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
        addSubscription(musicRepository.getAllArtists()
                .subscribeOn(ioScheduler)
                .observeOn(mainScheduler)
                .subscribe(
                        new Subscriber<List<Artist>>() {
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
                            public void onNext(List<Artist> artists) {
                                getView().hideLoading();
                                getView().showList(artists);
                            }
                        }
                )
        );
    }
}
