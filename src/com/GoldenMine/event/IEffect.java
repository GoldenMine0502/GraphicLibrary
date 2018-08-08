package com.GoldenMine.event;

import com.GoldenMine.graphic.Palette;
import com.GoldenMine.graphic.elements.Sprite;

/**
 * Created by ehe12 on 2018-08-06.
 */
public interface IEffect {
    void onInvoked(Palette palette, Sprite sprite, double percent, Object... parameters);

    default float calculate(float start, float finish, double percent) {
        //System.out.println(start + ", " + finish + ", " + percent);
        return (float) (start + (finish-start) * percent/10000D);
    }
}
