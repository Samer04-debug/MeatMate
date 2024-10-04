package com.example.meatmateapplication.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.meatmateapplication.R;
import com.example.meatmateapplication.databinding.ActivityCustomerLoginBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.FirebaseException;



import java.util.concurrent.TimeUnit;

public class CustomerLogin extends AppCompatActivity {
    ActivityCustomerLoginBinding binding;
    FirebaseAuth mAuth;
    private String verificationId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCustomerLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        mAuth = FirebaseAuth.getInstance();
        setVariable();
    }

    private void setVariable() {
        binding.loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String input = binding.userlogEdt.getText().toString();
                String password = binding.passlogEdt.getText().toString();

                if (input.isEmpty()) {
                    Toast.makeText(CustomerLogin.this, "Please enter email address/phone number and password", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (!password.isEmpty()) {
                    // Check if input is an email or phone number (assuming +63 or 09 for phone numbers)
                    if (input.contains("@")) {
                        // Email login
                        loginWithEmail(input, password);
                    } else {
                        // Phone number login
                        loginWithPhoneNumber(input);
                    }
                } else {
                    Toast.makeText(CustomerLogin.this, "Please enter password", Toast.LENGTH_SHORT).show();
                }
            }
        });

        binding.signuptxtBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(CustomerLogin.this, CustomerSignup.class));
            }
        });
    }

    private void loginWithEmail(String email, String password) {
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(CustomerLogin.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            startActivity(new Intent(CustomerLogin.this, MenuActivity.class));
                            finish();
                        } else {
                            Toast.makeText(CustomerLogin.this, "Authentication failed", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void loginWithPhoneNumber(String phoneNumber) {
        // Send verification code to the phone number (assuming country code +63 for Philippines)
        PhoneAuthOptions options = PhoneAuthOptions.newBuilder(mAuth)
                .setPhoneNumber(phoneNumber.startsWith("+63") ? phoneNumber : "+63" + phoneNumber.substring(1))  // format phone number to +63
                .setTimeout(60L, TimeUnit.SECONDS)
                .setActivity(this)
                .setCallbacks(new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                    @Override
                    public void onVerificationCompleted(@NonNull PhoneAuthCredential credential) {
                        // Auto verification
                        signInWithPhoneAuthCredential(credential);
                    }

                    @Override
                    public void onVerificationFailed(@NonNull FirebaseException e) {
                        Toast.makeText(CustomerLogin.this, "Verification Failed: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onCodeSent(@NonNull String verificationId, @NonNull PhoneAuthProvider.ForceResendingToken token) {
                        super.onCodeSent(verificationId, token);
                        CustomerLogin.this.verificationId = verificationId;
                        // Redirect to code input activity
                        Intent intent = new Intent(CustomerLogin.this, SignupVerification.class);
                        intent.putExtra("verificationId", verificationId);
                        intent.putExtra("phoneNumber", phoneNumber);
                        startActivity(intent);
                    }
                })
                .build();
        PhoneAuthProvider.verifyPhoneNumber(options);
    }

    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, navigate to menu
                            startActivity(new Intent(CustomerLogin.this, MenuActivity.class));
                            finish();
                        } else {
                            // Sign in failed
                            Toast.makeText(CustomerLogin.this, "Phone Authentication Failed", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}
