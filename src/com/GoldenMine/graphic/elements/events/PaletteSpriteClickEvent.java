package com.GoldenMine.graphic.elements.events;

import com.GoldenMine.graphic.Palette;

/**
 * Created by ehe12 on 2018-08-28.
 */
public class PaletteSpriteClickEvent extends PaletteEvent {

    public PaletteSpriteClickEvent(Palette palette) {
        super(palette);


    }

    @Override
    public String getType() {
        return "click";
    }
}
