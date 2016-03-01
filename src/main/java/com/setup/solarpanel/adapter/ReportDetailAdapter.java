package com.setup.solarpanel.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.setup.solarpanel.R;
import com.setup.solarpanel.data.ReportsModel;

import java.util.ArrayList;

/**
 * Created by Mayank on 9/26/2015.
 */
public class ReportDetailAdapter extends BaseAdapter {

    private Context _context;
    private ArrayList<ReportsModel> reportsDetailList;
    private static LayoutInflater inflater=null;

    public ReportDetailAdapter(Context context, ArrayList<ReportsModel> list) {
        // TODO Auto-generated constructor stub
        reportsDetailList = list;
        _context = context;
        inflater = ( LayoutInflater )context.
                getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }
    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return reportsDetailList.size();
    }

    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    public class Holder
    {
        TextView tvCapturedAt;
        TextView tvLuxValue;
    }
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        Holder holder = null;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.details_report_items, null);
            holder = new Holder();

            holder.tvCapturedAt = (TextView) convertView.findViewById(R.id.tvCapturedAt);
            holder.tvLuxValue = (TextView) convertView.findViewById(R.id.tvLuxValue);

            convertView.setTag(holder);
        } else {

            holder = (Holder) convertView.getTag();

        }

        holder.tvCapturedAt.setText(reportsDetailList.get(position).getCapturedAt());
        holder.tvLuxValue.setText(reportsDetailList.get(position).getLuxValue());

        return convertView;
    }
}
