package com.example.zuhair.computationalassignment;

import android.support.v4.app.Fragment;
import android.os.Bundle;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;

import android.graphics.Color;
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

public class GraphTabClass extends Fragment {

    LineChart lineChart;
    public static EditText valueX0;
    public static EditText valueY0;
    public static EditText valueX;
    public static EditText valueH;
    CheckBox checkEuler, checkImpEuler, checkRungeKutta;
    Button buttonRefresh;
    NumericalMethods obj;
    float x0, y0, h, x;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_graphs, container, false);


        valueX0 = (EditText) rootView.findViewById(R.id.editTextX0);
        valueY0 = (EditText) rootView.findViewById(R.id.editTextY0);
        valueX = (EditText) rootView.findViewById(R.id.editTextX);
        valueH = (EditText) rootView.findViewById(R.id.editTextH);

        checkEuler = (CheckBox) rootView.findViewById(R.id.checkBoxEul);
        checkImpEuler = (CheckBox) rootView.findViewById(R.id.checkBoxImp);
        checkRungeKutta = (CheckBox) rootView.findViewById(R.id.checkBoxRunge);

        obj = new NumericalMethods();
        lineChart = (LineChart) rootView.findViewById(R.id.lineChart);

        if(valueX0.getText().toString().equals("") || valueY0.getText().toString().equals("") || valueH.getText().toString().equals("") || valueX.getText().toString().equals("")) {
            generateGraph(1, 1, 99, 9.5f);
        }

        buttonRefresh = (Button) rootView.findViewById(R.id.refresh);
        buttonRefresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(!valueX0.getText().toString().equals("") && !valueY0.getText().toString().equals("") && !valueH.getText().toString().equals("") && !valueX.getText().toString().equals("")){
                    x0 = Float.parseFloat(valueX0.getText().toString());
                    y0 = Float.parseFloat(valueY0.getText().toString());
                    x = Float.parseFloat(valueX.getText().toString());
                    h = Float.parseFloat(valueH.getText().toString());
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

        ArrayList<Float> Exact = obj.exactSolution(x0, y0, h, X);
        ArrayList<Float> Euler = obj.eulerMethod(x0, y0, h, X);
        ArrayList<Float> ImpEuler = obj.improvedeEulerMethod(x0, y0, h, X);
        ArrayList<Float> RungeKutta = obj.rungeKuttaMethod(x0, y0, h, X);

        ArrayList<Entry> yAXESEuler = new ArrayList<>();
        ArrayList<Entry> yAXESImprovedEuler = new ArrayList<>();
        ArrayList<Entry> yAXESRungeKutta = new ArrayList<>();
        ArrayList<Entry> yAXESImprovedExactSol = new ArrayList<>();

        XAxis xAxis = lineChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        final String[] x_axis = new String[Euler.size()];

        float local_x0 = x0;
        float step = (X - x0) / h;

        for(int x = 0; x <= h; x++){

            float xi = x0 + x * step;
            yAXESEuler.add(new Entry(xi, Euler.get(x)));
            yAXESImprovedEuler.add(new Entry(xi, ImpEuler.get(x)));
            yAXESRungeKutta.add(new Entry(xi, RungeKutta.get(x)));
            yAXESImprovedExactSol.add(new Entry(xi, Exact.get(x)));

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
        xAxis.setAxisMinValue(x0);
        xAxis.setAxisMaxValue(X+1);
        xAxis.setGranularity(1);

        ArrayList<ILineDataSet> lineDataSets = new ArrayList<>();

        LineDataSet lineDataSet1 = new LineDataSet(yAXESEuler,"Euler");
        lineDataSet1.setDrawCircles(false);
        lineDataSet1.setCircleColor(Color.GREEN);
        lineDataSet1.setDrawValues(false);
        lineDataSet1.setLineWidth(2);
        lineDataSet1.setColor(Color.GREEN);

        LineDataSet lineDataSet2 = new LineDataSet(yAXESImprovedEuler,"Improved Euler");
        lineDataSet2.setDrawCircles(false);
        lineDataSet2.setCircleColor(Color.RED);
        lineDataSet2.setDrawValues(false);
        lineDataSet2.setLineWidth(2);
        lineDataSet2.setColor(Color.RED);

        LineDataSet lineDataSet3 = new LineDataSet(yAXESRungeKutta,"Runge Kutta");
        lineDataSet3.setDrawCircles(false);
        lineDataSet3.setCircleColor(Color.YELLOW);
        lineDataSet3.setDrawValues(false);
        lineDataSet3.setLineWidth(3);
        lineDataSet3.setColor(Color.YELLOW);

        LineDataSet lineDataSet4 = new LineDataSet(yAXESImprovedExactSol,"Exact");
    //    lineDataSet4.setDrawCircles(false);
        lineDataSet4.setCircleColor(Color.BLUE);
        lineDataSet4.setDrawValues(false);
        lineDataSet4.setLineWidth(3);
        lineDataSet4.setColor(Color.BLUE);

        lineDataSets.add(lineDataSet4);

        if(checkEuler.isChecked()){
            lineDataSets.add(lineDataSet1);
        }

        if(checkImpEuler.isChecked()){
            lineDataSets.add(lineDataSet2);

        }

        if(checkRungeKutta.isChecked()){
            lineDataSets.add(lineDataSet3);

        }

        YAxis yAxisRight = lineChart.getAxisRight();
        yAxisRight.setEnabled(false);

       YAxis yAxisLeft = lineChart.getAxisLeft();
       yAxisLeft.setEnabled(false);

        lineChart.getDescription().setEnabled(false);
        lineChart.setData(new LineData(lineDataSets));
        lineChart.animateX(500);
        lineChart.setVisibleXRangeMaximum(65f);
        lineChart.setNoDataText("Please press REFRESH button on the top right to display the graph.");
    //    lineChart.setNoDataTextColor();
        lineChart.invalidate();

    }

}
