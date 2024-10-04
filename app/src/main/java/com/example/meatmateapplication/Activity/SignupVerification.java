package com.example.meatmateapplication.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.example.meatmateapplication.R;
import com.example.meatmateapplication.databinding.ActivitySignupVerificationBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;

public class SignupVerification extends AppCompatActivity {
    ActivitySignupVerificationBinding binding;
    private String verificationId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySignupVerificationBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        verificationId = getIntent().getStringExtra("verificationId");

        binding.doneBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String code = binding.codeEdt.getText().toString();
                if (code.isEmpty()) {
                    Toast.makeText(SignupVerification.this, "Please enter the code", Toast.LENGTH_SHORT).show();
                    return;
                }
                verifyCode(code);
            }
        });
    }

    private void verifyCode(String code) {
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationId, code);
        signInWithCredential(credential);
    }

    private void signInWithCredential(PhoneAuthCredential credential) {
        FirebaseAuth.getInstance().signInWithCredential(credential).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    Toast.makeText(SignupVerification.this, "Verification Successful", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(SignupVerification.this, MenuActivity.class));
                    finish();
                } else {
                    Toast.makeText(SignupVerification.this, "Verification failed", Toast.LENGTH_SHORT).show();
                    Log.e("SignupVerification", "Error: " + task.getException().getMessage());
                }
            }
        });
    }
}
