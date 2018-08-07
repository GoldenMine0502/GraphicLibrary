package com.GoldenMine.util.interval;

public class CalculateModelDefault implements IntervalCalculateModel {
    @Override
    public double getCurrentValue(int current, int finish) {
        return 1;
    }
}
