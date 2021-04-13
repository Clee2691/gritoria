package edu.neu.madcourse.gritoria;

public class EquipmentItem {
    protected String itemName;
    protected int itemDurability;
    protected int itemAttack;
    protected int itemDefense;

    public EquipmentItem(String itemName, int itemDurability, int itemAttack, int itemDefense) {
        this.itemName = itemName;
        this.itemDurability = itemDurability;
        this.itemAttack = itemAttack;
        this.itemDefense = itemDefense;
    }

    public void onItemClick(int position) {
    }
}
