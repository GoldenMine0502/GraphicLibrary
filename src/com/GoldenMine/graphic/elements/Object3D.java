package com.GoldenMine.graphic.elements;

import org.joml.Vector3f;

/**
 * Created by ehe12 on 2018-08-08.
 */
public class  Object3D {

    protected final Vector3f position = new Vector3f();
    protected final Vector3f rotation = new Vector3f();

    private float scale = 1.0f;

    public Object3D() {
        setPosition(0,0,0);
    }

    public void setPosition(float x, float y, float z) {
        position.x = x;
        position.y = y;
        position.z = z;
    }

    public void setRotation(float x, float y, float z) {
        rotation.x = x;
        rotation.y = y;
        rotation.z = z;
    }

    public void setScale(float scale) {
        this.scale = scale;
    }

    public float getScale() {
        return scale;
    }

    public Vector3f getPosition() {
        return position;
    }

    public Vector3f getRotation() {
        return rotation;
    }

}
