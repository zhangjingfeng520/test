package com.example.myapplication.baiduyuyin;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.IntDef;
import android.util.Log;

import com.baidu.speech.EventListener;
import com.baidu.speech.asr.SpeechConstant;
import com.example.myapplication.DataStorageActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

public class WakeUpRecogService extends Service {

    private MyRecognizer myRecognizer;//识别
    private MyWakeup myWakeup;//唤醒
    private static final String TAG = "aaa";

    public WakeUpRecogService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        myWakeup=new MyWakeup(this,new WakeupEventListener());
        myRecognizer=new MyRecognizer(this,new RecognizerEventListener());

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        start();
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
//        stop();
        myWakeup.release();
        myRecognizer.release();
        super.onDestroy();
    }
    private void start() {
        Map<String,Object> params = new HashMap<String,Object>();
        params.put(SpeechConstant.ACCEPT_AUDIO_VOLUME, false);
        params.put(SpeechConstant.WP_WORDS_FILE, "assets:///WakeUp.bin");//"assets:///WakeUp.bin" 表示WakeUp.bin文件定义在assets目录下

        // params.put(SpeechConstant.ACCEPT_AUDIO_DATA,true);
        //params.put(SpeechConstant.ACCEPT_AUDIO_VOLUME,true);
        // params.put(SpeechConstant.IN_FILE,"res:///com/baidu/android/voicedemo/wakeup.pcm");
        // params里 "assets:///WakeUp.bin" 表示WakeUp.bin文件定义在assets目录下
        myWakeup.start(params);
        Log.i(TAG, "输入参数：" + new JSONObject(params).toString());
    }

    private void stop() {
        myWakeup.stop();
        myRecognizer.stop();
    }
    //识别事件监听
    public class RecognizerEventListener implements EventListener {

        @Override
        public void onEvent(String name, String params, byte[] data, int offset, int length) {
            String logTxt = "name: " + name;
            if (params != null && !params.isEmpty()) {
                logTxt += " ;params :" + params;
                try {
                   String str = new JSONObject(params).getString("best_result");
                    if(str!=null&&!str.isEmpty())
                        Log.i(TAG,"result: "+str);
                    DataStorageActivity.handler.obtainMessage(1,str).sendToTarget();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } else if (data != null) {
                logTxt += " ;data length=" + data.length;
            }
            Log.i(TAG,logTxt);

        }
    }

    //唤醒事件监听
    public class WakeupEventListener implements EventListener {

        @Override
        public void onEvent(String name, String params, byte[] data, int offset, int length) {
            //ui日志
            String logTxt = "name: " + name;
            if (params != null && !params.isEmpty()) {
                logTxt += " ;params :" + params;
            } else if (data != null) {
                logTxt += " ;data length=" + data.length;
            }
            Log.i(TAG,logTxt);
            //业务逻辑
            if (SpeechConstant.CALLBACK_EVENT_WAKEUP_SUCCESS.equals(name)) { //识别唤醒词成功
                int error = 1;
                try {
                    JSONObject json = new JSONObject(params);
                    error = json.optInt("errorCode");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                if (error == 0) { // error不为0依旧有可能是异常情况
                    //成功
//                Intent intent=new Intent();
//                intent.setAction(Intent.ACTION_VIEW);
//                intent.setData(Uri.parse("http://www.baidu.com"));
//                startActivity(intent);
                    startRecog();
                } else {
                    //失败
                }
            } else if (SpeechConstant.CALLBACK_EVENT_WAKEUP_ERROR.equals(name)) { // 识别唤醒词报错
                int error = 1;
                try {
                    JSONObject json = new JSONObject(params);
                    error = json.optInt("errorCode");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                if (error != 0) {
                    //失败
                }

            }
        }
    }

    /**
     *  0: 方案1， 唤醒词说完后，直接接句子，中间没有停顿。
     * >0 : 方案2： 唤醒词说完后，中间有停顿，然后接句子。推荐4个字 1500ms
     * 表示回溯1.5s的音频
     *  backTrackInMs 最大 15000，即15s
     */
    private int backTrackInMs = 1500;
    //开始识别
    private void startRecog() {
        // 此处 开始正常识别流程
        Map<String, Object> params = new LinkedHashMap<String, Object>();
        params.put(SpeechConstant.ACCEPT_AUDIO_VOLUME, false);
        params.put(SpeechConstant.VAD,SpeechConstant.VAD_DNN);
        int pid = PidBuilder.create().model(PidBuilder.INPUT).toPId(); //如识别短句，不需要需要逗号，将PidBuilder.INPUT改为搜索模型PidBuilder.SEARCH
        params.put(SpeechConstant.PID,pid);
        if (backTrackInMs > 0) { // 方案1， 唤醒词说完后，直接接句子，中间没有停顿。
            params.put(SpeechConstant.AUDIO_MILLS, System.currentTimeMillis() - backTrackInMs);

        }
        myRecognizer.cancel();
        myRecognizer.start(params);
    }
}
