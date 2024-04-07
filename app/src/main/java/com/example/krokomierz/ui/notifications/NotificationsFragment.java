package com.example.krokomierz.ui.notifications;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.krokomierz.MainActivity;
import com.example.krokomierz.databinding.FragmentNotificationsBinding;

import org.w3c.dom.Text;

public class NotificationsFragment extends Fragment {

    private FragmentNotificationsBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        NotificationsViewModel notificationsViewModel =
                new ViewModelProvider(this).get(NotificationsViewModel.class);

        binding = FragmentNotificationsBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        MainActivity mainActivity = (MainActivity) getActivity();
        if (mainActivity != null){
            TextView pointsDisplay = binding.pointsCounter;
            int storePoints = mainActivity.getPoints();
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