package com.example.meatmateapplication.Activity.adminFoodPanel;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.meatmateapplication.R;

public class AdminProfileFragment extends Fragment {

    private Button postMeat;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_admin_profile, container, false);
        getActivity().setTitle("Post Meat");

        postMeat = v.findViewById(R.id.post_meat);

        postMeat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(requireContext(), admin_postMeat.class));
            }
        });

        return v;
    }
}
