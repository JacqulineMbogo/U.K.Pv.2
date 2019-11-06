package com.example.jacquey.ukullimapoultry.SplashScreen;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;

import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.example.jacquey.ukullimapoultry.LogIn.SignInActivity;
import com.example.jacquey.ukullimapoultry.LogIn.SignUpActivity;
import com.example.jacquey.ukullimapoultry.MainActivity;
import com.example.jacquey.ukullimapoultry.R;
import com.example.jacquey.ukullimapoultry.Utility.Constant;
import com.example.jacquey.ukullimapoultry.Utility.SharedPreferenceActivity;
import com.example.jacquey.ukullimapoultry.cart.OrderAddress;
import com.example.jacquey.ukullimapoultry.cart.OrderAddress_AddNew;


public class SplashActivity extends AppCompatActivity {
    private String TAG ="splashAcctivity";

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_layout);
        init();
        Log.e(TAG, " splash start showing");
    }
    public void init(){

        new Handler().postDelayed( new Runnable() {
            @Override
            public void run() {
                /// if user registered user
                // then show home screen
                // else  show login screen
                // key  register_user
                Log.e(TAG, "  counter running ");
                if (SharedPreferenceActivity.getInstance().getString(Constant.USER_DATA).equalsIgnoreCase("")){
                    // not registted user  so show login screen
                    Intent intent = new Intent(SplashActivity.this, SignUpActivity.class);
                    startActivity(intent);
                }else {
                    // home sscreen
                    Intent intent = new Intent(SplashActivity.this, SignInActivity.class);
                    startActivity(intent);                finish();

                }

            }
        }, 5000 );

    }
}
