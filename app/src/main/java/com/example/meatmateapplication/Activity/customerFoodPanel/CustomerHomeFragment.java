package com.example.meatmateapplication.Activity.customerFoodPanel;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.meatmateapplication.Activity.adminFoodPanel.UpdateMeatModel;
import com.example.meatmateapplication.Activity.Customer;
import com.example.meatmateapplication.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class CustomerHomeFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {


    RecyclerView recyclerView;
    private List<UpdateMeatModel> updateMeatModelList;
    private CustomerHomeAdapter adapter;
    String Barangay, City, Sub;
    DatabaseReference dataaa, databaseReference;
    SwipeRefreshLayout swipeRefreshLayout;
    SearchView searchView;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_customerhome, null);
        getActivity().setTitle("Meat On");
        setHasOptionsMenu(true);
        recyclerView = v.findViewById(R.id.recycle_menu);
        recyclerView.setHasFixedSize(true);
        Animation animation = AnimationUtils.loadAnimation(getContext(), R.anim.move);
        recyclerView.startAnimation(animation);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        updateMeatModelList = new ArrayList<>();
        swipeRefreshLayout = (SwipeRefreshLayout) v.findViewById(R.id.swipelayout);
        swipeRefreshLayout.setOnRefreshListener(this);
        swipeRefreshLayout.setColorSchemeResources(R.color.colorPrimaryDark, R.color.green);


        swipeRefreshLayout.post(new Runnable() {
            @Override
            public void run() {
                swipeRefreshLayout.setRefreshing(true);
                String userid = FirebaseAuth.getInstance().getCurrentUser().getUid();
                dataaa = FirebaseDatabase.getInstance().getReference("Customer").child(userid);
                dataaa.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        Customer cust = dataSnapshot.getValue(Customer.class);
                        Barangay = cust.getBarangay();
                        City = cust.getCity();
                        customermenu();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }
        });

        return v;
    }


    @Override
    public void onRefresh() {

        customermenu();
    }

    private void customermenu() {

        swipeRefreshLayout.setRefreshing(true);
        databaseReference = FirebaseDatabase.getInstance().getReference("MeatSupplyDetails").child(Barangay).child(City).child(Sub);
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                updateMeatModelList.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    for (DataSnapshot snapshot1 : snapshot.getChildren()) {
                        UpdateMeatModel updateDishModel = snapshot1.getValue(UpdateMeatModel.class);
                        updateMeatModelList.add(updateDishModel);
                    }
                }
                adapter = new CustomerHomeAdapter(getContext(), updateMeatModelList);
                recyclerView.setAdapter(adapter);
                swipeRefreshLayout.setRefreshing(false);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

                swipeRefreshLayout.setRefreshing(false);
            }
        });
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                search(newText);
                return true;
            }
        });

    }

    private void search(final String searchtext) {

        ArrayList<UpdateMeatModel> mylist = new ArrayList<>();
        for (UpdateMeatModel object : updateMeatModelList) {
            if (object.getMeats().toLowerCase().contains(searchtext.toLowerCase())) {
                mylist.add(object);
            }
        }
        adapter = new CustomerHomeAdapter(getContext(), mylist);
        recyclerView.setAdapter(adapter);

    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.search, menu);
        MenuItem menuItem = menu.findItem(R.id.Searchmeat);
        searchView = (SearchView) menuItem.getActionView();
        searchView.setQueryHint("Search Dish");


    }
}
