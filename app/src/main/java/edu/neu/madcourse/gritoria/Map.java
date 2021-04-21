package edu.neu.madcourse.gritoria;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
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
        getDBData();
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
        } else if (currWorld == R.id.imageButtonWorld1_3){
            worldFight.putExtra("level", "1-3");
            worldFight.putExtra("bossHealth", 3000);
        } else if (currWorld == R.id.imageButtonWorld1_4) {
            worldFight.putExtra("level", "1-4");
            worldFight.putExtra("bossHealth", 4000);
        } else if (currWorld == R.id.imageButtonWorld1_5) {
            worldFight.putExtra("level", "1-5");
            worldFight.putExtra("bossHealth", 5000);
        }
        startActivity(worldFight);
    }

    private void getDBData() {
        DatabaseReference playerRef = gritFB.getReference();
        playerRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                teamList.clear();
                rcAdapter.notifyDataSetChanged();

                playerTeam = snapshot.child("users").child(playerUID)
                        .child("team").getValue(String.class);

                if (playerTeam.equals("")) {
                    Toast.makeText(Map.this, "Join a team to start your adventure!",
                            Toast.LENGTH_SHORT).show();
                } else {
                    isTeamFighting = snapshot.child("teams").child(playerTeam).child("currFight")
                            .child("isFighting").getValue(boolean.class);
                }

                for(DataSnapshot eachTeam : snapshot.child("teams").getChildren()) {
                    createTeam(eachTeam);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }

            public void createTeam(DataSnapshot dSnap) {
                int numPlayers = 0;
                for (DataSnapshot teammembers : dSnap.child("members").getChildren()) {
                    numPlayers++;
                }
                teamList.add(new RViewTeamRank(dSnap.getKey(), numPlayers));
                rcAdapter.notifyItemInserted(teamList.size() - 1);
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

    public void backButtonPress(View v) {
        super.onBackPressed();
    }
}