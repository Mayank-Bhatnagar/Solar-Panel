package com.setup.solarpanel;

import android.graphics.drawable.BitmapDrawable;
import android.hardware.SensorEvent;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.PopupWindow;

import com.setup.solarpanel.adapter.ExpandableListAdapter;
import com.setup.solarpanel.customview.CalendarNumbersView;
import com.setup.solarpanel.customview.CalendarPickerView;
import com.setup.solarpanel.data.GlobalData;
import com.setup.solarpanel.data.ReportsModel;
import com.setup.solarpanel.db.DatabaseHelper;
import com.setup.solarpanel.impl.DataListener;
import com.setup.solarpanel.utils.CommonMethods;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

/**
 * Created by Mayank on 9/5/2015.
 */
public class ReportsFragment extends Fragment implements DataListener {

    private Button btnDate;
    private String btn_date_str;
    private final String[] mMonths = {"01", "02", "03", "04", "05",
            "06", "07", "08", "09", "10", "11", "12"};
    private PopupWindow calendarPopup;
    private ExpandableListAdapter listAdapter;
    private ExpandableListView expListView;
    private int prev = -1;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.reports_fragment, container, false);

        Calendar rightNow = Calendar.getInstance();
        int date = rightNow.get(Calendar.DATE); // zero based
        int month = rightNow.get(Calendar.MONTH); // zero based
        int year = rightNow.get(Calendar.YEAR);

        btnDate = (Button) view.findViewById(R.id.dateselector);
        btnDate.setOnClickListener(onButtonClickListener);
        btn_date_str = date + "-" + mMonths[month] + "-" + year;
        btnDate.setText(btn_date_str);

        expListView = (ExpandableListView) view.findViewById(R.id.lvExp);

        setReportsUI(btn_date_str);

        // Listview Group click listener
        expListView.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {

            @Override
            public boolean onGroupClick(ExpandableListView parent, View v,
                                        int groupPosition, long id) {
                return false;
            }
        });

        // Listview Group expanded listener
        expListView.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {

            @Override
            public void onGroupExpand(int groupPosition) {
                /*
				 * Toast.makeText(getApplicationContext(),
				 * listDataHeader.get(groupPosition) + " Expanded",
				 * Toast.LENGTH_SHORT).show();
				 */

                if (prev != -1 && prev != groupPosition) {

                    expListView.collapseGroup(prev);

                }
                prev = groupPosition;

            }
        });

        // Listview Group collasped listener
        expListView.setOnGroupCollapseListener(new ExpandableListView.OnGroupCollapseListener() {

            @Override
            public void onGroupCollapse(int groupPosition) {
				/*
				 * Toast.makeText(getApplicationContext(),
				 * listDataHeader.get(groupPosition) + " Collapsed",
				 * Toast.LENGTH_SHORT).show();
				 */

            }
        });

        // Listview on child click listener
        expListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {

            @Override
            public boolean onChildClick(ExpandableListView parent, View v,
                                        int groupPosition, int childPosition, long id) {
                // TODO Auto-generated method stub

                return false;
            }
        });

        return view;
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

    private View.OnClickListener onButtonClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {

            DisplayMetrics displaymetrics = new DisplayMetrics();
            getActivity().getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
            int height = displaymetrics.heightPixels;
            int width = displaymetrics.widthPixels;

            calendarPopup = new PopupWindow(getActivity());
            CalendarPickerView calendarView = new CalendarPickerView(getActivity());
            calendarView.setListener(dateSelectionListener);
            calendarPopup.setContentView(calendarView);
            calendarPopup.setHeight(height);
            calendarPopup.setWidth(width);
            calendarPopup.setFocusable(true);
            calendarPopup.setTouchable(true);
            calendarPopup.setOutsideTouchable(true);
            calendarPopup.setBackgroundDrawable(new BitmapDrawable());
            calendarPopup.showAtLocation(calendarView, Gravity.CENTER, 0, 0);
        }

    };

    private CalendarNumbersView.DateSelectionListener dateSelectionListener = new CalendarNumbersView.DateSelectionListener() {
        @Override
        public void onDateSelected(Calendar selectedDate) {
            if (calendarPopup.isShowing()) {
                calendarPopup.getContentView().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        calendarPopup.dismiss();
                    }
                }, 500);//For clarity, we close the popup not immediately.
            }
            SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
            btnDate.setText(formatter.format(selectedDate.getTime()));

            setReportsUI(formatter.format(selectedDate.getTime()));

        }
    };


    private void setReportsUI(String date){
        DatabaseHelper databaseHelper = DatabaseHelper.getInstance(getActivity());
        ArrayList<ReportsModel> reportsModelList = databaseHelper.getDailyReport(date);

        if(reportsModelList != null/* && reportsModelList.size() > 0*/){

            expListView.setAdapter(new ExpandableListAdapter(
                    getActivity(), reportsModelList));
        }else
            CommonMethods.showToast("No Data to Display", getActivity());
    }

}
