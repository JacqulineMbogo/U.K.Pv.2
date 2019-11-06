package com.example.jacquey.ukullimapoultry.myaccount;


import android.app.AlertDialog;
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
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.jacquey.ukullimapoultry.MainActivity;
import com.example.jacquey.ukullimapoultry.R;
import com.example.jacquey.ukullimapoultry.Utility.AppUtilits;
import com.example.jacquey.ukullimapoultry.Utility.Constant;
import com.example.jacquey.ukullimapoultry.Utility.DataValidation;
import com.example.jacquey.ukullimapoultry.Utility.NetworkUtility;
import com.example.jacquey.ukullimapoultry.Utility.SharedPreferenceActivity;
import com.example.jacquey.ukullimapoultry.WebServices.ServiceWrapper;
import com.example.jacquey.ukullimapoultry.beanResponse.GetOrderProductDetails;
import com.example.jacquey.ukullimapoultry.beanResponse.clearbalanceAPI;
import com.example.jacquey.ukullimapoultry.beanResponse.payAPI;
import com.example.jacquey.ukullimapoultry.beanResponse.receiveAPI;
import com.example.jacquey.ukullimapoultry.cart.Cartitem_Model;
import com.example.jacquey.ukullimapoultry.cart.OrderSummary_Adapter;
import com.example.jacquey.ukullimapoultry.cart.PlaceOrderActivity;
import com.example.jacquey.ukullimapoultry.cart.payment2;
import com.google.gson.Gson;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class OrderHistory_ViewDetails extends AppCompatActivity {
    private String TAG ="orderViewdetails", orderId ="",delivermode="";
    private RecyclerView item_recyclerview;
    private ArrayList<Cartitem_Model> cartitemModels = new ArrayList<>();
    private OrderSummary_Adapter orderSummeryAdapter;
    private float totalamount =0;
    private String shippingadress = "";
    private TextView continuebtn1,codecontinuebtn,cleartext,subtotal_value,shipping_value, order_total_value, order_ship_address, order_billing_address,order_total,order_over,amount,pay,receive;
    private RelativeLayout layout2;
    private LinearLayout textid,txtid, textid2,textid3;
    private RadioButton radio_eazybanking, radio_cash_on;
    private EditText code;
    private RadioGroup radioGroup;



    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_orderhistory_viewdetails);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(true);

        Intent intent = getIntent();
        orderId = intent.getExtras().getString("order_id");
        shippingadress = intent.getExtras().getString("address");

        subtotal_value = (TextView) findViewById(R.id.subtotal_value);
        cleartext= (TextView) findViewById(R.id.cleartext);
        shipping_value = (TextView) findViewById(R.id.shipping_value);
        order_total_value = (TextView) findViewById(R.id.order_total_value);
        order_ship_address = (TextView) findViewById(R.id.order_ship_address);
        order_billing_address = (TextView) findViewById(R.id.order_billing_address);
        order_total= findViewById(R.id.order_total);
        order_over= findViewById(R.id.order_over);
        amount=findViewById(R.id.amount);
        pay=findViewById(R.id.continuebtn);
        receive=findViewById(R.id.receivebtn);
        amount.setText(SharedPreferenceActivity.getInstance().getString(Constant.USER_name));
        item_recyclerview = (RecyclerView) findViewById(R.id.item_recyclerview);
        layout2=findViewById(R.id.layout_2);
        textid= findViewById(R.id.textid1);
        txtid= findViewById(R.id.textid);
        textid2= findViewById(R.id.textid2);
        textid3= findViewById(R.id.textid3);
        codecontinuebtn =findViewById(R.id.codecontinuebtn);
        continuebtn1 =findViewById(R.id.continuebtn1);
        code = findViewById(R.id.code);
        radio_cash_on = (RadioButton) findViewById(R.id.radio_cash_on);
        radio_eazybanking = (RadioButton) findViewById(R.id.radio_eazybank);
        radioGroup = findViewById(R.id.radiogroup);


        LinearLayoutManager mLayoutManger3 = new LinearLayoutManager( this, LinearLayoutManager.VERTICAL, false);
        item_recyclerview.setLayoutManager(mLayoutManger3);
        item_recyclerview.setItemAnimator(new DefaultItemAnimator());

        orderSummeryAdapter = new OrderSummary_Adapter(this, cartitemModels);
        item_recyclerview.setAdapter(orderSummeryAdapter);

        getOrderDetails();


        continuebtn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(radio_cash_on.isChecked() || radio_eazybanking.isChecked()) {
                    if (radio_cash_on.isChecked()) {

                        delivermode = "M_Pesa";
                        textid3.setVisibility(View.GONE);
                        textid2.setVisibility(View.VISIBLE);



                    } else if (radio_eazybanking.isChecked()) {

                        delivermode = "Eazy banking";
                        textid3.setVisibility(View.GONE);
                        textid2.setVisibility(View.VISIBLE);



                    }
                }else{
                    Toast.makeText(getApplicationContext(),"Please select a payment method.",Toast.LENGTH_LONG).show();

                }



            }
        });

        receive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                receivegoods();
            }
        });

        pay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               // clearpayment();
                textid.setVisibility(View.GONE);
                textid3.setVisibility(View.VISIBLE);
            }
        });
        codecontinuebtn .setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if ( DataValidation.isNotValidcode(code.getText().toString())){
                    Toast.makeText(getApplicationContext(),"Invalid code length. Should be 10 characters.",Toast.LENGTH_LONG).show();

                }else {
                    clearpayment();
                }
            }
        });

        cleartext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                txtid.setVisibility(View.GONE);
                textid.setVisibility(View.VISIBLE);
            }
        });

    }

    public void getOrderDetails(){



        if (!NetworkUtility.isNetworkConnected(OrderHistory_ViewDetails.this)){
            AppUtilits.displayMessage(OrderHistory_ViewDetails.this,  getString(R.string.network_not_connected));

        }else {
            //  Log.e(TAG, "  user value "+ SharePreferenceUtils.getInstance().getString(Constant.USER_DATA));
            ServiceWrapper service = new ServiceWrapper(null);
            Call<GetOrderProductDetails> call = service.getorderproductcall("1234", SharedPreferenceActivity.getInstance().getString(Constant.USER_DATA),
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
                                if(Integer.parseInt(String.valueOf(response.body().getGrandtotal()))>0){
                                    order_total.setVisibility(View.VISIBLE);

                                    layout2.setVisibility(View.VISIBLE);
                                    receive.setVisibility(View.GONE);
                                    order_total_value.setText(response.body().getGrandtotal());
                                    amount.setText(response.body().getGrandtotal());

                                    SharedPreferenceActivity.getInstance().saveString(Constant.VIEW_TOTAL, response.body().getShippingfee());
                                    SharedPreferenceActivity.getInstance().saveString(Constant.VIEW_BALANCE, response.body().getGrandtotal());
                                       Log.e("SAMPLE", String.valueOf(Constant.VIEW_TOTAL));

                                    Log.e("SAMPLE1", String.valueOf(amount));





                                }


                                order_total_value.setText(response.body().getGrandtotal());

                                order_ship_address.setText(shippingadress);
                                order_billing_address.setText(shippingadress);

                                cartitemModels.clear();
                                for (int i=0; i<response.body().getInformation().size(); i++){


                                    cartitemModels.add( new Cartitem_Model(response.body().getInformation().get(i).getProdId(),
                                            response.body().getInformation().get(i).getProdName(), "", "",
                                            response.body().getInformation().get(i).getProdTotal(), response.body().getInformation().get(i).getQty()));

                                }

                                orderSummeryAdapter.notifyDataSetChanged();
                            }


                        } else {
                            AppUtilits.displayMessage(OrderHistory_ViewDetails.this, response.body().getMsg());

                        }

                    } else {
                        AppUtilits.displayMessage(OrderHistory_ViewDetails.this, getString(R.string.network_error));

                    }
                }

                @Override
                public void onFailure(Call<GetOrderProductDetails> call, Throwable t) {
                    //   Log.e(TAG, "  fail- add to cart item "+ t.toString());
                    AppUtilits.displayMessage(OrderHistory_ViewDetails.this, getString(R.string.fail_toorderhistory));

                }
            });








        }

    }
    public void clearpayment(){

        final AlertDialog progressbar =AppUtilits.createProgressBar(this);
        if (!NetworkUtility.isNetworkConnected(OrderHistory_ViewDetails.this)){
            Toast.makeText(getApplicationContext(),"Network error",Toast.LENGTH_LONG).show();

        }else {

            ServiceWrapper serviceWrapper = new ServiceWrapper(null);
            Call<clearbalanceAPI> clearbalanceAPICall=serviceWrapper.clearbalancecall("1234", orderId,
                    SharedPreferenceActivity.getInstance().getString(Constant.VIEW_TOTAL),String.valueOf(code.getText().toString()),delivermode,SharedPreferenceActivity.getInstance().getString(Constant.VIEW_BALANCE));
            clearbalanceAPICall.enqueue(new Callback<clearbalanceAPI>() {
                @Override
                public void onResponse(Call<clearbalanceAPI> call, Response<clearbalanceAPI> response) {

                    Log.e("Orderid",  orderId);
                    Log.e("total",  SharedPreferenceActivity.getInstance().getString(Constant.VIEW_TOTAL));
                    Log.e(TAG, "response is " + response.body() + "  ---- " + new Gson().toJson(response.body()));
                    //  Log.e(TAG, "  ss sixe 1 ");
                    if (response.body() != null && response.isSuccessful()) {
                        //    Log.e(TAG, "  ss sixe 2 ");
                        if (response.body().getStatus() == 1) {


                            getOrderDetails();
                            layout2.setVisibility(View.VISIBLE);
                            textid2.setVisibility(View.GONE);
                            AppUtilits.destroyDialog(progressbar);
                            AppUtilits.displayMessage(OrderHistory_ViewDetails.this, response.body().getMsg() );

                        } else {
                            AppUtilits.destroyDialog(progressbar);

                            AppUtilits.displayMessage(OrderHistory_ViewDetails.this, response.body().getMsg());

                        }

                    } else {
                        AppUtilits.displayMessage(OrderHistory_ViewDetails.this, getString(R.string.network_error));

                    }



                }

                @Override
                public void onFailure(Call<clearbalanceAPI> call, Throwable throwable) {


                    Toast.makeText(getApplicationContext(),"Failed to make payment",Toast.LENGTH_LONG).show();

                }


            });

        }


    }

    public void receivegoods(){
        final AlertDialog progressbar =AppUtilits.createProgressBar(this);
        if (!NetworkUtility.isNetworkConnected(OrderHistory_ViewDetails.this)){
            Toast.makeText(getApplicationContext(),"Network error",Toast.LENGTH_LONG).show();

        }else {

            ServiceWrapper serviceWrapper = new ServiceWrapper(null);
            Call<receiveAPI> receiveAPICall=serviceWrapper.receivecall("1234", orderId,
                    "Delivered");
            receiveAPICall.enqueue(new Callback<receiveAPI>() {
                @Override
                public void onResponse(Call<receiveAPI> call, Response<receiveAPI> response) {

                    Log.e("Orderid",  orderId);
                    Log.e("total",  SharedPreferenceActivity.getInstance().getString(Constant.VIEW_TOTAL));
                    Log.e(TAG, "response is " + response.body() + "  ---- " + new Gson().toJson(response.body()));
                    //  Log.e(TAG, "  ss sixe 1 ");
                    if (response.body() != null && response.isSuccessful()) {
                        //    Log.e(TAG, "  ss sixe 2 ");
                        if (response.body().getStatus() == 1) {


                            AppUtilits.destroyDialog(progressbar);

                            AppUtilits.displayMessage(OrderHistory_ViewDetails.this, response.body().getMsg() );

                        } else {
                            AppUtilits.destroyDialog(progressbar);

                            AppUtilits.displayMessage(OrderHistory_ViewDetails.this, response.body().getMsg());

                        }

                    } else {
                        AppUtilits.displayMessage(OrderHistory_ViewDetails.this, getString(R.string.network_error));

                    }



                }

                @Override
                public void onFailure(Call<receiveAPI> call, Throwable throwable) {


                    Toast.makeText(getApplicationContext(),"Failed to make payment",Toast.LENGTH_LONG).show();

                }


            });

        }


    }

    @Override
    public void onBackPressed() {


        Intent intent1 = new Intent(OrderHistory_ViewDetails.this, OrderHistory.class);

        startActivity(intent1);



    }

}

