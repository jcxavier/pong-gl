package com.jcxavier.game.pong;

import android.app.Activity;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.widget.FrameLayout;
import android.widget.TextView;

public class PongActivity extends Activity {

    private GLSurfaceView mGLView;
    private TextView mScore;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        FrameLayout gameView = (FrameLayout) getLayoutInflater().inflate(R.layout.game_view, null);
        setContentView(gameView);

        mScore = (TextView) gameView.findViewById(R.id.score);

        // Create a GLSurfaceView instance and set it
        // as the ContentView for this Activity.
        mGLView = new PongSurfaceView(this);
        mGLView.setRenderMode(GLSurfaceView.RENDERMODE_CONTINUOUSLY);

        gameView.addView(mGLView);
        mScore.bringToFront();
    }

    public void updateScore(final int leftScore, final int rightScore) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mScore.setText(String.format("%d - %d", leftScore, rightScore));
            }
        });
    }
}
