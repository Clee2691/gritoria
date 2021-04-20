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

import com.firebase.ui.auth.data.model.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import edu.neu.madcourse.gritoria.messages.BaseMessage;
import edu.neu.madcourse.gritoria.messages.MessageListAdapter;
import edu.neu.madcourse.gritoria.messages.UserMessage;


public class Team extends AppCompatActivity {
    protected DatabaseReference mDatabase;
    protected FirebaseAuth mAuth;
    protected TextView yourName;
    protected TextView ChatMessage;
    protected Bundle bundle;
    private RecyclerView mMessageRecycler;
    private MessageListAdapter mMessageAdapter;
    private ArrayList<BaseMessage> messageList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_team);
        bundle = getIntent().getExtras();

        if (bundle.getString("teamName").length() < 3) {
            this.openTeamSelection();
        }

        mDatabase = FirebaseDatabase.getInstance().getReference();
        mAuth = FirebaseAuth.getInstance();

        mMessageRecycler = (RecyclerView) findViewById(R.id.recycler_gchat);
        ChatMessage = findViewById(R.id.ChatMessage);

        generateMessageHistory();

        mMessageAdapter = new MessageListAdapter(this, messageList, bundle.getString("currUser"));
        LinearLayoutManager llm = new LinearLayoutManager(this);
        llm.scrollToPosition(messageList.size()-1);
        mMessageRecycler.setLayoutManager(llm);
        mMessageRecycler.setAdapter(mMessageAdapter);
        Button sendButton = findViewById(R.id.SendButton);
        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ChatMessage.getText().length() > 0) {
                    String currMessage = ChatMessage.getText().toString();
                    BaseMessage message = new UserMessage(bundle.getString("currUser"), currMessage, new Date(), messageList.size());
                    messageList.add(message);
                    ChatMessage.setText("");
                    InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(sendButton.getWindowToken(), InputMethodManager.RESULT_SHOWN);
                    mDatabase.child("teams").child(bundle.getString("teamName")).child("messages").push().setValue(message.getMessageMap());
                    llm.scrollToPosition(messageList.size()-1);
                }
            }
        });

        mDatabase.child("teams").child(bundle.getString("teamName")).child("messages").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                messageList.clear();
                for (DataSnapshot child : snapshot.getChildren()) {
                    String messageMessage = (String) child.child("message").getValue();
                    String dateMessage = (String) child.child("date").getValue();
                    String timeMessage = (String) child.child("time").getValue();
                    String senderMessage = (String) child.child("sender").getValue();
                    String indexMessage = (String) child.child("index").getValue();
                    messageList.add(Integer.parseInt(indexMessage) , new UserMessage(senderMessage, messageMessage, dateMessage, timeMessage,  indexMessage));
                    llm.scrollToPosition(messageList.size()-1);
                    mMessageRecycler.invalidate();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        TextView teamNameTitle = findViewById(R.id.TeamNameTitle);
        teamNameTitle.setText(bundle.getString("teamName"));
        HashMap<String, String> teamMembers = (HashMap<String, String>) bundle.getSerializable("teamMembers");
        yourName = findViewById(R.id.UserName);
        yourName.setText(bundle.getString("currUser"));
    }

    public void openTeamSelection(){
        Intent intent = new Intent(this, TeamSelectionActivity.class);
        intent.putExtras(bundle);
        startActivity(intent);
    }

    private void generateMessageHistory() {
        try{
            HashMap<String, HashMap<String, String>> messageHistory = (HashMap<String, HashMap<String, String>>) bundle.getSerializable("messages");
            for (HashMap<String,String> map :messageHistory.values()) {
                messageList.add(new UserMessage(map.get("sender"), map.get("message"), map.get("date"), map.get("time"), map.get("index")));
            }
            Comparator<BaseMessage> cmp = new Comparator<BaseMessage>() {
                @Override
                public int compare(BaseMessage o2, BaseMessage o1) {
                    return o2.getIndex() - (o1.getIndex());
                }
            };
            messageList.sort(cmp);
        } catch (NullPointerException e) {

        }

    }
}