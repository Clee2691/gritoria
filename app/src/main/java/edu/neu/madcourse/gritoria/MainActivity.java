package edu.neu.madcourse.gritoria;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.File;
import java.util.HashMap;

import com.google.firebase.auth.FirebaseAuth;
import java.io.File;

public class MainActivity extends AppCompatActivity {
    FirebaseAuth mAuth;
    DatabaseReference mDatabase;
    Bundle bundle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAuth = FirebaseAuth.getInstance();
        setContentView(R.layout.activity_main);
        bundle = getIntent().getExtras();
        String currentUser = bundle.getString("currUser");

        mDatabase = FirebaseDatabase.getInstance().getReference();
        mAuth = FirebaseAuth.getInstance();
        Log.e("log:", "name: " + currentUser + " token: " +  mAuth.getUid());
        Intent intent = new Intent(getApplicationContext(), Progress.class);
        intent.putExtra("tokenID", mAuth.getUid());
        startActivity(intent);

        Intent secondIntent = new Intent(getApplicationContext(), LiftingStaticView.class);
        intent.putExtra("tokenId", mAuth.getUid());
        startActivity(secondIntent);


        Button team = findViewById(R.id.TeamButton);
        team.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openTeam(currentUser);
            }
        });

        Button profile = findViewById(R.id.ProfileButton);
        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openProfile(currentUser);
            }
        });

        Button map = findViewById(R.id.MapButton);
        map.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openMap(currentUser);
            }
        });

        Button runButton = findViewById(R.id.RunButton);
        runButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openRun(currentUser);
            }
        });

        Button liftingButton = findViewById(R.id.liftButton);
        liftingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openLift();
            }
        });

        Button logoutButton = findViewById(R.id.Logout);
        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAuth.signOut();
                startActivity(new Intent(getApplicationContext(), LoginActivity.class));
                finish();
            }
        });


    }

    // Open new activities on button press

    public void openTeam(String currentUser){
        Intent intent = new Intent(this, Team.class);
        intent.putExtra("currUser", currentUser);
        intent.putExtra("teamName", bundle.getString("teamName"));
        bundle.getSerializable("teamMembers");
        intent.putExtra("teamMembers", bundle.getSerializable("teamMembers"));

        mDatabase.child("teams").child(bundle.getString("teamName")).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                bundle.putSerializable("messages" ,(HashMap<String, HashMap<String, String>>) snapshot.child("messages").getValue());
                intent.putExtras(bundle);
                startActivity(intent);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }


    public void openProfile(String currentUser){
        Intent intent = new Intent(this, Profile.class);
        intent.putExtra("currUser", currentUser);
        intent.putExtras(bundle);
        startActivity(intent);
    }

    public void openMap(String currentUser){
        Intent intent = new Intent(this, Map.class);
        intent.putExtra("currUser", currentUser);
        intent.putExtras(bundle);
        startActivity(intent);
    }

    public void openRun(String currentUser) {
        Intent intent = new Intent(this, RunningActivity.class);
        intent.putExtra("currUser", currentUser);
        intent.putExtras(bundle);
        startActivity(intent);
    }
    public void openLift() {
        Intent intent = new Intent(this, LiftingActivity.class);
        startActivity(intent);
    }

}