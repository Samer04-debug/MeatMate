package com.example.meatmateapplication.Activity.adminFoodPanel;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.meatmateapplication.Activity.ReusableCode.ReusableCodeForAll;
import com.example.meatmateapplication.Activity.SendNotification.APIService;
import com.example.meatmateapplication.Activity.SendNotification.Client;
import com.example.meatmateapplication.Activity.SendNotification.Data;
import com.example.meatmateapplication.Activity.SendNotification.MyResponse;
import com.example.meatmateapplication.Activity.SendNotification.NotificationSender;

import com.example.meatmateapplication.R;
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
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AdminPendingOrdersAdapter extends RecyclerView.Adapter<AdminPendingOrdersAdapter.ViewHolder> {
    private Context context;
    private List<AdminPendingOrders1> adminPendingOrders1list;
    private APIService apiService;
    String userid, meatid;


    public AdminPendingOrdersAdapter(Context context, List<AdminPendingOrders1> adminPendingOrders1list) {
        this.adminPendingOrders1list = adminPendingOrders1list;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.admin_orders, parent, false);
        apiService = Client.getClient("https://fcm.googleapis.com/").create(APIService.class);
        return new AdminPendingOrdersAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        final AdminPendingOrders1 adminPendingOrders1 = adminPendingOrders1list.get(position);
        holder.Address.setText(adminPendingOrders1.getAddress());
        holder.grandtotalprice.setText("GrandTotal: ₱ " + adminPendingOrders1.getGrandTotalPrice());
        final String random = adminPendingOrders1.getRandomUID();
        holder.Vieworder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, Admin_order_meats.class);
                intent.putExtra("RandomUID", random);
                context.startActivity(intent);
            }
        });

        holder.Accept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("AdminPendingOrders").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child(random).child("Meats");
                databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                            final AdminPendingOrders adminPendingOrders = snapshot.getValue(AdminPendingOrders.class);
                            HashMap<String, String> hashMap = new HashMap<>();
                            String adminid = adminPendingOrders.getAdminId();
                            String meatid = adminPendingOrders.getMeatId();
                            hashMap.put("AdminId", adminPendingOrders.getAdminId());
                            hashMap.put("MeatId", adminPendingOrders.getMeatId());
                            hashMap.put("MeatName", adminPendingOrders.getMeatName());
                            hashMap.put("MeatPrice", adminPendingOrders.getPrice());
                            hashMap.put("MeatQuantity", adminPendingOrders.getMeatQuantity());
                            hashMap.put("RandomUID", random);
                            hashMap.put("TotalPrice", adminPendingOrders.getTotalPrice());
                            hashMap.put("UserId", adminPendingOrders.getUserId());
                            FirebaseDatabase.getInstance().getReference("AdminPaymentOrders").child(meatid).child(random).child("Meats").child(meatid).setValue(hashMap);
                        }
                        DatabaseReference data = FirebaseDatabase.getInstance().getReference("AdminPendingOrders").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child(random).child("OtherInformation");
                        data.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                AdminPendingOrders1 adminPendingOrders1 = dataSnapshot.getValue(AdminPendingOrders1.class);
                                HashMap<String, String> hashMap1 = new HashMap<>();
                                hashMap1.put("Address", adminPendingOrders1.getAddress());
                                hashMap1.put("GrandTotalPrice", adminPendingOrders1.getGrandTotalPrice());
                                hashMap1.put("MobileNumber", adminPendingOrders1.getMobileNumber());
                                hashMap1.put("Name", adminPendingOrders1.getName());
                                hashMap1.put("Note",adminPendingOrders1.getNote());
                                hashMap1.put("RandomUID", random);
                                FirebaseDatabase.getInstance().getReference("AdminPaymentOrders").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child(random).child("OtherInformation").setValue(hashMap1).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {

                                        DatabaseReference Reference = FirebaseDatabase.getInstance().getReference("AdminPendingOrders").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child(random).child("Meats");
                                        Reference.addListenerForSingleValueEvent(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                                                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                                    final AdminPendingOrders adminPendingOrders = snapshot.getValue(AdminPendingOrders.class);
                                                    HashMap<String, String> hashMap2 = new HashMap<>();
                                                    userid = adminPendingOrders.getUserId();
                                                    meatid = adminPendingOrders.getMeatId();
                                                    hashMap2.put("AdminId", adminPendingOrders.getAdminId());
                                                    hashMap2.put("MeatId", adminPendingOrders.getMeatId());
                                                    hashMap2.put("MeatName", adminPendingOrders.getMeatName());
                                                    hashMap2.put("MeatPrice", adminPendingOrders.getPrice());
                                                    hashMap2.put("MeatQuantity", adminPendingOrders.getMeatQuantity());
                                                    hashMap2.put("RandomUID", random);
                                                    hashMap2.put("TotalPrice", adminPendingOrders.getTotalPrice());
                                                    hashMap2.put("UserId", adminPendingOrders.getUserId());
                                                    FirebaseDatabase.getInstance().getReference("CustomerPaymentOrders").child(userid).child(random).child("Meats").child(meatid).setValue(hashMap2);
                                                }
                                                DatabaseReference dataa = FirebaseDatabase.getInstance().getReference("AdminPendingOrders").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child(random).child("OtherInformation");
                                                dataa.addListenerForSingleValueEvent(new ValueEventListener() {
                                                    @Override
                                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                        AdminPendingOrders1 adminPendingOrders1 = dataSnapshot.getValue(AdminPendingOrders1.class);
                                                        HashMap<String, String> hashMap3 = new HashMap<>();
                                                        hashMap3.put("Address", adminPendingOrders1.getAddress());
                                                        hashMap3.put("GrandTotalPrice", adminPendingOrders1.getGrandTotalPrice());
                                                        hashMap3.put("MobileNumber", adminPendingOrders1.getMobileNumber());
                                                        hashMap3.put("Name", adminPendingOrders1.getName());
                                                        hashMap3.put("Note",adminPendingOrders1.getNote());
                                                        hashMap3.put("RandomUID", random);
                                                        FirebaseDatabase.getInstance().getReference("CustomerPaymentOrders").child(userid).child(random).child("OtherInformation").setValue(hashMap3).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                            @Override
                                                            public void onComplete(@NonNull Task<Void> task) {

                                                                FirebaseDatabase.getInstance().getReference("CustomerPendingOrders").child(userid).child(random).child("Meats").removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                    @Override
                                                                    public void onComplete(@NonNull Task<Void> task) {

                                                                        FirebaseDatabase.getInstance().getReference("CustomerPendingOrders").child(userid).child(random).child("OtherInformation").removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                            @Override
                                                                            public void onComplete(@NonNull Task<Void> task) {

                                                                                FirebaseDatabase.getInstance().getReference("AdminPendingOrders").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child(random).child("Meats").removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                                    @Override
                                                                                    public void onComplete(@NonNull Task<Void> task) {

                                                                                        FirebaseDatabase.getInstance().getReference("AdminPendingOrders").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child(random).child("OtherInformation").removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                                            @Override
                                                                                            public void onSuccess(Void aVoid) {
                                                                                                FirebaseDatabase.getInstance().getReference().child("Tokens").child(userid).child("token").addListenerForSingleValueEvent(new ValueEventListener() {
                                                                                                    @Override
                                                                                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                                                                        String usertoken = dataSnapshot.getValue(String.class);
                                                                                                        sendNotifications(usertoken, "Order Accepted", "Your Order has been Accepted by the Admin, Now make Payment for Order", "Payment");
                                                                                                        ReusableCodeForAll.ShowAlert(context,"","Wait for the Customer to make Payment");

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

        holder.Reject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                DatabaseReference Reference = FirebaseDatabase.getInstance().getReference("AdminPendingOrders").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child(random).child("Meats");
                Reference.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                            final AdminPendingOrders adminPendingOrders = snapshot.getValue(AdminPendingOrders.class);
                            userid = adminPendingOrders.getUserId();
                            meatid = adminPendingOrders.getMeatId();
                        }
                        FirebaseDatabase.getInstance().getReference().child("Tokens").child(userid).child("token").addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                String usertoken = dataSnapshot.getValue(String.class);
                                sendNotifications(usertoken, "Order Rejected", "Your Order has been Rejected by the Admin due to some Circumstances", "Home");
                                FirebaseDatabase.getInstance().getReference("CustomerPendingOrders").child(userid).child(random).child("Meats").removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {

                                        FirebaseDatabase.getInstance().getReference("CustomerPendingOrders").child(userid).child(random).child("OtherInformation").removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {

                                                FirebaseDatabase.getInstance().getReference("AdminPendingOrders").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child(random).child("Meats").removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<Void> task) {

                                                        FirebaseDatabase.getInstance().getReference("AdminPendingOrders").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child(random).child("OtherInformation").removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                                                            @Override
                                                            public void onSuccess(Void aVoid) {
                                                                FirebaseDatabase.getInstance().getReference("AlreadyOrdered").child(userid).child("isOrdered").setValue("false");
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

    private void sendNotifications(String usertoken, String title, String message, String order) {

        Data data = new Data(title, message, order);
        NotificationSender sender = new NotificationSender(data, usertoken);
        apiService.sendNotification(sender).enqueue(new Callback<MyResponse>() {
            @Override
            public void onResponse(Call<MyResponse> call, Response<MyResponse> response) {
                if (response.code() == 200) {
                    if (response.body().success != 1) {
                        Toast.makeText(context, "Failed", Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<MyResponse> call, Throwable t) {

            }
        });

    }

    @Override
    public int getItemCount() {
        return adminPendingOrders1list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView Address, grandtotalprice;
        Button Vieworder, Accept, Reject;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            Address = itemView.findViewById(R.id.AD);
            grandtotalprice = itemView.findViewById(R.id.TP);
            Vieworder = itemView.findViewById(R.id.vieww);
            Accept = itemView.findViewById(R.id.accept);
            Reject = itemView.findViewById(R.id.reject);


        }
    }
}
