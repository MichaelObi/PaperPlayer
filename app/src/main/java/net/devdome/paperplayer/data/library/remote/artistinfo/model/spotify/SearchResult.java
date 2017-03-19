package net.devdome.paperplayer.data.library.remote.artistinfo.model.spotify;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * PaperPlayer
 * Michael Obi
 * 19 03 2017 9:06 AM
 */

public class SearchResult {
    @SerializedName("artists")
    @Expose
    private ArtistsSearchResult artistsSearchResult;

    public ArtistsSearchResult getArtistsSearchResult() {
        return artistsSearchResult;
    }

    public void setArtistsSearchResult(ArtistsSearchResult artistsSearchResult) {
        this.artistsSearchResult = artistsSearchResult;
    }

    public List<Artist> getArtists() {
        return artistsSearchResult.getArtists();
    }
}
