package edu.neu.madcourse.gritoria;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.InputType;
import android.util.Patterns;
import android.view.View;
import android.webkit.URLUtil;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LiftLog extends AppCompatActivity {

    private String mParam1;
    private String mParam2;
    private RecyclerView recyclerView;
    private Adapter adapter;
    private RecyclerView.LayoutManager rLayoutManger;
    private FloatingActionButton addButton;
    private ArrayList<LiftItemCard> itemList = new ArrayList<>();
    private Adapter.recycleOnClick recycleListener;


    private String url_text;
    ConstraintLayout constraintLayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_liftlog);

        RecyclerView recycle_view = findViewById(R.id.recycle_widget);
        //recycle_view.setHasFixedSize(true);

        Adapter adapter = new Adapter(itemList,recycleListener);
        setOnClickListener();
        recycle_view.setAdapter(adapter);

        RecyclerView.LayoutManager layout = new LinearLayoutManager(this);
        recycle_view.setLayoutManager(layout);


        FloatingActionButton addButton = findViewById(R.id.floatingActionButton);
        constraintLayout = findViewById(R.id.frameLayout);
        Button info = findViewById(R.id.liftInfo);



        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LiftItemCard itemCard = new LiftItemCard();
                itemList.add(itemCard);
                adapter.notifyDataSetChanged();
                //change to snack bar
                Snackbar.make(constraintLayout,"Added a lift", Snackbar.LENGTH_LONG)
                        .setAction(";)", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                            }
                        })
                        .show();



//
            }

        });

        Button back_button = findViewById(R.id.go_back_third_to_first);
        back_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(LiftLog.this, MainActivity.class);
                LiftLog.this.startActivity(myIntent);
            }
        });

    }

    private void setOnClickListener(){
        recycleListener = new Adapter.recycleOnClick() {
            @Override
            public void onClick(View v, int position) {
                Intent intent = new Intent(getApplicationContext(), LiftingInfo.class);
                startActivity(intent);
            }
        };
    }

}