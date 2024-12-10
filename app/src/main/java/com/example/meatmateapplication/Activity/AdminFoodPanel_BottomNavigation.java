package com.example.meatmateapplication.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.MenuItem;

import com.example.meatmateapplication.Activity.adminFoodPanel.AdminHomeFragment;
import com.example.meatmateapplication.Activity.adminFoodPanel.AdminOrderFragment;
import com.example.meatmateapplication.Activity.adminFoodPanel.AdminPendingOrderFragment;
import com.example.meatmateapplication.Activity.adminFoodPanel.AdminProfileFragment;

import com.example.meatmateapplication.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;



public class AdminFoodPanel_BottomNavigation extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_food_panel_bottom_navigation);
        BottomNavigationView navigationView = findViewById(R.id.admin_bottom_food_navigation);
        navigationView.setOnNavigationItemSelectedListener(this);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        Fragment fragment = null;
        if (item.getItemId() == R.id.adminHome) {
            fragment = new AdminHomeFragment();
        } else if (item.getItemId() == R.id.PendingOrders) {
            fragment = new AdminPendingOrderFragment();
        } else if (item.getItemId() == R.id.Orders) {
            fragment = new AdminOrderFragment();
        } else if (item.getItemId() == R.id.adminProfile) {
            fragment = new AdminProfileFragment();
        }
        return loadadminfragment(fragment);
    }

    private boolean loadadminfragment(Fragment fragment) {

        if (fragment != null){
            getSupportFragmentManager().beginTransaction().replace(R.id.frame_container,fragment).commit();
            return true;
        }
        return false;
    }
}