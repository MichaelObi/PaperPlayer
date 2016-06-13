package net.devdome.paperplayer.playback;

import android.content.Context;
import android.database.Cursor;
import android.provider.MediaStore;

import net.devdome.paperplayer.data.Mood;
import net.devdome.paperplayer.data.model.Song;

import java.util.ArrayList;

public class LibraryManager {

    final ArrayList<net.devdome.paperplayer.data.model.Song> songList = new ArrayList<>();
    private Context context;

    public LibraryManager(Context context) {
        this.context = context;
    }

    /**
     * Pulls all songs in music library
     */
    public ArrayList<Song> getAllSongs() {
        Cursor musicCursor;

        final String where = MediaStore.Audio.Media.IS_MUSIC + "=1";
        final String orderBy = MediaStore.Audio.Media.TITLE;
        musicCursor = context.getContentResolver().query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
                null, where, null, orderBy);
        if (musicCursor != null && musicCursor.moveToFirst()) {
            int titleColumn = musicCursor.getColumnIndex
                    (android.provider.MediaStore.Audio.Media.TITLE);
            int idColumn = musicCursor.getColumnIndex
                    (android.provider.MediaStore.Audio.Media._ID);
            int artistColumn = musicCursor.getColumnIndex
                    (android.provider.MediaStore.Audio.Media.ARTIST);
            int pathColumn = musicCursor.getColumnIndex
                    (MediaStore.Audio.Media.DATA);
            int albumIdColumn = musicCursor.getColumnIndex
                    (MediaStore.Audio.Media.ALBUM_ID);
            int albumColumn = musicCursor.getColumnIndex
                    (MediaStore.Audio.Media.ALBUM);
            int i = 1;
            do {
                songList.add(new net.devdome.paperplayer.data.model.Song(musicCursor.getLong(idColumn),
                        musicCursor.getString(titleColumn),
                        musicCursor.getString(artistColumn),
                        musicCursor.getString(pathColumn), false,
                        musicCursor.getLong(albumIdColumn),
                        musicCursor.getString(albumColumn), i, Mood.UNKNOWN));
                i++;
            }
            while (musicCursor.moveToNext());
            musicCursor.close();
            return songList;
        }
        return null;
    }

    /**
     * Pulls all songs in an album
     */
    public ArrayList<Song> getAlbumSongs(long albumId) {
        Cursor musicCursor;

        final String where = MediaStore.Audio.Media.ALBUM_ID + "=?";
        final String orderBy = MediaStore.Audio.Media._ID;
        musicCursor = context.getContentResolver().query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
                null, where, new String[]{String.valueOf(albumId)}, orderBy);
        if (musicCursor != null && musicCursor.moveToFirst()) {
            int titleColumn = musicCursor.getColumnIndex
                    (android.provider.MediaStore.Audio.Media.TITLE);
            int idColumn = musicCursor.getColumnIndex
                    (android.provider.MediaStore.Audio.Media._ID);
            int artistColumn = musicCursor.getColumnIndex
                    (android.provider.MediaStore.Audio.Media.ARTIST);
            int pathColumn = musicCursor.getColumnIndex
                    (MediaStore.Audio.Media.DATA);
            int albumIdColumn = musicCursor.getColumnIndex
                    (MediaStore.Audio.Media.ALBUM_ID);
            int albumColumn = musicCursor.getColumnIndex
                    (MediaStore.Audio.Media.ALBUM);
            int i = 1;
            do {
                songList.add(new net.devdome.paperplayer.data.model.Song(musicCursor.getLong(idColumn),
                        musicCursor.getString(titleColumn),
                        musicCursor.getString(artistColumn),
                        musicCursor.getString(pathColumn), false,
                        musicCursor.getLong(albumIdColumn),
                        musicCursor.getString(albumColumn), i, Mood.UNKNOWN));
                i++;
            }
            while (musicCursor.moveToNext());
            musicCursor.close();
        }
        return songList;
    }
}
