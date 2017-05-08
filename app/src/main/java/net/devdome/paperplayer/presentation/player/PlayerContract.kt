package net.devdome.paperplayer.presentation.player

import net.devdome.paperplayer.mvp.Mvp
import net.devdome.paperplayer.playback.events.PlaybackState

/**
 * PaperPlayer
 * Michael Obi
 * 06 05 2017 11:29 PM
 */
interface PlayerContract {

    interface View : Mvp.View {
        fun updatePlaybackState(isPlaying: Boolean)
        fun updateTitleAndArtist(playbackState: PlaybackState)
        fun updateSongArt(uri: String?)
        fun updateSeeker(playbackState: PlaybackState)
    }

    interface Presenter : Mvp.Presenter<View> {
        fun playPauseToggle()
        fun next()
        fun previous()
    }
}