package edu.neu.madcourse.gritoria;


import android.content.Intent;
import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;

import androidx.appcompat.app.AppCompatDialogFragment;

import edu.neu.madcourse.gritoria.LiftItemCardListener;

public class LiftItemCard extends AppCompatDialogFragment implements LiftItemCardListener {

    private final int imageSource;
    private final String itemName;
    private final String my_url;


    //Constructor
    public LiftItemCard(int imageSource, String itemName, String my_url) {
        this.imageSource = imageSource;
        this.itemName = itemName;
        this.my_url = my_url;

    }

    public int getImageSource() {
        return imageSource;
    }

    public String getItemName(){ return itemName;}
    public String getMy_url(){return my_url;}

    @Override
    public void onItemClick(int position) {

    }


}