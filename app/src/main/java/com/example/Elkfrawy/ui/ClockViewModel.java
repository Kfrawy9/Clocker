package com.example.Elkfrawy.ui;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.Elkfrawy.core.DateThreads;
import com.example.Elkfrawy.data.remote.SetupRetrofit;
import com.example.Elkfrawy.model.Event;
import com.example.Elkfrawy.model.Task;
import com.example.Elkfrawy.model.Weather;
import com.example.Elkfrawy.respostiory.TaskRepository;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;
import dagger.hilt.android.qualifiers.ApplicationContext;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

@HiltViewModel
public class ClockViewModel extends ViewModel {
    private static final String TAG = "RecyclerTaskAdapter";

    DateThreads dt = new DateThreads();

    // task
    private MutableLiveData<String> liveClock = new MutableLiveData<>();
    private MutableLiveData<String> liveDate = new MutableLiveData<>();
    private LiveData<List<Task>> data;
    private LiveData<List<Event>> events;
    private MutableLiveData<Weather> weather = new MutableLiveData<>();
    SetupRetrofit retrofit = SetupRetrofit.getInstance();
    Task task;


    //event
    public boolean reminderState;
    public Calendar cal = Calendar.getInstance();
    private Event eventEdit;

    //clock
    MutableLiveData<List<String>> laps = new MutableLiveData<>();
    public List<String> lapList = new ArrayList<>();
    public MutableLiveData<String> stopwatch = new MutableLiveData<>();
    public MutableLiveData<String> milliSecond = new MutableLiveData<>();
    public boolean playPause;

    TaskRepository repo;
    //timer
    public int time;
    public String timeText = "";
    public String formattedText = "";
    public boolean isRunning;
    public boolean onOff;
    MutableLiveData<Integer> timerI = new MutableLiveData<>();

    // alarm
    public Calendar calendar;

    @Inject
    public ClockViewModel(TaskRepository repo) {
        this.repo = repo;
        data = repo.allTasks();
        events = repo.allEvents();
    }


    public LiveData<List<String>> observeLaps() {
        return laps;
    }

    public void setLaps(List<String> lap) {
        laps.setValue(lap);
    }

    public void stop() {
        dt.stopTrack();
    }

    public void stopStopWatch() {
        dt.stopStopWatch();
    }

    public void start() {
        dt.track();
        dt.getListener((String t, String d) -> {
            liveClock.postValue(t);
            liveDate.postValue(d);
        });
    }

    public void shutDownStopwatch() {
        dt.shutDownStopWatch();
    }

    public void startStopwatch() {
        dt.stopWatchTracker();
        dt.onStopwatchChangeListener(new DateThreads.onStopwatchChange() {
            @Override
            public void onTimeChange(String time) {
                stopwatch.postValue(time);
            }

            @Override
            public void onMilliSecondChange(String ms) {
                milliSecond.postValue(ms);
            }
        });
    }

    public LiveData<Integer> getTimerI(){
        return timerI;
    }


    public void insert(Task task) {
        repo.insert(task);
    }

    public void insert(Event event) {
        repo.insert(event);
    }

    public void update(Task task) {
        repo.update(task);
    }

    public void update(Event event) {
        repo.update(event);
    }

    public void setTask(Task t) {
        task = t;
    }

    public Task getTask() {
        return task;
    }

    public void delete(Task task) {
        repo.delete(task);
    }

    public void delete(Event event) {
        repo.delete(event);
    }


    public LiveData<List<Task>> getTasks() {
        return data;
    }

    public LiveData<String> getTime() {
        return liveClock;
    }

    public LiveData<String> getDate() {
        return liveDate;
    }

    public LiveData<String> getStopWatch() {
        return stopwatch;
    }

    public LiveData<String> getMilliSecond() {
        return milliSecond;
    }

    public LiveData<List<Event>> getEvents() {
        return events;
    }

    public LiveData<List<Event>> getEventsBy(String s) {
        return repo.allEventsByy(s);
    }

    public void pauseTimer(){
        dt.pauseTimer();
    }

    public void shutdownTimer(){
        dt.resetTimer();
    }

    public void startTimer(int t){
        dt.runTimer(t);
        dt.onTimeChangeListener(i -> timerI.postValue(i));
    }

    public void cachedWeather() {
        ExecutorService es = Executors.newSingleThreadExecutor();
        es.execute( () -> weather.postValue(repo.getWeather()) );
        es.shutdown();
    }

    public LiveData<Weather> getCachedWeather() {
        return weather;
    }


    public void setEvent(Event event) {
        eventEdit = event;
    }

    public Event getEvent() {
        return eventEdit;
    }

    public void setWeather() {
        retrofit.api.getWeather().enqueue(new Callback<List<Weather>>() {
            @Override
            public void onResponse(Call<List<Weather>> call, Response<List<Weather>> response) {
                if (response.isSuccessful()) {

                    Weather w = response.body().get(0);
                    w.setValue(w.getTemperature().getValue());
                    w.setId(1);
                    repo.updateCachedWeather(w);
                    weather.setValue(w);

                } else
                    cachedWeather();
            }

            @Override
            public void onFailure(Call<List<Weather>> call, Throwable t) {
                cachedWeather();
            }
        });
    }


}
