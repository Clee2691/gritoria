package edu.neu.madcourse.gritoria;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

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


        liftNames.add("Squats");
        liftNames.add("Deadlift");
        liftNames.add("Bench");
        liftNames.add("Overhead Press");

        //getting all the id for squats
        EditText squatWeight = findViewById(R.id.squatWeight);
        EditText squatSets = findViewById(R.id.squatSets);
        EditText squatReps = findViewById(R.id.squatReps);


        squatWeight.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                String input;
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    input = v.getText().toString();
                    squatWeight.setText(input);
                    return true;
                }
                return false;
            }
        });


        squatReps.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                String input;
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    input = v.getText().toString();
                    squatSets.setText(input);
                    return true;
                }
                return false;
            }
        });

        squatSets.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                String input;
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    input = v.getText().toString();
                    squatReps.setText(input);
                    return true;
                }
                return false;
            }
        });


        //edit texts all the id for dead

        EditText deadWeight = findViewById(R.id.deadWeight);
        EditText deadSets = findViewById(R.id.deadSets);
        EditText deadReps = findViewById(R.id.deadReps);

        deadWeight.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                String input;
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    input = v.getText().toString();
                    deadWeight.setText(input);
                    return true;
                }
                return false;
            }
        });
        deadSets.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                String input;
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    input = v.getText().toString();
                    deadSets.setText(input);
                    return true;
                }
                return false;
            }
        });
        deadReps.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                String input;
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    input = v.getText().toString();
                    deadReps.setText(input);
                    return true;
                }
                return false;
            }
        });

        //all the id for bench

        EditText benchWeight = findViewById(R.id.benchWeight);
        EditText benchSets = findViewById(R.id.benchSets);
        EditText benchReps = findViewById(R.id.benchReps);

        benchWeight.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                String input;
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    input = v.getText().toString();
                    benchWeight.setText(input);
                    return true;
                }
                return false;
            }
        });
        benchSets.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                String input;
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    input = v.getText().toString();
                    benchSets.setText(input);
                    return true;
                }
                return false;
            }
        });
        benchReps.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                String input;
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    input = v.getText().toString();
                    benchReps.setText(input);
                    return true;
                }
                return false;
            }
        });



        //all the id for overhead press
        EditText overWeight = findViewById(R.id.pressWeight);
        EditText overSets = findViewById(R.id.pressSets);
        EditText overReps = findViewById(R.id.pressReps);


        overWeight.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                String input;
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    input = v.getText().toString();
                    overWeight.setText(input);
                    return true;
                }
                return false;
            }
        });
        overSets.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                String input;
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    input = v.getText().toString();
                    overSets.setText(input);
                    return true;
                }
                return false;
            }
        });
        overReps.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                String input;
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    input = v.getText().toString();
                    overReps.setText(input);
                    return true;
                }
                return false;
            }
        });


        Button save = findViewById(R.id.saveProgress);
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Log.e(tag, "in save onclick");
                Log.e(tag, stringify(squatSets));
                Log.e(tag, stringify(squatReps));
                Log.e(tag, stringify(squatWeight));
                setMap(squatMap, integerify(squatWeight),  integerify(squatSets), integerify(squatReps));
                setMap(deadMap, integerify(deadWeight),  integerify(deadSets), integerify(deadReps));
                setMap(benchMap, integerify(benchWeight),  integerify(benchSets), integerify(benchReps));
                setMap(overHeadPressMap, integerify(overWeight),  integerify(overSets), integerify(overReps));

                Log.e("squats map???", squatMap.toString());
                Log.e("bench map???", benchMap.toString());
                Log.e("dead map???", deadMap.toString());
                Log.e("overhead press  map???", overHeadPressMap.toString());



            }

        });



    }

    public String stringify(EditText edit) {
        String returnText = edit.getText().toString();
        return returnText;
    }
    public Integer integerify(EditText edit) {
        if(edit==null){
            return 0;
        }
        String text = edit.getText().toString();
        int returnValue=Integer.parseInt(text);
        return returnValue;
    }


    public void setMap(HashMap<String, Integer> selectedMap, int weight, int sets, int reps) {
        selectedMap.put("weight", weight);
        selectedMap.put("sets", sets);
        selectedMap.put("reps", reps);

    }




}
