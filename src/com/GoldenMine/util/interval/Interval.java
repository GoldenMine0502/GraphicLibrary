package com.GoldenMine.util.interval;

public class Interval {
    int wait;
    int interval;

    int finishWait;
    int finishInterval;

    double result = 0;

    IntervalCalculateModel calculate;

    public Interval(int finishWait, int finishInterval, IntervalCalculateModel model){
        this.finishInterval = finishInterval;
        this.finishWait = finishWait;
        calculate = model;
        //System.out.println("init");
    }

    public double getIntervalPercent() {
        //System.out.println(result);
        return result/finishInterval*10000;
    }

    public boolean addTick() {
        result+=calculate.getCurrentValue(interval, finishInterval);
        interval++;

        return isTick();
    }

    public boolean addWait() {
        wait++;

        return isWait();
    }

    public int getInterval() {
        return interval;
    }

    public int getWait() {
        return wait;
    }

    public int getFinishWait() {
        return finishWait;
    }

    public int getFinishInterval() {
        return finishInterval;
    }

    public boolean isWait() {
        return finishWait < wait;
    }

    public boolean isTick() {
        return finishInterval < interval;
    }
}
