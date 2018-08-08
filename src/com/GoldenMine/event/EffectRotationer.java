package com.GoldenMine.event;

import com.GoldenMine.graphic.Palette;
import com.GoldenMine.graphic.elements.Sprite;

/**
 * Created by ehe12 on 2018-08-06.
 */
public class EffectRotationer implements IEffect {
    @Override
    public void onInvoked(Palette palette, Sprite sprite, double percent, Object... parameters) {
        //System.out.println("invoke" + System.currentTimeMillis());
        int xChange = -1;
        int yChange = -1;
        int zChange = -1;

        String type = (String) parameters[0];

        for(int i = 0; i < type.length(); i++) {
            char ch = type.charAt(i);

            switch(ch) {
                case 'x':
                    xChange = i;
                    break;
                case 'y':
                    yChange = i;
                    break;
                case 'z':
                    zChange = i;
                    break;
            }
        }

        float xAngle = xChange != -1 ? calculate((float)parameters[1 + 2*xChange], (float)parameters[1 + 2*xChange + 1], percent) : sprite.getRotation().x;
        float yAngle = yChange != -1 ? calculate((float)parameters[1 + 2*yChange], (float)parameters[1 + 2*yChange + 1], percent) : sprite.getRotation().y;
        float zAngle = zChange != -1 ? calculate((float)parameters[1 + 2*zChange], (float)parameters[1 + 2*zChange + 1], percent) : sprite.getRotation().z;

        //System.out.println(xChange + ", " + yChange + ", " + zChange + ", " + xAngle + ", " + yAngle + ", " + zAngle);

        sprite.getRotation().x = xAngle;
        sprite.getRotation().y = yAngle;
        sprite.getRotation().z = zAngle;
    }
}
