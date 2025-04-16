package com.example.Elkfrawy.data.local;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.example.Elkfrawy.model.Event;
import com.example.Elkfrawy.model.Task;
import com.example.Elkfrawy.model.Weather;

import java.util.Date;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Database(entities = {Task.class, Event.class, Weather.class}, version = 3)
public abstract  class ClockerDataBase extends RoomDatabase{

    public abstract TaskDAO getTDAo();
    public abstract EventDao getEDAo();
    public abstract WeatherCache getWDao();

    private static ClockerDataBase instance;

    // this singleton
    public static synchronized ClockerDataBase getInstance(Context c){

        if (instance == null){
            instance = Room.databaseBuilder(c.getApplicationContext(), ClockerDataBase.class, "Tasks")
                    .fallbackToDestructiveMigration()
                    .addCallback(roomCallback)
                    .build();

        }
        return instance;
    }


    private static RoomDatabase.Callback roomCallback = new Callback() {
        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db) {
            super.onCreate(db);
            insertCachedWeather();
        }


    };

    public static void insertCachedWeather(){
        Weather w = new Weather();
        w.setWeatherIcon(1);
        w.setTime(new Date());
        w.setValue(25);

        ExecutorService es = Executors.newSingleThreadExecutor();
        es.execute(() -> instance.getWDao().insert(w));
        es.shutdown();
    }

}
