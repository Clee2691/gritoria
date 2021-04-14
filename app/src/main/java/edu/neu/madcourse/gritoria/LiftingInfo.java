package edu.neu.madcourse.gritoria;

import android.media.Image;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatSpinner;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class LiftingInfo extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemSelectedListener {

    LinearLayout liftList;
    Button addButton;
    List<Integer> numberList = new ArrayList<>();
    private DatabaseReference rootRef;
    FirebaseStorage userStore;
    StorageReference userStoreRef;
    ArrayList<Integer> sets = new ArrayList<Integer>();
    ArrayList<Integer> reps = new ArrayList<Integer>();



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lifting_info);
        liftList = findViewById(R.id.liftList);
        addButton = findViewById(R.id.addLiftButton);
        addButton.setOnClickListener(this);

        rootRef = FirebaseDatabase.getInstance().getReference();
        userStore = FirebaseStorage.getInstance();
        userStoreRef = userStore.getReference();

        numberList.add(0,0);
        for(int i=1; i<13; i++){
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

        spinner.setOnItemSelectedListener(this);
        secondSpinner.setOnItemSelectedListener(this);


        secondSpinner.setAdapter(secondAdapter);

        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                removeCurrentView(liftView);
            }
        });

        liftList.addView(liftView);
        Log.e("final number is: ", sets.toString());

    }

    private void removeCurrentView(View view){
        liftList.removeView(view);
        sets.remove( sets.size() - 1 );

    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        String text = parent.getItemAtPosition(position).toString();
        int number = Integer.parseInt(text);

        if(parent.getId() == R.id.spinner){

        }


        if(position!=0){
            Log.e("number  is", sets.toString());
            sets.add(number);

        }

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

}
