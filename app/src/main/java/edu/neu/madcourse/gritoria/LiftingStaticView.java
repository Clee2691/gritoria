package edu.neu.madcourse.gritoria;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class LiftingStaticView extends AppCompatActivity {
    //map for all the names
    ArrayList<String> liftNames = new ArrayList<String>();
    //map for all input types
    HashMap<String, Integer> squatMap = new HashMap<>();
    HashMap<String, Integer> deadMap = new HashMap<>();
    HashMap<String, Integer> benchMap = new HashMap<>();
    HashMap<String, Integer> overHeadPressMap = new HashMap<>();
    List<HashMap> mapOfWorkoutActivity = new ArrayList();
    private String tag = "debugging...";


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.lifting_static_view);

        //adding all the lift names to map to db
        Log.e(tag, "all going well");


        liftNames.add("Squats");
        liftNames.add("Deadlift");
        liftNames.add("Bench");
        liftNames.add("Overhead Press");

        //getting all the id for squats
        EditText squatWeight = findViewById(R.id.squatWeight);
        EditText squatSets = findViewById(R.id.squatSets);
        EditText squatReps = findViewById(R.id.squatReps);


        //edit texts all the id for dead

        EditText deadWeight = findViewById(R.id.deadWeight);
        EditText deadSets = findViewById(R.id.deadSets);
        EditText deadReps = findViewById(R.id.deadReps);

        //all the id for bench

        EditText benchWeight = findViewById(R.id.benchWeight);
        EditText benchSets = findViewById(R.id.benchSets);
        EditText benchReps = findViewById(R.id.benchReps);

        //all the id for overhead press
        EditText overWeight = findViewById(R.id.pressWeight);
        EditText overSets = findViewById(R.id.pressSets);
        EditText overReps = findViewById(R.id.pressReps);

        Button save = findViewById(R.id.saveProgress);
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("alls going well", "all going well");
            }
        });




    }

    public void setMap(HashMap<String, Integer> selectedMap, int weight, int sets, int reps){
        selectedMap.put("weight",  weight);
        selectedMap.put("sets",  sets);
        selectedMap.put("reps",  reps);

    }


}
