package edu.neu.madcourse.gritoria;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.TimeUnit;


public class Team extends AppCompatActivity {
    protected DatabaseReference mDatabase;
    protected FirebaseAuth mAuth;
    protected TextView yourName;
    protected TextView ChatMessage;
    protected Bundle bundle;
    private RecyclerView mMessageRecycler;
    private MessageListAdapter mMessageAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_team);
        bundle = getIntent().getExtras();

        if (bundle.getString("teamName").length() < 3) {
            this.openTeamSelection();
        }

        mMessageRecycler = (RecyclerView) findViewById(R.id.recycler_gchat);
        List<BaseMessage> messageList = new ArrayList<>();
        messageList.add(0, new UserMessage(bundle.getString("currUser"), "test", new Date()));
        messageList.add(0,new UserMessage(bundle.getString("currUser"), "test", new Date()));
        messageList.add(0,new UserMessage(bundle.getString("currUser"), "test", new Date()));
        messageList.add(0,new UserMessage("Teammate 1", "test", new Date()));


        ChatMessage = findViewById(R.id.ChatMessage);
        mMessageAdapter = new MessageListAdapter(this, messageList, bundle.getString("currUser"));
        LinearLayoutManager llm = new LinearLayoutManager(this);
        llm.setStackFromEnd(true);
        mMessageRecycler.setLayoutManager(llm);
        mMessageRecycler.setAdapter(mMessageAdapter);
        Button sendButton = findViewById(R.id.SendButton);
        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ChatMessage.getText().length() > 0) {
                    messageList.add(new UserMessage(bundle.getString("currUser"), ChatMessage.getText().toString(), new Date()));
                    ChatMessage.setText("");
                    InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(sendButton.getWindowToken(), InputMethodManager.RESULT_SHOWN);
                }
            }
        });


        mDatabase = FirebaseDatabase.getInstance().getReference();

        TextView teamNameTitle = findViewById(R.id.TeamNameTitle);
        teamNameTitle.setText(bundle.getString("teamName"));


        mAuth = FirebaseAuth.getInstance();
        HashMap<String, String> teamMembers = (HashMap<String, String>) bundle.getSerializable("teamMembers");
        yourName = findViewById(R.id.UserName);
        yourName.setText(bundle.getString("currUser"));

    }

    public void openTeamSelection(){
        Intent intent = new Intent(this, TeamSelectionActivity.class);
        intent.putExtras(bundle);
        startActivity(intent);
    }

}