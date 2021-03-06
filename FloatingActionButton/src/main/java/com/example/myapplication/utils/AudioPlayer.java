package com.example.myapplication.utils;

import android.media.AudioTrack;

import com.baidu.speech.utils.LogUtil;
import com.example.myapplication.tool.Constants;

/**
 * Created by Administrator on 2017-12-07.
 */

public class AudioPlayer {
    private static final String tag = "【AudioPlayer】";

    private int DEFAULT_SAMPLERATEINHZ = Constants.sampleRateInHz;        // 采样频率
    private int DEFAULT_AUDIOFORMAT = Constants.audioFormat;              // 数据格式
    private int DEFAULT_STREAMTYPE = Constants.streamType;                // 音频类型
    private int DEFAULT_CHANNELCONFIG_OUT = Constants.channelConfigOut;   // 声道配置
    private int DEFAULT_MODE = Constants.mode;                            // 输出模式

    private AudioTrack player;      // 播放器实例
    private boolean isWorking;      // 是否正在工作
    private int playerBufferSize;   // 缓冲区大小

    public AudioPlayer() {
        init();
    }

    /** 初始化 */
    private void init() {

        //1. 获取最小缓冲区大小
        playerBufferSize = AudioTrack.getMinBufferSize(DEFAULT_SAMPLERATEINHZ,
                DEFAULT_CHANNELCONFIG_OUT, DEFAULT_AUDIOFORMAT);
        switch (playerBufferSize) {
            case AudioTrack.ERROR_BAD_VALUE:
                LogUtil.i(tag, "无效的音频参数");
                break;
            case AudioTrack.ERROR:
                LogUtil.i(tag, "不能够查询音频输出的性能");
                break;
            default:
                LogUtil.i(tag, "AudioTrack的音频缓冲区的最小尺寸(与本机硬件有关)：" + playerBufferSize);
                break;
        }

        //2. 创建AudioTrack实例
        player = new AudioTrack(DEFAULT_STREAMTYPE, DEFAULT_SAMPLERATEINHZ, DEFAULT_CHANNELCONFIG_OUT,
                DEFAULT_AUDIOFORMAT, playerBufferSize * 4, DEFAULT_MODE);
        switch (player.getState()) {
            case AudioTrack.STATE_INITIALIZED:
                LogUtil.i(tag, "AudioTrack实例初始化成功!");
                break;
            case AudioTrack.STATE_UNINITIALIZED:
                LogUtil.i(tag, "AudioTrack实例初始化失败!");
                break;
            case AudioTrack.STATE_NO_STATIC_DATA:
                LogUtil.i(tag, "AudioTrack实例初始化成功，目前没有静态数据输入!");
                break;
        }
        LogUtil.i(tag, "当前AudioTrack实例的播放状态：" + player.getPlayState());

        //3. 设置开始工作
        isWorking = true;
    }

    /** 播放语音数据 */
    public void play(byte[] audioData,int offset, int size) {

        if (!isWorking) {
//            CommUtil.Toast("AudioTrack is not working!");
        }

        try {
            //4. 写入数据
            int write = player.write(audioData, offset, size);

            if (write < 0) {
                LogUtil.i(tag, "write失败");
                switch (write) {
                    case AudioTrack.ERROR_INVALID_OPERATION:    // -3
                        LogUtil.i(tag, "AudioTrack实例初始化失败!");
                        break;
                    case AudioTrack.ERROR_BAD_VALUE:            // -2
                        LogUtil.i(tag, "无效的音频参数");
                        break;
                    case AudioTrack.ERROR:                      // -1
                        LogUtil.i(tag, "通用操作失败");
                        break;
                }
            } else {
                LogUtil.i(tag, "成功写入数据：" + size + " Shorts");
            }

            //5. 播放音频数据
            player.play();
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    /** 停止播放语音数据 */
    public void stop() {

        if (!isWorking) {
//            CommUtil.Toast("已经停止播放!");
            return;
        }

        //6. 停止播放
        try {
            player.stop();
            player.release();
            player = null;

            isWorking = false;
        }catch (Exception e) {
            e.printStackTrace();
        }
    }

    public int getMinBufferSize() {
        return playerBufferSize;
    }
}
