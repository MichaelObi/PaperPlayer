package net.devdome.paperplayer.playback.events

import net.devdome.paperplayer.data.model.Song

/**
 * PaperPlayer
 * Michael Obi
 * 08 04 2017 2:32 PM
 */
class PlaybackState(val song: Song, val playing: Boolean, val songDuration: Int, val currentDuration: Int ) {

}