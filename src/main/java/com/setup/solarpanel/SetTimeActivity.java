package com.setup.solarpanel;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.setup.solarpanel.utils.Constants;

/**
 * Created by Mayank on 9/9/2015.
 */
public class SetTimeActivity extends Activity {

    private Spinner captureDurationSpinner;
    private Spinner captureIntervalSpinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.set_time_popup);

        captureDurationSpinner = (Spinner)findViewById(R.id.spinner_capture_duration);
        captureIntervalSpinner = (Spinner)findViewById(R.id.spinner_capture_interval);

        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
                R.layout.spinner_row, getResources().getStringArray(R.array.capture_duration));
        captureDurationSpinner.setAdapter(dataAdapter);

        captureDurationSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {

                ArrayAdapter<String> dataAdapter = null;
                int duration = Integer.parseInt(captureDurationSpinner.getSelectedItem().toString().substring(0, captureDurationSpinner.getSelectedItem().toString().indexOf(" ")));

                if(duration==3 || duration==6){

                   dataAdapter = new ArrayAdapter<>(SetTimeActivity.this,
                            R.layout.spinner_row, getResources().getStringArray(R.array.capture_interval_2));
                }else{
                   dataAdapter = new ArrayAdapter<>(SetTimeActivity.this,
                            R.layout.spinner_row, getResources().getStringArray(R.array.capture_interval_1));
                }

                captureIntervalSpinner.setAdapter(dataAdapter);
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {

            }
        });
    }


    public void measureLux(View view){

        int duration = Integer.parseInt(captureDurationSpinner.getSelectedItem().toString().substring(0, captureDurationSpinner.getSelectedItem().toString().indexOf(" ")));

        Constants.durationInHours = duration;

        Constants.interval = Integer.parseInt(captureIntervalSpinner.getSelectedItem().toString().substring(0, captureIntervalSpinner.getSelectedItem().toString().indexOf(" ")));

        setResult(RESULT_OK);

        finish();
    }
}
