package xyz.michaelobi.paperplayer.presentation.musiclibrary.fragment.albums

import xyz.michaelobi.paperplayer.data.model.Album
import xyz.michaelobi.paperplayer.mvp.DataView
import xyz.michaelobi.paperplayer.mvp.Mvp

/**
 * PaperPlayer
 * Michael Obi
 * 30/04/2018, 9:17 PM
 */
interface AlbumsView : DataView {
    fun showList(albums: List<Album>)
}