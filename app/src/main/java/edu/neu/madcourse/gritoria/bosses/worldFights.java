package edu.neu.madcourse.gritoria.bosses;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_world_fights);
        currIntent = getIntent();
        setupWorld(currIntent);
        playerList = new ArrayList<>();
        setupRecyclerView();
        dummyPlayerData();
    }

    private void setupWorld(Intent currIntent) {
        TextView worldLogo = findViewById(R.id.textViewWorld);
        String currWorld = currIntent.getStringExtra("level");
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
        playerList.add(new RCViewPlayer("Test 1", 100));
        playerList.add(new RCViewPlayer("Test 2", 3));
        playerList.add(new RCViewPlayer("Test 3", 42));
        playerList.add(new RCViewPlayer("Test 4", 5));
        playerList.add(new RCViewPlayer("Test 5", 6));
    }
}