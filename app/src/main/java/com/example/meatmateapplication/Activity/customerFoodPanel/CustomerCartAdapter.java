package com.example.meatmateapplication.Activity.customerFoodPanel;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.NumberPicker;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.meatmateapplication.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.List;

public class CustomerCartAdapter extends RecyclerView.Adapter<CustomerCartAdapter.ViewHolder> {

    private Context mcontext;
    private List<Cart> cartModellist;
    static int total = 0;

    public CustomerCartAdapter(Context context, List<Cart> cartModellist) {
        this.cartModellist = cartModellist;
        this.mcontext = context;
        total = 0;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mcontext).inflate(R.layout.cart_placeorder, parent, false);
        return new CustomerCartAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {
        final Cart cart = cartModellist.get(position);
        holder.meatname.setText(cart.getMeatName());
        holder.PriceRs.setText("Price: ₱ " + cart.getPrice());
        holder.Qty.setText("× " + cart.getMeatQuantity());
        holder.Totalrs.setText("Total: ₱ " + cart.getTotalprice());
        total += Integer.parseInt(cart.getTotalprice());
        holder.numberPicker.setValue(Integer.parseInt(cart.getMeatQuantity()));
        final int meatprice = Integer.parseInt(cart.getPrice());

        holder.numberPicker.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                int num = newVal;
                int totalprice = num * meatprice;
                if (num != 0) {
                    HashMap<String, String> hashMap = new HashMap<>();
                    hashMap.put("MeatID", cart.getMeatID());
                    hashMap.put("MeatName", cart.getMeatName());
                    hashMap.put("MeatQuantity", String.valueOf(num));
                    hashMap.put("Price", String.valueOf(meatprice));
                    hashMap.put("Totalprice", String.valueOf(totalprice));
                    hashMap.put("AdminId", cart.getAdminId());

                    FirebaseDatabase.getInstance().getReference("Cart").child("CartItems").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child(cart.getMeatID()).setValue(hashMap);
                } else {
                    FirebaseDatabase.getInstance().getReference("Cart").child("CartItems").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child(cart.getMeatID()).removeValue();
                }
            }
        });
        CustomerCartFragment.grandt.setText("Grand Total: ₱ " + total);
        FirebaseDatabase.getInstance().getReference("Cart").child("GrandTotal").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("GrandTotal").setValue(String.valueOf(total));

    }

    @Override
    public int getItemCount() {
        return cartModellist.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView meatname, PriceRs, Qty, Totalrs;
        NumberPicker numberPicker;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            meatname = itemView.findViewById(R.id.Meatname);
            PriceRs = itemView.findViewById(R.id.pricephp);
            Qty = itemView.findViewById(R.id.qty);
            Totalrs = itemView.findViewById(R.id.totalphp);
            numberPicker = itemView.findViewById(R.id.numberPicker);
        }
    }
}