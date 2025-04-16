package com.example.Elkfrawy.ui.eventUi;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.Elkfrawy.R;
import com.example.Elkfrawy.databinding.FragmentEventBinding;
import com.example.Elkfrawy.model.Event;
import com.example.Elkfrawy.ui.ClockViewModel;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class EventFragment extends Fragment {
    private static final String TAG = "ALERT";
    public static final String EDIT_EVENT_KEY = "editEvent";
    public static final int EDIT_EVENT_VALUE = 11;
    public static final int EDIT_EVENT_CALENDER_VIEW = 12;

    ClockViewModel viewModel;
    FragmentEventBinding binding;
    NavController controller;
    EventRecycleAdapter adapter;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewModel = new ViewModelProvider(this).get(ClockViewModel.class);
        adapter = new EventRecycleAdapter(requireContext());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentEventBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        controller = Navigation.findNavController(view);

        binding.recyclerView.setAdapter(adapter);
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        binding.calenderView.setOnDateChangeListener((v, y, m, d) -> {
            openAddEventFragment(-1);
        });

        viewModel.getEvents().observe(getViewLifecycleOwner(), events -> adapter.getList(events));


        binding.addEvent.setOnClickListener(v -> {
            openAddEventFragment(-1);
        });

        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {

                int position = viewHolder.getAdapterPosition();
                Event event = adapter.getEvent(position);
                viewModel.delete(event);
                adapter.removeAlarm(event.hashCode());

            }
        }).attachToRecyclerView(binding.recyclerView);


        adapter.onChangeClickListener(new EventRecycleAdapter.onClickListener() {
            @Override
            public void onBellClickListener(Event event) {
                viewModel.update(event);
            }

            @Override
            public void onViewClickListener(Event event, int position) {
                viewModel.setEvent(event);
                openAddEventFragment(EDIT_EVENT_VALUE);
            }
        });

    }

    public void openAddEventFragment(int state) {

        FragmentManager manager = getChildFragmentManager();

        Bundle b = new Bundle();
        b.putInt(EDIT_EVENT_KEY, state);
        FragmentTransaction transaction = manager.beginTransaction()
                .add(R.id.eventContainer, AddEventFragment.class, b)
                .addToBackStack("Add event");
        transaction.commit();
    }


}