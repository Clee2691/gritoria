package edu.neu.madcourse.gritoria;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class LiftingActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lifting);

        Button progress = findViewById(R.id.progressButton);
        progress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openProgress();
            }
        });
        Button log = findViewById(R.id.liftBtn);
        log.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openLift();
            }
        });
    }

    public void openProgress(){
        Intent intent = new Intent(this, Progress.class);
        startActivity(intent);
    }
    public void openLift(){
        Intent intent = new Intent(this, LiftingStaticView.class);
        startActivity(intent);
    }





}
