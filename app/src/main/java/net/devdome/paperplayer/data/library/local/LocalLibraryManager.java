package net.devdome.paperplayer.data.library.local;

import android.content.Context;
import android.database.Cursor;
import android.provider.MediaStore;
import android.util.Log;

import net.devdome.paperplayer.data.library.LibraryManager;
import net.devdome.paperplayer.data.model.Album;
import net.devdome.paperplayer.data.model.Song;

import java.util.List;
import java.util.stream.Collectors;

import rx.Observable;

/**
 * PaperPlayer
 * Michael Obi
 * 22 10 2016 12:29 PM
 */

public class LocalLibraryManager implements LibraryManager {
    private static final String TAG = "LocalLibraryManager";
    private final Context context;

    public LocalLibraryManager(Context context) {
        this.context = context;
    }


    public static Observable<Cursor> create(Cursor cursor) {
        return Observable.create(sub -> {
            if (sub.isUnsubscribed()) return;

            try {
                while (cursor.moveToNext()) {
                    if (sub.isUnsubscribed()) return;
                    sub.onNext(cursor);
                }
                sub.onCompleted();
            } catch (Exception e) {
                sub.onError(e);
            }
        });
    }

    @Override
    public Observable<List<Song>> fetchAllSongs() {
        QueryBuilder builder = new QueryBuilder(context, MediaStore.Audio.Media.EXTERNAL_CONTENT_URI)
//                .where(MediaStore.Audio.Media.IS_MUSIC + "=1")
                .orderBy(MediaStore.Audio.Media.TITLE);
        Cursor cursor = builder.query();
        return create(cursor)
                .map(Song::from)
                .toList();
    }


    @Override
    public Observable<List<Album>> fetchAllAlbums() {
        return null;
    }

    @Override
    public Observable<Song> getSong(long id) {
        return null;
    }

}
