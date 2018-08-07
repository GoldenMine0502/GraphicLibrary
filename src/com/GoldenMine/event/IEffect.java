package com.GoldenMine.event;

import com.GoldenMine.graphic.ObjectSprite;
import com.GoldenMine.graphic.Palette;

/**
 * Created by ehe12 on 2018-08-06.
 */
public interface IEffect {
    void onInvoked(Palette palette, ObjectSprite sprite, double percent, Object... parameters);
}
