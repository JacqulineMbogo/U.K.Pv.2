package com.example.jacquey.ukullimapoultry.Home;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.jacquey.ukullimapoultry.R;

import java.util.ArrayList;
import java.util.List;



public class Search_Adapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>   {

    private Context mContext;
    private List<Search_Model> mSearchProdModelList;
    private List<TurkeyProd_Model> TurkeyProdlList;
    private String TAG ="NewProd_adapter";
    private int mScrenwith;

    public Search_Adapter(Context context, List<Search_Model> list, int screenwidth ){
        mContext = context;
        mSearchProdModelList = list;
        mScrenwith =screenwidth;

    }

    private class SearchProductHolder extends RecyclerView.ViewHolder {
        ImageView prod_img;
        TextView prod_name,prod_price,prod_stock;
        CardView cardView;

        public SearchProductHolder(View itemView) {
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
        return new SearchProductHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        final Search_Model model = mSearchProdModelList.get(position);
        Log.e(TAG, " assign value ");
        ((SearchProductHolder) holder).prod_name.setText(model.getProd_name());
        ((SearchProductHolder) holder).prod_price.setText("Ksh: "+model.getPrice());
        (( SearchProductHolder) holder).prod_stock.setText("Stock: "+model.getStock());



        Glide.with(mContext)
                .load(model.getImg_ulr())
                .into(((SearchProductHolder) holder).prod_img);
        // imageview glider lib to get imagge from url


        ((Search_Adapter.SearchProductHolder) holder).cardView.setOnClickListener(new View.OnClickListener() {
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
        return mSearchProdModelList.size();
    }

}
