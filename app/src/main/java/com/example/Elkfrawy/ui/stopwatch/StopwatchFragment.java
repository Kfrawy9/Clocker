package com.example.Elkfrawy.ui.stopwatch;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.Elkfrawy.R;
import com.example.Elkfrawy.databinding.FragmentStopwatchBinding;
import com.example.Elkfrawy.ui.ClockViewModel;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class StopwatchFragment extends Fragment {

    FragmentStopwatchBinding binding;
    LapListAdapter adapter;
    ClockViewModel viewModel;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewModel = new ViewModelProvider(this).get(ClockViewModel.class);
        adapter = new LapListAdapter(requireContext());
        }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentStopwatchBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        loadImg();
        binding.lapList.setAdapter(adapter);
        viewModel.observeLaps().observe(getViewLifecycleOwner(), laps -> adapter.setList(laps));
        viewModel.getStopWatch().observe(getViewLifecycleOwner(), sw -> binding.time.setText(sw));
        viewModel.getMilliSecond().observe(getViewLifecycleOwner(), ms -> binding.milliSecond.setText(ms));
        binding.reset.setOnClickListener(v -> reset());

        binding.lap.setOnClickListener(v -> {
            String s = binding.time.getText().toString();
            if (!s.equals("00:00:00")) {
                viewModel.lapList.add(0, s);
                viewModel.setLaps(viewModel.lapList);
            }
        });

        binding.playPause.setOnClickListener(v -> {
            viewModel.playPause = !viewModel.playPause;
            loadImg();

            if (viewModel.playPause) viewModel.startStopwatch();
            else viewModel.stopStopWatch();
        });
    }

    public void reset() {
        viewModel.shutDownStopwatch();
        viewModel.playPause = false;
        viewModel.stopwatch.setValue("00:00:00");
        viewModel.milliSecond.setValue("00");
        binding.playPause.setImageResource(R.drawable.ic_play);
        viewModel.lapList.clear();
        viewModel.setLaps(viewModel.lapList);
    }

    public void loadImg() {
        int icon = viewModel.playPause ? R.drawable.ic_pause : R.drawable.ic_play;
        binding.playPause.setImageResource(icon);
    }

}