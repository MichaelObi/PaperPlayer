package xyz.michaelobi.paperplayer.data.model

import android.database.Cursor
import android.provider.MediaStore

/**
 * PaperPlayer Michael Obi 21 01 2017 8:35 AM
 */

class Artist(var id: Long, var name: String?, var numOfAlbums: Int, var numOfSongs: Int, var artistKey: String?) {
    companion object {
        fun from(musicCursor: Cursor): Artist {
            val idColumn = musicCursor.getColumnIndex(MediaStore.Audio.Artists._ID)
            val artistColumn = musicCursor.getColumnIndex(MediaStore.Audio.Artists.ARTIST)
            val numOfSongsColumn = musicCursor.getColumnIndex(MediaStore.Audio.Artists.NUMBER_OF_TRACKS)
            val numOfAlbumsColumn = musicCursor.getColumnIndex(MediaStore.Audio.Artists.NUMBER_OF_ALBUMS)
            val artistKeyColumn = musicCursor.getColumnIndex(MediaStore.Audio.Artists.ARTIST_KEY)

            val artist = Artist(musicCursor.getLong(idColumn),
                    musicCursor.getString(artistColumn),
                    musicCursor.getInt(numOfAlbumsColumn),
                    musicCursor.getInt(numOfSongsColumn),
                    musicCursor.getString(artistKeyColumn))
            return artist
        }
    }
}
