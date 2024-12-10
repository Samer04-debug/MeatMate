package com.example.meatmateapplication.Activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class AuthCheckActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private FirebaseDatabase database;
    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Initialize Firebase
        mAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();

        // Delay for splash screen or initial load
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                checkUserAuthentication();
            }
        }, 3000); // 3-second delay for demonstration; adjust as needed
    }

    private void checkUserAuthentication() {
        if (mAuth.getCurrentUser() != null) {
            if (mAuth.getCurrentUser().isEmailVerified()) {
                databaseReference = database.getReference("User").child(mAuth.getUid() + "/Role");
                databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        String role = snapshot.getValue(String.class);
                        if ("Admin".equals(role)) {
                            startActivity(new Intent(AuthCheckActivity.this, AdminFoodPanel_BottomNavigation.class));
                        } else if ("Customer".equals(role)) {
                            startActivity(new Intent(AuthCheckActivity.this, CustomerFoodPanel_BottomNavigation.class));
                        }    if(role.equals("Courier")) {
                            startActivity(new Intent(AuthCheckActivity.this, DeliveryFoodPanel_BottomNavigation.class));
                            finish();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Toast.makeText(AuthCheckActivity.this, error.getMessage(), Toast.LENGTH_LONG).show();
                    }
                });
            } else {
                showVerificationDialog();
            }
        } else {
            startActivity(new Intent(AuthCheckActivity.this, MainMenu.class));
            finish();
        }
    }

    private void showVerificationDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(AuthCheckActivity.this);
        builder.setMessage("Check whether you have verified your details. Otherwise, please verify.")
                .setCancelable(false)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        startActivity(new Intent(AuthCheckActivity.this, MainActivity.class));
                        finish();
                    }
                });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
        mAuth.signOut();
    }
}
