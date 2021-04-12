package edu.neu.madcourse.gritoria;

import android.media.Image;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatSpinner;

import java.util.ArrayList;
import java.util.List;

public class LiftingInfo extends AppCompatActivity implements View.OnClickListener {

    LinearLayout list;
    Button addButton;
    List<String> numberList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lifting_info);
        list = findViewById(R.id.liftList);
        addButton = findViewById(R.id.add);
        addButton.setOnClickListener(this);

        numberList.add("1");
        numberList.add("2");
        numberList.add("3");
        numberList.add("4");

    }


    @Override
    public void onClick(View v) {
        addNewView();

    }
    private void addNewView(){
        View liftView = getLayoutInflater().inflate(R.layout.add_lift_row, null, false);
        EditText text = (EditText)liftView.findViewById(R.id.rowEditText);
        AppCompatSpinner  spinner = (AppCompatSpinner)liftView.findViewById(R.id.spinner);
        ImageView close = (ImageView)liftView.findViewById(R.id.removeImageView);
    }

    private void removeCurrentView(View view){

    }
}
