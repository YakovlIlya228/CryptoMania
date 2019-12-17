package com.example.cryptosampleproject.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.cryptosampleproject.API.Datum;
import com.example.cryptosampleproject.R;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

public class NewsAdapter extends RecyclerView.Adapter<RecyclerAdapter.ViewHolder> {

    private List<Datum> itemList;
    private Context context;


    public class ViewHolder extends RecyclerAdapter.ViewHolder{

        TextView title,source,timestamp;
        ImageView imageView;
        CardView cardView;

        public ViewHolder(@NonNull View itemView) {
            cardView = itemView.findViewById(R.id.newsCard);
            super(itemView);
        }
    }

    public NewsAdapter(Context context, List<Datum> newsList){
        itemList = newsList;
        this.context = context;
    }


    @NonNull
    @Override
    public NewsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        view = LayoutInflater.from(context).inflate(R.layout.news_item_layout,parent,false);
        return new NewsAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerAdapter.ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }
}
