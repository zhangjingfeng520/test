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
import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioTrack;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.support.annotation.RequiresApi;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.example.myapplication.base.BaseActivity;
import com.example.myapplication.broadcast.StartActivityReceiver;
import com.example.myapplication.service.MonitorService;
import com.example.myapplication.service.PhoneService;
import com.example.myapplication.service.TestService;
import com.example.myapplication.utils.AudioPlayer;
import com.example.myapplication.utils.AudioRecorder;

import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

public class MyBroadcastActivity extends BaseActivity implements ServiceConnection,AudioRecorder.onRecorderListener{
    MyBroadcast myBroadcast;
    IntentFilter intentFilter;
    NotificationManager notificationManager;
    Intent serviceIntent, monitorIntent, servicePhoneIntent;
    TestService testService;
    private TextView log_tv;
    public static int FLAG = 1;
    public static Handler handler;

    @Override
    public int getLayoutId() {
        return R.layout.activity_my_broad_cast;
    }

    @Override
    public void initData() {
        setToolbarTitle("广播 通知 服务");
        //广播
//        intentFilter=new IntentFilter("android.net.conn.CONNECTIVITY_CHANGE");
        intentFilter = new IntentFilter();
        intentFilter.addAction("android.intent.action.NEW_OUTGOING_CALL");
        intentFilter.addAction("android.intent.action.PHONE_STATE");
        myBroadcast = new MyBroadcast();
        registerReceiver(myBroadcast, intentFilter);
        //通知
        notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.cancel(123321);
        notificationManager.cancel(R.layout.activity_my_broad_cast);
        //service
        serviceIntent = new Intent(this, TestService.class);
        monitorIntent = new Intent(this, MonitorService.class);
        servicePhoneIntent = new Intent(this, PhoneService.class);
    }

    private int frequency = 16000;
    private int channelConfiguration = AudioFormat.CHANNEL_OUT_MONO;
    private int audioEncoding = AudioFormat.ENCODING_PCM_16BIT;

    private int bufferSize = AudioTrack.getMinBufferSize(frequency, channelConfiguration, audioEncoding);
    private AudioTrack track = new AudioTrack(AudioManager.STREAM_MUSIC, 16000, AudioFormat.CHANNEL_OUT_MONO,
            audioEncoding, bufferSize, AudioTrack.MODE_STREAM);
    private boolean playing = false;
    private DataInputStream in = null;

    private void sendMsg(String str) {
        Message msg = new Message();
        msg.what = FLAG;
        msg.obj = str + "\n";
        handler.sendMessage(msg);
    }
    //边录边播
    private AudioRecorder recorder;
    private AudioPlayer player;

    @Override
    public void initView() {
        log_tv = (TextView) findViewById(R.id.log_tv);
        handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                if (msg.what == FLAG) {
                    log_tv.append((String) msg.obj);
//                    log_tv.setText((String)msg.obj);
                }

            }
        };
        //边录边播
        findViewById(R.id.recorder_player_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                player = new AudioPlayer();         // 创建播放器对象
                recorder = new AudioRecorder(MyBroadcastActivity.this); // 创建录音对象
                recorder.start();
            }
        });
        //停止
        findViewById(R.id.stop_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                recorder.stop();    // 停止录音
                player.stop();      // 停止播放
            }
        });
        //录音
        findViewById(R.id.play_service_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendMsg("-------------------\n播放中...");
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        File file = new File(Environment.getExternalStorageDirectory() + "/12345.pcm");
                        byte[] bytes = new byte[bufferSize];
                        int readCount = 0;
                        try {
                            in = new DataInputStream(new FileInputStream(file));
                        } catch (FileNotFoundException e) {
                            e.printStackTrace();
                        }
                        sendMsg("文件名: " + file.getName() + "\n文件大小: " + bytes.length);
                        try {
                            while (in.available() > 0) {
                                readCount = in.read(bytes);
                                if (readCount == AudioTrack.ERROR_INVALID_OPERATION || readCount == AudioTrack.ERROR_BAD_VALUE) {
                                    continue;
                                }
                                if (readCount != 0 && readCount != -1) {
                                    track.play();
                                    track.write(bytes, 0, readCount);
                                }
                            }
                            sendMsg("播放完毕\n");
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }).start();
            }
        });
        //停止播放录音
        findViewById(R.id.stop_play_button).
                setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (track != null) {
                            if (track.getState() == track.STATE_INITIALIZED) {
                                track.stop();
                                sendMsg("已停止");
                            }
                            if (track != null) {
//                                track.release();
                            }
                        }
                        if (in != null) {
                            try {
                                in.close();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                });

        //发起广播
        findViewById(R.id.broadcast_button).
                setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
//                Intent intent = new Intent(MyBroadcast.ACTION);
                        Intent intent = new Intent(getApplicationContext(), StartActivityReceiver.class);
                        intent.putExtra("zjf", "123");
                        sendBroadcast(intent);
                    }
                });

        //发起通知
        findViewById(R.id.notification_button).
                setOnClickListener(new View.OnClickListener() {
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
        findViewById(R.id.start_service_button).

                setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
//                startService(serviceIntent);
                        startService(servicePhoneIntent);
                    }
                });

        //停止Service
        findViewById(R.id.stop_service_button).

                setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
//                stopService(serviceIntent);
                        stopService(servicePhoneIntent);
                    }
                });

        //绑定
        findViewById(R.id.bind_service_button).

                setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        bindService(serviceIntent, MyBroadcastActivity.this, BIND_AUTO_CREATE);
                    }
                });

        //解除绑定
        findViewById(R.id.Unbind_service_button).

                setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        unbindService(MyBroadcastActivity.this);
                    }
                });

        //
        findViewById(R.id.show_number_button).

                setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
//                Toast.makeText(MyBroadcastActivity.this,testService.getCurrentNum()+"",Toast.LENGTH_LONG).show();
                        Log.d("TestService", "onClick: " + testService.getCurrentNum());
                    }
                });

        //启动Service保活
        findViewById(R.id.start_monitor_button).

                setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        startService(monitorIntent);
                    }
                });

        //停止Service保活
        findViewById(R.id.stop_monitor_button).

                setOnClickListener(new View.OnClickListener() {
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
        Log.d("TestService", "onServiceConnected: " + componentName.getClassName());
        testService = ((TestService.TestServiceBinder) iBinder).getService();

    }

    //service崩溃时调用
    @Override
    public void onServiceDisconnected(ComponentName componentName) {
        Log.d("TestService", "onServiceDisconnected: ");
    }

    boolean isRecog = false;

    @Override
    public void handleRecordData(byte[] recordData, int offset, int size) {
        // 将录音捕捉的音频数据写入到播放器中播放
        if (player != null) {
            player.play(recordData, offset, size);
        }
    }

    //广播类
    class MyBroadcast extends BroadcastReceiver {
        public static final String ACTION = "com.example.myapplication.intent.action.MyBroadcast";
        private static final String TAG = "MyBroadcast";

        @Override
        public void onReceive(Context context, Intent intent) {
            Log.d(TAG, intent.getAction());
            if (!isRecog) {
                context.startService(servicePhoneIntent);
                isRecog = true;
            } else {
                context.stopService(servicePhoneIntent);
                isRecog = false;
            }
        }
//            ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
//            NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
//            if (networkInfo != null && networkInfo.isAvailable()) {
//                Toast.makeText(MyBroadcastActivity.this, "可用" + intent.getStringExtra("zjf"), Toast.LENGTH_LONG).show();
//            } else
//                Toast.makeText(MyBroadcastActivity.this, "不可用", Toast.LENGTH_LONG).show();
//        }
    }
}
