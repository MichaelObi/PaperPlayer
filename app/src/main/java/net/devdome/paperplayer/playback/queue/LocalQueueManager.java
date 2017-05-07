package net.devdome.paperplayer.playback.queue;

import net.devdome.paperplayer.data.model.Song;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public final class LocalQueueManager implements QueueManager {

    /**
     * Now Playing Queue
     */
    private List<QueueItem> playingQueue;

    private int currentIndex;

    private QueueActionListener queueActionListener;
    private String title;

    public LocalQueueManager() {
        playingQueue = Collections.synchronizedList(new ArrayList<QueueItem>());
        currentIndex = 0;
        title = "";
    }

    public int getCurrentIndex() {
        return currentIndex;
    }

    public void setCurrentIndex(int index) {
        if (index >= 0 && index < playingQueue.size()) {
            this.currentIndex = index;
        }
    }

    @Override
    public void setQueue(List<Song> songs, long startSongId) {
       setQueue("", songs, startSongId);
    }

    @Override
    public void setQueue(String title, List<Song> songs, long startSongId) {
        this.title = title;
        if (startSongId == 0) {
            startSongId = songs.get(0).getId();
        }
        playingQueue.clear();
        for (Song song : songs) {
            QueueItem item = new QueueItem(song, song.getId() == startSongId);
            playingQueue.add(item);
            if (song.getId() == startSongId) {
                currentIndex = playingQueue.lastIndexOf(item);
            }
        }
    }

    @Override
    public String getQueueTitle() {
        return null;
    }

    @Override
    public Song getCurrentSong() {
        return playingQueue.get(currentIndex).getSong();
    }

    @Override
    public Song next() {
        return null;
    }

    @Override
    public Song previous() {
        return null;
    }

    public void setQueueActionListener(QueueActionListener queueActionListener) {
        this.queueActionListener = queueActionListener;
    }
}