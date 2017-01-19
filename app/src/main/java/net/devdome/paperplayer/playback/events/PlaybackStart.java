package net.devdome.paperplayer.playback.events;

import net.devdome.paperplayer.data.model.Song;

/**
 * PaperPlayer
 * Michael Obi
 * 12 01 2017 7:09 AM
 */

public class PlaybackStart {

    private final Song song;

    public PlaybackStart(Song song) {
        this.song = song;
    }

    public Song getSong() {
        return this.song;
    }
}
