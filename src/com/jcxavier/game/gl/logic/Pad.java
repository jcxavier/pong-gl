package com.jcxavier.game.gl.logic;

import com.jcxavier.game.gl.util.Rectangle;

/**
 * Created on 10/07/2013.
 *
 * @author João Xavier <jcxavier@jcxavier.com>
 */
public class Pad extends GameObject {

    public enum Side { LEFT, RIGHT }

    private static final float PAD_HEIGHT =     0.25f;
    private static final float PAD_OFFSET_X =   1.45f;
    private static final float PAD_WIDTH =      0.05f;

    private static final float[] PAD_COLOR =    new float[] { 1.0f, 1.0f, 1.0f, 1.0f };

    private static final float PAD_LEFT_COORDS[] = {
            PAD_OFFSET_X, PAD_HEIGHT, 0.0f,  // top left
            PAD_OFFSET_X, -PAD_HEIGHT, 0.0f, // bottom left
            PAD_OFFSET_X + PAD_WIDTH, -PAD_HEIGHT, 0.0f, // bottom right
            PAD_OFFSET_X + PAD_WIDTH, PAD_HEIGHT, 0.0f,  // top right
    };
    private static final float PAD_RIGHT_COORDS[] = {
            -PAD_OFFSET_X - PAD_WIDTH, PAD_HEIGHT, 0.0f,  // top left
            -PAD_OFFSET_X - PAD_WIDTH, -PAD_HEIGHT, 0.0f, // bottom left
            -PAD_OFFSET_X, -PAD_HEIGHT, 0.0f, // bottom right
            -PAD_OFFSET_X, PAD_HEIGHT, 0.0f   // top right
    };

    private final float mPadCoords[];

    public Pad(Side controller) {
        super(controller == Side.LEFT ? PAD_LEFT_COORDS : PAD_RIGHT_COORDS, PAD_COLOR);
        mPadCoords = controller == Side.LEFT ? PAD_LEFT_COORDS : PAD_RIGHT_COORDS;
    }

    public Rectangle getBounds() {
        return new Rectangle((mPadCoords[0] + mPadCoords[6]) / 2, position.y, PAD_WIDTH * 2, PAD_HEIGHT * 2);
    }

    public void move(float speed) {
        if (position.y + PAD_HEIGHT + speed > 1.0f) {
            position.y = 1.0f - PAD_HEIGHT;
        } else if (position.y - PAD_HEIGHT + speed < -1.0f) {
            position.y = -1.0f + PAD_HEIGHT;
        } else {
            position.y += speed;
        }
    }
}
