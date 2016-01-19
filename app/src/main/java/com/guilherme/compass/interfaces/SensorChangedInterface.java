package com.guilherme.compass.interfaces;

/**
 * Interface to throw a callback when changes in the compass sensor are detected
 * Created by Guilherme on 15/01/2016.
 */
public interface SensorChangedInterface {
    /**
     * Throw when changes in compass sensor are detected
     * @param degrees amount of degrees in rad from north
     */
    void onSensorChanged(float degrees);
}
