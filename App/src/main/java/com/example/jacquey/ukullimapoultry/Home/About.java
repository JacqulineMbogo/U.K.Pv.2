package com.example.jacquey.ukullimapoultry.Home;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;

import com.example.jacquey.ukullimapoultry.LogIn.SignInActivity;
import com.example.jacquey.ukullimapoultry.MainActivity;
import com.example.jacquey.ukullimapoultry.R;
import com.example.jacquey.ukullimapoultry.Utility.AppUtilits;
import com.example.jacquey.ukullimapoultry.Utility.Constant;
import com.example.jacquey.ukullimapoultry.Utility.NetworkUtility;
import com.example.jacquey.ukullimapoultry.Utility.SharedPreferenceActivity;
import com.example.jacquey.ukullimapoultry.WebServices.ServiceWrapper;
import com.example.jacquey.ukullimapoultry.beanResponse.getCartDetails;
import com.example.jacquey.ukullimapoultry.cart.CartDetails;
import com.example.jacquey.ukullimapoultry.myaccount.OrderHistory;
import com.example.jacquey.ukullimapoultry.myaccount.myaccount;
import com.google.gson.Gson;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class About extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {



    private String TAG = "About";

    private NavigationView navigationView;
    private DrawerLayout drawer;
    private Menu mainmenu;

    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.open, R.string.close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();



        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);



    }

    public int GetScreenWidth(){
        int  width =100;

        DisplayMetrics displayMetrics = new DisplayMetrics();
        WindowManager windowManager =  (WindowManager) getApplicationContext().getSystemService(WINDOW_SERVICE);
        windowManager.getDefaultDisplay().getMetrics(displayMetrics);
        width = displayMetrics.widthPixels;

        return width;

    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.myaccount_toolbar_menu, menu);
        mainmenu = menu;
        if (mainmenu!=null){
            AppUtilits.UpdateCartCount(About.this, mainmenu);
            getUserCartDetails();
            Log.e(TAG , "  option menu create" );
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();
        Log.e(TAG, "  option click "+ item.getTitle() );
        //noinspection SimplifiableIfStatement
        if (id == R.id.search) {

            // Associate searchable configuration with the SearchView
            SearchManager searchManager =  (SearchManager) getSystemService(Context.SEARCH_SERVICE);
            SearchView searchView =  (SearchView) mainmenu.findItem(R.id.search).getActionView();
            final EditText searchEditText = (EditText) searchView.findViewById(android.support.v7.appcompat.R.id.search_src_text);

            searchEditText.setHint(getString(R.string.search_name));

            searchEditText.setHintTextColor(getResources().getColor(R.color.white));
            searchView.setSearchableInfo(
                    searchManager.getSearchableInfo(getComponentName()));

            searchEditText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
                @Override
                public boolean onEditorAction(TextView view, int actionId, KeyEvent event) {
                    //  Log.e("onClick: ", "-- " + searchEditText.getText().toString().trim());
                    if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                        //run query to the server
                        Log.e("onClick: ", "-- " + searchEditText.getText().toString().trim());
                        if ( null!=searchEditText.getText().toString().trim() && !searchEditText.getText().toString().trim().equals("")){

                        }
                        //  AppUtils.GetSearchResult( HomeActivity.this, TAG, searchEditText.getText().toString());
                    }
                    return false;
                }
            });
            return true;
        }else if (id==R.id.cart){
            Intent intent = new Intent(this, CartDetails.class);
            startActivity(intent);
        }

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




        }else if (id == R.id.nav_logout){


            Intent intent = new Intent(this, SignInActivity.class);
            startActivity(intent);

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
            AppUtilits.UpdateCartCount(About.this, mainmenu);
            Log.e(TAG , "  on resume mehtod " );
        }
    }

    public void getUserCartDetails(){

        if (!NetworkUtility.isNetworkConnected(About.this)){
            AppUtilits.displayMessage(About.this,  getString(R.string.network_not_connected));

        }else {
            Log.e(TAG, "  get user cart count "+ SharedPreferenceActivity.getInstance().getString(Constant.USER_DATA));
            ServiceWrapper service = new ServiceWrapper(null);
            Call<getCartDetails> call = service.getCartDetailsCall( "1234" , SharedPreferenceActivity.getInstance().getString(Constant.QUOTE_ID),
                    SharedPreferenceActivity.getInstance().getString(Constant.USER_DATA));

            call.enqueue(new Callback<getCartDetails>() {
                @Override
                public void onResponse(Call<getCartDetails> call, Response<getCartDetails> response) {
                    Log.e(TAG, "response is "+ response.body() + "  ---- "+ new Gson().toJson(response.body()));
                    //  Log.e(TAG, "  ss sixe 1 ");
                    if (response.body() != null && response.isSuccessful()) {
                        //    Log.e(TAG, "  ss sixe 2 ");
                        if (response.body().getStatus() == 1) {
                            //      Log.e(TAG, "  ss sixe 3 ");
                            Log.e(TAG, " size is  "+ String.valueOf(response.body().getInformation().getProdDetails().size()));
                            SharedPreferenceActivity.getInstance().saveInt( Constant.CART_ITEM_COUNT, response.body().getInformation().getProdDetails().size()  );
                            AppUtilits.UpdateCartCount(About.this, mainmenu);

                        }
                    }

                }

                @Override
                public void onFailure(Call<getCartDetails> call, Throwable t) {
                    Log.e(TAG, "  fail- add to cart item "+ t.toString());
                    //  AppUtilits.displayMessage(CartDetails.this, getString(R.string.fail_toGetcart));

                }
            });
        }






    }

}
