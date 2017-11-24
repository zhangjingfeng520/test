package com.example.myapplication.bean;

/**
 * Created by Administrator on 2017/11/2.
 */

public class Msg {
    public static final int TYPE_RECEIVED=0;
    public static final int TYPE_SEND=1;
    private String content;
    private int type;

    public String getContent() {
        return content;
    }

    public int getType() {
        return type;
    }

    public Msg(String content, int type) {
        this.content = content;
        this.type = type;
    }
}
