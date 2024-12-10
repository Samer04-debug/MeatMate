package com.example.meatmateapplication.Activity.adminFoodPanel;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.meatmateapplication.R;

import java.util.List;
public class AdminPreparedOrderViewAdapter extends RecyclerView.Adapter<AdminPreparedOrderViewAdapter.ViewHolder>  {
    private Context mcontext;
    private List<AdminFinalOrders> adminFinalOrderslist;

    public AdminPreparedOrderViewAdapter(Context context, List<AdminFinalOrders> adminFinalOrderslist) {
        this.adminFinalOrderslist = adminFinalOrderslist;
        this.mcontext = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mcontext).inflate(R.layout.admin_preparedorderview, parent, false);
        return new AdminPreparedOrderViewAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        final AdminFinalOrders adminFinalOrders=adminFinalOrderslist.get(position);
        holder.meatname.setText(adminFinalOrders.getMeatName());
        holder.price.setText("Price: ₱ " + adminFinalOrders.getMeatPrice());
        holder.quantity.setText("× " + adminFinalOrders.getMeatQuantity());
        holder.totalprice.setText("Total: ₱ " + adminFinalOrders.getTotalPrice());
    }

    @Override
    public int getItemCount() {
        return adminFinalOrderslist.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView meatname, price, totalprice, quantity;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            meatname = itemView.findViewById(R.id.Cmeatname);
            price = itemView.findViewById(R.id.Cmeatprice);
            totalprice = itemView.findViewById(R.id.Ctotalprice);
            quantity = itemView.findViewById(R.id.Cmeatqty);
        }
    }
}
