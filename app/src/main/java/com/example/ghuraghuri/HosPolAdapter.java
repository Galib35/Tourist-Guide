package com.example.ghuraghuri;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

public class HosPolAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    Context context;

    public HosPolAdapter(Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(context).inflate(R.layout.item_design_hos_pol,parent,false);
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        String name,phn,url;
        name=Constant.spot_name.get(position);
        phn=Constant.phone.get(position);
        url=Constant.url.get(position);

        ((MyViewHolder)holder).name.setText(name);
        ((MyViewHolder)holder).phone.setText(phn);
        //((MyViewHolder)holder).go.setText(phn);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                context.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return Constant.spot_name.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{

        TextView name,phone;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            name=itemView.findViewById(R.id.type);
            phone=itemView.findViewById(R.id.phn);

        }
    }
}
