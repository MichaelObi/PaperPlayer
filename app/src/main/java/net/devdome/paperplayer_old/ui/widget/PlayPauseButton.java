package net.devdome.paperplayer_old.ui.widget;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.widget.ImageButton;

import net.devdome.paperplayer_old.R;

/**
 * Created by Michael on 6/21/2016.
 */

public final class PlayPauseButton extends ImageButton {
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

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public PlayPauseButton(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
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
