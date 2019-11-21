package com.example.cryptosampleproject.Database;

import android.app.Application;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

//public class CurrencyViewModel extends AndroidViewModel {
//
//    private CurrencyRepo repository;
//    private LiveData<List<CurrencyEntity>> allCurrencies;
//
//    public CurrencyViewModel(@NonNull Application application) {
//        super(application);
//        repository = new CurrencyRepo(application);
//        allCurrencies = repository.getAllCurrencies();
//    }
//
//    public void insert(CurrencyEntity currencyEntity) {
//        repository.insert(currencyEntity);
//    }
//
//    public void update(CurrencyEntity currencyEntity) {
//        repository.update(currencyEntity);
//    }
//
//    public void delete(CurrencyEntity currencyEntity) {
//        repository.delete(currencyEntity);
//    }
//
//    public void deleteAllCurrencies() {
//        repository.deleteAllCurrencies();
//    }
//
//    public LiveData<List<CurrencyEntity>> getAllCurrencies() {
//        return allCurrencies;
//    }
//}
