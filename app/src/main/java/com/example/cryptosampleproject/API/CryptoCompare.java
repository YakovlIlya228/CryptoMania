package com.example.cryptosampleproject.API;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface CryptoCompare {
    @GET("data/price")
    Call<CurrencyData>getMyJson(
            @Query("fsym") String from,
            @Query("tsyms") String to,
            @Query("api_key") String apiKey);
}
