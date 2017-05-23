package xyz.michaelobi.paperplayer.playback.queue;

import java.util.List;

import xyz.michaelobi.paperplayer.data.model.Song;

/**
 * PaperPlayer Michael Obi 13 01 2017 7:06 AM
 */

public interface QueueManager {

    void setQueue(List<Song> songList, long startSongId);

    void setQueue(String title, List<Song> songList, long startSongId);

    List<QueueItem> getQueue();

    String getQueueTitle();

    Song getCurrentSong();

    Song next();

    Song previous();

    int getCurrentIndex();

    void setCurrentIndex(int index);

    boolean hasSongs();


    interface QueueActionListener {

        void onQueueSet();

        void onNext(Song nextSong);

        void onPrevious(Song nextSong);

        void onShuffle();
    }
}
