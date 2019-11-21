package com.example.cryptosampleproject;

import com.google.gson.*;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.*;

public class ExchangeInfo {

    private String api_key = "fafc9fa6683ceebd2d0764d35c1c6b0366a08156d69828ce2cbfbe7738d38bbd"; //ключ API
    private String from; //валюта, стоимость которой интересует пользователя
    private String to; //t - валюта, в которую конвертируется стоимость
    private String price; //текущая цена криптовалюты
    private String lastTimeUpdated; //последнее время обновления
    private long lastTimeUpdatedMillis; //время последнего обновления в миллисекундах
    private String volume24Hours; //объем на рынке за 24 часа
    private String volumeDay; //объем на рынке за день
    private String volumeHour; //объем на рынке за час
    private String highestPrice24Hours; //наибольшая цена за 24 часа
    private String highestPriceDay; //наибольшая цена за день
    private String highestPriceHour; //наибольшая цена за час
    private String lowestPrice24Hours; //наименьшая цена за 24 часа
    private String lowestPriceDay; //наименьшая цена за день
    private String lowestPriceHour; //наименьшая цена за час
    private String priceOpen; //цена на момент открытия торгов
    private String mktCap; //капитализация криптовалюты
    private String change24Hours; //изменение цены за 24 часа
    private String changePct24Hours; //изменение цены за 24 часа в процентах
    private String pictureURL; //ссылка для загрузки изображения

    public ExchangeInfo(String f, String t) {

        //Конструктор класса. Принимает на вход обозначения двух валют.
        //f - валюта, стоимость которой интересует пользователя
        //t - валюта, в которую конвертируется стоимость

        from = f;
        to = t;
        refresh();

    }

    public void refreshExchangeInfo(String from, String to){
        this.from = from;
        this.to = to;
        refresh();
    }
    //Функция для получения списка всех доступных монет
    //Список получается очень большой, его загрузка может занимать много времени
    //Вывод всех элементов у меня занял где-то 2-3 минуты
    public List<String> getAllCoins() {

        String url = "https://min-api.cryptocompare.com/data/all/coinlist" + "?api_key=" + api_key;
        List<String> coins = new ArrayList<>();
        try {
            URL obj = new URL(url);
            HttpURLConnection connection = (HttpURLConnection) obj.openConnection();
            connection.setRequestMethod("GET");

            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String inputLine;
            StringBuffer response = new StringBuffer();

            while ((inputLine = reader.readLine()) != null) {
                response.append(inputLine);
            }
            reader.close();

            JsonParser parser = new JsonParser();
            JsonElement element = parser.parse(response.toString());
            JsonObject object = element.getAsJsonObject();
            object = object.getAsJsonObject("Data");
            for (int i = 0; i < object.size(); i++) {

                for(Map.Entry<String, JsonElement> entry : object.entrySet()) {

                    String coin = entry.getKey();
                    coins.add(coin);

                }
            }

        }
        catch (Exception e) {
            e.printStackTrace();

        }

        return coins;

    }

    //Функция для обновления информации о валютной паре
    public void refresh() {

        lastTimeUpdatedMillis = System.currentTimeMillis();
        lastTimeUpdated = "0 minutes ago";

        String url = "https://min-api.cryptocompare.com/data/pricemultifull?fsyms=" + from + "&tsyms=" + to + "&api_key=" + api_key;
        try {
            URL obj = new URL(url);
            HttpURLConnection connection = (HttpURLConnection) obj.openConnection();
            connection.setRequestMethod("GET");

            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String inputLine;
            StringBuffer response = new StringBuffer();

            while ((inputLine = reader.readLine()) != null) {
                response.append(inputLine);
            }
            reader.close();

            JsonParser parser = new JsonParser();
            JsonElement element = parser.parse(response.toString());
            JsonObject object = element.getAsJsonObject();
            object = object.getAsJsonObject("DISPLAY");
            object = object.getAsJsonObject(from);
            object = object.getAsJsonObject(to);

            price = object.getAsJsonPrimitive("PRICE").getAsString();
            volume24Hours = object.getAsJsonPrimitive("TOTALVOLUME24H").getAsString();
            volumeHour = object.getAsJsonPrimitive("VOLUMEHOUR").getAsString();
            volumeDay = object.getAsJsonPrimitive("VOLUMEDAY").getAsString();
            highestPrice24Hours = object.getAsJsonPrimitive("HIGH24HOUR").getAsString();
            highestPriceHour = object.getAsJsonPrimitive("HIGHHOUR").getAsString();
            highestPriceDay = object.getAsJsonPrimitive("HIGHDAY").getAsString();
            lowestPrice24Hours = object.getAsJsonPrimitive("LOW24HOUR").getAsString();
            lowestPriceHour = object.getAsJsonPrimitive("LOWHOUR").getAsString();
            lowestPriceDay = object.getAsJsonPrimitive("LOWDAY").getAsString();
            priceOpen = object.getAsJsonPrimitive("OPENDAY").getAsString();
            mktCap = object.getAsJsonPrimitive("MKTCAP").getAsString();
            change24Hours = object.getAsJsonPrimitive("CHANGE24HOUR").getAsString();
            changePct24Hours = object.getAsJsonPrimitive("CHANGEPCT24HOUR").getAsString() + "%";
            pictureURL = object.getAsJsonPrimitive("IMAGEURL").getAsString();


        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    //Набор геттеров. При помощи них можно получить всю необходимую информацию

    public String getPrice() {
        return price;
    }

    public String getLastTimeUpdated() {

        long timeElapsed = System.currentTimeMillis() - lastTimeUpdatedMillis;
        long days = (timeElapsed / 1000) / 60 / 60 / 24;
        long hours = (timeElapsed / 1000) / 60 / 60;
        long minutes = (timeElapsed / 1000) / 60;
        if (days > 0) {
            lastTimeUpdated = String.valueOf(days) + " days ago";
            return lastTimeUpdated;
        }
        if (hours > 0) {
            lastTimeUpdated = String.valueOf(hours) + " hours ago";
            return lastTimeUpdated;
        }
        if (minutes > 0) {
            lastTimeUpdated = String.valueOf(minutes) + " minutes ago";
            return lastTimeUpdated;
        }

        return lastTimeUpdated;
    }

    public String getFrom() {return from;}

    public String getTo() {return to;}

    public String getVolume24Hours() {
        return volume24Hours;
    }

    public String getVolumeDay() {
        return volumeDay;
    }

    public String getVolumeHour() {
        return volumeHour;
    }

    public String getHighestPrice24Hours() {
        return highestPrice24Hours;
    }

    public String getHighestPriceDay() {
        return highestPriceDay;
    }

    public String getHighestPriceHour() {
        return highestPriceHour;
    }

    public String getLowestPrice24Hours() {
        return lowestPrice24Hours;
    }

    public String getLowestPriceDay() {
        return lowestPriceDay;
    }

    public String getLowestPriceHour() {
        return lowestPriceHour;
    }

    public String getPriceOpen() {
        return priceOpen;
    }

    public String getMktCap() {
        return mktCap;
    }

    public String getChange24Hours() {
        return change24Hours;
    }

    public String getChangePct24Hours() {
        return changePct24Hours;
    }

    public String getPictureURL() {
        return pictureURL;
    }
}
