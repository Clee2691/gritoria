package edu.neu.madcourse.gritoria.bosses;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import edu.neu.madcourse.gritoria.R;
import edu.neu.madcourse.gritoria.rcViewPlayer.RCViewPlayer;
import edu.neu.madcourse.gritoria.rcViewPlayer.RCAdapter;

public class worldFights extends AppCompatActivity {
    private Intent currIntent;
    private List<RCViewPlayer> playerList;
    private RecyclerView rcPlayers;
    private RCAdapter rcPlayerAdapter;
    private String currWorld;
    private int playerPower;
    private boolean currPlayerReadyStatus;
    private List<String> teammateUIDList;
    private String playerUID;
    FirebaseUser currPlayer;
    FirebaseDatabase gritFB;


    // TODO: Get team curr player is in and get team members
    // See if they are ready for this fight
    // Set curr player to the world if they are ready

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_world_fights);
        currIntent = getIntent();
        setupWorld(currIntent);
        playerList = new ArrayList<>();
        currPlayer = FirebaseAuth.getInstance().getCurrentUser();
        playerUID = currPlayer.getUid();
        //Firebase Realtime DB
        gritFB = FirebaseDatabase.getInstance();
        setupRecyclerView();

        setupUI();
    }

    private void setupWorld(Intent currIntent) {
        TextView worldLogo = findViewById(R.id.textViewWorld);
        currWorld = currIntent.getStringExtra("level");
        worldLogo.setText(String.format("World %s", currWorld));

    }

    private void setupRecyclerView() {
        rcPlayers = findViewById(R.id.recyclerViewTeamReady);
        rcPlayers.setHasFixedSize(true);
        rcPlayerAdapter = new RCAdapter(playerList);
        rcPlayers.setAdapter(rcPlayerAdapter);
        rcPlayers.setLayoutManager(new LinearLayoutManager(this));
    }

    private void setupUI() {
        Button readyButton = findViewById(R.id.btnReadyUp);
        DatabaseReference userRef = gritFB.getReference("users/" + playerUID);

        userRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                currPlayerReadyStatus = snapshot.child("isReady").getValue(boolean.class);
                Log.e("Player ready", String.valueOf(currPlayerReadyStatus));

                if(currPlayerReadyStatus) {
                    readyButton.setText("Cancel");
                    readyButton.setBackgroundColor(Color.RED);
                } else {
                    readyButton.setText("Ready Up");
                    readyButton.setBackgroundColor(Color.rgb(97,248, 15));
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(worldFights.this, "Error: " + error.getMessage(),
                        Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void refreshTeam(View v) {
        DatabaseReference gritdbRef = gritFB.getReference();

        gritdbRef.addValueEventListener( new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                playerList.clear();
                rcPlayerAdapter.notifyDataSetChanged();
                populateUserTeamRecyclerView(snapshot);

                for(DataSnapshot aUser : snapshot.child("users/").getChildren()) {
                    if (teammateUIDList.contains(aUser.getKey())) {
                        if (aUser.child("currWorld").getValue(String.class).equals(currWorld)) {
                            playerList.add(new RCViewPlayer(
                                    aUser.child("name").getValue(String.class),
                                    aUser.child("power").getValue(Integer.class),
                                    aUser.child("isReady").getValue(boolean.class)));
                            rcPlayerAdapter.notifyItemInserted(playerList.size() - 1);
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(worldFights.this, "Error: " + error.getMessage(),
                        Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void populateUserTeamRecyclerView(DataSnapshot dSS) {
        teammateUIDList = new ArrayList<>();
        String playerTeam = dSS.child("users").child(playerUID).child("team")
                .getValue(String.class);

        for (DataSnapshot teamMembers : dSS.child("teams").child(playerTeam)
                .child("members").getChildren()) {
            teammateUIDList.add(teamMembers.getKey());
        }
    }

    public void backButtonPress(View v) {
        super.onBackPressed();
    }

    public void setPlayerReady(View v) {
        DatabaseReference userRef = gritFB.getReference("users/" + playerUID);
        userRef.child("isReady").setValue(!currPlayerReadyStatus);
    }

    /**TODO: Fight mechanic:
     * Boss HP + Defense
     * Attack will be time dependent based on team total power
     * Need some kind of server sided timer
     * Progress can be seen by progress bar based on timer
     *
     * **/
}