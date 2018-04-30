package xyz.michaelobi.paperplayer.presentation.musiclibrary.fragment.songs

import xyz.michaelobi.paperplayer.data.model.Song
import xyz.michaelobi.paperplayer.mvp.DataView
import xyz.michaelobi.paperplayer.mvp.Mvp

/**
 * PaperPlayer
 * Michael Obi
 * 30/04/2018, 9:01 PM
 */
interface SongsView : DataView {

    fun showList(songs: MutableList<Song>)
}