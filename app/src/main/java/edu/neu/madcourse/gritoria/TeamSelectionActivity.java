package edu.neu.madcourse.gritoria;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Date;
import java.util.HashMap;

import edu.neu.madcourse.gritoria.messages.BaseMessage;
import edu.neu.madcourse.gritoria.messages.UserMessage;

public class TeamSelectionActivity extends AppCompatActivity {
    private Button createTeamBtn;
    private DatabaseReference mDatabase;
    private TextView teamName;
    private FirebaseAuth mAuth;
    private Button JoinTeamButton;
    private String currUser;
    private Bundle bundle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_team_selection);
        bundle = getIntent().getExtras();
        currUser = bundle.getString("currUser");


        mDatabase = FirebaseDatabase.getInstance().getReference();
        teamName = findViewById(R.id.TeamName);
        mAuth = FirebaseAuth.getInstance();

        createTeamBtn = findViewById(R.id.CreateTeamButton);
        createTeamBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (teamName.getText().length() < 3) {
                    teamName.setError("Team name must be longer than 3 letters.");
                }
                mDatabase.child("teams").child(teamName.getText().toString())
                        .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()) {
                            teamName.setError("Team name has already been taken.");
                        } else {
                            bundle.putString("teamName", teamName.getText().toString());
                            TeamSelectionActivity.this.generateTeam(teamName.getText().toString());
                            TeamSelectionActivity.this.openTeam();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }
        });
        JoinTeamButton = findViewById(R.id.JoinTeamButton);
        TextView teamLink = findViewById(R.id.TeamLink);
        JoinTeamButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (teamLink.getText().length() < 3) {
                    teamLink.setError("Team name must be at least 3 letters.");
                }
                mDatabase.child("teams").child(teamLink.getText().toString())
                        .addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                if (snapshot.exists()) {
                                    HashMap<String, String> memberList = (HashMap<String, String>) snapshot.child("members").getValue();
                                    if (memberList.size() >= 4) {
                                        teamLink.setError("Team is at max capacity.");
                                    } else {
                                        bundle.putString("teamName", teamLink.getText().toString());
                                        HashMap<String, HashMap<String, String>> messageHistory = (HashMap<String, HashMap<String, String>>) snapshot.child("messages").getValue();
                                        TeamSelectionActivity.this.joinTeam(teamLink.getText().toString(), messageHistory.size());
                                        bundle.putSerializable("teamMembers", memberList);

                                        bundle.putSerializable("messages", messageHistory);
                                        TeamSelectionActivity.this.openTeam();
                                    }
                                } else {
                                    teamLink.setError("Team name does not exist");
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });
            }
        });
    }

    protected void generateTeam(String teamNameToAdd) {
        // Initialize team
        mDatabase.child("teams").child(teamNameToAdd).child("currFight").child("isFighting").setValue(false);
        mDatabase.child("teams").child(teamNameToAdd).child("currFight").child("isKilled").setValue(false);
        mDatabase.child("teams").child(teamNameToAdd).child("currFight").child("startTime").setValue(0);
        mDatabase.child("teams").child(teamNameToAdd).child("currFight").child("world").setValue("1-1");
        mDatabase.child("teams").child(teamNameToAdd).child("currFight").child("bossCurrHealth").setValue(0);
        mDatabase.child("teams").child(teamNameToAdd).child("members").child(mAuth.getUid()).setValue(currUser);
        String currMessage = "Team, " + teamNameToAdd + ", has been created.";
        BaseMessage message = new UserMessage("System", currMessage, new Date(), 0);
        HashMap<String, HashMap<String, String>> messageMap = new HashMap<>();
        messageMap.put("firstMessage", (HashMap<String, String>) message.getMessageMap());
        bundle.putSerializable("messages", messageMap);
        mDatabase.child("teams").child(teamNameToAdd).child("messages").push().setValue(message.getMessageMap());
        mDatabase.child("teams").child(teamNameToAdd).child("name").setValue(teamNameToAdd);
        mDatabase.child("teams").child(teamNameToAdd).child("rank").setValue(10);
        mDatabase.child("teams").child(teamNameToAdd).child("teamIcon").setValue("");
        mDatabase.child("teams").child(teamNameToAdd).child("totalPower").setValue(0);

        // Add team to user profile
        mDatabase.child("users").child(mAuth.getUid()).child("team").setValue(teamNameToAdd);
        mDatabase.child("users").child(mAuth.getUid()).child("isLeader").setValue(true);
    }

    protected void joinTeam(String teamNameToJoin, int size){
        mDatabase.child("teams").child(teamNameToJoin).child("members").child(mAuth.getUid()).setValue(currUser);
        mDatabase.child("users").child(mAuth.getUid()).child("team").setValue(teamNameToJoin);
        String currMessage = currUser+ " has joined " + teamNameToJoin + ".";
        BaseMessage message = new UserMessage("System", currMessage, new Date(), size);
        mDatabase.child("teams").child(bundle.getString("teamName")).child("messages").push().setValue(message.getMessageMap());
    }

    protected void openTeam(){
        Intent intent = new Intent(this, Team.class);;
        intent.putExtras(bundle);
        startActivity(intent);
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtras(bundle);
        startActivity(intent);
    }
}