package com.example.Elkfrawy.broadcasts;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import androidx.work.ExistingWorkPolicy;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkManager;
import androidx.work.WorkRequest;

import com.example.Elkfrawy.core.ReAlarmWorker;

public class BootCast extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {

        WorkRequest request = OneTimeWorkRequest.from(ReAlarmWorker.class);
        WorkManager manager = WorkManager.getInstance(context);
        manager.enqueueUniqueWork("reAlarm", ExistingWorkPolicy.KEEP, (OneTimeWorkRequest) request);

    }
}
