package com.GoldenMine.graphic.elements;

import org.joml.Intersectionf;

/**
 * Created by ehe12 on 2018-08-12.
 */
public enum ObjectType {
    CIRCLE(1),
    PLANE(1),
    TRIANGLE(1),

    ;
    int time;

    private ObjectType(int time) {
        this.time = time;

    }

    public int getInt() {
        return time;
    }
}
