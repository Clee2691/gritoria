package edu.neu.madcourse.gritoria;

import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;

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

        //initialize graph view by id
        GraphView graph = (GraphView) findViewById(R.id.graph);



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
                             Log.e("weight", weight);
                             squatWeight.add(weight);


                         }

                    }
                }

                Log.e("lets look at squatWeight array...", squatWeight.toString());
                ArrayList<Integer> squatMapArray = getIntArray(squatWeight);

                Log.e("trying same thing but with myValue", squatMapArray.toString());
//                ArrayList<Integer> squatIntWeight =  getIntegerArray(squatWeight);
                if (squatMapArray.size() > 0) {
                    LineGraphSeries<DataPoint> series = new LineGraphSeries<>();
                    for (int i = 0; i < squatMapArray.size(); i++) {
                        DataPoint point = new DataPoint(i, squatMapArray.get(i));
                        series.appendData(point, true, squatMapArray.size());
                    }
                    graph.addSeries(series);
                }



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



}
