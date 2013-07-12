package com.jcxavier.game.gl.logic;

import android.opengl.GLES20;
import com.jcxavier.game.gl.util.Point;
import com.jcxavier.game.gl.util.Rectangle;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

/**
 * Created by jcxavier on 10/07/2013.
 */
public class Pad extends GameObject {

    public enum Side { LEFT, RIGHT }

    // number of coordinates per vertex in this array
    private static final int COORDS_PER_VERTEX = 3;
    private static final float[] PAD_COLOR = new float[] {1.0f, 1.0f, 1.0f, 1.0f};

    private static final float PAD_HEIGHT = 0.25f;
    private static final float PAD_OFFSET_X = 1.45f;
    private static final float PAD_WIDTH = 0.05f;

    private static final float leftPadCoords[] = {
            PAD_OFFSET_X, PAD_HEIGHT, 0.0f,  // top left
            PAD_OFFSET_X, -PAD_HEIGHT, 0.0f,  // bottom left
            PAD_OFFSET_X + PAD_WIDTH, -PAD_HEIGHT, 0.0f,  // bottom right
            PAD_OFFSET_X + PAD_WIDTH, PAD_HEIGHT, 0.0f,  // top right
    };

    private static final float rightPadCoords[] = {
            -PAD_OFFSET_X - PAD_WIDTH, PAD_HEIGHT, 0.0f,  // top left
            -PAD_OFFSET_X - PAD_WIDTH, -PAD_HEIGHT, 0.0f,  // bottom left
            -PAD_OFFSET_X, -PAD_HEIGHT, 0.0f,  // bottom right
            -PAD_OFFSET_X, PAD_HEIGHT, 0.0f   // top right
    };

    private float padCoords[];


    private short drawOrder[] = {0, 1, 2, 0, 2, 3}; // order to draw vertices

    private int vertexStride;

    public Pad(Side controller) {
        super();

        if (controller == Side.LEFT) {
            padCoords = leftPadCoords;
        } else {
            padCoords = rightPadCoords;
        }

        position = new Point(0.0f, 0.0f);

        // initialize vertex byte buffer for shape coordinates
        ByteBuffer bb = ByteBuffer.allocateDirect(
                // (# of coordinate values * 4 bytes per float)
                padCoords.length * 4);

        bb.order(ByteOrder.nativeOrder());
        vertexBuffer = bb.asFloatBuffer();
        vertexBuffer.put(padCoords);
        vertexBuffer.position(0);

        // initialize byte buffer for the draw list
        ByteBuffer dlb = ByteBuffer.allocateDirect(
                // (# of coordinate values * 2 bytes per short)
                drawOrder.length * 2);
        dlb.order(ByteOrder.nativeOrder());
        drawListBuffer = dlb.asShortBuffer();
        drawListBuffer.put(drawOrder);
        drawListBuffer.position(0);

        vertexStride = COORDS_PER_VERTEX * 4;  //4 are how many bytes in a float
    }

    @Override
    public void draw(int program, float[] mvpMatrix) {
        // get handle to vertex shader's vPosition member
        mPositionHandle = GLES20.glGetAttribLocation(program, "aPosition");

        // Enable a handle to the triangle vertices
        GLES20.glEnableVertexAttribArray(mPositionHandle);

        // Prepare the triangle coordinate data
        GLES20.glVertexAttribPointer(mPositionHandle, COORDS_PER_VERTEX,
                GLES20.GL_FLOAT, false,
                vertexStride, vertexBuffer);

        // get handle to fragment shader's vColor member
        mColorHandle = GLES20.glGetUniformLocation(program, "vColor");

        // Get handle to shape's transformation matrix
        mMVPMatrixHandle = GLES20.glGetUniformLocation(program, "uMVPMatrix");

        // Set color for drawing the square
        GLES20.glUniform4fv(mColorHandle, 1, PAD_COLOR, 0);

        // Apply the projection and view transformation
        GLES20.glUniformMatrix4fv(mMVPMatrixHandle, 1, false, mvpMatrix, 0);

        // Draw the square
        GLES20.glDrawElements(GLES20.GL_TRIANGLES, drawOrder.length,
                GLES20.GL_UNSIGNED_SHORT, drawListBuffer);

        // Disable vertex array
        GLES20.glDisableVertexAttribArray(mPositionHandle);
    }

    public Rectangle getBounds() {
        return new Rectangle((padCoords[0] + padCoords[6]) / 2.0f, position.y, PAD_WIDTH * 2, PAD_HEIGHT * 2);
    }

    @Override
    public Point getPosition() {
        return position;
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
