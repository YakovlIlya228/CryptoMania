package com.example.cryptosampleproject;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.anychart.AnyChart;
import com.anychart.AnyChartView;
import com.anychart.chart.common.dataentry.DataEntry;
import com.anychart.chart.common.dataentry.HighLowDataEntry;
import com.anychart.charts.Stock;
import com.anychart.core.stock.Plot;
import com.anychart.data.Table;
import com.anychart.data.TableMapping;
import com.anychart.enums.StockSeriesType;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        AnyChartView anyChartView = findViewById(R.id.cryptochart);
        Table table = Table.instantiate("x");
        table.addData(getData());

        TableMapping mapping = table.mapAs("{open: 'open', high: 'high', low: 'low', close: 'close'}");

        Stock stock = AnyChart.stock();

        Plot plot = stock.plot(0);
        plot.yGrid(true)
                .xGrid(true)
                .yMinorGrid(true)
                .xMinorGrid(true);

        plot.ema(table.mapAs("{value: 'close'}"), 20d, StockSeriesType.LINE);

        plot.ohlc(mapping)
                .name("CSCO")
                .legendItem("{\n" +
                        "        iconType: 'rising-falling'\n" +
                        "      }");

        stock.scroller().ohlc(mapping);

        anyChartView.setChart(stock);

    }

    private List<DataEntry> getData() {


        Chart chart = new Chart("BTC", "USD");
        chart.start();
        try {
            chart.join();
        }
        catch (Exception e) {
            e.printStackTrace();
        }

        List<Long> time = chart.getTime();
        List<Double> open = chart.getOpenData();
        List<Double> close = chart.getCloseData();
        List<Double> high = chart.getHighData();
        List<Double> low = chart.getLowData();
        List<DataEntry> data = new ArrayList<>();
        for (int i = 0; i < low.size(); i++) {

            Long timeTemp = time.get(i);
            Double openTemp = open.get(i);
            Double closeTemp = close.get(i);
            Double highTemp = high.get(i);
            Double lowTemp = low.get(i);
            data.add(new OHCLDataEntry(timeTemp, (double) openTemp, (double) highTemp, (double) lowTemp, (double) closeTemp));

        }

        return data;

    }

    private class OHCLDataEntry extends HighLowDataEntry {
        OHCLDataEntry(Long x, Double open, Double high, Double low, Double close) {
            super(x, high, low);
            setValue("open", open);
            setValue("close", close);

        }
    }
}
