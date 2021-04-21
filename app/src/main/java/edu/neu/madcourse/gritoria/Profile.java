package edu.neu.madcourse.gritoria;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
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
    TextView name;
    Bundle bundle;
    ImageView profileImage;
    FloatingActionButton fab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        mDatabase = FirebaseDatabase.getInstance().getReference();
        mAuth = FirebaseAuth.getInstance();
        EXP = findViewById(R.id.EXP);
        STR = findViewById(R.id.Strength);
        INT = findViewById(R.id.Intelligence);
        DEX = findViewById(R.id.Dexterity);
        Level = findViewById(R.id.Level);
        name = findViewById(R.id.userName);
        bundle = getIntent().getExtras();
        profileImage = findViewById(R.id.profileImage);

        try {
            this.setProfileImage(bundle.getString("profileImage"));
        } catch (Exception e) {
            this.setProfileImage("mage");
        }

        profileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String[] images = {"Mage", "Adventurer", "Clairvoyant", "Necromancer", "Rogue", "Warrior"};
                AlertDialog.Builder builder = new AlertDialog.Builder(Profile.this);
                builder.setTitle("Pick a profile image: ");
                builder.setItems(images, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Profile.this.setProfileImage(images[which]);
                        mDatabase.child("users").child(mAuth.getUid()).child("profileImage").setValue(images[which].toLowerCase());
                    }
                });
                builder.show();
            }
        });

        name.setText(bundle.getString("currUser"));
        HashMap<String, String> statMap = (HashMap<String, String>) bundle.getSerializable("statMap");
        EXP.setText("EXP: " + String.valueOf(statMap.get("exp")));
        INT.setText("Intelligence: " + String.valueOf(statMap.get("int")));
        STR.setText("Strength: " + String.valueOf(statMap.get("str")));
        DEX.setText("Dexterity: " + String.valueOf(statMap.get("dex")));
        Level.setText("Level: " + String.valueOf(statMap.get("level")));


        fab = findViewById(R.id.floatingActionButton);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDatabase.child("users").child(mAuth.getUid()).child("stats").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        HashMap<String, String> statMapFromFB = (HashMap<String, String>) snapshot.getValue();
                        EXP.setText("EXP: " + String.valueOf(statMapFromFB.get("exp")));
                        INT.setText("Intelligence: " + String.valueOf(statMapFromFB.get("int")));
                        STR.setText("Strength: " + String.valueOf(statMapFromFB.get("str")));
                        DEX.setText("Dexterity: " + String.valueOf(statMapFromFB.get("dex")));
                        Level.setText("Level: " + String.valueOf(statMapFromFB.get("level")));
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }
        });

    }

    private void setProfileImage(String setImage){
        if (setImage.equalsIgnoreCase("Mage")) {
            profileImage.setImageResource(R.drawable.mage);
        } else if (setImage.equalsIgnoreCase("Adventurer")) {
            profileImage.setImageResource(R.drawable.adventurer);
        } else if (setImage.equalsIgnoreCase("Clairvoyant")) {
            profileImage.setImageResource(R.drawable.clairvoyant);
        } else if (setImage.equalsIgnoreCase("Necromancer")) {
            profileImage.setImageResource(R.drawable.necromancer);
        } else if (setImage.equalsIgnoreCase("Rogue")) {
            profileImage.setImageResource(R.drawable.rogue);
        } else {
            profileImage.setImageResource(R.drawable.warrior);
        }
        bundle.putString("profileImage", setImage.toLowerCase());
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtras(bundle);
        startActivity(intent);
    }
}