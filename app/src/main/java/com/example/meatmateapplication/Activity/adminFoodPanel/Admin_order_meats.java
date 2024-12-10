package com.example.meatmateapplication.Activity.adminFoodPanel;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.example.meatmateapplication.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;


public class Admin_order_meats extends AppCompatActivity {

    RecyclerView recyclerViewdish;
    private List<AdminPendingOrders> adminPendingOrdersList;
    private AdminOrderMeatsAdapater adapter;
    DatabaseReference reference;
    String RandomUID;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_order_meats);
        recyclerViewdish = findViewById(R.id.Recycle_orders_dish);
        recyclerViewdish.setHasFixedSize(true);
        recyclerViewdish.setLayoutManager(new LinearLayoutManager(this));
        adminPendingOrdersList = new ArrayList<>();
        Adminordermeats();

    }

    private void Adminordermeats() {

        RandomUID = getIntent().getStringExtra("RandomUID");

        reference = FirebaseDatabase.getInstance().getReference("AdminPendingOrders").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child(RandomUID).child("Meats");
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                adminPendingOrdersList.clear();

                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    AdminPendingOrders adminPendingOrders = snapshot.getValue(AdminPendingOrders.class);
                    adminPendingOrdersList.add(adminPendingOrders);
                }
                adapter = new AdminOrderMeatsAdapater(Admin_order_meats.this, adminPendingOrdersList);
                recyclerViewdish.setAdapter(adapter);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
