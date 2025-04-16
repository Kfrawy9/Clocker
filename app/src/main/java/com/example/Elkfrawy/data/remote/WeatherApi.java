package com.example.Elkfrawy.data.remote;

import com.example.Elkfrawy.model.Weather;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface WeatherApi {

    @GET("hourly/1hour/126941?apikey=h5aGiSA5cSbyZDgvfdbsvbdryCmcmAJw")
    Call<List<Weather>> getWeather();


}
