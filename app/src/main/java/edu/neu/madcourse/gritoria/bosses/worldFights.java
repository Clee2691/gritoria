package edu.neu.madcourse.gritoria.bosses;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import edu.neu.madcourse.gritoria.R;

public class worldFights extends AppCompatActivity {
    private Intent currIntent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_world_fights);
        currIntent = getIntent();
        setupWorld(currIntent);
    }

    private void setupWorld(Intent currIntent) {
        TextView worldLogo = findViewById(R.id.textViewWorld);
        String currWorld = currIntent.getStringExtra("level");
        worldLogo.setText(String.format("World %s", currWorld));
    }
}