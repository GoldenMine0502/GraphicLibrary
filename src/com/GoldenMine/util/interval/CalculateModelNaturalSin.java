package com.GoldenMine.util.interval;

/**
 * Created by ehe12 on 2018-07-18.
 */
public class CalculateModelNaturalSin implements IntervalCalculateModel {
    @Override
    public double getCurrentValue(int current, int finish) {
        //System.out.println(Math.PI / 2D * Math.sin(Math.PI * ((double) current / finish) / 10000D));
        return ((Math.PI / 2D) * Math.sin(Math.PI * ((double) current / finish)));
    }

    public static void main(String[] args) {
        double result = 0;

        double plus = 0.1;

        double value = 11234;

        for(double i = 1; i <= value; i+=plus) {
            result += (Math.PI / 2D) * Math.sin( ((i / value * Math.PI))) * plus;

            /*if(i<=5000) {
                //1~5000
                //y=1/90 * x
                result += 1D/2500D * i; // percent 5000일때 기울기 2
            } else {
                result += 1D/2500D * (10000-i); // percent 0~10000
                //5001~10000
                //y=-1/90 * x + 4
                // percent 10000일때 기울기 0
            }*/
        }

        System.out.println(result);
    }
}
