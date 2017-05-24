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
import java.io.File

/**
 * PaperPlayer
 * Michael Obi
 * 23 10 2016 4:06 PM
 */

class Album(val id: Long, val title: String, val artist: String, val isFavorite: Boolean, var artPath: String?, val numberOfSongs: Int) {

    val art: File?
        get() = artPath?.let { File(it) }

    companion object {

        fun from(cursor: Cursor): Album {
            val titleColumn = cursor.getColumnIndex(MediaStore.Audio.Albums.ALBUM)
            val idColumn = cursor.getColumnIndex(android.provider.MediaStore.Audio.Albums._ID)
            val artistColumn = cursor.getColumnIndex(android.provider.MediaStore.Audio.Albums.ARTIST)
            val numOfSongsColumn = cursor.getColumnIndex(android.provider.MediaStore.Audio.Albums.NUMBER_OF_SONGS)
            val albumArtColumn = cursor.getColumnIndex(android.provider.MediaStore.Audio.Albums.ALBUM_ART)

            return Album(cursor.getLong(idColumn),
                    cursor.getString(titleColumn),
                    cursor.getString(artistColumn),
                    false, cursor.getString(albumArtColumn),
                    cursor.getInt(numOfSongsColumn))
        }
    }
}
