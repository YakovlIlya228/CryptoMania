package com.example.cryptosampleproject.API;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.converter.gson.GsonConverterFactory;
import okhttp3.OkHttpClient;

public class NetModule {
    private static final String BASE_URL_CryptoCompare = "https://min-api.cryptocompare.com/";
    private static final String BASE_URL_Cryptonator = "https://api.cryptonator.com/";
    private static Retrofit getRetrofitInstance(){
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.HEADERS);
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient client = new OkHttpClient.Builder().addInterceptor(interceptor).build();
        return new Retrofit.Builder()
                .baseUrl(BASE_URL_Cryptonator)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }

    public static Cryptonator getApiService(){
        return getRetrofitInstance().create(Cryptonator.class);
    }

}
