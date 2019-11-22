package com.example.cryptosampleproject;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.listener.OnChartGestureListener;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;

import java.util.ArrayList;
import java.util.List;

public class Linechart extends AppCompatActivity {

    private static final String TAG = "Linechart";

    private LineChart mChart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_linechart);

        mChart = findViewById(R.id.linechart);

//        mChart.setOnChartGestureListener(Linechart.this);
//        mChart.setOnChartValueSelectedListener(Linechart.this);

        mChart.setDragEnabled(true);
        mChart.setScaleEnabled(true);
        mChart.getDescription().setEnabled(false);

        Chart chart = new Chart("BTC", "USD");
        chart.start();
        try {
            chart.join();
            List<String> dates = chart.getfTimes();
            List<Long> values = chart.getOpenData();
            String[] arrayDates = dates.toArray(new String[dates.size()]);

            ArrayList<Entry> yValues = new ArrayList<>();

            for (int i = 0; i < dates.size(); i++) {
                yValues.add(new Entry(i,values.get(i)));
                Log.d("DATE", arrayDates[i]);
            }

            LineDataSet set1 = new LineDataSet(yValues, "Price of " + chart.getFrom() + " in " + chart.getTo());
            set1.setFillAlpha(110);
            set1.setColor(getResources().getColor(R.color.orange));
            set1.setLineWidth(5f);
            set1.setValueTextSize(10f);
            set1.setDrawFilled(true);
            set1.setFillColor(getResources().getColor(R.color.orange));

            ArrayList<ILineDataSet> dataSets = new ArrayList<>();
            dataSets.add(set1);

            LineData data = new LineData(dataSets);


            mChart.setData(data);


            XAxis xAxis = mChart.getXAxis();
            xAxis.setValueFormatter(new MyAxisValueFormatter(arrayDates));
            xAxis.setGranularity(1);
            xAxis.setTextSize(10f);
            xAxis.setPosition(XAxis.XAxisPosition.BOTH_SIDED);



        }
        catch (Exception e) {
            e.printStackTrace();
        }

    }

    public class MyAxisValueFormatter extends ValueFormatter {

        private String[] mValues;

        public MyAxisValueFormatter(String values[]) {
            this.mValues = values;
        }

        @Override
        public String getFormattedValue(float value) {
            return mValues[(int)value];
        }
    }
}
