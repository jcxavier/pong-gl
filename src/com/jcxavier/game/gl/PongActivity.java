package com.jcxavier.game.gl;

import android.app.Activity;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.widget.FrameLayout;
import android.widget.TextView;

public class PongActivity extends Activity {

    private TextView mScore;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        FrameLayout gameView = (FrameLayout) getLayoutInflater().inflate(R.layout.game_view, null);
        setContentView(gameView);

        mScore = (TextView) gameView.findViewById(R.id.score);

        GLSurfaceView glSurfaceView = new PongSurfaceView(this);
        glSurfaceView.setRenderMode(GLSurfaceView.RENDERMODE_CONTINUOUSLY);

        gameView.addView(glSurfaceView);
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
