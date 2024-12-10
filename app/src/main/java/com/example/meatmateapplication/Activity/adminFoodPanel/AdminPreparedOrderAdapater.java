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
public class AdminPreparedOrderAdapater extends RecyclerView.Adapter<AdminPreparedOrderAdapater.ViewHolder> {
    private Context context;
    private List<AdminFinalOrders1> adminFinalOrders1list;

    public AdminPreparedOrderAdapater(Context context, List<AdminFinalOrders1> adminFinalOrders1list) {
        this.adminFinalOrders1list = adminFinalOrders1list;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.admin_preparedorder, parent, false);
        return new AdminPreparedOrderAdapater.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final AdminFinalOrders1 adminFinalOrders1 = adminFinalOrders1list.get(position);
        holder.Address.setText(adminFinalOrders1.getAddress());
        holder.grandtotalprice.setText("Grand Total: ₱ " + adminFinalOrders1.getGrandTotalPrice());
        final String random = adminFinalOrders1.getRandomUID();
        holder.Vieworder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, AdminPreparedOrderView.class);
                intent.putExtra("RandomUID", random);
                context.startActivity(intent);
                ((AdminPreparedOrder) context).finish();
            }
        });

    }

    @Override
    public int getItemCount() {
        return adminFinalOrders1list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView Address, grandtotalprice;
        Button Vieworder;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            Address = itemView.findViewById(R.id.customer_address);
            grandtotalprice = itemView.findViewById(R.id.customer_totalprice);
            Vieworder = itemView.findViewById(R.id.View);
        }
    }
}

