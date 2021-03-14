package edu.neu.madcourse.gritoria;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import java.sql.Date;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class RunningLogActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private RunningLogAdapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_running_log);

        List<RunningLogItem> runningLogItems = createDemoRunRecords();
        buildRecyclerView(runningLogItems);
    }

    public void buildRecyclerView(List<RunningLogItem> runningLogList) {
        recyclerView = findViewById(R.id.RunningLogRecycler);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        mAdapter = new RunningLogAdapter(runningLogList);

        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(mAdapter);
        mAdapter.setOnItemClickListener(new RunningLogAdapter.OnItemClickListener() {
            @Override
            public void onGoClick(int position) {
                RunningLogItem item = runningLogList.get(position);
            }
        });
    }

    private List<RunningLogItem> createDemoRunRecords() {
        Date now = new Date(Calendar.getInstance().getTime().getTime());
        List<RunningLogItem> runningLogList = new ArrayList<>();
        for (int i = 1; i < 20; i++) {
            runningLogList.add((new RunningLogItem(i + " miles", now)));
        }

        return runningLogList;
    }
}