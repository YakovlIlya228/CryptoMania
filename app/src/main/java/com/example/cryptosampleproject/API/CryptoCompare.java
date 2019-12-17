package com.example.cryptosampleproject.API;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface CryptoCompare {
    @GET("data/price")
    Call<Ticker>getMyJson(
            @Query("fsym") String from,
            @Query("tsyms") String to,
            @Query("api_key") String apiKey);

    @GET("/data/v2/news/?lang=EN")
    Call<News>getLatestNews(
            @Query("api_key") String apiKey
    );
}


