package com.example.jacquey.ukullimapoultry.cart;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.res.ConfigurationHelper;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.jacquey.ukullimapoultry.MainActivity;
import com.example.jacquey.ukullimapoultry.R;
import com.example.jacquey.ukullimapoultry.Utility.AppUtilits;
import com.example.jacquey.ukullimapoultry.Utility.Constant;
import com.example.jacquey.ukullimapoultry.Utility.DataValidation;
import com.example.jacquey.ukullimapoultry.Utility.NetworkUtility;
import com.example.jacquey.ukullimapoultry.Utility.SharedPreferenceActivity;
import com.example.jacquey.ukullimapoultry.WebServices.ServiceWrapper;
import com.example.jacquey.ukullimapoultry.beanResponse.AddNewAddress;
import com.example.jacquey.ukullimapoultry.myaccount.FeedbackHistory;
import com.google.gson.Gson;

import java.util.Calendar;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OrderAddress_AddNew extends AppCompatActivity {

    private String TAG = "orderaddress_addnew";
    private CheckBox checkbill, checkship;
    private EditText  address1, address2, city,state,pincode;
    private TextView savecontinue;
    private EditText fullname,phone;
    private Spinner spinner;
    private String pin,selectedItemText,date;
    DatePickerDialog picker;



    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_editaddress);

        fullname = findViewById(R.id.fullname);
        phone=findViewById(R.id.phone);
        address1 = (EditText) findViewById(R.id.address1);
        address2 = (EditText) findViewById(R.id.address2);
        Spinner spinner=(Spinner)findViewById(R.id.city);

        address1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final Calendar calendar = Calendar.getInstance();
                int yy = calendar.get(Calendar.YEAR);
                int mm = calendar.get(Calendar.MONTH);
                int dd = calendar.get(Calendar.DAY_OF_MONTH);
                DatePickerDialog datePicker = new DatePickerDialog(OrderAddress_AddNew.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        //calendar.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
                        String date = String.valueOf(dayOfMonth) + "/" + String.valueOf(monthOfYear+1)
                                + "/" + String.valueOf(year);
                        address1.setText(date);
                        Log.d("xyzy",String.valueOf(address1.getText().toString().trim()));
                    }
                }, yy, mm, dd);
                datePicker.show();
            }
        });

        String[] counties=getResources().getStringArray(R.array.county_arrays);

        ArrayAdapter<String> adapter=new ArrayAdapter<String>(this,R.layout.spinner,R.id.text, counties);

        spinner.setAdapter(adapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                 selectedItemText = (String) parent.getItemAtPosition(position);
                // Notify the selected item text


        if(selectedItemText.equals("Mombasa")){

            pin="1000";
        }else  if(selectedItemText.equals("Nairobi")){

            pin="300";
        }else  if(selectedItemText.equals("Kisumu")){

            pin="800";
        }else  if(selectedItemText.equals("Nyeri")){

            pin="150";
        }else  if(selectedItemText.equals("Meru")){

            pin="500";
        }else  if(selectedItemText.equals("Kiambu")){

            pin="300";
        }else  if(selectedItemText.equals("Embu")){

            pin="100";
        }else  if(selectedItemText.equals("Migori")){

            pin="1500";
        }

                Toast.makeText
                        (getApplicationContext(), "transport cost for : " + selectedItemText +"is "  + pin, Toast.LENGTH_SHORT)
                        .show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        String selectedItem = spinner.getSelectedItem().toString();
        state = (EditText) findViewById(R.id.state);
        pincode = (EditText) findViewById(R.id.pincode);

        fullname.setText(SharedPreferenceActivity.getInstance().getString(Constant.USER_name));
        phone.setText(SharedPreferenceActivity.getInstance().getString(Constant.USER_phone));


        savecontinue = (TextView) findViewById(R.id.savecontinue);


        savecontinue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (DataValidation.isNotValidFullName(  fullname.getText().toString())){
                    AppUtilits.displayMessage(OrderAddress_AddNew.this, getString(R.string.full_name) + " "+ getString(R.string.is_invalid));

                } else if (TextUtils.isEmpty(address1.getText().toString().trim())){

                    AppUtilits.displayMessage(OrderAddress_AddNew.this, "Delivery  date" + " "+ getString(R.string.is_invalid));
                }else if ( DataValidation.isValidPhoneNumber(phone.getText().toString().trim())){
                    AppUtilits.displayMessage(OrderAddress_AddNew.this, getString(R.string.phone_no) + " "+ getString(R.string.is_invalid));

                }else {


                    addnewAddressAPI();

                }


                }



        });


    }

    public void addnewAddressAPI(){
        final android.app.AlertDialog progressbar =AppUtilits.createProgressBar(this);
        if (!NetworkUtility.isNetworkConnected(OrderAddress_AddNew.this)){
            AppUtilits.displayMessage(OrderAddress_AddNew.this,  getString(R.string.network_not_connected));

        }else {
            ServiceWrapper serviceWrapper = new ServiceWrapper(null);
            Call<AddNewAddress> call = serviceWrapper.addNewAddressCall("1234", SharedPreferenceActivity.getInstance().getString(Constant.USER_DATA),
                    SharedPreferenceActivity.getInstance().getString(Constant.USER_name),pin,address1.getText().toString().trim(), "", String.valueOf(selectedItemText),
                    "", SharedPreferenceActivity.getInstance().getString(Constant.USER_phone));

            call.enqueue(new Callback<AddNewAddress>() {
                @Override
                public void onResponse(Call<AddNewAddress> call, Response<AddNewAddress> response) {
                    Log.e(TAG, "response is "+ response.body() + "  ---- "+ new Gson().toJson(response.body()));
                    Log.d("xyzz",String.valueOf(address1.getText().toString().trim()));
                    Log.d("xyy",String.valueOf(pin));
                    if (response.body() != null && response.isSuccessful()) {
                        //    Log.e(TAG, "  ss sixe 2 ");
                        if (response.body().getStatus() == 1) {

                            AppUtilits.destroyDialog(progressbar);
                               Log.e(TAG, String.valueOf(address1));

                            AlertDialog.Builder builder = new AlertDialog.Builder(OrderAddress_AddNew.this);
                            LayoutInflater inflater = LayoutInflater.from(OrderAddress_AddNew.this);
                            View g=    inflater.inflate(R.layout.display_message_popup, null);
                            TextView txtview = (TextView) g.findViewById(R.id.txt_msg);
                            TextView btn_ok = (TextView) g.findViewById(R.id.btn_ok);

                            txtview.setText(response.body().getMsg());
                            builder.setView(g);
                            final AlertDialog alert = builder.create();
                            alert.setCancelable(false);
                            alert.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

                            btn_ok.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    alert.dismiss();
                                    finish();
                                }
                            });

                            alert.show();

                        }else {
                            AppUtilits.destroyDialog(progressbar);
                            AppUtilits.displayMessage(OrderAddress_AddNew.this, response.body().getMsg() );
                        }
                    }else {
                        AppUtilits.destroyDialog(progressbar);
                        AppUtilits.displayMessage(OrderAddress_AddNew.this, getString(R.string.network_error));
                    }
                }

                @Override
                public void onFailure(Call<AddNewAddress> call, Throwable t) {
                    AppUtilits.destroyDialog(progressbar);
                    Log.e(TAG, "  fail- add new address "+ t.toString());
                    AppUtilits.displayMessage(OrderAddress_AddNew.this, getString(R.string.fail_toaddaddress));

                }
            });




        }


    }

    @Override
    public void onBackPressed() {


        Intent intent1 = new Intent(OrderAddress_AddNew.this, OrderAddress.class);

        startActivity(intent1);



    }



}