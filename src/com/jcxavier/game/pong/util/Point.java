package com.jcxavier.game.pong.util;

/**
 * Created by jcxavier on 10/07/2013.
 */
public class Point {
    public float x;
    public float y;

    public Point(float x, float y) {
        this.x = x;
        this.y = y;
    }

    @Override
    public String toString() {
        return "x:" + x + ", y:" + y;
    }
}
