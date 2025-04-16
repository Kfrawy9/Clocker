package com.example.Elkfrawy.ui;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.Elkfrawy.R;
import com.example.Elkfrawy.core.DateThreads;
import com.example.Elkfrawy.databinding.FragmentTimerBinding;

import java.util.Locale;


public class TimerFragment extends Fragment implements View.OnClickListener {
    private static final String TAG = "Timer";
    FragmentTimerBinding binding;
    ClockViewModel viewModel;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewModel = new ViewModelProvider(this).get(ClockViewModel.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentTimerBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if (viewModel.onOff) {
            binding.startTimer.timeProgress.setMax(viewModel.time);
            playView();
        }

        viewModel.time = formatTime(viewModel.timeText);

        binding.days.bu0.setOnClickListener(this);
        binding.days.bu1.setOnClickListener(this);
        binding.days.bu2.setOnClickListener(this);
        binding.days.bu3.setOnClickListener(this);
        binding.days.bu4.setOnClickListener(this);
        binding.days.bu5.setOnClickListener(this);
        binding.days.bu6.setOnClickListener(this);
        binding.days.bu7.setOnClickListener(this);
        binding.days.bu8.setOnClickListener(this);
        binding.days.bu9.setOnClickListener(this);

        viewModel.getTimerI().observe(getViewLifecycleOwner(), i -> {
            if (i == viewModel.time)
                alert();
            updateRunningTimer(i);
        });

        binding.spTimer.setOnClickListener(v -> {
            viewModel.isRunning = !viewModel.isRunning;
            if (!viewModel.onOff){
                playView();
                binding.startTimer.timeProgress.setMax(viewModel.time);
                viewModel.startTimer(viewModel.time);

                viewModel.onOff = true;
            }else {

                int res = viewModel.isRunning? R.drawable.ic_pause : R.drawable.ic_play;
                binding.spTimer.setImageResource(res);

                if (viewModel.isRunning) viewModel.startTimer(viewModel.time);
                else viewModel.pauseTimer();

            }
        });


        binding.reset.setOnClickListener(v -> resetView());

        binding.remove.setOnClickListener(v -> {
            if (viewModel.timeText.length() > 0) {
                viewModel.timeText = viewModel.timeText.substring(0, viewModel.timeText.length() - 1);
                viewModel.time = formatTime(viewModel.timeText);
            }
        });
    }

    @Override
    public void onClick(View v) {
        String s = ((TextView) v).getText().toString();
        callFormat(s);
    }

    public void playView() {
        binding.days.getRoot().setVisibility(View.GONE);
        binding.reset.setVisibility(View.VISIBLE);
        binding.startTimer.getRoot().setVisibility(View.VISIBLE);
        binding.spTimer.setImageResource(R.drawable.ic_pause);
        binding.timer.setVisibility(View.INVISIBLE);
        binding.remove.setVisibility(View.INVISIBLE);

        binding.startTimer.countdown.setText(binding.timer.getText().toString());
    }

    public void resetView() {

        binding.days.getRoot().setVisibility(View.VISIBLE);
        binding.reset.setVisibility(View.INVISIBLE);
        binding.startTimer.getRoot().setVisibility(View.GONE);
        binding.spTimer.setImageResource(R.drawable.ic_play);
        binding.timer.setVisibility(View.VISIBLE);
        binding.remove.setVisibility(View.VISIBLE);
        binding.spTimer.setVisibility(View.INVISIBLE);
        binding.timer.setText("00:00:00");
        viewModel.time = 0;
        viewModel.timeText = "";
        viewModel.onOff = false;
        viewModel.isRunning = false;

        viewModel.shutdownTimer();
    }

    public int formatTime(String s) {
        int time = 0;

        if (!s.equals(""))
            time = Integer.parseInt(s);

        if (time > 0)
            binding.spTimer.setVisibility(View.VISIBLE);
        else
            binding.spTimer.setVisibility(View.INVISIBLE);

        String txt = "";
        for (int i = 0; i < (6 - s.length()); i++)
            txt += "0";

        txt += s;

        for (int i = 0; i < 6; i++) {
            if (i == 2 || i == 4)
                viewModel.formattedText += ":";
            viewModel.formattedText += txt.charAt(i);
        }
        binding.timer.setText(viewModel.formattedText);
        viewModel.formattedText = "";
        return time;
    }

    public void callFormat(String s){
        int len = viewModel.timeText.length();
        if (len < 6) {
            if (s.equals("0")) {
                if (viewModel.timeText.length() != 0) {
                    viewModel.timeText += s;
                    viewModel.time = formatTime(viewModel.timeText);
                }
            }
            else {
                viewModel.timeText += s;
                viewModel.time = formatTime(viewModel.timeText);
            }
        }
    }

    public void updateRunningTimer(int s){

        int second = viewModel.time - s;

        int hours = second / 3600;
        int minutes = (second % 3600) / 60;
        int secs = second % 60;

        String stopwatch = String.format(Locale.getDefault(), "%02d:%02d:%02d", hours, minutes, secs);
        binding.startTimer.timeProgress.setProgress(s);
        binding.startTimer.countdown.setText(stopwatch);
        if (secs == 0)
            resetView();
    }

    public void alert(){
        MediaPlayer player = MediaPlayer.create(requireContext(), R.raw.event);
        player.start();

        player.setOnCompletionListener(MediaPlayer::release);
    }

}
