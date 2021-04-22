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

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.MutableData;
import com.google.firebase.database.Transaction;
import com.google.firebase.database.ValueEventListener;

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
    Integer myPower;

    List<HashMap> mapOfWorkoutActivity = new ArrayList();


    private DatabaseReference rootRef;
    DatabaseReference userStoreRef;
    private String tag = "debugging...";


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.lifting_static_view);

        //items for database
        String myDate = getIntent().getStringExtra("date");
        //root level , root document this is gritoria
        rootRef = FirebaseDatabase.getInstance().getReference();
//        userStore = FirebaseStorage.getInstance();
        userStoreRef = rootRef.child("users");

        String mAuth = FirebaseAuth.getInstance().getUid();
        String myPower = rootRef.child("users").child(mAuth).child("power").toString();

        String powerString =  rootRef.child("users").child(mAuth).child("stats").child("str").toString();

        Log.e("getting power...", powerString);


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

        Button backButton = findViewById(R.id.liftBackButton);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                exit();
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
                                "exit? You'll" + " " +
                                "go back to the lifting page to log more when you're ready but can't " +
                                " " + "return to today's workout");
                        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "yes",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        LiftingGlobal.globalSets.add(integerify(squatSets));


                                        setMap(squatMap, integerify(squatWeight),
                                                integerify(squatSets), integerify(squatReps));
                                        setMap(deadMap, integerify(deadWeight), integerify(deadSets)
                                                , integerify(deadReps));
                                        setMap(benchMap, integerify(benchWeight),
                                                integerify(benchSets), integerify(benchReps));
                                        setMap(overHeadPressMap,
                                                integerify(overWeight), integerify(overSets),
                                                integerify(overReps));

                                        setFinalMap(squatMap, deadMap, benchMap, overHeadPressMap);

                                        AtomicInteger counter = new AtomicInteger(0);
                                        rootRef.child("users").child(mAuth).addListenerForSingleValueEvent(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                if (snapshot.exists()) {
                                                    liftNames.forEach(name -> {
                                                        rootRef.child("users").
                                                                child(mAuth).
                                                                child("workouts").child(myDate).child(name).
                                                                setValue(mapOfWorkoutActivity.get(counter.get()));

                                                        counter.addAndGet(1);

                                                    });
                                                     addPower(mAuth);
                                                } else {
                                                    Log.e("Does", "exist");
                                                }
                                            }

                                            @Override
                                            public void onCancelled(@NonNull DatabaseError error) {

                                            }
                                        });

                                        dialog.dismiss();
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
        if (edit == null) {
            return 0;
        }
        String text = edit.getText().toString();

        if (isNumeric(text)) {
            int returnValue = Integer.parseInt(text);
            return returnValue;
        }
        return 0;

    }

    public Integer stringToInt(String string) {
        if (string == null) {
            return 0;
        }

        if (isNumeric(string)) {
            int returnValue = Integer.parseInt(string);
            return returnValue;
        }
        return 0;

    }

    public static boolean isNumeric(String str) {
        return str.matches("-?\\d+(\\.\\d+)?");
    }

    public void setMap(HashMap<String, Integer> selectedMap, int weight, int sets, int reps) {
        selectedMap.put("weight", weight);
        selectedMap.put("sets", sets);
        selectedMap.put("reps", reps);

    }

    public void setFinalMap(HashMap<String, Integer> _squatMap, HashMap<String, Integer> _deadMap,
                            HashMap<String, Integer> _benchMap, HashMap<String, Integer> _pressMap) {

        mapOfWorkoutActivity.add(_squatMap);
        mapOfWorkoutActivity.add(_deadMap);
        mapOfWorkoutActivity.add(_benchMap);
        mapOfWorkoutActivity.add(_pressMap);

    }


    public void exit() {
        Intent intent = new Intent(this, LiftingActivity.class);
        startActivity(intent);
    }

    public void addPower(String mAuth) {
        rootRef.child("users").child(mAuth).child("stats").child("str").runTransaction(new Transaction.Handler() {
            @NonNull
            @Override
            public Transaction.Result doTransaction(@NonNull MutableData currentData) {
                Log.e("what is current data?", currentData.toString());
                int currStr = currentData.getValue(Integer.class);
                currStr += 1;
                currentData.setValue(currStr);
                return Transaction.success(currentData);
            }

            @Override
            public void onComplete(@Nullable DatabaseError error, boolean committed, @Nullable DataSnapshot currentData) {

            }
        });

    }

}


