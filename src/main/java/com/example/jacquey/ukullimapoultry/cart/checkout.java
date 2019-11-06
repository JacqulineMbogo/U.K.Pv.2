package com.example.jacquey.ukullimapoultry.cart;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.jacquey.ukullimapoultry.R;

public class checkout extends AppCompatActivity {


    private String TAG =" checkout";
    private TextView mpesabutton;
    private ImageView cod;



    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checkout );

        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mpesabutton= findViewById(R.id.mpesabutton);
        cod= findViewById(R.id.cod);

        mpesabutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(checkout.this,PlaceOrderActivity.class);
                startActivity(intent);

            }
        });





    }

}
