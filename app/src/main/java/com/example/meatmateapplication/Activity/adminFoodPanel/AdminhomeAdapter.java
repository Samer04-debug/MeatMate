package com.example.meatmateapplication.Activity.adminFoodPanel;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.meatmateapplication.Activity.UpdateMeatModel;
import com.example.meatmateapplication.R;

import java.util.List;

public class AdminhomeAdapter extends RecyclerView.Adapter<AdminhomeAdapter.ViewHolder> {

    private Context mcont;
    private List<UpdateMeatModel>updateMeatModellist;

    public AdminhomeAdapter(Context context,List<UpdateMeatModel>updateMeatModellist)
    {
        this.updateMeatModellist=updateMeatModellist;
        this.mcont=context;
    }



    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(mcont).inflate(R.layout.admin_menu_update_delete,parent,false);
        return new AdminhomeAdapter.ViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        final UpdateMeatModel updateMeatModel=updateMeatModellist.get(position);
        holder.meats.setText(updateMeatModel.getMeats());
        updateMeatModel.getRandomUID();
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(mcont,Update_Delete_Meat.class);
                intent.putExtra("updatedeletemeat",updateMeatModel.getRandomUID());
                mcont.startActivity(intent);

            }
        });
    }


    @Override
    public int getItemCount() {
        return updateMeatModellist.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView meats;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            meats=itemView.findViewById(R.id.meat_name);

        }
    }
}
