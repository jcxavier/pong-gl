package com.jcxavier.game.gl.logic;

import android.opengl.GLES20;
import com.jcxavier.game.gl.util.Point;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.ShortBuffer;

/**
 * Created on 10/07/2013.
 *
 * @author João Xavier <jcxavier@jcxavier.com>
 */
public abstract class GameObject {

    // number of coordinates per vertex in this array
    private static final int COORDS_PER_VERTEX = 3;
    // order to draw vertices
    private static final short[] VERTEX_DRAW_ORDER = { 0, 1, 2, 0, 2, 3 };

    private final float[] mColor;

    // GL Stuff
    private final FloatBuffer mVertexBuffer;
    private final ShortBuffer mDrawListBuffer;
    private final int mVertexStride;

    private int mPositionHandle;
    private int mColorHandle;
    private int mMVPMatrixHandle;

    public final Point position;

    GameObject(final float[] coordinates, final float[] color) {
        mColor = color;

        position = new Point();

        // initialize vertex byte buffer for shape coordinates
        ByteBuffer bb = ByteBuffer.allocateDirect(
                // (# of coordinate values * 4 bytes per float)
                coordinates.length * 4);

        bb.order(ByteOrder.nativeOrder());
        mVertexBuffer = bb.asFloatBuffer();
        mVertexBuffer.put(coordinates);
        mVertexBuffer.position(0);

        // initialize byte buffer for the draw list
        ByteBuffer dlb = ByteBuffer.allocateDirect(
                // (# of coordinate values * 2 bytes per short)
                VERTEX_DRAW_ORDER.length * 2);
        dlb.order(ByteOrder.nativeOrder());
        mDrawListBuffer = dlb.asShortBuffer();
        mDrawListBuffer.put(VERTEX_DRAW_ORDER);
        mDrawListBuffer.position(0);

        mVertexStride = COORDS_PER_VERTEX * 4;  //4 are how many bytes in a float
    }

    public void draw(int program, float[] mvpMatrix) {
        // get handle to vertex shader's vPosition member
        mPositionHandle = GLES20.glGetAttribLocation(program, "aPosition");

        // Enable a handle to the triangle vertices
        GLES20.glEnableVertexAttribArray(mPositionHandle);

        // Prepare the triangle coordinate data
        GLES20.glVertexAttribPointer(mPositionHandle, COORDS_PER_VERTEX,
                GLES20.GL_FLOAT, false,
                mVertexStride, mVertexBuffer);

        // get handle to fragment shader's vColor member
        mColorHandle = GLES20.glGetUniformLocation(program, "vColor");

        // Get handle to shape's transformation matrix
        mMVPMatrixHandle = GLES20.glGetUniformLocation(program, "uMVPMatrix");

        // Apply the projection and view transformation
        GLES20.glUniformMatrix4fv(mMVPMatrixHandle, 1, false, mvpMatrix, 0);

        // Set mColor for drawing the square
        GLES20.glUniform4fv(mColorHandle, 1, mColor, 0);

        // Draw the square
        GLES20.glDrawElements(GLES20.GL_TRIANGLES, VERTEX_DRAW_ORDER.length,
                GLES20.GL_UNSIGNED_SHORT, mDrawListBuffer);

        // Disable vertex array
        GLES20.glDisableVertexAttribArray(mPositionHandle);
    }
}
