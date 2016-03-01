package com.setup.solarpanel.impl;

import android.hardware.SensorEvent;
import android.location.Location;

import com.setup.solarpanel.data.GlobalData;

/**
 * Created by Mayank on 9/5/2015.
 */
public interface DataListener {

    public void onSensorChanged(SensorEvent evt);

    public void onLocationChanged(Location location);

    public void updateUI(float bearing);
}
