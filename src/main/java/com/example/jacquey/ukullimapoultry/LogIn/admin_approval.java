package com.example.jacquey.ukullimapoultry.LogIn;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;

import com.example.jacquey.ukullimapoultry.MainActivity;
import com.example.jacquey.ukullimapoultry.R;
import com.example.jacquey.ukullimapoultry.beanResponse.approval;

import retrofit2.Call;
import retrofit2.Response;

public class admin_approval extends AppCompatActivity {
    private String TAG = " admin_approval";

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_approval);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setHomeButtonEnabled(true);

        final Intent intent = getIntent();
    }

    public void onResponse(Call<approval> call, Response<approval> response) {
        Log.e(TAG, "reposne is " + response.body().getInformation());
        if (response.body() != null && response.isSuccessful()) {
            if (response.body().getStatus() == 1) {

                Intent intent = new Intent(this, MainActivity.class);
                startActivity(intent);


            }


        }


    }
}




