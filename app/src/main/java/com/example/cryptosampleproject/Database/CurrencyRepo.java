package com.example.cryptosampleproject.Database;

import android.app.Application;
import android.os.AsyncTask;

import com.example.cryptosampleproject.Currency;

import java.util.List;

import androidx.lifecycle.LiveData;

public class CurrencyRepo {
//    private CurrencyDao currencyDao;
//    private LiveData<List<CurrencyEntity>> allCurrencies;
//    public CurrencyRepo(Application application){
//        CurrencyDatabase currencyDatabase = CurrencyDatabase.getInstance(application);
//        currencyDao = currencyDatabase.currencyDao();
//        allCurrencies = currencyDao.getAllCurrencies();
//    }
//    public void insert(CurrencyEntity currency) {
//        new InsertCurrencyAsyncTask(currencyDao).execute(currency);
//    }
//
//    public void update(CurrencyEntity currency) {
//        new UpdateCurrencyAsyncTask(currencyDao).execute(currency);
//    }
//
//    public void delete(CurrencyEntity currency) {
//        new DeleteCurrencyAsyncTask(currencyDao).execute(currency);
//    }
//
//    public void deleteAllCurrencies() {
//        new DeleteAllCurrenciesAsyncTask(currencyDao).execute();
//    }
//
//    public LiveData<List<CurrencyEntity>> getAllCurrencies() {
//        return allCurrencies;
//    }
//
//    private static class InsertCurrencyAsyncTask extends AsyncTask<CurrencyEntity, Void, Void> {
//        private CurrencyDao currencyDao;
//
//        private InsertCurrencyAsyncTask(CurrencyDao currencyDao) {
//            this.currencyDao = currencyDao;
//        }
//
//        @Override
//        protected Void doInBackground(CurrencyEntity... currencyEntities) {
//            currencyDao.insert(currencyEntities[0]);
//            return null;
//        }
//    }
//
//    private static class UpdateCurrencyAsyncTask extends AsyncTask<CurrencyEntity, Void, Void> {
//        private CurrencyDao currencyDao;
//
//        private UpdateCurrencyAsyncTask(CurrencyDao currencyDao) {
//            this.currencyDao = currencyDao;
//        }
//
//        @Override
//        protected Void doInBackground(CurrencyEntity... currencyEntities) {
//            currencyDao.update(currencyEntities[0]);
//            return null;
//        }
//    }
//
//    private static class DeleteCurrencyAsyncTask extends AsyncTask<CurrencyEntity, Void, Void> {
//        private CurrencyDao currencyDao;
//
//        private DeleteCurrencyAsyncTask(CurrencyDao noteDao) {
//            this.currencyDao = noteDao;
//        }
//
//        @Override
//        protected Void doInBackground(CurrencyEntity... currencyEntities) {
//            currencyDao.delete(currencyEntities[0]);
//            return null;
//        }
//    }
//
//    private static class DeleteAllCurrenciesAsyncTask extends AsyncTask<Void, Void, Void> {
//        private CurrencyDao currencyDao;
//
//        private DeleteAllCurrenciesAsyncTask(CurrencyDao currencyDao) {
//            this.currencyDao = currencyDao;
//        }
//
//        @Override
//        protected Void doInBackground(Void... voids) {
//            currencyDao.deleteAllCurrencies();
//            return null;
//        }
//    }
}
