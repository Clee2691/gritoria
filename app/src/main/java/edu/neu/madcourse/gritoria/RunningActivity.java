package edu.neu.madcourse.gritoria;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.widget.TextView;

import pl.droidsonroids.gif.GifImageView;

public class RunningActivity extends AppCompatActivity implements SensorEventListener {

    private SensorManager sensorManager;
    private Sensor stepCounterSensor;
    private TextView stepCounterValue;
    private TextView distanceValue;
    private GifImageView gifAnimation;
    private boolean active = false;
    private int stepCount = 0;
    private float distance = 0;
    private static final int PERMISSIONS_ACTIVITY_RECOGNITION = 666;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_running);

        gifAnimation = findViewById(R.id.ninjaRunnerGif);
        stepCounterValue = findViewById(R.id.stepsValue);
        distanceValue = findViewById(R.id.distanceValue);

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACTIVITY_RECOGNITION)
                != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[] {Manifest.permission.ACTIVITY_RECOGNITION}, PERMISSIONS_ACTIVITY_RECOGNITION);
        }

        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        stepCounterSensor = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_DETECTOR);


        Button runningLogButton = findViewById(R.id.RunningLogButton);
        runningLogButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openRunningLog();
            }
        });


        Button questButton = findViewById(R.id.StartRunButton);
        questButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (active) {
                    active = false;
                    questButton.setText("Begin Crawl");
                    questButton.setBackgroundColor(Color.GREEN);
                    gifAnimation.setImageResource(R.drawable.idle);
                }
                else {
                    active = true;
                    stepCount = 0;
                    distance = 0;
                    gifAnimation.setImageResource(R.drawable.running);
                    String totalSteps = "Steps: " + String.valueOf(stepCount);
                    String distanceTraveled = "Distance: " + String.valueOf(distance) + " miles";
                    stepCounterValue.setText(totalSteps);
                    distanceValue.setText(distanceTraveled);
                    questButton.setText("Finish Crawl");
                    questButton.setBackgroundColor(Color.RED);
                }
            }
        });
    }

    public void onSensorChanged(SensorEvent event) {
        if (event.sensor == stepCounterSensor && active) {
            stepCount = stepCount + (int) event.values[0];
            String totalSteps = "Steps: " + String.valueOf(stepCount);
            String distanceTraveled = String.format("%.2f", (stepCount * .0005));
            String formattedDistance = "Distance: " + distanceTraveled + " miles";
            stepCounterValue.setText(totalSteps);
            distanceValue.setText(formattedDistance);
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    @Override
    protected void onResume() {
        super.onResume();
        if (sensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER) != null) {
            sensorManager.registerListener(this, stepCounterSensor, SensorManager.SENSOR_DELAY_NORMAL);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (sensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER) != null) {
            sensorManager.unregisterListener(this, stepCounterSensor);
        }
    }


    public void openRunningLog(){
        Intent intent = new Intent(this, RunningLogActivity.class);
        startActivity(intent);
    }
}