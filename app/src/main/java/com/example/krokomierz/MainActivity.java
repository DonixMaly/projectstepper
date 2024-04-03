package com.example.krokomierz;


import android.content.Context;
import android.content.SharedPreferences;
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

import java.text.SimpleDateFormat;
import java.util.Date;

import com.example.krokomierz.publicVariables;

public class MainActivity extends AppCompatActivity implements SensorEventListener, publicVariables {

    private ActivityMainBinding binding;
    private TextView stepsCountDisplay;
    private TextView distanceDisplay;
    private SensorManager sensorManager;
    private Sensor stepSensor;
    private TextView caloriesDisplay;
    public float storePoints = 0;
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

        loadData();

        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        stepSensor = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER);

        if (stepSensor == null) {
            stepsCountDisplay.setText("Licznik kroków nie dostępny");
        }

        SimpleDateFormat sdf = new SimpleDateFormat("HH");
        String hour = sdf.format(new Date());

        if(hour == "00"){
            stepCount = 0;
            saveData();
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

        float storePoints = distanceTravelled / 10;
        saveData();
        loadData();
    }
    @Override
    public void onSaveInstanceState(Bundle savedInstanceState){
        super.onSaveInstanceState(savedInstanceState);

        savedInstanceState.putString("stepsDone", stepsCountDisplay.getText().toString());
    }
    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState){
        super.onRestoreInstanceState(savedInstanceState);

        String stepsDone = savedInstanceState.getString("stepsDone");
        stepsCountDisplay.setText(stepsDone);

    }
    private void saveData(){
        SharedPreferences sharedPreferences = getSharedPreferences("myData", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        StringBuilder dataToSave = new StringBuilder();
        dataToSave.append(stepCount);

        editor.putString("stepsTaken", dataToSave.toString());
        editor.apply();
    }
    private void loadData(){
        SharedPreferences sharedPreferences = getSharedPreferences("mydata", MODE_PRIVATE);
        String savedData = sharedPreferences.getString("stepsTaken", "");

        if(!savedData.isEmpty()){
            stepsCountDisplay.setText("błąd: brak danych");
        }
    }

}