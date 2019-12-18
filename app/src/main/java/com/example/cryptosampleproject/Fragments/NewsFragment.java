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
import com.example.cryptosampleproject.Adapters.Article;
import com.example.cryptosampleproject.Adapters.NewsAdapter;
import com.example.cryptosampleproject.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NewsFragment extends Fragment {
    List<Datum> newsList;
    List<Article> articleList;
    RecyclerView newsRecycler;
    private final static String apiKey = "fafc9fa6683ceebd2d0764d35c1c6b0366a08156d69828ce2cbfbe7738d38bbd";
    private static final String BASE_URL_CryptoCompare = "https://min-api.cryptocompare.com/";


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.news_fragment,container,false);
        articleList = new ArrayList<>();
        newsRecycler = view.findViewById(R.id.newsView);
        RecyclerView.LayoutManager newsManager = new LinearLayoutManager(getContext());
        newsRecycler.setLayoutManager(newsManager);
        newsCall();
        return view;
    }

    public void newsCall(){
        CryptoCompare apiInterface = NetModule.getCryptoCompareService();
        Call<News> call = apiInterface.getLatestNews(apiKey);
        call.enqueue(new Callback<News>() {
            @Override
            public void onResponse(Call<News> call, Response<News> response) {
                newsList = response.body().getData();
                for(int i=0; i<newsList.size()-1;i++){
                    long timestamp = newsList.get(i).getPublishedOn();
                    Date date = new java.util.Date(timestamp*1000L);
                    SimpleDateFormat sdf = new java.text.SimpleDateFormat("dd-MM-yyyy HH:mm:ss", Locale.US);
                    sdf.setTimeZone(java.util.TimeZone.getTimeZone("GMT-3"));
                    String formattedDate = sdf.format(date);
                    articleList.add(new Article(newsList.get(i).getTitle(),newsList.get(i).getSourceInfo().getName(),formattedDate,newsList.get(i).getImageurl()));
                    NewsAdapter newsAdapter = new NewsAdapter(getContext(),articleList);
                    newsRecycler.setAdapter(newsAdapter);
                }

            }

            @Override
            public void onFailure(Call<News> call, Throwable t) {
                Toast.makeText(getContext(),"Failed to load news",Toast.LENGTH_LONG);
            }
        });
    }


}
