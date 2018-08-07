package com.GoldenMine.util.interval;

public class CalculateModelNatural implements IntervalCalculateModel {
    @Override
    public double getCurrentValue(int current, int finish) {
        double percent = (double)current/finish*10000;
        if(percent<=5000) {
            //1~5000
            //y=1/90 * x
            return 1D/2500D * percent; // percent 5000일때 2
        } else {
            return 1D/2500D * (10000-percent); // percent 0~10000
            //5001~10000
            //y=-1/90 * x + 4
            // percent 10000일때 기울기 0
        }
    }
}
