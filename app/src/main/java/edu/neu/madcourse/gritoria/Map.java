package edu.neu.madcourse.gritoria;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import edu.neu.madcourse.gritoria.bosses.worldFights;
import edu.neu.madcourse.gritoria.rcViewTeamRank.RCAdapter;
import edu.neu.madcourse.gritoria.rcViewTeamRank.RViewTeamRank;

public class Map extends AppCompatActivity {

    private List<RViewTeamRank> teamList;
    RecyclerView rcTeamRank;
    RCAdapter rcAdapter;
    private String playerTeam;
    private String playerUID;
    private boolean isTeamFighting;
    FirebaseUser currPlayer;
    FirebaseDatabase gritFB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        teamList = new ArrayList<>();
        createRCView();
        currPlayer = FirebaseAuth.getInstance().getCurrentUser();
        playerUID = currPlayer.getUid();
        //Firebase Realtime DB
        gritFB = FirebaseDatabase.getInstance();
        getPlayerInfo();
        //TODO: CONNECT TO DB AND DISPLAY TEAMS
        dummyTeamData();
    }

    public void enterWorld(View v) {
        int currWorld = v.getId();
        Intent worldFight = new Intent(this, worldFights.class);
        worldFight.putExtra("playerTeam", playerTeam);
        worldFight.putExtra("isTeamFighting", isTeamFighting);

        if (currWorld == R.id.imageButtonWorld1_1) {
            worldFight.putExtra("level", "1-1");
            worldFight.putExtra("bossHealth", 1000);

        } else if (currWorld == R.id.imageButtonWorld1_2) {
            worldFight.putExtra("level", "1-2");
            worldFight.putExtra("bossHealth", 2000);
        } else {
            worldFight.putExtra("level", "DEFAULT WORLD");
        }
        startActivity(worldFight);
    }

    private void getPlayerInfo() {
        DatabaseReference playerRef = gritFB.getReference();
        playerRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                playerTeam = snapshot.child("users").child(playerUID)
                        .child("team").getValue(String.class);
                isTeamFighting = snapshot.child("teams").child(playerTeam).child("currFight")
                        .child("isFighting").getValue(boolean.class);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }


    private void createRCView() {
        rcTeamRank = findViewById(R.id.recyclerViewTeamRanks);
        rcTeamRank.setHasFixedSize(true);
        rcAdapter = new RCAdapter(teamList);
        rcTeamRank.setAdapter(rcAdapter);
        rcTeamRank.setLayoutManager(new LinearLayoutManager(this));
    }

    public void dummyTeamData() {
        teamList.add(new RViewTeamRank("Test Team 1", 4));
        teamList.add(new RViewTeamRank("Test Team 2", 3));
        teamList.add(new RViewTeamRank("Test Team 3", 6));
        teamList.add(new RViewTeamRank("Test Team 4", 1));
        teamList.add(new RViewTeamRank("Test Team 5", 20));
        rcAdapter.notifyDataSetChanged();
    }

    public void backButtonPress(View v) {
        super.onBackPressed();
    }
}