package com.example.cryptosampleproject.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.cryptosampleproject.R;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

public class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.ArticleViewHolder> {

    private List<Article> itemList;
    private Context context;


    public class ArticleViewHolder extends RecyclerView.ViewHolder{

        TextView title,source,timestamp;
        ImageView imageView;
        CardView cardView;

        public ArticleViewHolder(@NonNull View itemView) {
            super(itemView);
            cardView = itemView.findViewById(R.id.newsCard);
            cardView.setBackgroundResource(R.drawable.round_card);
            title = itemView.findViewById(R.id.articleTitle);
            source = itemView.findViewById(R.id.articleSource);
            timestamp = itemView.findViewById(R.id.articleTimestamp);
            imageView = itemView.findViewById(R.id.articleImage);
        }
    }

    public NewsAdapter(Context context, List<Article> newsList){
        itemList = newsList;
        this.context = context;
    }


    @NonNull
    @Override
    public NewsAdapter.ArticleViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        view = LayoutInflater.from(context).inflate(R.layout.news_item_layout,parent,false);
        return new NewsAdapter.ArticleViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NewsAdapter.ArticleViewHolder holder, int position) {
            holder.title.setText(itemList.get(position).getTitle());
            holder.source.setText(itemList.get(position).getSource());
            holder.timestamp.setText(itemList.get(position).getTimestamp());
            Glide.with(context)
                .load(itemList.get(position).getImageLink())
                .apply(new RequestOptions().override(70,70))
                .into(holder.imageView);
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }
}
