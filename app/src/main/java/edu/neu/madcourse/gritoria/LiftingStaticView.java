package edu.neu.madcourse.gritoria;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class LiftingStaticView extends AppCompatActivity {
    //map for all the names
    ArrayList<String> liftNames = new ArrayList<String>();
    //map for all input types
    HashMap<String, Integer> squatMap = new HashMap<>();
    HashMap<String, Integer> deadMap = new HashMap<>();
    HashMap<String, Integer> benchMap = new HashMap<>();
    HashMap<String, Integer> overHeadPressMap = new HashMap<>();
    List<HashMap> mapOfWorkoutActivity = new ArrayList();
    private DatabaseReference rootRef;
    DatabaseReference userStoreRef;
    private String tag = "debugging...";


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.lifting_static_view);

        //items for database
        String myDate= getIntent().getStringExtra("date");
        //root level , root document this is gritoria
        rootRef = FirebaseDatabase.getInstance().getReference();
//        userStore = FirebaseStorage.getInstance();
        userStoreRef = rootRef.child("users");





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
                save.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        AlertDialog alertDialog = new AlertDialog.Builder(LiftingStaticView.
                                this).create();
                        alertDialog.setMessage("Would you like to save your current progress and " +
                                "exit? You'll" +
                                "go back to the lifting page to log more when you're ready but can't " +
                                "return to today's" +
                                "workout.");
                        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "yes",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {


                                        setMap(squatMap, integerify(squatWeight),
                                                integerify(squatSets), integerify(squatReps));
                                        setMap(deadMap, integerify(deadWeight),  integerify(deadSets)
                                                , integerify(deadReps));
                                        setMap(benchMap, integerify(benchWeight),
                                                integerify(benchSets), integerify(benchReps));
                                        setMap(overHeadPressMap,
                                                integerify(overWeight),  integerify(overSets),
                                                integerify(overReps));

                                        setFinalMap(squatMap, deadMap, benchMap,overHeadPressMap);


                                 AtomicInteger counter = new AtomicInteger(0);
                                 Log.e("names before", liftNames.toString());
                                 Log.e("map before", mapOfWorkoutActivity.toString());
                                  liftNames.forEach(name -> {
                                      rootRef.child("users").
                                              child("4RX89PfEBUVDkH6FSHogqRse5Q72").
                                                child("workouts").child(myDate).child(name).
                                              setValue(mapOfWorkoutActivity.get(counter.get()));

                                      counter.addAndGet(1);


                                  });


                                        dialog.dismiss();
                                        Toast.makeText(getApplicationContext(),
                                                "progress saved!", Toast.LENGTH_SHORT).show();

                                        exit();


                                    }
                                });
                        alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "no",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                    }
                                });
                        alertDialog.show();

                    }
                });

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

    public void setFinalMap(HashMap<String,Integer> _squatMap,HashMap<String,Integer> _deadMap,
                           HashMap<String,Integer> _benchMap, HashMap<String,Integer> _pressMap){

        mapOfWorkoutActivity.add(_squatMap);
        mapOfWorkoutActivity.add(_deadMap);
        mapOfWorkoutActivity.add(_benchMap);
        mapOfWorkoutActivity.add(_pressMap);

    }


    public void exit(){
        Intent intent = new Intent(this, LiftingActivity.class);
        startActivity(intent);
    }




}
