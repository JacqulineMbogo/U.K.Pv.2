package com.example.jacquey.ukullimapoultry.Home;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.jacquey.ukullimapoultry.R;

import java.util.ArrayList;
import java.util.List;



public class DuckProd_Adapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>   {

    private Context mContext;
    private List<DuckProd_Model> mDuckProdModelList;
    private List<DuckProd_Model> DuckProdlList;
    private String TAG ="NewProd_adapter";
    private int mScrenwith;


    public DuckProd_Adapter(Context context, List<DuckProd_Model> list, int screenwidth ){
        mContext = context;
        mDuckProdModelList = list;
        mScrenwith =screenwidth;

    }

    private class DuckProductHolder extends RecyclerView.ViewHolder {
        ImageView prod_img;
        TextView prod_name,prod_price,prod_stock;
        CardView cardView;

        public DuckProductHolder(View itemView) {
            super(itemView);
            prod_img = (ImageView) itemView.findViewById(R.id.prod_img);
            prod_name = (TextView) itemView.findViewById(R.id.prod_name);
            prod_price = (TextView) itemView.findViewById(R.id.prod_price);

            prod_stock = (TextView) itemView.findViewById(R.id.prod_stock);
            cardView = (CardView) itemView.findViewById(R.id.card_view);

            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams( mScrenwith - (mScrenwith/100*70), LinearLayout.LayoutParams.MATCH_PARENT);
            params.setMargins(10,10,10,10);
            cardView.setLayoutParams(params);
            cardView.setPadding(5,5,5,5);



        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_newproduct, parent,false);
        Log.e(TAG, "  view created ");
        return new DuckProductHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        final DuckProd_Model model = mDuckProdModelList.get(position);
        Log.e(TAG, " assign value ");
        ((DuckProductHolder) holder).prod_name.setText(model.getProd_name());
        ((DuckProductHolder) holder).prod_price.setText("Ksh: "+model.getPrice());
        ((DuckProductHolder) holder).prod_stock.setText("Stock: "+model.getStock());



        Glide.with(mContext)
                .load(model.getImg_ulr())
                .into(((DuckProductHolder) holder).prod_img);
        // imageview glider lib to get imagge from url


        ((DuckProd_Adapter.DuckProductHolder) holder).cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(mContext, com.example.jacquey.ukullimapoultry.productpreview.ProductDetails.class);
                intent.putExtra("prod_id", model.getProd_id());

                mContext.startActivity(intent);
                //  Log.e(TAG, "  prod_id "+String.valueOf(prod_id));



            }
        });





    }




    @Override
    public int getItemCount() {
        return mDuckProdModelList.size();
    }





}
