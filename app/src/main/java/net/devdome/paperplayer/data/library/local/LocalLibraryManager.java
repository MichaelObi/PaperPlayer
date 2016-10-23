package net.devdome.paperplayer.data.library.local;

import net.devdome.paperplayer.data.library.LibraryManager;
import net.devdome.paperplayer.data.model.Song;
import net.devdome.paperplayer.data.model.SongList;

import java.util.concurrent.Callable;

import rx.Observable;
import rx.Subscriber;

/**
 * PaperPlayer
 * Michael Obi
 * 22 10 2016 12:29 PM
 */

public class LocalLibraryManager implements LibraryManager {
    private static final String TAG = "LocalLibraryManager";

    private static <T> Observable<T> makeObservable(final Callable<T> func) {
        return Observable.create(
                new Observable.OnSubscribe<T>() {
                    @Override
                    public void call(Subscriber<? super T> subscriber) {
                        try {
                            subscriber.onNext(func.call());
                        } catch (Exception ex) {
                            subscriber.onError(ex);
                        }
                    }
                });
    }

    @Override
    public Observable<SongList> fetchAllSongs() {
        return null;
    }

}
