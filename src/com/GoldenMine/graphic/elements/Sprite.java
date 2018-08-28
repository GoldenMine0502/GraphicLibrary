package com.GoldenMine.graphic.elements;

import java.util.List;

/**
 * Created by ehe12 on 2018-08-08.
 */
public abstract class Sprite extends Object3D {
    private static final int MINUS_Z = 2;

    public abstract ObjectElement getObjectElement();

    public abstract List<String> getReceiveEvents();

    public abstract void invoke(String type, Object... objects);

    @Override
    public void setPosition(float x, float y, float z) {
        super.setPosition(x, y, z-MINUS_Z);
    }
}
