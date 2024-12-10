package com.example.meatmateapplication.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.meatmateapplication.R;

public class MainActivity extends BaseActivity {
    Button Admin,Customer,Courier;
    Intent intent;
    String type;
    ConstraintLayout bgimage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        intent = getIntent();
        type = intent.getStringExtra("Home").toString().trim();

        Admin = (Button)findViewById(R.id.adminBtn);
        Courier = (Button)findViewById(R.id.courierBtn);
        Customer = (Button)findViewById(R.id.customerBtn);


        Admin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(type.equals("Email")){
                    Intent loginemail  = new Intent(MainActivity.this,AdminLogin.class);
                    startActivity(loginemail);
                    finish();
                }
                if(type.equals("Phone")){
                    Intent loginphone  = new Intent(MainActivity.this,Adminloginphone.class);
                    startActivity(loginphone);
                    finish();
                }
                if(type.equals("SignUp")){
                    Intent Register  = new Intent(MainActivity.this,AdminSignup.class);
                    startActivity(Register);
                }
            }
        });

        Customer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(type.equals("Email")){
                    Intent loginemailcust  = new Intent(MainActivity.this,CustomerLogin.class);
                    startActivity(loginemailcust);
                    finish();
                }
                if(type.equals("Phone")){
                    Intent loginphonecust  = new Intent(MainActivity.this,CustomerLoginphone.class);
                    startActivity(loginphonecust);
                    finish();
                }
                if(type.equals("SignUp")){
                    Intent Registercust  = new Intent(MainActivity.this,CustomerSignup.class);
                    startActivity(Registercust);
                }

            }
        });

        Courier.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(type.equals("Email")){
                    Intent loginemail = new Intent(MainActivity.this,CourierLogin.class);
                    startActivity(loginemail);
                    finish();
                }
                if(type.equals("Phone")){
                    Intent loginphone  = new Intent(MainActivity.this,CourierLoginphone.class);
                    startActivity(loginphone);
                    finish();
                }
                if(type.equals("SignUp")){
                    Intent Registerdelivery  = new Intent(MainActivity.this,CourierSignup.class);
                    startActivity(Registerdelivery);
                }

            }
        });

    }
}