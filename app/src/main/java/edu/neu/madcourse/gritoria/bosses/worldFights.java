package edu.neu.madcourse.gritoria.bosses;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
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

import static android.os.SystemClock.sleep;

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
    private boolean bossKilled;
    private boolean teamIsFighting;
    private boolean isLeader;
    private final double expMultiplier = 0.25;
    private int teamPower;
    private String teamWorld;
    private TimeThread bossFight;
    private Thread attackThread;
    private long bossCurrHealth;
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
        bossFight = new TimeThread();
        setupRecyclerView();
        refreshTeam();
        setupUI();
        // Create a new thread when coming back from any other page
        continueFighting();
    }

    @Override
    protected void onPause() {
        if (attackThread !=null) {
            DatabaseReference teamCurrFight = gritFB.getReference("teams/" +
                    playerTeam + "/currFight");
            teamCurrFight.child("bossCurrHealth").setValue(bossCurrHealth);
            try {
                bossFight.setInterrupted();
                attackThread.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        super.onPause();
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
        teamWorld = currIntent.getStringExtra("currTeamWorld");
        bossStartTime = currIntent.getIntExtra("bossStartTime", 0);
        teamPower = currIntent.getIntExtra("teamPower", 1);

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
                DataSnapshot teamRef = snapshot.child("teams").child(playerTeam);
                currPlayerReadyStatus = userRef.child("isReady").getValue(boolean.class);
                playerWorld = userRef.child("currWorld").getValue(String.class);
                playerIsFighting = userRef.child("isFighting").getValue(boolean.class);
                isLeader = userRef.child("isLeader").getValue(boolean.class);
                bossKilled = teamRef.child("currFight").child("isKilled").getValue(boolean.class);
                bossCurrHealth = teamRef.child("currFight").child("bossCurrHealth").
                        getValue(Integer.class);
                Button endButton = findViewById(R.id.buttonResults);
                TextView bossTime = findViewById(R.id.textViewTimeLeftVal);

                if (playerTeam.equals("")) {
                    readyButton.setVisibility(View.GONE);
                    attackBut.setVisibility(View.GONE);
                    return;
                }

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
                } else if (!isLeader || teamIsFighting){
                    attackBut.setEnabled(false);
                    attackBut.setVisibility(View.GONE);
                }

                if (bossKilled && teamWorld.equals(currWorld)) {
                    bossTime.setVisibility(View.GONE);
                    endButton.setEnabled(true);
                    endButton.setVisibility(View.VISIBLE);
                } else if (!bossKilled && playerWorld.equals(currWorld)) {
                    endButton.setEnabled(false);
                    endButton.setVisibility(View.GONE);
                } else if (!playerWorld.equals(currWorld)) {
                    endButton.setEnabled(false);
                    endButton.setVisibility(View.GONE);
                } else if (bossKilled && !teamWorld.equals(currWorld)) {
                    endButton.setEnabled(false);
                    endButton.setVisibility(View.GONE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(worldFights.this, "Error: " + error.getMessage(),
                        Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void continueFighting() {
        if (teamWorld.equals(currWorld) && bossStartTime > 0) {
            attackThread = new Thread(bossFight);
            attackThread.start();
        }
    }

    // If team finishes boss, show results
    public void getEndResults(View v) {
        TextView bossTime = findViewById(R.id.textViewTimeLeftVal);
        Button attackButton = findViewById(R.id.buttonAttack);
        String message = String.format("Congratulations %s!\nYou gained %.0f exp each!",
                playerTeam, (bossHealth * expMultiplier));
        DialogFragment finishFightDialog = new FinishFightDialog(message);
        finishFightDialog.show(getSupportFragmentManager(), "LOOT");

        v.setEnabled(false);
        v.setVisibility(View.GONE);
        attackButton.setVisibility(View.VISIBLE);
        gritFB.getReference("teams/" + playerTeam + "/currFight").
                child("isKilled").setValue(false);
        gritFB.getReference("teams/" + playerTeam + "/currFight").
                child("world").setValue("");

        bossTime.setVisibility(View.VISIBLE);
    }

    private void determineTime() {
        attackThread = new Thread(bossFight);
        attackThread.start();
    }

    // Logic for "idle" part of the game
    // Calculates the current time with when the boss time started and determines
    // if the elapsed time is == boss health
    class TimeThread implements Runnable {
        long deltaTime;
        DatabaseReference teamFight;
        DatabaseReference playerRef;
        TextView bossTimer;
        Button readyButton;

        ProgressBar bossProgBar;
        TextView bossHealthNum;
        boolean isInterrupted = false;

        @Override
        public void run() {
            bossTimer = findViewById(R.id.textViewTimeLeftVal);
            bossProgBar = findViewById(R.id.progressBarBossHealth);
            bossHealthNum = findViewById(R.id.textViewBossHealth);

            teamFight = gritFB.getReference("teams/" + playerTeam + "/currFight");

            while(teamIsFighting && bossStartTime > 0 && !isInterrupted) {
                currTimeInEpoch = Instant.now().getEpochSecond();
                Log.e("CurrEpoch", String.format("%d", currTimeInEpoch));
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                deltaTime = currTimeInEpoch - bossStartTime;
                bossCurrHealth = bossHealth - (deltaTime * (teamPower * 2));
                Log.e("final time",String.format("%d",bossCurrHealth));
                uiHandler.post(()-> bossTimer.setText(
                        String.format("%d seconds left", bossCurrHealth)));
                uiHandler.post(()->bossProgBar.setProgress((int)bossCurrHealth));
                uiHandler.post(()->bossHealthNum.setText(String.format("Health: %d/%d",
                        bossCurrHealth, bossHealth)));
                if (bossCurrHealth <= 0) {
                    finishFight();
                    break;
                }
            }
            Log.e("Thread exit", "Thread attack has exited");
        }

        public void setInterrupted() {
            isInterrupted = !isInterrupted;
        }

        public boolean getisInterrupted() {
            return isInterrupted;
        }

        private void finishFight() {
            readyButton = findViewById(R.id.btnReadyUp);
            TextView bossTime = findViewById(R.id.textViewTimeLeftVal);
            uiHandler.post(()->bossTime.setVisibility(View.GONE));

            uiHandler.post(()-> bossTimer.setText(String.format("Boss Complete!")));
            uiHandler.post(()-> bossHealthNum.setText(String.format("0/%d", bossHealth)));
            teamFight.child("isFighting").setValue(false);
            teamFight.child("startTime").setValue(0);
            teamFight.child("isKilled").setValue((true));
            teamFight.child("bossCurrHealth").setValue(0);

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
                        currExp += bossHealth * expMultiplier;
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
        Button atkButton = findViewById(R.id.buttonAttack);
        DatabaseReference currFightRef = gritFB.getReference("teams/" + playerTeam +
                                                            "/currFight");
        DatabaseReference userRef = gritFB.getReference("users/");
        if (canAttack && !bossKilled) {
            teamIsFighting = true;
            bossStartTime = Instant.now().getEpochSecond();
            currFightRef.child("startTime").setValue(bossStartTime);
            currFightRef.child("bossCurrHealth").setValue(bossHealth);
            currFightRef.child("isFighting").setValue(true);
            currFightRef.child("world").setValue(currWorld);
            for (String teamMate : teammateUIDList) {
                userRef.child(teamMate).child("isFighting").setValue(true);
            }
            atkButton.setVisibility(View.GONE);
            determineTime();
        } else {
            Toast.makeText(this,
                    "Either your team isn't ready or the boss is already dead! Claim the loot!",
                    Toast.LENGTH_SHORT).show();
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
                                    aUser.child("username").getValue(String.class),
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

    public void backButtonPress(View v) throws InterruptedException {
        super.onBackPressed();
        if (attackThread != null) {
            DatabaseReference teamCurrFight = gritFB.getReference("teams/" +
                    playerTeam + "/currFight");
            teamCurrFight.child("bossCurrHealth").setValue(bossCurrHealth);
            try {
                bossFight.setInterrupted();
                attackThread.join();
                attackThread = null;
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onBackPressed(){
        super.onBackPressed();
        if (attackThread != null) {
            DatabaseReference teamCurrFight = gritFB.getReference("teams/" +
                    playerTeam + "/currFight");
            teamCurrFight.child("bossCurrHealth").setValue(bossCurrHealth);
            try {
                bossFight.setInterrupted();
                attackThread.join();
                attackThread = null;
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
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