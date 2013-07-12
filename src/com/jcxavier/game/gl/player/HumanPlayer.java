package com.jcxavier.game.gl.player;

import com.jcxavier.game.gl.logic.Pad;

/**
 * Created on 10/07/2013.
 *
 * @author João Xavier <jcxavier@jcxavier.com>
 */
public class HumanPlayer extends Player {
    public HumanPlayer(Pad pad) {
        super(pad);
    }

    @Override
    public void play(float speed) {
        mPad.move(speed);
    }
}
