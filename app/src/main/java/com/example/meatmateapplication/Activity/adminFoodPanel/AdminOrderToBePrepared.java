package com.example.meatmateapplication.Activity.adminFoodPanel;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

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

public class AdminOrderToBePrepared extends AppCompatActivity {

    private RecyclerView recyclerView;
    private List<AdminWaitingOrders1> adminWaitingOrders1List;
    private AdminOrderToBePreparedAdapter adapter;
    private DatabaseReference databaseReference;
    private SwipeRefreshLayout swipeRefreshLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_order_to_be_prepared);
        recyclerView = findViewById(R.id.Recycle_orderstobeprepared);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(AdminOrderToBePrepared.this));
        adminWaitingOrders1List = new ArrayList<>();
        swipeRefreshLayout = findViewById(R.id.Swipe1);
        swipeRefreshLayout.setColorSchemeResources(com.google.android.material.R.color.design_default_color_primary_dark, R.color.green);
        adapter = new AdminOrderToBePreparedAdapter(AdminOrderToBePrepared.this, adminWaitingOrders1List);
        recyclerView.setAdapter(adapter);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                adminWaitingOrders1List.clear();
                recyclerView = findViewById(R.id.Recycle_orderstobeprepared);
                recyclerView.setHasFixedSize(true);
                recyclerView.setLayoutManager(new LinearLayoutManager(AdminOrderToBePrepared.this));
                adminWaitingOrders1List = new ArrayList<>();
                adminorderstobePrepare();
            }
        });
        adminorderstobePrepare();

    }

    private void adminorderstobePrepare() {

        databaseReference = FirebaseDatabase.getInstance().getReference("AdminWaitingOrders").child(FirebaseAuth.getInstance().getCurrentUser().getUid());
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    adminWaitingOrders1List.clear();
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        DatabaseReference data = FirebaseDatabase.getInstance().getReference("AdminWaitingOrders").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child(snapshot.getKey()).child("OtherInformation");
                        data.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                AdminWaitingOrders1 adminWaitingOrders1 = dataSnapshot.getValue(AdminWaitingOrders1.class);
                                adminWaitingOrders1List.add(adminWaitingOrders1);
                                adapter = new AdminOrderToBePreparedAdapter(AdminOrderToBePrepared.this, adminWaitingOrders1List);
                                recyclerView.setAdapter(adapter);
                                swipeRefreshLayout.setRefreshing(false);
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });

                    }
                } else {
                    swipeRefreshLayout.setRefreshing(false);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}