package com.example.meatmateapplication.Activity.customerFoodPanel;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.meatmateapplication.Activity.CustomerFoodPanel_BottomNavigation;

import com.example.meatmateapplication.R;
import com.example.meatmateapplication.Activity.SendNotification.APIService;
import com.example.meatmateapplication.Activity.SendNotification.Client;
import com.example.meatmateapplication.Activity.SendNotification.Data;
import com.example.meatmateapplication.Activity.SendNotification.MyResponse;
import com.example.meatmateapplication.Activity.SendNotification.NotificationSender;

import com.example.meatmateapplication.Activity.SendNotification.Client;
import com.example.meatmateapplication.Activity.SendNotification.Data;
import com.example.meatmateapplication.Activity.SendNotification.MyResponse;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CustomerPayment extends AppCompatActivity {

    TextView OnlinePayment, CashPayment;
    String RandomUID, AdminID;
    private APIService apiService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_payment);

        OnlinePayment = (TextView) findViewById(R.id.online);
        CashPayment = (TextView) findViewById(R.id.cash);
        RandomUID = getIntent().getStringExtra("RandomUID");
        apiService = Client.getClient("https://fcm.googleapis.com/").create(APIService.class);

        OnlinePayment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CustomerPayment.this, CustomerOnlinePayment.class);
                intent.putExtra("randomUID", RandomUID);
                startActivity(intent);
            }
        });


        CashPayment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("CustomerPaymentOrders").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child(RandomUID).child("Meats");
                databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                            final CustomerPaymentOrders customerPaymentOrders = dataSnapshot1.getValue(CustomerPaymentOrders.class);
                            HashMap<String, String> hashMap = new HashMap<>();
                            String meatid = customerPaymentOrders.getMeatId();
                            hashMap.put("AdminId", customerPaymentOrders.getAdminId());
                            hashMap.put("MeatId", customerPaymentOrders.getMeatId());
                            hashMap.put("MeatName", customerPaymentOrders.getMeatName());
                            hashMap.put("MeatPrice", customerPaymentOrders.getMeatPrice());
                            hashMap.put("MeatQuantity", customerPaymentOrders.getMeatQuantity());
                            hashMap.put("RandomUID", RandomUID);
                            hashMap.put("TotalPrice", customerPaymentOrders.getTotalPrice());
                            hashMap.put("UserId", customerPaymentOrders.getUserId());
                            FirebaseDatabase.getInstance().getReference("CustomerFinalOrders").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child(RandomUID).child("Meats").child(meatid).setValue(hashMap);
                        }
                        DatabaseReference data = FirebaseDatabase.getInstance().getReference("CustomerPaymentOrders").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child(RandomUID).child("OtherInformation");
                        data.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                                final CustomerPaymentOrders1 customerPaymentOrders1 = dataSnapshot.getValue(CustomerPaymentOrders1.class);
                                HashMap<String, String> hashMap1 = new HashMap<>();
                                hashMap1.put("Address", customerPaymentOrders1.getAddress());
                                hashMap1.put("GrandTotalPrice", customerPaymentOrders1.getGrandTotalPrice());
                                hashMap1.put("MobileNumber", customerPaymentOrders1.getMobileNumber());
                                hashMap1.put("Name", customerPaymentOrders1.getName());
                                hashMap1.put("Note", customerPaymentOrders1.getNote());
                                hashMap1.put("RandomUID", RandomUID);
                                hashMap1.put("Status", "Your order is waiting to be prepared by Admin...");
                                FirebaseDatabase.getInstance().getReference("CustomerFinalOrders").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child(RandomUID).child("OtherInformation").setValue(hashMap1).addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        DatabaseReference Reference = FirebaseDatabase.getInstance().getReference("CustomerPaymentOrders").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child(RandomUID).child("Meats");
                                        Reference.addListenerForSingleValueEvent(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                                                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                                    final CustomerPaymentOrders customerPaymentOrderss = snapshot.getValue(CustomerPaymentOrders.class);
                                                    HashMap<String, String> hashMap2 = new HashMap<>();
                                                    String meatid = customerPaymentOrderss.getMeatId();
                                                    AdminID = customerPaymentOrderss.getAdminId();
                                                    hashMap2.put("AdminId", customerPaymentOrderss.getAdminId());
                                                    hashMap2.put("MeatId", customerPaymentOrderss.getMeatId());
                                                    hashMap2.put("MeatName", customerPaymentOrderss.getMeatName());
                                                    hashMap2.put("MeatPrice", customerPaymentOrderss.getMeatPrice());
                                                    hashMap2.put("MeatQuantity", customerPaymentOrderss.getMeatQuantity());
                                                    hashMap2.put("RandomUID", RandomUID);
                                                    hashMap2.put("TotalPrice", customerPaymentOrderss.getTotalPrice());
                                                    hashMap2.put("UserId", customerPaymentOrderss.getUserId());
                                                    FirebaseDatabase.getInstance().getReference("AdminWaitingOrders").child(AdminID).child(RandomUID).child("Meats").child(meatid).setValue(hashMap2);
                                                }
                                                DatabaseReference dataa = FirebaseDatabase.getInstance().getReference("CustomerPaymentOrders").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child(RandomUID).child("OtherInformation");
                                                dataa.addListenerForSingleValueEvent(new ValueEventListener() {
                                                    @Override
                                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                        CustomerPaymentOrders1 customerPaymentOrders11 = dataSnapshot.getValue(CustomerPaymentOrders1.class);
                                                        HashMap<String, String> hashMap3 = new HashMap<>();
                                                        hashMap3.put("Address", customerPaymentOrders11.getAddress());
                                                        hashMap3.put("GrandTotalPrice", customerPaymentOrders11.getGrandTotalPrice());
                                                        hashMap3.put("MobileNumber", customerPaymentOrders11.getMobileNumber());
                                                        hashMap3.put("Name", customerPaymentOrders11.getName());
                                                        hashMap3.put("Note", customerPaymentOrders11.getNote());
                                                        hashMap3.put("RandomUID", RandomUID);
                                                        hashMap3.put("Status", "Your order is waiting to be prepared by Admin...");
                                                        FirebaseDatabase.getInstance().getReference("AdminWaitingOrders").child(AdminID).child(RandomUID).child("OtherInformation").setValue(hashMap3).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                            @Override
                                                            public void onComplete(@NonNull Task<Void> task) {
                                                                FirebaseDatabase.getInstance().getReference("AdminPaymentOrders").child(AdminID).child(RandomUID).child("Meats").removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                    @Override
                                                                    public void onComplete(@NonNull Task<Void> task) {
                                                                        FirebaseDatabase.getInstance().getReference("AdminPaymentOrders").child(AdminID).child(RandomUID).child("OtherInformation").removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                            @Override
                                                                            public void onComplete(@NonNull Task<Void> task) {
                                                                                FirebaseDatabase.getInstance().getReference("CustomerPaymentOrders").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child(RandomUID).child("Meats").removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                                    @Override
                                                                                    public void onComplete(@NonNull Task<Void> task) {
                                                                                        FirebaseDatabase.getInstance().getReference("CustomerPaymentOrders").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child(RandomUID).child("OtherInformation").removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                                            @Override
                                                                                            public void onSuccess(Void aVoid) {
                                                                                                FirebaseDatabase.getInstance().getReference().child("Tokens").child(AdminID).child("token").addListenerForSingleValueEvent(new ValueEventListener() {
                                                                                                    @Override
                                                                                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                                                                        String usertoken = dataSnapshot.getValue(String.class);
                                                                                                        sendNotifications(usertoken, "Order Confirmed", "Payment mode is confirmed by user, Now you can start the order", "Confirm");
                                                                                                    }

                                                                                                    @Override
                                                                                                    public void onCancelled(@NonNull DatabaseError databaseError) {

                                                                                                    }
                                                                                                });

                                                                                            }
                                                                                        }).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                                            @Override
                                                                                            public void onSuccess(Void aVoid) {
                                                                                                AlertDialog.Builder builder = new AlertDialog.Builder(CustomerPayment.this);
                                                                                                builder.setMessage("Payment mode confirmed, Now you can track your order.");
                                                                                                builder.setCancelable(false);
                                                                                                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                                                                                    @Override
                                                                                                    public void onClick(DialogInterface dialog, int which) {

                                                                                                        dialog.dismiss();
                                                                                                        Intent b = new Intent(CustomerPayment.this, CustomerFoodPanel_BottomNavigation.class);
                                                                                                        b.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                                                                                        startActivity(b);
                                                                                                        finish();

                                                                                                    }
                                                                                                });
                                                                                                AlertDialog alert = builder.create();
                                                                                                alert.show();
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

    private void sendNotifications(String usertoken, String title, String message, String confirm) {
        Data data = new Data(title, message, confirm);
        NotificationSender sender = new NotificationSender(data, usertoken);
        apiService.sendNotification(sender).enqueue(new Callback<MyResponse>() {
            @Override
            public void onResponse(Call<MyResponse> call, Response<MyResponse> response) {
                if (response.code() == 200) {
                    if (response.body().success != 1) {
                        Toast.makeText(CustomerPayment.this, "Failed", Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<MyResponse> call, Throwable t) {

            }
        });
    }
}
