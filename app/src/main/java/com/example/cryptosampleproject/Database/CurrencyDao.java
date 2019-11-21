package com.example.cryptosampleproject.Database;


import android.util.Log;

import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

@Dao
public interface CurrencyDao {
    @Insert
    void insert(CurrencyEntity currencyEntity);

    @Update
    void update(CurrencyEntity currencyEntity);

    @Delete
    void  delete(CurrencyEntity currencyEntity);

    @Query("DELETE FROM currencies WHERE code = :Code")
    void deleteByCode(String Code);

    @Query("DELETE FROM currencies")
    void deleteAllCurrencies();

    @Query("SELECT * FROM currencies ORDER BY id ASC")
    List<CurrencyEntity> getAllCurrencies();

    @Query("SELECT id FROM currencies WHERE code = :myCode")
    Long getId(String myCode);
}
