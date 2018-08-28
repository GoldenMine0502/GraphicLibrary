package com.GoldenMine.event;

import com.GoldenMine.graphic.Palette;
import com.GoldenMine.graphic.elements.Sprite;
import com.GoldenMine.graphic.elements.SpriteData;

/**
 * Created by ehe12 on 2018-08-08.
 */
public class EffectScaler implements IEffect {
    @Override
    public void onInvoked(Palette palette, Sprite sprite, SpriteData spriteData, double percent, Object... parameters) {
        sprite.setScale(calculate((float) parameters[0], (float) parameters[1], percent));
    }
}
