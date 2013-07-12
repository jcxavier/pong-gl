package com.jcxavier.game.pong.player;

import com.jcxavier.game.pong.logic.Ball;
import com.jcxavier.game.pong.logic.Pad;
import com.jcxavier.game.pong.player.Player;

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
