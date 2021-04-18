package edu.neu.madcourse.gritoria;

import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import java.util.ArrayList;

public class Progress extends AppCompatActivity {

    private DatabaseReference rootRef;
    DatabaseReference reference;
    ArrayList<String> squatWeight = new ArrayList<String>();



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_progress);
        rootRef = FirebaseDatabase.getInstance().getReference();
        FirebaseDatabase.getInstance().getReference()
                .child("users").child("4RX89PfEBUVDkH6FSHogqRse5Q72").
                child("workouts").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for (DataSnapshot parentDS : dataSnapshot.getChildren()) {

                    for (DataSnapshot ds : parentDS.getChildren()) {
                         String key = ds.getKey();
                         if (key.equals("Squats")){
                             String weight = ds.child("weight").getValue().toString();
                             Log.e("squat weight...?", weight);
                         }

                    }
                }

            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.d("TAG", databaseError.getMessage());
            }
        });




        GraphView graph = (GraphView) findViewById(R.id.graph);
    LineGraphSeries<DataPoint> series = new LineGraphSeries<DataPoint>(new DataPoint[] {
            new DataPoint(0, 1),
            new DataPoint(1, 5),
            new DataPoint(2, 3),
            new DataPoint(3, 2),
            new DataPoint(4, 6)
    });
    graph.addSeries(series);
    series.setTitle("This will display in the legend.");


    }

    
}
