package com.example.Elkfrawy.core;

import android.app.Application;
import android.content.Context;
import android.net.ConnectivityManager;
import android.os.SystemClock;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.work.WorkRequest;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import com.example.Elkfrawy.data.local.ClockerDataBase;
import com.example.Elkfrawy.data.local.EventDao;
import com.example.Elkfrawy.model.Event;
import com.example.Elkfrawy.respostiory.TaskRepository;
import com.example.Elkfrawy.ui.eventUi.EventRecycleAdapter;

import java.util.ArrayList;
import java.util.List;

public class ReAlarmWorker extends Worker {
    public ReAlarmWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
    }

    @NonNull
    @Override
    public Result doWork() {

        Context c = getApplicationContext();
        ClockerDataBase dataBase =  ClockerDataBase.getInstance(c);
        EventDao dao =  dataBase.getEDAo();

        List<Event> eventList = dao.getEvents();
        EventRecycleAdapter adapter = new EventRecycleAdapter(getApplicationContext());

        for (Event e : eventList) {
            adapter.createAlarm(e.getTitle(), e.getDescription(), e.getDateTime(), e.hashCode());
        }

        return Result.success();
    }
}
