package com.example.meatmateapplication.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.meatmateapplication.R;
import com.example.meatmateapplication.databinding.ActivityCustomerSignupBinding;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.auth.FirebaseAuth;

import java.util.concurrent.TimeUnit;

public class CustomerSignup extends BaseActivity {
    ActivityCustomerSignupBinding binding;
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks;
    private String verificationId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCustomerSignupBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setVariable();
        setupCallbacks();
    }

    private void setVariable() {
        binding.signupBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = binding.userEdt.getText().toString().trim(); // Full name
                String email = binding.emailEdt.getText().toString().trim(); // Email address
                String number = binding.numberEdt.getText().toString().trim(); // Phone number
                String password = binding.passEdt.getText().toString().trim(); // Password

                if (name.isEmpty() || email.isEmpty() || number.isEmpty() || password.isEmpty()) {
                    Toast.makeText(CustomerSignup.this, "Please enter a name", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                    Toast.makeText(CustomerSignup.this, "Please enter a valid email address", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (password.length() < 6) {
                    Toast.makeText(CustomerSignup.this, "Password must be at least 6 characters long", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (!isValidPhilippinesNumber(number)) {
                    Toast.makeText(CustomerSignup.this, "Please enter a valid phone number in +639 format", Toast.LENGTH_SHORT).show();
                    return;
                }

                // Send OTP to the phone number
                sendVerificationCode(number);
            }
        });
    }

    // Validate phone number in +639XXXXXXXXX format
    private boolean isValidPhilippinesNumber(String number) {
        // Ensure the number starts with +639 and is 13 characters long (including +63)
        return number.startsWith("+639") && number.length() == 13;
    }

    // Sends verification code to the phone number using Firebase
    private void sendVerificationCode(String number) {
        PhoneAuthOptions options = PhoneAuthOptions.newBuilder(FirebaseAuth.getInstance())
                .setPhoneNumber(number)       // Phone number to verify
                .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
                .setActivity(this)             // Activity (for callback binding)
                .setCallbacks(mCallbacks)      // OnVerificationStateChangedCallbacks
                .build();
        PhoneAuthProvider.verifyPhoneNumber(options);
    }

    // Setup the verification callbacks for OTP
    private void setupCallbacks() {
        mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            @Override
            public void onVerificationCompleted(@NonNull PhoneAuthCredential credential) {
                // Automatically verifies in some cases
                Toast.makeText(CustomerSignup.this, "Verification Completed", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onVerificationFailed(@NonNull FirebaseException e) {
                // Failed verification
                Toast.makeText(CustomerSignup.this, "Verification Failed: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                Log.e("CustomerSignup", "Verification failed", e);
            }

            @Override
            public void onCodeSent(@NonNull String verificationId, @NonNull PhoneAuthProvider.ForceResendingToken token) {
                // OTP sent, move to the verification screen
                super.onCodeSent(verificationId, token);
                CustomerSignup.this.verificationId = verificationId;

                // Start verification activity
                Intent intent = new Intent(CustomerSignup.this, SignupVerification.class);
                intent.putExtra("verificationId", verificationId);
                intent.putExtra("phoneNumber", binding.numberEdt.getText().toString().trim());
                startActivity(intent);
            }
        };
    }
}
