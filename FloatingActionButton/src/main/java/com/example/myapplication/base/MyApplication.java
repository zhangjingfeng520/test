package com.example.myapplication.base;

import android.app.Application;
import android.content.Context;
import android.util.Log;

/**
 * Created by Administrator on 2017/11/29.
 */

public class MyApplication extends Application {
    private static Context context;
    private static final String TAG = "MyApplication";
    @Override
    public void onCreate() {
        super.onCreate();
        context=getApplicationContext();
        Log.d(TAG, "onCreate: ");
    }
    public static Context getAppContext(){
        return context;
    }
}
