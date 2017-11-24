package com.example.myapplication.broadcast;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.example.myapplication.MainActivity;
import com.example.myapplication.utils.SystemUtils;

public class StartActivityReceiver extends BroadcastReceiver {
    private static final String TAG = "StartActivityReceiver";

    @Override
    public void onReceive(Context context, Intent intent1) {
        Log.d(TAG, "onReceive: ");
        if (SystemUtils.isAppAlive(context, "com.example.myapplication")) {
            Intent intent=new Intent(context, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TOP);
            context.startActivity(intent);
        } else {
//        Intent intent=new Intent();
//        intent.setComponent(new ComponentName("com.example.myapplication","com.example.myapplication.MainActivity"));
//        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED
//        );
            Intent intent = context.getPackageManager().getLaunchIntentForPackage("com.example.myapplication");
            context.startActivity(intent);
        }
    }
}
