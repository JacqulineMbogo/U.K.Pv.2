package com.example.jacquey.ukullimapoultry.myaccount;


import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.jacquey.ukullimapoultry.R;
import com.example.jacquey.ukullimapoultry.cart.Cartitem_Model;

import java.util.List;


public class receipt_adapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<Cartitem_Model> cartitem_models;
    private Context mContext;
    private String TAG = "order_adapter";

    public receipt_adapter(Context context, List<Cartitem_Model> cartitemModels) {
        this.cartitem_models = cartitemModels;
        this.mContext = context;

    }
    private class receipt_adapterItemView extends RecyclerView.ViewHolder {
        TextView prod_name,  prod_qty, prod_price;


        public  receipt_adapterItemView(View itemView) {
            super(itemView);
            prod_name = (TextView) itemView.findViewById(R.id.prod_name);
            prod_price = (TextView) itemView.findViewById(R.id.prod_price);
            prod_qty = (TextView) itemView.findViewById(R.id.prod_qty);


        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_ordersum_item, parent,false);
        Log.e(TAG, "  view created ");
        return new receipt_adapterItemView(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        Log.e(TAG, "bind view "+ position);
        final Cartitem_Model model =  cartitem_models.get(position);

        ((receipt_adapterItemView) holder).prod_name.setText(model.getProd_name());
        ((receipt_adapterItemView) holder).prod_price.setText(model.getPrice());
        ((receipt_adapterItemView) holder).prod_qty.setText(model.getQty());


        Log.e(TAG, "qqty "+  cartitem_models.get(position));




    }

    @Override
    public int getItemCount() {
        return cartitem_models.size();
    }
}