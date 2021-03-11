package edu.neu.madcourse.gritoria;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class RunningActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_running);

        Button runningLogButton = findViewById(R.id.RunningLogButton);
        runningLogButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openRunningLog();
            }
        });
    }

    public void openRunningLog(){
        Intent intent = new Intent(this, RunningLogActivity.class);
        startActivity(intent);
    }
}