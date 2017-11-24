package com.example.myapplication.utils;

import android.app.ActivityManager;
import android.content.Context;
import android.util.Log;

import java.util.List;

/**
 * Created by Administrator on 2017/11/24.
 */

public class SystemUtils {
    private static final String TAG = "SystemUtils";
    public static boolean isAppAlive(Context context,String packageName){
        ActivityManager activityManager= (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningAppProcessInfo> list=activityManager.getRunningAppProcesses();
        for (int i=0;i<list.size();i++){
            if(list.get(i).processName.equals(packageName)){
                Log.d(TAG, "进程存在");
                return true;
            }
        }
        Log.d(TAG, "进程不存在");
        return false;
    }
}
