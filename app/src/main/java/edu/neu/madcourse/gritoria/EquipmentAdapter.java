package edu.neu.madcourse.gritoria;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class EquipmentAdapter extends RecyclerView.Adapter<EquipmentHolder> {

    private final ArrayList<EquipmentItem> itemList;

    public EquipmentAdapter(ArrayList<EquipmentItem> itemList) {
        this.itemList = itemList;
    }


    @NonNull
    @Override
    public EquipmentHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_card, parent, false);
        return new EquipmentHolder(view, itemList.get(0));
    }

    @Override
    public void onBindViewHolder(@NonNull EquipmentHolder holder, int position) {
        EquipmentItem currentItem = itemList.get(position);
        holder.itemName.setText(currentItem.itemName);
        holder.itemDurability.setText(currentItem.itemDurability);
        holder.itemStats.setText("Att: " + String.valueOf(currentItem.itemAttack) + " Def:"+ String.valueOf(currentItem.itemDefense));
    }

    @Override
    public int getItemCount() {
        return 5;
    }
}
