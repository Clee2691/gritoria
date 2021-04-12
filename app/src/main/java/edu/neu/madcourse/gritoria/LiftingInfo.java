package edu.neu.madcourse.gritoria;

import android.media.Image;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatSpinner;

import java.util.ArrayList;
import java.util.List;

public class LiftingInfo extends AppCompatActivity implements View.OnClickListener {

    LinearLayout liftList;
    Button addButton;
    List<Integer> numberList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lifting_info);
        liftList = findViewById(R.id.liftList);
        addButton = findViewById(R.id.addLiftButton);
        addButton.setOnClickListener(this);


        for(int i=0; i<13; i++){
            numberList.add(i);
        }


    }


    @Override
    public void onClick(View v) {
        addNewView();

    }
    private void addNewView(){
        View liftView = getLayoutInflater().inflate(R.layout.add_lift_row, null, false);
        EditText text = (EditText)liftView.findViewById(R.id.rowEditText);
        EditText weight = (EditText)liftView.findViewById(R.id.weightEdit);

        AppCompatSpinner  spinner = (AppCompatSpinner)liftView.findViewById(R.id.spinner);
        AppCompatSpinner  secondSpinner = (AppCompatSpinner)liftView.findViewById(R.id.secondSpinner);
        ImageView close = (ImageView)liftView.findViewById(R.id.removeImageView);

        ArrayAdapter adapter = new ArrayAdapter(this,android.R.layout.simple_spinner_item, numberList);
        ArrayAdapter secondAdapter = new ArrayAdapter(this,android.R.layout.simple_spinner_item, numberList);

        spinner.setAdapter(adapter);
        secondSpinner.setAdapter(secondAdapter);

        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                removeCurrentView(liftView);
            }
        });

        liftList.addView(liftView);

    }

    private void removeCurrentView(View view){
        liftList.removeView(view);

    }
}
