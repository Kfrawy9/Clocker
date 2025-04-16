package com.example.Elkfrawy.ui.preTask;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavBackStackEntry;
import androidx.navigation.NavController;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.Elkfrawy.R;
import com.example.Elkfrawy.databinding.FragmentTaskBinding;
import com.example.Elkfrawy.model.Task;
import com.example.Elkfrawy.model.Weather;
import com.example.Elkfrawy.ui.ClockViewModel;

import java.util.ArrayList;
import java.util.List;

public class TaskFragment extends Fragment {

    public static final int STATE_NEW_TASK = 1;
    public static final int STATE_EDIT_TASK = 2;
    private static final String TAG = "RecyclerTaskAdapter";

    ClockViewModel viewModel;
    NavController controller;
    FragmentTaskBinding binding;
    RecyclerTaskAdapter adapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

       viewModel = new ViewModelProvider(requireActivity()).get(ClockViewModel.class);
       adapter = new RecyclerTaskAdapter();
       adapter.getContext(requireContext());

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentTaskBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        controller = Navigation.findNavController(view);
        binding.recyclerView.setAdapter(adapter);
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));


        viewModel.getTime().observe(getViewLifecycleOwner(), s -> binding.clock.setText(s));
        viewModel.getDate().observe(getViewLifecycleOwner(), d -> binding.date.setText(d));
        viewModel.getTasks().observe(getViewLifecycleOwner(), t -> adapter.submitList(t));
        viewModel.getCachedWeather().observe(getViewLifecycleOwner(), this::putWeatherData);

        binding.fabAddTask.setOnClickListener(v -> {
            NavDirections dir = TaskFragmentDirections.actionTaskFragmentToAddTaskFragment(STATE_NEW_TASK);
            controller.navigate(dir);
        });

        adapter.onChange(new RecyclerTaskAdapter.onCheckedListener() {
            @Override
            public void onCheckedChange(Task t, boolean isChecked) {
                t.setChecked(isChecked);
                viewModel.update(t);
            }

            @Override
            public void onViewClicked(Task t) {
                viewModel.setTask(t);
                showDialog(STATE_EDIT_TASK);
            }
        });

        deleteTask();
    }

    public void deleteTask(){
        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                int pos = viewHolder.getAdapterPosition();
                viewModel.delete(adapter.getTask(pos));
            }
        }).attachToRecyclerView(binding.recyclerView);
    }

    public void putWeatherData(Weather weather){
        String imgId = "s" + weather.getWeatherIcon();
        int res = getResources().getIdentifier(imgId, "drawable", requireContext().getPackageName());
        binding.weatherState.weatherIcon.setImageResource(res);

        int value = (weather.getValue() - 32) * 5 / 9;
        binding.weatherState.tempValue.setText(value + "");
    }

    @Override
    public void onStart() {
        super.onStart();
        viewModel.start();
        viewModel.setWeather();
    }

    @Override
    public void onStop() {
        super.onStop();
        viewModel.stop();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        viewModel.stop();
        binding = null;
    }

    public void showDialog(int state){
        NavDirections dir = TaskFragmentDirections.actionTaskFragmentToAddTaskFragment(state);
        controller.navigate(dir);
    }
}