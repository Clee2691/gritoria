package edu.neu.madcourse.gritoria.bosses;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.MutableData;
import com.google.firebase.database.Transaction;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.time.Instant;

import edu.neu.madcourse.gritoria.R;
import edu.neu.madcourse.gritoria.rcViewPlayer.RCViewPlayer;
import edu.neu.madcourse.gritoria.rcViewPlayer.RCAdapter;

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
    private long bossStartTime;
    private int bossHealth;
    private boolean teamIsFighting;
    private boolean isLeader;
    Handler uiHandler;
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
        refreshTeam();
        // Sets up "ready" button based on if the logged in player is ready or not
        setupUI();
    }

    private void setupWorld(Intent currIntent) {
        TextView worldLogo = findViewById(R.id.textViewWorld);
        ProgressBar bossProgBar = findViewById(R.id.progressBarBossHealth);
        TextView bossHealthDisplay = findViewById(R.id.textViewBossHealth);
        TextView timeLeftDisplay = findViewById(R.id.textViewTimeLeftVal);

        currWorld = currIntent.getStringExtra("level");
        playerTeam = currIntent.getStringExtra("playerTeam");
        bossHealth = currIntent.getIntExtra("bossHealth", 0);
        teamIsFighting = currIntent.getBooleanExtra("isTeamFighting", false);
        worldLogo.setText(String.format("World %s", currWorld));
        bossHealthDisplay.setText(String.format("Health: %d/%d", bossHealth, bossHealth));
        bossProgBar.setMax(bossHealth);
        bossProgBar.setProgress(bossHealth);
        timeLeftDisplay.setText(String.format("%d seconds left", bossHealth));

        View v = findViewById(R.id.world_fight_activity);
        ImageView bossAvatar = findViewById(R.id.imageViewBossAvatar);

        if (currWorld.equals("1-1")) {
            v.setBackground(ContextCompat.getDrawable(this, R.drawable.boss_background1));
            bossAvatar.setImageResource(R.drawable.boss1);
        } else if (currWorld.equals("1-2")) {
            v.setBackground(ContextCompat.getDrawable(this, R.drawable.boss_bg2));
            bossAvatar.setImageResource(R.drawable.boss2);
        } else if (currWorld.equals("1-3")) {
            v.setBackground(ContextCompat.getDrawable(this, R.drawable.boss_bg3));
            bossAvatar.setImageResource(R.drawable.boss3);
        } else if (currWorld.equals("1-4")) {
            v.setBackground(ContextCompat.getDrawable(this, R.drawable.boss_bg4));
            bossAvatar.setImageResource(R.drawable.boss4);
        } else if (currWorld.equals("1-5")) {
            v.setBackground(ContextCompat.getDrawable(this, R.drawable.boss_bg5));
            bossAvatar.setImageResource(R.drawable.boss5);
        }
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
        DatabaseReference dbRef = gritFB.getReference();

        dbRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                DataSnapshot userRef = snapshot.child("users").child(playerUID);
                DataSnapshot teamRef = snapshot.child("teams").child(playerTeam).child("currFight");
                currPlayerReadyStatus = userRef.child("isReady").getValue(boolean.class);
                playerWorld = userRef.child("currWorld").getValue(String.class);
                playerIsFighting = userRef.child("isFighting").getValue(boolean.class);
                isLeader = userRef.child("isLeader").getValue(boolean.class);

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
                if (isLeader && !teamIsFighting) {
                    attackBut.setVisibility(View.VISIBLE);
                    attackBut.setEnabled(true);
                } else if (!isLeader){
                    attackBut.setEnabled(false);
                    attackBut.setVisibility(View.GONE);
                }

                if (teamIsFighting &&
                        teamRef.child("world").getValue(String.class).equals(currWorld)) {
                    bossStartTime = teamRef.child("startTime").getValue(long.class);
                    determineTime();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(worldFights.this, "Error: " + error.getMessage(),
                        Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void determineTime() {
        timeThread getTime = new timeThread();
        new Thread(getTime).start();
    }

    // Logic for "idle" part of the game
    // Calculates the current time with when the boss time started and determines
    // if the elapsed time is == boss health
    class timeThread implements Runnable {
        long deltaTime;
        int finalTime;
        DatabaseReference teamFight;
        DatabaseReference playerRef;
        TextView bossTimer;
        Button readyButton;

        ProgressBar bossProgBar;
        TextView bossHealthNum;

        @Override
        public void run() {
            bossTimer = findViewById(R.id.textViewTimeLeftVal);
            bossProgBar = findViewById(R.id.progressBarBossHealth);
            bossHealthNum = findViewById(R.id.textViewBossHealth);

            teamFight = gritFB.getReference("teams/" + playerTeam + "/currFight");

            while(teamIsFighting && bossStartTime > 0) {
                try {
                    currTimeInEpoch = Instant.now().getEpochSecond();
                    Thread.sleep(1000);
                    deltaTime= currTimeInEpoch - bossStartTime;
                    finalTime = (int) (bossHealth - deltaTime);
                    uiHandler.post(()-> bossTimer.setText(
                            String.format("%d seconds left", finalTime)));
                    uiHandler.post(()->bossProgBar.setProgress(finalTime));
                    uiHandler.post(()->bossHealthNum.setText(String.format("Health: %d/%d",
                            finalTime, bossHealth)));

                    if (finalTime <= 0) {
                        finishFight();
                        break;
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }

        private void finishFight() {
            readyButton = findViewById(R.id.btnReadyUp);

            uiHandler.post(()-> bossTimer.setText(String.format("Boss Complete!")));
            uiHandler.post(()-> bossHealthNum.setText(String.format("0/%d", bossHealth)));
            teamFight.child("isFighting").setValue(false);
            teamFight.child("startTime").setValue(0);
            teamFight.child("isKilled").setValue((true));

            for (String teamMateUID : teammateUIDList) {
                playerRef = gritFB.getReference("users/" + teamMateUID);
                playerRef.child("isFighting").setValue(false);
                playerRef.child("isReady").setValue(false);
                // Update exp with a transaction to allow adding to the value
                // Also avoids problems with concurrent read/writes from other users
                playerRef.child("stats").child("exp").runTransaction(new Transaction.Handler() {
                    @NonNull
                    @Override
                    public Transaction.Result doTransaction(@NonNull MutableData currentData) {
                        int currExp = currentData.getValue(Integer.class);
                        currExp += bossHealth * 0.15;
                        currentData.setValue(currExp);
                        return Transaction.success(currentData);
                    }

                    @Override
                    public void onComplete(@Nullable DatabaseError error, boolean committed, @Nullable DataSnapshot currentData) {

                    }
                });
            }
            uiHandler.post(()->readyButton.setEnabled(true));
            if(isLeader) {
                Button attackButton = findViewById(R.id.buttonAttack);
                uiHandler.post(()->attackButton.setVisibility(View.VISIBLE));
                uiHandler.post(()->attackButton.setEnabled(true));
            }
        }
    }

    public void startAttack(View v) {
        boolean canAttack = determineIfReady();
        DatabaseReference currFightRef = gritFB.getReference("teams/" + playerTeam +
                                                            "/currFight");
        DatabaseReference userRef = gritFB.getReference("users/");
        if (canAttack && !teamIsFighting) {
            teamIsFighting = true;
            bossStartTime = Instant.now().getEpochSecond();
            currFightRef.child("startTime").setValue(bossStartTime);
            currFightRef.child("isFighting").setValue(true);
            currFightRef.child("world").setValue("1-1");
            for (String teamMate : teammateUIDList) {
                userRef.child(teamMate).child("isFighting").setValue(true);
            }
            determineTime();
        }
    }

    private boolean determineIfReady() {
        if (playerList.size() <  teammateUIDList.size()) {
            Toast.makeText(this,
                    "All players must be at the current world to fight this boss!",
                    Toast.LENGTH_SHORT).show();
            return false;
        }
        for (RCViewPlayer player : playerList) {
            if (!player.isReady() && player.getPlayerWorld().equals(currWorld)) {
                Toast.makeText(this, "All players must be ready before attacking",
                        Toast.LENGTH_SHORT).show();
                return false;
            } else if (!player.getPlayerWorld().equals(currWorld)) {
                Toast.makeText(this,
                        "All players must be at the current world to fight this boss!",
                        Toast.LENGTH_SHORT).show();
                return false;
            }
        }
        return true;
    }

    public void refreshTeam() {
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
                                    aUser.child("isReady").getValue(boolean.class),
                                    aUser.child("currWorld").getValue(String.class)));
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
}