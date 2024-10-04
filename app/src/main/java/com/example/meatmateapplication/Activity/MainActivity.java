package com.example.meatmateapplication.Activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import androidx.appcompat.app.AppCompatActivity;
import com.example.meatmateapplication.R;
import com.example.meatmateapplication.databinding.ActivityMainBinding;

public class MainActivity extends BaseActivity {
    ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setVariable();
        getWindow().setStatusBarColor(Color.parseColor("#ffffff"));
    }
    private void setVariable(){
        binding.customerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mAuth.getCurrentUser() != null) {
                    startActivity(new Intent(MainActivity.this, MenuActivity.class));
                } else {
                    startActivity(new Intent(MainActivity.this, CustomerLogin.class));
                }
            }
        });
}
}
