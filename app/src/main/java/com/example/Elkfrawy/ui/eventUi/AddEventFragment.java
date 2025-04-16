package com.example.Elkfrawy.ui.eventUi;

import static com.example.Elkfrawy.ui.eventUi.EventFragment.EDIT_EVENT_KEY;
import static com.example.Elkfrawy.ui.eventUi.EventFragment.EDIT_EVENT_VALUE;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.Elkfrawy.R;
import com.example.Elkfrawy.broadcasts.EventBroadCast;
import com.example.Elkfrawy.databinding.FragmentAddEventBinding;
import com.example.Elkfrawy.model.Event;
import com.example.Elkfrawy.ui.ClockViewModel;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.timepicker.MaterialTimePicker;
import com.google.android.material.timepicker.TimeFormat;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class AddEventFragment extends DialogFragment {
    private static final String TAG = "ALERT";
    public static final String EVENT_EXTRA_TITLE = "eventTitle";
    public static final String EVENT_EXTRA_DESC = "eventTime";

    ClockViewModel vm;
    FragmentAddEventBinding binding;
    String time, date;
    EventRecycleAdapter adapter;
    int state;


    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        vm = new ViewModelProvider(requireParentFragment()).get(ClockViewModel.class);
        vm.cal = Calendar.getInstance();
        vm.cal.set(Calendar.SECOND, 0);
        adapter = new EventRecycleAdapter(requireContext());
        state = getArguments().getInt(EDIT_EVENT_KEY);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentAddEventBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Log.d(TAG, "onViewCreated: " + state);
        if (state == EDIT_EVENT_VALUE){
            putData(vm.getEvent());
        }



        loadDate(vm.cal);

        binding.saveEvent.setOnClickListener(v -> {

            String title = binding.addEventTitle.getText().toString();
            String desc = binding.desc.getText().toString();
            String fullDate = date + " " + time;

            if (title.trim().length() != 0) {

                if (!vm.cal.before(Calendar.getInstance())) {

                   if (state == EDIT_EVENT_VALUE){
                       Event e = vm.getEvent();
                       adapter.removeAlarm(e.hashCode());
                       e.setTitle(title);
                       e.setDescription(desc);
                       e.setDateTime(fullDate);
                       e.setReminder(vm.reminderState);

                       vm.update(e);
                       createAlarm(title, desc, e.hashCode());
                   }
                   else {
                       Event newEvent = new Event(title, desc, fullDate, vm.reminderState);
                       vm.insert(newEvent);

                       createAlarm(title, desc, newEvent.hashCode());
                   }

                    requireActivity().onBackPressed();
                } else
                    Toast.makeText(requireContext(), "The date passed", Toast.LENGTH_LONG).show();
            } else
                Toast.makeText(requireContext(), "Add a title", Toast.LENGTH_LONG).show();
        });

        binding.reminder.setOnClickListener(v -> {
            vm.reminderState = !vm.reminderState;

            if (vm.reminderState)
                binding.reminder.setImageResource(R.drawable.ic_notification_alert);
            else
                binding.reminder.setImageResource(R.drawable.ic_no_ring);

        });

        binding.timeEventPicker.setOnClickListener(v -> showTimeDialog());
        binding.dateEventPicker.setOnClickListener(v -> showDateDialog());

    }

    public void showTimeDialog() {

        MaterialTimePicker.Builder builder = new MaterialTimePicker.Builder()
                .setHour(vm.cal.get(Calendar.HOUR_OF_DAY))
                .setMinute(vm.cal.get(Calendar.MINUTE))
                .setTimeFormat(TimeFormat.CLOCK_12H)
                .setTitleText("Add Time")
                .setInputMode(MaterialTimePicker.INPUT_MODE_CLOCK)
                .setPositiveButtonText("Save")
                .setNegativeButtonText("Cancel");

        MaterialTimePicker picker = builder.build();
        picker.show(getChildFragmentManager(), "eventTime");

        picker.addOnPositiveButtonClickListener(v -> {

            int h = picker.getHour();
            int m = picker.getMinute();

            vm.cal.set(Calendar.HOUR_OF_DAY, h);
            vm.cal.set(Calendar.MINUTE, m);
            vm.cal.set(Calendar.SECOND, 0);

            time = DateFormat.getTimeInstance().format(vm.cal.getTime());
            binding.timeEventPicker.setText(time);

        });
    }

    public void showDateDialog() {

        MaterialDatePicker.Builder<Long> builder = MaterialDatePicker.Builder.datePicker()
                .setInputMode(MaterialDatePicker.INPUT_MODE_CALENDAR)
                .setTitleText("Date")
                .setSelection(vm.cal.getTimeInMillis());

        MaterialDatePicker<Long> datePicker = builder.build();
        datePicker.show(getChildFragmentManager(), "eventDate");

        datePicker.addOnPositiveButtonClickListener(s -> {

            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(s);

            vm.cal.set(Calendar.YEAR, calendar.get(Calendar.YEAR));
            vm.cal.set(Calendar.MONTH, calendar.get(Calendar.MONTH));
            vm.cal.set(Calendar.DAY_OF_MONTH, calendar.get(Calendar.DAY_OF_MONTH));

            date = DateFormat.getDateInstance().format(vm.cal.getTime());
            binding.dateEventPicker.setText(date);
        });
    }

    public void createAlarm(String title, String time, int requestCode) {

        if (vm.reminderState){
            Intent broadcast = new Intent(requireContext(), EventBroadCast.class);
            broadcast.putExtra(EVENT_EXTRA_TITLE, title);
            broadcast.putExtra(EVENT_EXTRA_DESC, time);

            PendingIntent pi = PendingIntent.getBroadcast(requireContext(), requestCode, broadcast, PendingIntent.FLAG_CANCEL_CURRENT);

            AlarmManager manager = (AlarmManager) requireActivity().getSystemService(Context.ALARM_SERVICE);
            manager.setExact(AlarmManager.RTC_WAKEUP, vm.cal.getTimeInMillis(), pi);
        }

    }

    public void parseDate(String fd) {

        @SuppressLint("SimpleDateFormat") SimpleDateFormat format = new SimpleDateFormat("MMM dd, yyyy hh:mm:ss a");
        try {
            Date d = format.parse(fd);
            vm.cal.setTime(d);
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    public void loadDate(Calendar c) {

        time = DateFormat.getTimeInstance().format(c.getTime());
        binding.timeEventPicker.setText(time);

        date = DateFormat.getDateInstance().format(c.getTime());
        binding.dateEventPicker.setText(date);

        int img = vm.reminderState? R.drawable.ic_notification_alert : R.drawable.ic_no_ring;
        binding.reminder.setImageResource(img);
    }

    public void putData(Event event){

        binding.addEventTitle.setText(event.getTitle());
        binding.desc.setText(event.getDescription());
        parseDate(event.getDateTime());
        vm.reminderState = event.isReminder();

    }
}