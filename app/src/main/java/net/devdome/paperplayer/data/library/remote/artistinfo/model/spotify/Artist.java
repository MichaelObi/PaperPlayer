package net.devdome.paperplayer.data.library.remote.artistinfo.model.spotify;

import java.util.List;

/**
 * PaperPlayer
 * Michael Obi
 * 19 03 2017 8:29 AM
 */


public class Artist {
    private String id;
    private List<Image> images;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public List<Image> getImages() {
        return images;
    }

    public void setImages(List<Image> images) {
        this.images = images;
    }

}
