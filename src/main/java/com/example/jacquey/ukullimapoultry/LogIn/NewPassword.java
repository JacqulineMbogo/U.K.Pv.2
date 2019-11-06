package com.example.jacquey.ukullimapoultry.LogIn;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.example.jacquey.ukullimapoultry.MainActivity;
import com.example.jacquey.ukullimapoultry.R;
import com.example.jacquey.ukullimapoultry.Utility.AppUtilits;
import com.example.jacquey.ukullimapoultry.Utility.DataValidation;
import com.example.jacquey.ukullimapoultry.Utility.NetworkUtility;
import com.example.jacquey.ukullimapoultry.WebServices.ServiceWrapper;
import com.example.jacquey.ukullimapoultry.beanResponse.NewPasswordRes;


import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NewPassword  extends AppCompatActivity {
    private String TAG ="New_Password", userid;
    private EditText password, retype_password;
    private TextView submit;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_password);

        password = (EditText) findViewById(R.id.password);
        retype_password = (EditText) findViewById(R.id.retype_password);
        submit = (TextView) findViewById(R.id.submit);

        Intent intent = getIntent();

        if (intent!=null && !intent.getExtras().getString("userid").equals(null) ){

            userid = intent.getExtras().getString("userid");

        }else {
            userid = "";
        }

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (DataValidation.isNotValidPassword(password.getText().toString())){
                    AppUtilits.displayMessage(NewPassword.this, getString(R.string.password) + " "+ getString(R.string.is_invalid));

                }else if (DataValidation.isNotValidPassword(retype_password.getText().toString())){
                    AppUtilits.displayMessage(NewPassword.this, getString(R.string.retype_password) + " "+ getString(R.string.is_invalid));

                }else if (!password.getText().toString().equals(retype_password.getText().toString())){
                    AppUtilits.displayMessage(NewPassword.this,  getString(R.string.password_not_match));

                }else {
                    // network connection and progroess dialog
                    /// here call retrofit method

                    sendNewPasswordReq();
                }



            }
        });

    }


    public void sendNewPasswordReq(){


        if (!NetworkUtility.isNetworkConnected(NewPassword.this)){
            AppUtilits.displayMessage(NewPassword.this,  getString(R.string.network_not_connected));

        }else {

            ServiceWrapper serviceWrapper = new ServiceWrapper(null);
            Call<NewPasswordRes>  call =   serviceWrapper.UserNewPassword(userid, password.getText().toString() );
            call.enqueue(new Callback<NewPasswordRes>() {
                @Override
                public void onResponse(Call<NewPasswordRes> call, Response<NewPasswordRes> response) {

                    Log.d(TAG, "reponse : "+ response.toString());
                    if (response.body()!= null && response.isSuccessful()){
                        if (response.body().getStatus() ==1){

                            // response.body().getInformation().getOtp()
                            // start home activity
                            Intent intent = new Intent(NewPassword.this, MainActivity.class);
                            startActivity(intent);


                        }else {
                            AppUtilits.displayMessage(NewPassword.this,  response.body().getMsg());
                        }
                    }else {
                        AppUtilits.displayMessage(NewPassword.this,  getString(R.string.failed_request));

                    }
                }

                @Override
                public void onFailure(Call<NewPasswordRes> call, Throwable t) {
                    Log.e(TAG, " failure "+ t.toString());
                    AppUtilits.displayMessage(NewPassword.this,  getString(R.string.failed_request));
                }
            });




        }




    }




}