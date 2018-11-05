package com.example.zuhair.computationalassignment;

import com.github.mikephil.charting.data.Entry;
import java.util.ArrayList;

public class NumericalMethods {

    public NumericalMethods(){}

    public ArrayList<Float> eulerMethod(float x0, float y0, float h, float x){

        ArrayList<Float> fin = new ArrayList<>();

        float y = y0;
        float last;
        float step = (x - x0) / h;
        for(int i = 0; i <=h; i++) {

            float xi = x0 + i * step;
            fin.add(y);
            last = equation_helper(xi, y);
            y = y + step * last;
        }

        return fin;

    }

    public ArrayList<Float> improvedeEulerMethod(float x0, float y0, float h, float x){

        ArrayList<Float> fin = new ArrayList<>();


        float y = y0;
        float last = equation_helper(x0, y0);
        float xInCalc, yInCalc;
        float step = (x - x0) / h;

        for(int i = 0; i <= h; i++){

            float xi = x0 + i * step;
            fin.add(y);
            xInCalc = xi + step / 2;
            yInCalc = y + (step / 2) * last;
            y = y + step * equation_helper(xInCalc, yInCalc);
            last = equation_helper(xi + step, y);
        }

        return fin;
    }

    public ArrayList<Float> rungeKuttaMethod(float x0, float y0, float h, float x){

        ArrayList<Float> fin = new ArrayList<>();

        float y = y0;
        float last = equation_helper(x0, y0);
        float step = (x - x0) / h;

        for(int i = 0; i <= h; i++){

            float xi = x0 + i * step;
            fin.add(y);
            float k1 = last;
            double k2 = equation_helper(xi + step / 2, y + (step * k1) / 2);
            double k3 = equation_helper(xi + step / 2, (float) (y + (step * k2) / 2));
            double k4 = equation_helper(xi + step, (float) (y + step * k3));
            y = (float) (y + step / 6 * (k1 + 2 * k2 + 2 * k3 + k4));
            last = equation_helper(xi + step, y);
        }

        return fin;
    }

    public ArrayList<Float> exactSolution(float x0, float y0, float h, float x){

        float num = (2 * y0) - x0;
        float den = (float) (2 * x0 * Math.pow(2.71828182846, (2*x0)));
        float c = num / den;
        double step = (x - x0) / h;
        float y = y0;

        ArrayList<Float> fin = new ArrayList<>();

        for(int i = 0; i <=h; i++) {

            float xi = (float) (x0 + i * step);
            fin.add(y);
            y = (float) ((xi + step) * ((2*c*Math.pow(2.71828182846, (2*(xi + step)))) + 1)) / 2;
        }

        return fin;

    }

    public ArrayList<Entry> getMaxEulerError(double N0, double Nn, float N, float x0, float y0, float X) {

        ArrayList<Entry> fin = new ArrayList<>();
        ArrayList<Float> error;
        float oldValueOfN = N;

        for(double i = N0; i <= Nn; i++){

            error = new ArrayList<>();

            for(int z = 0; z < i; z++) {
                error.add(exactSolution(x0, y0, (float) i, X).get(z) - eulerMethod(x0, y0, (float) i, X).get(z));
            }

            double max_error = Double.MIN_VALUE;
            for(int j = 0; j < error.size(); j++){
                double currentData = error.get(j);
                max_error = Math.max(max_error, currentData);
            }

            fin.add(new Entry((float) i, (float) max_error));
        }

        return fin;
    }

    public ArrayList<Entry> getMaxImpEulerError(double N0, double Nn, float N, float x0, float y0, float X) {

        ArrayList<Entry> fin = new ArrayList<>();
        ArrayList<Float> error;
        float oldValueOfN = N;

        for(double i = N0; i <= Nn; i++){

            error = new ArrayList<>();

            for(int z = 0; z < i; z++) {
                error.add(exactSolution(x0, y0, (float) i, X).get(z) - improvedeEulerMethod(x0, y0, (float) i, X).get(z));
            }

            double max_error = Double.MIN_VALUE;
            for(int j = 0; j < error.size(); j++){
                double currentData = error.get(j);
                max_error = Math.max(max_error, currentData);
            }

            fin.add(new Entry((float) i, (float) max_error));
        }

        return fin;
    }

    public ArrayList<Entry> getMaxRungeKuttaError(double N0, double Nn, float N, float x0, float y0, float X) {

        ArrayList<Entry> fin = new ArrayList<>();
        ArrayList<Float> error;
        float oldValueOfN = N;

        for(double i = N0; i <= Nn; i++){

            error = new ArrayList<>();

            for(int z = 0; z < i; z++) {
                error.add(exactSolution(x0, y0, (float) i, X).get(z) - rungeKuttaMethod(x0, y0, (float) i, X).get(z));
            }

            double max_error = Double.MIN_VALUE;
            for(int j = 0; j < error.size(); j++){
                double currentData = error.get(j);
                max_error = Math.max(max_error, currentData);
            }

            fin.add(new Entry((float) i, (float) max_error));
        }

        return fin;
    }


    float equation_helper(float x, float y)
    {
        return (-x + ((y*(2*x + 1))/x));
    }

}
