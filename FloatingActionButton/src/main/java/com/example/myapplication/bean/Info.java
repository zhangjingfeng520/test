package com.example.myapplication.bean;

import com.example.myapplication.R;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/10/30.
 */

public class Info implements Serializable {
    private double latitude;
    private double longitude;
    private int imgID;
    private String name;
    public static List<Info> infos=new ArrayList<Info>();

    static{
        infos.add(new Info(27.674897,109.168553, R.mipmap.hong,"贵州工程职业学院"));
        infos.add(new Info(27.675897,108.168553, R.mipmap.hong,"贵州工程职业学院"));
        infos.add(new Info(26.676897,108.168553, R.mipmap.hong,"贵州工程职业学院"));


    }

    public Info(int imgID, String name) {
        this.imgID = imgID;
        this.name = name;
    }

    public Info(double latitude, double longitude, int imgID, String name) {
        this.latitude = latitude;
        this.longitude = longitude;
        this.imgID = imgID;
        this.name = name;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public int getImgID() {
        return imgID;
    }

    public void setImgID(int imgID) {
        this.imgID = imgID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}
