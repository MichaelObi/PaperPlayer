package xyz.michaelobi.paperplayer.playback.events

import xyz.michaelobi.paperplayer.data.model.Song

/**
 * PaperPlayer
 * Michael Obi
 * 08 04 2017 2:32 PM
 */
class PlaybackState(val song: Song, val playing: Boolean, val songDuration: Int, val currentDuration: Int ) {

}