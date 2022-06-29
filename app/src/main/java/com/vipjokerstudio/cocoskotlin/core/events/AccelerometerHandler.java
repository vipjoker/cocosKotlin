package com.vipjokerstudio.cocoskotlin.core.events;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Oleh Makhobei  on 06.06.20.
 */
public class AccelerometerHandler {
    private static AccelerometerHandler instance = new AccelerometerHandler();
    private List<AccelerationListener> listeners = new ArrayList<>();
    public static AccelerometerHandler getInstance(){
        return instance;
    }
    private AccelerometerHandler(){

    }


    public void  addListener(AccelerationListener listener){
        listeners.add(listener);
    }

    public void removeListener(AccelerationListener listener){
        listeners.remove(listener);
    }

    public void proceedAcceleration(SensorEvent event){
        Sensor mySensor = event.sensor;

        if (mySensor.getType() == Sensor.TYPE_ACCELEROMETER) {

            float x = event.values[0];
            float y = event.values[1];
            float z = event.values[2];
            for (AccelerationListener listener : listeners) {
                listener.onAcceleration(x,y,z);
            }
        }
    }


}
