package xyz.michaelobi.paperplayer.data.model

import android.database.Cursor
import android.provider.MediaStore

/**
 * PaperPlayer
 * Michael Obi
 * 15 10 2016 3:23 PM
 */

class Song {

    var title: String? = null
    var album: String? = null
    var artist: String? = null
    var year: String? = null
    var songUri: String? = null
    var id: Long = 0
    var albumId: Long = 0

    constructor()

    constructor(id: Long, title: String, album: String, artist: String, year: String, songUri: String) {
        this.id = id
        this.title = title
        this.album = album
        this.artist = artist
        this.year = year
        this.songUri = songUri
    }

    companion object {
        fun from(cursor: Cursor): Song {
            val titleColumn = cursor.getColumnIndex(android.provider.MediaStore.Audio.Media.TITLE)
            val idColumn = cursor.getColumnIndex(android.provider.MediaStore.Audio.Media._ID)
            val artistColumn = cursor.getColumnIndex(android.provider.MediaStore.Audio.Media.ARTIST)
            val pathColumn = cursor.getColumnIndex(MediaStore.Audio.Media.DATA)
            val albumIdColumn = cursor.getColumnIndex(MediaStore.Audio.Media.ALBUM_ID)
            val albumColumn = cursor.getColumnIndex(MediaStore.Audio.Media.ALBUM)
            val song = Song(
                    cursor.getLong(idColumn),
                    cursor.getString(titleColumn),
                    cursor.getString(albumColumn),
                    cursor.getString(artistColumn),
                    "",
                    cursor.getString(pathColumn)
            )
            song.albumId = cursor.getLong(albumIdColumn)
            return song
        }
    }
}
