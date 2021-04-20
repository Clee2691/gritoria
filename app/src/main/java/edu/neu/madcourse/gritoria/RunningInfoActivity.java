package edu.neu.madcourse.gritoria;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

public class RunningInfoActivity extends AppCompatActivity {

    public TextView runningDateValue;
    public TextView runningDistanceValue;
    public TextView runningSteps;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_running_info);

        runningDateValue = findViewById(R.id.runningInfoDate);
        runningDateValue.setText(getIntent().getStringExtra("date"));

        runningDistanceValue = findViewById(R.id.runningInfoDistance);
        runningDistanceValue.setText(getIntent().getStringExtra("distance"));

        runningSteps = findViewById(R.id.runningInfoSteps);
        runningSteps.setText(getIntent().getStringExtra("steps"));
        
    }
}