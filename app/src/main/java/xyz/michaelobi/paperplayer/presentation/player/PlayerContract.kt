package xyz.michaelobi.paperplayer.presentation.player

import xyz.michaelobi.paperplayer.mvp.Mvp
import xyz.michaelobi.paperplayer.playback.events.PlaybackState

/**
 * PaperPlayer
 * Michael Obi
 * 06 05 2017 11:29 PM
 */
interface PlayerContract {

    interface View : Mvp.View {
        fun updatePlaybackState(playbackState: PlaybackState)
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