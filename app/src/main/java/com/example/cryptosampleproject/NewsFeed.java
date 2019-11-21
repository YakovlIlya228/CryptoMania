package com.example.cryptosampleproject;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.*;

public class NewsFeed {

    private String api_key = "fafc9fa6683ceebd2d0764d35c1c6b0366a08156d69828ce2cbfbe7738d38bbd";
    private List<String> categories; //Запрашиваемые категории
    private List<String> titles = new ArrayList<>(); //Заголовок новости
    private List<String> newsLinks = new ArrayList<>(); //Ссылка на новость
    private List<String> bodys = new ArrayList<>(); //Краткое описание новости
    private List<Long> publishedDateMsecs = new ArrayList<>(); //дата публикации новости в миллисекундах
    private List<String> publishedDate = new ArrayList<>(); //дата публикации новости в виде строки
    private List<String> imgLinks = new ArrayList<>(); //Ссылки на картинки к новостям
    private long lastTimeUpdateMsecs; //последнее время обновления ленты в миллисекундах
    private String lastTimeUpdate; //последнее время обновления ленты в виде строки
    private int size = 0; //Количество новостей

    NewsFeed(List<String> categories) {

        this.categories = categories;
        refresh();

    }

    //Функция для обновления списка новостей
    public void refresh() {

        lastTimeUpdateMsecs = System.currentTimeMillis();
        lastTimeUpdate = "0 minutes ago";

        String url = "https://min-api.cryptocompare.com/data/v2/news/?lang=EN" + "&api_key=" + api_key +"&sortOrder=latest" + "&categories=";
        for (String category: categories)
            url = url + category + ",";
        url = url.substring(0, url.length() - 1);
        System.out.println(url);
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
            JsonArray array = object.getAsJsonArray("Data");
            size = array.size();
            for (int i = 0; i < array.size(); i++) {

                JsonObject temp = array.get(i).getAsJsonObject();
                titles.add(temp.getAsJsonPrimitive("title").getAsString());
                newsLinks.add(temp.getAsJsonPrimitive("url").getAsString());
                bodys.add(temp.getAsJsonPrimitive("body").getAsString());
                publishedDateMsecs.add(temp.getAsJsonPrimitive("published_on").getAsLong() * 1000);
                imgLinks.add(temp.getAsJsonPrimitive("imageurl").getAsString());
                SimpleDateFormat formatter= new SimpleDateFormat("yyyy-MM-dd 'at' HH:mm:ss");
                Date date = new Date(publishedDateMsecs.get(publishedDateMsecs.size() - 1));
                publishedDate.add(formatter.format(date));

            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }


    }

    public String getLastTimeUpdate() {

        long timeElapsed = System.currentTimeMillis() - lastTimeUpdateMsecs;
        long days = timeElapsed / 1000 / 60 / 60 / 24;
        long hours = timeElapsed / 1000 / 60 / 60;
        long minutes = timeElapsed / 1000 / 60;
        if (days > 0) {
            lastTimeUpdate = String.valueOf(days) + " days ago";
            return lastTimeUpdate;
        }
        if (hours > 0) {
            lastTimeUpdate = String.valueOf(hours) + " hours ago";
            return lastTimeUpdate;
        }
        if (minutes > 0) {
            lastTimeUpdate = String.valueOf(minutes) + " minutes ago";
            return lastTimeUpdate;
        }

        return lastTimeUpdate;
    }

    public List<String> getCategories() {
        return categories;
    }

    public List<String> getTitles() {
        return titles;
    }

    public List<String> getNewsLinks() {
        return newsLinks;
    }

    public List<String> getBodys() {
        return bodys;
    }

    public List<String> getPublishedDate() {
        return publishedDate;
    }

    public int getSize() {

        return size;
    }

    public List<String> getImgLinks() {
        return imgLinks;
    }
}
