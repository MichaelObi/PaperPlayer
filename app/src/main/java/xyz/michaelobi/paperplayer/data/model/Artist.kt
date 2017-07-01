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

package xyz.michaelobi.paperplayer.data.model

import android.database.Cursor
import android.provider.MediaStore

/**
 * PaperPlayer Michael Obi 21 01 2017 8:35 AM
 */

class Artist(var id: Long, var name: String?, var numOfAlbums: Int, var numOfSongs: Int, var artistKey: String?) {
    companion object {
        fun from(musicCursor: Cursor): Artist {
            val idColumn = musicCursor.getColumnIndex(MediaStore.Audio.Artists._ID)
            val artistColumn = musicCursor.getColumnIndex(MediaStore.Audio.Artists.ARTIST)
            val numOfSongsColumn = musicCursor.getColumnIndex(MediaStore.Audio.Artists.NUMBER_OF_TRACKS)
            val numOfAlbumsColumn = musicCursor.getColumnIndex(MediaStore.Audio.Artists.NUMBER_OF_ALBUMS)
            val artistKeyColumn = musicCursor.getColumnIndex(MediaStore.Audio.Artists.ARTIST_KEY)

            val artist = Artist(musicCursor.getLong(idColumn),
                    musicCursor.getString(artistColumn),
                    musicCursor.getInt(numOfAlbumsColumn),
                    musicCursor.getInt(numOfSongsColumn),
                    musicCursor.getString(artistKeyColumn))
            return artist
        }
    }
}
