package com.example.jacquey.ukullimapoultry.cart;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.jacquey.ukullimapoultry.Home.About;
import com.example.jacquey.ukullimapoultry.Home.FAQs;
import com.example.jacquey.ukullimapoultry.LogIn.SignInActivity;
import com.example.jacquey.ukullimapoultry.MainActivity;
import com.example.jacquey.ukullimapoultry.R;
import com.example.jacquey.ukullimapoultry.Utility.AppUtilits;
import com.example.jacquey.ukullimapoultry.Utility.Constant;
import com.example.jacquey.ukullimapoultry.Utility.NetworkUtility;
import com.example.jacquey.ukullimapoultry.Utility.SharedPreferenceActivity;
import com.example.jacquey.ukullimapoultry.WebServices.ServiceWrapper;
import com.example.jacquey.ukullimapoultry.beanResponse.getCartDetails;
import com.example.jacquey.ukullimapoultry.myaccount.FeedbackHistory;
import com.example.jacquey.ukullimapoultry.myaccount.OrderHistory;
import com.example.jacquey.ukullimapoultry.myaccount.myaccount;
import com.google.gson.Gson;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CartDetails extends AppCompatActivity  implements NavigationView.OnNavigationItemSelectedListener{

    private NavigationView navigationView;
    private DrawerLayout drawer;
    public static Menu mainmenu;

    private String TAG ="cartdetailss";
    private RecyclerView recycler_cartitem;
    private TextView cart_count, continuebtn,homebtn;
    public  TextView cart_totalamt;
    private Cart_Adapter cartAdapter;
    private ArrayList<Cartitem_Model> cartitemModels = new ArrayList<>();

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cartdetails);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.open, R.string.close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();


        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        recycler_cartitem = (RecyclerView) findViewById(R.id.recycler_cartitem);
        cart_count = (TextView) findViewById(R.id.cart_count);
        cart_totalamt = (TextView) findViewById(R.id.cart_totalamt);
        continuebtn = (TextView) findViewById(R.id.continuebtn);
        homebtn = (TextView) findViewById(R.id.homebtn);

        LinearLayoutManager mLayoutManger3 = new LinearLayoutManager( this, LinearLayoutManager.VERTICAL, false);
        recycler_cartitem.setLayoutManager(mLayoutManger3);
        recycler_cartitem.setItemAnimator(new DefaultItemAnimator());

        cartAdapter = new Cart_Adapter(this, cartitemModels);
        recycler_cartitem.setAdapter(cartAdapter);


            getUserCartDetails();

        continuebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

              //  String temp = cart_totalamt.getText().toString().replace("Ksh ", "");
              /*  if (!temp.equalsIgnoreCase("") && Integer.valueOf(temp)>0 ){

                    // then go to order summer page
                    Intent intent = new Intent(CartDetails.this , Order_Summary. class);
                    startActivity(intent);

                }else {

                  //  Toast.makeText(getApplicationContext(),"Please refresh by clicking on the cart icon on the top right corner ",Toast.LENGTH_LONG).show();
                    AppUtilits.displayMessage(CartDetails.this,  "Please refresh by clicking on the cart icon on the top right corner ");
                }
                */
                getUserCartDetails();
                Intent intent = new Intent(CartDetails.this , Order_Summary. class);
                startActivity(intent);


            }
        });
        homebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getUserCartDetails();

                Intent intent = new Intent(CartDetails.this , MainActivity. class);
                startActivity(intent);


            }
        });

    }

    public void getUserCartDetails(){

        if (!NetworkUtility.isNetworkConnected(CartDetails.this)){
            Toast.makeText(getApplicationContext(),"Network error",Toast.LENGTH_LONG).show();


        }else {
            //  Log.e(TAG, "  user value "+ SharePreferenceUtils.getInstance().getString(Constant.USER_DATA));
            ServiceWrapper service = new ServiceWrapper(null);
            Call<getCartDetails> call = service.getCartDetailsCall( "1234" , SharedPreferenceActivity.getInstance().getString(Constant.QUOTE_ID),
                    SharedPreferenceActivity.getInstance().getString(Constant.USER_DATA));

            call.enqueue(new Callback<getCartDetails>() {
                @Override
                public void onResponse(Call<getCartDetails> call, Response<getCartDetails> response) {
                    Log.e(TAG, "response is "+ response.body() + "  ---- "+ new Gson().toJson(response.body()));
                    Log.e(TAG, "  ss sixe 1 ");
                    if (response.body() != null && response.isSuccessful()) {
                        Log.e(TAG, "  ss sixe 2 ");
                        if (response.body().getStatus() == 1) {
                            Log.e(TAG, "  ss sixe 3 ");

                            cart_totalamt .setText(  "Ksh " + response.body().getInformation().getTotalprice());
                            cart_count.setText(getString(R.string.you_have)+" "+ String.valueOf(response.body().getInformation().getProdDetails().size()) +" "+
                                    getString(R.string.product_in_cart));

                            Log.e(TAG, " size is  "+ String.valueOf(response.body().getInformation().getProdDetails().size()));
                            SharedPreferenceActivity.getInstance().saveInt( Constant.CART_ITEM_COUNT, response.body().getInformation().getProdDetails().size()  );
                            AppUtilits.UpdateCartCount(CartDetails.this, mainmenu);

                            SharedPreferenceActivity.getInstance().saveString(Constant.USER_Totalprice, response.body().getInformation().getTotalprice());

Log.d("YYY", String.valueOf(response.body().getInformation().getTotalprice()));
                            cartitemModels.clear();

                            for (int i=0; i<response.body().getInformation().getProdDetails().size(); i++){


                                cartitemModels.add( new Cartitem_Model(response.body().getInformation().getProdDetails().get(i).getId(),
                                        response.body().getInformation().getProdDetails().get(i).getName(),
                                        response.body().getInformation().getProdDetails().get(i).getImgUrl(),"",
                                        response.body().getInformation().getProdDetails().get(i).getPrice(), response.body().getInformation().getProdDetails().get(i).getQty()));

                            }

                            cartAdapter.notifyDataSetChanged();



                        }else {
                            AppUtilits.displayMessage(CartDetails.this, response.body().getMsg() );
                        }
                    }else {
                        Toast.makeText(getApplicationContext(),"Please try again",Toast.LENGTH_LONG).show();
                    }

                }

                @Override
                public void onFailure(Call<getCartDetails> call, Throwable t) {
                    Log.e(TAG, "  fail- add to cart item "+ t.toString());
                    Toast.makeText(getApplicationContext(),"please try again. Failed to get user cart details ",Toast.LENGTH_LONG).show();

                }
            });





        }






    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.myaccount_toolbar_menu, menu);
        mainmenu = menu;
        if (mainmenu!=null)
            AppUtilits.UpdateCartCount(CartDetails.this, mainmenu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();
        Log.e(TAG, "  option click "+ item.getTitle() );
        //noinspection SimplifiableIfStatement


        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        int id =  item.getItemId();
        Log.e(TAG, "navi option "+ item.getTitle());

        if (id == R.id.nav_home){

            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);


        }else if (id == R.id.my_cart){
            Intent intent = new Intent(this,CartDetails.class);
            startActivity(intent);



        }else if (id == R.id.nav_myaccount){
            Intent intent = new Intent(this, myaccount.class);
            startActivity(intent);




        }else if (id == R.id.nav_feedback){
            Intent intent = new Intent(this, FeedbackHistory.class);
            startActivity(intent);




        }else if (id == R.id.nav_logout){

            SharedPreferenceActivity.getInstance().clear();
            Intent intent = new Intent(this, SignInActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);// this will clear all the stack
            startActivity(intent);
            finish();



        }else if (id == R.id.nav_order_history){

            Intent intent = new Intent(this, OrderHistory.class);
            startActivity(intent);


        }else if (id == R.id.about){


            Intent intent = new Intent(this, About.class);
            startActivity(intent);

        }else if (id == R.id.faq){


            Intent intent = new Intent(this, FAQs.class);
            startActivity(intent);

        }
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mainmenu!=null){
            AppUtilits.UpdateCartCount(CartDetails.this, mainmenu);
            Log.e(TAG , "  on resume mehtod " );
        }
    }


}
