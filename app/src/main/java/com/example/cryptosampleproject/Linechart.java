package com.example.cryptosampleproject;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

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
        TextView from = findViewById(R.id.from);
        TextView to = findViewById(R.id.to);
        TextView price = findViewById(R.id.price);
//        mChart.setOnChartGestureListener(Linechart.this);
//        mChart.setOnChartValueSelectedListener(Linechart.this);

        mChart.setDragEnabled(true);
        mChart.setScaleEnabled(true);
        mChart.getDescription().setEnabled(false);
        Bundle codes = getIntent().getExtras();
        from.setText(codes.getString("codeFrom"));
        to.setText(codes.getString("codeTo").toUpperCase());
        price.setText(codes.getString("price"));
        Chart chart = new Chart(codes.getString("codeFrom"), codes.getString("codeTo").toUpperCase());
        chart.start();
        try {
            chart.join();
            List<String> dates = chart.getfTimes();
            List<Long> values = chart.getOpenData();
            String[] arrayDates = dates.toArray(new String[dates.size()]);

            ArrayList<Entry> yValues = new ArrayList<>();

            for (int i = 0; i < dates.size(); i++) {
                yValues.add(new Entry(i,values.get(i)));
//                Log.d("DATE", arrayDates[i]);
            }

            LineDataSet set1 = new LineDataSet(yValues, "Price of " + chart.getFrom() + " in " + chart.getTo());
            set1.setFillAlpha(150);
            set1.setHighlightEnabled(false);
//            set1.setColor(getResources().getColor(R.color.orange));
            set1.setLineWidth(1f);
            set1.setColor(Color.WHITE);
            set1.setDrawValues(false);
            set1.setValueTextSize(10f);
            set1.setDrawFilled(true);
            if(codes.getChar("change")=='-'){
                set1.setFillDrawable(getResources().getDrawable(R.drawable.gradient_color_red));
                set1.setColor(getResources().getColor(R.color.contrastRed));
            }
            else{
                set1.setFillDrawable(getResources().getDrawable(R.drawable.gradient_color_green));
                set1.setColor(getResources().getColor(R.color.contrastGreen));
            }
            set1.setMode(LineDataSet.Mode.CUBIC_BEZIER);
            ArrayList<ILineDataSet> dataSets = new ArrayList<>();
            dataSets.add(set1);

            LineData data = new LineData(dataSets);

            mChart.setData(data);
            mChart.getLegend().setTextColor(Color.WHITE);
            XAxis xAxis = mChart.getXAxis();
            xAxis.setTextColor(Color.WHITE);
            xAxis.setAxisLineColor(Color.WHITE);
            YAxis yAxis = mChart.getAxis(YAxis.AxisDependency.RIGHT);
            yAxis.setDrawAxisLine(false);
            yAxis.setDrawLabels(false);
            yAxis = mChart.getAxis(YAxis.AxisDependency.LEFT);
            yAxis.setTextColor(Color.WHITE);
            yAxis.setAxisLineColor(Color.WHITE);
            yAxis.setTextSize(11f);
            xAxis.setValueFormatter(new MyAxisValueFormatter(arrayDates));
            xAxis.setGranularity(1);
            xAxis.setTextSize(11f);
            xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);


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
