package net.devdome.paperplayer.data.library.local;

import android.content.Context;
import android.database.Cursor;
import android.provider.MediaStore;

import net.devdome.paperplayer.data.library.LibraryManager;
import net.devdome.paperplayer.data.model.Song;

import java.util.ArrayList;
import java.util.List;
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
    public Observable<List<Song>> fetchAllSongs() {

        return makeObservable(new Callable<List<Song>>() {
            @Override
            public List<Song> call() throws Exception {
                return new ArrayList<>();

//                Cursor musicCursor;
//
//                final String where = MediaStore.Audio.Media.IS_MUSIC + "=1";
//                final String orderBy = MediaStore.Audio.Media.TITLE;
//                musicCursor = context.getContentResolver().query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
//                        null, where, null, orderBy);
//                if (musicCursor != null && musicCursor.moveToFirst()) {
//                    int titleColumn = musicCursor.getColumnIndex
//                            (android.provider.MediaStore.Audio.Media.TITLE);
//                    int idColumn = musicCursor.getColumnIndex
//                            (android.provider.MediaStore.Audio.Media._ID);
//                    int artistColumn = musicCursor.getColumnIndex
//                            (android.provider.MediaStore.Audio.Media.ARTIST);
//                    int pathColumn = musicCursor.getColumnIndex
//                            (MediaStore.Audio.Media.DATA);
//                    int albumIdColumn = musicCursor.getColumnIndex
//                            (MediaStore.Audio.Media.ALBUM_ID);
//                    int albumColumn = musicCursor.getColumnIndex
//                            (MediaStore.Audio.Media.ALBUM);
//                    int i = 1;
//                    do {
//                        songList.add(new net.devdome.paperplayer_old.data.model.Song(musicCursor.getLong(idColumn),
//                                musicCursor.getString(titleColumn),
//                                musicCursor.getString(artistColumn),
//                                musicCursor.getString(pathColumn), false,
//                                musicCursor.getLong(albumIdColumn),
//                                musicCursor.getString(albumColumn), i, Mood.UNKNOWN));
//                        i++;
//                    }
//                    while (musicCursor.moveToNext());
//                    musicCursor.close();
//                    return songList;
//                }
//                return null;
            }
        });
    }

}
