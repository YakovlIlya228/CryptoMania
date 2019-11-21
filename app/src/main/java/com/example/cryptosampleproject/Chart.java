package com.example.cryptosampleproject;

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
    private List<Long> openData = new ArrayList<>();

    private List<Long> closeData = new ArrayList<>();
    private List<Long> highData = new ArrayList<>();
    private List<Long> lowData = new ArrayList<>();

    @Override
    public void run() {

        refresh();
    }

    Chart(String from, String to) {

        this.from = from;
        this.to = to;

    }

    public void refresh() {

        String url = "https://min-api.cryptocompare.com/data/v2/histohour?fsym=BTC&tsym=USD&limit=24&api_key=" + apiKey;
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
//                Date date = new Date(tempTime);
//                DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd 'at' HH:mm:ss");
//                String formattedTime = formatter.format(date);
                time.add(tempTime);
                openData.add(tempObject.getAsJsonPrimitive("open").getAsLong());
                closeData.add(tempObject.getAsJsonPrimitive("close").getAsLong());
                highData.add(tempObject.getAsJsonPrimitive("high").getAsLong());
                lowData.add(tempObject.getAsJsonPrimitive("low").getAsLong());

            }
        }
        catch (Exception e) {

            e.printStackTrace();
        }


    }

    public List<Long> getTime() {
        return time;
    }

    public List<Long> getOpenData() {
        return openData ;
    }

    public List<Long> getCloseData() {
        return closeData;
    }

    public List<Long> getHighData() {
        return highData;
    }

    public List<Long> getLowData() {
        return lowData;
    }
}
