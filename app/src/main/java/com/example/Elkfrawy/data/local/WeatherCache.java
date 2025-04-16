package com.example.Elkfrawy.data.local;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.Elkfrawy.model.Weather;

@Dao
public interface WeatherCache {

    @Insert
    void insert(Weather w);

    @Query("select * from weather limit 1")
    Weather getCachedWeather();

    @Update
    void update(Weather w);

    @Delete
    void delete(Weather w);
}
