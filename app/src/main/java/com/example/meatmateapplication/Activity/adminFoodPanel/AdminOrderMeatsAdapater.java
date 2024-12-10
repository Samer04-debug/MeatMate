package com.example.meatmateapplication.Activity.adminFoodPanel;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;
import com.example.meatmateapplication.R;

public class AdminOrderMeatsAdapater extends RecyclerView.Adapter<AdminOrderMeatsAdapater.ViewHolder>{


    private Context mcontext;
    private List<AdminPendingOrders> adminPendingOrderslist;

    public AdminOrderMeatsAdapater(Context context, List<AdminPendingOrders> adminPendingOrderslist) {
        this.adminPendingOrderslist = adminPendingOrderslist;
        this.mcontext = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mcontext).inflate(R.layout.admin_order_meats, parent, false);
        return new AdminOrderMeatsAdapater.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        final AdminPendingOrders adminPendingOrders = adminPendingOrderslist.get(position);
        holder.meatname.setText(adminPendingOrders.getMeatName());
        holder.price.setText("Price: ₱ " + adminPendingOrders.getPrice());
        holder.quantity.setText("× " + adminPendingOrders.getMeatQuantity());
        holder.totalprice.setText("Total: ₱ " + adminPendingOrders.getTotalPrice());


    }

    @Override
    public int getItemCount() {
        return adminPendingOrderslist.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView meatname, price, totalprice, quantity;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            meatname = itemView.findViewById(R.id.DN);
            price = itemView.findViewById(R.id.PR);
            totalprice = itemView.findViewById(R.id.TR);
            quantity = itemView.findViewById(R.id.QY);
        }
    }
}
