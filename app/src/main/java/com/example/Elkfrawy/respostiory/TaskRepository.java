package com.example.Elkfrawy.respostiory;

import android.app.Application;
import android.net.wifi.WifiManager;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;

import com.example.Elkfrawy.data.local.ClockerDataBase;
import com.example.Elkfrawy.data.local.EventDao;
import com.example.Elkfrawy.data.local.TaskDAO;
import com.example.Elkfrawy.data.local.WeatherCache;
import com.example.Elkfrawy.model.Event;
import com.example.Elkfrawy.model.Task;
import com.example.Elkfrawy.model.Weather;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import kotlin.text.UStringsKt;

public class TaskRepository {
    private static final String TAG = "ReAlarmWorker";


    Application context;
    ClockerDataBase dataBase;
    TaskDAO tDao;
    EventDao eDao;
    WeatherCache weatherDao;
    ExecutorService es;
    
    public TaskRepository(Application context){
        this.context = context;
        dataBase = ClockerDataBase.getInstance(context);
        tDao = dataBase.getTDAo();
        eDao = dataBase.getEDAo();
        weatherDao = dataBase.getWDao();
    }

    public void insert(Task task){
        backgroundInsert(task);
    }
    public void insert(Event event){
        backgroundInsert(event);
    }

    public void update(Task task){
        backgroundUpdate(task);
    }
    public void update(Event event){
        backgroundUpdate(event);
    }

    public void delete(Task task){
        backgroundDelete(task);
    }
    public void delete(Event event){
        backgroundDelete(event);
    }
    public void deleteBy(String t, String d){
        backgroundDeleteBy(t, d);
    }

    public LiveData<List<Task>> allTasks(){
        return tDao.getData();
    }
    public LiveData<List<Event>> allEvents(){
        return eDao.getAllEvents();
    }
    public LiveData<List<Event>> allEventsByy(String date){
        return eDao.getEventsBy(date);
    }

    private void backgroundInsert(Task task){
        es = Executors.newSingleThreadExecutor();
        es.execute(() -> tDao.insert(task));
        es.shutdown();
    }

    private void backgroundInsert(Event event){
        es = Executors.newSingleThreadExecutor();
        es.execute(() -> eDao.insert(event));
        es.shutdown();
    }

    private void backgroundUpdate(Task task){
        es = Executors.newSingleThreadExecutor();
        es.execute(() -> tDao.update(task));
        es.shutdown();
    }

    private void backgroundUpdate(Event event){
        es = Executors.newSingleThreadExecutor();
        es.execute(() -> eDao.update(event));
        es.shutdown();
    }

    private void backgroundDelete(Task task){
        es = Executors.newSingleThreadExecutor();
        es.execute(() -> tDao.delete(task));
        es.shutdown();
    }

    private void backgroundDelete(Event event){
        es = Executors.newSingleThreadExecutor();
        es.execute(() -> eDao.delete(event));
        es.shutdown();
    }

    private void backgroundDeleteBy(String t, String d){
        es = Executors.newSingleThreadExecutor();
        es.execute(() -> eDao.deleteBy(t, d));
        es.shutdown();
    }

    public void updateCachedWeather(Weather weather){
        es = Executors.newSingleThreadExecutor();
        es.execute(() -> weatherDao.update(weather));
        es.shutdown();
    }

    public Weather getWeather(){
        return weatherDao.getCachedWeather();
    }

}
