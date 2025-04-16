package com.example.Elkfrawy.core;

import android.app.Application;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.os.Build;
import android.util.Log;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import dagger.hilt.android.HiltAndroidApp;

@HiltAndroidApp
public class NotificationChannelBuild extends Application {

    public static final String CHANNEL_ID = "clockerChannelID";
    public static final String CHANNEL_NAME = "Clocker channel";
    private static final String TAG = "TimerAA";

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(TAG, "onCreate: creating executor");

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, CHANNEL_NAME, NotificationManager.IMPORTANCE_DEFAULT);
            channel.setDescription("This channel for displaying all notification of event");

            NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            manager.createNotificationChannel(channel);
        }
    }
}
