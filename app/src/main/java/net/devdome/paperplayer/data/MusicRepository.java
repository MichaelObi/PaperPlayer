package net.devdome.paperplayer.data;

import net.devdome.paperplayer.data.library.LibraryManager;
import net.devdome.paperplayer.data.model.Album;
import net.devdome.paperplayer.data.model.Song;

import java.io.IOException;
import java.util.List;

import rx.Observable;

/**
 * PaperPlayer
 * Michael Obi
 * 25 10 2016 3:03 AM
 */
public class MusicRepository implements MusicRepositoryInterface {


    private LibraryManager libraryManager;

    public MusicRepository(LibraryManager libraryManager) {

        this.libraryManager = libraryManager;
    }

    @Override
    public Observable<List<Song>> getAllSongs() {
        return Observable.defer(() -> libraryManager.fetchAllSongs())
                .retryWhen(observable -> observable.flatMap(o -> {
                            if (o instanceof IOException) {
                                return null;
                            }
                            return Observable.error(o);
                        })

                );
    }

    @Override
    public Observable<List<Album>> getAllAlbums() {
        return Observable.defer(() -> libraryManager.fetchAllAlbums())
                .retryWhen(observable -> observable.flatMap(o -> {
                            if (o instanceof IOException) {
                                return null;
                            }
                            return Observable.error(o);
                        })
                );
    }
}
