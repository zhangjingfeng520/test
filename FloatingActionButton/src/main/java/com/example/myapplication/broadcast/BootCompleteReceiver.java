package com.example.myapplication.broadcast;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.widget.Toast;

import com.example.myapplication.R;

public class BootCompleteReceiver extends BroadcastReceiver {

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void onReceive(Context context, Intent intent) {
        Intent intent1=context.getPackageManager().getLaunchIntentForPackage("com.example.myapplication");
        Toast.makeText(context,"开机了哦",Toast.LENGTH_LONG).show();
        NotificationManager notificationManager= (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        Notification.Builder notification=new Notification.Builder(context)
                .setContentTitle("提示")
                .setContentText("已开机")
                .setSmallIcon(R.mipmap.ic_launcher)
                .setDefaults(Notification.DEFAULT_ALL)
                .setContentIntent(PendingIntent.getActivity(context,101,intent1,PendingIntent.FLAG_UPDATE_CURRENT));
        notificationManager.notify(123321,notification.build());
    }
}
