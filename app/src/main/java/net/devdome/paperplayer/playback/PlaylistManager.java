package net.devdome.paperplayer.playback;

import net.devdome.paperplayer.Constants;
import net.devdome.paperplayer.data.model.Song;

import java.util.ArrayList;
import java.util.Random;

public final class PlaylistManager {

    private static final String TAG = "PlaylistManager";

    private static PlaylistManager playlistManager = null;

    private ArrayList<PlaylistItem> playlist = new ArrayList<>();

    private int currentIndex = 0;

    private boolean shuffled;

    private String repeatState;

    private PlaylistManager() {
    }

    public static PlaylistManager getInstance() {
        if (playlistManager == null)
            playlistManager = new PlaylistManager();

        return playlistManager;
    }

    public int getCurrentIndex() {
        return currentIndex;
    }

    public void setCurrentIndex(int index) {
        currentIndex = index;
    }

    /**
     * Generate new playlist and set the playlist item playback should begin with
     *
     * @param songs {@link ArrayList<PlaylistItem>} of {@link Song}
     * @return {@link ArrayList<PlaylistItem>}
     */
    public void generatePlaylist(ArrayList<Song> songs) {
        generatePlaylist(songs, 0);
    }

    /**
     * Generate new playlist and set the playlist item playback should begin with
     *
     * @param songs              {@link ArrayList<PlaylistItem>} of {@link Song}
     * @param playbackInitSongId song id of track that should be played first
     * @return {@link ArrayList<PlaylistItem>}
     */
    public void generatePlaylist(ArrayList<Song> songs, long playbackInitSongId) {
        if (playbackInitSongId == 0) {
            playbackInitSongId = songs.get(0).getId();
        }
        clearPlaylist();
        for (Song song : songs) {
            PlaylistItem item = new PlaylistItem(song, song.getId() == playbackInitSongId);
            playlist.add(item);
            if (song.getId() == playbackInitSongId) {
                currentIndex = playlist.lastIndexOf(item);
            }
        }
    }

    /**
     * Removes all items from playlist
     */
    protected void clearPlaylist() {
        playlist.clear();
    }

    /**
     * Get the playlist currently loaded
     *
     * @return {@link ArrayList<PlaylistItem>} playlist
     */
    public ArrayList<PlaylistItem> getCurrentPlaylist() {
        return this.playlist;
    }


    public Song getCurrentSong() {
        return playlist.get(getCurrentIndex()).getSong();
    }

    public void shufflePlaylist() {
        if (!isShuffled()) {
            Random random = new Random();
            for (int i = playlist.size() - 1; i > 0; i--) {

                // Get Pseudo-random number
                int index = random.nextInt(i + 1);

                // Dont swap playing track
                if (playlist.get(i).isPlaying() || playlist.get(index).isPlaying()) {
                    continue;
                }
                // Swap out the tracks for one another
                playlist.set(index, playlist.set(i, playlist.get(index)));
            }
            setShuffled(true);
        } else {
            setShuffled(false);
        }
    }

    public boolean isShuffled() {
        return shuffled;
    }

    public void setShuffled(boolean shuffled) {
        this.shuffled = shuffled;
    }

    /**
     * Move Current Index to next {@link PlaylistItem}
     *
     * @return true if playback should continue and false if not
     */
    public boolean next(boolean invokedByUser) {
        currentIndex++;

        // Handle Repeat One
        if (!invokedByUser && repeatState.equals(Constants.REPEAT_ONE)) {
            currentIndex--;
        }

        // If the end of the playlist has been reached
        if (currentIndex >= playlist.size()) {
            currentIndex = 0;
            // Handle Repeat All
            return repeatState.equals(Constants.REPEAT_ALL); // should playback continue?
        }

        return true;
    }

    /**
     * Move Current Index to previous {@link PlaylistItem}
     */
    public void previous() {
        currentIndex--;
        if (currentIndex < 0) {
            currentIndex = 0;
        }
    }

    public String getRepeatState() {
        return repeatState;
    }

    public void setRepeatState(String repeatState) {
        this.repeatState = repeatState;
    }

    /**
     * Set the song to play after the currently playing song
     */
    public void setUpNext(Song song) {
        playlist.add(currentIndex + 1, new PlaylistItem(song));
    }

    /**
     * A song in a playlist to improve SoC
     */
    public class PlaylistItem {
        private Song song;
        private boolean isPlaying = false;

        public PlaylistItem() {
        }

        public PlaylistItem(Song song) {
            this.song = song;
        }

        PlaylistItem(Song song, boolean isPlaying) {
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
}