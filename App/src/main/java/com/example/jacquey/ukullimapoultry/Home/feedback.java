package com.example.jacquey.ukullimapoultry.Home;

import android.app.AlertDialog;
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
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.jacquey.ukullimapoultry.LogIn.SignInActivity;
import com.example.jacquey.ukullimapoultry.MainActivity;
import com.example.jacquey.ukullimapoultry.R;
import com.example.jacquey.ukullimapoultry.Utility.AppUtilits;
import com.example.jacquey.ukullimapoultry.Utility.Constant;
import com.example.jacquey.ukullimapoultry.Utility.NetworkUtility;
import com.example.jacquey.ukullimapoultry.Utility.SharedPreferenceActivity;
import com.example.jacquey.ukullimapoultry.WebServices.ServiceWrapper;
import com.example.jacquey.ukullimapoultry.beanResponse.feedbackAPI;
import com.example.jacquey.ukullimapoultry.beanResponse.getCartDetails;
import com.example.jacquey.ukullimapoultry.beanResponse.receiveAPI;
import com.example.jacquey.ukullimapoultry.cart.CartDetails;
import com.example.jacquey.ukullimapoultry.myaccount.FeedbackHistory;
import com.example.jacquey.ukullimapoultry.myaccount.OrderHistory;
import com.example.jacquey.ukullimapoultry.myaccount.OrderHistory_ViewDetails;
import com.example.jacquey.ukullimapoultry.myaccount.myaccount;
import com.google.gson.Gson;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class feedback  extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

    private String TAG = "feedbackAPI";

    private NavigationView navigationView;
    private DrawerLayout drawer;
    private Menu mainmenu;
    TextView submit;
    EditText title, comment;

    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        submit= findViewById(R.id.send);
       title= findViewById(R.id.title);
       comment=findViewById(R.id.comment);



        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                submitfeedback();
            }
        });
    }

    public void submitfeedback(){
        final AlertDialog progressbar =AppUtilits.createProgressBar(this);
        if (!NetworkUtility.isNetworkConnected(feedback.this)){
            Toast.makeText(getApplicationContext(),"Network error",Toast.LENGTH_LONG).show();

        }else {

            ServiceWrapper serviceWrapper = new ServiceWrapper(null);
            Call<feedbackAPI> feedbackAPICall=serviceWrapper.feedbackcall("1234", String.valueOf(title.getText().toString()),
                  String.valueOf(comment.getText().toString()),String.valueOf(SharedPreferenceActivity.getInstance().getString(Constant.USER_DATA)));
            feedbackAPICall.enqueue(new Callback<feedbackAPI>() {
                @Override
                public void onResponse(Call<feedbackAPI> call, Response<feedbackAPI> response) {


                    Log.d(TAG, "reponse : "+ response.toString());
                    Log.d(TAG, "title : "+ String.valueOf(title));
                    Log.d(TAG, "comment : "+ String.valueOf(comment));
                    if (response.body() != null && response.isSuccessful()) {
                        //    Log.e(TAG, "  ss sixe 2 ");
                        if (response.body().getStatus() == 1) {


                            AppUtilits.destroyDialog(progressbar);
                            AppUtilits.displayMessage(feedback.this, response.body().getMsg() );

                            Intent intent = new Intent(feedback.this, FeedbackHistory.class);
                            startActivity(intent);
                            finish();

                        } else {
                            AppUtilits.destroyDialog(progressbar);

                            AppUtilits.displayMessage(feedback.this, response.body().getMsg());

                        }

                    } else {
                        Toast.makeText(getApplicationContext(),"Request failed",Toast.LENGTH_LONG).show();
                        AppUtilits.destroyDialog(progressbar);

                    }





                }

                @Override
                public void onFailure(Call<feedbackAPI> call, Throwable throwable) {


                    Toast.makeText(getApplicationContext(),"Failed to send feedback",Toast.LENGTH_LONG).show();

                }


            });

        }


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
            AppUtilits.UpdateCartCount(feedback.this, mainmenu);
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
            AppUtilits.UpdateCartCount(feedback.this, mainmenu);
            Log.e(TAG , "  on resume mehtod " );
        }
    }

    public void getUserCartDetails(){

        if (!NetworkUtility.isNetworkConnected(feedback.this)){
            AppUtilits.displayMessage(feedback.this,  getString(R.string.network_not_connected));

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
                            AppUtilits.UpdateCartCount(feedback.this, mainmenu);

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

    @Override
    public void onBackPressed() {


        Intent intent1 = new Intent(feedback.this, FeedbackHistory.class);

        startActivity(intent1);



    }

}
