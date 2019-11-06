package com.example.jacquey.ukullimapoultry.admin;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.jacquey.ukullimapoultry.R;
import com.example.jacquey.ukullimapoultry.Utility.AppUtilits;
import com.example.jacquey.ukullimapoultry.Utility.Constant;
import com.example.jacquey.ukullimapoultry.Utility.NetworkUtility;
import com.example.jacquey.ukullimapoultry.Utility.SharedPreferenceActivity;
import com.example.jacquey.ukullimapoultry.WebServices.ServiceWrapper;
import com.example.jacquey.ukullimapoultry.beanResponse.GetOrderProductDetails;
import com.example.jacquey.ukullimapoultry.cart.Cartitem_Model;
import com.example.jacquey.ukullimapoultry.cart.OrderSummary_Adapter;
import com.example.jacquey.ukullimapoultry.myaccount.OrderHistory_ViewDetails;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DeliveryHistory_ViewDetails extends AppCompatActivity {
    private String TAG ="deliveryViewdetails", orderId ="";
    private RecyclerView item_recyclerview;
    private ArrayList<Cartitem_Model> cartitemModels = new ArrayList<>();
    private OrderSummary_Adapter orderSummeryAdapter;
    private float totalamount =0;
    private String shippingadress = "", user="";
    private TextView subtotal_value,shipping_value, order_total_value, order_ship_address, order_billing_address,order_total,order_over,amount,pay,receive;
    private RelativeLayout layout2;



    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_deliverydetails);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(true);

        Intent intent = getIntent();
        orderId = intent.getExtras().getString("order_id");
        shippingadress = intent.getExtras().getString("address");
        user = intent.getExtras().getString("user_id");

        subtotal_value = (TextView) findViewById(R.id.subtotal_value);
        shipping_value = (TextView) findViewById(R.id.shipping_value);
        order_total_value = (TextView) findViewById(R.id.order_total_value);
        order_ship_address = (TextView) findViewById(R.id.order_ship_address);
        order_billing_address = (TextView) findViewById(R.id.order_billing_address);
        order_total= findViewById(R.id.order_total);
        order_over= findViewById(R.id.order_over);
       // amount=findViewById(R.id.amount);
        pay=findViewById(R.id.continuebtn);
        receive=findViewById(R.id.receivebtn);
       // amount.setText(SharedPreferenceActivity.getInstance().getString(Constant.USER_name));
        item_recyclerview = (RecyclerView) findViewById(R.id.item_recyclerview);
        layout2=findViewById(R.id.layout_2);

        LinearLayoutManager mLayoutManger3 = new LinearLayoutManager( this, LinearLayoutManager.VERTICAL, false);
        item_recyclerview.setLayoutManager(mLayoutManger3);
        item_recyclerview.setItemAnimator(new DefaultItemAnimator());

        orderSummeryAdapter = new OrderSummary_Adapter(this, cartitemModels);
        item_recyclerview.setAdapter(orderSummeryAdapter);

        getOrderDetails();

    }

    public void getOrderDetails(){



        if (!NetworkUtility.isNetworkConnected(DeliveryHistory_ViewDetails.this)){
            AppUtilits.displayMessage(DeliveryHistory_ViewDetails.this,  getString(R.string.network_not_connected));

        }else {
            //  Log.e(TAG, "  user value "+ SharePreferenceUtils.getInstance().getString(Constant.USER_DATA));
            ServiceWrapper service = new ServiceWrapper(null);
            Call<GetOrderProductDetails> call = service.getorderproductcall("1234", user,
                    orderId  );
            call.enqueue(new Callback<GetOrderProductDetails>() {
                @Override
                public void onResponse(Call<GetOrderProductDetails> call, Response<GetOrderProductDetails> response) {
                    //   Log.e(TAG, "response is " + response.body() + "  ---- " + new Gson().toJson(response.body()));
                    //  Log.e(TAG, "  ss sixe 1 ");
                    if (response.body() != null && response.isSuccessful()) {
                        //    Log.e(TAG, "  ss sixe 2 ");
                        if (response.body().getStatus() == 1) {

                            if (response.body().getInformation().size()>0){
                                subtotal_value.setText(response.body().getSubtotal());
                                shipping_value.setText(response.body().getShippingfee());
                                order_total_value.setText(response.body().getGrandtotal());

                                order_ship_address.setText(shippingadress);
                                order_billing_address.setText(shippingadress);

                                cartitemModels.clear();
                                for (int i=0; i<response.body().getInformation().size(); i++){


                                    cartitemModels.add( new Cartitem_Model(response.body().getInformation().get(i).getProdId(),
                                            response.body().getInformation().get(i).getProdName(), "", "",
                                            response.body().getInformation().get(i).getPrice(), response.body().getInformation().get(i).getQty()));

                                }

                                orderSummeryAdapter.notifyDataSetChanged();
                            }

                        } else {
                            AppUtilits.displayMessage(DeliveryHistory_ViewDetails.this, response.body().getMsg());

                        }

                    } else {
                        AppUtilits.displayMessage(DeliveryHistory_ViewDetails.this, getString(R.string.network_error));

                    }
                }

                @Override
                public void onFailure(Call<GetOrderProductDetails> call, Throwable t) {
                    //   Log.e(TAG, "  fail- add to cart item "+ t.toString());
                    AppUtilits.displayMessage(DeliveryHistory_ViewDetails.this, getString(R.string.fail_toorderhistory));

                }
            });








        }

    }
}
