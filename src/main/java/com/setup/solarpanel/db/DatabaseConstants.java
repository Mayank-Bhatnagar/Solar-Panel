package com.setup.solarpanel.db;

/**
 * Created by Mayank on 9/9/2015.
 */
public class DatabaseConstants {

    public static class CompassData {
        public static final String TABLE = "COMPASS_DATA";
        public static final String Date = "DATE";
        public static final String StartTime = "START_TIME";
        public static final String EndTime = "END_TIME";
        public static final String Interval = "INTERVAL";
        public static final String Longitude = "LONGITUDE";
        public static final String Latitude = "LATITUDE";
        public static final String CurrentDirection = "CURRENT_DIRECTION";
        public static final String CurrentAngle = "CURRENT_ANGLE";
        public static final String OptimalDirection = "OPTIMAL_DIRECTION";
        public static final String OptimalAngle = "OPTIMAL_ANGLE";
        public static final String AverageLux = "AVERAGE_LUX";

    }

    public static class CapturedLux {
        public static final String TABLE = "CAPTURED_LUX";
        public static final String Date = "DATE";
        public static final String StartTime = "START_TIME";
        public static final String EndTime = "END_TIME";
        public static final String LuxValue = "LUX_VALUE";
        public static final String CapturedAt = "CAPTURED_AT";

    }
}
