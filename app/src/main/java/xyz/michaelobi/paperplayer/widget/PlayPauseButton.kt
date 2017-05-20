package xyz.michaelobi.paperplayer.widget

import android.content.Context
import android.graphics.drawable.Drawable
import android.support.v4.content.ContextCompat
import android.support.v7.widget.AppCompatImageButton
import android.util.AttributeSet
import xyz.michaelobi.paperplayer.R

/**
 * An [AppCompatImageButton] with two playback states for media players:

 *
 * play() and pause() methods are called to alter these states updating the Image [ ]
 */
class PlayPauseButton : AppCompatImageButton {

    constructor(context: Context) : super(context)

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    fun play() {
        changeState(ContextCompat.getDrawable(context, R.drawable.ic_play_arrow_white_24dp))
    }

    fun pause() {
        changeState(ContextCompat.getDrawable(context, R.drawable.ic_pause_white_24dp))
    }

    private fun changeState(drawable: Drawable) {
        setImageDrawable(drawable)
    }
}