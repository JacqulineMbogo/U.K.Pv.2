package com.example.jacquey.ukullimapoultry.cart;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import com.example.jacquey.ukullimapoultry.R;
import com.example.jacquey.ukullimapoultry.Utility.AppUtilits;
import com.example.jacquey.ukullimapoultry.Utility.Constant;
import com.example.jacquey.ukullimapoultry.Utility.NetworkUtility;
import com.example.jacquey.ukullimapoultry.Utility.SharedPreferenceActivity;
import com.example.jacquey.ukullimapoultry.WebServices.ServiceWrapper;
import com.example.jacquey.ukullimapoultry.beanResponse.GetAddress;
import com.example.jacquey.ukullimapoultry.beanResponse.PlaceOrder;
import com.google.gson.Gson;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.support.constraint.Constraints.TAG;

public class OrderAddress extends AppCompatActivity {

    private FloatingActionButton fab;
    private RecyclerView recyclerView;
    private String TAG = "orderaddress";
    private TextView continuebtn;

    private OrderAddress_Adapter adapter;
    private ArrayList<OrderAddress_Model>  modellist = new ArrayList<>();
    private String totalamount="0";
    public String addressid ="0";
    public String pin="0";

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_orderaddress);

        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setHomeButtonEnabled(true);

        final Intent intent = getIntent();
       // totalamount =  intent.getExtras().getString("amount");


        fab =  findViewById(R.id.fab);
        recyclerView = findViewById(R.id.order_recyclerview);
        continuebtn = findViewById(R.id.continuebtn);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(OrderAddress.this, OrderAddress_AddNew.class);
                startActivity(intent);

            }
        });

        Log.e(TAG, " payAPI detaisl -"+SharedPreferenceActivity.getInstance().getString(Constant.USER_email)+"--"+
                SharedPreferenceActivity.getInstance().getString(Constant.USER_phone)+"--"+
                totalamount+ " buy from app "+"--" +SharedPreferenceActivity.getInstance().getString(Constant.USER_name) );

        // totalamount ="12";
        continuebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (!addressid.equalsIgnoreCase("0")){



                    AlertDialog.Builder alertDialog = new AlertDialog.Builder(OrderAddress.this);
                    alertDialog.setTitle("Order Request Confirmation!");
                    alertDialog.setIcon(R.drawable.chick);
                    alertDialog.setMessage("You are about to submit an order request,\n\n" +
                            "You cannot make any changes upon submitting\n\n" +
                            "Would you like to proceed?\n\n");
                    alertDialog.setNeutralButton("No, Stop", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                            Intent intent1 = new Intent(OrderAddress.this, CartDetails.class);

                            startActivity(intent1);



                        }
                    }).setPositiveButton("Yes, Continue", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Intent intent1 = new Intent(OrderAddress.this, PlaceOrderActivity.class);
                            intent1.putExtra("addressid", addressid);
                            intent1.putExtra("pin", pin);
                            startActivity(intent1);

                            Log.d("address", addressid);

                            Log.d("pin", pin);

                        }

                    }).show();



                }else {
                    AppUtilits.displayMessage(OrderAddress.this, getResources().getString(R.string.select_address) );
                }

            }
        });

        LinearLayoutManager mLayoutManger3 = new LinearLayoutManager( this, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(mLayoutManger3);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        adapter = new OrderAddress_Adapter(OrderAddress.this, modellist);
        recyclerView.setAdapter(adapter);





    }

    @Override
    protected void onResume() {
        super.onResume();
        getUserAddress();

    }


    public void getUserAddress(){

        if (!NetworkUtility.isNetworkConnected(OrderAddress.this)){
            AppUtilits.displayMessage(OrderAddress.this,  getString(R.string.network_not_connected));

        }else {

            ServiceWrapper serviceWrapper = new ServiceWrapper(null);
            Call<GetAddress> call = serviceWrapper.getUserAddresscall("1234", SharedPreferenceActivity.getInstance().getString(Constant.USER_DATA));
            call.enqueue(new Callback<GetAddress>() {
                @Override
                public void onResponse(Call<GetAddress> call, Response<GetAddress> response) {
                    Log.e(TAG, "response is "+ response.body() + "  ---- "+ new Gson().toJson(response.body()));
                    //  Log.e(TAG, "  ss sixe 1 ");
                    if (response.body() != null && response.isSuccessful()) {
                        //    Log.e(TAG, "  ss sixe 2 ");
                        if (response.body().getStatus() == 1) {

                            if (response.body().getInformation().getAddressDetails().size()>0){

                                modellist.clear();
                                for (int i=0; i<response.body().getInformation().getAddressDetails().size() ; i++){

                                    modellist.add(new OrderAddress_Model(response.body().getInformation().getAddressDetails().get(i).getAddressId(),
                                            response.body().getInformation().getAddressDetails().get(i).getFullname(),
                                            response.body().getInformation().getAddressDetails().get(i).getAddress1() +"\n"+
                                                    response.body().getInformation().getAddressDetails().get(i).getAddress2()+"\n"+
                                                    response.body().getInformation().getAddressDetails().get(i).getCity()+" "+
                                                   response.body().getInformation().getAddressDetails().get(i).getState()+"\n"+
                                                    response.body().getInformation().getAddressDetails().get(i).getPincode(),
                                            response.body().getInformation().getAddressDetails().get(i).getPhone()));
                                }

                                adapter.notifyDataSetChanged();

                            }



                        }else {
                            //AppUtilits.displayMessage(OrderAddress.this, response.body().getMsg() );
                            Intent intent = new Intent(OrderAddress.this, OrderAddress_AddNew.class);
                            startActivity(intent);
                        }
                    }else {
                        AppUtilits.displayMessage(OrderAddress.this, getString(R.string.network_error));
                    }

                }

                @Override
                public void onFailure(Call<GetAddress> call, Throwable t) {
                    Log.e(TAG, "  fail- get user address "+ t.toString());
                    AppUtilits.displayMessage(OrderAddress.this, getString(R.string.fail_togetaddress));


                }
            });


        }
    }



}
