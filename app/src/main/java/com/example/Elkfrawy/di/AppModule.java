package com.example.Elkfrawy.di;

import com.example.Elkfrawy.data.remote.WeatherApi;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import dagger.hilt.InstallIn;
import dagger.hilt.components.SingletonComponent;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

@InstallIn(SingletonComponent.class)
@Module()
public class AppModule {


    @Singleton
    @Provides
    public ExecutorService getExecutor(){
        return new ThreadPoolExecutor(3, 4, 5, TimeUnit.SECONDS, new ArrayBlockingQueue<>(50));
    }

    @Singleton
    @Provides
    public Retrofit setupRetrofit(){
        return  new Retrofit.Builder().baseUrl("http://dataservice.accuweather.com/forecasts/v1/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }

    @Singleton
    @Provides
    public WeatherApi getWeatherApi(Retrofit retrofit){
        return  retrofit.create(WeatherApi.class);
    }



}
