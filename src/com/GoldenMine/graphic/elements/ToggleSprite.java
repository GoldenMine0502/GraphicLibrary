package com.GoldenMine.graphic.elements;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ehe12 on 2018-08-28.
 */
public class ToggleSprite extends Sprite {
    List<ObjectElement> textures = new ArrayList<>();

    int index = 0;

    public ToggleSprite(ObjectElement element) {
        addTexture(element);
    }

    public void addTexture(ObjectElement texture) {
        textures.add(texture);
    }

    public void setTexture(int index) {
        this.index = index;
    }

    @Override
    public ObjectElement getObjectElement() {
        return textures.get(index);
    }
}
