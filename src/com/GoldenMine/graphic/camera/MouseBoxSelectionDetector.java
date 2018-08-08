package com.GoldenMine.graphic.camera;

import com.GoldenMine.graphic.util.Window;
import com.GoldenMine.graphic.elements.Sprite;
import java.util.List;
import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.joml.Vector4f;

public class MouseBoxSelectionDetector extends CameraBoxSelectionDetector {

    private final Matrix4f invProjectionMatrix;
    
    private final Matrix4f invViewMatrix;

    private final Vector3f mouseDir;
    
    private final Vector4f tmpVec;

    public MouseBoxSelectionDetector() {
        super();
        invProjectionMatrix = new Matrix4f();
        invViewMatrix = new Matrix4f();
        mouseDir = new Vector3f();
        tmpVec = new Vector4f();
    }
    
    public Sprite selectGameItem(List<Sprite> gameItems, Window window, int mousePosX, int mousePosY, Camera camera) {
        // Transform mouse coordinates into normalized spaze [-1, 1]
        int wdwWitdh = window.getPaletteSize().getXInt();
        int wdwHeight = window.getPaletteSize().getYInt();
        
        float x = (float)(2 * mousePosX) / (float)wdwWitdh - 1.0f;
        float y = 1.0f - (float)(2 * mousePosY) / (float)wdwHeight;
        float z = -1.0f;

        invProjectionMatrix.set(window.getProjectionMatrix());
        invProjectionMatrix.invert();
        
        tmpVec.set(x, y, z, 1.0f);
        tmpVec.mul(invProjectionMatrix);
        tmpVec.z = -1.0f;
        tmpVec.w = 0.0f;
        
        Matrix4f viewMatrix = camera.getViewMatrix();
        invViewMatrix.set(viewMatrix);
        invViewMatrix.invert();
        tmpVec.mul(invViewMatrix);
        
        mouseDir.set(tmpVec.x, tmpVec.y, tmpVec.z);

        return selectGameItem(gameItems, camera.getPosition(), mouseDir);
    }
}
