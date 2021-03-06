package com.jcxavier.game.gl.player;

import com.jcxavier.game.gl.logic.Pad;

/**
 * Created on 10/07/2013.
 *
 * @author João Xavier <jcxavier@jcxavier.com>
 */
public abstract class Player {
    final Pad mPad;
    private int mPoints;

    Player(Pad pad) {
        mPad = pad;
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
