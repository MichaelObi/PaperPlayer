package net.devdome.paperplayer.data.library.remote.artistinfo.model.spotify;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * PaperPlayer
 * Michael Obi
 * 19 03 2017 9:18 AM
 */

public class ArtistsSearchResult {

    @Expose
    @SerializedName("items")
    private List<Artist> artists;

    public List<Artist> getArtists() {
        return artists;
    }

    public void setArtists(List<Artist> artists) {
        this.artists = artists;
    }
}
