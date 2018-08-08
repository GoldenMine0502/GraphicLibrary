package com.GoldenMine.graphic.elements;

import com.GoldenMine.graphic.event.SpriteClickedEvent;

/**
 * Created by ehe12 on 2018-08-08.
 */
public class ButtonSprite extends Sprite implements Clickable {
    ObjectElement unclickedImage;
    ObjectElement clickedImage;

    boolean clicked = false;

    SpriteClickedEvent event;

    public ButtonSprite(ObjectElement unClicked, ObjectElement clicked) {
        unclickedImage = unClicked;
        clickedImage = clicked;
    }

    public void addButtonClickedEvent(SpriteClickedEvent event) {
        this.event = event;
    }

    public void setClicked(boolean clicked) {
        this.clicked = clicked;
        if(event!=null)
            event.onClicked(clicked);
    }

    @Override
    public boolean getClicked() {
        return clicked;
    }


    @Override
    public ObjectElement getObjectElement() {
        return clicked ? clickedImage : unclickedImage;
    }
}
