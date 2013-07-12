package com.jcxavier.game.gl.util;

/**
 * Created by jcxavier on 10/07/2013.
 */
public class Point {
    public float x;
    public float y;

    public Point() {
        this(0.0f, 0.0f);
    }

    public Point(float x, float y) {
        this.x = x;
        this.y = y;
    }

    @Override
    public String toString() {
        return "x:" + x + ", y:" + y;
    }
}
