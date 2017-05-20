package xyz.michaelobi.paperplayer.data.model

import android.database.Cursor
import android.provider.MediaStore
import java.io.File

/**
 * PaperPlayer
 * Michael Obi
 * 23 10 2016 4:06 PM
 */

class Album(val id: Long, val title: String, val artist: String, val isFavorite: Boolean, var artPath: String?, val numberOfSongs: Int) {

    val art: File?
        get() = artPath?.let { File(it) }

    companion object {

        fun from(cursor: Cursor): Album {
            val titleColumn = cursor.getColumnIndex(MediaStore.Audio.Albums.ALBUM)
            val idColumn = cursor.getColumnIndex(android.provider.MediaStore.Audio.Albums._ID)
            val artistColumn = cursor.getColumnIndex(android.provider.MediaStore.Audio.Albums.ARTIST)
            val numOfSongsColumn = cursor.getColumnIndex(android.provider.MediaStore.Audio.Albums.NUMBER_OF_SONGS)
            val albumArtColumn = cursor.getColumnIndex(android.provider.MediaStore.Audio.Albums.ALBUM_ART)

            return Album(cursor.getLong(idColumn),
                    cursor.getString(titleColumn),
                    cursor.getString(artistColumn),
                    false, cursor.getString(albumArtColumn),
                    cursor.getInt(numOfSongsColumn))
        }
    }
}
