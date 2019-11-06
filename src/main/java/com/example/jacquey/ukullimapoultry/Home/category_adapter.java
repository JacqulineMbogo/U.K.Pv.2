package com.example.jacquey.ukullimapoultry.Home;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.jacquey.ukullimapoultry.R;

import java.util.List;

import static android.support.constraint.Constraints.TAG;

public class category_adapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

private Context mContext;
private List<category_model> mNewcatModelList;
private String TAG ="Newcat_adapter";
private int mScrenwith;

public category_adapter(Context context, List<category_model> list, int screenwidth ){
        mContext = context;
        mNewcatModelList = list;
        mScrenwith =screenwidth;

        }

private class NewcatHolder extends RecyclerView.ViewHolder {
    ImageView prod_img;
    TextView prod_name;
    CardView cardView;

    public NewcatHolder(View itemView) {
        super(itemView);
        prod_img = (ImageView) itemView.findViewById(R.id.prod_img);
        prod_name = (TextView) itemView.findViewById(R.id.prod_name);

        cardView = (CardView) itemView.findViewById(R.id.card_view);

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams( mScrenwith - (mScrenwith/100*75), LinearLayout.LayoutParams.WRAP_CONTENT);
        params.setMargins(15,10,15,10);
        cardView.setLayoutParams(params);
        cardView.setPadding(5,5,5,5);

    }
}

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_newcat, parent,false);
        Log.e(TAG, "  view created ");
        return new category_adapter.NewcatHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
       final category_model model = mNewcatModelList.get(position);
        Log.e(TAG, " assign value ");
        ((category_adapter.NewcatHolder) holder).prod_name.setText(model.getProd_name());



        Glide.with(mContext)
                .load(model.getImg_ulr())
                .into(((category_adapter.NewcatHolder) holder).prod_img);
        // imageview glider lib to get imagge from url

        ((category_adapter.NewcatHolder) holder).cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(mContext, com.example.jacquey.ukullimapoultry.productpreview.ProductDetails.class);
                intent.putExtra("prod_id", model.getProd_id());

                mContext.startActivity(intent);

            }
        });

    }

    @Override
    public int getItemCount() {
        return mNewcatModelList.size();
    }
}
