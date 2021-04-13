package edu.neu.madcourse.gritoria.bosses;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
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
    FirebaseUser currPlayer;
    DatabaseReference gritDB;
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

        //Firebase Realtime DB
        gritFB = FirebaseDatabase.getInstance();

        setupRecyclerView();
        dummyPlayerData();
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

    public void dummyPlayerData() {
        String playerUID = currPlayer.getUid();
        // Check player's ready status for the world.
        gritDB = gritFB.getReference("users/" + playerUID);

        gritDB.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String playerWorld = snapshot.child("currWorld").getValue(String.class);
                Log.e("World", playerWorld);
                playerPower = snapshot.child("power").getValue(Integer.class);
                Log.e("Playerworld", currWorld);
                Log.e("playerPower", String.format("%d",playerPower));
                // TODO: FIX this! problems with adding the same person twice
                if (currWorld.equals(playerWorld) && playerList.contains()) {
                    currPlayerReadyStatus = snapshot.child("isReady").getValue(Boolean.class);
                    playerList.add(new RCViewPlayer(playerUID, playerPower, currPlayerReadyStatus));
                    rcPlayerAdapter.notifyItemInserted(playerList.size() - 1);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void backButtonPress(View v) {
        super.onBackPressed();
    }

    public void setPlayerReady(View v) {
        Toast.makeText(this, String.format("%s",currPlayer.getEmail()), Toast.LENGTH_SHORT).show();
    }

    /**TODO: Fight mechanic:
     * Boss HP + Defense
     * Attack will be time dependent based on team total power
     * Need some kind of server sided timer
     * Progress can be seen by progress bar based on timer
     *
     * **/
}