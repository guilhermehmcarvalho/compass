package com.guilherme.compass.services;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

import com.guilherme.compass.interfaces.SensorChangedInterface;

/**
 * Provide resources to manage the compass sensor
 * Created by Guilherme on 15/01/2016.
 */
public class CompassService implements SensorEventListener {

    // device sensor manager
    private SensorManager mSensorManager;

    // Callback for onSensorChanged
    private SensorChangedInterface callback;

    /**
     * Provide resources to manage the compass sensor
     */
    public CompassService(Context context, SensorChangedInterface onSensorChangedCallback){
        callback = onSensorChangedCallback;

        // initialize the compass sensor
        mSensorManager = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (callback != null)
        {
            // get the angle rotated around the z-axis
            float degrees = Math.round(event.values[0]);
            callback.onSensorChanged(degrees);
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    /**
     * Get instance of the SensorManager
     */
    public SensorManager getmSensorManager() {
        return mSensorManager;
    }
}
