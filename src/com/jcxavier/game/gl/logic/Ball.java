package com.jcxavier.game.gl.logic;

import android.opengl.GLES20;
import com.jcxavier.game.gl.util.Point;
import com.jcxavier.game.gl.util.Rectangle;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

/**
 * Created by jcxavier on 10/07/2013.
 */
public class Ball extends GameObject {
    private float xSpeed;
    private float ySpeed;

    private boolean mIsColliding;

    // number of coordinates per vertex in this array
    private static final int COORDS_PER_VERTEX = 3;
    private static final float BALL_SIZE = 0.02f;
    private static final float[] BALL_COLOR = new float[]{1.0f, 1.0f, 1.0f, 1.0f};

    private static float ballCoords[] = {
            -BALL_SIZE, BALL_SIZE, 0.0f,   // top left
            -BALL_SIZE, -BALL_SIZE, 0.0f,   // bottom left
            BALL_SIZE, -BALL_SIZE, 0.0f,   // bottom right
            BALL_SIZE, BALL_SIZE, 0.0f    // top right
    };

    private short drawOrder[] = {0, 1, 2, 0, 2, 3}; // order to draw vertices

    public Ball() {
        super();

        setRandomSpeed();
        mIsColliding = false;

        position = new Point(0.0f, 0.0f);

        // initialize vertex byte buffer for shape coordinates
        ByteBuffer bb = ByteBuffer.allocateDirect(
                // (# of coordinate values * 4 bytes per float)
                ballCoords.length * 4);

        bb.order(ByteOrder.nativeOrder());
        vertexBuffer = bb.asFloatBuffer();
        vertexBuffer.put(ballCoords);
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

    private void setRandomSpeed() {
        // 0.01f - 0.04f
        xSpeed = randomSign() * (float) (Math.random() * 0.03 + 0.01);
        ySpeed = randomSign() * (float) (Math.random() * 0.03 + 0.01);
    }

    private float randomSign() {
        int zeroOrOne = (int) (Math.random() + 0.5);
        return zeroOrOne == 1 ? 1.0f : -1.0f;
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

        // Apply the projection and view transformation
        GLES20.glUniformMatrix4fv(mMVPMatrixHandle, 1, false, mvpMatrix, 0);

        // Set color for drawing the square
        GLES20.glUniform4fv(mColorHandle, 1, BALL_COLOR, 0);

        // Draw the square
        GLES20.glDrawElements(GLES20.GL_TRIANGLES, drawOrder.length,
                GLES20.GL_UNSIGNED_SHORT, drawListBuffer);

        // Disable vertex array
        GLES20.glDisableVertexAttribArray(mPositionHandle);
    }

    public void move() {
        if ((position.y - BALL_SIZE) < -1.0f || (position.y + BALL_SIZE) > 1.0f) {
            ySpeed *= -1.0f;
        }

        position.x += xSpeed;
        position.y += ySpeed;
    }

    public Collision intersectsWithHorizontalBounds(float width) {
        if ((position.x - BALL_SIZE) < -width) {
            return Collision.RIGHT_SIDE;
        } else if ((position.x + BALL_SIZE) > width) {
            return Collision.LEFT_SIDE;
        }

        return Collision.NONE;
    }

    public void resetPosition() {
        position.x = 0.0f;
        position.y = 0.0f;

        setRandomSpeed();
    }

    public void handleIntersectionWithBounds(Rectangle bounds) {
        if (Math.abs(bounds.x - position.x) < (Math.abs(bounds.width + BALL_SIZE * 2) / 2)
                && (Math.abs(bounds.y - position.y) < (Math.abs(bounds.height + BALL_SIZE * 2) / 2))) {

            if (!mIsColliding) {
                xSpeed *= -1.0f;
                mIsColliding = true;
            }
        } else {
            mIsColliding = false;
        }
    }

    @Override
    public Point getPosition() {
        return position;
    }
}
