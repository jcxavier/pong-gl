package com.jcxavier.game.gl;

import android.content.Context;
import android.opengl.GLSurfaceView;
import android.view.MotionEvent;

/**
 * Created by jcxavier on 10/07/2013.
 */
public class PongSurfaceView extends GLSurfaceView {
    // yay for magic numbers, tweak for input experience
    private static final float TOUCH_SCALE_FACTOR = 180.0f / (320 * 2000);

    private PongRenderer mPongRenderer;

    private float mPreviousY;

    public PongSurfaceView(Context context) {
        super(context);

        mPreviousY = 0.0f;

        // Create an OpenGL ES 2.0 context
        setEGLContextClientVersion(2);

        // Set the Renderer for drawing on the GLSurfaceView
        mPongRenderer = new PongRenderer((PongActivity) context);
        setRenderer(mPongRenderer);
    }

    @Override
    public boolean onTouchEvent(MotionEvent e) {
        // MotionEvent reports input details from the touch screen
        // and other input controls. In this case, you are only
        // interested in events where the touch position changed.

        float x = e.getX();
        float y = e.getY();

        switch (e.getAction()) {
            case MotionEvent.ACTION_MOVE:

                float dy = y - mPreviousY;

                // reverse direction of rotation to left of the mid-line
                if (x < getWidth() / 2) {
                    dy = dy * -1;
                }

                mPongRenderer.speed += dy * TOUCH_SCALE_FACTOR;
        }

        mPreviousY = y;
        return true;
    }
}
