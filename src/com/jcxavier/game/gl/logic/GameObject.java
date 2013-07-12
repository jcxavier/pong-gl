package com.jcxavier.game.gl.logic;

import com.jcxavier.game.gl.util.Point;

import java.nio.FloatBuffer;
import java.nio.ShortBuffer;

/**
 * Created by jcxavier on 10/07/2013.
 */
public abstract class GameObject {
    // GL Stuff
    FloatBuffer vertexBuffer;
    ShortBuffer drawListBuffer;

    int mPositionHandle;
    int mColorHandle;
    int mMVPMatrixHandle;

    int vertexStride;

    public Point position;

    public abstract Point getPosition();
    public abstract void draw(int program, float[] mvpMatrix);
}
