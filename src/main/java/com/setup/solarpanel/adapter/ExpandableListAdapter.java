package com.setup.solarpanel.adapter;

/**
 * Created by Mayank on 9/26/2015.
 */

import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.setup.solarpanel.HomeScreenActivity;
import com.setup.solarpanel.R;
import com.setup.solarpanel.customview.CalendarPickerView;
import com.setup.solarpanel.data.ReportsModel;
import com.setup.solarpanel.db.DatabaseHelper;
import com.setup.solarpanel.utils.CommonMethods;

import java.util.ArrayList;

public class ExpandableListAdapter extends BaseExpandableListAdapter {

    private Context _context;
    private ArrayList<ReportsModel> reportsList;
    private LayoutInflater l_Inflater;

    public ExpandableListAdapter(Context context, ArrayList<ReportsModel> list) {
        reportsList = list;
        _context = context;
        l_Inflater = LayoutInflater.from(context);
    }

    @Override
    public Object getChild(int groupPosition, int childPosititon) {
        return reportsList.get(groupPosition);
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public View getChildView(int groupPosition, final int childPosition,
                             boolean isLastChild, View convertView, ViewGroup parent) {

        ViewHolder holder = null;
        if (convertView == null) {
            convertView = l_Inflater.inflate(R.layout.list_item, null);
            holder = new ViewHolder();

            holder.tvLongitude = (TextView) convertView.findViewById(R.id.tvLongitude);
            holder.tvLatitude = (TextView) convertView.findViewById(R.id.tvLatitude);
            holder.tvDirectionCaptured = (TextView) convertView.findViewById(R.id.tvDirection);
            holder.tvAngleCaptured = (TextView) convertView.findViewById(R.id.tvAngle);
            holder.tvOptimalDirection = (TextView) convertView.findViewById(R.id.tvOptimalDirection);
            holder.tvOptimalAngle = (TextView) convertView.findViewById(R.id.tvOptimalAngle);
            holder.tvAverageLux = (TextView) convertView.findViewById(R.id.tvAverageLux);
            holder.btnDetails = (Button) convertView.findViewById(R.id.btn_details);

            convertView.setTag(holder);
        } else {

            holder = (ViewHolder) convertView.getTag();

        }
        holder.tvLongitude.setText(reportsList.get(groupPosition).getLongitude());
        holder.tvLatitude.setText(reportsList.get(groupPosition).getLatitude());
        holder.tvDirectionCaptured.setText(reportsList.get(groupPosition).getDirectionCaptured());
        holder.tvAngleCaptured.setText(reportsList.get(groupPosition).getAngleCaptured());
        holder.tvOptimalDirection.setText(reportsList.get(groupPosition).getOptimalDirection());
        holder.tvOptimalAngle.setText(reportsList.get(groupPosition).getOptimalAngle());
        holder.tvAverageLux.setText(reportsList.get(groupPosition).getAverageLux());
        holder.btnDetails.setTag(reportsList.get(groupPosition));
        holder.btnDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                DatabaseHelper databaseHelper = DatabaseHelper.getInstance(_context);
                String date = ((ReportsModel) v.getTag()).getDate();
                String startTime = ((ReportsModel) v.getTag()).getStartTime();
                String endTime = ((ReportsModel) v.getTag()).getEndTime();
                ArrayList<ReportsModel> reportsModelList = databaseHelper.getDetailReport(date, startTime, endTime);

                if(reportsModelList != null && reportsModelList.size() > 0){

                    DisplayMetrics displaymetrics = new DisplayMetrics();
                    ((HomeScreenActivity)_context).getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
                    int height = displaymetrics.heightPixels;
                    int width = displaymetrics.widthPixels;

                    LayoutInflater inflater = LayoutInflater.from(_context);
                    View view = inflater.inflate(R.layout.details_report_popup, null);
                    final PopupWindow detailsPopup = new PopupWindow(_context);
                    detailsPopup.setContentView(view);
                    detailsPopup.setHeight(height);
                    detailsPopup.setWidth(width);
                    detailsPopup.setFocusable(true);
                    detailsPopup.setTouchable(true);
                    detailsPopup.setOutsideTouchable(true);
                    detailsPopup.setBackgroundDrawable(new BitmapDrawable());
                    detailsPopup.showAtLocation(view, Gravity.CENTER, 0, 0);

                    ListView lvReportsDetail = (ListView) view.findViewById(R.id.lvDetailReport);
                    Button btnOk = (Button) view.findViewById(R.id.btn_ok);
                    btnOk.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            detailsPopup.dismiss();
                        }
                    });

                    ReportDetailAdapter reportDetailAdapter = new ReportDetailAdapter(_context, reportsModelList);

                    lvReportsDetail.setAdapter(reportDetailAdapter);

                }else
                    CommonMethods.showToast("No Data to Display", _context);

            }
        });

        return convertView;
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return 1;
    }

    @Override
    public Object getGroup(int groupPosition) {
        return reportsList.get(groupPosition);
    }

    @Override
    public int getGroupCount() {
        return reportsList.size();
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded,
                             View convertView, ViewGroup parent) {

        ViewHolder holder = null;
        if (convertView == null) {
            convertView = l_Inflater.inflate(R.layout.list_group, null);
            holder = new ViewHolder();

            holder.tvDate = (TextView) convertView.findViewById(R.id.tvHeaderDate);
            holder.tvStartTime = (TextView) convertView.findViewById(R.id.tvHeaderStartTime);
            holder.tvEndTime = (TextView) convertView.findViewById(R.id.tvHeaderEndTime);

            convertView.setTag(holder);
        } else {

            holder = (ViewHolder) convertView.getTag();

        }
        holder.tvDate.setText(reportsList.get(groupPosition).getDate());
        holder.tvStartTime.setText(reportsList.get(groupPosition).getStartTime());
        holder.tvEndTime.setText(reportsList.get(groupPosition).getEndTime());

        return convertView;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }

    static class ViewHolder {
        private TextView tvDate;
        private TextView tvStartTime;
        private TextView tvEndTime;
        private TextView tvLongitude;
        private TextView tvLatitude;
        private TextView tvDirectionCaptured;
        private TextView tvAngleCaptured;
        private TextView tvOptimalDirection;
        private TextView tvOptimalAngle;
        private TextView tvAverageLux;
        private Button btnDetails;
    }

}