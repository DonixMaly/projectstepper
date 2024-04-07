package com.example.krokomierz.ui.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.krokomierz.MainActivity;
import com.example.krokomierz.databinding.FragmentHomeBinding;

import java.text.DecimalFormat;

public class HomeFragment extends Fragment {

    private FragmentHomeBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        HomeViewModel homeViewModel =
                new ViewModelProvider(this).get(HomeViewModel.class);

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        MainActivity mainActivity = (MainActivity) getActivity();
        if (mainActivity != null) {
            int stepCount = mainActivity.getStepCount();
            float distanceTravelled = stepCount * 0.762f / 1000;
            float caloriesBurnt = stepCount * 0.04f;
            int storePoints = mainActivity.getPoints();

            TextView stepsCountDisplay = binding.stepsCounter;
            TextView distanceDisplay = binding.distanceTravelled;
            TextView caloriesDisplay = binding.caloriesBurnt;
            TextView pointsDisplay = binding.pointsCounter;

            stepsCountDisplay.setText(String.valueOf(stepCount));
            distanceDisplay.setText(String.format("Przebyta droga: %skm", new DecimalFormat("##.##").format(distanceTravelled)));
            caloriesDisplay.setText(String.format("Spalone kalorie: %skcal", new DecimalFormat("#####").format(caloriesBurnt)));
            pointsDisplay.setText("Punkty: " + storePoints);
        }


        return root;
    }
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;

    }
}