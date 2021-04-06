package edu.neu.madcourse.gritoria;

import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class EquipmentHolder extends RecyclerView.ViewHolder {
    protected TextView itemName;
    protected TextView itemDurability;
    protected TextView itemStats;
    protected Button equip;

    // Use this later when we need functionality on click.
    public EquipmentHolder(@NonNull View itemView, EquipmentItem equipmentItem) {
        super(itemView);
        itemName = itemView.findViewById(R.id.item_name);
        itemDurability = itemView.findViewById(R.id.durability);
        itemStats = itemView.findViewById(R.id.stats);
        equip = itemView.findViewById(R.id.equipButton);
        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (equipmentItem != null) {
                    int position = getLayoutPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        equipmentItem.onItemClick(position);
                    }
                }
            }
        });

    }
}
