package com.example.meatmateapplication.Activity.deliveryFoodPanel;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.example.meatmateapplication.R;

import java.util.List;

public class DeliveryPendingOrderViewAdapter extends RecyclerView.Adapter<DeliveryPendingOrderViewAdapter.ViewHolder> {


    private Context mcontext;
    private List<DeliveryShipOrders> deliveryShipOrderslist;

    public DeliveryPendingOrderViewAdapter(Context context, List<DeliveryShipOrders> deliveryShipOrderslist) {
        this.deliveryShipOrderslist = deliveryShipOrderslist;
        this.mcontext = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mcontext).inflate(R.layout.delivery_pendingorder, parent, false);
        return new DeliveryPendingOrderViewAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        final DeliveryShipOrders deliveryShipOrders = deliveryShipOrderslist.get(position);
        holder.meatname.setText(deliveryShipOrders.getMeatName());
        holder.price.setText("Price: ₱ " + deliveryShipOrders.getMeatPrice());
        holder.quantity.setText("× " + deliveryShipOrders.getMeatQuantity());
        holder.totalprice.setText("Total: ₱ " + deliveryShipOrders.getTotalPrice());
    }

    @Override
    public int getItemCount() {
        return deliveryShipOrderslist.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView meatname, price, totalprice, quantity;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            meatname = itemView.findViewById(R.id.Meat1);
            price = itemView.findViewById(R.id.Price1);
            totalprice = itemView.findViewById(R.id.Total1);
            quantity = itemView.findViewById(R.id.Quantity1);
        }
    }
}