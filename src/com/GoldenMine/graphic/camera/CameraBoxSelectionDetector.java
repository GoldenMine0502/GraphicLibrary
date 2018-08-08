package com.GoldenMine.graphic.camera;

import com.GoldenMine.graphic.elements.Clickable;
import com.GoldenMine.graphic.elements.Sprite;
import java.util.List;
import org.joml.Intersectionf;
import org.joml.Vector2f;
import org.joml.Vector3f;

public class CameraBoxSelectionDetector {

    private final Vector3f max;

    private final Vector3f min;

    private final Vector2f nearFar;

    private Vector3f dir;

    public CameraBoxSelectionDetector() {
        dir = new Vector3f();
        min = new Vector3f();
        max = new Vector3f();
        nearFar = new Vector2f();
    }

    public void selectGameItem(List<Sprite> gameItems, Camera camera) {
        dir = camera.getViewMatrix().positiveZ(dir).negate();
        selectGameItem(gameItems, camera.getPosition(), dir);
    }
    
    protected Sprite selectGameItem(List<Sprite> gameItems, Vector3f center, Vector3f dir) {
        Sprite selectedGameItem = null;
        float closestDistance = Float.POSITIVE_INFINITY;

        for (Sprite gameItem : gameItems) {
            if(gameItem instanceof Clickable)
                ((Clickable)gameItem).setClicked(false);
            min.set(gameItem.getPosition());
            max.set(gameItem.getPosition());
            min.add(-gameItem.getScale()/2, -gameItem.getScale()/2, -gameItem.getScale()/2);
            max.add(gameItem.getScale()/2, gameItem.getScale()/2, gameItem.getScale()/2);
            if (Intersectionf.intersectRayAab(center, dir, min, max, nearFar) && nearFar.x < closestDistance) {
                closestDistance = nearFar.x;
                selectedGameItem = gameItem;
            }
        }

        if (selectedGameItem != null) {
            if(selectedGameItem instanceof Clickable)
                ((Clickable)selectedGameItem).setClicked(true);
        }

        return selectedGameItem;
    }
}
