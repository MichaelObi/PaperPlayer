package xyz.michaelobi.paperplayer.presentation.musiclibrary.fragment.miniplayer

import xyz.michaelobi.paperplayer.mvp.Mvp
import xyz.michaelobi.paperplayer.playback.events.PlaybackState

/**
 * PaperPlayer
 * Michael Obi
 * 08 04 2017 10:43 AM
 */
interface MiniPlayerContract {

    interface View : Mvp.View {
        fun updatePlayPauseButton(isPlaying: Boolean)
        fun updateTitleAndArtist(playbackState: PlaybackState)
        fun updateSongArt(uri: String?)
    }

    interface Presenter : Mvp.Presenter<View> {
        fun initialize()

        fun onPlayButtonClicked()

        fun onPlayerStateUpdate()
    }
}