package com.example.myapplication;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.support.annotation.RequiresApi;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.myapplication.base.BaseActivity;
import com.example.myapplication.broadcast.StartActivityReceiver;
import com.example.myapplication.service.MonitorService;
import com.example.myapplication.service.TestService;

public class MyBroadcastActivity extends BaseActivity implements ServiceConnection {
    MyBroadcast myBroadcast;
    IntentFilter intentFilter;
    NotificationManager notificationManager;
    Intent serviceIntent,monitorIntent;
    TestService testService;

    @Override
    public int getLayoutId() {
        return R.layout.activity_my_broad_cast;
    }

    @Override
    public void initData() {
        setToolbarTitle("广播 通知 服务");
        //广播
//        intentFilter=new IntentFilter("android.net.conn.CONNECTIVITY_CHANGE");
        intentFilter = new IntentFilter(MyBroadcast.ACTION);
        myBroadcast = new MyBroadcast();
        registerReceiver(myBroadcast, intentFilter);
        //通知
        notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.cancel(123321);
        notificationManager.cancel(R.layout.activity_my_broad_cast);
        //service
        serviceIntent=new Intent(this, TestService.class);
        monitorIntent=new Intent(this, MonitorService.class);
    }

    @Override
    public void initView() {
        //发起广播
        findViewById(R.id.broadcast_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Intent intent = new Intent(MyBroadcast.ACTION);
                Intent intent=new Intent(getApplicationContext(),StartActivityReceiver.class);
                intent.putExtra("zjf", "123");
                sendBroadcast(intent);
            }
        });
        //发起通知
        findViewById(R.id.notification_button).setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
            @Override
            public void onClick(View view) {
                final Notification.Builder builder = new Notification.Builder(MyBroadcastActivity.this)
                        .setTicker("Ticker")
                        .setContentTitle("我是标题")
                        .setContentText("我是内容")
                        .setContentIntent(PendingIntent.getActivity(MyBroadcastActivity.this, 1, getIntent(), 0))
                        .setLights(0xFF0000, 2000, 2000)
                        .setDefaults(Notification.DEFAULT_ALL)
                        .setSmallIcon(R.mipmap.ic_launcher);
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        notificationManager.notify(R.layout.activity_my_broad_cast, builder.build());
                    }
                }, 5000);
            }
        });
        //启动Service
        findViewById(R.id.start_service_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startService(serviceIntent);
            }
        });
        //停止Service
        findViewById(R.id.stop_service_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                stopService(serviceIntent);
            }
        });
        //绑定
        findViewById(R.id.bind_service_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bindService(serviceIntent,MyBroadcastActivity.this,BIND_AUTO_CREATE);
            }
        });
        //解除绑定
        findViewById(R.id.Unbind_service_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                unbindService(MyBroadcastActivity.this);
            }
        });
        //
        findViewById(R.id.show_number_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Toast.makeText(MyBroadcastActivity.this,testService.getCurrentNum()+"",Toast.LENGTH_LONG).show();
                Log.d("TestService", "onClick: "+testService.getCurrentNum());
            }
        });
        //启动Service保活
        findViewById(R.id.start_monitor_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startService(monitorIntent);
            }
        });
        //停止Service保活
        findViewById(R.id.stop_monitor_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                stopService(monitorIntent);
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(myBroadcast);
    }
    //绑定成功时调用
    @Override
    public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
        Log.d("TestService", "onServiceConnected: "+componentName.getClassName());
        testService=((TestService.TestServiceBinder)iBinder).getService();

    }
    //service崩溃时调用
    @Override
    public void onServiceDisconnected(ComponentName componentName) {
        Log.d("TestService", "onServiceDisconnected: ");
    }

    //广播类
    class MyBroadcast extends BroadcastReceiver {
        public static final String ACTION = "com.example.myapplication.intent.action.MyBroadcast";

        @Override
        public void onReceive(Context context, Intent intent) {
            ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
            if (networkInfo != null && networkInfo.isAvailable()) {
                Toast.makeText(MyBroadcastActivity.this, "可用" + intent.getStringExtra("zjf"), Toast.LENGTH_LONG).show();
            } else
                Toast.makeText(MyBroadcastActivity.this, "不可用", Toast.LENGTH_LONG).show();
        }
    }
}
