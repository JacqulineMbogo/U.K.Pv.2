package com.example.jacquey.ukullimapoultry.myaccount;


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
import com.example.jacquey.ukullimapoultry.Utility.SharedPreferenceActivity;
import com.example.jacquey.ukullimapoultry.cart.CartDetails;
import com.example.jacquey.ukullimapoultry.cart.OrderAddress_AddNew;


public class myaccount extends AppCompatActivity  implements NavigationView.OnNavigationItemSelectedListener{
    private NavigationView navigationView;
    private DrawerLayout drawer;
    public static Menu mainmenu;

    private String TAG ="myaccount";
    private TextView myacc_username, myacc_email, myacc_phone, myacc_address, myacc_orderhistory;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_myaccount);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.open, R.string.close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();


        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        myacc_username = (TextView) findViewById(R.id.myacc_username);
        myacc_email = (TextView) findViewById(R.id.myacc_email);
        myacc_phone =(TextView) findViewById(R.id.myacc_phone);
        myacc_address = (TextView) findViewById(R.id.myacc_address);
        //  myacc_order =(TextView) findViewById(R.id.myacc_orderhistory);
        myacc_orderhistory = (TextView) findViewById(R.id.myacc_orderhistory);

        myacc_username.setText(SharedPreferenceActivity.getInstance().getString(Constant.USER_name));
        myacc_email.setText(SharedPreferenceActivity.getInstance().getString(Constant.USER_email));
        myacc_phone.setText(SharedPreferenceActivity.getInstance().getString(Constant.USER_phone));

        myacc_address.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(myaccount.this, OrderAddress_AddNew.class);
                startActivity(intent);

            }
        });
        myacc_orderhistory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(myaccount.this, OrderHistory.class);
                startActivity(intent);
            }
        });


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.myaccount_toolbar_menu, menu);
        mainmenu = menu;
        if (mainmenu!=null)
            AppUtilits.UpdateCartCount(myaccount.this, mainmenu);
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
            //  Intent intent = new Intent(this, CartDetails.class);
            // startActivity(intent);
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
        if (mainmenu!=null)
            AppUtilits.UpdateCartCount(myaccount.this, mainmenu);
    }


    @Override
    public void onBackPressed() {


        Intent intent1 = new Intent(myaccount.this, MainActivity.class);

        startActivity(intent1);



    }

}
