package com.GoldenMine.graphic.util;

import com.GoldenMine.event.IEffect;
import com.GoldenMine.graphic.elements.ObjectSprite;
import com.GoldenMine.graphic.elements.Sprite;
import com.GoldenMine.graphic.elements.SpriteData;
import com.GoldenMine.util.interval.Interval;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by ehe12 on 2018-08-14.
 */
public class SpriteGroup {
    List<Sprite> sprites = new ArrayList<Sprite>();
    HashMap<Sprite, SpriteData> spriteData = new HashMap<>();

    public SpriteGroup() {

    }

    public void addSprite(Sprite sprite, SpriteData data) {
        sprites.add(sprite);
        spriteData.put(sprite, data);
    }

    public List<Sprite> getSprites() {
        return sprites;
    }

    public void addEffects(IEffect effect, Interval interval, Object... parameters) {
        for(int i = 0; i < sprites.size(); i++) {
            spriteData.get(sprites.get(i)).addEffect(effect, new Interval(interval.getFinishWait(), interval.getFinishInterval(), interval.getCalculateAlgorithm()), parameters);
        }
    }

    public void setOpacity(float opacity) {
        for(int i = 0; i < sprites.size(); i++) {
            SpriteData data = spriteData.get(sprites.get(i));
            data.setOpacity(opacity);
        }
    }

    public void setEnabled(boolean istrue) {
        for(int i = 0; i < sprites.size(); i++) {
            SpriteData data = spriteData.get(sprites.get(i));
            data.setEnabled(istrue);
        }
    }
}
