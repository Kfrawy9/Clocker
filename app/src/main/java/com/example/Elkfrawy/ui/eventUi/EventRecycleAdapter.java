package com.example.Elkfrawy.ui.eventUi;

import static com.example.Elkfrawy.ui.eventUi.AddEventFragment.EVENT_EXTRA_DESC;
import static com.example.Elkfrawy.ui.eventUi.AddEventFragment.EVENT_EXTRA_TITLE;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.Elkfrawy.R;
import com.example.Elkfrawy.broadcasts.EventBroadCast;
import com.example.Elkfrawy.databinding.RecyclerEventItemsBinding;
import com.example.Elkfrawy.model.Event;
import com.example.Elkfrawy.ui.MainActivity;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class EventRecycleAdapter extends RecyclerView.Adapter<EventRecycleAdapter.viewHolder> {

    private static final String TAG = "ReAlarmWorker";
    Context c;
    List<Event> events;
    onClickListener mListener;

    public EventRecycleAdapter(Context c){
        this.c = c;
        events = new ArrayList<>();
    }

    public void getList(List<Event> events){
        this.events = events;
        notifyDataSetChanged();
    }

    public class viewHolder extends RecyclerView.ViewHolder{
        RecyclerEventItemsBinding binding;
        public viewHolder(@NonNull RecyclerEventItemsBinding b) {
            super(b.getRoot());
            binding = b;
        }
    }

    @NonNull
    @Override
    public viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new viewHolder(RecyclerEventItemsBinding.inflate(LayoutInflater.from(c), parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull viewHolder holder, int position) {

        Event event = events.get(position);

        holder.binding.dateTime.setText(event.getDateTime());
        holder.binding.eventTitle.setText(event.getTitle());
        holder.binding.eventBody.setText(event.getDescription());

        if (event.isReminder())
            holder.binding.ringImg.setImageResource(R.drawable.ic_notification_alert);
        else
            holder.binding.ringImg.setImageResource(R.drawable.ic_no_ring);

        holder.binding.ringImg.setOnClickListener(v -> {

            boolean onOff = !event.isReminder();

            if (onOff){
                if (createAlarm(event.getTitle(), event.getDescription(), event.getDateTime(), event.hashCode()))
                    holder.binding.ringImg.setImageResource(R.drawable.ic_notification_alert);
                else
                    onOff = false;
            }
            else {
                holder.binding.ringImg.setImageResource(R.drawable.ic_no_ring);
                removeAlarm(event.hashCode());
            }

            event.setReminder(onOff);
            mListener.onBellClickListener(event);
        });
        holder.binding.getRoot().setOnClickListener(v -> mListener.onViewClickListener(event, position));

    }

    public Event getEvent(int pos){
        return events.get(pos);
    }

    @Override
    public int getItemCount() {
        return events.size();
    }

    public void onChangeClickListener(onClickListener listener){
        mListener = listener;
    }

    public interface onClickListener{
        void onBellClickListener(Event event);
        void onViewClickListener(Event event, int position);
    }


    public Calendar formatDate(String s){
        Calendar c = Calendar.getInstance();
        SimpleDateFormat format = new SimpleDateFormat("MMM dd, yyyy hh:mm:ss a");
        try {
            Date d = format.parse(s);
            Log.d(TAG, "formatDate: date: " + d.toString());
            c.setTime(d);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return c;
    }

    public boolean createAlarm(String title, String desc, String date, int requestCode) {

        Calendar cal = formatDate(date);

        if (!cal.before(Calendar.getInstance())){

            Intent broadcast = new Intent(c, EventBroadCast.class);
            broadcast.putExtra(EVENT_EXTRA_TITLE, title);
            broadcast.putExtra(EVENT_EXTRA_DESC, desc);
            PendingIntent pi = PendingIntent.getBroadcast(c, requestCode, broadcast, PendingIntent.FLAG_UPDATE_CURRENT);
            AlarmManager manager = (AlarmManager) c.getSystemService(Context.ALARM_SERVICE);
            manager.setExact(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(), pi);

            return true;
        }
        else {
            Toast.makeText(c, "The date passed either edit it or remove it", Toast.LENGTH_SHORT).show();
            return false;
        }
    }

    public void removeAlarm(int id){
        Intent broadcast = new Intent(c, EventBroadCast.class);

        PendingIntent pi = PendingIntent.getBroadcast(c, id, broadcast, PendingIntent.FLAG_CANCEL_CURRENT);
        AlarmManager manager = (AlarmManager) c.getSystemService(Context.ALARM_SERVICE);
        manager.cancel(pi);
    }

}
