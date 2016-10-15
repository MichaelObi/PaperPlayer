package net.devdome.paperplayer_old.playback;

import net.devdome.paperplayer_old.Constants;
import net.devdome.paperplayer_old.data.model.Song;

import java.util.ArrayList;
import java.util.Random;

public final class QueueManager {

    private static final String TAG = "QueueManager";

    private static QueueManager queueManager = null;

    private ArrayList<QueueItem> queue = new ArrayList<>();

    private int currentIndex = 0;

    private boolean shuffled;

    private String repeatState = Constants.REPEAT_NONE;

    private QueueManager() {
    }

    public static QueueManager getInstance() {
        if (queueManager == null) queueManager = new QueueManager();

        return queueManager;
    }

    public int getCurrentIndex() {
        return currentIndex;
    }

    public void setCurrentIndex(int index) {
        currentIndex = index;
    }

    /**
     * Generate new queue and set the queue item playback should begin with
     *
     * @param songs {@link ArrayList<QueueItem>} of {@link Song}
     * @return {@link ArrayList<QueueItem>}
     */
    public void generateQueue(ArrayList<Song> songs) {
        generateQueue(songs, 0);
    }

    /**
     * Generate new queue and set the queue item playback should begin with
     *
     * @param songs              {@link ArrayList<QueueItem>} of {@link Song}
     * @param playbackInitSongId song id of track that should be played first
     * @return {@link ArrayList<QueueItem>}
     */
    public void generateQueue(ArrayList<Song> songs, long playbackInitSongId) {
        if (playbackInitSongId == 0) {
            playbackInitSongId = songs.get(0).getId();
        }
        clearQueue();
        for (Song song : songs) {
            QueueItem item = new QueueItem(song, song.getId() == playbackInitSongId);
            queue.add(item);
            if (song.getId() == playbackInitSongId) {
                currentIndex = queue.lastIndexOf(item);
            }
        }
    }

    /**
     * Removes all items from queue
     */
    protected void clearQueue() {
        queue.clear();
    }

    /**
     * Get the queue currently loaded
     *
     * @return {@link ArrayList<QueueItem>} queue
     */
    public ArrayList<QueueItem> getCurrentQueue() {
        return this.queue;
    }


    public Song getCurrentSong() {
        return queue.get(getCurrentIndex()).getSong();
    }

    public void shuffleQueue() {
        if (!isShuffled()) {
            Random random = new Random();
            for (int i = queue.size() - 1; i > 0; i--) {

                // Get Pseudo-random number
                int index = random.nextInt(i + 1);

                // Dont swap playing track
                if (queue.get(i).isPlaying() || queue.get(index).isPlaying()) {
                    continue;
                }
                // Swap out the tracks for one another
                queue.set(index, queue.set(i, queue.get(index)));
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
     * Move Current Index to next {@link QueueItem}
     *
     * @return true if playback should continue and false if not
     */
    public boolean next(boolean invokedByUser) {
        currentIndex++;

        // Handle Repeat One
        if (!invokedByUser && getRepeatState().equals(Constants.REPEAT_ONE)) {
            currentIndex--;
        }

        // If the end of the queue has been reached
        if (currentIndex >= queue.size()) {
            currentIndex = 0;
            // Handle Repeat All
            return getRepeatState().equals(Constants.REPEAT_ALL); // should playback continue?
        }

        return true;
    }

    /**
     * Move Current Index to previous {@link QueueItem}
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
        queue.add(currentIndex + 1, new QueueItem(song));
    }

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
}