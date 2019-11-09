package com.sozolab.sumon;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import java.util.ArrayList;

public class ActivityListAdapter extends BaseAdapter {

    private ArrayList<Activity> listData;
    private LayoutInflater layoutInflater;

    public ActivityListAdapter(Context aContext, ArrayList<Activity> listData) {
        this.listData = listData;
        layoutInflater = LayoutInflater.from(aContext);
    }

    @Override
    public int getCount() {
        return listData.size();
    }

    @Override
    public Object getItem(int position) {
        return listData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public View getView(int position, View v, ViewGroup vg) {
        ViewHolder holder;
        if (v == null) {
            v = layoutInflater.inflate(R.layout.list_item, null);
            holder = new ViewHolder();
            holder.startTime = (TextView) v.findViewById(R.id.startTime);
            holder.stopTime = (TextView) v.findViewById(R.id.stopTime);
            holder.activity = (TextView) v.findViewById(R.id.listItemActivity);
            holder.duration = (TextView) v.findViewById(R.id.totalTime);
            v.setTag(holder);
        } else {
            holder = (ViewHolder) v.getTag();
        }
        holder.startTime.setText(listData.get(position).getStartTime());
        holder.stopTime.setText(listData.get(position).getStopTime());
        holder.activity.setText(listData.get(position).getActivityName());
        holder.duration.setText(listData.get(position).getDuration());
        return v;
    }
    static class ViewHolder {
        TextView startTime;
        TextView stopTime;
        TextView activity;
        TextView duration;
    }
}
