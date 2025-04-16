package com.example.Elkfrawy.ui.preTask;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowAnimationFrameStats;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.BinderThread;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.Elkfrawy.R;
import com.example.Elkfrawy.data.SpinnerData;
import com.example.Elkfrawy.databinding.SpinnerItemsBinding;
import com.example.Elkfrawy.model.SpinnerItems;

import java.util.List;

public class SpinnerAdapter extends ArrayAdapter {
    private static final String TAG = "SpinnerAdapter";
    Context c;
    List<SpinnerItems> data;
    int res;
    LayoutInflater inflater;
    SpinnerItemsBinding binding;

    public SpinnerAdapter(@NonNull Context context, int resource, @NonNull List<SpinnerItems> objects) {
        super(context, resource, objects);
        data = objects;
        c = context;
        res = resource;
        inflater = LayoutInflater.from(c);
    }


    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        return setupView(convertView, position);
    }

    @Override
    public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        return setupView(convertView, position);
    }

    public View setupView(View convertView, int position){

        if (convertView == null)
            convertView= inflater.inflate(res, null, true);

        TextView t = convertView.findViewById(R.id.colorText);
        View c = convertView.findViewById(R.id.pickedColor);

        t.setText(data.get(position).getPriority());
        c.setBackgroundResource(data.get(position).getColor());

        return convertView;

    }
    
    
}
