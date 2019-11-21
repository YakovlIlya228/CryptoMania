package com.example.cryptosampleproject.Database;


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

    @Query("DELETE FROM currencies")
    void deleteAllCurrencies();

    @Query("SELECT * FROM currencies ORDER BY id ASC")
    List<CurrencyEntity> getAllCurrencies();
}
