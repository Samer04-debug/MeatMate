package com.example.meatmateapplication.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import com.example.meatmateapplication.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class IntroActivity extends BaseActivity {

    private FirebaseAuth auth;
    private Button startBtn;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        auth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = auth.getCurrentUser();

        if (currentUser != null && currentUser.isEmailVerified()) {
            // User is signed in and verified, redirect to Dashboard
            startActivity(new Intent(IntroActivity.this, MainMenu.class));
            finish();
        } else {
            // User is not signed in, show Intro screen
            setContentView(R.layout.activity_intro);

            // Find the button and set a click listener on it
            startBtn = findViewById(R.id.startBtn); // Replace with your button's ID
            startBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Add action for the button click, e.g., navigate to the login screen
                    startActivity(new Intent(IntroActivity.this, MainMenu.class));
                }
            });
        }
    }
}
