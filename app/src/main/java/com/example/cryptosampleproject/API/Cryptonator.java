package com.example.cryptosampleproject.API;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface Cryptonator {
    @GET("api/ticker/{from}-{to}")
    Call<CurrencyData> getMyJson(
        @Path("from") String from,
        @Path("to") String to
    );

    @GET("api/currencies")
    Call<Rows> getName();
}
