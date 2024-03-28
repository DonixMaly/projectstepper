package com.example.krokomierz;


import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.widget.TextView;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.krokomierz.databinding.ActivityMainBinding;

import java.text.DecimalFormat;

public class MainActivity extends AppCompatActivity implements SensorEventListener {

    private ActivityMainBinding binding;
    private TextView stepsCountDisplay;
    private TextView distanceDisplay;
    private SensorManager sensorManager;
    private Sensor stepSensor;

    private TextView caloriesDisplay;
    private int stepCount = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        BottomNavigationView navView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(R.id.navigation_home, R.id.navigation_dashboard, R.id.navigation_notifications).build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_activity_main);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(binding.navView, navController);

        stepsCountDisplay = findViewById(R.id.stepsCounter);
        distanceDisplay = findViewById(R.id.distanceTravelled);
        caloriesDisplay = findViewById(R.id.caloriesBurnt);

        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        stepSensor = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER);

        if (stepSensor == null) {
            stepsCountDisplay.setText("Licznik kroków nie dostępny");
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (stepSensor != null) {
            sensorManager.registerListener(this, stepSensor, SensorManager.SENSOR_DELAY_NORMAL);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        sensorManager.unregisterListener(this);
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor == stepSensor) {
            stepCount = (int) event.values[0];
            stepsCountDisplay.setText(String.valueOf(stepCount));
        }

        float distanceTravelled = stepCount * 0.762f / 1000;
        distanceDisplay.setText(String.format("Przebyta droga: %skm", new DecimalFormat("##.##").format(distanceTravelled)));

        float caloriesBurnt = stepCount * 0.04f;
        caloriesDisplay.setText(String.format("Spalone kalorie: %skcal", new DecimalFormat("#####").format(caloriesBurnt)));

    }
}