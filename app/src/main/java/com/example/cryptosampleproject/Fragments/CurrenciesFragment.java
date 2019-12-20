package com.example.cryptosampleproject.Fragments;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.example.cryptosampleproject.API.Cryptonator;
import com.example.cryptosampleproject.API.CurrencyData;
import com.example.cryptosampleproject.API.NetModule;
import com.example.cryptosampleproject.API.Rows;
import com.example.cryptosampleproject.Activities.TabbedActivity;
import com.example.cryptosampleproject.Adapters.Currency;
import com.example.cryptosampleproject.Adapters.RecyclerAdapter;
import com.example.cryptosampleproject.Adapters.RecyclerViewListener;
import com.example.cryptosampleproject.Database.CurrencyDatabase;
import com.example.cryptosampleproject.Database.CurrencyEntity;
import com.example.cryptosampleproject.Dialog;
import com.example.cryptosampleproject.Activities.Linechart;
import com.example.cryptosampleproject.NetworkUtility;
import com.example.cryptosampleproject.R;
import java.util.ArrayList;
import java.util.List;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import io.reactivex.Flowable;
import io.reactivex.schedulers.Schedulers;
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
    private String realCurrency = "usd";
    private TextView noConnect;
    private ImageView noConnectImage;
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


    public void setVisible(boolean visible, View view){
            noConnect = view.findViewById(R.id.noConnection);
            noConnectImage = view.findViewById(R.id.noConnectionImage);
            if(visible){
                noConnect.setVisibility(View.INVISIBLE);
                noConnectImage.setVisibility(View.INVISIBLE);
                recyclerView.setVisibility(View.VISIBLE);
            }
            else{
                noConnect.setVisibility(View.VISIBLE);
                noConnectImage.setVisibility(View.VISIBLE);
                recyclerView.setVisibility(View.INVISIBLE);
            }

    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.refresh:
                if(NetworkUtility.isNetworkConnected(getActivity().getApplicationContext())) {
                    noConnect = getActivity().findViewById(R.id.noConnection);
                    noConnectImage = getActivity().findViewById(R.id.noConnectionImage);
                    noConnect.setVisibility(View.INVISIBLE);
                    noConnectImage.setVisibility(View.INVISIBLE);
                    recyclerView.setVisibility(View.VISIBLE);
                    RecyclerAdapter recyclerAdapter = new RecyclerAdapter(getContext(), itemList);
                    int count = recyclerAdapter.getItemCount();
                    for (int i = 0; i < count; i++) {
                        String code = ((TextView) recyclerView.findViewHolderForAdapterPosition(i).itemView.findViewById(R.id.codename)).getText().toString().toLowerCase();
                        String name = ((TextView) recyclerView.findViewHolderForAdapterPosition(i).itemView.findViewById(R.id.name)).getText().toString();
                        CurrencyRefresh(code, realCurrency, name, recyclerAdapter, i);
                    }
                    recyclerView.setAdapter(recyclerAdapter);
                }
                else {
                    AlertDialog.Builder noConnection = new AlertDialog.Builder(getActivity());
                    noConnection.setMessage(R.string.no_connection)
                            .setNeutralButton("OK", (dialog, which) -> dialog.dismiss());
                    AlertDialog dialog = noConnection.create();
                    dialog.show();
                }
                break;
            case R.id.add:
                if(NetworkUtility.isNetworkConnected(getActivity().getApplicationContext())){
                    openDialog();
                }
                else{
                    AlertDialog.Builder noConnection = new AlertDialog.Builder(getActivity());
                    noConnection.setMessage(R.string.no_connection)
                            .setNeutralButton("OK", (dialog, which) -> dialog.dismiss());
                    AlertDialog dialog = noConnection.create();
                    dialog.show();
                }
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
        Button selectBtn = view.findViewById(R.id.selectBtn);
        noConnect = view.findViewById(R.id.noConnection);
        noConnectImage = view.findViewById(R.id.noConnectionImage);
        Flowable.fromCallable(() -> {
            dataList = database.getInstance(getContext()).currencyDao().getAllCurrencies();
            return "Data was imported!";
        })
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.single())
                .subscribe(System.out::println,Throwable::printStackTrace);
        try {
            Thread.sleep(1500);
        }
        catch (InterruptedException e){
            e.printStackTrace();
        }
        RecyclerAdapter recyclerAdapter = new RecyclerAdapter(getContext(),itemList);
        recyclerView = view.findViewById(R.id.recyclerView);
        if(NetworkUtility.isNetworkConnected(getActivity().getApplicationContext())){
            for(int i=0;i<dataList.size();i++){
                CurrencyCall(dataList.get(i).getCode(),realCurrency,dataList.get(i).getName(),recyclerAdapter,dataList.get(i).getId());
            }
        }
        else{
//            AlertDialog.Builder noConnection = new AlertDialog.Builder(getActivity());
//            noConnection.setMessage(R.string.no_connection)
//                    .setNeutralButton("OK", (dialog, which) -> dialog.dismiss());
//            AlertDialog dialog = noConnection.create();
//            dialog.show();
            for(int i=0;i<dataList.size();i++){
                itemList.add(new Currency(dataList.get(i).getName(), dataList.get(i).getCode(),"—","—"));
                itemList.get(itemList.size()-1).setId(dataList.get(i).getId());
            }
            setVisible(false,view);
        }
        RecyclerView.LayoutManager manager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(manager);
        recyclerView.setAdapter(recyclerAdapter);
        SwipeRefreshLayout swipeRefreshLayout = view.findViewById(R.id.pullToRefreshCurrencies);
        swipeRefreshLayout.setOnRefreshListener(() -> {
            int count = recyclerAdapter.getItemCount();
            if(NetworkUtility.isNetworkConnected(getActivity().getApplicationContext())){
                for(int i=0; i<count;i++){
                    String code = ((TextView) recyclerView.findViewHolderForAdapterPosition(i).itemView.findViewById(R.id.codename)).getText().toString().toLowerCase();
                    String name = ((TextView) recyclerView.findViewHolderForAdapterPosition(i).itemView.findViewById(R.id.name)).getText().toString();
                    CurrencyRefresh(code,realCurrency,name,recyclerAdapter,i);
                }
                setVisible(true,view);
                recyclerView.setAdapter(recyclerAdapter);

            }
            else{
                Toast.makeText(getContext(),"There is no Internet connection",Toast.LENGTH_LONG).show();
            }
            swipeRefreshLayout.setRefreshing(false);
        });

        recyclerView.addOnItemTouchListener(new RecyclerViewListener(getContext(), recyclerView, (view1, position) -> {
            String codeFrom = ((TextView)recyclerView.findViewHolderForAdapterPosition(position).itemView.findViewById(R.id.codename)).getText().toString();
            String price = ((TextView) recyclerView.findViewHolderForAdapterPosition(position).itemView.findViewById(R.id.price)).getText().toString();
            char chng = ((TextView) recyclerView.findViewHolderForAdapterPosition(position).itemView.findViewById(R.id.change)).getText().charAt(0);
            Bundle bundle = new Bundle();
            bundle.putString("codeFrom",codeFrom);
            bundle.putString("codeTo",realCurrency);
            bundle.putString("price",price);
            bundle.putChar("change",chng);
            Intent intent = new Intent(view1.getContext(),Linechart.class);
            intent.putExtras(bundle);
            startActivity(intent);
        }));

        selectBtn.setOnClickListener(v -> {
            AlertDialog.Builder mBuilder = new AlertDialog.Builder(getContext());
            mBuilder.setTitle("Choose a currency");
            String[] currenciesList = getResources().getStringArray(R.array.currencies);
            mBuilder.setSingleChoiceItems(currenciesList, -1, (dialog, which) -> {
                selectBtn.setText(currenciesList[which]);
                switch (currenciesList[which]){
                    case "US Dollar":
                        realCurrency = "usd";
                        break;
                    case "Euro":
                        realCurrency = "eur";
                        break;
                    case "Pound sterling":
                        realCurrency = "gbp";
                        break;
                    case "Russian ruble":
                        realCurrency = "rub";
                        break;
                    case "Japanese yen":
                        realCurrency = "jpy";
                        break;
                    case "Ukrainian hryvnia":
                        realCurrency = "uah";
                        break;
                    case "Australian dollar":
                        realCurrency = "aud";
                        break;
                    case "Canadian dollar":
                        realCurrency = "cad";
                        break;
                }
                RecyclerAdapter recyclerAdapterNew = new RecyclerAdapter(getContext(),itemList);
                int count = recyclerAdapterNew.getItemCount();
                for(int i=0; i<count;i++){
                    String code = ((TextView) recyclerView.findViewHolderForAdapterPosition(i).itemView.findViewById(R.id.codename)).getText().toString().toLowerCase();
                    String name = ((TextView) recyclerView.findViewHolderForAdapterPosition(i).itemView.findViewById(R.id.name)).getText().toString();
                    CurrencyRefresh(code,realCurrency,name,recyclerAdapterNew,i);
                }
                recyclerView.setAdapter(recyclerAdapterNew);
                dialog.dismiss();
            });
            AlertDialog mDialog = mBuilder.create();
            mDialog.show();

        });


        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                Flowable.fromCallable(() -> {
                    final int pos = viewHolder.getAdapterPosition();
                    int id = itemList.get(pos).getId();
                    itemList.remove(viewHolder.getAdapterPosition());
                    getInstance(getContext()).currencyDao().deleteById(id);
                    return "Item was deleted!";
                })
                        .subscribeOn(Schedulers.io())
                        .observeOn(Schedulers.single())
                        .subscribe(System.out::println,Throwable::printStackTrace);
                RecyclerAdapter newRecyclerAdapter = new RecyclerAdapter(getContext(),itemList);
                recyclerView.setAdapter(newRecyclerAdapter);
            }
        }).attachToRecyclerView(recyclerView);
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
                        final int pos = i;
                        Flowable.fromCallable(() -> {
                            getInstance(getContext()).currencyDao().insert(new CurrencyEntity(nameList.getRows().get(pos).getName(),code.toUpperCase()));
                            dataList = getInstance(getContext()).currencyDao().getAllCurrencies();
                            CurrencyCall(code,realCurrency,nameList.getRows().get(pos).getName(),recyclerAdapter,dataList.get(dataList.size()-1).getId());
                            return "Item was added!";
                        })
                                .subscribeOn(Schedulers.io())
                                .observeOn(Schedulers.single())
                                .subscribe(System.out::println,Throwable::printStackTrace);

                    }
                }

            }

            @Override
            public void onFailure(Call<Rows> call, Throwable t) {}
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
        }
    }

    public void CurrencyCall(String from, String to, final String name, final RecyclerAdapter adapter,int id){
        if(from.toUpperCase().equals(to.toUpperCase())){
            itemList.add(new Currency(name,from.toUpperCase(),"1.0000","0.0000"));
            itemList.get(itemList.size()-1).setId(id);
//            RecyclerAdapter recyclerAdapter = new RecyclerAdapter(getContext(),itemList);
            recyclerView.setAdapter(adapter);
            return;
        }
        Cryptonator apiInterface = NetModule.getApiService();
        Call<CurrencyData> call = apiInterface.getMyJson(from,to);
        call.enqueue(new Callback<CurrencyData>() {
            @Override
            public void onResponse(Call<CurrencyData> call, Response<CurrencyData> response) {
                currencyData = response.body();
                itemList.add(new Currency(name, from.toUpperCase(),currencyData.getTicker().getPrice().substring(0,currencyData.getTicker().getPrice()
                        .length()- 4),currencyData.getTicker().getChange().substring(0,currencyData.getTicker().getChange().length()-4)));
                itemList.get(itemList.size()-1).setId(id);
//                RecyclerAdapter recyclerAdapter = new RecyclerAdapter(getContext(),itemList);
                recyclerView.setAdapter(adapter);
            }

            @Override
            public void onFailure(Call<CurrencyData> call, Throwable t) {
                Toast.makeText(getActivity(),"Couldn't add the currency", Toast.LENGTH_LONG).show();
            }
        });
    }
    public void CurrencyRefresh(String from, String to , final String name, final RecyclerAdapter adapter, final int pos){
        if(from.toUpperCase().equals(to.toUpperCase())){
            itemList.get(pos).setPrice("1.0000");
            itemList.get(pos).setChange("0.0000");
            RecyclerAdapter recyclerAdapter = new RecyclerAdapter(getContext(),itemList);
            recyclerView.setAdapter(recyclerAdapter);
            return;
        }
        Cryptonator apiInterface = NetModule.getApiService();
        Call<CurrencyData> call = apiInterface.getMyJson(from,to);
        call.enqueue(new Callback<CurrencyData>() {
            @Override
            public void onResponse(Call<CurrencyData> call, Response<CurrencyData> response) {
                currencyData = response.body();
                itemList.get(pos).setPrice(currencyData.getTicker().getPrice().substring(0,currencyData.getTicker().getPrice()
                        .length()- 4));
                itemList.get(pos).setChange(currencyData.getTicker().getChange().substring(0,currencyData.getTicker().getChange().length()-4));
//                RecyclerAdapter recyclerAdapter = new RecyclerAdapter(getContext(),itemList);
                recyclerView.setAdapter(adapter);
            }

            @Override
            public void onFailure(Call<CurrencyData> call, Throwable t) {
//                Toast.makeText(getActivity(),"Couldn't refresh currency", Toast.LENGTH_LONG).show();
            }
        });
    }

}