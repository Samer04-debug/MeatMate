package com.example.meatmateapplication.Activity.customerFoodPanel;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.ImageView;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.meatmateapplication.Activity.Admin;
import com.example.meatmateapplication.Activity.CustomerFoodPanel_BottomNavigation;
import com.example.meatmateapplication.Activity.UpdateMeatModel;
import com.example.meatmateapplication.Activity.Admin;
import com.example.meatmateapplication.Activity.Customer;

import com.example.meatmateapplication.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class OrderMeat extends AppCompatActivity {


    String RandomId, AdminID;
    ImageView imageView;
    NumberPicker additem;
    TextView Meatname, AdminName, AdminLoaction, MeatQuantity, MeatPrice, MeatDescription;
    DatabaseReference databaseReference, dataaa, admindata, reference, data, dataref;
    String Barangay, City, meatname;
    int meatprice;
    String custID;
    FirebaseDatabase firebaseDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_meat);


        Meatname = (TextView) findViewById(R.id.meat_name);
        AdminName = (TextView) findViewById(R.id.admin_name);
        AdminLoaction = (TextView) findViewById(R.id.admin_location);
        MeatQuantity = (TextView) findViewById(R.id.meat_quantity);
        MeatPrice = (TextView) findViewById(R.id.meat_price);
        MeatDescription = (TextView) findViewById(R.id.meat_description);
        imageView = (ImageView) findViewById(R.id.image);
        additem = (NumberPicker) findViewById(R.id.number_btn);

        final String userid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        dataaa = FirebaseDatabase.getInstance().getReference("Customer").child(userid);
        dataaa.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Customer cust = dataSnapshot.getValue(Customer.class);
                Barangay = cust.getBarangay();
                City = cust.getCity();


                RandomId = getIntent().getStringExtra("MeatMenu");
                AdminID = getIntent().getStringExtra("AdminId");

                databaseReference = FirebaseDatabase.getInstance().getReference("MeatSupplyDetails").child(Barangay).child(City).child(AdminID).child(RandomId);
                databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        UpdateMeatModel updateMeatModel = dataSnapshot.getValue(UpdateMeatModel.class);
                        Meatname.setText(updateMeatModel.getMeats());
                        String qua = "<b>" + "Quantity: " + "</b>" + updateMeatModel.getQuantity();
                        MeatQuantity.setText(Html.fromHtml(qua));
                        String ss = "<b>" + "Description: " + "</b>" + updateMeatModel.getDescription();
                        MeatDescription.setText(Html.fromHtml(ss));
                        String pri = "<b>" + "Price: ₱ " + "</b>" + updateMeatModel.getPrice();
                        MeatPrice.setText(Html.fromHtml(pri));
                        Glide.with(OrderMeat.this).load(updateMeatModel.getImageURL()).into(imageView);

                        admindata = FirebaseDatabase.getInstance().getReference("Admin").child(AdminID);
                        admindata.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                Admin admin = dataSnapshot.getValue(Admin.class);

                                String name = "<b>" + "Admin Name: " + "</b>" + admin.getFname() + " " + admin.getLname();
                                AdminName.setText(Html.fromHtml(name));
                                String loc = "<b>" + "Location: " + "</b>" + admin.getSuburban();
                                AdminLoaction.setText(Html.fromHtml(loc));
                                custID = FirebaseAuth.getInstance().getCurrentUser().getUid();
                                databaseReference = FirebaseDatabase.getInstance().getReference("Cart").child("CartItems").child(custID).child(RandomId);
                                databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                        Cart cart = dataSnapshot.getValue(Cart.class);
                                        if (dataSnapshot.exists()) {
                                            additem.setValue(Integer.parseInt(cart.getMeatQuantity()));
                                        }
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

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
                additem.setOnClickListener(new NumberPicker.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        dataref = FirebaseDatabase.getInstance().getReference("Cart").child("CartItems").child(FirebaseAuth.getInstance().getCurrentUser().getUid());
                        dataref.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                Cart cart1=null;
                                if (dataSnapshot.exists()) {
                                    int totalcount=0;
                                    for(DataSnapshot snapshot:dataSnapshot.getChildren()){
                                        totalcount++;
                                    }
                                    int i=0;
                                    for(DataSnapshot snapshot:dataSnapshot.getChildren()){
                                        i++;
                                        if(i==totalcount){
                                            cart1= snapshot.getValue(Cart.class);
                                        }
                                    }

                                    if (AdminID.equals(cart1.getAdminId())) {
                                        data = FirebaseDatabase.getInstance().getReference("MeatSupplyDetails").child(Barangay).child(City).child(AdminID).child(RandomId);
                                        data.addListenerForSingleValueEvent(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                UpdateMeatModel update = dataSnapshot.getValue(UpdateMeatModel.class);
                                                meatname = update.getMeats();
                                                meatprice = Integer.parseInt(update.getPrice());

                                                int num = additem.getValue();
                                                int totalprice = num * meatprice;
                                                if (num != 0) {
                                                    HashMap<String, String> hashMap = new HashMap<>();
                                                    hashMap.put("MeatName", meatname);
                                                    hashMap.put("MeatID", RandomId);
                                                    hashMap.put("MeatQuantity", String.valueOf(num));
                                                    hashMap.put("Price", String.valueOf(meatprice));
                                                    hashMap.put("Totalprice", String.valueOf(totalprice));
                                                    hashMap.put("AdminId", AdminID);
                                                    custID = FirebaseAuth.getInstance().getCurrentUser().getUid();
                                                    reference = FirebaseDatabase.getInstance().getReference("Cart").child("CartItems").child(custID).child(RandomId);
                                                    reference.setValue(hashMap).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                        @Override
                                                        public void onSuccess(Void aVoid) {

                                                            Toast.makeText(OrderMeat.this, "Added to cart", Toast.LENGTH_SHORT).show();
                                                        }
                                                    });

                                                } else {

                                                    firebaseDatabase.getInstance().getReference("Cart").child(custID).child(RandomId).removeValue();
                                                }
                                            }

                                            @Override
                                            public void onCancelled(@NonNull DatabaseError databaseError) {

                                            }
                                        });
                                    }
                                    else
                                    {
                                        AlertDialog.Builder builder = new AlertDialog.Builder(OrderMeat.this);
                                        builder.setMessage("You can't add meat items of multiple admin at a time. Try to add items of same admin");
                                        builder.setCancelable(false);
                                        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {

                                                dialog.dismiss();
                                                Intent intent = new Intent(OrderMeat.this, CustomerFoodPanel_BottomNavigation.class);
                                                startActivity(intent);
                                                finish();

                                            }
                                        });
                                        AlertDialog alert = builder.create();
                                        alert.show();
                                    }
                                } else {
                                    data = FirebaseDatabase.getInstance().getReference("MeatSupplyDetails").child(Barangay).child(City).child(AdminID).child(RandomId);
                                    data.addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                            UpdateMeatModel update = dataSnapshot.getValue(UpdateMeatModel.class);
                                            meatname = update.getMeats();
                                            meatprice = Integer.parseInt(update.getPrice());
                                            int num = additem.getValue();
                                            int totalprice = num * meatprice;
                                            if (num != 0) {
                                                HashMap<String, String> hashMap = new HashMap<>();
                                                hashMap.put("MeatName", meatname);
                                                hashMap.put("MeatID", RandomId);
                                                hashMap.put("MeatQuantity", String.valueOf(num));
                                                hashMap.put("Price", String.valueOf(meatprice));
                                                hashMap.put("Totalprice", String.valueOf(totalprice));
                                                hashMap.put("AdminId", AdminID);
                                                custID = FirebaseAuth.getInstance().getCurrentUser().getUid();
                                                reference = FirebaseDatabase.getInstance().getReference("Cart").child("CartItems").child(custID).child(RandomId);
                                                reference.setValue(hashMap).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                    @Override
                                                    public void onSuccess(Void aVoid) {

                                                        Toast.makeText(OrderMeat.this, "Added to cart", Toast.LENGTH_SHORT).show();
                                                    }
                                                });

                                            } else {

                                                firebaseDatabase.getInstance().getReference("Cart").child(custID).child(RandomId).removeValue();
                                            }
                                        }

                                        @Override
                                        public void onCancelled(@NonNull DatabaseError databaseError) {

                                        }
                                    });
                                }
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
}


