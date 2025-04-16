package com.example.Elkfrawy;

import android.annotation.SuppressLint;
import android.content.Context;

import androidx.annotation.NonNull;
import androidx.work.Constraints;
import androidx.work.Data;
import androidx.work.NetworkType;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkContinuation;
import androidx.work.WorkRequest;
import androidx.work.Worker;
import androidx.work.WorkerParameters;
import androidx.work.impl.constraints.WorkConstraintsCallback;

public class Tempo extends Worker {


    public Tempo(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);

    }

    @NonNull
    @Override
    public Result doWork() {

        Data.Builder d = new Data.Builder().putInt("Ah", 234);

        return Result.retry();
    }


    public void testWorker(){

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
            @SuppressLint("IdleBatteryChargingConstraints") Constraints constraints = new Constraints.Builder()
                    .setRequiredNetworkType(NetworkType.METERED)
                    .setRequiresCharging(true)
                    .setRequiresDeviceIdle(true)
                    .setRequiresStorageNotLow(true)
                    .build();

            WorkRequest request = OneTimeWorkRequest.from(Tempo.class);

        }


    }

}
