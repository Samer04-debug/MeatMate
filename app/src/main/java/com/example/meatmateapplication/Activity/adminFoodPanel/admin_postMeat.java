package com.example.meatmateapplication.Activity.adminFoodPanel;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.meatmateapplication.Activity.Admin;
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

import java.util.Objects;
import java.util.UUID;

import com.example.meatmateapplication.R;
import com.google.firebase.storage.UploadTask;

public class admin_postMeat extends AppCompatActivity {

    ImageView imageView;
    CardView cameraCard, galleryCard;
    private final int CAM_REQ = 1000;
    private final int IMG_REQ = 2000;
    Uri imageUri;
    Button post_meat;
    Spinner Meats;
    TextInputLayout desc, qty, pri;
    String description, quantity, price, meats;
    FirebaseStorage storage;
    StorageReference storageReference;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference, dataRef;
    FirebaseAuth Fauth;
    String AdminId, RandomUID, Barangay, City, Area;

    private static final int CAMERA_PERMISSION_REQUEST_CODE = 101;
    private static final int STORAGE_PERMISSION_REQUEST_CODE = 102;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_post_meat);

        // Initialize UI elements
        imageView = findViewById(R.id.imageView);
        cameraCard = findViewById(R.id.cameraCard);
        galleryCard = findViewById(R.id.galleryCard);
        post_meat = findViewById(R.id.post);
        Meats = findViewById(R.id.meats);
        desc = findViewById(R.id.description);
        qty = findViewById(R.id.quantity);
        pri = findViewById(R.id.price);

        // Initialize Firebase instances
        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();
        Fauth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference("MeatDetails");

        Window w = getWindow();
        w.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);


        // Handle camera click event
        cameraCard.setOnClickListener(view -> {
            if (ContextCompat.checkSelfPermission(admin_postMeat.this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(admin_postMeat.this, new String[]{Manifest.permission.CAMERA}, CAMERA_PERMISSION_REQUEST_CODE);
            } else {
                launchCamera();
            }
        });

        // Handle gallery click event
        galleryCard.setOnClickListener(view -> {
            if (ContextCompat.checkSelfPermission(admin_postMeat.this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(admin_postMeat.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, STORAGE_PERMISSION_REQUEST_CODE);
            } else {
                launchGallery();
            }
        });

        try {
            String userid = FirebaseAuth.getInstance().getCurrentUser().getUid();
            dataRef = firebaseDatabase.getInstance().getReference("Admin").child(userid);
            dataRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {

                    Admin adminn = snapshot.getValue(Admin.class);
                    Barangay = adminn.getBarangay();
                    City = adminn.getCity();
                    Area = adminn.getArea();
                    imageView = findViewById(R.id.imageView);


                    post_meat.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            meats = Meats.getSelectedItem().toString().trim();
                            description = Objects.requireNonNull(desc.getEditText()).getText().toString().trim();
                            quantity = Objects.requireNonNull(qty.getEditText()).getText().toString().trim();
                            price = Objects.requireNonNull(pri.getEditText()).getText().toString().trim();

                            if(isValid()){
                                uploadImage();
                            }
                        }
                    });
                }



                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }catch (Exception e){
            Log.e("Error: ",e.getMessage());
        }

    }
    private void launchCamera() {
        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(cameraIntent, CAM_REQ);
    }

    private void launchGallery() {
        Intent galleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(galleryIntent, IMG_REQ);
    }


    private void uploadImage() {

        if(imageUri != null){
            final ProgressDialog progressDialog = new ProgressDialog(admin_postMeat.this);
            progressDialog.setTitle("Uploading.....");
            progressDialog.show();
            RandomUID = UUID.randomUUID().toString();
            StorageReference ref = storageReference.child(RandomUID);
            AdminId = FirebaseAuth.getInstance().getCurrentUser().getUid();
            ref.putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    ref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            MeatDetails info = new MeatDetails(meats, quantity, price, description, uri.toString(), RandomUID, AdminId);
                            firebaseDatabase.getInstance().getReference("MeatDetails").child(Barangay).child(City).child(Area).child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child(RandomUID)
                                    .setValue(info).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {

                                            progressDialog.dismiss();
                                            Toast.makeText(admin_postMeat.this,"Meat Posted Successfully!",Toast.LENGTH_SHORT).show();
                                        }
                                    });

                        }
                    });
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    progressDialog.dismiss();
                    Toast.makeText(admin_postMeat.this,e.getMessage(),Toast.LENGTH_SHORT).show();
                }
            }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onProgress(@NonNull UploadTask.TaskSnapshot taskSnapshot) {
                    double progress = (100.0*taskSnapshot.getBytesTransferred()/taskSnapshot.getTotalByteCount());
                    progressDialog.setMessage("Uploaded "+(int) progress+"%");
                    progressDialog.setCanceledOnTouchOutside(false);
                }
            });
        }

    }

    private boolean isValid() {

        desc.setErrorEnabled(false);
        desc.setError("");
        qty.setErrorEnabled(false);
        qty.setError("");
        pri.setErrorEnabled(false);
        pri.setError("");

        boolean isValidDescription = false,isValidPrice=false,isValidQuantity=false,isValid=false;
        if(TextUtils.isEmpty(description)){
            desc.setErrorEnabled(true);
            desc.setError("Description is Required");
        }else{
            desc.setError(null);
            isValidDescription=true;
        }
        if(TextUtils.isEmpty(quantity)){
            qty.setErrorEnabled(true);
            qty.setError("Enter number of Items");
        }else{
            isValidQuantity=true;
        }
        if(TextUtils.isEmpty(price)){
            pri.setErrorEnabled(true);
            pri.setError("Please Mention Price");
        }else{
            isValidPrice=true;
        }
        isValid = (isValidDescription && isValidQuantity && isValidPrice)?true:false;
        return isValid;
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK && data != null) {
            if (requestCode == CAM_REQ) {
                Bitmap camBitmap = (Bitmap) data.getExtras().get("data");
                if (camBitmap != null) {
                    imageView.setImageBitmap(camBitmap);
                } else {
                    Toast.makeText(this, "Failed to capture image", Toast.LENGTH_SHORT).show();
                }
            } else if (requestCode == IMG_REQ) {
                imageUri = data.getData();
                if (imageUri != null) {
                    imageView.setImageURI(imageUri);
                } else {
                    Toast.makeText(this, "Failed to select image", Toast.LENGTH_SHORT).show();
                }
            }
        } else {
            Toast.makeText(this, "Action cancelled", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == CAMERA_PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                launchCamera();
            } else {
                Toast.makeText(this, "Camera permission is required", Toast.LENGTH_SHORT).show();
            }
        } else if (requestCode == STORAGE_PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                launchGallery();
            } else {
                Toast.makeText(this, "Storage permission is required", Toast.LENGTH_SHORT).show();
            }
        }
    }
}