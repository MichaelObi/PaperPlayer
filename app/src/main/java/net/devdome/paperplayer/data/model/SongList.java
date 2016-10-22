package net.devdome.paperplayer.data.model;

import java.util.ArrayList;

/**
 * PaperPlayer
 * Michael Obi
 * 22 10 2016 2:57 AM
 */

public class SongList {

    private ArrayList<Song> songs;

    public SongList(ArrayList<Song> songs) {
        this.songs = songs;
    }

    public ArrayList<Song> getSongs() {
        return songs;
    }

    public void setSongs(ArrayList<Song> songs) {
        this.songs = songs;
    }
}
