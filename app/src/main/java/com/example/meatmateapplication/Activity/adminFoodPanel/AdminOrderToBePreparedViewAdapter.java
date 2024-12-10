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

public class AdminOrderToBePreparedViewAdapter extends RecyclerView.Adapter<AdminOrderToBePreparedViewAdapter.ViewHolder> {
    private Context mcontext;
    private List<AdminWaitingOrders> adminWaitingOrderslist;

    public AdminOrderToBePreparedViewAdapter(Context context, List<AdminWaitingOrders> adminWaitingOrderslist) {
        this.adminWaitingOrderslist = adminWaitingOrderslist;
        this.mcontext = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mcontext).inflate(R.layout.admin_ordertobeprepared_view, parent, false);
        return new AdminOrderToBePreparedViewAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        final AdminWaitingOrders adminWaitingOrders = adminWaitingOrderslist.get(position);
        holder.meatname.setText(adminWaitingOrders.getMeatName());
        holder.price.setText("Price: ₱ " + adminWaitingOrders.getMeatPrice());
        holder.quantity.setText("× " + adminWaitingOrders.getMeatQuantity());
        holder.totalprice.setText("Total: ₱ " + adminWaitingOrders.getTotalPrice());
    }

    @Override
    public int getItemCount() {
        return adminWaitingOrderslist.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView meatname, price, totalprice, quantity;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            meatname = itemView.findViewById(R.id.Dname);
            price = itemView.findViewById(R.id.Dprice);
            totalprice = itemView.findViewById(R.id.Tprice);
            quantity = itemView.findViewById(R.id.Dqty);
        }
    }
}

