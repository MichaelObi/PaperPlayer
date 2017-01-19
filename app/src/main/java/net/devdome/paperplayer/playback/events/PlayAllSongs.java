package net.devdome.paperplayer.playback.events;

import android.util.Log;

/**
 * PaperPlayer Michael Obi 14 01 2017 10:33 AM
 */

public class PlayAllSongs {
    private static final String TAG = "PlayAllSongs";
    /**
     * ID of SOng to start play with
     */
    private long startSongId;

    public PlayAllSongs(long startSongId) {
        this.startSongId = startSongId;
    }

    public long getStartSongId() {
        Log.d(TAG, "getStartSongId() called");
        return this.startSongId;
    }
}
