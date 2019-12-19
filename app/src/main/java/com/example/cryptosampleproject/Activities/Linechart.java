package com.example.cryptosampleproject.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;
import android.widget.TextView;

import com.example.cryptosampleproject.R;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;

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

            String median = chart.getMedian(); //медиана
            String[] mp = median.split(" ");
            median = "";
            for (int i = 1; i < mp.length; i++) {
                median = median + mp[i] + " ";
            }
            median = median.replaceAll(",", "");
            TextView mdn = findViewById(R.id.median);
            mdn.setText(median);

            String mktcap = chart.getMktCap(); //капитализация
            String[] capp = mktcap.split(" ");
            mktcap = "";
            for (int i = 1; i < capp.length; i++) {
                mktcap = mktcap + capp[i] + " ";
            }
            TextView cap = findViewById(R.id.marketCap);
            cap.setText(mktcap);

            List<Double> highs = chart.getHighData(); //максимум
            Double max = new Double(-10000);
            for (int i = 0; i < highs.size(); i++) {
                if (highs.get(i) > max)
                    max = highs.get(i);
            }
            TextView maximum = findViewById(R.id.hour24High);
            maximum.setText(Double.toString(max));

            List<Double> lows = chart.getLowData(); //минимум
            Double min = new Double(20000000);
            for (int i = 0; i < lows.size(); i++) {
                if (lows.get(i) < min)
                    min = lows.get(i);
            }
            TextView minimum = findViewById(R.id.hour24Low);
            minimum.setText(Double.toString(min));

            List<String> dates = chart.getfTimes();
            List<Double> values = chart.getOpenData();

            String[] arrayDates = dates.toArray(new String[dates.size()]);

            ArrayList<Entry> yValues = new ArrayList<>();

            for (int i = 0; i < dates.size(); i++) {
                yValues.add(new Entry(i, values.get(i).floatValue()));
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
