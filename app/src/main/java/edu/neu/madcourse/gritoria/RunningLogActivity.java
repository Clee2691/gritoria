package edu.neu.madcourse.gritoria;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.sql.Date;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.List;

import edu.neu.madcourse.gritoria.messages.UserMessage;

public class RunningLogActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private RunningLogAdapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;
    List<RunningLogItem> runList = new ArrayList<>();
    private DatabaseReference dbRef;
    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_running_log);

        mAuth = FirebaseAuth.getInstance();
        dbRef = FirebaseDatabase.getInstance().getReference();

        dbRef.child("users").child(mAuth.getCurrentUser().getUid()).child("runs").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot child : snapshot.getChildren()) {
                    try {
                        String runDate = child.getKey();
                        String distance = (String) child.child("distance").getValue();
                        long steps = (long) child.child("steps").getValue();
                        runList.add(new RunningLogItem(distance + " miles", runDate, String.valueOf(steps)));
                    } catch (NullPointerException e) {
                        break;
                    }

                }
                buildRecyclerView(runList);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }

//        List<RunningLogItem> runningLogItems = createDemoRunRecords();
//        buildRecyclerView(runningLogItems);
        });
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
            runningLogList.add((new RunningLogItem(i + " miles", now.toString(), i + " steps")));
        }

        return runningLogList;
    }
}