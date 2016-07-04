package net.devdome.paperplayer.ui;


import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by mahes on 19/8/15.
 */
public class MaterialPlayPauseButton extends View {

    public static final int PLAY = 0;
    public static final int PAUSE = 1;
    public int animDuration = 300;
    int width;
    int height;
    int actualDrawWidth;
    int centerWidth;
    int centerHeight;
    int state;
    int LEFT_PLAY_PAUSE_COMMON_LEFT_TOP_EDGE_X = 0;
    int LEFT_PLAY_PAUSE_COMMON_LEFT_TOP_EDGE_Y = 0;

    int LEFT_PLAY_PAUSE_COMMON_LEFT_BOTTOM_EDGE_X = 0;
    int LEFT_PLAY_PAUSE_COMMON_LEFT_BOTTOM_EDGE_Y = 0;

    int LEFT_PLAY_RIGHT_TOP_EDGE_X = 0;
    int LEFT_PLAY_RIGHT_TOP_EDGE_Y = 0;

    int LEFT_PLAY_RIGHT_BOTTOM_EDGE_X = 0;
    int LEFT_PLAY_RIGHT_BOTTOM_EDGE_Y = 0;

    int LEFT_PAUSE_RIGHT_TOP_EDGE_X = 0;
    int LEFT_PAUSE_RIGHT_TOP_EDGE_Y = 0;

    int LEFT_PAUSE_RIGHT_BOTTOM_EDGE_X = 0;
    int LEFT_PAUSE_RIGHT_BOTTOM_EDGE_Y = 0;

    int leftRightTopEdgeX = 0;
    int leftRightTopEdgeY = 0;

    int leftRightBottomEdgeX = 0;
    int leftRightBottomEdgeY = 0;


    int RIGHT_PLAY_LEFT_TOP_EDGE_X = 0;
    int RIGHT_PLAY_LEFT_TOP_EDGE_Y = 0;

    int RIGHT_PLAY_RIGHT_TOP_EDGE_X = 0;
    int RIGHT_PLAY_RIGHT_TOP_EDGE_Y = 0;

    int RIGHT_PLAY_RIGHT_BOTTOM_EDGE_X = 0;
    int RIGHT_PLAY_RIGHT_BOTTOM_EDGE_Y = 0;

    int RIGHT_PLAY_LEFT_BOTTOM_EDGE_X = 0;
    int RIGHT_PLAY_LEFT_BOTTOM_EDGE_Y = 0;

    int RIGHT_PAUSE_LEFT_TOP_EDGE_X = 0;
    int RIGHT_PAUSE_LEFT_TOP_EDGE_Y = 0;

    int RIGHT_PAUSE_RIGHT_TOP_EDGE_X = 0;
    int RIGHT_PAUSE_RIGHT_TOP_EDGE_Y = 0;

    int RIGHT_PAUSE_RIGHT_BOTTOM_EDGE_X = 0;
    int RIGHT_PAUSE_RIGHT_BOTTOM_EDGE_Y = 0;

    int RIGHT_PAUSE_LEFT_BOTTOM_EDGE_X = 0;
    int RIGHT_PAUSE_LEFT_BOTTOM_EDGE_Y = 0;

    int rightLeftTopEdgeX;
    int rightLeftTopEdgeY;

    int rightRightTopEdgeX;
    int rightRightTopEdgeY;

    int rightRightBottomEdgeX;
    int rightRightBottomEdgeY;

    int rightLeftBottomEdgeX;
    int rightLeftBottomEdgeY;


    Paint mPaint;

    public MaterialPlayPauseButton(Context context) {
        super(context);
        init();
    }

    public MaterialPlayPauseButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public MaterialPlayPauseButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    void init() {

        mPaint = new Paint();

        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setColor(0xFFEEEEEE);
        mPaint.setAntiAlias(true);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        width = getLayoutParams().width;
        height = getLayoutParams().height;

        actualDrawWidth = width < height ? width : height;

        centerHeight = height / 2;
        centerWidth = width / 2;

        state = PLAY;

        LEFT_PLAY_PAUSE_COMMON_LEFT_TOP_EDGE_X = centerWidth - (actualDrawWidth * 50 / 100);
        LEFT_PLAY_PAUSE_COMMON_LEFT_TOP_EDGE_Y = centerHeight - (actualDrawWidth * 50 / 100);

        LEFT_PLAY_PAUSE_COMMON_LEFT_BOTTOM_EDGE_X = centerWidth - (actualDrawWidth * 50 / 100);
        LEFT_PLAY_PAUSE_COMMON_LEFT_BOTTOM_EDGE_Y = centerHeight + (actualDrawWidth * 50 / 100);


        LEFT_PLAY_RIGHT_TOP_EDGE_X = centerWidth;
        LEFT_PLAY_RIGHT_TOP_EDGE_Y = centerHeight - (actualDrawWidth * 25 / 100);

        LEFT_PLAY_RIGHT_BOTTOM_EDGE_X = centerWidth;
        LEFT_PLAY_RIGHT_BOTTOM_EDGE_Y = centerHeight + (actualDrawWidth * 25 / 100);

        LEFT_PAUSE_RIGHT_TOP_EDGE_X = centerWidth - (actualDrawWidth * 15 / 100);
        LEFT_PAUSE_RIGHT_TOP_EDGE_Y = centerHeight - (actualDrawWidth * 50 / 100);

        LEFT_PAUSE_RIGHT_BOTTOM_EDGE_X = centerWidth - (actualDrawWidth * 15 / 100);
        LEFT_PAUSE_RIGHT_BOTTOM_EDGE_Y = centerHeight + (actualDrawWidth * 50 / 100);

        leftRightTopEdgeX = LEFT_PLAY_RIGHT_TOP_EDGE_X;
        leftRightTopEdgeY = LEFT_PLAY_RIGHT_TOP_EDGE_Y;

        leftRightBottomEdgeX = LEFT_PLAY_RIGHT_BOTTOM_EDGE_X;
        leftRightBottomEdgeY = LEFT_PLAY_RIGHT_BOTTOM_EDGE_Y;

        RIGHT_PAUSE_LEFT_TOP_EDGE_X = centerWidth + (actualDrawWidth * 15 / 100);
        RIGHT_PAUSE_LEFT_TOP_EDGE_Y = centerHeight - (actualDrawWidth * 50 / 100);

        RIGHT_PAUSE_RIGHT_TOP_EDGE_X = centerWidth + (actualDrawWidth * 50 / 100);
        RIGHT_PAUSE_RIGHT_TOP_EDGE_Y = centerHeight - (actualDrawWidth * 50 / 100);

        RIGHT_PAUSE_RIGHT_BOTTOM_EDGE_X = centerWidth + (actualDrawWidth * 50 / 100);
        RIGHT_PAUSE_RIGHT_BOTTOM_EDGE_Y = centerHeight + (actualDrawWidth * 50 / 100);

        RIGHT_PAUSE_LEFT_BOTTOM_EDGE_X = centerWidth + (actualDrawWidth * 15 / 100);
        RIGHT_PAUSE_LEFT_BOTTOM_EDGE_Y = centerHeight + (actualDrawWidth * 50 / 100);

        RIGHT_PLAY_LEFT_TOP_EDGE_X = centerWidth;
        RIGHT_PLAY_LEFT_TOP_EDGE_Y = centerHeight - (actualDrawWidth * 25 / 100);

        RIGHT_PLAY_RIGHT_TOP_EDGE_X = centerWidth + (actualDrawWidth * 50 / 100);
        RIGHT_PLAY_RIGHT_TOP_EDGE_Y = centerHeight;

        RIGHT_PLAY_RIGHT_BOTTOM_EDGE_X = centerWidth + (actualDrawWidth * 50 / 100);
        RIGHT_PLAY_RIGHT_BOTTOM_EDGE_Y = centerHeight;

        RIGHT_PLAY_LEFT_BOTTOM_EDGE_X = centerWidth;
        RIGHT_PLAY_LEFT_BOTTOM_EDGE_Y = centerHeight + (actualDrawWidth * 25 / 100);

        rightLeftTopEdgeX = RIGHT_PLAY_LEFT_TOP_EDGE_X;
        rightLeftTopEdgeY = RIGHT_PLAY_LEFT_TOP_EDGE_Y;

        rightRightTopEdgeX = RIGHT_PLAY_RIGHT_TOP_EDGE_X;
        rightRightTopEdgeY = RIGHT_PLAY_RIGHT_TOP_EDGE_Y;

        rightRightBottomEdgeX = RIGHT_PLAY_RIGHT_BOTTOM_EDGE_X;
        rightRightBottomEdgeY = RIGHT_PLAY_RIGHT_BOTTOM_EDGE_Y;

        rightLeftBottomEdgeX = RIGHT_PLAY_LEFT_BOTTOM_EDGE_X;
        rightLeftBottomEdgeY = RIGHT_PLAY_LEFT_BOTTOM_EDGE_Y;

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        Path wallPlayLeft = new Path();
        wallPlayLeft.reset();
        wallPlayLeft.moveTo(LEFT_PLAY_PAUSE_COMMON_LEFT_TOP_EDGE_X, LEFT_PLAY_PAUSE_COMMON_LEFT_TOP_EDGE_Y);
        wallPlayLeft.lineTo(leftRightTopEdgeX, leftRightTopEdgeY);
        wallPlayLeft.lineTo(leftRightBottomEdgeX, leftRightBottomEdgeY);
        wallPlayLeft.lineTo(LEFT_PLAY_PAUSE_COMMON_LEFT_BOTTOM_EDGE_X, LEFT_PLAY_PAUSE_COMMON_LEFT_BOTTOM_EDGE_Y);
        wallPlayLeft.lineTo(LEFT_PLAY_PAUSE_COMMON_LEFT_TOP_EDGE_X, LEFT_PLAY_PAUSE_COMMON_LEFT_TOP_EDGE_Y);


        Path wallPlayRight = new Path();
        wallPlayRight.reset();
        wallPlayRight.moveTo(rightLeftTopEdgeX, rightLeftTopEdgeY);
        wallPlayRight.lineTo(rightRightTopEdgeX, rightRightTopEdgeY);
        wallPlayRight.lineTo(rightRightBottomEdgeX, rightRightBottomEdgeY);
        wallPlayRight.lineTo(rightLeftBottomEdgeX, rightLeftBottomEdgeY);
        wallPlayRight.lineTo(rightLeftTopEdgeX, rightLeftTopEdgeY);

        canvas.drawPath(wallPlayLeft, mPaint);
        canvas.drawPath(wallPlayRight, mPaint);
    }

    public void setToPlay() {

        if (state == PAUSE) {
            state = PLAY;
        } else {
            return;
        }

        leftPlayAnimation();

        rightPlayAnimation();

    }

    void rightPlayAnimation() {

        ObjectAnimator rightLeftTopEdgeXAnim = ObjectAnimator.ofInt(this, "rightLeftTopEdgeX", rightLeftTopEdgeX, RIGHT_PLAY_LEFT_TOP_EDGE_X);
        rightLeftTopEdgeXAnim.setDuration(animDuration);
        rightLeftTopEdgeXAnim.start();

        ObjectAnimator rightLeftTopEdgeYAnim = ObjectAnimator.ofInt(this, "rightLeftTopEdgeY", rightLeftTopEdgeY, RIGHT_PLAY_LEFT_TOP_EDGE_Y);
        rightLeftTopEdgeYAnim.setDuration(animDuration);
        rightLeftTopEdgeYAnim.start();

        ObjectAnimator rightRightTopEdgeXAnim = ObjectAnimator.ofInt(this, "rightRightTopEdgeX", rightRightTopEdgeX, RIGHT_PLAY_RIGHT_TOP_EDGE_X);
        rightRightTopEdgeXAnim.setDuration(animDuration);
        rightRightTopEdgeXAnim.start();

        ObjectAnimator rightRightTopEdgeYAnim = ObjectAnimator.ofInt(this, "rightRightTopEdgeY", rightRightTopEdgeY, RIGHT_PLAY_RIGHT_TOP_EDGE_Y);
        rightRightTopEdgeYAnim.setDuration(animDuration);
        rightRightTopEdgeYAnim.start();

        ObjectAnimator rightLeftBottomEdgeXAnim = ObjectAnimator.ofInt(this, "rightLeftBottomEdgeX", rightLeftBottomEdgeX, RIGHT_PLAY_LEFT_BOTTOM_EDGE_X);
        rightLeftBottomEdgeXAnim.setDuration(animDuration);
        rightLeftBottomEdgeXAnim.start();

        ObjectAnimator rightLeftBottomEdgeYAnim = ObjectAnimator.ofInt(this, "rightLeftBottomEdgeY", rightLeftBottomEdgeY, RIGHT_PLAY_LEFT_BOTTOM_EDGE_Y);
        rightLeftBottomEdgeYAnim.setDuration(animDuration);
        rightLeftBottomEdgeYAnim.start();

        ObjectAnimator rightRightBottomEdgeXAnim = ObjectAnimator.ofInt(this, "rightRightBottomEdgeX", rightRightBottomEdgeX, RIGHT_PLAY_RIGHT_BOTTOM_EDGE_X);
        rightRightBottomEdgeXAnim.setDuration(animDuration);
        rightRightBottomEdgeXAnim.start();

        ObjectAnimator rightRightBottomEdgeYAnim = ObjectAnimator.ofInt(this, "rightRightBottomEdgeY", rightRightBottomEdgeY, RIGHT_PLAY_RIGHT_BOTTOM_EDGE_Y);
        rightRightBottomEdgeYAnim.setDuration(animDuration);
        rightRightBottomEdgeYAnim.start();
    }

    void leftPlayAnimation() {

        ObjectAnimator leftRightTopEdgeXAnim = ObjectAnimator.ofInt(this, "leftRightTopEdgeX", leftRightTopEdgeX, LEFT_PLAY_RIGHT_TOP_EDGE_X);
        leftRightTopEdgeXAnim.setDuration(animDuration);
        leftRightTopEdgeXAnim.start();

        ObjectAnimator leftRightTopEdgeYAnim = ObjectAnimator.ofInt(this, "leftRightTopEdgeY", leftRightTopEdgeY, LEFT_PLAY_RIGHT_TOP_EDGE_Y);
        leftRightTopEdgeYAnim.setDuration(animDuration);
        leftRightTopEdgeYAnim.start();

        ObjectAnimator leftRightBottomEdgeXAnim = ObjectAnimator.ofInt(this, "leftRightBottomEdgeX", leftRightBottomEdgeX, LEFT_PLAY_RIGHT_BOTTOM_EDGE_X);
        leftRightBottomEdgeXAnim.setDuration(animDuration);
        leftRightBottomEdgeXAnim.start();

        ObjectAnimator leftRightBottomEdgeYAnim = ObjectAnimator.ofInt(this, "leftRightBottomEdgeY", leftRightBottomEdgeY, LEFT_PLAY_RIGHT_BOTTOM_EDGE_Y);
        leftRightBottomEdgeYAnim.setDuration(animDuration);
        leftRightBottomEdgeYAnim.start();
    }

    public void setToPause() {

        if (state == PLAY) {
            state = PAUSE;
        } else {
            return;
        }

        leftPauseAnimation();

        rightPauseButton();
    }

    void rightPauseButton() {

        ObjectAnimator rightLeftTopEdgeXAnim = ObjectAnimator.ofInt(this, "rightLeftTopEdgeX", rightLeftTopEdgeX, RIGHT_PAUSE_LEFT_TOP_EDGE_X);
        rightLeftTopEdgeXAnim.setDuration(animDuration);
        rightLeftTopEdgeXAnim.start();

        ObjectAnimator rightLeftTopEdgeYAnim = ObjectAnimator.ofInt(this, "rightLeftTopEdgeY", rightLeftTopEdgeY, RIGHT_PAUSE_LEFT_TOP_EDGE_Y);
        rightLeftTopEdgeYAnim.setDuration(animDuration);
        rightLeftTopEdgeYAnim.start();

        ObjectAnimator rightRightTopEdgeXAnim = ObjectAnimator.ofInt(this, "rightRightTopEdgeX", rightRightTopEdgeX, RIGHT_PAUSE_RIGHT_TOP_EDGE_X);
        rightRightTopEdgeXAnim.setDuration(animDuration);
        rightRightTopEdgeXAnim.start();

        ObjectAnimator rightRightTopEdgeYAnim = ObjectAnimator.ofInt(this, "rightRightTopEdgeY", rightRightTopEdgeY, RIGHT_PAUSE_RIGHT_TOP_EDGE_Y);
        rightRightTopEdgeYAnim.setDuration(animDuration);
        rightRightTopEdgeYAnim.start();

        ObjectAnimator rightLeftBottomEdgeXAnim = ObjectAnimator.ofInt(this, "rightLeftBottomEdgeX", rightLeftBottomEdgeX, RIGHT_PAUSE_LEFT_BOTTOM_EDGE_X);
        rightLeftBottomEdgeXAnim.setDuration(animDuration);
        rightLeftBottomEdgeXAnim.start();

        ObjectAnimator rightLeftBottomEdgeYAnim = ObjectAnimator.ofInt(this, "rightLeftBottomEdgeY", rightLeftBottomEdgeY, RIGHT_PAUSE_LEFT_BOTTOM_EDGE_Y);
        rightLeftBottomEdgeYAnim.setDuration(animDuration);
        rightLeftBottomEdgeYAnim.start();

        ObjectAnimator rightRightBottomEdgeXAnim = ObjectAnimator.ofInt(this, "rightRightBottomEdgeX", rightRightBottomEdgeX, RIGHT_PAUSE_RIGHT_BOTTOM_EDGE_X);
        rightRightBottomEdgeXAnim.setDuration(animDuration);
        rightRightBottomEdgeXAnim.start();

        ObjectAnimator rightRightBottomEdgeYAnim = ObjectAnimator.ofInt(this, "rightRightBottomEdgeY", rightRightBottomEdgeY, RIGHT_PAUSE_RIGHT_BOTTOM_EDGE_Y);
        rightRightBottomEdgeYAnim.setDuration(animDuration);
        rightRightBottomEdgeYAnim.start();
    }

    void leftPauseAnimation() {

        ObjectAnimator leftRightTopEdgeXAnim = ObjectAnimator.ofInt(this, "leftRightTopEdgeX", leftRightTopEdgeX, LEFT_PAUSE_RIGHT_TOP_EDGE_X);
        leftRightTopEdgeXAnim.setDuration(animDuration);
        leftRightTopEdgeXAnim.start();

        ObjectAnimator leftRightTopEdgeYAnim = ObjectAnimator.ofInt(this, "leftRightTopEdgeY", leftRightTopEdgeY, LEFT_PAUSE_RIGHT_TOP_EDGE_Y);
        leftRightTopEdgeYAnim.setDuration(animDuration);
        leftRightTopEdgeYAnim.start();

        ObjectAnimator leftRightBottomEdgeXAnim = ObjectAnimator.ofInt(this, "leftRightBottomEdgeX", leftRightBottomEdgeX, LEFT_PAUSE_RIGHT_BOTTOM_EDGE_X);
        leftRightBottomEdgeXAnim.setDuration(animDuration);
        leftRightBottomEdgeXAnim.start();

        ObjectAnimator leftRightBottomEdgeYAnim = ObjectAnimator.ofInt(this, "leftRightBottomEdgeY", leftRightBottomEdgeY, LEFT_PAUSE_RIGHT_BOTTOM_EDGE_Y);
        leftRightBottomEdgeYAnim.setDuration(animDuration);
        leftRightBottomEdgeYAnim.start();
    }

    /**
     * Get Material Button Current State
     */
    public int getState() {
        return state;
    }

    /**
     * Set Animation Duration
     */
    public void setAnimDuration(int animDuration) {
        this.animDuration = animDuration;
    }

    /**
     * Set Button Color
     */
    public void setColor(int color) {
        mPaint.setColor(color);
        invalidate();
    }

    /**
     * Animation Update Methods
     */

    private void setLeftRightTopEdgeX(int leftRightTopEdgeX) {
        this.leftRightTopEdgeX = leftRightTopEdgeX;
    }

    private void setLeftRightTopEdgeY(int leftRightTopEdgeY) {
        this.leftRightTopEdgeY = leftRightTopEdgeY;
    }

    private void setLeftRightBottomEdgeX(int leftRightBottomEdgeX) {
        this.leftRightBottomEdgeX = leftRightBottomEdgeX;
    }

    private void setLeftRightBottomEdgeY(int leftRightBottomEdgeY) {
        this.leftRightBottomEdgeY = leftRightBottomEdgeY;
    }

    private void setRightLeftTopEdgeX(int rightLeftTopEdgeX) {
        this.rightLeftTopEdgeX = rightLeftTopEdgeX;
    }

    private void setRightLeftTopEdgeY(int rightLeftTopEdgeY) {
        this.rightLeftTopEdgeY = rightLeftTopEdgeY;
    }

    private void setRightRightTopEdgeX(int rightRightTopEdgeX) {
        this.rightRightTopEdgeX = rightRightTopEdgeX;
    }

    private void setRightRightTopEdgeY(int rightRightTopEdgeY) {
        this.rightRightTopEdgeY = rightRightTopEdgeY;
    }

    private void setRightRightBottomEdgeX(int rightRightBottomEdgeX) {
        this.rightRightBottomEdgeX = rightRightBottomEdgeX;
    }

    private void setRightRightBottomEdgeY(int rightRightBottomEdgeY) {
        this.rightRightBottomEdgeY = rightRightBottomEdgeY;
    }

    private void setRightLeftBottomEdgeX(int rightLeftBottomEdgeX) {
        this.rightLeftBottomEdgeX = rightLeftBottomEdgeX;
    }

    private void setRightLeftBottomEdgeY(int rightLeftBottomEdgeY) {
        this.rightLeftBottomEdgeY = rightLeftBottomEdgeY;
        invalidate();
    }
}