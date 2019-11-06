package com.example.jacquey.ukullimapoultry.cart;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.jacquey.ukullimapoultry.MainActivity;
import com.example.jacquey.ukullimapoultry.R;
import com.example.jacquey.ukullimapoultry.Utility.AppUtilits;
import com.example.jacquey.ukullimapoultry.Utility.Constant;
import com.example.jacquey.ukullimapoultry.Utility.NetworkUtility;
import com.example.jacquey.ukullimapoultry.Utility.SharedPreferenceActivity;
import com.example.jacquey.ukullimapoultry.WebServices.ServiceWrapper;
import com.example.jacquey.ukullimapoultry.beanResponse.PlaceOrder;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static java.lang.Integer.valueOf;

public class PlaceOrderActivity extends AppCompatActivity {
    private String TAG =" PlaceOrderActivity";
    private String pin="0", addressid ="0", delivermode ="instant_pay";
    private TextView place_order,  pay, totalpricetxt, shipping,TOTAL;
    private RadioButton radio_eazybanking, radio_cash_on;
    private RelativeLayout relative_lay1, relative_lay2, relative_lay3;
    public String plus="0";
    private RadioGroup radioGroup;

    private Boolean gotoHomeflag = false;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_placeorder);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setHomeButtonEnabled(true);

        final Intent intent = getIntent();

        addressid =  intent.getExtras().getString("addressid");
        pin= intent.getExtras().getString("pin");



        radio_cash_on = (RadioButton) findViewById(R.id.radio_cash_on);
        radio_eazybanking = (RadioButton) findViewById(R.id.radio_eazybank);
        radioGroup = findViewById(R.id.radiogroup);

        totalpricetxt = (TextView) findViewById(R.id.totalpricetxt);
        shipping = (TextView) findViewById(R.id.shipping);
        TOTAL =(TextView) findViewById(R.id.TOTAL);
        pay =  findViewById(R.id.pay);

        relative_lay1 = (RelativeLayout) findViewById(R.id.relative_lay1);
        relative_lay2 = (RelativeLayout) findViewById(R.id.relative_lay2);
        relative_lay3 = (RelativeLayout) findViewById(R.id.relative_lay3);


        place_order = (TextView) findViewById(R.id.place_order);
        place_order.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(radio_cash_on.isChecked() || radio_eazybanking.isChecked()) {
                    if (radio_cash_on.isChecked()) {

                        delivermode = "M_Pesa";

                        CallPlaceOrderAPI(addressid, pin);

                        pay.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Intent intent2 = new Intent(PlaceOrderActivity.this, payment2.class);
                                intent2.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(intent2);

                                finish();

                            }
                        });
                    } else if (radio_eazybanking.isChecked()) {

                        delivermode = "Eazy banking";

                        CallPlaceOrderAPI(addressid, pin);

                        pay.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Intent intent2 = new Intent(PlaceOrderActivity.this, payment2.class);
                                intent2.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(intent2);

                                finish();

                            }
                        });
                    }
                }
                else{
                    Toast.makeText(getApplicationContext(),"Please select a payment method.",Toast.LENGTH_LONG).show();

                }



            }
        });






    }

    public void  CallcashondeliveryAPI( final String addressid, final String pin){
        if (!NetworkUtility.isNetworkConnected(PlaceOrderActivity.this)){
            AppUtilits.displayMessage(PlaceOrderActivity.this,  getString(R.string.network_not_connected));

        }else {

            ServiceWrapper serviceWrapper = new ServiceWrapper(null);

            Call<PlaceOrder> call = serviceWrapper.placceOrdercall("1234", SharedPreferenceActivity.getInstance().getString(Constant.USER_DATA),
                    addressid, SharedPreferenceActivity.getInstance().getString(Constant.USER_Totalprice), SharedPreferenceActivity.getInstance().getString(Constant.QUOTE_ID), delivermode );

            call.enqueue(new Callback<PlaceOrder>() {
                @Override
                public void onResponse(Call<PlaceOrder> call, Response<PlaceOrder> response) {
                    Log.e(TAG, "response is "+ response.body() + "  ---- "+ new Gson().toJson(response.body()));
                    Log.d("amount", String.valueOf(Constant.USER_Totalprice));
                    Log.d("address", addressid);

                    if (response.body() != null && response.isSuccessful()) {
                        //    Log.e(TAG, "  ss sixe 2 ");
                        if (response.body().getStatus() == 1) {

                            gotoHomeflag = true;
                            relative_lay1.setVisibility(View.GONE);
                            relative_lay3.setVisibility(View.GONE);
                            relative_lay2.setVisibility(View.VISIBLE);

                            SharedPreferenceActivity.getInstance().saveString(Constant.USER_order_id, response.body().getInformation().getOrderId());

                            totalpricetxt.setText("Subtotal : " + "Ksh " +SharedPreferenceActivity.getInstance().getString(Constant.USER_Totalprice));
                            totalpricetxt.setVisibility(View.GONE);
                            shipping.setText("Shipping : " + "Ksh " +String.valueOf(pin));
                            shipping.setVisibility(View.GONE);
                            String totals, shippings;
                            totals  = SharedPreferenceActivity.getInstance().getString(Constant.USER_Totalprice);
                            shippings  =   String.valueOf(pin);
                            if(totals.isEmpty())
                            {
                                totals  =   "0";
                            }

                            if(shippings.isEmpty())
                            {
                                shippings   =   "0";
                            }

                            int total= valueOf(totals);
                            int ship =valueOf(shippings);
                            int plus = total + ship;


                            TOTAL.setText("AMOUNT : " + "Ksh " +String.valueOf(pin));

                            Log.d("pin", pin);
                            Log.d("TT", String.valueOf(plus));
                            SharedPreferenceActivity.getInstance().saveString(Constant.QUOTE_ID, "");

                            SharedPreferenceActivity.getInstance().saveString(Constant.TOTAL_TOTAL,String.valueOf(plus));
                            SharedPreferenceActivity.getInstance().saveString(Constant.pin,String.valueOf(pin));



                        }else {
                            AppUtilits.displayMessage(PlaceOrderActivity.this, response.body().getMsg() );
                        }
                    }else {
                        AppUtilits.displayMessage(PlaceOrderActivity.this, getString(R.string.network_error));
                    }
                }

                @Override
                public void onFailure(Call<PlaceOrder> call, Throwable t) {

                    Log.e(TAG, "  fail- get user address "+ t.toString());
                    AppUtilits.displayMessage(PlaceOrderActivity.this, getString(R.string.fail_togetaddress));

                }
            });


        }


    }
    public void CallPlaceOrderAPI( final String addressid, final String pin){

        final AlertDialog progressbar =AppUtilits.createProgressBar(this);
        if (!NetworkUtility.isNetworkConnected(PlaceOrderActivity.this)){
            AppUtilits.displayMessage(PlaceOrderActivity.this,  getString(R.string.network_not_connected));

            AppUtilits.destroyDialog(progressbar);
        }else {

            ServiceWrapper serviceWrapper = new ServiceWrapper(null);

            Call<PlaceOrder> call = serviceWrapper.placceOrdercall("1234", SharedPreferenceActivity.getInstance().getString(Constant.USER_DATA),
                    addressid, SharedPreferenceActivity.getInstance().getString(Constant.USER_Totalprice), SharedPreferenceActivity.getInstance().getString(Constant.QUOTE_ID), delivermode );

            call.enqueue(new Callback<PlaceOrder>() {
                @Override
                public void onResponse(Call<PlaceOrder> call, Response<PlaceOrder> response) {
                    Log.e(TAG, "response is "+ response.body() + "  ---- "+ new Gson().toJson(response.body()));
                    Log.d("amount", String.valueOf(Constant.USER_Totalprice));
                    Log.d("address", addressid);

                    if (response.body() != null && response.isSuccessful()) {
                        //    Log.e(TAG, "  ss sixe 2 ");
                        if (response.body().getStatus() == 1) {

                            AppUtilits.destroyDialog(progressbar);

                            gotoHomeflag = true;
                            relative_lay1.setVisibility(View.GONE);
                            relative_lay3.setVisibility(View.GONE);
                            relative_lay2.setVisibility(View.VISIBLE);

                            SharedPreferenceActivity.getInstance().saveString(Constant.USER_order_id, response.body().getInformation().getOrderId());

                            totalpricetxt.setText("Subtotal : " + "Ksh " +SharedPreferenceActivity.getInstance().getString(Constant.USER_Totalprice));

                            shipping.setText("Shipping : " + "Ksh " +String.valueOf(pin));

                            String totals, shippings;
                            totals  = SharedPreferenceActivity.getInstance().getString(Constant.USER_Totalprice);
                            shippings  =   String.valueOf(pin);
                            if(totals.isEmpty())
                            {
                                totals  =   "0";
                            }

                            if(shippings.isEmpty())
                            {
                                shippings   =   "0";
                            }

                            int total= valueOf(totals);
                            int ship =valueOf(shippings);
                            int plus = total + ship;


                            TOTAL.setText("TOTAL : " + "Ksh " +plus);

                            Log.d("pin", pin);
                            SharedPreferenceActivity.getInstance().saveString(Constant.QUOTE_ID, "");

                            SharedPreferenceActivity.getInstance().saveString(Constant.TOTAL_TOTAL,String.valueOf(plus));



                        }else {
                            AppUtilits.displayMessage(PlaceOrderActivity.this, response.body().getMsg() );
                            AppUtilits.destroyDialog(progressbar);


                        }
                    }else {
                        AppUtilits.displayMessage(PlaceOrderActivity.this, getString(R.string.network_error));
                        AppUtilits.destroyDialog(progressbar);

                    }
                }

                @Override
                public void onFailure(Call<PlaceOrder> call, Throwable t) {

                    Log.e(TAG, "  fail- get user address "+ t.toString());
                    AppUtilits.displayMessage(PlaceOrderActivity.this, getString(R.string.fail_togetaddress));

                    AppUtilits.destroyDialog(progressbar);
                }
            });


        }


    }
    @Override
    public void onBackPressed() {



        android.support.v7.app.AlertDialog.Builder alertDialog = new android.support.v7.app.AlertDialog.Builder(PlaceOrderActivity.this);
        alertDialog.setTitle("Cancel Request Confirmation!");
        alertDialog.setIcon(R.drawable.chick);
        alertDialog.setMessage("By going back, your order will be cancelled,\n\n" +
                "You cannot make any changes upon submitting\n\n" +
                "Would you like to proceed?\n\n");
        alertDialog.setNeutralButton("No, Stop", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {



            }
        }).setPositiveButton("Yes, Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent1 = new Intent(PlaceOrderActivity.this, MainActivity.class);

                startActivity(intent1);
                Toast.makeText(getApplicationContext(),"Order Cancelled",Toast.LENGTH_LONG).show();

            }

        }).show();





    }
}
