package com.example.Elkfrawy.services;

import static com.example.Elkfrawy.broadcasts.EventBroadCast.DELETE_EVENT_DESC_KEY;
import static com.example.Elkfrawy.broadcasts.EventBroadCast.DELETE_EVENT_TITLE_KEY;
import static com.example.Elkfrawy.broadcasts.EventBroadCast.NOTIFICATION_DISMISS_ID;

import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationManagerCompat;

import com.example.Elkfrawy.respostiory.TaskRepository;

import javax.inject.Inject;

import dagger.hilt.android.AndroidEntryPoint;
import dagger.hilt.android.HiltAndroidApp;

@AndroidEntryPoint
public class DeleteEvent extends Service {
    private static final String TAG = "ALERT";
    @Inject
    TaskRepository repo;

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {



        String title = intent.getStringExtra(DELETE_EVENT_TITLE_KEY);
        String des = intent.getStringExtra(DELETE_EVENT_DESC_KEY);
        int id = intent.getIntExtra(NOTIFICATION_DISMISS_ID, -1);
        repo.deleteBy(title, des);

        NotificationManager manager = (NotificationManager) getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);
        manager.cancel(id);

        return START_NOT_STICKY;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
