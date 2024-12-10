package com.example.meatmateapplication.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.os.Bundle;
import android.view.MenuItem;

import com.example.meatmateapplication.Activity.adminFoodPanel.AdminHomeFragment;
import com.example.meatmateapplication.Activity.adminFoodPanel.AdminOrderFragment;
import com.example.meatmateapplication.Activity.adminFoodPanel.AdminPendingOrderFragment;
import com.example.meatmateapplication.Activity.adminFoodPanel.AdminProfileFragment;
import com.example.meatmateapplication.Activity.customerFoodPanel.CustomerCartFragment;
import com.example.meatmateapplication.Activity.customerFoodPanel.CustomerHomeFragment;
import com.example.meatmateapplication.Activity.customerFoodPanel.CustomerOrdersFragment;
import com.example.meatmateapplication.Activity.customerFoodPanel.CustomerProfileFragment;
import com.example.meatmateapplication.Activity.customerFoodPanel.CustomerTrackFragment;
import com.example.meatmateapplication.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;


public class CustomerFoodPanel_BottomNavigation extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_food_panel_bottom_navigation);
        BottomNavigationView navigationView = findViewById(R.id.bottom_navigation);
        navigationView.setOnNavigationItemSelectedListener(this);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        Fragment fragment = null;
        if (item.getItemId() == R.id.cust_Home) {
            fragment = new Fragment();
        } else if (item.getItemId() == R.id.cart) {
            fragment = new CustomerCartFragment();
        } else if (item.getItemId() == R.id.Cust_order) {
            fragment = new CustomerOrdersFragment();
        } else if (item.getItemId() == R.id.track) {
            fragment = new CustomerTrackFragment();
        }else if (item.getItemId() == R.id.cust_profile) {
            fragment = new CustomerProfileFragment();
        }
        return loadadminfragment(fragment);
    }

    private boolean loadadminfragment(Fragment fragment) {

        if(fragment != null){
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,fragment).commit();
            return true;
        }
        return false;
    }
}