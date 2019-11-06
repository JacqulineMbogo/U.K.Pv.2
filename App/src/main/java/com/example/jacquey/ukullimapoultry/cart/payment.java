package com.example.jacquey.ukullimapoultry.cart;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.jacquey.ukullimapoultry.LogIn.SignInActivity;
import com.example.jacquey.ukullimapoultry.LogIn.SignUpActivity;
import com.example.jacquey.ukullimapoultry.MainActivity;
import com.example.jacquey.ukullimapoultry.R;
import com.example.jacquey.ukullimapoultry.Utility.AppUtilits;
import com.example.jacquey.ukullimapoultry.Utility.Constant;
import com.example.jacquey.ukullimapoultry.Utility.NetworkUtility;
import com.example.jacquey.ukullimapoultry.Utility.SharedPreferenceActivity;
import com.example.jacquey.ukullimapoultry.WebServices.ServiceWrapper;
import com.example.jacquey.ukullimapoultry.beanResponse.NewUserRegistration;
import com.example.jacquey.ukullimapoultry.beanResponse.OrderHistoryAPI;
import com.example.jacquey.ukullimapoultry.beanResponse.PlaceOrder;
import com.example.jacquey.ukullimapoultry.beanResponse.payAPI;
import com.example.jacquey.ukullimapoultry.myaccount.OrderHistory;
import com.example.jacquey.ukullimapoultry.myaccount.orderhistory_model;
import com.google.gson.Gson;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class payment extends AppCompatActivity {
    private String TAG = "payment";

    EditText payment_amount;
    TextView thanks, request, continuebtn ;
    Button home, order;
     RelativeLayout relative_lay2, relative_lay3;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setHomeButtonEnabled(true);

        relative_lay3 = (RelativeLayout) findViewById(R.id.relative_lay3);
        relative_lay2 = (RelativeLayout) findViewById(R.id.relative_lay2);

        thanks = (TextView) findViewById(R.id.thanks);
        request = (TextView) findViewById(R.id.your_request);
        payment_amount = (EditText) findViewById(R.id.amount);
        continuebtn = (TextView) findViewById(R.id.continuebtn);
        home=findViewById(R.id.homebutton);
        order=findViewById(R.id.shopimage);



        continuebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                    makepayment();
                }


        });
        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(payment.this, OrderHistory.class);
                startActivity(intent);
                finish();

            }
        });
        order.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(payment.this, MainActivity.class);
                startActivity(intent);
                finish();

            }
        });
    }
    public void makepayment(){
        if (!NetworkUtility.isNetworkConnected(payment.this)){
            Toast.makeText(getApplicationContext(),"Network error",Toast.LENGTH_LONG).show();

        }else {

            ServiceWrapper serviceWrapper = new ServiceWrapper(null);
            Call<payAPI> makepaymentAPICall=serviceWrapper.makepaymentcall("1234", SharedPreferenceActivity.getInstance().getString(Constant.USER_DATA),  SharedPreferenceActivity.getInstance().getString(Constant.USER_order_id),
                    SharedPreferenceActivity.getInstance().getString(Constant.TOTAL_TOTAL), payment_amount.getText().toString());
            makepaymentAPICall.enqueue(new Callback<payAPI>() {
                @Override
                public void onResponse(Call<payAPI> call, Response<payAPI> response) {

                    if(Integer.valueOf(String.valueOf(payment_amount.getText()))<(Integer.valueOf(SharedPreferenceActivity.getInstance().getString(Constant.pin)))){
                        Toast.makeText(getApplicationContext(),"Minimum amount expected\n Amount : "+ String.valueOf(SharedPreferenceActivity.getInstance().getString(Constant.pin)),Toast.LENGTH_LONG).show();

                        Log.e("amnt", String.valueOf(payment_amount.getText()));
                        Log.e("amnt", String.valueOf(SharedPreferenceActivity.getInstance().getString(Constant.TOTAL_TOTAL)));

                    }else {

                        Log.e(TAG, "response is " + response.body() + "  ---- " + new Gson().toJson(response.body()));
                        //  Log.e(TAG, "  ss sixe 1 ");
                        if (response.body() != null && response.isSuccessful()) {
                            //    Log.e(TAG, "  ss sixe 2 ");
                            if (response.body().getStatus() == 1) {


                                    relative_lay3.setVisibility(View.VISIBLE);
                                    relative_lay2.setVisibility(View.GONE);

                            } else {

                                AppUtilits.displayMessage(payment.this, response.body().getMsg());

                            }

                        } else {
                            AppUtilits.displayMessage(payment.this, getString(R.string.network_error));

                        }
                    }


                }

                @Override
                public void onFailure(Call<payAPI> call, Throwable t) {

                    Log.e(TAG, "  fail- to make payment"+ t.toString());
                    Toast.makeText(getApplicationContext(),"Failed to make payment",Toast.LENGTH_LONG).show();


                }
            });

        }


    }

}

