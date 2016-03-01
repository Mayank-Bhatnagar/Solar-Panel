package com.setup.solarpanel;

import android.hardware.SensorEvent;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.setup.solarpanel.data.GlobalData;
import com.setup.solarpanel.impl.DataListener;
import com.setup.solarpanel.utils.CommonMethods;

/**
 * Created by Mayank on 9/5/2015.
 */
public class ExportDBFragment extends Fragment implements DataListener {

    private View compassView = null;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

//        Toast.makeText(getActivity(), "ExportDBFragment", Toast.LENGTH_SHORT).show();

        CommonMethods.copyDatabaseToSDCard(getActivity());

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.export_db_fragment, container, false);
    }

    @Override
    public void onSensorChanged(SensorEvent evt) {

    }

    @Override
    public void updateUI(float bearing) {

    }



    @Override
    public void onLocationChanged(Location location) {

    }
}
