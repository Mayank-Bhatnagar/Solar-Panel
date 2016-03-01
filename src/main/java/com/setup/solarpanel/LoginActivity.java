package com.setup.solarpanel;

import android.content.Context;
import android.content.res.Resources;
import android.hardware.SensorEvent;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.animation.RotateAnimation;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.location.LocationServices;
import com.setup.solarpanel.db.DatabaseHelper;
import com.setup.solarpanel.impl.DataListener;
import com.setup.solarpanel.utils.CommonMethods;

import java.text.DateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by Mayank on 9/4/2015.
 */
public class LoginActivity extends BaseActivity implements DataListener {

    private EditText etUserName;
    private EditText etPassword;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_layout);

        try {

            DatabaseHelper databaseHelper = DatabaseHelper.getInstance(this);
            databaseHelper.createDataBase();

        }catch (Exception e){
            e.printStackTrace();
        }


        startUpdatesButtonHandler();

        etUserName = (EditText) findViewById(R.id.etUserName);
        etPassword = (EditText) findViewById(R.id.etPassword);

    }

    public void onSubmitClicked(View view){

        if(TextUtils.isEmpty(etUserName.getText().toString()))
            CommonMethods.showToast("Please enter Username", this);
        else if(TextUtils.isEmpty(etPassword.getText().toString()))
            CommonMethods.showToast("Please enter Password", this);
        else {
            if(!etUserName.getText().toString().equalsIgnoreCase("Admin"))
                CommonMethods.showToast("Either Username or Password is Invalid", this);
            else if (!etPassword.getText().toString().equals("Admin@2050"))
                CommonMethods.showToast("Either Username or Password is Invalid", this);
            else{
                CommonMethods.switchToNextActivity(HomeScreenActivity.class, this);
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        stopUpdatesButtonHandler();
    }


    @Override
    public void onLocationChanged(Location location) {
        super.onLocationChanged(location);


       /* final Resources res = this.getResources();
        final int id = Resources.getSystem().getIdentifier(
                "config_ntpServer", "string","android");
        final String defaultServer = res.getString(id);*/

        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {

                SntpClient client = new SntpClient();
                long time;
                long newTime;

                String dateFromNtpServer = "";
                if (client.requestTime("0.us.pool.ntp.org", 30000)) {
                    time = client.getNtpTime();
                    newTime = time;
                    Log.d("shetty", newTime + "....newTime");

                    Calendar calendar = Calendar.getInstance();
                    try {

                        calendar.setTimeInMillis(time);



                        calendar.getTime();


                /*GMTtoEST gmttoest = new GMTtoEST();
                dateFromNtpServer = gmttoest
                        .ReturnMeEst(calendar.getTime());

                dateFromNtpServer = dateFromNtpServer + "  EST";*/


                    } catch (Exception e) {
                        // TODO: handle exception
                        dateFromNtpServer = "No Response from NTP";
                    }

                }


            }
        });
        thread.start();

        Toast.makeText(this, "location: " + LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient).getTime(), Toast.LENGTH_SHORT).show();

        System.out.println("location: " + LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient).getTime());
    }


    @Override
    public void onSensorChanged(SensorEvent evt){

    }

    @Override
    public void updateUI(float bearing){

    }
}
