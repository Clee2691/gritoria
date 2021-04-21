package edu.neu.madcourse.gritoria;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class Adapter extends RecyclerView.Adapter<Holder>{

    private final ArrayList<LiftItemCard> itemList;
    private LiftItemCard listener;
    private recycleOnClick recycleListener;

    public Adapter(ArrayList<LiftItemCard> itemList, recycleOnClick recycleListener) {
        this.itemList = itemList;
        this.recycleListener = recycleListener;
    }
    public void setOnItemClickListener(LiftItemCard listener) {
        this.listener = listener;
    }

    @Override
    public Holder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_lifting_item_card, parent, false);
        return new Holder(view, listener);
    }


    public void onBindViewHolder(@NonNull Holder holder, int position) {
        LiftItemCard currentItem = itemList.get(position);


        holder.liftItem.setText(currentItem.getItemName());

    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    public interface recycleOnClick{
        void onClick(View v, int position);
    }


}