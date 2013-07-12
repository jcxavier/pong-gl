package com.jcxavier.game.gl;

import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.opengl.Matrix;
import android.util.Log;
import com.jcxavier.game.gl.player.AIPlayer;
import com.jcxavier.game.gl.player.HumanPlayer;
import com.jcxavier.game.gl.logic.Ball;
import com.jcxavier.game.gl.logic.GameObject;
import com.jcxavier.game.gl.logic.Pad;
import com.jcxavier.game.gl.util.Point;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

/**
 * Created on 10/07/2013.
 *
 * @author João Xavier <jcxavier@jcxavier.com>
 */
class PongRenderer implements GLSurfaceView.Renderer {

    private static final String TAG = PongRenderer.class.getSimpleName();

    private static final int MATRIX_SIZE = 4 * 4;

    // GLSL shader code
    private final String mVertexShader =
            "uniform mat4 uMVPMatrix;\n" +
                    "attribute vec4 aPosition;\n" +
                    "void main() {\n" +
                    "  gl_Position = uMVPMatrix * aPosition;\n" +
                    "}\n";

    private final String mFragmentShader =
            "precision mediump float;\n" +
                    "uniform vec4 vColor;" +
                    "void main() {\n" +
                    "  gl_FragColor = vColor;\n" +
                    "}\n";
    // GL handles
    private int mProgram;
    private int muMVPMatrixHandle;
    private int maPositionHandle;

    // GL matrixes
    private final float[] mMVPMatrix = new float[MATRIX_SIZE];
    private final float[] mProjMatrix = new float[MATRIX_SIZE];
    private final float[] mVMatrix = new float[MATRIX_SIZE];
    private final float[] mMMatrix = new float[MATRIX_SIZE];
    private float[] mTempMatrix = new float[MATRIX_SIZE];

    // activity handle for score updates
    private final PongActivity mActivity;

    // game logic
    private static final float SCREEN_WIDTH = 1.7f;

    private HumanPlayer mPlayerOne;
    private AIPlayer mPlayerTwo;
    private Ball mBall;

    // input speed (controlled in SurfaceView)
    public volatile float speed;

    public PongRenderer(PongActivity activity) {
        super();
        mActivity = activity;
    }

    private void initGameLogic() {
        speed = 0.0f;
        mBall = new Ball();
        mPlayerOne = new HumanPlayer(new Pad(Pad.Side.LEFT), mBall);
        mPlayerTwo = new AIPlayer( new Pad(Pad.Side.RIGHT), mBall);

        mActivity.updateScore(mPlayerOne.getPoints(), mPlayerTwo.getPoints());
    }


    @Override
    public void onSurfaceCreated(GL10 glUnused, EGLConfig config) {
        // where the magic happens
        initGameLogic();

        // Ignore the passed-in GL10 interface, and use the GLES20
        // class's static methods instead.
        mProgram = createProgram(mVertexShader, mFragmentShader);
        if (mProgram == 0) {
            return;
        }
        maPositionHandle = GLES20.glGetAttribLocation(mProgram, "aPosition");
        checkGlError("glGetAttribLocation aPosition");
        if (maPositionHandle == -1) {
            throw new RuntimeException("Could not get attrib location for aPosition");
        }

        muMVPMatrixHandle = GLES20.glGetUniformLocation(mProgram, "uMVPMatrix");
        checkGlError("glGetUniformLocation uMVPMatrix");
        if (muMVPMatrixHandle == -1) {
            throw new RuntimeException("Could not get attrib location for uMVPMatrix");
        }

        Matrix.setLookAtM(mVMatrix, 0, 0, 0, -5, 0f, 0f, 0f, 0f, 1.0f, 0.0f);
    }

    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {
        GLES20.glViewport(0, 0, width, height);
        float ratio = (float) width / height;

        // this projection matrix is applied to object coordinates
        // in the onDrawFrame() method
        Matrix.frustumM(mProjMatrix, 0, -ratio, ratio, -1, 1, 3, 7);
    }

    @Override
    public void onDrawFrame(GL10 gl) {
        // Redraw background color
        GLES20.glClearColor(0.0f, 0.0f, 0.0f, 1.0f);
        GLES20.glClear(GLES20.GL_DEPTH_BUFFER_BIT | GLES20.GL_COLOR_BUFFER_BIT);
        GLES20.glUseProgram(mProgram);
        checkGlError("glUseProgram");

        // Set the camera position (View matrix)
        Matrix.setLookAtM(mVMatrix, 0, 0, 0, -3, 0f, 0f, 0f, 0f, 1.0f, 0.0f);

        // Calculate the projection and view transformation
        Matrix.multiplyMM(mMVPMatrix, 0, mProjMatrix, 0, mProjMatrix, 0);

        Matrix.setIdentityM(mMMatrix, 0);
        Matrix.multiplyMM(mMVPMatrix, 0, mVMatrix, 0, mMMatrix, 0);
        Matrix.multiplyMM(mMVPMatrix, 0, mProjMatrix, 0, mMVPMatrix, 0);


        // detect ball collision with pads and updates the speed
        mBall.handleIntersectionWithBounds(mPlayerOne.getPad().getBounds());
        mBall.handleIntersectionWithBounds(mPlayerTwo.getPad().getBounds());

        // detect ball collision with screen
        switch (mBall.intersectsWithHorizontalBounds(SCREEN_WIDTH)) {
            case RIGHT_SIDE:
                mBall.resetPosition();
                mPlayerOne.score();
                mActivity.updateScore(mPlayerOne.getPoints(), mPlayerTwo.getPoints());
                break;

            case LEFT_SIDE:
                mBall.resetPosition();
                mPlayerTwo.score();
                mActivity.updateScore(mPlayerOne.getPoints(), mPlayerTwo.getPoints());
                break;
        }

        // move ball and handle vertical collision
        mBall.move();

        // players play
        mPlayerOne.play(speed); // user input
        mPlayerTwo.play();

        // move pads
        moveAndDrawGameObject(mBall);
        moveAndDrawGameObject(mPlayerOne.getPad());
        moveAndDrawGameObject(mPlayerTwo.getPad());
    }

    private void moveAndDrawGameObject(GameObject gameObject) {
        Point position = gameObject.position;
        Matrix.setIdentityM(mMMatrix, 0);
        Matrix.translateM(mMMatrix, 0, position.x, position.y, 0);

        mTempMatrix = mMVPMatrix.clone();
        Matrix.multiplyMM(mTempMatrix, 0, mVMatrix, 0, mMMatrix, 0);
        Matrix.multiplyMM(mTempMatrix, 0, mProjMatrix, 0, mTempMatrix, 0);
        gameObject.draw(mProgram, mTempMatrix);
    }

    private int loadShader(int shaderType, String source) {
        int shader = GLES20.glCreateShader(shaderType);
        if (shader != 0) {
            GLES20.glShaderSource(shader, source);
            GLES20.glCompileShader(shader);
            int[] compiled = new int[1];
            GLES20.glGetShaderiv(shader, GLES20.GL_COMPILE_STATUS, compiled, 0);
            if (compiled[0] == 0) {
                Log.e(TAG, "Could not compile shader " + shaderType + ":");
                Log.e(TAG, GLES20.glGetShaderInfoLog(shader));
                GLES20.glDeleteShader(shader);
                shader = 0;
            }
        }
        return shader;
    }

    private int createProgram(String vertexSource, String fragmentSource) {
        int vertexShader = loadShader(GLES20.GL_VERTEX_SHADER, vertexSource);
        if (vertexShader == 0) {
            return 0;
        }

        int pixelShader = loadShader(GLES20.GL_FRAGMENT_SHADER, fragmentSource);
        if (pixelShader == 0) {
            return 0;
        }

        int program = GLES20.glCreateProgram();
        if (program != 0) {
            GLES20.glAttachShader(program, vertexShader);
            checkGlError("glAttachShader");
            GLES20.glAttachShader(program, pixelShader);
            checkGlError("glAttachShader");
            GLES20.glLinkProgram(program);
            int[] linkStatus = new int[1];
            GLES20.glGetProgramiv(program, GLES20.GL_LINK_STATUS, linkStatus, 0);
            if (linkStatus[0] != GLES20.GL_TRUE) {
                Log.e(TAG, "Could not link program: ");
                Log.e(TAG, GLES20.glGetProgramInfoLog(program));
                GLES20.glDeleteProgram(program);
                program = 0;
            }
        }
        return program;
    }

    private void checkGlError(String op) {
        int error;
        while ((error = GLES20.glGetError()) != GLES20.GL_NO_ERROR) {
            Log.e(TAG, op + ": glError " + error);
            throw new RuntimeException(op + ": glError " + error);
        }
    }
}
