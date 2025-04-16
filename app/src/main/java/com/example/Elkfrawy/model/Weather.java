package com.example.Elkfrawy.model;

import androidx.room.Dao;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;
import androidx.room.util.TableInfo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.Date;

@Entity(tableName = "weather")
public class Weather {

    @PrimaryKey(autoGenerate = true)
    @Expose(serialize = false, deserialize = false)
    int id;

    @SerializedName("DateTime")
    @Ignore
    Date time;

    @SerializedName("WeatherIcon")
    int weatherIcon;

    @Expose(serialize = false, deserialize = false)
    int value;

    @Ignore
    @SerializedName("Temperature")
    Temperature temperature;


    public int getWeatherIcon() {
        return weatherIcon;
    }

    public Temperature getTemperature() {
        return temperature;
    }

    public void setTime(Date time) {
        this.time = time;
    }

    public void setWeatherIcon(int weatherIcon) {
        this.weatherIcon = weatherIcon;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public void setTemperature(Temperature temperature) {
        this.temperature = temperature;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public Date getTime() {
        return time;
    }

    public int getValue() {
        return value;
    }
}
