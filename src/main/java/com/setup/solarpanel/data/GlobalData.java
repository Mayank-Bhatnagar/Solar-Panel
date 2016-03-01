package com.setup.solarpanel.data;

/**
 * Abstract class which should be used to set global data.
 *
 * @author Mayank Bhatnagar (mayank_bhatnagar@live.in)
 * @version 1.0
 */
public class GlobalData {

    private static final Object lock = new Object();
    private static float bearing = 0;

    /*private static GlobalData instance = null;
    private GlobalData() {
        // Exists only to defeat instantiation.
    }

    public static synchronized GlobalData getInstance(){
        if(instance == null){
            instance = new GlobalData();
        }
        return instance;
    }*/

    /**
     * Set the bearing.
     *
     * @param bearing
     *            int representing the current bearing.
     */
    public static void setBearing(float bearing) {
        synchronized (lock) {
            GlobalData.bearing = bearing;
        }
    }

    /**
     * Get the bearing.
     *
     * @return int representing the bearing.
     */
    public static float getBearing() {
        synchronized (lock) {
            return bearing;
        }
    }
}
