package com.example.myapplication.baiduyuyin;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.baidu.speech.EventListener;
import com.baidu.speech.asr.SpeechConstant;
import com.example.myapplication.R;

import junit.framework.TestResult;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Created by fujiayi on 2017/8/15.
 */

public class ActivityWakeUpRecog extends AppCompatActivity {
    protected TextView txtLog;
    protected TextView txtResult;
    protected Button btn;
    protected Button stopBtn;
    private static String DESC_TEXT = "精简版唤醒，带有SDK唤醒运行的最少代码，仅仅展示如何调用，\n" +
            "也可以用来反馈测试SDK输入参数及输出回调。\n" +
            "本示例需要自行根据文档填写参数，可以使用之前唤醒示例中的日志中的参数。\n" +
            "需要完整版请参见之前的唤醒示例。\n\n" +
            "唤醒词是纯离线功能，需要获取正式授权文件（与离线命令词的正式授权文件是同一个）。 第一次联网使用唤醒词功能自动获取正式授权文件。之后可以断网测试\n" +
            "请说“小度你好”或者 “百度一下”\n\n";

    private boolean logTime = true;
    private MyRecognizer myRecognizer;//识别
    private MyWakeup myWakeup;//唤醒

    /**
     * 测试参数填在这里
     */
    private void start() {
        txtLog.setText("");
        Map<String,Object> params = new HashMap<String,Object>();
        params.put(SpeechConstant.ACCEPT_AUDIO_VOLUME, false);
        params.put(SpeechConstant.WP_WORDS_FILE, "assets:///WakeUp.bin");//"assets:///WakeUp.bin" 表示WakeUp.bin文件定义在assets目录下

        // params.put(SpeechConstant.ACCEPT_AUDIO_DATA,true);
        //params.put(SpeechConstant.ACCEPT_AUDIO_VOLUME,true);
        // params.put(SpeechConstant.IN_FILE,"res:///com/baidu/android/voicedemo/wakeup.pcm");
        // params里 "assets:///WakeUp.bin" 表示WakeUp.bin文件定义在assets目录下
        myWakeup.start(params);
        printLog("输入参数：" + new JSONObject(params).toString());
    }

    private void stop() {
        myWakeup.stop();
        myRecognizer.stop();
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.common_mini);
        initView();

        myWakeup=new MyWakeup(this,new WakeupEventListener());
        myRecognizer=new MyRecognizer(this,new RecognizerEventListener());
        btn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                start();
            }
        });
        stopBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                stop();
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        myWakeup.release();
        myRecognizer.release();
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
        stopBtn = (Button) findViewById(R.id.btn_stop);
        txtLog.setText(DESC_TEXT + "\n");
    }
    //识别事件监听
    public class RecognizerEventListener implements EventListener {

        private static final String TAG = "WakeupEventAdapter";

        @Override
        public void onEvent(String name, String params, byte[] data, int offset, int length) {
            String logTxt = "name: " + name;
            if (params != null && !params.isEmpty()) {
                logTxt += " ;params :" + params;
                String str = null;
                try {
                    str = new JSONObject(params).getString("best_result");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                if(str!=null&!str.isEmpty())
                    txtResult.setText(str);
            } else if (data != null) {
                logTxt += " ;data length=" + data.length;
            }
            printLog(logTxt);

            }
        }

    //唤醒事件监听
    public class WakeupEventListener implements EventListener {

        private static final String TAG = "WakeupEventAdapter";

        @Override
        public void onEvent(String name, String params, byte[] data, int offset, int length) {
            //ui日志
            String logTxt = "name: " + name;
            if (params != null && !params.isEmpty()) {
                logTxt += " ;params :" + params;
            } else if (data != null) {
                logTxt += " ;data length=" + data.length;
            }
            printLog(logTxt);
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
