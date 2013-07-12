package com.jcxavier.game.gl.player;

import com.jcxavier.game.gl.logic.Ball;
import com.jcxavier.game.gl.logic.Pad;

/**
 * Created by jcxavier on 10/07/2013.
 */
public class HumanPlayer extends Player {
    public HumanPlayer(Pad pad, Ball ball) {
        super(pad, ball);
    }

    @Override
    public void play(float speed) {
        mPad.move(speed);
    }
}