package com.example.ghuraghuri;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.smarteist.autoimageslider.SliderViewAdapter;

import java.util.Objects;

public class ProductImageAdapter extends SliderViewAdapter<ProductImageAdapter.ViewHolderVH> {

    Context context;

    public ProductImageAdapter(Context context) {
        this.context = context;
    }

    @Override
    public ViewHolderVH onCreateViewHolder(ViewGroup parent) {
        View v= LayoutInflater.from(context).inflate(R.layout.testing,parent,false);
        return new ViewHolderVH(v);
    }

    @Override
    public void onBindViewHolder(ViewHolderVH viewHolder, int position) {
        String url= Constant.productImg.get(position);
        //System.out.println(url);
        Glide.with(viewHolder.itemView).load(url).into(viewHolder.img);
    }

    @Override
    public int getCount() {
        return Constant.productImg.size();
    }

    public static class ViewHolderVH extends ProductImageAdapter.ViewHolder{
        ImageView img;

        public ViewHolderVH(View itemView) {
            super(itemView);
            img=itemView.findViewById(R.id.slider_img);
        }
    }
}
