package com.example.cryptosampleproject.Adapters;

import android.content.Context;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.cryptosampleproject.Currency;
import com.example.cryptosampleproject.R;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.lifecycle.LiveData;
import androidx.recyclerview.widget.RecyclerView;

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.ViewHolder> {

    private List<com.example.cryptosampleproject.Currency> data;
    private Context context;

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView name, codename, price, change;
        CardView cardView;

        public TextView getCodename() {
            return codename;
        }

        public ViewHolder(@NonNull View itemView){
            super(itemView);
            cardView = itemView.findViewById(R.id.card);
            cardView.setBackgroundResource(R.drawable.round_card);
            name = itemView.findViewById(R.id.name);
            codename =  itemView.findViewById(R.id.codename);
            price =  itemView.findViewById(R.id.price);
            change =  itemView.findViewById(R.id.change);

        }
    }
    public RecyclerAdapter(Context context, List<Currency> currencyList){
        data = currencyList;
        this.context = context;
    }

    public String getCode(@NonNull ViewHolder holder, long pos){
        return holder.codename.getText().toString();
    }

    public RecyclerAdapter(){}

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        view = LayoutInflater.from(context).inflate(R.layout.item_layout,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.name.setText(data.get(position).getName());
        holder.codename.setText(data.get(position).getCodename());
        holder.price.setText(data.get(position).getPrice());
        holder.change.setText(data.get(position).getChange());
        if(holder.change.getText().charAt(0)=='-'){
            holder.change.setBackgroundResource(R.drawable.rounded_corner_red);
        }
        else{
            holder.change.setText( '+' + data.get(position).getChange());
            holder.change.setBackgroundResource(R.drawable.rounded_corner);
        }
    }

    @Override
    public int getItemCount() {
        return data.size();
    }
}
