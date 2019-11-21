package com.example.cryptosampleproject.Database;

import android.content.Context;
import android.os.AsyncTask;

import java.util.Currency;
import java.util.Observable;
import java.util.concurrent.Callable;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

@Database(entities = {CurrencyEntity.class}, version = 1)
public abstract class CurrencyDatabase extends RoomDatabase {

    private static CurrencyDatabase instance;

    public abstract CurrencyDao currencyDao();

    public static synchronized CurrencyDatabase getInstance(Context context) {
        if (instance == null) {
            instance = Room.databaseBuilder(context.getApplicationContext(),
                    CurrencyDatabase.class, "currencies")
                    .fallbackToDestructiveMigration()
                    .build();
        }
        return instance;
    }
}
//    private static RoomDatabase.Callback roomCallback = new RoomDatabase.Callback() {
//        @Override
//        public void onCreate(@NonNull SupportSQLiteDatabase db) {
//            super.onCreate(db);
//            new PopulateDbAsyncTask(instance).execute();
//        }
//    };
//    private static class PopulateDbAsyncTask extends AsyncTask<Void, Void, Void> {
//        private CurrencyDao currencyDao;
//
//        private PopulateDbAsyncTask(CurrencyDatabase db) {
//            currencyDao = db.currencyDao();
//        }
//
//        @Override
//        protected Void doInBackground(Void... voids) {
//
//            return null;
//        }
//    }
//}
