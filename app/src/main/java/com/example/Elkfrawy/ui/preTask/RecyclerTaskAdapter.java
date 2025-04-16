package com.example.Elkfrawy.ui.preTask;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Paint;
import android.os.Build;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.ColorInt;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.core.content.ContextCompat;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.example.Elkfrawy.R;
import com.example.Elkfrawy.databinding.RecyclerTaskItemsBinding;
import com.example.Elkfrawy.model.Task;

import java.util.ArrayList;
import java.util.List;

public class RecyclerTaskAdapter extends ListAdapter<Task, RecyclerTaskAdapter.viewHolder> {
    private static final String TAG = "RecyclerTaskAdapter";
    Context c;
    onCheckedListener mListener;

    private static DiffUtil.ItemCallback<Task> diff = new DiffUtil.ItemCallback<Task>() {
        @Override
        public boolean areItemsTheSame(@NonNull Task oldItem, @NonNull Task newItem) {
            return oldItem.getId() == newItem.getId();
        }

        @Override
        public boolean areContentsTheSame(@NonNull Task oldItem, @NonNull Task newItem) {
            boolean result = (oldItem.getTitle().equals(newItem.getTitle()) && oldItem.getTime().equals(newItem.getTime()) && oldItem.getPriority().equals(newItem.getPriority()));
            return result;
        }
    };

    protected RecyclerTaskAdapter() {
        super(diff);
    }

    public class viewHolder extends RecyclerView.ViewHolder{
        RecyclerTaskItemsBinding binding;
        public viewHolder(@NonNull RecyclerTaskItemsBinding b) {
            super(b.getRoot());
            binding = b;
        }
    }

    @NonNull
    @Override
    public viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        return new viewHolder(RecyclerTaskItemsBinding.inflate(LayoutInflater.from(c), parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull viewHolder holder, int position) {

        Task t = getItem(position);

        holder.binding.tvTitle.setText(t.getTitle());
        holder.binding.tvTime.setText(t.getTime());
        holder.binding.tvPriority.setText(t.getPriority());
        holder.binding.tvPriority.setTextColor(ContextCompat.getColor(c ,t.getColor()));

        if (t.isChecked())
            textThroughLine(holder.binding.tvTitle);
        holder.binding.checker.setChecked(t.isChecked());

        holder.binding.checker.setOnCheckedChangeListener((bv, c) -> {
            if (c)
                textThroughLine(holder.binding.tvTitle);
            else
                holder.binding.tvTitle.setPaintFlags(0);

            mListener.onCheckedChange(t, c);
        });

        holder.binding.getRoot().setOnClickListener(v -> {
            mListener.onViewClicked(t);
        });

    }

    public void onChange(onCheckedListener listener){
        mListener = listener;
    }



    public interface onCheckedListener{
        void onCheckedChange(Task t, boolean isChecked);
        void onViewClicked(Task t);
    }

    public void textThroughLine(TextView tv){
        tv.setPaintFlags(tv.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
    }

    public Task getTask(int pos){
        return getItem(pos);
    }

    public void getContext(Context c){
        this.c = c;
    }
}
