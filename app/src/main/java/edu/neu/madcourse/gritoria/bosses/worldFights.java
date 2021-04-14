package edu.neu.madcourse.gritoria.bosses;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
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
import java.time.Instant;

import edu.neu.madcourse.gritoria.R;
import edu.neu.madcourse.gritoria.rcViewPlayer.RCViewPlayer;
import edu.neu.madcourse.gritoria.rcViewPlayer.RCAdapter;

import static java.lang.Thread.sleep;

public class worldFights extends AppCompatActivity {
    private Intent currIntent;
    private List<RCViewPlayer> playerList;
    private RecyclerView rcPlayers;
    private RCAdapter rcPlayerAdapter;
    private String currWorld;
    private boolean currPlayerReadyStatus;
    private List<String> teammateUIDList;
    private String playerUID;
    private long currTimeInEpoch;
    private boolean playerIsFighting;
    private String playerWorld;
    private String playerTeam;
    Handler uiHandler;
    Boss currBoss;
    FirebaseUser currPlayer;
    FirebaseDatabase gritFB;

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
        uiHandler = new Handler();
        setupRecyclerView();
        Button btnRefresh = findViewById(R.id.buttonRefreshTeam);
        refreshTeam(btnRefresh);
        // Get the time in seconds
        determineTime();
        // Sets up "ready" button based on if the logged in player is ready or not
        setupUI();
        currBoss = new Boss();
        getBossInfo();
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
        Button attackBut = findViewById(R.id.buttonAttack);
        DatabaseReference userRef = gritFB.getReference("users/" + playerUID);

        userRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                currPlayerReadyStatus = snapshot.child("isReady").getValue(boolean.class);
                playerWorld = snapshot.child("currWorld").getValue(String.class);
                playerIsFighting = snapshot.child("isFighting").getValue(boolean.class);

                // Sets the ready button for player depending on world/ready status
                if (playerWorld.equals(currWorld) && playerIsFighting == false) {
                    if(currPlayerReadyStatus) {
                        readyButton.setText("Cancel");
                        readyButton.setBackgroundColor(Color.RED);
                    } else {
                        readyButton.setText("Ready Up");
                        readyButton.setBackgroundColor(Color.rgb(97,248, 15));
                    }
                } else if (!playerWorld.equals(currWorld) && playerIsFighting == true) {
                    readyButton.setEnabled(false);
                } else if (!playerWorld.equals(currWorld)) {
                    readyButton.setText("Fight Here");
                } else if (playerWorld.equals(currWorld)  && playerIsFighting== true) {
                    readyButton.setEnabled(false);
                }

                // Sets the button up if you're leader to allow attacking of boss
                if (snapshot.child("isLeader").getValue(boolean.class)) {
                    attackBut.setVisibility(View.VISIBLE);
                    attackBut.setEnabled(true);
                } else {
                    attackBut.setEnabled(false);
                    attackBut.setVisibility(View.GONE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(worldFights.this, "Error: " + error.getMessage(),
                        Toast.LENGTH_SHORT).show();
            }
        });
        determineBossHealth();
    }
    class Boss {
        String name;
        int health;
        boolean isKilled;
        long startTime;

        public Boss() {
            this.name = "";
            this.health = 0;
            this.isKilled = false;
            this.startTime = Instant.now().getEpochSecond();
        }

        public void setName(String name) {
            this.name = name;
        }

        public void setHealth(int health) {
            this.health = health;
        }

        public String getName() {
            return name;
        }

        public boolean isKilled() {
            return isKilled;
        }

        public long getStartTime() {
            return startTime;
        }

        public int getHealth() {
            return this.health;
        }
    }

    private void getBossInfo() {

        if (playerIsFighting && playerWorld.equals(currWorld)){
            DatabaseReference currFightRef = gritFB.getReference("teams/" + playerTeam + "/currFight/");
            currFightRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    String worldName = snapshot.child("world").getValue(String.class);
                    int bossHealth = snapshot.child("bossHealth").getValue(Integer.class);
                    currBoss.setName(worldName);
                    currBoss.setHealth(bossHealth);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });

        } else {
            DatabaseReference bossInfo = gritFB.getReference("");
        }

    }

    private void determineTime() {
        timeThread getTime = new timeThread();
        new Thread(getTime).start();
    }

    class timeThread implements Runnable {
        @Override
        public void run() {
            while(true) {
                try {
                    currTimeInEpoch = Instant.now().getEpochSecond();
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void determineBossHealth() {
        ProgressBar bossHealth = findViewById(R.id.progressBarBossHealth);
        TextView bossHealthNum = findViewById(R.id.textViewBossHealth);
        int healthLeft = 54;
        bossHealth.setProgress(healthLeft);
        bossHealthNum.setText(String.format("Health: %d/100", healthLeft));
    }

    public void startAttack(View v) {
        boolean canAttack = determineIfReady();
        int bossHealth = 100;

        // TODO: Probably need to thread this
        if (canAttack) {

        }
    }

    private boolean determineIfReady() {
        for (RCViewPlayer player : playerList) {
            if (!player.isReady()) {
                Toast.makeText(this, "All players must be ready before attacking",
                        Toast.LENGTH_SHORT).show();
                return false;
            }
        }
        return true;
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
        playerTeam = dSS.child("users").child(playerUID).child("team")
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
        Button readyButton = findViewById(v.getId());
        if (readyButton.getText().equals("Fight Here")) {
            userRef.child("isReady").setValue(true);
            userRef.child("currWorld").setValue(currWorld);
        } else if (readyButton.getText().equals("Ready Up")) {
            userRef.child("isReady").setValue(true);
        } else if (readyButton.getText().equals("Cancel")) {
            userRef.child("isReady").setValue(false);
        }
    }

    /**TODO: Fight mechanic:
     * Boss HP + Defense
     * Attack will be time dependent based on team total power
     * Need some kind of server sided timer
     * Progress can be seen by progress bar based on timer
     *
     * **/
}