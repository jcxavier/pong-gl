package com.jcxavier.game.gl.player;

import com.jcxavier.game.gl.logic.Ball;
import com.jcxavier.game.gl.logic.Pad;

/**
 * Created on 10/07/2013.
 *
 * @author João Xavier <jcxavier@jcxavier.com>
 */
public class AIPlayer extends Player {
    private static final float AI_MOVE_SPEED = 0.02f;

    private final Ball mBall;

    public AIPlayer(Pad pad, Ball ball) {
        super(pad);
        mBall = ball;
    }

    public void play() {
        play(AI_MOVE_SPEED);
    }

    @Override
    public void play(float speed) {
        if (mBall.position.y < mPad.position.y) {
            mPad.move(-speed);
        } else if (mBall.position.y > mPad.position.y) {
            mPad.move(speed);
        }
    }
}
