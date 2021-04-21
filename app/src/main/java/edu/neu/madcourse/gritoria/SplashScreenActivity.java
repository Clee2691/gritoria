package edu.neu.madcourse.gritoria;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ProgressBar;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class SplashScreenActivity extends AppCompatActivity {
    FirebaseAuth mAuth;
    ProgressBar progressBar;
    DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        mDatabase = FirebaseDatabase.getInstance().getReference();
        mAuth = FirebaseAuth.getInstance();
        if (mAuth.getCurrentUser() != null) {
            DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
            mAuth = FirebaseAuth.getInstance();
            mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
                // Initialize data for user.
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    String currentUser = (String) snapshot.child("users").child(mAuth.getUid()).child("username").getValue();
                    HashMap<String, String> statMap = (HashMap<String, String>) snapshot.child("users").child(mAuth.getUid()).child("stats").getValue();
                    String teamName = (String) snapshot.child("users").child(mAuth.getUid()).child("team").getValue();
                    HashMap<String, String> teamMembers = (HashMap<String, String>) snapshot.child("teams").child(teamName).child("members").getValue();
                    String profileImage = (String) snapshot.child("users").child(mAuth.getUid()).child("profileImage").getValue();

                    Intent intent = new Intent(SplashScreenActivity.this, MainActivity.class);
                    intent.putExtra("currUser", currentUser);
                    intent.putExtra("statMap", statMap);
                    intent.putExtra("teamName", teamName);
                    intent.putExtra("teamMembers", teamMembers);
                    intent.putExtra("profileImage", profileImage);
                    startActivity(intent);
                    finish();
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        } else {
            Intent intent = new Intent(this, RegisterActivity.class);
            startActivity(intent);
        }
    }
}