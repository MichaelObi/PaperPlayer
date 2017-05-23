package xyz.michaelobi.paperplayer.playback.queue

import xyz.michaelobi.paperplayer.data.model.Song
import java.util.*

class LocalQueueManager : QueueManager {

    /**
     * Now Playing Queue
     */
    private val playingQueue: MutableList<QueueItem> = Collections.synchronizedList(ArrayList<QueueItem>())

    private var currentIndex: Int = 0

    private var queueActionListener: QueueManager.QueueActionListener? = null
    private var title: String? = null

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
<<<<<<< HEAD
        if (startSongId != 0L) firstSongId = startSongId
        playingQueue.clear()
        songs.forEach { song ->
=======
        if (startSongId != 0L) {
            firstSongId = startSongId
        }
        playingQueue.clear()
        for (song in songs) {
>>>>>>> 2e08beec3af65bc67c6f4d7157cb082bdd65b8cd
            val item = QueueItem(song, song.id == firstSongId)
            playingQueue.add(item)
            if (song.id == startSongId) {
                currentIndex = playingQueue.lastIndexOf(item)
            }
        }
    }

    override fun getQueue() = playingQueue


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

    fun setQueueActionListener(queueActionListener: QueueManager.QueueActionListener) {
        this.queueActionListener = queueActionListener
    }
}