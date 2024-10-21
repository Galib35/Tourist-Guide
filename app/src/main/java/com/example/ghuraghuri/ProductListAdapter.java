package com.example.ghuraghuri;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

public class ProductListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{
    Context context;

    public ProductListAdapter(Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(context).inflate(R.layout.product_recview,parent,false);
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        Glide.with(context).load(Constant.thumbnail.get(position)).into(((MyViewHolder)holder).image);

        String rate=Constant.productRating.get(position)+"/5";
        String price=Constant.productPrice.get(position)+"tk";
        ((MyViewHolder)holder).name.setText(Constant.productName.get(position));
        ((MyViewHolder)holder).rating.setText(rate);
        ((MyViewHolder)holder).price.setText(price);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(context,ProductInfo.class);
                intent.putExtra("product_id",Constant.productKey.get(position));
                intent.putExtra("product_index",position);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return Constant.productKey.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{

        ImageView image;
        TextView name,rating,price;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            image=itemView.findViewById(R.id.pdct_Img);
            name=itemView.findViewById(R.id.pdct_name_list);
            rating=itemView.findViewById(R.id.pdct_rate_list);
            price=itemView.findViewById(R.id.pdct_price_list);
        }
    }
}
