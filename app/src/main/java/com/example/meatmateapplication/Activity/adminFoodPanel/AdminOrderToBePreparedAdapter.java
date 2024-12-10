package com.example.meatmateapplication.Activity.adminFoodPanel;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.meatmateapplication.R;

import java.util.List;

public class AdminOrderToBePreparedAdapter extends RecyclerView.Adapter<AdminOrderToBePreparedAdapter.ViewHolder> {

    private Context context;
    private List<AdminWaitingOrders1> adminWaitingOrders1list;

    public AdminOrderToBePreparedAdapter(Context context, List<AdminWaitingOrders1> adminWaitingOrders1list) {
        this.adminWaitingOrders1list = adminWaitingOrders1list;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.admin_ordertobeprepared, parent, false);
        return new AdminOrderToBePreparedAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        AdminWaitingOrders1 adminWaitingOrders1 = adminWaitingOrders1list.get(position);
        holder.Address.setText(adminWaitingOrders1.getAddress());
        holder.grandtotalprice.setText("Grand Total: ₱ " + adminWaitingOrders1.getGrandTotalPrice());
        final String random = adminWaitingOrders1.getRandomUID();
        holder.Vieworder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, AdminOrderToBePreparedView.class);
                intent.putExtra("RandomUID", random);
                context.startActivity(intent);
                ((AdminOrderToBePrepared) context).finish();
            }
        });
    }

    @Override
    public int getItemCount() {
        return adminWaitingOrders1list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView Address, grandtotalprice;
        Button Vieworder;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            Address = itemView.findViewById(R.id.cust_address);
            grandtotalprice = itemView.findViewById(R.id.Grandtotalprice);
            Vieworder = itemView.findViewById(R.id.View_order);
        }
    }
}