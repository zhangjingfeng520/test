package com.example.myapplication.sensor;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

/**
 * Created by Administrator on 2017/10/28.
 */

public class MyOrientationListener implements SensorEventListener {
    private SensorManager sensorManager;
    private Context context;
    private Sensor sensor;

    private float lastX;

    public  MyOrientationListener(Context context){
        this.context=context;
    }

    public void start(){
        sensorManager=(SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
        if(sensorManager!=null){
            //获得方向传感器
           sensor= sensorManager.getDefaultSensor(Sensor.TYPE_ORIENTATION);
        }
        if(sensor!=null){
            sensorManager.registerListener(this,sensor,SensorManager.SENSOR_DELAY_FASTEST);
        }
    }

    public void stop(){
        if(sensorManager!=null) {
            sensorManager.unregisterListener(this);
        }
    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        if(sensorEvent.sensor.getType()==Sensor.TYPE_ORIENTATION){
            float x=sensorEvent.values[SensorManager.DATA_X];
            if(Math.abs(x-lastX)>1.0){
                if(onOrientationListener!=null){
                    onOrientationListener.onOrientationChanged(x);
                }
            }
            lastX=x;
        }

    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }

    public void setOnOrientationListener(OnOrientationListener onOrientationListener) {
        this.onOrientationListener = onOrientationListener;
    }

    private OnOrientationListener onOrientationListener;

    public interface OnOrientationListener{
        void onOrientationChanged(float x);
    }
}
