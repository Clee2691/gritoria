package edu.neu.madcourse.gritoria;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.common.graph.Graph;
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


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_progress);



        //initialize graph view by id and setting it's title
        GraphView graph = (GraphView) findViewById(R.id.graph);
        graph.setTitle("Weight Over Time in Pounds");




        //initialize buttons that are relevant
        Button squatButton = findViewById(R.id.squatProg);
        Button benchButton = findViewById(R.id.benchProg);
        Button deadButton = findViewById(R.id.deadProg);
        Button overHeadButton = findViewById(R.id.overHeadProg);


        rootRef = FirebaseDatabase.getInstance().getReference();
        FirebaseDatabase.getInstance().getReference()
                .child("users").child("4RX89PfEBUVDkH6FSHogqRse5Q72").
                child("workouts").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

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
                        graphProgress(graph, squatInt, "green") ;
                    }
                });

                benchButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        graphProgress(graph, benchInt, "red");
                    }
                });

                deadButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        graphProgress(graph, deadInt, "blue");
                    }
                });

                overHeadButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        graphProgress(graph, overInt, "cyan");
                    }
                });



            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.d("TAG", databaseError.getMessage());
            }
        });

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

    private void graphProgress(GraphView graphId, ArrayList<Integer> progressArray, String color ){

        if (progressArray.size() > 0) {
            LineGraphSeries<DataPoint> series = new LineGraphSeries<>();
            for (int i = 0; i < progressArray.size(); i++) {
                DataPoint point = new DataPoint(i, progressArray.get(i));
                series.appendData(point, true, progressArray.size());
            }
            series.setOnDataPointTapListener(new OnDataPointTapListener() {
                @Override
                public void onTap(Series series, DataPointInterface dataPoint) {
                    Toast.makeText(getApplicationContext(), "Weight at Day"+dataPoint, Toast.LENGTH_SHORT).show();
                }
            });
            series.setColor(Color.parseColor(color));

            graphId.addSeries(series);
        }

    }




}
