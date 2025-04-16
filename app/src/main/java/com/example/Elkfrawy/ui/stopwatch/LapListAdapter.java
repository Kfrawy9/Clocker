package com.example.Elkfrawy.ui.stopwatch;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.Elkfrawy.R;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;

public class LapListAdapter extends BaseAdapter {

    List<String> laps;
    Context c;

    public LapListAdapter(Context c) {
        laps = new ArrayList<>();
        this.c = c;
    }

    public void setList(List<String> laps) {
        this.laps = laps;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return laps.size();
    }

    @Override
    public String getItem(int position) {
        return laps.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (convertView == null)
            convertView = LayoutInflater.from(c).inflate(R.layout.lap_item, null, true);

        TextView t = convertView.findViewById(R.id.lap);
        t.setText(getItem(position));

        return convertView;
    }


}
