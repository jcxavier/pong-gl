package com.jcxavier.game.gl.logic;

import com.jcxavier.game.gl.util.Rectangle;

/**
 * Created on 10/07/2013.
 *
 * @author João Xavier <jcxavier@jcxavier.com>
 */
public class Ball extends GameObject {

    private static final float BALL_SIZE =      0.02f;
    private static final float[] BALL_COLOR =   new float[] { 1.0f, 1.0f, 1.0f, 1.0f };
    private static final float[] BALL_COORDS =  {
            -BALL_SIZE,  BALL_SIZE, 0.0f, // top left
            -BALL_SIZE, -BALL_SIZE, 0.0f, // bottom left
             BALL_SIZE, -BALL_SIZE, 0.0f, // bottom right
             BALL_SIZE,  BALL_SIZE, 0.0f  // top right
    };

    private static final float SPEED_OFFSET =   0.01f;
    private static final float SPEED_RANGE =    0.03f;

    private float mXSpeed;
    private float mYSpeed;
    private boolean mIsColliding;

    public Ball() {
        super(BALL_COORDS, BALL_COLOR);
        resetPosition();
    }

    private float randomSign() {
        int zeroOrOne = (int) (Math.random() + 1/2);
        return zeroOrOne == 1 ? 1.0f : -1.0f;
    }

    public void resetPosition() {
        position.x = 0.0f;
        position.y = 0.0f;

        mIsColliding = false;

        mXSpeed = randomSign() * (float) (Math.random() * SPEED_RANGE + SPEED_OFFSET);
        mYSpeed = randomSign() * (float) (Math.random() * SPEED_RANGE + SPEED_OFFSET);
    }

    public void move() {
        if ((position.y - BALL_SIZE) < -1.0f || (position.y + BALL_SIZE) > 1.0f) {
            mYSpeed *= -1.0f;
        }

        position.x += mXSpeed;
        position.y += mYSpeed;
    }

    public Collision intersectsWithHorizontalBounds(float width) {
        if ((position.x - BALL_SIZE) < -width) {
            return Collision.RIGHT_SIDE;
        } else if ((position.x + BALL_SIZE) > width) {
            return Collision.LEFT_SIDE;
        }

        return Collision.NONE;
    }

    public void handleIntersectionWithBounds(Rectangle bounds) {
        if (Math.abs(bounds.x - position.x) < (Math.abs(bounds.width + BALL_SIZE * 2) / 2)
                && (Math.abs(bounds.y - position.y) < (Math.abs(bounds.height + BALL_SIZE * 2) / 2))) {

            if (!mIsColliding) {
                mXSpeed *= -1.0f;
                mIsColliding = true;
            }
        } else {
            mIsColliding = false;
        }
    }
}
