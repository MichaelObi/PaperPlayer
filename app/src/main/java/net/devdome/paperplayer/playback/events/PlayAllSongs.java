package net.devdome.paperplayer.playback.events;

/**
 * PaperPlayer Michael Obi 14 01 2017 10:33 AM
 */

public class PlayAllSongs {

    /**
     * ID of SOng to start play with
     */
    private long startSongId;

    public PlayAllSongs(long startSongId) {
        this.startSongId = startSongId;
    }

    public long getStartSongId() {
        return this.startSongId;
    }
}
