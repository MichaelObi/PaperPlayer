package net.devdome.paperplayer_old;

public final class Constants {

    // Last.FM API
    //TODO: Move sensitive data to a file that wont be pushed up
    public static final String LAST_FM_API_KEY = "153b4614e938797c4ebc0f69246942e9";

    // Song Properties Keys
    public static final String SONG_ID = "song_id";
    public static final String SONG_PATH = "song_path";
    public static final String SONG_NAME = "song_name";
    public static final String SONG_ALBUM_NAME = "song_album_name";
    public static final String SONG_ART = "song_art";
    public static final String SONG_ALBUM_ID = "song_album_id";
    public static final String SONG_ARTIST = "song_artist";
    public static final String SONG_DURATION = "song_duration";
    public static final String SONG_CURRENT_TIME = "song_current_time";

    // Action Keys
    public static final String ACTION_PAUSE = "pause";
    public static final String ACTION_NEXT = "next_song";
    public static final String ACTION_SEEK_TO = "player_seek_to_song";
    public static final String ACTION_PLAY_ALL = "play_all";
    public static final String ACTION_VIEW_NOW_PLAYING = "view_now_playing";
    public static final String ACTION_SEEK_GET = "action_get_seek";
    public static final String ACTION_PLAY_ALBUM = "play_album";
    public static final String ACTION_REQUEST_PLAY_STATE = "request_play_state";
    public static final String ACTION_PREVIOUS = "previous_song";
    public static final String ACTION_SHUFFLE = "shuffle";
    public static final String ACTION_REPEAT = "repeat";


    public static final String KEY_REPEAT_STATE_EXTRA = "repeat_state";



    // Song ID of track to start with
    public static final String KEY_PLAY_START_WITH = "start_with";
    public static final String KEY_SEEK_CHANGE = "change_seek";
    public static final String KEY_SONG_SEEK_VALUE = "song_seek_value";
    public static final String KEY_IS_PLAYING = "is_playing";
    public static final int NOTIFICATION_ID = 127;
    public static final String MEDIA_SESSION_TAG = "net.devdome.player.session";

    // Repeat States
    public static final String REPEAT_NONE = "repeat_none";
    public static final String REPEAT_ALL = "repeat_all";
    public static final String REPEAT_ONE = "repeat_one";

}
