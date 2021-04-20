package edu.neu.madcourse.gritoria;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class Profile extends AppCompatActivity {
    DatabaseReference mDatabase;
    FirebaseAuth mAuth;
    TextView EXP;
    TextView STR;
    TextView INT;
    TextView DEX;
    TextView Level;
    EditText name;
    Bundle bundle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        EXP = findViewById(R.id.EXP);
        STR = findViewById(R.id.Strength);
        INT = findViewById(R.id.Intelligence);
        DEX = findViewById(R.id.Dexterity);
        Level = findViewById(R.id.Level);
        name = findViewById(R.id.editTextTextPersonName);
        bundle = getIntent().getExtras();

        name.setText(bundle.getString("currUser"));
        HashMap<String, String> statMap = (HashMap<String, String>) bundle.getSerializable("statMap");
        EXP.setText("EXP: " + String.valueOf(statMap.get("exp")));
        INT.setText("Intelligence: " + String.valueOf(statMap.get("int")));
        STR.setText("Strength: " + String.valueOf(statMap.get("str")));
        DEX.setText("Dexterity: " + String.valueOf(statMap.get("dex")));
        Level.setText("Level: " + String.valueOf(statMap.get("level")));

        mDatabase = FirebaseDatabase.getInstance().getReference();
        mAuth = FirebaseAuth.getInstance();
    }
}