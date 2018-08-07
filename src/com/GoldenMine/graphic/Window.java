package com.GoldenMine.graphic;

import com.GoldenMine.Utility.Point;

/**
 * Created by ehe12 on 2018-08-07.
 */
public class Window {
    private long window;

    private float FOV;
    private float Z_NEAR;
    private float Z_FAR;

    private boolean resized;

    public Window(String title, Point paletteSize) {
        setFOV(67.4f);
        setZNEAR(0.01f);
        setZFAR(1000000f);
    }

    public float getFOV() {
        return FOV;
    }

    public float getZNEAR() {
        return Z_NEAR;
    }

    public float getZFAR() {
        return Z_FAR;
    }

    public void setFOV(float fov) {
        FOV = (float)Math.toRadians(fov);
        resized = true;
    }

    public void setZNEAR(float near) {
        Z_NEAR = near;
        resized = true;
    }

    public void setZFAR(float far) {
        Z_FAR = far;
        resized = true;
    }
}
