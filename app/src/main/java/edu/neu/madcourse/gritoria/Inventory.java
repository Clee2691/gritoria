package edu.neu.madcourse.gritoria;

<<<<<<< HEAD
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

public class Inventory extends AppCompatActivity {
=======
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.os.Bundle;
import android.widget.Toast;

import java.util.ArrayList;

public class Inventory extends AppCompatActivity {
    protected ArrayList<EquipmentItem> invList;
    protected EquipmentAdapter rviewAdapter;
    protected RecyclerView recyclerView;
    protected RecyclerView.LayoutManager rLayoutManger;
>>>>>>> origin/firebase_and_equipment

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inventory);
<<<<<<< HEAD
=======
        //invList = createInventory();
        //createRecyclerView();
    }

    private void createRecyclerView() {
        rLayoutManger = new LinearLayoutManager(this);
        recyclerView = findViewById(R.id.equipmentList);
        recyclerView.setHasFixedSize(true);
        rviewAdapter = new EquipmentAdapter(invList);
        recyclerView.setAdapter(rviewAdapter);
        recyclerView.setLayoutManager(rLayoutManger);
    }

    private ArrayList<EquipmentItem> createInventory() {
        ArrayList<EquipmentItem> inventoryList = new ArrayList<>();
        int count = 0;
        while (count < 5) {
            inventoryList.add(new EquipmentItem("Test", 10, 10, 10));
            count ++;
        }
        return inventoryList;
>>>>>>> origin/firebase_and_equipment
    }
}