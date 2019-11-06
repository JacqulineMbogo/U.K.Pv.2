package com.example.jacquey.ukullimapoultry.admin;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.Toast;

import com.example.jacquey.ukullimapoultry.R;
import com.example.jacquey.ukullimapoultry.Utility.AppUtilits;
import com.example.jacquey.ukullimapoultry.Utility.Constant;
import com.example.jacquey.ukullimapoultry.Utility.NetworkUtility;
import com.example.jacquey.ukullimapoultry.Utility.SharedPreferenceActivity;
import com.example.jacquey.ukullimapoultry.WebServices.ServiceWrapper;
import com.example.jacquey.ukullimapoultry.beanResponse.DeliveryHistoryAPI;
import com.example.jacquey.ukullimapoultry.beanResponse.OrderHistoryAPI;
import com.example.jacquey.ukullimapoultry.myaccount.OrderHistory;
import com.example.jacquey.ukullimapoultry.myaccount.OrderHistory_Adapter;
import com.example.jacquey.ukullimapoultry.myaccount.orderhistory_model;
import com.google.gson.Gson;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class delivery_home extends AppCompatActivity {

    private String TAG = "deliveryhistory";
    private RecyclerView recyclerView_delivery;
    private ArrayList<deliveryhistory_model> Models = new ArrayList<>();
    private delivery_adapter adapter;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_orderhistory);



        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(true);

        recyclerView_delivery = (RecyclerView) findViewById(R.id.recycler_orderhistory);
        LinearLayoutManager mLayoutManger3 = new LinearLayoutManager( this, LinearLayoutManager.VERTICAL, false);
        recyclerView_delivery.setLayoutManager(mLayoutManger3);
        recyclerView_delivery.setItemAnimator(new DefaultItemAnimator());

        adapter = new delivery_adapter( delivery_home.this, Models);

        recyclerView_delivery.setAdapter(adapter);


        getdeliveryOrderHistory();

    }



    public void getdeliveryOrderHistory(){
        if (!NetworkUtility.isNetworkConnected(delivery_home.this)){
            Toast.makeText(getApplicationContext(),"Network error",Toast.LENGTH_LONG).show();

        }else {
              Log.e(TAG, "  user value "+ SharedPreferenceActivity.getInstance().getString(Constant.USER_staffDATA));
            ServiceWrapper service = new ServiceWrapper(null);
            Call<DeliveryHistoryAPI> call = service.deliveryhistorycall("1234", SharedPreferenceActivity.getInstance().getString(Constant.USER_staffDATA));
            call.enqueue(new Callback<DeliveryHistoryAPI>() {
                @Override
                public void onResponse(Call<DeliveryHistoryAPI> call, Response<DeliveryHistoryAPI>response) {
                    Log.e(TAG, "response is " + response.body() + "  ---- " + new Gson().toJson(response.body()));
                    //  Log.e(TAG, "  ss sixe 1 ");
                    if (response.body() != null && response.isSuccessful()) {
                        //    Log.e(TAG, "  ss sixe 2 ");
                        if (response.body().getStatus() == 1) {
                            //      Log.e(TAG, "  ss sixe 3 ");
                            Models.clear();

                            Log.d("staffid", Constant.USER_staffDATA);

                            if (response.body().getInformation().size()>0){

                                for (int i =0; i<response.body().getInformation().size(); i++){

                                    Models.add(  new deliveryhistory_model(response.body().getInformation().get(i).getOrderId(), response.body().getInformation().get(i).getShippingaddress(),
                                            response.body().getInformation().get(i).getUser(), response.body().getInformation().get(i).getDate(),response.body().getInformation().get(i).getDelivery_status()));


                                }
                                adapter.notifyDataSetChanged();
                            }

                        } else {
                            AppUtilits.displayMessage(delivery_home.this, response.body().getMsg());

                        }

                    } else {
                        AppUtilits.displayMessage(delivery_home.this, getString(R.string.network_error));

                    }
                }
                @Override
                public void onFailure(Call<DeliveryHistoryAPI> call, Throwable t) {
                    Log.e(TAG, "  fail- add to cart item "+ t.toString());
                    Toast.makeText(getApplicationContext(),"Failed to get deliveryhistory",Toast.LENGTH_LONG).show();

                }
            });


        }


    }
}
