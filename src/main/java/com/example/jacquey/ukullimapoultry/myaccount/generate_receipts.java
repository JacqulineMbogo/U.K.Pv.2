package com.example.jacquey.ukullimapoultry.myaccount;


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.pdf.PdfDocument;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
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
import com.example.jacquey.ukullimapoultry.beanResponse.GetOrderProductDetails;
import com.example.jacquey.ukullimapoultry.beanResponse.clearbalanceAPI;
import com.example.jacquey.ukullimapoultry.beanResponse.payAPI;
import com.example.jacquey.ukullimapoultry.beanResponse.receiveAPI;
import com.example.jacquey.ukullimapoultry.cart.Cartitem_Model;
import com.example.jacquey.ukullimapoultry.cart.OrderSummary_Adapter;
import com.example.jacquey.ukullimapoultry.cart.PlaceOrderActivity;
import com.example.jacquey.ukullimapoultry.cart.payment2;
import com.google.gson.Gson;

import android.os.Environment;
import android.print.PrintAttributes;
import android.print.pdf.PrintedPdfDocument;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class generate_receipts extends AppCompatActivity {
    private String TAG ="orderViewdetails", orderId ="";
    private RecyclerView item_recyclerview;
    private ArrayList<Cartitem_Model> cartitemModels = new ArrayList<>();
    private receipt_adapter orderSummeryAdapter;
    private float totalamount =0;
    private String shippingadress = "";
    private TextView print1,t2,t3,subtotal_value,shipping_value, order_total_value, order_ship_address, order_billing_address,order_total,order_over,amount,pay,receive;
    private RelativeLayout layout2,relativeLayout;
    LinearLayout R1;




    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_orderreceipt_viewdetails);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(true);

        Intent intent = getIntent();
        orderId = intent.getExtras().getString("order_id");
        shippingadress = intent.getExtras().getString("address");

        subtotal_value = (TextView) findViewById(R.id.subtotal_value);
        shipping_value = (TextView) findViewById(R.id.shipping_value);
        order_total_value = (TextView) findViewById(R.id.order_total_value);
        t2 = (TextView) findViewById(R.id.t2);
        t3 = (TextView) findViewById(R.id.t3);
        print1 = (TextView) findViewById(R.id.print1);

        order_total= findViewById(R.id.order_total);
        order_over= findViewById(R.id.order_over);
        //amount=findViewById(R.id.amount);

        toolbar=findViewById(R.id.toolbar);
        R1 = findViewById(R.id.R1);

        item_recyclerview = (RecyclerView) findViewById(R.id.item_recyclerview);
        layout2=findViewById(R.id.layout_2);

        LinearLayoutManager mLayoutManger3 = new LinearLayoutManager( this, LinearLayoutManager.VERTICAL, false);
        item_recyclerview.setLayoutManager(mLayoutManger3);
        item_recyclerview.setItemAnimator(new DefaultItemAnimator());

        orderSummeryAdapter = new receipt_adapter(this, cartitemModels);
        item_recyclerview.setAdapter(orderSummeryAdapter);

        getOrderDetails();

        print1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                writePDFDocument();
                print1.setVisibility(View.GONE);
            }
        });
        //writePDFDocument();



    }

    public void getOrderDetails(){



        if (!NetworkUtility.isNetworkConnected(generate_receipts.this)){
            AppUtilits.displayMessage(generate_receipts.this,  getString(R.string.network_not_connected));

        }else {
            //  Log.e(TAG, "  user value "+ SharePreferenceUtils.getInstance().getString(Constant.USER_DATA));
            ServiceWrapper service = new ServiceWrapper(null);
            Call<GetOrderProductDetails> call = service.getorderproductcall("1234", SharedPreferenceActivity.getInstance().getString(Constant.USER_DATA),
                    orderId  );
            call.enqueue(new Callback<GetOrderProductDetails>() {
                @Override
                public void onResponse(Call<GetOrderProductDetails> call, Response<GetOrderProductDetails> response) {
                    //   Log.e(TAG, "response is " + response.body() + "  ---- " + new Gson().toJson(response.body()));
                    //  Log.e(TAG, "  ss sixe 1 ");
                    if (response.body() != null && response.isSuccessful()) {
                        //    Log.e(TAG, "  ss sixe 2 ");
                        if (response.body().getStatus() == 1) {

                            if (response.body().getInformation().size()>0){
                                subtotal_value.setText(response.body().getSubtotal());
                                shipping_value.setText(response.body().getShippingfee());
                                if(Integer.parseInt(String.valueOf(response.body().getGrandtotal()))>0){
                                    order_total.setVisibility(View.VISIBLE);


                                    order_total_value.setText(response.body().getGrandtotal());
                                   // amount.setText(response.body().getGrandtotal());

                                    SharedPreferenceActivity.getInstance().saveString(Constant.VIEW_TOTAL, response.body().getShippingfee());
                                    SharedPreferenceActivity.getInstance().saveString(Constant.VIEW_BALANCE, response.body().getGrandtotal());
                                    Log.e("SAMPLE", String.valueOf(Constant.VIEW_TOTAL));

                                    Log.e("SAMPLE1", String.valueOf(amount));





                                }


                                order_total_value.setText(response.body().getGrandtotal());

                                t2.setText("Receipt Number:" + orderId);
                                t3.setText(AppUtilits.getCurrentDates());
                                cartitemModels.clear();
                                for (int i=0; i<response.body().getInformation().size(); i++){


                                    cartitemModels.add( new Cartitem_Model(response.body().getInformation().get(i).getProdId(),
                                            response.body().getInformation().get(i).getProdName(), "", "",
                                            response.body().getInformation().get(i).getProdTotal(), response.body().getInformation().get(i).getQty()));

                                }

                                orderSummeryAdapter.notifyDataSetChanged();
                            }


                        } else {
                            AppUtilits.displayMessage(generate_receipts.this, response.body().getMsg());

                        }

                    } else {
                        AppUtilits.displayMessage(generate_receipts.this, getString(R.string.network_error));

                    }
                }

                @Override
                public void onFailure(Call<GetOrderProductDetails> call, Throwable t) {
                    //   Log.e(TAG, "  fail- add to cart item "+ t.toString());
                    AppUtilits.displayMessage(generate_receipts.this, getString(R.string.fail_toorderhistory));

                }
            });








        }

    }


    private void writePDFDocument() {

        final File file = new File(getStorageDir("PDF"), "receipt.pdf");
        int PDF_PAGE_WIDTH = 595, PDF_PAGE_HEIGHT = 842;


        // create a new document
        PdfDocument pdfDocument = new PdfDocument();

        //Get Content View.
        View contentView = R1;

        // crate a page description
        PdfDocument.PageInfo pageInfo = new PdfDocument.PageInfo.
                Builder( PDF_PAGE_WIDTH,PDF_PAGE_HEIGHT, 1).create();

        // start a page
        PdfDocument.Page page = pdfDocument.startPage(pageInfo);

        // draw view on the page
        Canvas pageCanvas = page.getCanvas();

        int pageWidth = pageCanvas.getWidth();
        int pageHeight = pageCanvas.getHeight();
        int measureWidth = View.MeasureSpec.makeMeasureSpec(pageWidth, View.MeasureSpec.EXACTLY);
        int measuredHeight = View.MeasureSpec.makeMeasureSpec(pageHeight, View.MeasureSpec.EXACTLY);
        contentView.measure(measureWidth, measuredHeight);
        contentView.layout(0, 0, pageWidth, pageHeight);
        contentView.draw(pageCanvas);

        // finish the page
        pdfDocument.finishPage(page);

        try {
            pdfDocument.writeTo(new FileOutputStream(file));
        } catch (IOException e) {
            e.printStackTrace();
        }

        // close the document
        pdfDocument.close();
        // Set on UI Thread
        runOnUiThread(new Runnable() {
            @Override
            public void run() {

                AlertDialog.Builder builder = new AlertDialog.Builder(generate_receipts.this);
                builder.setTitle("Success")
                        .setMessage("PDF File Generated Successfully.")
                        //.setIcon(android.R.drawable.ic_dialog_alert)
                        .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                Intent intent = new Intent(Intent.ACTION_VIEW);
                                intent.setDataAndType(Uri.fromFile(file), "application/pdf");
                                intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                                startActivity(intent);
                            }

                        }).show();
            }
        });

    }

    private File getStorageDir(String pdf) {

        // Get the directory for the user's public pictures directory.
        File file = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_DOCUMENTS), "pdf");
        if (!file.mkdirs()) {
            Log.e("", "Directory not created");
        }
        return file;
    }


    @Override
    public void onBackPressed() {


        Intent intent1 = new Intent(generate_receipts.this, MainActivity.class);

        startActivity(intent1);



    }
}

