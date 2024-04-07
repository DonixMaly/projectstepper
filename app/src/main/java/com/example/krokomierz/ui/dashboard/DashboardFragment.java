package com.example.krokomierz.ui.dashboard;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.krokomierz.MainActivity;
import com.example.krokomierz.databinding.FragmentDashboardBinding;

public class DashboardFragment extends Fragment {

    private FragmentDashboardBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        DashboardViewModel dashboardViewModel =
                new ViewModelProvider(this).get(DashboardViewModel.class);

        binding = FragmentDashboardBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        MainActivity mainActivity = (MainActivity) getActivity();
        if (mainActivity != null) {
            int storePoints = mainActivity.getPoints();

            TextView pointsDisplay = binding.pointsCounter;
            Button spotifyBuy = binding.button2;
            Button nikeBuy = binding.button3;
            Button giftCardBuy = binding.button4;
            Button zabkaBuy = binding.button5;

            pointsDisplay.setText("Punkty: " + storePoints);

            spotifyBuy.setOnClickListener(v -> buyItem(storePoints, 1000));
            nikeBuy.setOnClickListener(v -> buyItem(storePoints, 5000));
            giftCardBuy.setOnClickListener(v -> buyItem(storePoints, 50000));
            zabkaBuy.setOnClickListener(v -> buyItem(storePoints, 2500));
        }
        return root;
    }

    private void buyItem(int points, int requiredPoints) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setMessage("Masz pewność, że chcesz kupić?")
                .setTitle("Stepper")
                .setPositiveButton("TAK", (dialog, id) -> {
                    if (points >= requiredPoints) {
                        int remainingPoints = points - requiredPoints;
                        MainActivity mainActivity = (MainActivity) getActivity();
                        if (mainActivity != null) {
                            mainActivity.setPoints(remainingPoints);
                            TextView pointsDisplay = binding.pointsCounter;
                            pointsDisplay.setText("Punkty: " + remainingPoints);
                        }
                        AlertDialog.Builder builder2 = new AlertDialog.Builder(getContext());
                        builder2.setMessage("Dziękujemy za zakup")
                                .setTitle("Zakup udany")
                                .setPositiveButton("OK", (dialog2, id2) -> {
                                });
                        AlertDialog dialog2 = builder2.create();
                        dialog2.show();
                    } else {
                        AlertDialog.Builder builder2 = new AlertDialog.Builder(getContext());
                        builder2.setMessage("Za mało punktów")
                                .setTitle("Zakup nieudany")
                                .setPositiveButton("OK", (dialog2, id2) -> {
                                });
                        AlertDialog dialog2 = builder2.create();
                        dialog2.show();
                    }
                });
        AlertDialog dialog = builder.create();
        dialog.show();
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}