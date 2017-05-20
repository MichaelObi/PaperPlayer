package xyz.michaelobi.paperplayer.data.model

import java.util.*

/**
 * PaperPlayer
 * Michael Obi
 * 22 10 2016 2:57 AM
 */

class SongList(songs: List<Song>) {

    var songs: List<Song>? = null
        private set

    init {
        this.songs = songs
    }

    fun setSongs(songs: ArrayList<Song>) {
        this.songs = songs
    }
}
