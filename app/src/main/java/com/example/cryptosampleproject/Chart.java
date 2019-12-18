package com.example.cryptosampleproject;

import android.util.Log;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

public class Chart extends Thread {

    private String apiKey = "fafc9fa6683ceebd2d0764d35c1c6b0366a08156d69828ce2cbfbe7738d38bbd";
    private String from;
    private String to;
    private List<Long> time = new ArrayList<>();
    private List<String> fTimes = new ArrayList<>();
    private List<Double> openData = new ArrayList<>();
    private List<Double> closeData = new ArrayList<>();
    private List<Double> highData = new ArrayList<>();
    private List<Double> lowData = new ArrayList<>();
    private String currentPrice;
    private String median;
    private String mktCap;

    @Override
    public void run() {

        refresh(from,to);
    }

    Chart(String from, String to) {

        this.from = from;
        this.to = to;

    }

    public void refresh(String from, String to) {

        String url = "https://min-api.cryptocompare.com/data/v2/histohour?fsym="+from+"&tsym="+to+"&limit=24&api_key=" + apiKey;
        try {
            URL obj = new URL(url);
            HttpURLConnection connection = (HttpURLConnection) obj.openConnection();
            connection.setRequestMethod("GET");

            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String inputLine;
            StringBuffer response = new StringBuffer();

            while ((inputLine = reader.readLine()) != null) {

                response.append(inputLine);
                System.out.println(inputLine);
            }
            reader.close();

            JsonParser parser = new JsonParser();
            JsonElement element = parser.parse(response.toString());
            JsonObject object = element.getAsJsonObject();
            object = object.getAsJsonObject("Data");
            JsonArray array = object.getAsJsonArray("Data");
            for (int i = 0; i < array.size(); i++) {
                JsonObject tempObject = array.get(i).getAsJsonObject();
                long tempTime = tempObject.getAsJsonPrimitive("time").getAsLong() * 1000;
                Date date = new Date(tempTime);
                DateFormat formatter = new SimpleDateFormat("HH:mm:ss");
                String formattedTime = formatter.format(date);
                fTimes.add(formattedTime);
                time.add(tempTime);
                Log.d("OPEN", String.valueOf(tempObject.getAsJsonPrimitive("open").getAsLong()));
                openData.add(tempObject.getAsJsonPrimitive("open").getAsDouble());
                closeData.add(tempObject.getAsJsonPrimitive("close").getAsDouble());
                highData.add(tempObject.getAsJsonPrimitive("high").getAsDouble());
                lowData.add(tempObject.getAsJsonPrimitive("low").getAsDouble());

            }
        }
        catch (Exception e) {

            e.printStackTrace();
        }

        try {
            url = "https://min-api.cryptocompare.com/data/pricemultifull?fsyms=" + from + "&tsyms=" + to + "&api_key=" + apiKey;
            URL obj = new URL(url);
            HttpURLConnection connection = (HttpURLConnection) obj.openConnection();
            connection.setRequestMethod("GET");

            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String inputLine;
            StringBuffer response = new StringBuffer();


            while ((inputLine = reader.readLine()) != null) {

                response.append(inputLine);
                System.out.println(inputLine);
            }
            reader.close();

            JsonParser parser = new JsonParser();
            JsonElement element = parser.parse(response.toString());
            JsonObject object = element.getAsJsonObject();
            object = object.getAsJsonObject("DISPLAY").getAsJsonObject(from).getAsJsonObject(to);
            currentPrice = object.getAsJsonPrimitive("PRICE").getAsString();
            median = object.getAsJsonPrimitive("MEDIAN").getAsString();
            mktCap = object.getAsJsonPrimitive("MKTCAP").getAsString();

        }
        catch (Exception e) {
            e.printStackTrace();
        }



    }

    public List<Long> getTime() {
        return time;
    }

    public List<Double> getOpenData() {
        return openData ;
    }

    public List<Double> getCloseData() {
        return closeData;
    }

    public List<Double> getHighData() {
        return highData;
    }

    public List<Double> getLowData() {
        return lowData;
    }

    public List<String> getfTimes() {
        return fTimes;
    }

    public String getFrom() {
        return from;
    }

    public String getTo() {
        return to;
    }

    public String getCurrentPrice() {
        return currentPrice;
    }

    public void setCurrentPrice(String currentPrice) {
        this.currentPrice = currentPrice;
    }

    public String getMedian() {
        return median;
    }

    public void setMedian(String median) {
        this.median = median;
    }

    public String getMktCap() {
        return mktCap;
    }

    public void setMktCap(String mktCap) {
        this.mktCap = mktCap;
    }
}
