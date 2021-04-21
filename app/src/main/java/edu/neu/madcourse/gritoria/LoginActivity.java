package edu.neu.madcourse.gritoria;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
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

public class LoginActivity extends AppCompatActivity {
    EditText mEmail, mPassword;
    Button loginButton;
    TextView registerLink;
    FirebaseAuth mAuth;
    ProgressBar progressBar;
    DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mEmail = findViewById(R.id.email);
        mPassword = findViewById(R.id.password);
        progressBar = findViewById(R.id.progressBar2);
        registerLink = findViewById(R.id.registerPage);
        registerLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), RegisterActivity.class));
            }
        });
        mDatabase = FirebaseDatabase.getInstance().getReference();
        mAuth = FirebaseAuth.getInstance();
        if (mAuth.getCurrentUser() != null) {
            DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
            mAuth = FirebaseAuth.getInstance();
            mDatabase.child("users").child(mAuth.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    String currentUser = (String) snapshot.child("username").getValue();
                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                    intent.putExtra("currUser", currentUser);
                    startActivity(intent);
                    finish();
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }
        loginButton = findViewById(R.id.loginButton);
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = mEmail.getText().toString().trim();
                String password = mPassword.getText().toString().trim();
                if (TextUtils.isEmpty(email)) {
                    mEmail.setError("Email can not be empty");
                } else if (TextUtils.isEmpty(password) || password.length() < 6) {
                    mPassword.setError("Password must be at least 6 characters long");
                } else {
                    progressBar.setVisibility(View.VISIBLE);
                    // authenticate user
                    mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                mAuth = FirebaseAuth.getInstance();
                                mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
                                    // Initialize data for user.
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                        String currentUser = (String) snapshot.child("users").child(mAuth.getUid()).child("username").getValue();
                                        HashMap<String, String> statMap = (HashMap<String, String>) snapshot.child("users").child(mAuth.getUid()).child("stats").getValue();
                                        String teamName = (String) snapshot.child("users").child(mAuth.getUid()).child("team").getValue();
                                        HashMap<String, String> teamMembers = (HashMap<String, String>) snapshot.child("teams").child(teamName).child("members").getValue();


                                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);

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
                            else {
                                Toast.makeText(LoginActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT);
                            }
                        }
                    });
                }
            }
        });
    }
}