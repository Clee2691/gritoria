package edu.neu.madcourse.gritoria;

import android.content.Intent;
import android.net.Uri;
import android.view.View;
import android.webkit.URLUtil;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class Holder extends RecyclerView.ViewHolder{


    public TextView liftDate;
    public TextView liftItem;


    public Holder(View itemView, final LiftItemCard listener) {
        super(itemView);
        liftDate = itemView.findViewById(R.id.liftDate);
        liftItem = itemView.findViewById(R.id.liftItem);

        itemView.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){

//                String url =  itemName.getText().toString();
//                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
//                view.getContext().startActivity(intent);

            }

        });



    }
}
