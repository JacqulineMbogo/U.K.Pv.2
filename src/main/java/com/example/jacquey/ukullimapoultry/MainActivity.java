package com.example.jacquey.ukullimapoultry;


import android.app.AlertDialog;
import android.app.SearchManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.util.DisplayMetrics;
import android.util.Log;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.jacquey.ukullimapoultry.Home.About;
import com.example.jacquey.ukullimapoultry.Home.DuckProd_Model;
import com.example.jacquey.ukullimapoultry.Home.ChickenProd_Adapter;
import com.example.jacquey.ukullimapoultry.Home.DuckProd_Adapter;
import com.example.jacquey.ukullimapoultry.Home.FAQs;
import com.example.jacquey.ukullimapoultry.Home.MyDividerItemDecoration;
import com.example.jacquey.ukullimapoultry.Home.NewProd_Adapter;
import com.example.jacquey.ukullimapoultry.Home.NewProd_Model;
import com.example.jacquey.ukullimapoultry.Home.Search_Adapter;
import com.example.jacquey.ukullimapoultry.Home.Search_Model;
import com.example.jacquey.ukullimapoultry.Home.TurkeyProd_Adapter;
import com.example.jacquey.ukullimapoultry.Home.TurkeyProd_Model;
import com.example.jacquey.ukullimapoultry.LogIn.SignInActivity;
import com.example.jacquey.ukullimapoultry.Utility.AppUtilits;
import com.example.jacquey.ukullimapoultry.Utility.Constant;
import com.example.jacquey.ukullimapoultry.Utility.NetworkUtility;
import com.example.jacquey.ukullimapoultry.Utility.SharedPreferenceActivity;
import com.example.jacquey.ukullimapoultry.WebServices.ServiceWrapper;
import com.example.jacquey.ukullimapoultry.beanResponse.ChickenProductRes;
import com.example.jacquey.ukullimapoultry.beanResponse.DuckProductRes;
import com.example.jacquey.ukullimapoultry.beanResponse.GetbannerModel;
import com.example.jacquey.ukullimapoultry.beanResponse.SearchProductRes;
import com.example.jacquey.ukullimapoultry.beanResponse.TurkeyProductRes;
import com.example.jacquey.ukullimapoultry.beanResponse.getCartDetails;
import com.example.jacquey.ukullimapoultry.cart.CartDetails;
import com.example.jacquey.ukullimapoultry.cart.payment2;
import com.example.jacquey.ukullimapoultry.myaccount.FeedbackHistory;
import com.example.jacquey.ukullimapoultry.myaccount.OrderHistory;
import com.example.jacquey.ukullimapoultry.myaccount.myaccount;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import ss.com.bannerslider.banners.Banner;
import ss.com.bannerslider.banners.RemoteBanner;
import ss.com.bannerslider.views.BannerSlider;


public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener
        {



    private String TAG = "MainActivity";
    private RecyclerView recycler_chicken,recycler_ducks,recycler_turkey,recycler_search;
    private ArrayList<DuckProd_Model> DuckProdModelList = new ArrayList<DuckProd_Model>();
            private ArrayList<TurkeyProd_Model> TurkeyProdModelList = new ArrayList<>();


    private ArrayList<NewProd_Model> NewProdModelList = new ArrayList<>();
    private ArrayList<Search_Model> SearchProdModelList = new ArrayList<>();
    private NewProd_Model newProd_model;
    private NewProd_Adapter newProd_adapter;

            private ChickenProd_Adapter chickenProd_adapter;
            private DuckProd_Adapter duckProd_adapter;
            private TurkeyProd_Adapter turkeyProd_adapter;
            private Search_Adapter searchProd_adapter;

            private NavigationView navigationView;
            private DrawerLayout drawer;
            private Menu mainmenu;

            private BannerSlider bannerSlider;
            private List<Banner> remoteBanners=new ArrayList<>();
            private TextView txtusername;
            LinearLayout search;


            @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

                drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
                ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.open, R.string.close);
                drawer.addDrawerListener(toggle);
                toggle.syncState();



                navigationView = (NavigationView) findViewById(R.id.nav_view);
                navigationView.setNavigationItemSelectedListener(this);




                bannerSlider = (BannerSlider) findViewById(R.id.banner_slider1);
                search = findViewById(R.id.search);

                //for chicken product


                recycler_chicken=(RecyclerView) findViewById(R.id.recycler_chicken);
                recycler_search=(RecyclerView) findViewById(R.id.recycler_search);

                LinearLayoutManager mLayoutManger3 = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);

                recycler_chicken.setLayoutManager(mLayoutManger3);
                recycler_chicken.setItemAnimator(new

                        DefaultItemAnimator());

                chickenProd_adapter=new ChickenProd_Adapter(this,NewProdModelList, GetScreenWidth());
                recycler_chicken.addItemDecoration(new MyDividerItemDecoration(this, LinearLayoutManager.VERTICAL, 16));
                recycler_chicken.setAdapter(chickenProd_adapter);



                //for duck product


                recycler_ducks=(RecyclerView) findViewById(R.id.recycler_ducks);

                LinearLayoutManager mLayoutManger4 = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);

                recycler_ducks.setLayoutManager(mLayoutManger4);
                recycler_ducks.setItemAnimator(new

                        DefaultItemAnimator());

                duckProd_adapter=new DuckProd_Adapter(this,DuckProdModelList, GetScreenWidth());
                recycler_ducks.addItemDecoration(new MyDividerItemDecoration(this, LinearLayoutManager.VERTICAL, 16));
                recycler_ducks.setAdapter(duckProd_adapter);

                //for turkey product


                recycler_turkey=(RecyclerView) findViewById(R.id.recycler_turkey);

                LinearLayoutManager mLayoutManger5 = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);

                recycler_turkey.setLayoutManager(mLayoutManger5);
                recycler_turkey.setItemAnimator(new

                        DefaultItemAnimator());

                turkeyProd_adapter=new TurkeyProd_Adapter(this,TurkeyProdModelList, GetScreenWidth());
                recycler_turkey.addItemDecoration(new MyDividerItemDecoration(this, LinearLayoutManager.VERTICAL, 16));
                recycler_turkey.setAdapter(turkeyProd_adapter);

                LinearLayoutManager mLayoutManger7 = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
                searchProd_adapter=new Search_Adapter(this,SearchProdModelList, GetScreenWidth());

                recycler_search.setAdapter(searchProd_adapter);
                recycler_search.setLayoutManager(mLayoutManger7);
                recycler_search.setItemAnimator(new  DefaultItemAnimator());



                        getChickenProdRes();
                getDuckProdRes();
                getTurkeyProdRes();
                getbannerimg();


            }


            public void getbannerimg(){
                if (!NetworkUtility.isNetworkConnected(MainActivity.this)){
                    AppUtilits.displayMessage(MainActivity.this,  getString(R.string.network_not_connected));


                }else {
                    ServiceWrapper service = new ServiceWrapper(null);
                    Call<GetbannerModel> call = service.getbannerModelCall("1234");
                    call.enqueue(new Callback<GetbannerModel>() {
                        @Override
                        public void onResponse(Call<GetbannerModel> call, Response<GetbannerModel> response) {
                            //  Log.e(TAG, " banner response is "+ response.body().getInformation().toString());
                            if (response.body()!= null && response.isSuccessful()){
                                if (response.body().getStatus() ==1) {
                                    if (response.body().getInformation().size() > 0) {

                                        for (int i=0; i<response.body().getInformation().size(); i++) {
                                            remoteBanners.add(new RemoteBanner(response.body().getInformation().get(i).getImgurl()));

                                        }


                                    }else {

                                        remoteBanners.add(new RemoteBanner("https://assets.materialup.com/uploads/dcc07ea4-845a-463b-b5f0-4696574da5ed/preview.jpg"));
                                        remoteBanners.add(new RemoteBanner("https://assets.materialup.com/uploads/dcc07ea4-845a-463b-b5f0-4696574da5ed/preview.jpg"));
                                    }

                                    bannerSlider.setBanners(remoteBanners);
                                }else {
                                    remoteBanners.add(new RemoteBanner("https://assets.materialup.com/uploads/dcc07ea4-845a-463b-b5f0-4696574da5ed/preview.jpg"));
                                    remoteBanners.add(new RemoteBanner("https://assets.materialup.com/uploads/dcc07ea4-845a-463b-b5f0-4696574da5ed/preview.jpg"));
                                    bannerSlider.setBanners(remoteBanners);
                                }
                            }
                        }

                        @Override
                        public void onFailure(Call<GetbannerModel> call, Throwable t) {
                            //  Log.e(TAG, "fail banner ads "+ t.toString());
                        }
                    });
                }

            }



            public void getTurkeyProdRes() {

                if (!NetworkUtility.isNetworkConnected(MainActivity.this)) {
                    AppUtilits.displayMessage(MainActivity.this, getString(R.string.network_not_connected));

                } else {
                    ServiceWrapper service = new ServiceWrapper(null);
                    Call<TurkeyProductRes> call = service.getTurkeyProductRes("1234");
                    call.enqueue(new Callback<TurkeyProductRes>() {
                        @Override
                        public void onResponse(Call<TurkeyProductRes> call, Response<TurkeyProductRes> response) {
                            Log.e(TAG, " response is " + response.body().getInformation().toString());
                            if (response.body() != null && response.isSuccessful()) {
                                if (response.body().getStatus() == 1) {
                                    if (response.body().getInformation().size() > 0) {

                                        TurkeyProdModelList.clear();
                                        for (int i = 0; i < response.body().getInformation().size(); i++) {

                                            TurkeyProdModelList.add(new TurkeyProd_Model(response.body().getInformation().get(i).getId(), response.body().getInformation().get(i).getName(),
                                                    response.body().getInformation().get(i).getImgUrl(),response.body().getInformation().get(i).getPrice(),response.body().getInformation().get(i).getStock()));

                                        }

                                        turkeyProd_adapter.notifyDataSetChanged();
                                    }

                                } else {
                                    Log.e(TAG, "failed to get rnew prod " + response.body().getMsg());
                                    // AppUtilits.displayMessage(HomeActivity.this,  response.body().getMsg());
                                }
                            } else {
                                // AppUtilits.displayMessage(HomeActivity.this,  getString(R.string.failed_request));

                                //  getNewProdRes();
                            }
                        }

                        @Override
                        public void onFailure(Call<TurkeyProductRes> call, Throwable t) {
                            Log.e(TAG, "fail new prod " + t.toString());

                        }
                    });

                }

            }



            public void getDuckProdRes() {

                if (!NetworkUtility.isNetworkConnected(MainActivity.this)) {
                    AppUtilits.displayMessage(MainActivity.this, getString(R.string.network_not_connected));

                } else {
                    ServiceWrapper service = new ServiceWrapper(null);
                    Call<DuckProductRes> call = service.getDuckProductRes("1234");
                    call.enqueue(new Callback<DuckProductRes>() {
                        @Override
                        public void onResponse(Call<DuckProductRes> call, Response<DuckProductRes> response) {
                            Log.e(TAG, " response is " + response.body().getInformation().toString());
                            if (response.body() != null && response.isSuccessful()) {
                                if (response.body().getStatus() == 1) {
                                    if (response.body().getInformation().size() > 0) {

                                        DuckProdModelList.clear();
                                        for (int i = 0; i < response.body().getInformation().size(); i++) {

                                            DuckProdModelList.add(new DuckProd_Model(response.body().getInformation().get(i).getId(), response.body().getInformation().get(i).getName(),
                                                    response.body().getInformation().get(i).getImgUrl(),response.body().getInformation().get(i).getPrice(),response.body().getInformation().get(i).getStock()));

                                        }

                                        duckProd_adapter.notifyDataSetChanged();
                                    }

                                } else {
                                    Log.e(TAG, "failed to get rnew prod " + response.body().getMsg());
                                    // AppUtilits.displayMessage(HomeActivity.this,  response.body().getMsg());
                                }
                            } else {
                                // AppUtilits.displayMessage(HomeActivity.this,  getString(R.string.failed_request));

                                //  getNewProdRes();
                            }
                        }

                        @Override
                        public void onFailure(Call<DuckProductRes> call, Throwable t) {
                            Log.e(TAG, "fail new prod " + t.toString());

                        }
                    });

                }

            }



            public void getsearchresult(String s) {


                final AlertDialog progressbar =AppUtilits.createProgressBar(this);


                if (!NetworkUtility.isNetworkConnected(MainActivity.this)) {
                    AppUtilits.displayMessage(MainActivity.this, getString(R.string.network_not_connected));

                } else {
                    ServiceWrapper service = new ServiceWrapper(null);
                    Call<SearchProductRes> call = service.getSearchProductRes(String.valueOf(s));
                    call.enqueue(new Callback<SearchProductRes>() {
                        @Override
                        public void onResponse(Call<SearchProductRes> call, Response<SearchProductRes> response) {
                            Log.e(TAG, " response is " + response.body().getInformation().toString());
                            if (response.body() != null && response.isSuccessful()) {
                                if (response.body().getStatus() == 1) {
                                    if (response.body().getInformation().size() > 0) {

                                        AppUtilits.destroyDialog(progressbar);
                                        SearchProdModelList.clear();
                                        for (int i = 0; i < response.body().getInformation().size(); i++) {

                                            SearchProdModelList.add(new Search_Model(response.body().getInformation().get(i).getId(), response.body().getInformation().get(i).getName(),
                                                    response.body().getInformation().get(i).getImgUrl(),response.body().getInformation().get(i).getPrice(),response.body().getInformation().get(i).getStock()));


                                            search.setVisibility(View.VISIBLE);


                                        }

                                        searchProd_adapter.notifyDataSetChanged();
                                    }

                                } else {
                                    AppUtilits.destroyDialog(progressbar);
                                    Log.e(TAG, "failed to get rnew prod " + response.body().getMsg());

                                    Toast.makeText(getApplicationContext(),"No Product found",Toast.LENGTH_LONG).show();

                                    // AppUtilits.displayMessage(HomeActivity.this,  response.body().getMsg());
                                }
                            } else {
                                AppUtilits.destroyDialog(progressbar);
                                // AppUtilits.displayMessage(HomeActivity.this,  getString(R.string.failed_request));

                                //  getNewProdRes();
                            }
                        }

                        @Override
                        public void onFailure(Call<SearchProductRes> call, Throwable t) {
                            AppUtilits.destroyDialog(progressbar);
                            Log.e(TAG, "fail new prod " + t.toString());

                        }
                    });

                }

            }



            public void getChickenProdRes() {

                if (!NetworkUtility.isNetworkConnected(MainActivity.this)) {
                    AppUtilits.displayMessage(MainActivity.this, getString(R.string.network_not_connected));

                } else {
                    ServiceWrapper service = new ServiceWrapper(null);
                    Call<ChickenProductRes> call = service.getChickenProductRes("1234");
                    call.enqueue(new Callback<ChickenProductRes>() {
                        @Override
                        public void onResponse(Call<ChickenProductRes> call, Response<ChickenProductRes> response) {
                            Log.e(TAG, " response is " + response.body().getInformation().toString());
                            if (response.body() != null && response.isSuccessful()) {
                                if (response.body().getStatus() == 1) {
                                    if (response.body().getInformation().size() > 0) {

                                        NewProdModelList.clear();
                                        for (int i = 0; i < response.body().getInformation().size(); i++) {

                                            NewProdModelList.add(new NewProd_Model(response.body().getInformation().get(i).getId(), response.body().getInformation().get(i).getName(),
                                                    response.body().getInformation().get(i).getImgUrl(),response.body().getInformation().get(i).getPrice(),response.body().getInformation().get(i).getStock()));

                                        }

                                        chickenProd_adapter.notifyDataSetChanged();
                                    }

                                } else {
                                    Log.e(TAG, "failed to get rnew prod " + response.body().getMsg());
                                    // AppUtilits.displayMessage(HomeActivity.this,  response.body().getMsg());
                                }
                            } else {
                                // AppUtilits.displayMessage(HomeActivity.this,  getString(R.string.failed_request));

                                //  getNewProdRes();
                            }
                        }

                        @Override
                        public void onFailure(Call<ChickenProductRes> call, Throwable t) {
                            Log.e(TAG, "fail new prod " + t.toString());

                        }
                    });

                }

            }






            public int GetScreenWidth(){
                int  width =100;

                DisplayMetrics  displayMetrics = new DisplayMetrics();
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
                    AppUtilits.UpdateCartCount(MainActivity.this, mainmenu);
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

                                    getsearchresult(searchEditText.getText().toString());
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
                    AppUtilits.UpdateCartCount(MainActivity.this, mainmenu);
                    Log.e(TAG , "  on resume mehtod " );
                }
            }

            public void getUserCartDetails(){

                if (!NetworkUtility.isNetworkConnected(MainActivity.this)){
                    AppUtilits.displayMessage(MainActivity.this,  getString(R.string.network_not_connected));

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
                                    AppUtilits.UpdateCartCount(MainActivity.this, mainmenu);

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


                android.support.v7.app.AlertDialog.Builder alertDialog = new android.support.v7.app.AlertDialog.Builder(MainActivity.this);
                alertDialog.setTitle("Log Out Confirmation!");
                alertDialog.setIcon(R.drawable.chick);
                alertDialog.setMessage("By going back, you log out,\n\n" +
                        "You cannot make any changes upon submitting\n\n" +
                        "Would you like to log out?\n\n");
                alertDialog.setNeutralButton("No, Not Yet", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {



                    }
                }).setPositiveButton("Yes,Log Out", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        SharedPreferenceActivity.getInstance().clear();
                        Intent intent1 = new Intent(MainActivity.this, SignInActivity.class);
                        intent1.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);// this will clear all the stack
                        startActivity(intent1);


                    }

                }).show();






            }

        }
