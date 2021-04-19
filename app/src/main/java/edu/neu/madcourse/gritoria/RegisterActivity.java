package edu.neu.madcourse.gritoria;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

public class RegisterActivity extends AppCompatActivity {
    EditText mUser, mEmail, mPassword, mVerifyPassword;
    Button registerButton;
    TextView loginLink;
    FirebaseAuth mAuth;
    ProgressBar progressBar;
    DatabaseReference mDatabase;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mDatabase = FirebaseDatabase.getInstance().getReference();

        setContentView(R.layout.activity_register);
        mUser = findViewById(R.id.username);
        mEmail = findViewById(R.id.email);
        mPassword = findViewById(R.id.password);
        mVerifyPassword = findViewById(R.id.password2);
        progressBar = findViewById(R.id.progressBar);
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


                    Intent intent = new Intent(RegisterActivity.this, MainActivity.class);

                    intent.putExtra("currUser", currentUser);
                    intent.putExtra("statMap", statMap);
                    intent.putExtra("teamName", teamName);
                    intent.putExtra("teamMembers", teamMembers);
                    startActivity(intent);
                    finish();
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }
        loginLink = findViewById(R.id.loginPage);
        loginLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), LoginActivity.class));
            }
        });

        registerButton = findViewById(R.id.registerButton);
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = mEmail.getText().toString().trim();
                String password = mPassword.getText().toString().trim();
                String verifyPassword = mVerifyPassword.getText().toString().trim();
                String username = mUser.getText().toString();
                if (TextUtils.isEmpty(email)) {
                    mEmail.setError("Email can not be empty");
                } else if (TextUtils.isEmpty(password) || password.length() < 6) {
                    mPassword.setError("Password must be at least 6 characters long");
                } else if (!password.equals(verifyPassword)){
                    mVerifyPassword.setError("Passwords do not match");
                } else if(username.isEmpty() || username.length() < 4) {
                    mUser.setError("Username must be at least 4 characters long");
                } else {
                    progressBar.setVisibility(View.VISIBLE);
                    mDatabase.child("taken names").child(username).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if(snapshot.exists()){
                                mUser.setError("Username already taken");
                            } else {
                                mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                    @Override
                                    public void onComplete(@NonNull Task<AuthResult> task) {
                                        if (task.isSuccessful()) {
                                            progressBar.setVisibility(View.INVISIBLE);
                                            mAuth.signInWithEmailAndPassword(email, password);
                                            RegisterActivity.this.generateUser(mDatabase, username);
                                            Toast.makeText(RegisterActivity.this, "User created", Toast.LENGTH_SHORT);
                                            startActivity(new Intent(getApplicationContext(), MainActivity.class));
                                        }
                                        else {
                                            Toast.makeText(RegisterActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT);
                                        }
                                    }
                                });
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                }
            }
        });
    }


    private void generateUser(DatabaseReference postRef, String username){
        // Initalize user with their stats
        // Level, str, int, dex, and exp
        postRef.child("users").child(mAuth.getCurrentUser().getUid()).child("username").setValue(username);
        Map<String, Integer> userUpdates = new HashMap<>();
        userUpdates.put("level", 1);
        userUpdates.put("str", 1);
        userUpdates.put("int", 1);
        userUpdates.put("dex", 1);
        userUpdates.put("exp", 1);
        postRef.child("users").child(mAuth.getCurrentUser().getUid()).child("stats").setValue(userUpdates);
        postRef.child("taken names").child(username).setValue("true");
        postRef.child("users").child(mAuth.getCurrentUser().getUid()).child("currWorld").setValue("");
        postRef.child("users").child(mAuth.getCurrentUser().getUid()).child("isFighting").setValue(false);
        postRef.child("users").child(mAuth.getCurrentUser().getUid()).child("isLeader").setValue(false);
        postRef.child("users").child(mAuth.getCurrentUser().getUid()).child("isReady").setValue(false);
        postRef.child("users").child(mAuth.getCurrentUser().getUid()).child("team").setValue("");
    }
}