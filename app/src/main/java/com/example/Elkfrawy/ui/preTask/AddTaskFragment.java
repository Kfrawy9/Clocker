package com.example.Elkfrawy.ui.preTask;

import static com.example.Elkfrawy.ui.preTask.TaskFragment.STATE_EDIT_TASK;

import android.app.Dialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.room.Insert;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Toast;

import com.example.Elkfrawy.R;
import com.example.Elkfrawy.data.SpinnerData;
import com.example.Elkfrawy.databinding.FragmentAddTaskBinding;
import com.example.Elkfrawy.model.SpinnerItems;
import com.example.Elkfrawy.model.Task;
import com.example.Elkfrawy.ui.ClockViewModel;
import com.google.android.material.timepicker.MaterialTimePicker;
import com.google.android.material.timepicker.TimeFormat;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.zip.DataFormatException;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class AddTaskFragment extends DialogFragment implements AdapterView.OnItemSelectedListener {

    private static final String TAG = "TaskFragment";

    String time;
    List<SpinnerItems> data;
    FragmentAddTaskBinding binding;
    SpinnerItems pickedP;
    ClockViewModel vm;
    Calendar cal;
    Task task;
    int state;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        vm = new ViewModelProvider(requireActivity()).get(ClockViewModel.class);
        data = SpinnerData.loadData();
        pickedP = data.get(0);
        state = AddTaskFragmentArgs.fromBundle(getArguments()).getState();
        cal = Calendar.getInstance();
        cal.set(Calendar.SECOND, 0);

    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentAddTaskBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        SpinnerAdapter adapter = new SpinnerAdapter(requireContext(), R.layout.spinner_items, data);
        binding.colorPri.setAdapter(adapter);
        binding.colorPri.setOnItemSelectedListener(this);

        if (state == STATE_EDIT_TASK)
           putData();

        binding.addTime.setOnClickListener(v -> showTimeDialog());

        binding.save.setOnClickListener(v -> {
            String title = binding.addTitle.getText().toString();
            time = binding.addTime.getText().toString();

            if (title.length() > 0){
               if (state == STATE_EDIT_TASK) {
                   task.setTitle(title);
                   task.setColor(pickedP.getColor());
                   task.setPriority(pickedP.getPriority());
                   task.setTime(time);
                   vm.update(task);
               }
               else
                   vm.insert(new Task(title, time, pickedP.getPriority(), pickedP.getColor()));

               dismiss();
           }else
               Toast.makeText(requireContext(), "Enter a title", Toast.LENGTH_LONG).show();
        });
    }



    public void putData(){
        task = vm.getTask();
        binding.addTitle.setText(task.getTitle());
        binding.addTime.setText(task.getTime());
        int pos = data.indexOf(new SpinnerItems(task.getPriority(), task.getColor()));
        binding.colorPri.setSelection(pos);
        updateCal();
    }

    public void showTimeDialog(){

        MaterialTimePicker.Builder builder = new MaterialTimePicker.Builder()
                .setInputMode(MaterialTimePicker.INPUT_MODE_CLOCK)
                .setTimeFormat(TimeFormat.CLOCK_12H)
                .setTitleText("Add Time")
                .setHour(cal.get(Calendar.HOUR_OF_DAY))
                .setMinute(cal.get(Calendar.MINUTE))
                .setPositiveButtonText("Save")
                .setNegativeButtonText("Cancel");

        MaterialTimePicker picker = builder.build();
        picker.show(getChildFragmentManager(), "TimePicker");

        picker.addOnPositiveButtonClickListener(v -> {
            int h = picker.getHour();
            int m = picker.getMinute();

            cal.set(Calendar.HOUR_OF_DAY, h);
            cal.set(Calendar.MINUTE, m);

            time = DateFormat.getTimeInstance().format(cal.getTime());
            binding.addTime.setText(time);

        });

    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        pickedP = data.get(position);
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

   public void updateCal(){
       SimpleDateFormat format = new SimpleDateFormat("hh:mm:ss a");
       try {
           Date d = format.parse(task.getTime());
           cal.setTime(d);
       } catch (ParseException e) {
           e.printStackTrace();
       }

   }
}