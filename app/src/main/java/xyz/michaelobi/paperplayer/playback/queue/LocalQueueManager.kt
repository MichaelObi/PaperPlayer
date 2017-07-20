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

package xyz.michaelobi.paperplayer.playback.queue

import rx.Observable
import xyz.michaelobi.paperplayer.data.model.Song
import java.util.*

class LocalQueueManager : QueueManager {

    /**
     * Now Playing Queue
     */
    private var playingQueue = mutableListOf<QueueItem>()
    private var originalQueue = mutableListOf<QueueItem>()
    private var currentIndex: Int = 0

    private var queueActionListener: QueueManager.QueueActionListener? = null
    private var title: String? = null

    private var shuffled = false

    init {
        currentIndex = 0
        title = ""
    }

    override fun getCurrentIndex() = currentIndex

    override fun setCurrentIndex(index: Int) {
        if (index >= 0 && index < playingQueue.size) {
            this.currentIndex = index
        }
    }

    override fun setQueue(songs: List<Song>, startSongId: Long) = setQueue("", songs, startSongId)

    override fun setQueue(title: String, songs: List<Song>, startSongId: Long) {
        this.title = title
        var firstSongId = songs[0].id
        if (startSongId != 0L) firstSongId = startSongId
        playingQueue.clear()
        songs.forEach { song ->
            val item = QueueItem(song)
            playingQueue.add(item)
            if (song.id == startSongId) {
                currentIndex = playingQueue.lastIndexOf(item)
            }
        }
    }

    override fun getQueue(): Observable<MutableList<QueueItem>> = Observable.just(playingQueue)

    override fun getQueueTitle(): String? = null

    override fun getCurrentSong(): Song? {
        if (currentIndex >= playingQueue.size) {
            return null
        }
        return playingQueue[currentIndex].song
    }

    override fun next(): Song? {
        currentIndex++
        if (currentIndex >= playingQueue.size) {
            currentIndex = 0
            return null
        }
        return playingQueue[currentIndex].song
    }

    override fun previous(): Song? {
        currentIndex--
        if (currentIndex < 0) {
            currentIndex = 0
        }
        return playingQueue[currentIndex].song
    }

    override fun hasSongs() = playingQueue.size > 0

    override fun toggleShuffle(): Boolean {
        if (!shuffled) {
            shuffle()
        } else {
            unshuffle()
        }
        return shuffled
    }

    override fun isShuffled() = shuffled

    private fun shuffle() {
        val currentSongId = currentSong?.id
        originalQueue = playingQueue.toMutableList()
        val random = Random()
        for (i in playingQueue.size - 1 downTo 0) {
            // Get Pseudo-random number
            val index = random.nextInt(i + 1)
            if (playingQueue[index].song.id == currentSongId || playingQueue[i].song.id == currentSongId) {
                // Dont swap playing track
                continue
            }
            // Swap out the tracks for one another
            playingQueue[index] = playingQueue.set(i, playingQueue[index])
        }
        shuffled = true
    }

    private fun unshuffle() {
        val currentSongId = currentSong?.id ?: -1L
        if (currentSongId != -1L) {
            playingQueue = originalQueue.toMutableList()
            for (i in playingQueue.size - 1 downTo 0) {
                val queueItem = playingQueue[i]
                if (queueItem.song.id == currentSongId) {
                    currentIndex = i
                }
            }
        }
        shuffled = false
    }

    fun setQueueActionListener(queueActionListener: QueueManager.QueueActionListener) {
        this.queueActionListener = queueActionListener
    }

}