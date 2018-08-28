package com.GoldenMine.graphic.elements.events;

import com.GoldenMine.graphic.Palette;
import com.GoldenMine.graphic.elements.Sprite;

/**
 * Created by ehe12 on 2018-08-28.
 */
public abstract class PaletteEvent {
    Palette palette;

    public PaletteEvent(Palette palette) {
        this.palette = palette;
    }



    public abstract String getType();
}
