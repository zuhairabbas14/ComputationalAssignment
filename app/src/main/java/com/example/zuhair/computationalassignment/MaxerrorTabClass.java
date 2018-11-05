package com.example.zuhair.computationalassignment;

import android.graphics.Color;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;

import java.util.ArrayList;

public class MaxerrorTabClass extends Fragment {

    LineChart lineChart;
    EditText valueN0;
    EditText valueNn;
    EditText valueX0;
    EditText valueY0;
    EditText valueX;
    EditText valueH;
    Button buttonRefresh;
    float x0, y0, h, x, N0, Nn;
    NumericalMethods obj;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_maxerror, container, false);

        obj = new NumericalMethods();
        lineChart = (LineChart) rootView.findViewById(R.id.lineChart);

        valueN0 = (EditText) rootView.findViewById(R.id.editTextN0);
        valueNn = (EditText) rootView.findViewById(R.id.editTextNn);

        valueX0 = GraphTabClass.valueX0;
        valueY0 = GraphTabClass.valueY0;
        valueX = GraphTabClass.valueX;
        valueH = GraphTabClass.valueH;

        buttonRefresh = (Button) rootView.findViewById(R.id.refresh);
        buttonRefresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                valueX0 = GraphTabClass.valueX0;
                valueY0 = GraphTabClass.valueY0;
                valueX = GraphTabClass.valueX;
                valueH = GraphTabClass.valueH;

                if(valueN0.getText().toString().equals("") || valueNn.getText().toString().equals("")) {
                    Toast.makeText(getContext(), "Please Enter the values!", Toast.LENGTH_SHORT).show();
                    return;
                }

                if(!valueX0.getText().toString().equals("") && !valueY0.getText().toString().equals("") && !valueH.getText().toString().equals("") && !valueX.getText().toString().equals("")){

                    x0 = Float.parseFloat(valueX0.getText().toString());
                    y0 = Float.parseFloat(valueY0.getText().toString());
                    x = Float.parseFloat(valueX.getText().toString());
                    h = Float.parseFloat(valueH.getText().toString());
                }

                if(!valueN0.getText().toString().equals("") && !valueNn.getText().toString().equals("")){
                    N0 = Float.parseFloat(valueN0.getText().toString());
                    Nn = Float.parseFloat(valueNn.getText().toString());
                }

                if(valueX0.getText().toString().equals("") || valueY0.getText().toString().equals("") || valueH.getText().toString().equals("") || valueX.getText().toString().equals("")) {
                    Toast.makeText(getContext(), "Incomplete Initial Conditions! \n Using default values.", Toast.LENGTH_SHORT).show();
                    generateGraph(1, 1, 99, 9.5f);

                } else {

                    if(obj.exactSolution(x0, y0, h, x).get(2).isInfinite() || obj.exactSolution(x0, y0, h, x).get(2).isNaN()){

                        Toast.makeText(getContext(), "Undefined values!", Toast.LENGTH_SHORT).show();

                    }  else {

                        generateGraph(x0, y0, h, x);
                    }
                }
            }
        });

        return rootView;
    }

    // GRAPH MAKING STARTS HERE

    public void generateGraph(float x0, float y0, float h, float X){

        lineChart.fitScreen();

        ArrayList<Entry> yAXESEuler;
        ArrayList<Entry> yAXESImprovedEuler;
        ArrayList<Entry> yAXESRungeKutta;

        yAXESEuler = obj.getMaxEulerError(N0, Nn, h, x0, y0, X);
        yAXESImprovedEuler = obj.getMaxImpEulerError(N0, Nn, h, x0, y0, X);
        yAXESRungeKutta = obj.getMaxRungeKuttaError(N0, Nn, h, x0, y0, X);

        XAxis xAxis = lineChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        final String[] x_axis = new String[ErrorTabClass.EulerError.size()];

        float local_x0 = x0;

        for(int x = 0; x < h; x++){

            x_axis[x] = String.valueOf(local_x0);
            local_x0 += 0.1;
        }

        IAxisValueFormatter formatter = new IAxisValueFormatter() {
            @Override
            public String getFormattedValue(float value, AxisBase axis) {
                return x_axis[(int) value];
            }
        };

        xAxis.setGranularityEnabled(true);
        xAxis.setLabelCount((int) X);
        xAxis.setAxisMinValue(N0);
        xAxis.setAxisMaxValue(Nn);
        xAxis.setGranularity(1);

        ArrayList<ILineDataSet> lineDataSets = new ArrayList<>();

        LineDataSet lineDataSet1 = new LineDataSet(yAXESEuler,"Euler");
        //    lineDataSet1.setDrawCircles(false);
        lineDataSet1.setCircleColor(Color.GREEN);
        lineDataSet1.setDrawValues(false);
        lineDataSet1.setLineWidth(2);
        lineDataSet1.setColor(Color.GREEN);

        LineDataSet lineDataSet2 = new LineDataSet(yAXESImprovedEuler,"Improved Euler");
    //    lineDataSet2.setDrawCircles(false);
        lineDataSet2.setCircleColor(Color.RED);
        lineDataSet2.setDrawValues(false);
        lineDataSet2.setLineWidth(2);
        lineDataSet2.setColor(Color.RED);

        LineDataSet lineDataSet3 = new LineDataSet(yAXESRungeKutta,"Runge Kutta");
    //    lineDataSet3.setDrawCircles(false);
        lineDataSet3.setCircleColor(Color.YELLOW);
        lineDataSet3.setDrawValues(false);
        lineDataSet3.setLineWidth(3);
        lineDataSet3.setColor(Color.YELLOW);

        lineDataSets.add(lineDataSet1);

        lineDataSets.add(lineDataSet2);

        lineDataSets.add(lineDataSet3);


        YAxis yAxisRight = lineChart.getAxisRight();
        yAxisRight.setEnabled(false);

        YAxis yAxisLeft = lineChart.getAxisLeft();
        yAxisLeft.setEnabled(false);

        lineChart.getDescription().setEnabled(false);
        lineChart.setData(new LineData(lineDataSets));
        lineChart.animateX(500);
        lineChart.setVisibleXRangeMaximum(65f);
        lineChart.invalidate();
    }

}