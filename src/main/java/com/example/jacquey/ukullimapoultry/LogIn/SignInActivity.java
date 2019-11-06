package com.example.jacquey.ukullimapoultry.LogIn;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;

import android.widget.ProgressBar;
import android.widget.RelativeLayout;
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
import com.example.jacquey.ukullimapoultry.admin.delivery_home;
import com.example.jacquey.ukullimapoultry.beanResponse.StaffSignInRes;
import com.example.jacquey.ukullimapoultry.beanResponse.UserSignInRes;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class SignInActivity extends AppCompatActivity {
    private String TAG = "SigninActivity";
    private TextView skip, forgot_password, login;
    private EditText phone_no, password;
    private LinearLayout signup_here;
    ProgressBar progressBar;
    RelativeLayout R2,pb;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signin);
        Log.e(TAG,"  start siggnin Activity" );

        skip =(TextView) findViewById(R.id.btn_skip);
        login = (TextView) findViewById(R.id.login);
        forgot_password = (TextView) findViewById(R.id.forgot_password);
        signup_here = (LinearLayout) findViewById(R.id.signup_here);
        progressBar = findViewById(R.id.progressBar);
        pb = findViewById(R.id.pb1);

        phone_no = (EditText) findViewById(R.id.phone_number);
        password = (EditText) findViewById(R.id.password);
        R2 = findViewById(R.id.r2);

        skip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SignInActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });

        forgot_password.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                if ( DataValidation.isNotValidFullName(phone_no.getText().toString())){
                    Toast.makeText(getApplicationContext(),"Invalid phone number",Toast.LENGTH_LONG).show();

                }else if (DataValidation.isNotValidPassword(password.getText().toString())){
                    Toast.makeText(getApplicationContext(),"Invalid password",Toast.LENGTH_LONG).show();

                }else {

                    sendstaffLoginData();

                }

            }
        });



        signup_here.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SignInActivity.this, SignUpActivity.class);
                startActivity(intent);
            }
        });

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if ( DataValidation.isNotValidFullName(phone_no.getText().toString())){
                    Toast.makeText(getApplicationContext(),"Invalid phone number",Toast.LENGTH_LONG).show();

                }else if (DataValidation.isNotValidPassword(password.getText().toString())){
                    Toast.makeText(getApplicationContext(),"Invalid password",Toast.LENGTH_LONG).show();

                }else {

                    sendUserLoginData();

                }
            }
        });

    }

    public void sendUserLoginData(){

        final AlertDialog progressbar =AppUtilits.createProgressBar(this);


        if (!NetworkUtility.isNetworkConnected(SignInActivity.this)){
            Toast.makeText(getApplicationContext(),"Network error",Toast.LENGTH_LONG).show();
            AppUtilits.destroyDialog(progressbar);

        }else {

            ServiceWrapper serviceWrapper = new ServiceWrapper(null);
            Call<UserSignInRes> userSigninCall = serviceWrapper.UserSigninCall(phone_no.getText().toString(), password.getText().toString());
            userSigninCall.enqueue(new Callback<UserSignInRes>() {
                @Override
                public void onResponse(Call<UserSignInRes> call, Response<UserSignInRes> response) {

                    Log.d(TAG, "reponse : "+ response.toString());
                    if (response.body()!= null && response.isSuccessful()){
                        if (response.body().getStatus() ==1){
                            Log.e(TAG, "  user data "+  response.body().getInformation());
                            SharedPreferenceActivity.getInstance().saveString(Constant.USER_DATA, response.body().getInformation().getUserId());
                            SharedPreferenceActivity.getInstance().saveString(Constant.USER_name, response.body().getInformation().getFullname());
                            SharedPreferenceActivity.getInstance().saveString(Constant.USER_email, response.body().getInformation().getEmail());
                            SharedPreferenceActivity.getInstance().saveString(Constant.USER_phone, response.body().getInformation().getPhone());


                           AppUtilits.destroyDialog(progressbar);
                            // start home activity
                            Intent intent = new Intent(SignInActivity.this, MainActivity.class);
                            //intent.putExtra("userid", "sdfsd");
                            startActivity(intent);
                            finish();





                        }else  if (response.body().getStatus() ==0){
                            AppUtilits.displayMessage(SignInActivity.this,  response.body().getMsg());
                            AppUtilits.destroyDialog(progressbar);
                        }
                    }else {
                        AppUtilits.displayMessage(SignInActivity.this,  response.body().getMsg());
                        Toast.makeText(getApplicationContext(),"Request failed",Toast.LENGTH_LONG).show();
                        AppUtilits.destroyDialog(progressbar);

                    }
                }

                @Override
                public void onFailure(Call<UserSignInRes> call, Throwable t) {
                    Log.e(TAG, " failure "+ t.toString());
                    Toast.makeText(getApplicationContext(),"Request failed",Toast.LENGTH_LONG).show();
                    AppUtilits.destroyDialog(progressbar);

                }
            });




        }








    }
    public void sendstaffLoginData(){

        final AlertDialog progressbar =AppUtilits.createProgressBar(this);


        if (!NetworkUtility.isNetworkConnected(SignInActivity.this)){
            Toast.makeText(getApplicationContext(),"Network error",Toast.LENGTH_LONG).show();
            AppUtilits.destroyDialog(progressbar);

        }else {

            ServiceWrapper serviceWrapper = new ServiceWrapper(null);
            Call<StaffSignInRes> staffSigninCall = serviceWrapper.StaffSigninCall(phone_no.getText().toString(), password.getText().toString());
            staffSigninCall.enqueue(new Callback<StaffSignInRes>() {
                @Override
                public void onResponse(Call<StaffSignInRes> call, Response<StaffSignInRes> response) {

                    Log.d(TAG, "reponse : "+ response.toString());
                    if (response.body()!= null && response.isSuccessful()){
                        if (response.body().getStatus() ==1){
                            Log.e(TAG, "  user data "+  response.body().getInformation());
                            SharedPreferenceActivity.getInstance().saveString(Constant.USER_staffDATA, response.body().getInformation().getUserId());
                            SharedPreferenceActivity.getInstance().saveString(Constant.USER_staffname, response.body().getInformation().getFullname());
                            //SharedPreferenceActivity.getInstance().saveString(Constant.USER_email, response.body().getInformation().getEmail());
                            SharedPreferenceActivity.getInstance().saveString(Constant.USER_staffphone, response.body().getInformation().getPhone());


                            Log.d("staffid", Constant.USER_staffDATA);
                            AppUtilits.destroyDialog(progressbar);
                            // start home activity
                            Intent intent = new Intent(SignInActivity.this, delivery_home.class);
                            //intent.putExtra("userid", "sdfsd");
                            startActivity(intent);
                            finish();





                        }else {
                            AppUtilits.displayMessage(SignInActivity.this,  response.body().getMsg());
                            AppUtilits.destroyDialog(progressbar);
                        }
                    }else {
                        Toast.makeText(getApplicationContext(),"Request failed",Toast.LENGTH_LONG).show();
                        AppUtilits.destroyDialog(progressbar);

                    }
                }

                @Override
                public void onFailure(Call<StaffSignInRes> call, Throwable t) {
                    Log.e(TAG, " failure "+ t.toString());
                    Toast.makeText(getApplicationContext(),"Request failed",Toast.LENGTH_LONG).show();
                    AppUtilits.destroyDialog(progressbar);

                }
            });




        }








    }





}




