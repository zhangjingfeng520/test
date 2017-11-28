package com.example.myapplication.service;

import android.app.ActivityManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import java.util.List;

/**
 * Created by Administrator on 2017/11/22.
 */

public class MonitorService extends Service {
    private static boolean isCheck = true;
    private static boolean isRunning = false;
    private static final String SERVICE_NAME = "com.example.myapplication.service.TestService";
    private static final String TAG = "MonitorService";
    private Thread monitorThread=null;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        Log.d(TAG, "onCreate: ");
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(TAG, "onStartCommand: ");
        monitorThread = new Thread() {
            @Override
            public void run() {
                while (isCheck) {
                    try {
                        Thread.sleep(5 * 1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                        Log.d(TAG, "run: 异常");
                    }
                    if (!isServiceWork(getApplicationContext(), SERVICE_NAME)) {
                        Log.d(TAG, "TestService已停止，正在重启");
                        startService(new Intent(MonitorService.this, TestService.class));
                    } else
                        Log.d(TAG, "TestService正在运行");
                }
            }
        };
        monitorThread.start();
        return super.onStartCommand(intent, flags, startId);
    }

    private boolean isServiceWork(Context context, String serviceName) {
        boolean isWork = false;
        ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningServiceInfo> list = activityManager.getRunningServices(80);
        if (list.size() <= 0) {
            return false;
        } else {
            for (int i = 0; i < list.size(); i++) {
                if (list.get(i).service.getClassName().equals(serviceName)) {
                    Log.d(TAG, "isServioceWork: " + list.get(i).service.getClassName());
                    isWork = true;
                    break;
                }
            }
        }
        return isWork;
    }

    @Override
    public void onDestroy() {
        Log.d(TAG, "onDestroy: ");
//        if(monitorThread!=null&monitorThread.isAlive()){
//            monitorThread.interrupt();
//            try {
//                monitorThread.join();
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//                Log.d(TAG, "monitorThread线程中断异常");
//            }
//        }
        isCheck=false;
//        monitorThread.stop();
        super.onDestroy();
    }
}
