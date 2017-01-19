package net.devdome.paperplayer.data.model;

import java.util.ArrayList;
import java.util.List;

/**
 * PaperPlayer
 * Michael Obi
 * 22 10 2016 2:57 AM
 */

public class SongList {

    private List<Song> songs;

    public SongList(List<Song> songs) {
        this.songs = songs;
    }

    public List<Song> getSongs() {
        return songs;
    }

    public void setSongs(ArrayList<Song> songs) {
        this.songs = songs;
    }
}
