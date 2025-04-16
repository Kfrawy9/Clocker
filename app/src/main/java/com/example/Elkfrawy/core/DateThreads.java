package com.example.Elkfrawy.core;

import android.content.Context;
import android.media.MediaPlayer;
import android.os.SystemClock;

import com.example.Elkfrawy.R;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class DateThreads {
    private static final String TAG = "DateThreads";

    ExecutorService es;
    onDateListener mListener;
    onStopwatchChange mStopwatchListener;
    boolean stop;
    boolean stopWatchState;
    int second = 0;
    boolean stopTimer;
    int mSecond = 0;
    onTimerChange mTListener;
    int i = 0;
    ExecutorService executor;




    public void stopWatchTracker() {
        stopWatchState = false;
        es = Executors.newFixedThreadPool(2);

        es.execute(() -> {
            while (!stopWatchState) {

                if (mSecond % 100 == 0) mSecond = 0;
                String ms = String.format(Locale.getDefault(), "%02d", mSecond);
                if (mStopwatchListener != null) mStopwatchListener.onMilliSecondChange(ms);

                SystemClock.sleep(50);
                mSecond += 10;
            }
        });

        es.execute(() -> {
            while (!stopWatchState) {
                int hours = second / 3600;
                int minutes = (second % 3600) / 60;
                int secs = second % 60;
                String stopwatch = String.format(Locale.getDefault(), "%02d:%02d:%02d", hours, minutes, secs);
                if (mStopwatchListener != null) mStopwatchListener.onTimeChange(stopwatch);
                SystemClock.sleep(1000);
                second++;
            }
        });
    }

    public void track() {
        stop = false;
        es = Executors.newSingleThreadExecutor();
        es.execute(() -> {
            while (!stop) {

                SimpleDateFormat format = new SimpleDateFormat("hh:mm:ss a");
                SimpleDateFormat format2 = new SimpleDateFormat("EEE', 'MMM dd");
                Date d = new Date();
                String time = format.format(d);
                String date = format2.format(d);
                if (mListener != null) mListener.onDateChange(time, date);
                SystemClock.sleep(1000);

            }
        });
        es.shutdown();
    }

    public void runTimer(int t) {
        stopTimer = false;
            es = Executors.newSingleThreadExecutor();
            es.execute(() -> {

                while (!stopTimer) {

                    if (i > t ){
                        i = 0;

                        break;
                    }
                    mTListener.onChange(i);
                    SystemClock.sleep(1000);
                    i++;
                }
            });

            es.shutdown();
    }

    public void pauseTimer() {
        stopTimer = true;
    }

    public void resetTimer() {
        stopTimer = true;
        i = 0;
    }


    public void stopTrack() {
        stop = true;
    }

    public void stopStopWatch() {
        stopWatchState = true;
    }

    public void shutDownStopWatch() {
        mSecond = 0;
        second = 0;
        stopWatchState = true;

       if (es != null){
           es.shutdown();
           es = null;
       }
    }

    public void getListener(onDateListener listener) {
        mListener = listener;
    }

    public void onStopwatchChangeListener(onStopwatchChange stopwatchListener) {
        mStopwatchListener = stopwatchListener;
    }

    public interface onDateListener {
        void onDateChange(String time, String date);
    }

    public interface onStopwatchChange {
        void onTimeChange(String time);

        void onMilliSecondChange(String ms);
    }

    public void onTimeChangeListener(onTimerChange listener) {
        mTListener = listener;
    }

    public interface onTimerChange {
        void onChange(int t);
    }

}
