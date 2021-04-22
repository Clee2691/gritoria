package edu.neu.madcourse.gritoria;

import android.content.Intent;
import android.net.Uri;
import android.view.View;
import android.webkit.URLUtil;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import static com.firebase.ui.auth.AuthUI.getApplicationContext;

public class Holder extends RecyclerView.ViewHolder implements View.OnClickListener{


    public EditText liftDate;
    public EditText liftItem;
    public Button liftInfo;
    private Adapter.recycleOnClick recycleListener;


    public Holder(View itemView, final LiftItemCard listener) {
        super(itemView);
        liftDate = itemView.findViewById(R.id.liftDate);
        liftInfo = itemView.findViewById(R.id.liftInfo);


        itemView.setOnClickListener(this);


        liftInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), LiftingStaticView.class);
                intent.putExtra("date", liftDate.getText().toString());
                v.getContext().startActivity(intent);
            }
        });



    }

    @Override
    public void onClick(View v) {
        recycleListener.onClick(v, getAdapterPosition());

    }
}
