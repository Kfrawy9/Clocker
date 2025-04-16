package com.example.Elkfrawy.data.remote;

import com.google.gson.GsonBuilder;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class SetupRetrofit {

    private static SetupRetrofit instance;
    public WeatherApi api;

    public SetupRetrofit(){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://dataservice.accuweather.com/forecasts/v1/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        api = retrofit.create(WeatherApi.class);
    }

    public static synchronized SetupRetrofit getInstance(){
        if (instance == null)
            instance = new SetupRetrofit();

        return instance;
    }


}
