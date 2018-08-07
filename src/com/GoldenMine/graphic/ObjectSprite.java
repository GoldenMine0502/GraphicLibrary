package com.GoldenMine.graphic;

import org.joml.Vector3f;

/**
 * Created by ehe12 on 2018-08-05.
 */
public class ObjectSprite {
    private static final int MINUS_Z = 2;

    ObjectElement element;

    private final Vector3f position = new Vector3f();
    private final Vector3f rotation = new Vector3f();

    private float scale = 1.0f;

    public ObjectSprite(ObjectElement element) {
        this.element = element;
        setPosition(0,0,0);
    }

    public void setPosition(float x, float y, float z) {
        position.x = x;
        position.y = y;
        position.z = z-MINUS_Z;
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

    public ObjectElement getObjectElement() {
        return element;
    }
}
