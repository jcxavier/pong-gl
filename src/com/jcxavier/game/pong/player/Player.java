package com.jcxavier.game.pong.player;

import com.jcxavier.game.pong.logic.Ball;
import com.jcxavier.game.pong.logic.Pad;

/**
 * Created by jcxavier on 10/07/2013.
 */
public abstract class Player {
    protected final Pad mPad;
    protected final Ball mBall;

    private int mPoints;

    public Player(Pad pad, Ball ball) {
        mPad = pad;
        mBall = ball;

        mPoints = 0;
    }

    public abstract void play(float speed);

    public int getPoints() {
        return mPoints;
    }

    public Pad getPad() {
        return mPad;
    }

    public void score() {
        mPoints++;
    }
}
