package com.GoldenMine.util;

import com.GoldenMine.event.IEffect;
import com.GoldenMine.util.interval.Interval;

public class EffectData {
    IEffect effect;
    Interval interval;
    Object[] parameters;

    public EffectData(IEffect effect, Interval interval, Object... parameters) {
        this.effect = effect;
        this.interval = interval;
        this.parameters = parameters;
    }

    public IEffect getEffect() {
        return effect;
    }

    public Interval getInterval() {
        return interval;
    }

    public Object[] getParameters() {
        return parameters;
    }

}