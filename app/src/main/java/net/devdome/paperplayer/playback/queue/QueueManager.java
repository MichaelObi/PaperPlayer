package net.devdome.paperplayer.playback.queue;

import net.devdome.paperplayer.data.model.Song;

import java.util.List;

/**
 * PaperPlayer Michael Obi 13 01 2017 7:06 AM
 */

public interface QueueManager {

    void setQueue(List<Song> songList, long startSongId);

    void setQueue(String title, List<Song> songList, long startSongId);

    String getQueueTitle();

    Song getCurrentSong();

    Song next();

    Song previous();

    int getCurrentIndex();

    void setCurrentIndex(int index);


    public interface QueueActionListener {

        void onQueueSet();

        void onNext(Song nextSong);

        void onPrevious(Song nextSong);

        void onShuffle();
    }
}
