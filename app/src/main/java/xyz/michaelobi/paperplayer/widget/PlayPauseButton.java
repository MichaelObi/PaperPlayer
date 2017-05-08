package xyz.michaelobi.paperplayer.widget;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;

import xyz.michaelobi.paperplayer.R;

/**
 * TODO: document your custom view class.
 */
public final class PlayPauseButton extends android.support.v7.widget.AppCompatImageButton {
    private final Context context;

    public PlayPauseButton(Context context) {
        super(context);
        this.context = context;
    }

    public PlayPauseButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
    }

    public PlayPauseButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
    }

    public void play() {
        changeState(ContextCompat.getDrawable(context, R.drawable.ic_play_arrow_white_24dp));
    }

    public void pause() {
        changeState(ContextCompat.getDrawable(context, R.drawable.ic_pause_white_24dp));
    }

    private void changeState(Drawable drawable) {
        setImageDrawable(drawable);
    }
}