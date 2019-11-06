package com.example.jacquey.ukullimapoultry.cart;

import android.app.AlertDialog;
import android.content.DialogInterface;
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
import com.example.jacquey.ukullimapoultry.Utility.DataValidation;
import com.example.jacquey.ukullimapoultry.Utility.NetworkUtility;
import com.example.jacquey.ukullimapoultry.Utility.SharedPreferenceActivity;
import com.example.jacquey.ukullimapoultry.WebServices.ServiceWrapper;
import com.example.jacquey.ukullimapoultry.beanResponse.NewUserRegistration;
import com.example.jacquey.ukullimapoultry.beanResponse.OrderHistoryAPI;
import com.example.jacquey.ukullimapoultry.beanResponse.PlaceOrder;
import com.example.jacquey.ukullimapoultry.beanResponse.codeAPI;
import com.example.jacquey.ukullimapoultry.beanResponse.payAPI;
import com.example.jacquey.ukullimapoultry.beanResponse.receiveAPI;
import com.example.jacquey.ukullimapoultry.myaccount.OrderHistory;
import com.example.jacquey.ukullimapoultry.myaccount.OrderHistory_ViewDetails;
import com.example.jacquey.ukullimapoultry.myaccount.orderhistory_model;
import com.google.gson.Gson;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class payment2 extends AppCompatActivity {
    private String TAG = "payment";

    EditText payment_amount,payment_amount1;
    TextView thanks, request, continuebtn,continuebtn1 ;
    Button home, order,home2;
    RelativeLayout relative_lay2, relative_lay3,extrarelative_lay2;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setHomeButtonEnabled(true);

        relative_lay3 = (RelativeLayout) findViewById(R.id.relative_lay3);
        relative_lay2 = (RelativeLayout) findViewById(R.id.relative_lay2);
        extrarelative_lay2 = (RelativeLayout) findViewById(R.id.extralayout);

        thanks = (TextView) findViewById(R.id.thanks);
        request = (TextView) findViewById(R.id.your_request);
        payment_amount = (EditText) findViewById(R.id.amount);
        continuebtn = (TextView) findViewById(R.id.continuebtn);
        payment_amount1 = (EditText) findViewById(R.id.amount1);
        continuebtn1 = (TextView) findViewById(R.id.continuebtn1);
        home=findViewById(R.id.homebutton);
        home2=findViewById(R.id.homebutton2);
        order=findViewById(R.id.shopimage);


        payment_amount.setText(String.valueOf(SharedPreferenceActivity.getInstance().getString(Constant.TOTAL_TOTAL)));


        continuebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                makepayment();
            }


        });
        continuebtn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if ( DataValidation.isNotValidcode(payment_amount1.getText().toString())){
                    Toast.makeText(getApplicationContext(),"Invalid code length. Should be 10 characters.",Toast.LENGTH_LONG).show();

                }else {
                    getcode();

                }
            }


        });

        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(payment2.this, OrderHistory.class);
                startActivity(intent);
                finish();

            }
        });
        home2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(payment2.this, OrderHistory.class);
                startActivity(intent);
                finish();

            }
        });
        order.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(payment2.this, MainActivity.class);
                startActivity(intent);
                finish();

            }
        });
    }
    public void makepayment(){

        final AlertDialog progressbar =AppUtilits.createProgressBar(this);
        if (!NetworkUtility.isNetworkConnected(payment2.this)){
            Toast.makeText(getApplicationContext(),"Network error",Toast.LENGTH_LONG).show();
            AppUtilits.destroyDialog(progressbar);

        }else {

            ServiceWrapper serviceWrapper = new ServiceWrapper(null);
            Call<payAPI> makepaymentAPICall=serviceWrapper.makepaymentcall("1234", SharedPreferenceActivity.getInstance().getString(Constant.USER_DATA),  SharedPreferenceActivity.getInstance().getString(Constant.USER_order_id),
                   String.valueOf(SharedPreferenceActivity.getInstance().getString(Constant.TOTAL_TOTAL)), String.valueOf(payment_amount.getText().toString()));
            makepaymentAPICall.enqueue(new Callback<payAPI>() {
                @Override
                public void onResponse(Call<payAPI> call, Response<payAPI> response) {

                        Log.e(TAG, "response is " + response.body() + "  ---- " + new Gson().toJson(response.body()));
                        //  Log.e(TAG, "  ss sixe 1 ");
                        if (response.body() != null && response.isSuccessful()) {
                            //    Log.e(TAG, "  ss sixe 2 ");
                            if (response.body().getStatus() == 1) {

                                AppUtilits.destroyDialog(progressbar);
                               // AppUtilits.displayMessage(payment2.this, response.body().getMsg() );
                                extrarelative_lay2.setVisibility(View.VISIBLE);

                                relative_lay2.setVisibility(View.GONE);


                            } else {
                                AppUtilits.destroyDialog(progressbar);

                                AppUtilits.displayMessage(payment2.this, response.body().getMsg());

                            }

                        } else {
                            AppUtilits.displayMessage(payment2.this, getString(R.string.network_error));

                            AppUtilits.destroyDialog(progressbar);
                        }



                }

                @Override
                public void onFailure(Call<payAPI> call, Throwable t) {

                    Log.e(TAG, "  fail- to make payment"+ t.toString());
                    Toast.makeText(getApplicationContext(),"Failed to make payment",Toast.LENGTH_LONG).show();

                    AppUtilits.destroyDialog(progressbar);

                }
            });

        }


    }

    public void getcode(){
        final AlertDialog progressbar =AppUtilits.createProgressBar(this);
        if (!NetworkUtility.isNetworkConnected(payment2.this)){
            Toast.makeText(getApplicationContext(),"Network error",Toast.LENGTH_LONG).show();
            AppUtilits.destroyDialog(progressbar);

        }else {

            ServiceWrapper serviceWrapper = new ServiceWrapper(null);
            Call<codeAPI> codeAPICall=serviceWrapper.codecall("1234", SharedPreferenceActivity.getInstance().getString(Constant.USER_order_id),
                    String.valueOf(payment_amount1.getText().toString()));
            codeAPICall.enqueue(new Callback<codeAPI>() {
                @Override
                public void onResponse(Call<codeAPI> call, Response<codeAPI> response) {


                    if (response.body() != null && response.isSuccessful()) {
                        //    Log.e(TAG, "  ss sixe 2 ");
                        if (response.body().getStatus() == 1) {


                            AppUtilits.destroyDialog(progressbar);
                            extrarelative_lay2.setVisibility(View.GONE);

                            relative_lay3.setVisibility(View.VISIBLE);

                        } else {
                            AppUtilits.destroyDialog(progressbar);

                            AppUtilits.displayMessage(payment2.this, response.body().getMsg());

                        }

                    } else {
                        AppUtilits.displayMessage(payment2.this, getString(R.string.network_error));

                        AppUtilits.destroyDialog(progressbar);
                    }



                }

                @Override
                public void onFailure(Call<codeAPI> call, Throwable throwable) {


                    Toast.makeText(getApplicationContext(),"Failed to make payment",Toast.LENGTH_LONG).show();

                    AppUtilits.destroyDialog(progressbar);
                }


            });

        }


    }


    @Override
    public void onBackPressed() {



        android.support.v7.app.AlertDialog.Builder alertDialog = new android.support.v7.app.AlertDialog.Builder(payment2.this);
        alertDialog.setTitle("Cancel Request Confirmation!");
        alertDialog.setIcon(R.drawable.chick);
        alertDialog.setMessage("By going back, your order will be cancelled,\n\n" +
                "You cannot make any changes upon submitting\n\n" +
                "Would you like to cancel your order?\n\n");
        alertDialog.setNeutralButton("No, Stop", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {



            }
        }).setPositiveButton("Yes, Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent1 = new Intent(payment2.this, MainActivity.class);

                startActivity(intent1);


            }

        }).show();





    }

}

