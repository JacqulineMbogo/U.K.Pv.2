package com.example.jacquey.ukullimapoultry.admin;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.jacquey.ukullimapoultry.R;
import com.example.jacquey.ukullimapoultry.myaccount.OrderHistory_Adapter;
import com.example.jacquey.ukullimapoultry.myaccount.OrderHistory_ViewDetails;
import com.example.jacquey.ukullimapoultry.myaccount.orderhistory_model;

import java.util.ArrayList;
import java.util.List;

public class delivery_adapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<deliveryhistory_model> history_model;
    private Context mContext;
    private String TAG = "delivery_adapter";

    private ArrayList<RelativeLayout> addrlayoutsList=  new ArrayList<>();
    private ArrayList<ImageView> imagelist = new ArrayList<>();

    public delivery_adapter(Context context, List<deliveryhistory_model> addressModels) {
        this.history_model = addressModels;
        this.mContext = context;

    }
    private class deliveryhistoryItemView extends RecyclerView.ViewHolder {
        TextView order_id,  order_shipping, order_price, order_date, order_viewdetails, delivery_status;


        public deliveryhistoryItemView(View itemView) {
            super(itemView);
            order_id = (TextView) itemView.findViewById(R.id.order_id);
            order_price = (TextView) itemView.findViewById(R.id.order_price);
            order_shipping = (TextView) itemView.findViewById(R.id.order_shipping);
            order_date = (TextView) itemView.findViewById(R.id.order_date);
            order_viewdetails = (TextView) itemView.findViewById(R.id.order_viewdetails);
            delivery_status = itemView.findViewById(R.id.delivery_status);

        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_delivery, parent,false);
        //Log.e(TAG, "  view created ");
        return new delivery_adapter.deliveryhistoryItemView(view);
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, int position) {

        Log.e(TAG, "bind view "+ position);
        final deliveryhistory_model model =  history_model.get(position);

        ((delivery_adapter.deliveryhistoryItemView) holder).order_id.setText(model.getOrderid());


        ((delivery_adapter.deliveryhistoryItemView) holder).order_date.setText(model.getDate());

        ((deliveryhistoryItemView) holder).delivery_status.setText(model.getDelivery_status());


        // ((OrderAddressItemView) holder).address_layoutmain.setTag( model.getaddress_id());
        ((delivery_adapter.deliveryhistoryItemView) holder).order_viewdetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Log.e(TAG, "  user select the order id " + model.getOrderid() );


                Intent intent = new Intent(mContext, DeliveryHistory_ViewDetails.class);
                intent.putExtra("order_id", model.getOrderid());
                intent.putExtra("user_id", model.getUser());
                intent.putExtra("address", model.getShippingaddress());
                mContext.startActivity(intent);

            }
        });


    }

    @Override
    public int getItemCount() {
        return history_model.size();
    }
}

