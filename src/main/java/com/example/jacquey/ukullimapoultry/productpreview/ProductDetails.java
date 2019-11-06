package com.example.jacquey.ukullimapoultry.productpreview;

import android.app.AlertDialog;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatRatingBar;
import android.support.v7.widget.RecyclerView;
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
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.jacquey.ukullimapoultry.Home.About;
import com.example.jacquey.ukullimapoultry.Home.BestSelling_Adapter;
import com.example.jacquey.ukullimapoultry.Home.DuckProd_Model;
import com.example.jacquey.ukullimapoultry.Home.FAQs;
import com.example.jacquey.ukullimapoultry.LogIn.SignInActivity;
import com.example.jacquey.ukullimapoultry.MainActivity;
import com.example.jacquey.ukullimapoultry.R;
import com.example.jacquey.ukullimapoultry.Utility.AppUtilits;
import com.example.jacquey.ukullimapoultry.Utility.Constant;
import com.example.jacquey.ukullimapoultry.Utility.NetworkUtility;
import com.example.jacquey.ukullimapoultry.Utility.SharedPreferenceActivity;
import com.example.jacquey.ukullimapoultry.WebServices.ServiceWrapper;
import com.example.jacquey.ukullimapoultry.beanResponse.AddtoCart;
import com.example.jacquey.ukullimapoultry.beanResponse.ProductDetail_Res;
import com.example.jacquey.ukullimapoultry.beanResponse.getCartDetails;
import com.example.jacquey.ukullimapoultry.cart.CartDetails;
import com.example.jacquey.ukullimapoultry.cart.Order_Summary;
import com.example.jacquey.ukullimapoultry.myaccount.FeedbackHistory;
import com.example.jacquey.ukullimapoultry.myaccount.OrderHistory;
import com.example.jacquey.ukullimapoultry.myaccount.myaccount;
import com.google.gson.Gson;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class ProductDetails extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private String TAG ="productDetails";
    private String prod_id="";
    private TextView prod_name, prod_price, prod_oldprice, prod_rating_count, prod_stock, prod_qty,details;
    private AppCompatRatingBar prod_rating;
    private ImageView add_to_cart,view_cart,imageurl,add_to_cart1;
    // related product
    private RecyclerView recycler_relatedProd;
    private ArrayList<DuckProd_Model> relatedProdModelArrayList = new ArrayList<DuckProd_Model>();
    private BestSelling_Adapter relatedProdAdapter;
    // overview and review tab layout


    public String prod_overview ="";
    public String prod_fulldetails ="";
    public String prod_review ="";
    private Context mContext;

    private FragmentManager fragmentManager = getSupportFragmentManager();

    private NavigationView navigationView;
    private DrawerLayout drawer;
    private Menu mainmenu;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_preview);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.open, R.string.close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();



        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);



        final Intent intent = getIntent();
        prod_id =  intent.getExtras().getString("prod_id");


        details = (TextView) findViewById(R.id.detail);
        prod_name = (TextView) findViewById(R.id.prod_name);
        prod_price =(TextView) findViewById(R.id.prod_price);
        prod_stock = (TextView) findViewById(R.id.stock_avail);
        //prod_qty = (TextView) findViewById(R.id.prod_qty_value);


        add_to_cart = (ImageView) findViewById(R.id.add_to_cart);
      //  add_to_cart1 = (ImageView) findViewById(R.id.add_to_cart1);
        view_cart = (ImageView) findViewById(R.id.view_cart);
        imageurl=findViewById(R.id.imgurl);

        getProductDetails();


         add_to_cart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // store produ id with user id on server  and get quate id as response and store it on share preferernce
                if (prod_stock.getText().toString().equals("Out of Stock") ){

                    Toast.makeText(getApplicationContext(),"Sorry,product is out out stock", Toast.LENGTH_LONG).show();
                }else{
                    addtocartapi( );
                }

            }
        });


        view_cart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ProductDetails.this,CartDetails.class);
                startActivity(intent);


            }
        });




    }

   public int GetScreenWidth(){
        int  width =100;

        DisplayMetrics displayMetrics = new DisplayMetrics();
        WindowManager windowManager =  (WindowManager) getApplicationContext().getSystemService(WINDOW_SERVICE);
        windowManager.getDefaultDisplay().getMetrics(displayMetrics);
        width = displayMetrics.widthPixels;

        return width;

    }


    public void getProductDetails(){

        final AlertDialog progressbar =AppUtilits.createProgressBar(this);


        if (!NetworkUtility.isNetworkConnected(ProductDetails.this)){
            AppUtilits.displayMessage(ProductDetails.this,  getString(R.string.network_not_connected));

            AppUtilits.destroyDialog(progressbar);
        }else {
            ServiceWrapper service = new ServiceWrapper(null);
            Call<ProductDetail_Res> call = service.getProductDetails("1234", prod_id );
            call.enqueue(new Callback<ProductDetail_Res>() {
                @Override
                public void onResponse(Call<ProductDetail_Res> call, Response<ProductDetail_Res> response) {
                    Log.e(TAG, "reposne is " + response.body().getInformation());
                    if (response.body() != null && response.isSuccessful()) {
                        if (response.body().getStatus() == 1) {

                            if (response.body().getInformation().getName()!=null){
                                prod_name.setText(response.body().getInformation().getName());

                                if (response.body().getInformation().getStock() >0){
                                    prod_stock.setText( getString(R.string.instock));
                                }else {
                                    prod_stock.setText( getString(R.string.outofstock));
                                }



                                AppUtilits.destroyDialog(progressbar);


                                 prod_price.setText("Ksh "+""+response.body().getInformation().getPrice());
                                details.setText(response.body().getInformation().getDescription());
//                                prod_qty.setText("1");
                                // prod image

                                Glide.with(ProductDetails.this)
                                        .load(response.body().getInformation().getImgUrl())
                                        .into(imageurl);

                                // Log.e(TAG, "rating count "+
                                Log.e(TAG, "prod_price "+ prod_price);



                            }


                        } else {
                            Log.e(TAG, "failed to get rnew prod " + response.body().getMsg());
                            // AppUtilits.displayMessage(HomeActivity.this,  response.body().getMsg());
                        }
                    } else {
                        Log.e(TAG, "failed to get rnew prod " + response.body().getMsg());
                        // AppUtilits.displayMessage(HomeActivity.this,  response.body().getMsg());
                    }
                }

                @Override
                public void onFailure(Call<ProductDetail_Res> call, Throwable t) {
                    Log.e(TAG, " fail best sell "+ t.toString());
                }
            });

        }


    }

    public void addtocartapi(){

        final AlertDialog progressbar =AppUtilits.createProgressBar(this);

        if (!NetworkUtility.isNetworkConnected(ProductDetails.this)){
            AppUtilits.displayMessage(ProductDetails.this,  getString(R.string.network_not_connected));

            AppUtilits.destroyDialog(progressbar);
        }else {

            //  Log.e(TAG, "  user value "+ SharePreferenceUtils.getInstance().getString(Constant.USER_DATA));
            ServiceWrapper service = new ServiceWrapper(null);
            Call<AddtoCart> call = service.addtoCartCall("12345", prod_id,SharedPreferenceActivity.getInstance().getString(Constant.USER_DATA)  );
            call.enqueue(new Callback<AddtoCart>() {
                @Override
                public void onResponse(Call<AddtoCart> call, Response<AddtoCart> response) {
                     Log.e(TAG, "prod_id "+ prod_id);
                    Log.e(TAG, "prod_price "+ prod_price);
                    if (response.body() != null && response.isSuccessful()) {
                        if (response.body().getStatus() == 1) {

                            AppUtilits.destroyDialog(progressbar);
                        SharedPreferenceActivity.getInstance().saveString(Constant.QUOTE_ID, response.body().getInformation().getQouteId());
                            Intent intent = new Intent(ProductDetails.this , CartDetails. class);
                            startActivity(intent);
                            SharedPreferenceActivity.getInstance().saveInt( Constant.CART_ITEM_COUNT,   response.body().getInformation().getCartCount());
                            AppUtilits.UpdateCartCount(ProductDetails.this,mainmenu);

                        }else {
                            AppUtilits.destroyDialog(progressbar);
                            AppUtilits.displayMessage(ProductDetails.this, getString(R.string.fail_add_to_cart));
                        }
                    }else {
                        AppUtilits.destroyDialog(progressbar);
                        AppUtilits.displayMessage(ProductDetails.this, getString(R.string.network_error));
                    }


                }

                @Override
                public void onFailure(Call<AddtoCart> call, Throwable t) {
                    // Log.e(TAG, "  fail- add to cart item "+ t.toString());
                    AppUtilits.destroyDialog(progressbar);
                    AppUtilits.displayMessage(ProductDetails.this, getString(R.string.fail_add_to_cart));
                }
            });
        }
    }







        @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.myaccount_toolbar_menu, menu);
        mainmenu = menu;
        if (mainmenu!=null)
            AppUtilits.UpdateCartCount(ProductDetails.this, mainmenu);
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
        if (mainmenu!=null)
            AppUtilits.UpdateCartCount(ProductDetails.this, mainmenu);
    }


}
