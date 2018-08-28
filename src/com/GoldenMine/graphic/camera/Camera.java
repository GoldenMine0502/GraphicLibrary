package com.GoldenMine.graphic.camera;

import com.GoldenMine.graphic.elements.Object3D;
import com.GoldenMine.graphic.util.Transformation;
import org.joml.Matrix4f;
import org.joml.Vector3f;

/**
 * Created by ehe12 on 2018-08-07.
 */
public class Camera extends Object3D {
    private Matrix4f viewMatrix;

    public Camera() {
        viewMatrix = new Matrix4f();
    }

    public Camera(Vector3f position, Vector3f rotation) {
        setPosition(position.x, position.y, position.z);
        setRotation(rotation.x, rotation.y, rotation.z);
    }

    public Matrix4f getViewMatrix() {
        return viewMatrix;
    }

    public Matrix4f updateViewMatrix() {
        return Transformation.updateGenericViewMatrix(position, rotation, viewMatrix);
    }

    public void movePosition(float offsetX, float offsetY, float offsetZ) {
        if ( offsetZ != 0 ) {
            position.x += (float)Math.sin(Math.toRadians(rotation.y)) * -1.0f * offsetZ;
            position.z += (float)Math.cos(Math.toRadians(rotation.y)) * offsetZ;
        }
        if ( offsetX != 0) {
            position.x += (float)Math.sin(Math.toRadians(rotation.y - 90)) * -1.0f * offsetX;
            position.z += (float)Math.cos(Math.toRadians(rotation.y - 90)) * offsetX;
        }
        position.y += offsetY;
    }

    public Vector3f getRotation() {
        return rotation;
    }

    public void setRotation(float x, float y, float z) {
        rotation.x = x;
        rotation.y = y;
        rotation.z = z;
    }

    public void moveRotation(float offsetX, float offsetY, float offsetZ) {
        rotation.x += offsetX;
        rotation.y += offsetY;
        rotation.z += offsetZ;
    }
}
