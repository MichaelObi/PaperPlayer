package xyz.michaelobi.paperplayer.presentation.musiclibrary.fragment.artist

import xyz.michaelobi.paperplayer.data.model.Artist
import xyz.michaelobi.paperplayer.mvp.DataView

/**
 * PaperPlayer
 * Michael Obi
 * 30/04/2018, 10:21 PM
 */
interface ArtistsView : DataView {
    fun showList(artists: List<Artist>)
}