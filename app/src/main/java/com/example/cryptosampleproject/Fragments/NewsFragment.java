package com.example.cryptosampleproject.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.cryptosampleproject.API.CryptoCompare;
import com.example.cryptosampleproject.API.Datum;
import com.example.cryptosampleproject.API.NetModule;
import com.example.cryptosampleproject.API.News;
import com.example.cryptosampleproject.R;

import java.util.List;

import androidx.fragment.app.Fragment;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NewsFragment extends Fragment {
    List<Datum> newsList;

    private final static String apiKey = "fafc9fa6683ceebd2d0764d35c1c6b0366a08156d69828ce2cbfbe7738d38bbd";
    private static final String BASE_URL_CryptoCompare = "https://min-api.cryptocompare.com/";
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.news_fragment,container,false);

        return view;
    }

    public void newsCall(){
        CryptoCompare apiInterface = NetModule.getCryptoCompareService();
        Call<News> call = apiInterface.getLatestNews(apiKey);
        call.enqueue(new Callback<News>() {
            @Override
            public void onResponse(Call<News> call, Response<News> response) {
                newsList = response.body().getData();

            }

            @Override
            public void onFailure(Call<News> call, Throwable t) {
                Toast.makeText(getContext(),"Failed to load news",Toast.LENGTH_LONG);
            }
        });
    }


}
