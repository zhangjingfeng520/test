package com.example.myapplication.base;

import android.app.Application;
import android.content.Context;
import android.util.Log;

import com.baidu.speech.utils.LogUtil;

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
        LogUtil.setLogLevel(4);
        Log.d(TAG, "onCreate: ");
    }
    public static Context getAppContext(){
        return context;
    }
}
