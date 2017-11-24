package com.example.myapplication.service;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by Administrator on 2017/11/20.
 */

public class TestService extends Service {

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        Log.d("TestService", "onBind: ");
        return testServiceBinder;
    }

    private TestServiceBinder testServiceBinder = new TestServiceBinder();

    public class TestServiceBinder extends Binder {

        public TestService getService() {
            return TestService.this;
        }
    }
    public int getCurrentNum(){
        return i;
    }

    @Override
    public void onCreate() {
        Log.d("TestService", "onCreate: ");
        startTimer();
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d("TestService", "onStartCommand: ");
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        Log.d("TestService", "onDestroy: ");
        stopTimer();
        super.onDestroy();
    }

    private Timer timer = null;
    private TimerTask timerTask = null;
    private int i = 0;

    public void startTimer() {
        if (timer == null) {
            timer = new Timer();
            timerTask = new TimerTask() {
                @Override
                public void run() {
                    i++;
                    Log.d("TestService", "run: " + i);
                }
            };
            timer.schedule(timerTask, 1000, 1000);
        }

    }

    public void stopTimer() {
        if (timer != null) {
            timerTask.cancel();
            timer.cancel();

            timerTask = null;
            timer = null;
        }
    }

}
