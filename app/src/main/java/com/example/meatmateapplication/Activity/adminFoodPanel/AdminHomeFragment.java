package com.example.meatmateapplication.Activity.adminFoodPanel;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.meatmateapplication.Activity.Admin;
import androidx.recyclerview.widget.RecyclerView;
import com.example.meatmateapplication.Activity.MainMenu;
import com.example.meatmateapplication.R;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.example.meatmateapplication.Activity.UpdateMeatModel;

import java.util.ArrayList;
import java.util.List;

public class AdminHomeFragment extends Fragment {


    RecyclerView recyclerView;
    private List<UpdateMeatModel> updateMeatModelList;
    private AdminhomeAdapter adapter;
    DatabaseReference dataaa;
    private String Barangay, City, Sub;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_admin_home, null);
        getActivity().setTitle("Meats On");
        setHasOptionsMenu(true);
        recyclerView = v.findViewById(R.id.Recycle_menu);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        updateMeatModelList = new ArrayList<>();
        String userid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        dataaa = FirebaseDatabase.getInstance().getReference("Admin").child(userid);
        dataaa.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Admin adminn = dataSnapshot.getValue(Admin.class);
                Barangay = adminn.getBarangay();
                City = adminn.getCity();
                Sub = adminn.getSuburban();
                adminMeats();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        return v;
    }


    private void adminMeats() {

        String useridd = FirebaseAuth.getInstance().getCurrentUser().getUid();
        DatabaseReference databaseReference = FirebaseDatabase.getInstance()
                .getReference("MeatSupplyDetails")
                .child(Barangay)
                .child(City)
                .child(Sub)
                .child(useridd);
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                updateMeatModelList.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    UpdateMeatModel updateMeatModel = snapshot.getValue(UpdateMeatModel.class);
                    updateMeatModelList.add(updateMeatModel);

                }
                adapter = new AdminhomeAdapter(getContext(), updateMeatModelList);
                recyclerView.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.logout, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int idd = item.getItemId();
        if (idd == R.id.LOGOUT) {
            Logout();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void Logout() {

        FirebaseAuth.getInstance().signOut();
        Intent intent = new Intent(getActivity(), MainMenu.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK );
        startActivity(intent);

    }



}