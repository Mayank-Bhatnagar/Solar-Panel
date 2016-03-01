package com.setup.solarpanel;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DigitalClock;
import android.widget.TextView;
import android.widget.Toast;

import com.setup.solarpanel.data.GlobalData;
import com.setup.solarpanel.db.DatabaseHelper;
import com.setup.solarpanel.impl.DataListener;
import com.setup.solarpanel.utils.CommonMethods;
import com.setup.solarpanel.utils.Constants;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Mayank on 9/2/2015.
 */
public class HomeScreenFragment extends Fragment implements DataListener {

    private TextView tvLongitude;
    private TextView tvLatitude;
    private TextView tvCurrentDirection;
    private TextView tvCurrentAngle;
    private TextView tvOptimalDirection;
    private TextView tvOptimalAngle;
    private View compassView = null;
    private DigitalClock digitalClock;

    private SensorEventListener sensorEventListener;
    private SensorManager sensorManager;
    private Sensor sensor;
    private int workingMode = 2;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.home_screen_fragment_layout, container, false);

        digitalClock = (DigitalClock) view.findViewById(R.id.text_clock);
        compassView = view.findViewById(R.id.compass);
        tvLongitude = (TextView) view.findViewById(R.id.tvLongitude);
        tvLatitude = (TextView) view.findViewById(R.id.tvLatitude);
        tvCurrentDirection = (TextView) view.findViewById(R.id.tvDirection);
        tvCurrentAngle = (TextView) view.findViewById(R.id.tvAngle);
        tvOptimalDirection = (TextView) view.findViewById(R.id.tvOptimalDirection);
        tvOptimalAngle = (TextView) view.findViewById(R.id.tvOptimalAngle);
        Button btnSetTime = (Button) view.findViewById(R.id.btn_set_time);
        btnSetTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(tvLatitude.getText().toString().equalsIgnoreCase("--------"))
                    CommonMethods.showToast("Couldn't proceed as Location is not found", getActivity().getApplicationContext());
                else {
                    Intent intent = new Intent(getActivity().getApplicationContext(), SetTimeActivity.class);
                    startActivityForResult(intent, 1);
                }
            }
        });

        return view;
    }


    private void setUpAngleData() {

        sensorManager = (SensorManager) getActivity().getSystemService(Context.SENSOR_SERVICE/*"sensor"*/);
        sensor = sensorManager.getDefaultSensor(1);
        sensorEventListener = new SensorEventListener() {

            @Override
            public void onSensorChanged(SensorEvent sensorevent) {
                // TODO Auto-generated method stub

                if (sensorevent.sensor.getType() == 1) {
                    float f1 = sensorevent.values[0] / 9.8F;
                    float f2 = sensorevent.values[1] / 9.8F;
                    float f3 = sensorevent.values[2] / 9.8F;
                    float f = f1;
                    if (f1 == 0.0F) {
                        f = 0.01F;
                    }
                    f1 = f2;
                    if (f2 == 0.0F) {
                        f1 = 0.01F;
                    }
                    f2 = f3;
                    if (f3 == 0.0F) {
                        f2 = 0.01F;
                    }
                    if (workingMode == 1) {
                        int i = (int) ((180D * Math.atan(f1 / f)) / 3.1415926535897931D);
                        int k = Math.abs(i);
                        if (Math.abs(f2) < 1.0F) {
                            tvCurrentAngle.setText(Integer.toString(k));
                            return;
                        } else {
                            tvCurrentAngle.setText("---");
                            return;
                        }
                    }
                    if (workingMode == 2) {
                        int i1 = (int) (double) (int) ((180D * Math.atan(f1
                                / f2)) / 3.1415926535897931D);
                        int l = i1 - 0;
                        int j = l;
                        if (l > 90) {
                            j = 90;
                        }
                        if (Math.abs(f) < 0.9F) {

                            String angle = Integer.toString(Math.abs(j));


                            if (!tvOptimalAngle.getText().toString().equalsIgnoreCase("--------")) {

                                int optimalAngle = Integer.parseInt(tvOptimalAngle.getText().toString().substring(0, tvOptimalAngle.getText().toString().indexOf("°")));

                                if (Integer.parseInt(angle) > optimalAngle - 3 && Integer.parseInt(angle) < optimalAngle + 3)
                                    tvCurrentAngle.setBackgroundColor(Color.GREEN);
                                else {

                                    if (isAdded()) {

                                        int sdk = android.os.Build.VERSION.SDK_INT;
                                        if (sdk < android.os.Build.VERSION_CODES.JELLY_BEAN) {
                                            tvCurrentAngle.setBackgroundDrawable(getResources().getDrawable(R.drawable.value_bg));
                                        } else {
                                            tvCurrentAngle.setBackground(getResources().getDrawable(R.drawable.value_bg));
                                        }
                                    }
                                }
                            }


                            tvCurrentAngle.setText(Integer.toString(Math.abs(j)));
                            return;
                        } else {
                            tvCurrentAngle.setText("---");
                            return;
                        }
                    }
                }
            }

            @Override
            public void onAccuracyChanged(Sensor sensor, int accuracy) {
                // TODO Auto-generated method stub

            }
        };
        sensorManager.registerListener(sensorEventListener, sensor, 3);
    }


    @Override
    public void onSensorChanged(SensorEvent evt) {
        if (evt.sensor.getType() == Sensor.TYPE_ACCELEROMETER || evt.sensor.getType() == Sensor.TYPE_MAGNETIC_FIELD) {
            // Tell the compass to update it's graphics
            if (compassView != null) compassView.postInvalidate();
        }

        // Update the direction text
        updateUI(GlobalData.getBearing());
    }

    @Override
    public void updateUI(float bearing) {
        int range = (int) (bearing / (360f / 16f));
        String dirTxt = "";
        if (range == 15 || range == 0) dirTxt = "N";
        else if (range == 1 || range == 2) dirTxt = "NE";
        else if (range == 3 || range == 4) dirTxt = "E";
        else if (range == 5 || range == 6) dirTxt = "SE";
        else if (range == 7 || range == 8) dirTxt = "S";
        else if (range == 9 || range == 10) dirTxt = "SW";
        else if (range == 11 || range == 12) dirTxt = "W";
        else if (range == 13 || range == 14) dirTxt = "NW";


        String duration = "" + ((int) bearing);

        if (Integer.parseInt(duration) > 354 || Integer.parseInt(duration) < 6)
            tvCurrentDirection.setBackgroundColor(Color.GREEN);
        else {

            if (isAdded()) {
                int sdk = android.os.Build.VERSION.SDK_INT;
                if (sdk < android.os.Build.VERSION_CODES.JELLY_BEAN) {
                    tvCurrentDirection.setBackgroundDrawable(getResources().getDrawable(R.drawable.value_bg));
                } else {
                    tvCurrentDirection.setBackground(getResources().getDrawable(R.drawable.value_bg));
                }
            }
        }

        tvCurrentDirection.setText("" + ((int) bearing) + ((char) 176) + " " + dirTxt);
    }


    @Override
    public void onLocationChanged(Location location) {

        if (location != null) {
            tvLatitude.setText(String.valueOf(location.getLatitude()));
            tvLongitude.setText(String.valueOf(location.getLongitude()));
            tvOptimalAngle.setText(Math.round((float) location.getLatitude()) + "°");
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

    }

    @Override
    public void onResume() {
        super.onResume();

        setUpAngleData();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == getActivity().RESULT_OK) {
            captureLux();
        }
    }

    public void captureLux() {
        if (Constants.durationInHours != 0) {

            DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
            //get current date time with Date()
            Date date = new Date();
            System.out.println(dateFormat.format(date));

            Constants.date = dateFormat.format(date);
            Constants.startTime = CommonMethods.getTime(0);
            Constants.endTime = CommonMethods.getTime(Constants.durationInHours);

            Constants.longitude = tvLongitude.getText().toString();
            Constants.latitude = tvLatitude.getText().toString();
            Constants.currentDirection = tvCurrentDirection.getText().toString();
            Constants.currentAngle = tvCurrentAngle.getText().toString();
            Constants.optimalDirection = tvOptimalDirection.getText().toString();
            Constants.optimalAngle = tvOptimalAngle.getText().toString();

            try {

                DatabaseHelper databaseHelper = DatabaseHelper.getInstance(getActivity());
                databaseHelper.insertCompassData();

            } catch (Exception e) {
                e.printStackTrace();
            }

            FragmentManager fragmentManager = getActivity()
                    .getSupportFragmentManager();
            FragmentTransaction transaction = fragmentManager
                    .beginTransaction();
            LuxCalculationFragment luxCalculationFragment = new LuxCalculationFragment();
            // transaction.addToBackStack(null);
            transaction.replace(R.id.content_frame, luxCalculationFragment);
            transaction.commit();

        }
    }

}
