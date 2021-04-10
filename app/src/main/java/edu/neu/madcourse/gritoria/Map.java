package edu.neu.madcourse.gritoria;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import edu.neu.madcourse.gritoria.bosses.worldFights;

public class Map extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
    }

    public void enterWorld(View v) {
        int currWorld = v.getId();
        Intent worldFight = new Intent(this, worldFights.class);
        if (currWorld == R.id.imageButtonWorld1_1) {
            worldFight.putExtra("level", "1-1");

        } else if (currWorld == R.id.imageButtonWorld1_2) {
            worldFight.putExtra("level", "1-2");
        } else {
            worldFight.putExtra("level", "DEFAULT WORLD");
        }

        startActivity(worldFight);

    }
}