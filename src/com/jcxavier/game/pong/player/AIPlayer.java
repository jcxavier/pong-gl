package com.jcxavier.game.pong.player;

import com.jcxavier.game.pong.logic.Ball;
import com.jcxavier.game.pong.logic.Pad;
import com.jcxavier.game.pong.player.Player;

/**
 * Created by jcxavier on 10/07/2013.
 */
public class AIPlayer extends Player {
    private static final float AI_MOVE_SPEED = 0.02f;

    public AIPlayer(Pad pad, Ball ball) {
        super(pad, ball);
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