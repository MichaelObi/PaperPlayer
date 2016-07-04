package net.devdome.paperplayer.utils;

import android.animation.ValueAnimator;
import android.graphics.Color;
import android.view.View;

/**
 * Created by Michael on 6/20/2016.
 */

public class ViewUtils {

    public static void crossColors(final View view, int initialColor, int finalColor) {
        final float[] from = new float[3],
                to = new float[3];
        String hexColorFrom = String.format("#%06X", (0xFFFFFF & initialColor));
        String hexColorTo = String.format("#%06X", (0xFFFFFF & finalColor));

        Color.colorToHSV(Color.parseColor(hexColorFrom), from);
        Color.colorToHSV(Color.parseColor(hexColorTo), to);

        final ValueAnimator animator = ValueAnimator.ofFloat(0, 1);
        animator.setDuration(300);

        final float[] hsv = new float[3];
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                hsv[0] = from[0] + (to[0] - from[0]) * animator.getAnimatedFraction();
                hsv[1] = from[1] + (to[1] - from[1]) * animator.getAnimatedFraction();
                hsv[2] = from[2] + (to[2] - from[2]) * animator.getAnimatedFraction();
                view.setBackgroundColor(Color.HSVToColor(hsv));
            }
        });
        animator.start();
    }
}
