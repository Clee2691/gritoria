package edu.neu.madcourse.gritoria;

import android.content.DialogInterface;
import android.content.Intent;
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

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatSpinner;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class LiftingInfo extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemSelectedListener {

    LinearLayout liftList;
    Button addButton;
    List<Integer> numberList = new ArrayList<>();
    private DatabaseReference rootRef;
    FirebaseStorage userStore;
    DatabaseReference userStoreRef;
    ArrayList<Integer> sets = new ArrayList<Integer>();
    ArrayList<Integer> reps = new ArrayList<Integer>();
    ArrayList<String> liftName;
    ArrayList<String> liftWeight;
    Integer totalVolume;
//    Map<String, Object> updates = new HashMap<String,Object>();
//    updates.put("userid", newID);
//    ref.updateChildren(updates);


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lifting_info);
        liftList = findViewById(R.id.liftList);
        addButton = findViewById(R.id.addLiftButton);
        addButton.setOnClickListener(this);

        //root level , root document this is gritoria
        rootRef = FirebaseDatabase.getInstance().getReference();
//        userStore = FirebaseStorage.getInstance();
        userStoreRef = rootRef.child("users");

        numberList.add(0,0);
        for(int i=1; i<13; i++){
            numberList.add(i);
        }
        Button submit = findViewById(R.id.submitLift);
        //if you press submit you would have to
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int setsSum = getSum(sets);
                int repsSum = getSum(reps);
                AlertDialog alertDialog = new AlertDialog.Builder(LiftingInfo.this).create();
                alertDialog.setMessage("Would you like to save your current progress and exit?");
                alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "yes",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
//                                Log.e("new sets sum is:",String.valueOf(setsSum));
//                                Log.e("new reps sum is:",String.valueOf(repsSum));
                                Toast.makeText(getApplicationContext(),
                                        "progress saved!", Toast.LENGTH_SHORT).show();
                                try {
                                    Log.e("jsonify", jsonify().toString());
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }





//                                rootRef.child("users").child("4RX89PfEBUVDkH6FSHogqRse5Q72").child("reps").setValue(0);

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


    @Override
    public void onClick(View v) {
        addNewView();

    }
    private void addNewView(){
        View liftView = getLayoutInflater().inflate(R.layout.add_lift_row, null, false);
        EditText text = (EditText)liftView.findViewById(R.id.rowEditText);
        String tempText = text.getText().toString();
        EditText weight = (EditText)liftView.findViewById(R.id.weightEdit);
        String weightText = weight.getText().toString();


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



    }

    private void removeCurrentView(View view){
        liftList.removeView(view);
        sets.remove( sets.size() - 1 );
        reps.remove(reps.size()-1);

    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        String text = parent.getItemAtPosition(position).toString();
        int number = Integer.parseInt(text);

        if(position!=0){
            if(parent.getId() == R.id.spinner){
                sets.add(number);
            }
            else if(parent.getId() == R.id.secondSpinner){
                reps.add(number);
            }
        }


    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    public int getSum(ArrayList<Integer> array){
        Integer someSum = array.stream()
                .mapToInt(a -> a)
                .sum();
        return(someSum);
    }

    public void exit(){
        Intent intent = new Intent(this, LiftingActivity.class);
        startActivity(intent);
    }

    public JSONObject jsonify() throws JSONException {

        JSONObject mainObject = new JSONObject();
        JSONObject subKeys = new JSONObject();

        JSONArray array = new JSONArray();

        List<String> setsList = new ArrayList<>();
        setsList.add("3");
        setsList.add("8");
        setsList.add("85");


        List<String> repsList = new ArrayList<>();
        repsList.add("4");
        repsList.add("12");
        repsList.add("135");

        List<String> weightList = new ArrayList<>();
        weightList.add("5");
        weightList.add("5");
        weightList.add("225");

        List<String>[] arrayOfList = new List[3];
        arrayOfList[0] = setsList;
        arrayOfList[1] = repsList;
        arrayOfList[2] = weightList;

        JSONArray output = new JSONArray();


        for (int i = 0; i < arrayOfList.length; i++) {
                List<String> l = arrayOfList[i];
                Log.e("l is", l.toString());
                Log.e("zero", l.get(0));
                Log.e("one", l.get(1));
                Log.e("two", l.get(2));
                JSONObject temp = new JSONObject();
                temp.put("sets", l.get(0));
                temp.put("reps", l.get(1));
                temp.put("weight", l.get(2));

                output.put(temp);

        }

        Log.e("array is... ", output.get(0).toString());

        for(int i = 0; i < output.length(); i++) {
            Log.e("output i value is", output.get(i).toString());
        }




            //adds outterkey
        ArrayList<String> names = new ArrayList<String>();

        names.add("Java");
        names.add("Kotlin");
        names.add("Android");

        names.forEach(name ->{
            try {
                mainObject.put(name,output.toString());
            } catch (JSONException e) {
                e.printStackTrace();
            }
        });


        return mainObject;
    }




}
