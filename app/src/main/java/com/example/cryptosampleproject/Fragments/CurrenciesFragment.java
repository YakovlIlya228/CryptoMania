package com.example.cryptosampleproject.Fragments;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.cryptosampleproject.API.CryptoCompare;
import com.example.cryptosampleproject.API.Cryptonator;
import com.example.cryptosampleproject.API.CurrencyData;
import com.example.cryptosampleproject.API.NetModule;
import com.example.cryptosampleproject.API.Row;
import com.example.cryptosampleproject.API.Rows;
import com.example.cryptosampleproject.API.Ticker;
import com.example.cryptosampleproject.Adapters.RecyclerAdapter;
import com.example.cryptosampleproject.Currency;
import com.example.cryptosampleproject.Database.CurrencyDatabase;
import com.example.cryptosampleproject.Database.CurrencyEntity;
//import com.example.cryptosampleproject.Database.CurrencyViewModel;
import com.example.cryptosampleproject.Dialog;
import com.example.cryptosampleproject.ExchangeInfo;
import com.example.cryptosampleproject.R;
import com.example.cryptosampleproject.TabbedActivity;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;
import io.reactivex.Flowable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.Observable;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.example.cryptosampleproject.Database.CurrencyDatabase.getInstance;

public class CurrenciesFragment extends Fragment implements Dialog.DialogListener {

    private RecyclerView recyclerView;
    private List<Currency> itemList;
    private List<CurrencyEntity> dataList;
    private CurrencyData currencyData;
    private CurrencyDatabase database;
    final private String realCurrency = "usd";
    public static final int REQUEST_CODE = 10;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    public CurrenciesFragment(){

    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.refresh:
                recyclerView = getActivity().findViewById(R.id.recyclerView);
                RecyclerAdapter recyclerAdapter = new RecyclerAdapter(getContext(),itemList);
                int count = recyclerAdapter.getItemCount();
                for(int i=0; i<count;i++){
                    String code = ((TextView) recyclerView.findViewHolderForAdapterPosition(i).itemView.findViewById(R.id.codename)).getText().toString().toLowerCase();
                    String name = ((TextView) recyclerView.findViewHolderForAdapterPosition(i).itemView.findViewById(R.id.name)).getText().toString().toLowerCase();
                    CurrencyRefresh(code,realCurrency,name,recyclerAdapter,i);

                }
                RecyclerView.LayoutManager manager = new LinearLayoutManager(getContext());
                recyclerView.setLayoutManager(manager);
                recyclerView.setAdapter(recyclerAdapter);
                break;
            case R.id.add:
                openDialog();
                break;
        }

        return super.onOptionsItemSelected(item);
    }
    public void openDialog(){
        FragmentManager fm = getActivity().getSupportFragmentManager();
        Dialog dialogFragment = new Dialog();
        dialogFragment.setTargetFragment(this, REQUEST_CODE);
        dialogFragment.show(fm,"getCode");
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.currencies_fragment,container,false);
        itemList = new ArrayList<>();
        Flowable.fromCallable(() -> {
            dataList = database.getInstance(getContext()).currencyDao().getAllCurrencies();
            return "Data was imported!";
        })
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.single())
                .subscribe(System.out::println,Throwable::printStackTrace);
        try {
            Thread.sleep(1500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        RecyclerAdapter recyclerAdapter = new RecyclerAdapter(getContext(),itemList);
        for(int i=0;i<dataList.size();i++){
//            itemList.add(new Currency(dataList.get(i).getName(),dataList.get(i).getCode()));
            CurrencyCall(dataList.get(i).getCode(),realCurrency,dataList.get(i).getName(),recyclerAdapter);
        }
//        CurrencyCall("btc",realCurrency, "Bitcoin",recyclerAdapter);
//        CurrencyCall("eth",realCurrency, "Ethereum",recyclerAdapter);
//        CurrencyCall("ltc",realCurrency,"Litecoin",recyclerAdapter);
        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
        RecyclerView.LayoutManager manager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(manager);
//        recyclerView.setAdapter(recyclerAdapter);
        return view;
    }

    public void applyBaseCurrency(String base){
        RecyclerAdapter recyclerAdapter = new RecyclerAdapter(getContext(),itemList);
        getNameAndMakeCall(base,recyclerAdapter);
    }


    public void getNameAndMakeCall(final String code, final RecyclerAdapter recyclerAdapter){
        Cryptonator apiInterface = NetModule.getApiService();
        Call<Rows> call = apiInterface.getName();
        call.enqueue(new Callback <Rows>() {
            @Override
            public void onResponse(Call<Rows> call, Response<Rows> response) {
                Rows nameList = response.body();
                for(int i=0; i<nameList.getRows().size();i++){
                    if(nameList.getRows().get(i).getCode().equals(code.toUpperCase())){
                        int pos = i;
                        Toast.makeText(getActivity(),"Got the currency's full name", Toast.LENGTH_LONG).show();
                        CurrencyCall(code,realCurrency,nameList.getRows().get(i).getName(),recyclerAdapter);
                        Flowable.fromCallable(() -> {
                            getInstance(getContext()).currencyDao().insert(new CurrencyEntity(nameList.getRows().get(pos).getName(),code));
                            return "Data was imported!";
                        })
                                .subscribeOn(Schedulers.io())
                                .observeOn(Schedulers.single())
                                .subscribe(System.out::println,Throwable::printStackTrace);

                    }
                }

            }

            @Override
            public void onFailure(Call<Rows> call, Throwable t) {
                Toast.makeText(getActivity(),"Couldn't get the currency full name", Toast.LENGTH_SHORT).show();
            }
        });
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==Dialog.REQUEST_CODE){
            if(resultCode==Activity.RESULT_OK){
                String baseCurrency = data.getStringExtra("currency_code");
                applyBaseCurrency(baseCurrency);
            }
            if(resultCode==Activity.RESULT_CANCELED){
                Toast.makeText(getContext(),"Adding was canceled",Toast.LENGTH_SHORT).show();
            }
        }
    }

    public void CurrencyCall(String from, String to , final String name, final RecyclerAdapter adapter){
        Cryptonator apiInterface = NetModule.getApiService();
        Call<CurrencyData> call = apiInterface.getMyJson(from,to);
        call.enqueue(new Callback<CurrencyData>() {
            @Override
            public void onResponse(Call<CurrencyData> call, Response<CurrencyData> response) {
                currencyData = response.body();
                itemList.add(new Currency(name, currencyData.getTicker().getBase(),currencyData.getTicker().getPrice().substring(0,currencyData.getTicker().getPrice()
                        .length()- 4),currencyData.getTicker().getChange().substring(0,currencyData.getTicker().getChange().length()-4)));
                RecyclerAdapter recyclerAdapter = new RecyclerAdapter(getContext(),itemList);
                recyclerView.setAdapter(recyclerAdapter);
                Toast.makeText(getActivity(),"Successfully loaded!", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<CurrencyData> call, Throwable t) {
                Toast.makeText(getActivity(),"Failure", Toast.LENGTH_LONG).show();
            }
        });
    }
    public void CurrencyRefresh(String from, String to , final String name, final RecyclerAdapter adapter, final int pos){
        Cryptonator apiInterface = NetModule.getApiService();
        Call<CurrencyData> call = apiInterface.getMyJson(from,to);
        call.enqueue(new Callback<CurrencyData>() {
            @Override
            public void onResponse(Call<CurrencyData> call, Response<CurrencyData> response) {
                currencyData = response.body();
                itemList.get(pos).setCodename(currencyData.getTicker().getBase());
                itemList.get(pos).setPrice(currencyData.getTicker().getPrice().substring(0,currencyData.getTicker().getPrice()
                        .length()- 4));
                itemList.get(pos).setChange(currencyData.getTicker().getChange().substring(0,currencyData.getTicker().getChange().length()-4));
                adapter.notifyDataSetChanged();
                Toast.makeText(getActivity(),"Data refreshed!", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<CurrencyData> call, Throwable t) {
                Toast.makeText(getActivity(),"Failure", Toast.LENGTH_LONG).show();
            }
        });
    }

}