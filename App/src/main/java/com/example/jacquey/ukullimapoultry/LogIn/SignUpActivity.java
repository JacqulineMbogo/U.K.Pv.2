package com.example.jacquey.ukullimapoultry.LogIn;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;

import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
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
import com.example.jacquey.ukullimapoultry.beanResponse.NewUserRegistration;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SignUpActivity  extends AppCompatActivity {
    private String TAG ="SignupActivity";

    EditText fullname, phone_no, email, password, retype_password,fname, lname;
    TextView create_acc, signin;
    RelativeLayout R1, R2;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        fullname = (EditText) findViewById(R.id.fullname);
        fname = (EditText) findViewById(R.id.f_name);
        lname= (EditText) findViewById(R.id.l_name);
        phone_no = (EditText) findViewById(R.id.phone_number);
        email = (EditText) findViewById(R.id.username);
        password = (EditText) findViewById(R.id.password);
        retype_password = (EditText) findViewById(R.id.retype_password);
        signin = (TextView) findViewById(R.id.sign_in);

        R1 = findViewById(R.id.r1);
        R2 = findViewById(R.id.r2);

        signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SignUpActivity.this, SignInActivity.class);
                startActivity(intent);
            }
        });

        create_acc = (TextView) findViewById(R.id.create_account);


        create_acc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (DataValidation.isNotValidFName(fullname.getText().toString())){
                    /// show error pupup
                    Toast.makeText(getApplicationContext(),"Invalid name", Toast.LENGTH_LONG).show();
                }else if (DataValidation.isNotValidLName(fullname.getText().toString())){
                    /// show error pupup
                    Toast.makeText(getApplicationContext(),"Invalid name", Toast.LENGTH_LONG).show();
                }else
                if (DataValidation.isNotValidFullName(fullname.getText().toString())){
                    /// show error pupup
                    Toast.makeText(getApplicationContext(),"Invalid name", Toast.LENGTH_LONG).show();
                }else if ( DataValidation.isValidPhoneNumber(phone_no.getText().toString())){
                    Toast.makeText(getApplicationContext(),"Invalid phone number.",Toast.LENGTH_LONG).show();

                }else if (DataValidation.isNotValidemail(email.getText().toString())){
                    Toast.makeText(getApplicationContext(),"Invalid email",Toast.LENGTH_LONG).show();

                }else if (DataValidation.isNotValidPassword(password.getText().toString())){
                    Toast.makeText(getApplicationContext(),"Password should be at least 6 characters ",Toast.LENGTH_LONG).show();

                }else if (!password.getText().toString().equals(retype_password.getText().toString())){
                    Toast.makeText(getApplicationContext(),"passwords do not match",Toast.LENGTH_LONG).show();


                }else {
                    // network connection and progroess dialog
                    /// here call retrofit method

                    sendNewRegistrationReq();
                }

            }
        });

    }

    public void sendNewRegistrationReq(){

        final AlertDialog progressbar =AppUtilits.createProgressBar(this);

        if (!NetworkUtility.isNetworkConnected(SignUpActivity.this)){
            Toast.makeText(getApplicationContext(),"Network error",Toast.LENGTH_LONG).show();
            AppUtilits.destroyDialog(progressbar);


        }else {

            ServiceWrapper serviceWrapper = new ServiceWrapper(null);
            Call<NewUserRegistration> callNewREgistration=   serviceWrapper.newUserRegistrationCall( fname.getText().toString(), lname.getText().toString(),fullname.getText().toString(),
                    email.getText().toString(), phone_no.getText().toString(),
                    "username", password.getText().toString() );
            callNewREgistration.enqueue(new Callback<NewUserRegistration>() {
                @Override
                public void onResponse(Call<NewUserRegistration> call, Response<NewUserRegistration> response) {
                    Log.d(TAG, "reponse : "+ response.toString());
                    if (response.body()!= null && response.isSuccessful()){
                        if (response.body().getStatus() ==1){
                            AppUtilits.destroyDialog(progressbar);
                            // store userdata to share prerference
                            SharedPreferenceActivity.getInstance().saveString(Constant.USER_DATA, response.body().getInformation().getUserId());
                            SharedPreferenceActivity.getInstance().saveString(Constant.USER_name, response.body().getInformation().getFullname());
                            SharedPreferenceActivity.getInstance().saveString(Constant.USER_email, response.body().getInformation().getEmail());
                            SharedPreferenceActivity.getInstance().saveString(Constant.USER_phone, response.body().getInformation().getPhone());

                            // start home activity
                            //AppUtilits.displayMessage(SignUpActivity.this,  response.body().getMsg());

                           // R1.setVisibility(View.GONE);
                           // R2.setVisibility(View.VISIBLE);
                            AppUtilits.createToaster(SignUpActivity.this, "Welcome, "+SharedPreferenceActivity.getInstance().getString(Constant.USER_name)+"\n Please continue to sign in upon admin approval",Toast.LENGTH_LONG);
                            Intent intent = new Intent(SignUpActivity.this, SignInActivity.class);
                            //intent.putExtra("userid", "sdfsd");
                            startActivity(intent);
                            finish();

                        }else {
                            AppUtilits.destroyDialog(progressbar);
                            AppUtilits.displayMessage(SignUpActivity.this,  response.body().getMsg());
                        }
                    }else {
                        AppUtilits.destroyDialog(progressbar);
                        Toast.makeText(getApplicationContext(),"Request failed",Toast.LENGTH_LONG).show();

                    }
                }

                @Override
                public void onFailure(Call<NewUserRegistration> call, Throwable t) {
                    AppUtilits.destroyDialog(progressbar);
                    Log.e(TAG, " failure "+ t.toString());
                    Toast.makeText(getApplicationContext(),"Request failed",Toast.LENGTH_LONG).show();


                }
            });
        }


    }

}
