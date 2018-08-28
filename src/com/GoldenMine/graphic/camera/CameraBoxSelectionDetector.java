package com.GoldenMine.graphic.camera;

import com.GoldenMine.graphic.Palette;
import com.GoldenMine.graphic.elements.Clickable;
import com.GoldenMine.graphic.elements.Sprite;
import com.GoldenMine.graphic.elements.SpriteData;
import java.util.Calendar;
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

    public void selectGameItem(Palette palette, Camera camera) {
        dir = camera.getViewMatrix().positiveZ(dir).negate();
        selectGameItem(palette, camera.getPosition(), dir);
    }
    
    protected Sprite selectGameItem(Palette palette, Vector3f center, Vector3f dir) {
        Sprite selectedGameItem = null;
        float closestDistance = Float.POSITIVE_INFINITY;

        for (Sprite gameItem : palette.getSprites()) {
            SpriteData data = palette.getSpriteData(gameItem);
            if(!data.isEnabled()) {
                continue;
            }
            if(gameItem instanceof Clickable) {
                if(((Clickable) gameItem).getClicked())
                    ((Clickable) gameItem).setClicked(false);
            }
            min.set(gameItem.getPosition());
            max.set(gameItem.getPosition());

            //gameItem.getObjectElement().getPositions();

            float xMax = gameItem.getObjectElement().getXMaxSize();
            float yMax = gameItem.getObjectElement().getYMaxSize();
            float zMax = gameItem.getObjectElement().getZMaxSize();



            //System.out.println(xMax + ", " + yMax + ", " + zMax);

            //min.add(-gameItem.getScale()*xMax, -gameItem.getScale()*yMax, -gameItem.getScale()*zMax);
            //max.add(gameItem.getScale()*xMax, gameItem.getScale()*yMax, gameItem.getScale()*zMax);

            //System.out.println(min + ", " + max);

            min.add(-gameItem.getScale()*xMax, -(float)gameItem.getScale()*yMax, -gameItem.getScale()*zMax-0.01f);
            max.add(gameItem.getScale()*xMax, (float)gameItem.getScale()*yMax, gameItem.getScale()*zMax);

            //Intersectionf.intersect
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
