package com.GoldenMine.graphic.elements;

import com.GoldenMine.graphic.event.SpriteClickedEvent;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by ehe12 on 2018-08-08.
 */
public class ButtonSprite extends Sprite implements Clickable {
    private static List<String> receiveEvents = new ArrayList<>();

    static {
        receiveEvents.add("click");
    }

    ObjectElement unclickedImage;
    ObjectElement clickedImage;

    boolean clicked = false;

    List<SpriteClickedEvent> events = new ArrayList<>();

    public ButtonSprite(ObjectElement unClicked, ObjectElement clicked) {
        unclickedImage = unClicked;
        clickedImage = clicked;
    }

    public void addButtonClickedEvent(SpriteClickedEvent event) {
        events.add(event);
    }

    public void setClicked(boolean clicked) {
        //System.out.println(clicked);
        this.clicked = clicked;

        for(int i = 0; i < events.size(); i++) {
            SpriteClickedEvent event = events.get(i);
            event.onClicked(clicked);
        }
        //System.out.println(clicked);
    }

    @Override
    public boolean getClicked() {
        return clicked;
    }


    @Override
    public ObjectElement getObjectElement() {
        return clicked ? clickedImage : unclickedImage;
    }

    @Override
    public List<String> getReceiveEvents() {
        return receiveEvents;
    }

    @Override
    public void invoke(String type, Object... objects) {
        switch(type) {
            case "click":
            {
                setClicked((boolean)objects[0]);
            }
                break;
        }
    }
}
