package net.devdome.paperplayer.playback.events.action

/**
 * PaperPlayer Michael Obi 14 01 2017 10:33 AM
 */

class PlayAllSongs(private val startSongId: Long) {

    fun getStartSongId(): Long {
        return this.startSongId
    }
}
