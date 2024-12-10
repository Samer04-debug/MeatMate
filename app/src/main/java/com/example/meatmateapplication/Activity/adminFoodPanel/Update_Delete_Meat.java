package com.example.meatmateapplication.Activity.adminFoodPanel;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.meatmateapplication.Activity.Admin;
import com.example.meatmateapplication.Activity.AdminFoodPanel_BottomNavigation;
import com.example.meatmateapplication.R;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;


import java.util.UUID;




    public class Update_Delete_Meat extends AppCompatActivity {

        TextInputLayout desc, qty, pri;
        TextView Meatname;
        ImageView imageView;
        Uri imageuri;
        String dburi;
        Button Update_meat, Delete_meat;
        String description, quantity, price, meats, AdminId;
        String RandomUId;
        StorageReference ref;
        FirebaseStorage storage;
        StorageReference storageReference;
        FirebaseDatabase firebaseDatabase;
        DatabaseReference databaseReference;
        FirebaseAuth FAuth;
        String ID;
        ProgressDialog progressDialog;
        DatabaseReference dataaa;
        String Barangay, City, Sub;
        CardView cameraCard, galleryCard;

        private static final int CAM_REQ = 1;
        private static final int IMG_REQ = 2;
        private static final int CAMERA_PERMISSION_REQUEST_CODE = 101;
        private static final int STORAGE_PERMISSION_REQUEST_CODE = 102;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_update_delete_meat);

            // Initialize UI components
            desc = findViewById(R.id.description);
            qty = findViewById(R.id.quantity);
            pri = findViewById(R.id.price);
            Meatname = findViewById(R.id.meat_name);
            cameraCard = findViewById(R.id.cameraCard);
            galleryCard = findViewById(R.id.galleryCard);
            imageView = findViewById(R.id.imageView);
            Update_meat = findViewById(R.id.Updatemeat);
            Delete_meat = findViewById(R.id.Deletemeat);
            ID = getIntent().getStringExtra("updatedeletemeat");

            // Firebase initialization
            storage = FirebaseStorage.getInstance();
            storageReference = storage.getReference();
            FAuth = FirebaseAuth.getInstance();
            progressDialog = new ProgressDialog(Update_Delete_Meat.this);

            String userid = FAuth.getCurrentUser().getUid();
            dataaa = FirebaseDatabase.getInstance().getReference("Meat").child(userid);

            dataaa.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    Admin adminn = dataSnapshot.getValue(Admin.class);
                    if (adminn != null) {
                        Barangay = adminn.getBarangay();
                        City = adminn.getCity();
                        Sub = adminn.getSuburban();
                    } else {
                        Toast.makeText(Update_Delete_Meat.this, "Failed to retrieve admin data", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    setupUpdateButton();
                    setupDeleteButton();
                    loadMeatData();
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    Toast.makeText(Update_Delete_Meat.this, "Failed: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });

            cameraCard.setOnClickListener(v -> requestCameraPermission());
            galleryCard.setOnClickListener(v -> requestStoragePermission());
        }

        private void setupUpdateButton() {
            Update_meat.setOnClickListener(v -> {
                description = desc.getEditText().getText().toString().trim();
                quantity = qty.getEditText().getText().toString().trim();
                price = pri.getEditText().getText().toString().trim();

                if (isValid()) {
                    if (imageuri != null) {
                        uploadImage();
                    } else {
                        updatedesc(dburi);
                    }
                }
            });
        }

        private void setupDeleteButton() {
            Delete_meat.setOnClickListener(v -> {
                new AlertDialog.Builder(Update_Delete_Meat.this)
                        .setMessage("Are you sure you want to Delete Meat?")
                        .setPositiveButton("YES", (dialog, which) -> deleteMeat())
                        .setNegativeButton("NO", (dialog, which) -> dialog.cancel())
                        .show();
            });
        }

        private void deleteMeat() {
            String useridd = FAuth.getCurrentUser().getUid();
            FirebaseDatabase.getInstance().getReference("MeatSupplyDetails")
                    .child(Barangay)
                    .child(City)
                    .child(Sub)
                    .child(useridd)
                    .child(ID)
                    .removeValue()
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            new AlertDialog.Builder(Update_Delete_Meat.this)
                                    .setMessage("Your Meat has been Deleted")
                                    .setPositiveButton("OK", (dialog, which) -> {
                                        startActivity(new Intent(Update_Delete_Meat.this, AdminFoodPanel_BottomNavigation.class));
                                    })
                                    .show();
                        }
                    });
        }

        private void loadMeatData() {
            String useridd = FAuth.getCurrentUser().getUid();
            databaseReference = FirebaseDatabase.getInstance().getReference("MeatSupplyDetails")
                    .child(Barangay)
                    .child(City)
                    .child(Sub)
                    .child(useridd)
                    .child(ID);

            databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    UpdateMeatModel updateMeatModel = dataSnapshot.getValue(UpdateMeatModel.class);
                    if (updateMeatModel != null) {
                        desc.getEditText().setText(updateMeatModel.getDescription());
                        qty.getEditText().setText(updateMeatModel.getQuantity());
                        Meatname.setText("Meat name: " + updateMeatModel.getMeats());
                        meats = updateMeatModel.getMeats();
                        pri.getEditText().setText(updateMeatModel.getPrice());
                        dburi = updateMeatModel.getImageURL();
                        Glide.with(Update_Delete_Meat.this).load(dburi).into(imageView);
                    } else {
                        Toast.makeText(Update_Delete_Meat.this, "No data available for this meat", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    Toast.makeText(Update_Delete_Meat.this, "Failed: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }

        private void requestCameraPermission() {
            if (checkSelfPermission(Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
                launchCamera();
            } else {
                requestPermissions(new String[]{Manifest.permission.CAMERA}, CAMERA_PERMISSION_REQUEST_CODE);
            }
        }

        private void requestStoragePermission() {
            if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                launchGallery();
            } else {
                requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, STORAGE_PERMISSION_REQUEST_CODE);
            }
        }

        private void launchCamera() {
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            startActivityForResult(intent, CAM_REQ);
        }

        private void launchGallery() {
            Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(intent, IMG_REQ);
        }

        private boolean isValid() {
            desc.setErrorEnabled(false);
            desc.setError("");
            qty.setErrorEnabled(false);
            qty.setError("");
            pri.setErrorEnabled(false);
            pri.setError("");

            boolean isValiDescription = false, isValidPrice = false, isvalidQuantity = false, isvalid = false;
            if (TextUtils.isEmpty(description)) {
                desc.setErrorEnabled(true);
                desc.setError("Description is Required");

            } else {

                desc.setError(null);
                isValiDescription = true;
            }
            if (TextUtils.isEmpty(quantity)) {
                qty.setErrorEnabled(true);
                qty.setError("Quantity is Required");
            } else {
                isvalidQuantity = true;
            }
            if (TextUtils.isEmpty(price)) {
                pri.setErrorEnabled(true);
                pri.setError("Price is Required");
            } else {
                isValidPrice = true;
            }
            isvalid = (isValiDescription && isvalidQuantity && isValidPrice) ? true : false;

            return isvalid;
        }

        private void uploadImage() {
            progressDialog.setMessage("Uploading image...");
            progressDialog.show();

            RandomUId = UUID.randomUUID().toString();
            ref = storageReference.child("Meat Images/" + RandomUId);

            ref.putFile(imageuri)
                    .addOnSuccessListener(taskSnapshot -> ref.getDownloadUrl()
                            .addOnSuccessListener(uri -> updatedesc(uri.toString())))
                    .addOnFailureListener(e -> {
                        progressDialog.dismiss();
                        Toast.makeText(Update_Delete_Meat.this, "Image upload failed: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    });
        }

        private void updatedesc(String uri) {
            AdminId = FirebaseAuth.getInstance().getCurrentUser().getUid();
            FoodSupplyDetails info = new FoodSupplyDetails(meats, quantity, price, description, uri, ID, AdminId);
            firebaseDatabase.getInstance().getReference("MeatSupplyDetails").child(Barangay).child(City).child(Sub)
                    .child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child(ID)
                    .setValue(info).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            progressDialog.dismiss();
                            Toast.makeText(Update_Delete_Meat.this, "Dish Updated Successfully", Toast.LENGTH_SHORT).show();
                        }
                    });
        }
    }
