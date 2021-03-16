package edu.neu.madcourse.gritoria;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;

import java.io.File;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // Initialize the buttons
        Button team = findViewById(R.id.TeamButton);
        team.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openTeam();
            }
        });

        Button planner = findViewById(R.id.PlannerButton);
        planner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openPlanner();
            }
        });

        Button profile = findViewById(R.id.ProfileButton);
        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openProfile();
            }
        });

        Button map = findViewById(R.id.MapButton);
        map.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openMap();
            }
        });

        Button runButton = findViewById(R.id.RunButton);
        runButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openRun();
            }
        });
        Button liftingButton_ = findViewById(R.id.liftingButton);
        liftingButton_.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openLift();
            }
        });

    }



    // Open new activities on button press

    public void openTeam(){
        Intent intent = new Intent(this, Team.class);
        startActivity(intent);
    }

    public void openPlanner(){
        Intent intent = new Intent(this, Planner.class);
        startActivity(intent);
    }

    public void openProfile(){
        Intent intent = new Intent(this, Profile.class);
        startActivity(intent);
    }

    public void openMap(){
        Intent intent = new Intent(this, Map.class);
        startActivity(intent);
    }

    public void openRun() {
        Intent intent = new Intent(this, RunningActivity.class);
        startActivity(intent);
    }

    public void openLift(){
        Intent intent = new Intent(this, LiftingActivity.class);
        startActivity(intent);
    }
}