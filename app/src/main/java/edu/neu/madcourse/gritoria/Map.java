package edu.neu.madcourse.gritoria;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

import edu.neu.madcourse.gritoria.bosses.worldFights;
import edu.neu.madcourse.gritoria.rcViewTeamRank.RCAdapter;
import edu.neu.madcourse.gritoria.rcViewTeamRank.RViewTeamRank;

public class Map extends AppCompatActivity {

    private List<RViewTeamRank> teamList;
    RecyclerView rcTeamRank;
    RCAdapter rcAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        teamList = new ArrayList<>();
        createRCView();
        //TODO: CONNECT TO DB AND DISPLAY TEAMS
        dummyTeamData();
    }

    public void enterWorld(View v) {
        int currWorld = v.getId();
        Intent worldFight = new Intent(this, worldFights.class);
        if (currWorld == R.id.imageButtonWorld1_1) {
            worldFight.putExtra("level", "1-1");

        } else if (currWorld == R.id.imageButtonWorld1_2) {
            worldFight.putExtra("level", "1-2");
        } else {
            worldFight.putExtra("level", "DEFAULT WORLD");
        }
        startActivity(worldFight);
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