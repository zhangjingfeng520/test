package com.example.myapplication.service;

import android.app.Service;
import android.content.Intent;
import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.MediaRecorder;
import android.os.Environment;
import android.os.IBinder;
import android.os.Message;
import android.support.annotation.Nullable;
import android.util.Log;

import com.example.myapplication.MyBroadcastActivity;

import java.io.BufferedOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Administrator on 2017/12/5.
 */

public class PhoneService extends Service {
    private static final String TAG = "PhoneService";
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    private MediaRecorder mediaRecorder;
    private File file;

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
//        //MediaRecorder
//        startMediaRecorder();

        //AudioRecord
        file = new File(Environment.getExternalStorageDirectory().getAbsolutePath(),"12345.pcm");
        Log.i(TAG, "生成文件");
        stringBuilder.append("->生成文件");
        //如果存在，就先删除再创建
        if (file.exists()) {
            file.delete();
            Log.i(TAG, "删除文件");
            stringBuilder.append("->删除文件");
        }
        try {
            file.createNewFile();
            Log.i(TAG, "创建文件");
            stringBuilder.append("->创建文件");
        } catch (IOException e) {
            e.printStackTrace();
            Log.i(TAG, "创建文件失败");
            stringBuilder.append("->创建文件失败");
        }
        new Thread(new Runnable() {
            @Override
            public void run() {
                startRecord();
            }
        }).start();

        return super.onStartCommand(intent, flags, startId);

    }

    private boolean isRecording = false;
    private StringBuilder stringBuilder=new StringBuilder();
    //AudioRecord
    public void startRecord() {
        Log.i(TAG, "-------------------\n开始录音");
        stringBuilder.append("-------------------\n开始录音");
        //16K采集率
        int frequency = 16000;
        //格式
        int channelConfiguration = AudioFormat.CHANNEL_IN_MONO;
        //16Bit
        int audioEncoding = AudioFormat.ENCODING_PCM_16BIT;
        //生成PCM文件


        try {
            //输出流
            OutputStream os = new FileOutputStream(file);
            DataOutputStream dos = new DataOutputStream(os);
            int bufferSize = AudioRecord.getMinBufferSize(frequency, channelConfiguration, audioEncoding);
            AudioRecord audioRecord = new AudioRecord(MediaRecorder.AudioSource.MIC, frequency, channelConfiguration, audioEncoding, bufferSize);

            byte[] buffer = new byte[bufferSize];
            audioRecord.startRecording();
            Log.i(TAG, "开始录音");
            stringBuilder.append("->读写音频流");
            sendMsg(stringBuilder.toString());
            isRecording = true;
            while (isRecording) {
                int bufferReadResult = audioRecord.read(buffer, 0, bufferSize);
                    dos.write(buffer,0,bufferReadResult);

            }
            sendMsg("->录音线程已关闭\n录音文件为："+file.getName());
            audioRecord.stop();
            dos.close();
            Log.d(TAG, "onStop: 11");
        } catch (Throwable t) {
            t.printStackTrace();
            Log.e(TAG, "录音失败");
            sendMsg("->读写音频流失败");
        }
    }

    //MediaRecorder
    private void startMediaRecorder() {
        File file1 = new File(Environment.getExternalStorageDirectory() + "/2222.amr");
        if (file1.exists()) {
            file1.delete();
            Log.d(TAG, "onStartCommand: 删除成功");
        }
        file = new File(Environment.getExternalStorageDirectory(), "2222.amr");
        mediaRecorder = new MediaRecorder();
        mediaRecorder.setAudioSource(MediaRecorder.AudioSource.DEFAULT);
        mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.DEFAULT);
        mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
        mediaRecorder.setOutputFile(file.getAbsolutePath());   //输出文件
        try {
            mediaRecorder.prepare();    //准备
        } catch (IOException e) {
            e.printStackTrace();
        }
        mediaRecorder.start();
        Log.d(TAG, "onStartCommand: ");
    }

    @Override
    public void onDestroy() {
        onStop();
        Log.d(TAG, "onDestroy: ");
        sendMsg("->service已销毁 "+new SimpleDateFormat("hh:mm:ss").format(new Date()));
        super.onDestroy();
        
    }

    private void onStop() {
//        if(mediaRecorder != null)
//        {
//            mediaRecorder.stop();
//            mediaRecorder.release();
//            mediaRecorder = null;
//        }
        isRecording = false;
        sendMsg("->录音停止");
        Log.d(TAG, "onStop: ");
    }

    private void sendMsg(String str) {
        Message msg=new Message();
        msg.what= MyBroadcastActivity.FLAG;
        msg.obj=str+"\n";
        MyBroadcastActivity.handler.sendMessage(msg);
    }

}
