/*
 * MIT License
 *
 * Copyright (c) 2017 MIchael Obi
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package xyz.michaelobi.paperplayer.presentation.album

import xyz.michaelobi.paperplayer.data.model.Album
import xyz.michaelobi.paperplayer.data.model.Song
import xyz.michaelobi.paperplayer.mvp.DataView
import xyz.michaelobi.paperplayer.mvp.Mvp
import java.io.File

/**
 * PaperPlayer
 * Michael Obi
 * 06 09 2017 7:37 PM
 */
interface AlbumContract {
    interface View : DataView {
        fun showAlbumArt(albumArt: File)

        fun showAlbumSongList(songs: List<Song>)

        fun showReleaseYear(year: String?)

        fun showSongCount(numberOfSongs: Int)
    }

    interface Presenter : Mvp.Presenter<View> {
        fun loadAlbum(albumId: Long)
    }
}