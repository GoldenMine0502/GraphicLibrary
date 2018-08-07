package com.GoldenMine.graphic;

import com.GoldenMine.event.IEffect;
import com.GoldenMine.util.EffectData;
import com.GoldenMine.util.interval.Interval;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by ehe12 on 2018-08-06.
 */
public class SpriteData {
    //HashMap<IEffect, Interval> events = new HashMap<>();
    List<EffectData> effects = new ArrayList<>();

    ShaderProgram program;

    public void eventTickPassed() {
        //int test = 0;
        for(int index = 0; index < effects.size(); index++){
            EffectData effectData = effects.get(index);
            //IEffect effect = effectData.getEffect();
            Interval interval = effectData.getInterval();
            //Object[] parameters = effectData.getParameters();

            if(interval.addWait()) {
                if(interval.addTick()) {
                    effects.remove(index);
                    index--;
                }
                //test++;
            }
        }
        //System.out.println(effects.size() + ", " + test);
    }

    public void addEffect(EffectData data) {
        effects.add(data);
    }

    public void addEffect(IEffect effect, Interval interval, Object... parameters) {
        addEffect(new EffectData(effect, interval, parameters));
    }

    public List<EffectData> getEffects() {
        return effects;
    }
}
