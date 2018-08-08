package com.GoldenMine.graphic.elements;

/**
 * Created by ehe12 on 2018-08-05.
 */
public class ObjectSprite extends Sprite {
    private ObjectElement element;

    public ObjectSprite(ObjectElement element) {
        this.element = element;
        setPosition(0,0,0);
    }

    public ObjectElement getObjectElement() {
        return element;
    }
}
