package com.jcxavier.game.gl.logic;

import com.jcxavier.game.gl.util.Point;

import java.nio.FloatBuffer;
import java.nio.ShortBuffer;

/**
 * Created by jcxavier on 10/07/2013.
 */
public abstract class GameObject {
    // GL Stuff
    protected FloatBuffer vertexBuffer;
    protected ShortBuffer drawListBuffer;

    protected int mPositionHandle;
    protected int mColorHandle;
    protected int mMVPMatrixHandle;

    protected int vertexStride;

    public Point position;

    public abstract Point getPosition();
    public abstract void draw(int program, float[] mvpMatrix);
}
