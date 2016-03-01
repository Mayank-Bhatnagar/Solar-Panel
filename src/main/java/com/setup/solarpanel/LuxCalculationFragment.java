package com.setup.solarpanel;

import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.setup.solarpanel.db.DatabaseHelper;
import com.setup.solarpanel.utils.CommonMethods;
import com.setup.solarpanel.utils.Constants;

import java.util.concurrent.TimeUnit;

/**
 * Created by Mayank on 9/9/2015.
 */
public class LuxCalculationFragment extends Fragment {

    private Handler mHandler;
    private float totalLux;
    private int counter = 1;
    private ProgressBar progressBar;
    private TextView tvProgress;
    private boolean captureInitialLux = true;
    private int maxProgress = Constants.durationInHours * 60;

    private float averageCounter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.lux_calculation_fragment, container, false);

        TextView tvInterval = (TextView) view.findViewById(R.id.tv_interval);
        progressBar = (ProgressBar) view.findViewById(R.id.progressBar);
        progressBar.setMax(maxProgress);
        tvProgress = (TextView) view.findViewById(R.id.tv_progress);
        Button btnStop = (Button) view.findViewById(R.id.btnStop);

        btnStop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mHandler.removeCallbacks(mRunnable);
                Toast.makeText(getActivity(),
                        "Process Completed", Toast.LENGTH_LONG).show();

                showPopUp();

            }
        });

        if(String.valueOf(Constants.interval).length()==1)
            tvInterval.setText("00:0"+Constants.interval+":00");
        else
            tvInterval.setText("00:"+Constants.interval+":00");


        SensorManager sensorManager = (SensorManager) getActivity().getSystemService(Context.SENSOR_SERVICE);
        Sensor lightSensor = sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT);

        if (lightSensor == null) {
            Toast.makeText(getActivity(),
                    "No Light Sensor! quit-", Toast.LENGTH_LONG).show();

            System.exit(0);
        } else {
            float max = lightSensor.getMaximumRange();
            sensorManager.registerListener(lightSensorEventListener,
                    lightSensor, SensorManager.SENSOR_DELAY_NORMAL);
        }

        return view;
    }

    private Runnable mRunnable = new Runnable() {
        @Override
        public void run() {


                progressBar.setProgress(counter);

                double progress = Math.floor((100 * counter) / maxProgress );

                tvProgress.setText("Progress: " + (int)progress + "%");

                if(counter % Constants.interval == 0){

                    Constants.capturedAt = CommonMethods.getTime(0);

                    DatabaseHelper databaseHelper = DatabaseHelper.getInstance(getActivity());

                    databaseHelper.insertCapturedLux();

                    totalLux += Float.valueOf(Constants.lux);

                    averageCounter++;

                }

                if(counter < maxProgress)
                    mHandler.postDelayed(mRunnable, TimeUnit.MINUTES.toMillis(1));
                else {
                    mHandler.removeCallbacks(mRunnable);

                    showPopUp();

                    Toast.makeText(getActivity(),
                            "Process Completed", Toast.LENGTH_LONG).show();
                }

                counter++;

        }
    };

    public void startTimer() {
        mHandler = new Handler();
        mHandler.postDelayed(mRunnable, TimeUnit.MINUTES.toMillis(1));
    }

    SensorEventListener lightSensorEventListener = new SensorEventListener() {

        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) {
            // TODO Auto-generated method stub

        }

        @Override
        public void onSensorChanged(SensorEvent event) {
            // TODO Auto-generated method stub
            if (event.sensor.getType() == Sensor.TYPE_LIGHT) {
                float currentReading = event.values[0];
				Constants.lux = String.valueOf(currentReading);

                if(Constants.lux != null && !TextUtils.isEmpty(Constants.lux) && captureInitialLux) {

                    Constants.capturedAt = CommonMethods.getTime(0);

                    DatabaseHelper databaseHelper = DatabaseHelper.getInstance(getActivity());

                    databaseHelper.insertCapturedLux();

                    totalLux += Float.valueOf(Constants.lux);

                    averageCounter++;

                    startTimer();

                    captureInitialLux = false;

                }

            }
        }

    };

    private void showPopUp(){

        WindowManager wm = (WindowManager) getActivity().getSystemService(Context.WINDOW_SERVICE);
        final DisplayMetrics displayMetrics = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(displayMetrics);
        int height = displayMetrics.heightPixels;
        int width = displayMetrics.widthPixels;

        LayoutInflater inflater = LayoutInflater.from(getActivity());
        View view = inflater.inflate(R.layout.set_result_popup, null);
        TextView tvDate = (TextView) view.findViewById(R.id.tv_date);
        TextView tvLongitude = (TextView) view.findViewById(R.id.tv_longitude);
        TextView tvLatitude = (TextView) view.findViewById(R.id.tv_latitude);
        TextView tvDirection = (TextView) view.findViewById(R.id.tv_direction);
        TextView tvAngle = (TextView) view.findViewById(R.id.tv_angle);
        TextView tvLux = (TextView) view.findViewById(R.id.tv_lux);
        Button btnOk = (Button) view.findViewById(R.id.btn_ok);

        tvDate.setText(Constants.date);
        tvLongitude.setText(Constants.longitude);
        tvLatitude.setText(Constants.latitude);
        tvDirection.setText(Constants.currentDirection);
        tvAngle.setText(Constants.currentAngle);

        float averageLux = totalLux/averageCounter;

        tvLux.setText(""+ averageLux);

        Constants.averageLux = String.valueOf(averageLux);
        DatabaseHelper databaseHelper = DatabaseHelper.getInstance(getActivity());
        databaseHelper.insertAverageLux();

        final PopupWindow popupWindow = new PopupWindow(getActivity());
        popupWindow.setContentView(view);
        popupWindow.setHeight(height);
        popupWindow.setWidth(width);
        popupWindow.setFocusable(true);
        popupWindow.setTouchable(true);
        popupWindow.setOutsideTouchable(true);
        popupWindow.setBackgroundDrawable(new BitmapDrawable());
        popupWindow.showAtLocation(view, Gravity.CENTER, 0, 0);

        btnOk.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                popupWindow.dismiss();

                getActivity().finish();

                Constants.durationInHours = 0;
            }
        });
    }
}
