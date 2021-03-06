package com.example.myapplication;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.baidu.speech.EventListener;
import com.baidu.speech.EventManager;
import com.baidu.speech.EventManagerFactory;
import com.baidu.speech.asr.SpeechConstant;
import com.example.myapplication.service.PhoneService;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Created by fujiayi on 2017/8/15.
 */

public class ActivityMiniRecog extends AppCompatActivity implements EventListener {
    protected TextView txtLog;
    protected TextView txtResult;
    protected Button btn,btn2;
    protected Button stopBtn;
    protected Button btn1;
    private static String DESC_TEXT = "精简版识别，带有SDK唤醒运行的最少代码，仅仅展示如何调用，\n" +
            "也可以用来反馈测试SDK输入参数及输出回调。\n" +
            "本示例需要自行根据文档填写参数，可以使用之前识别示例中的日志中的参数。\n" +
            "需要完整版请参见之前的识别示例。\n" +
            "需要测试离线命令词识别功能可以将本类中的enableOffline改成true，首次测试离线命令词请联网使用。之后请说出“打电话给张三”";

    private EventManager asr;

    private boolean logTime = true;

    private boolean enableOffline = false; // 测试离线命令词，需要改成true
    private MyBroadcast myBroadcast;
    private static final String TAG = "ActivityMiniRecog";
    private Intent intent1;

    /**
     * 测试参数填在这里
     */
    private void start() {
        if(asr!=null){
            stop();
        }
        txtLog.setText("");
        Map<String, Object> params = new LinkedHashMap<String, Object>();
        String event = null;
        event = SpeechConstant.ASR_START; // 替换成测试的event
        params.put(SpeechConstant.DISABLE_PUNCTUATION, false);
        params.put(SpeechConstant.ACCEPT_AUDIO_VOLUME, false);
        params.put(SpeechConstant.VAD, SpeechConstant.VAD_DNN);
        params.put(SpeechConstant.ACCEPT_AUDIO_DATA, false);
        params.put(SpeechConstant.VAD_ENDPOINT_TIMEOUT,0);//长语音，无静音短句.
        params.put(SpeechConstant.PID,19361);//模型

//        params.put(SpeechConstant.OUT_FILE, Environment.getExternalStorageDirectory() + "/123.pcm");
        if (enableOffline) {
            params.put(SpeechConstant.DECODER, 2);
        }
        //  params.put(SpeechConstant.NLU, "enable");
        // params.put(SpeechConstant.VAD_ENDPOINT_TIMEOUT, 800);
        // params.put(SpeechConstant.VAD, SpeechConstant.VAD_DNN);
        //  params.put(SpeechConstant.PROP ,20000);
        String json = null; //可以替换成自己的json
        json = new JSONObject(params).toString(); // 这里可以替换成你需要测试的json
        asr.send(event, json, null, 0, 0);
        printLog("输入参数：" + json);
        Log.d(TAG, "start: ");
//        startService(intent);
    }

    /**
     * 测试参数填在这里
     */
    private void start1() {
        if(asr!=null){
            stop();
        }
        txtLog.setText("");
        Map<String, Object> params = new LinkedHashMap<String, Object>();
        String event = null;
        event = SpeechConstant.ASR_START; // 替换成测试的event
        params.put(SpeechConstant.DISABLE_PUNCTUATION, false);
        params.put(SpeechConstant.ACCEPT_AUDIO_VOLUME, false);
        params.put(SpeechConstant.VAD, SpeechConstant.VAD_DNN);
        params.put(SpeechConstant.ACCEPT_AUDIO_DATA, false);
        params.put(SpeechConstant.VAD_ENDPOINT_TIMEOUT,0);
        params.put(SpeechConstant.PID,19361);//模型
//        params.put(SpeechConstant.IN_FILE,Environment.getExternalStorageDirectory()+"/123.pcm");
        params.put(SpeechConstant.IN_FILE, "#com.example.myapplication.ActivityMiniRecog.getFileBytes()");
        if (enableOffline) {
            params.put(SpeechConstant.DECODER, 2);
        }
        String json = null; //可以替换成自己的json
        json = new JSONObject(params).toString(); // 这里可以替换成你需要测试的json
//        asr.send(event, json, bytes, 0, length);
        asr.send(event, json, null, 0, 0);
        printLog("输入参数：" + json);
        Log.d(TAG, "start: ");
//        startService(intent);
    }

    private static DataInputStream din=null;
    public static InputStream getFileBytes() {
        int length = 0;
        File file = new File(Environment.getExternalStorageDirectory() + "/12345.pcm");
        try {
            din=new DataInputStream(new FileInputStream(file));
            Log.d(TAG, "getFileBytes: 1");
            return din;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            Log.d(TAG, "getFileBytes: 2");
            return null;
        }

    }

    //16K采集率
    public static int frequency = 16000;
    //格式
    public static int channelConfiguration = AudioFormat.CHANNEL_IN_MONO;
    //16Bit
    public static int audioEncoding = AudioFormat.ENCODING_PCM_16BIT;

    private static int bufferSize = AudioRecord.getMinBufferSize(frequency, channelConfiguration, audioEncoding);

    private static AudioRecord audioRecord = new AudioRecord(MediaRecorder.AudioSource.MIC, frequency, channelConfiguration, audioEncoding, bufferSize);

    boolean isRecording = false;
    DataOutputStream dos = null;

    public void  startRecord() {
        isRecording=true;
        File file = new File(Environment.getExternalStorageDirectory().getAbsolutePath(),"12345.pcm");
        Log.i(TAG, "生成文件");
        //如果存在，就先删除再创建
        if (file.exists()) {
            file.delete();
            Log.i(TAG, "删除文件");
        }
        try {
            file.createNewFile();
            Log.i(TAG, "创建文件");
        } catch (IOException e) {
            e.printStackTrace();
            Log.i(TAG, "创建文件失败");
        }
        try {
            dos = new DataOutputStream(new FileOutputStream(file));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        Log.i(TAG, "开始录音");
        byte[] buffer = new byte[bufferSize];
        audioRecord.startRecording();
        while (isRecording) {
            int bufferReadResult = audioRecord.read(buffer, 0, bufferSize);
            try {
                dos.write(buffer,0,bufferReadResult);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        audioRecord.stop();
        try {
            dos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Log.d(TAG, "onStop: 11");

    }

    private void stop()
    {
        isRecording=false;

//        if(audioRecord!=null) {
//            if (audioRecord.getState() == AudioRecord.STATE_INITIALIZED) {
//                audioRecord.stop();
//            }
//            if (audioRecord != null) {
//                audioRecord.release();
//            }
//            Log.d(TAG, "stop: 释放audioRecord");
//        }

        asr.send(SpeechConstant.ASR_STOP, null, null, 0, 0);
        Log.d(TAG, "stop: ");//
        if (din!=null){
            try {
                din.close();
                din=null;
                cancel();
                Log.d(TAG, "stop: 释放din");

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
//        stopService(intent);
    }
    /**
     * 取消本次识别，取消后将立即停止不会返回识别结果。
     * cancel 与stop的区别是 cancel在stop的基础上，完全停止整个识别流程，
     */
    public void cancel() {
        if (asr != null){
            Log.d(TAG, "cancel: 取消识别");
            asr.send(SpeechConstant.ASR_CANCEL, "{}", null, 0, 0);
        }
    }

    private void loadOfflineEngine() {
        Map<String, Object> params = new LinkedHashMap<String, Object>();
        params.put(SpeechConstant.DECODER, 2);
        params.put(SpeechConstant.ASR_OFFLINE_ENGINE_GRAMMER_FILE_PATH, "assets://baidu_speech_grammar.bsg");
        asr.send(SpeechConstant.ASR_KWS_LOAD_ENGINE, new JSONObject(params).toString(), null, 0, 0);
    }

    private void unloadOfflineEngine() {
        asr.send(SpeechConstant.ASR_KWS_UNLOAD_ENGINE, null, null, 0, 0); //
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.common_mini);
        initView();
        initPermission();
        asr = EventManagerFactory.create(this, "asr");

        asr.registerListener(this); //  EventListener 中 onEvent方法
        btn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                start();
            }
        });
        btn2.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
//                start();
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        startRecord();
                    }
                }).start();
                try {
                    Thread.sleep(5000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                isRecording=false;
                start1();


            }
        });
        stopBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                stop();
            }
        });
        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                start1();
            }
        });
        if (enableOffline) {
            loadOfflineEngine(); //测试离线命令词请开启, 测试 ASR_OFFLINE_ENGINE_GRAMMER_FILE_PATH 参数时开启
        }

        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("android.intent.action.NEW_OUTGOING_CALL");
        intentFilter.addAction("android.intent.action.PHONE_STATE");
        myBroadcast = new MyBroadcast();
        registerReceiver(myBroadcast, intentFilter);
        intent1 = new Intent(this, PhoneService.class);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        asr.send(SpeechConstant.ASR_CANCEL, "{}", null, 0, 0);
        if (enableOffline) {
            unloadOfflineEngine(); //测试离线命令词请开启, 测试 ASR_OFFLINE_ENGINE_GRAMMER_FILE_PATH 参数时开启
        }
        unregisterReceiver(myBroadcast);
        asr.unregisterListener(this);
        asr=null;
    }

    //   EventListener  回调方法
    @Override
    public void onEvent(String name, String params, byte[] data, int offset, int length) {
        String logTxt = "name: " + name;


        if (params != null && !params.isEmpty()) {
            logTxt += " ;params :" + params;
            try {
                String str = new JSONObject(params).getString("best_result");
                Log.d(TAG, "onEvent: " + str);
                if(str!=null&!str.isEmpty())
                txtResult.setText(str);
            } catch (JSONException e) {
                e.printStackTrace();

            }
        }
        if (name.equals(SpeechConstant.CALLBACK_EVENT_ASR_PARTIAL)) {
            if (params.contains("\"nlu_result\"")) {
                if (length > 0 && data.length > 0) {
                    logTxt += ", 语义解析结果：" + new String(data, offset, length);
                }
            }
        } else if (data != null) {
//            logTxt += " ;data length=" + data.length;
            return;
        }
        printLog(logTxt);
    }

    private void printLog(String text) {
        if (logTime) {
            text += "  ;time=" + System.currentTimeMillis();
        }
        text += "\n";
        Log.i(getClass().getName(), text);
        txtLog.append(text + "\n");
    }


    private void initView() {
        txtResult = (TextView) findViewById(R.id.txtResult);
        txtLog = (TextView) findViewById(R.id.txtLog);
        btn = (Button) findViewById(R.id.btn);
        btn1 = (Button) findViewById(R.id.btn1);
        btn2 = (Button) findViewById(R.id.btn2);
        stopBtn = (Button) findViewById(R.id.btn_stop);
        txtLog.setText(DESC_TEXT + "\n");
    }

    /**
     * android 6.0 以上需要动态申请权限
     */
    private void initPermission() {
        String permissions[] = {Manifest.permission.RECORD_AUDIO,
                Manifest.permission.ACCESS_NETWORK_STATE,
                Manifest.permission.INTERNET,
                Manifest.permission.READ_PHONE_STATE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
        };

        ArrayList<String> toApplyList = new ArrayList<String>();

        for (String perm : permissions) {
            if (PackageManager.PERMISSION_GRANTED != ContextCompat.checkSelfPermission(this, perm)) {
                toApplyList.add(perm);
                //进入到这里代表没有权限.

            }
        }
        String tmpList[] = new String[toApplyList.size()];
        if (!toApplyList.isEmpty()) {
            ActivityCompat.requestPermissions(this, toApplyList.toArray(tmpList), 123);
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        // 此处为android 6.0以上动态授权的回调，用户自行实现。
    }

    private boolean recog = true;

    //广播类
    class MyBroadcast extends BroadcastReceiver {
        public static final String ACTION = "com.example.myapplication.intent.action.MyBroadcast";

        @Override
        public void onReceive(Context context, Intent intent) {
            Log.d(TAG, intent.getAction());
            if (recog) {
                recog = false;
//                context.startService(intent1);
//                start1();
            } else {
                recog = true;
//                context.stopService(intent1);
//                stop();
            }
        }
    }

}
