package com.example.meatmateapplication.Activity.customerFoodPanel;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.meatmateapplication.R;

import java.util.List;

public class PayableOrdersAdapter extends RecyclerView.Adapter<PayableOrdersAdapter.ViewHolder> {

    private Context context;
    private List<CustomerPaymentOrders> customerPaymentOrderslist;

    public PayableOrdersAdapter(Context context, List<CustomerPaymentOrders> customerPendingOrderslist) {
        this.customerPaymentOrderslist = customerPendingOrderslist;
        this.context = context;
    }

    @NonNull
    @Override
    public PayableOrdersAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.customer_payableorders, parent, false);
        return new PayableOrdersAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PayableOrdersAdapter.ViewHolder holder, int position) {

        final CustomerPaymentOrders customerPaymentOrders = customerPaymentOrderslist.get(position);
        holder.Meatname.setText(customerPaymentOrders.getMeatName());
        holder.Price.setText("Price: ₱ " + customerPaymentOrders.getMeatPrice());
        holder.Quantity.setText("× " + customerPaymentOrders.getMeatQuantity());
        holder.Totalprice.setText("Total: ₱ " + customerPaymentOrders.getTotalPrice());
    }

    @Override
    public int getItemCount() {
        return customerPaymentOrderslist.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView Meatname, Price, Quantity, Totalprice;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            Meatname = itemView.findViewById(R.id.meat);
            Price = itemView.findViewById(R.id.pri);
            Quantity = itemView.findViewById(R.id.qt);
            Totalprice = itemView.findViewById(R.id.Tot);
        }
    }
}