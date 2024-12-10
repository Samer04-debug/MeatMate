package com.example.meatmateapplication.Activity.adminFoodPanel;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.meatmateapplication.R;
import com.example.meatmateapplication.Activity.SendNotification.APIService;
import com.example.meatmateapplication.Activity.SendNotification.Client;
import com.example.meatmateapplication.Activity.SendNotification.Data;
import com.example.meatmateapplication.Activity.SendNotification.MyResponse;
import com.example.meatmateapplication.Activity.SendNotification.NotificationSender;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
public class AdminOrderToBePreparedView extends AppCompatActivity {

    RecyclerView recyclerViewmeat;
    private List<AdminWaitingOrders> adminWaitingOrdersList;
    private AdminOrderToBePreparedViewAdapter adapter;
    DatabaseReference reference;
    String RandomUID, userid;
    TextView grandtotal, note, address, name, number;
    LinearLayout l1;
    Button Preparing;
    private ProgressDialog progressDialog;
    private APIService apiService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_order_to_be_prepared_view);
        recyclerViewmeat = findViewById(R.id.Recycle_viewOrder);
        grandtotal = findViewById(R.id.rupees);
        note = findViewById(R.id.NOTE);
        address = findViewById(R.id.ad);
        name = findViewById(R.id.nm);
        number = findViewById(R.id.num);
        l1 = findViewById(R.id.button1);
        Preparing = findViewById(R.id.preparing);
        apiService = Client.getClient("https://fcm.googleapis.com/").create(APIService.class);
        progressDialog = new ProgressDialog(AdminOrderToBePreparedView.this);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.setCancelable(false);
        recyclerViewmeat.setHasFixedSize(true);
        recyclerViewmeat.setLayoutManager(new LinearLayoutManager(AdminOrderToBePreparedView.this));
        adminWaitingOrdersList = new ArrayList<>();
        AdminordermeatsView();
    }

    private void AdminordermeatsView() {
        RandomUID = getIntent().getStringExtra("RandomUID");

        reference = FirebaseDatabase.getInstance().getReference("AdminWaitingOrders").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child(RandomUID).child("Meats");
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                adminWaitingOrdersList.clear();

                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    AdminWaitingOrders adminWaitingOrders = snapshot.getValue(AdminWaitingOrders.class);
                    adminWaitingOrdersList.add(adminWaitingOrders);
                }
                if (adminWaitingOrdersList.size() == 0) {
                    l1.setVisibility(View.INVISIBLE);

                } else {
                    l1.setVisibility(View.VISIBLE);
                    Preparing.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            progressDialog.setMessage("Please wait...");
                            progressDialog.show();

                            DatabaseReference databaseReference1 = FirebaseDatabase.getInstance().getReference("AdminWaitingOrders").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child(RandomUID).child("Meats");
                            databaseReference1.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                                        final AdminWaitingOrders adminWaitingOrders = dataSnapshot1.getValue(AdminWaitingOrders.class);
                                        HashMap<String, String> hashMap = new HashMap<>();
                                        String meatid = adminWaitingOrders.getMeatId();
                                        userid = adminWaitingOrders.getUserId();
                                        hashMap.put("AdminId", adminWaitingOrders.getAdminId());
                                        hashMap.put("MeatId", adminWaitingOrders.getMeatId());
                                        hashMap.put("MeatName", adminWaitingOrders.getMeatName());
                                        hashMap.put("MeatPrice", adminWaitingOrders.getMeatPrice());
                                        hashMap.put("MeatQuantity", adminWaitingOrders.getMeatQuantity());
                                        hashMap.put("RandomUID", RandomUID);
                                        hashMap.put("TotalPrice", adminWaitingOrders.getTotalPrice());
                                        hashMap.put("UserId", adminWaitingOrders.getUserId());
                                        FirebaseDatabase.getInstance().getReference("AdminFinalOrders").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child(RandomUID).child("Meats").child(meatid).setValue(hashMap);
                                    }
                                    DatabaseReference data = FirebaseDatabase.getInstance().getReference("AdminWaitingOrders").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child(RandomUID).child("OtherInformation");
                                    data.addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                            final AdminWaitingOrders1 adminWaitingOrders1 = dataSnapshot.getValue(AdminWaitingOrders1.class);
                                            HashMap<String, String> hashMap1 = new HashMap<>();
                                            hashMap1.put("Address", adminWaitingOrders1.getAddress());
                                            hashMap1.put("GrandTotalPrice", adminWaitingOrders1.getGrandTotalPrice());
                                            hashMap1.put("MobileNumber", adminWaitingOrders1.getMobileNumber());
                                            hashMap1.put("Name", adminWaitingOrders1.getName());
                                            hashMap1.put("RandomUID", RandomUID);
                                            hashMap1.put("Status", "Admin is preparing your Order...");
                                            FirebaseDatabase.getInstance().getReference("AdminFinalOrders").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child(RandomUID).child("OtherInformation").setValue(hashMap1).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    FirebaseDatabase.getInstance().getReference("CustomerFinalOrders").child(userid).child(RandomUID).child("OtherInformation").child("Status").setValue("Admin is preparing your order...").addOnCompleteListener(new OnCompleteListener<Void>() {
                                                        @Override
                                                        public void onComplete(@NonNull Task<Void> task) {
                                                            FirebaseDatabase.getInstance().getReference("AdminWaitingOrders").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child(RandomUID).child("Dishes").removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                @Override
                                                                public void onComplete(@NonNull Task<Void> task) {
                                                                    FirebaseDatabase.getInstance().getReference("AdminWaitingOrders").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child(RandomUID).child("OtherInformation").removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                        @Override
                                                                        public void onSuccess(Void aVoid) {

                                                                            FirebaseDatabase.getInstance().getReference().child("Tokens").child(userid).child("token").addListenerForSingleValueEvent(new ValueEventListener() {
                                                                                @Override
                                                                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                                                    String usertoken = dataSnapshot.getValue(String.class);
                                                                                    sendNotifications(usertoken, "Estimated Time", "Admin is Preparing your Order", "Preparing");
                                                                                    progressDialog.dismiss();
                                                                                    AlertDialog.Builder builder = new AlertDialog.Builder(AdminOrderToBePreparedView.this);
                                                                                    builder.setMessage("See Orders which are Prepared");
                                                                                    builder.setCancelable(false);
                                                                                    builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                                                                        @Override
                                                                                        public void onClick(DialogInterface dialog, int which) {

                                                                                            dialog.dismiss();
                                                                                            Intent b = new Intent(AdminOrderToBePreparedView.this, AdminOrderToBePrepared.class);
                                                                                            startActivity(b);
                                                                                            finish();


                                                                                        }
                                                                                    });
                                                                                    AlertDialog alert = builder.create();
                                                                                    alert.show();
                                                                                }

                                                                                @Override
                                                                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                                                                }
                                                                            });

                                                                        }
                                                                    });
                                                                }
                                                            });
                                                        }
                                                    });
                                                }
                                            });
                                        }

                                        @Override
                                        public void onCancelled(@NonNull DatabaseError databaseError) {

                                        }
                                    });
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                }
                            });

                        }
                    });

                }
                adapter = new AdminOrderToBePreparedViewAdapter(AdminOrderToBePreparedView.this, adminWaitingOrdersList);
                recyclerViewmeat.setAdapter(adapter);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("AdminWaitingOrders").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child(RandomUID).child("OtherInformation");
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                AdminWaitingOrders1 adminWaitingOrders1 = dataSnapshot.getValue(AdminWaitingOrders1.class);
                grandtotal.setText("₱ " + adminWaitingOrders1.getGrandTotalPrice());
                note.setText(adminWaitingOrders1.getNote());
                address.setText(adminWaitingOrders1.getAddress());
                name.setText(adminWaitingOrders1.getName());
                number.setText("+63" + adminWaitingOrders1.getMobileNumber());

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void sendNotifications(String usertoken, String title, String message, String preparing) {
        Data data = new Data(title, message, preparing);
        NotificationSender sender = new NotificationSender(data, usertoken);
        apiService.sendNotification(sender).enqueue(new Callback<MyResponse>() {
            @Override
            public void onResponse(Call<MyResponse> call, Response<MyResponse> response) {
                if (response.code() == 200) {
                    if (response.body().success != 1) {
                        Toast.makeText(AdminOrderToBePreparedView.this, "Failed", Toast.LENGTH_SHORT).show();
                    }
                }

            }

            @Override
            public void onFailure(Call<MyResponse> call, Throwable t) {

            }
        });
    }
}
