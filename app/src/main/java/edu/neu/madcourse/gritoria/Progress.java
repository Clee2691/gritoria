package edu.neu.madcourse.gritoria;

import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.common.graph.Graph;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.LegendRenderer;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.DataPointInterface;
import com.jjoe64.graphview.series.LineGraphSeries;
import com.jjoe64.graphview.series.OnDataPointTapListener;
import com.jjoe64.graphview.series.Series;

import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicInteger;

public class Progress extends AppCompatActivity {

    private DatabaseReference rootRef;
    DatabaseReference reference;
    //strings to get all the datashot items
    ArrayList<String> squatWeight = new ArrayList<String>();
    ArrayList<String> deadWeight = new ArrayList<String>();
    ArrayList<String> benchWeight = new ArrayList<String>();
    ArrayList<String> overheadWeight = new ArrayList<String>();
    private boolean clicked = false;
    private boolean deadClick = false;
    private boolean benchClick = false;
    private boolean overClick = false;





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_progress);
        //getting the user
        String mAuth = FirebaseAuth.getInstance().getUid();

        //initialize graph view by id and setting it's title
        GraphView graph = (GraphView) findViewById(R.id.graph);
        graph.setTitle("Weight Over Time in Pounds");



        //initialize buttons that are relevant
        Button squatButton = findViewById(R.id.squatProg);
        Button benchButton = findViewById(R.id.benchProg);
        Button deadButton = findViewById(R.id.deadProg);
        Button overHeadButton = findViewById(R.id.overHeadProg);


        rootRef = FirebaseDatabase.getInstance().getReference();
        mainFunction(graph, mAuth, squatButton, benchButton, deadButton, overHeadButton);


    }


    private ArrayList<Integer> getIntArray(ArrayList<String> stringArray){
        ArrayList<Integer> resultArray = new ArrayList<Integer>();
        stringArray.forEach(item -> {
            try{
                resultArray.add(Integer.parseInt(item));
            }
            catch(NumberFormatException exception){
                Log.w("NumberFormatException", "cannot coerce" + item + "into an int");

            }
    });
        return resultArray;

    }

    private void graphProgress(GraphView graphId, ArrayList<Integer> progressArray, String color,
                               String title){
        if(progressArray.size() < 2 ){
            Toast.makeText(getApplicationContext(), "You need to lift at least twice to " +
                    "see results", Toast.LENGTH_SHORT).show();
        }


        if (progressArray.size() > 0) {
            LineGraphSeries<DataPoint> series = new LineGraphSeries<>();
            for (int i = 0; i < progressArray.size(); i++) {
                DataPoint point = new DataPoint(i, progressArray.get(i));
                series.appendData(point, true, progressArray.size());
            }

            series.setColor(Color.parseColor(color));
            series.setTitle(title);
            graphId.getLegendRenderer().resetStyles();
            graphId.getLegendRenderer().setVisible(true);

            graphId.addSeries(series);
            series.setOnDataPointTapListener(new OnDataPointTapListener() {
                @Override
                public void onTap(Series series, DataPointInterface dataPoint) {
                    Toast.makeText(getApplicationContext(), "weight for" + title + dataPoint, Toast.LENGTH_SHORT).show();
                }
            });

        }


    }

    private void mainFunction(GraphView graph, String mAuth, Button squatButton, Button deadButton,
                              Button benchButton, Button overHeadButton) {


            FirebaseDatabase.getInstance().getReference()
                    .child("users").child(mAuth).
                    child("workouts").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {
                        Log.e("i exist", "existing");

                        for (DataSnapshot parentDS : dataSnapshot.getChildren()) {

                            for (DataSnapshot ds : parentDS.getChildren()) {
                                String key = ds.getKey();
                                switch (key) {
                                    case "Squats": {
                                        String weight = ds.child("weight").getValue().toString();
                                        squatWeight.add(weight);
                                        break;
                                    }
                                    case "Bench": {
                                        String weight = ds.child("weight").getValue().toString();
                                        benchWeight.add(weight);
                                        break;
                                    }
                                    case "Deadlift": {
                                        String weight = ds.child("weight").getValue().toString();
                                        deadWeight.add(weight);
                                        break;
                                    }
                                    case "Overhead Press": {
                                        String weight = ds.child("weight").getValue().toString();
                                        overheadWeight.add(weight);
                                        break;
                                    }
                                }

                            }
                        }

//               getting all the values and converting them to an int
                        ArrayList<Integer> squatInt = getIntArray(squatWeight);
                        ArrayList<Integer> benchInt = getIntArray(benchWeight);
                        ArrayList<Integer> deadInt = getIntArray(deadWeight);
                        ArrayList<Integer> overInt = getIntArray(overheadWeight);

//                onClick for all of the buttons
                        squatButton.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                                if (!clicked) {
                                    graphProgress(graph, squatInt, "green", "Squats");
                                }

                                clicked = true;

                            }
                        });

                        benchButton.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                if (!benchClick) {
                                    graphProgress(graph, benchInt, "blue", "Bench");
                                }
                                benchClick = true;

                            }

                        });

                        deadButton.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                if (!deadClick) {
                                    graphProgress(graph, deadInt, "red", "Deadlift");
                                }
                                deadClick = true;
                            }
                        });

                        overHeadButton.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                if (!overClick) {
                                    graphProgress(graph, overInt, "#CCCCFF", "Press");
                                }
                                overClick = true;
                            }
                        });

                        Log.e("all of the arrays", deadInt.size() + "\n" + overInt.size()
                                + "\n" +
                                squatInt.size() + "\n" + benchInt.size());
                    } else {
                        Log.e("it", "doesn't exist in database");
                    }


                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    Log.d("TAG", databaseError.getMessage());

                }
            });




        }



}
