package xyz.michaelobi.paperplayer.playback.queue;

import xyz.michaelobi.paperplayer.data.model.Song;

/**
 * A song in a queue to improve SoC
 */
public class QueueItem {
    private Song song;
    private boolean isPlaying = false;

    public QueueItem() {
    }

    public QueueItem(Song song) {
        this.song = song;
    }

    QueueItem(Song song, boolean isPlaying) {
        this.song = song;
        this.isPlaying = isPlaying;
    }

    public Song getSong() {
        return song;
    }

    public void setSong(Song song) {
        this.song = song;
    }

    public boolean isPlaying() {
        return isPlaying;
    }

    public void setPlaying(boolean playing) {
        isPlaying = playing;
    }
}