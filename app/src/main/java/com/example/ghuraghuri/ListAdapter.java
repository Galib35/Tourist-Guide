package com.example.ghuraghuri;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class ListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    Context context;

    public ListAdapter(Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v;
        if(Constant.point=='s')
        {
            v=LayoutInflater.from(context).inflate(R.layout.special_layout,parent,false);
        }
        else
        {
            v= LayoutInflater.from(context).inflate(R.layout.list_layout,parent,false);
        }
        return new MyViewHolder(v);
    }


    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        String name,serial,des,cat;
        name=Constant.spot_name.get(position);
        //des=Constant.spot_description.get(position);
        serial=String.valueOf(position+1)+"/";



        if(Constant.point!='s')
        {
            ((MyViewHolder)holder).name.setText(name);
            ((MyViewHolder)holder).num.setText(serial);
        }
        else
        {
            cat=Constant.spot_name.get(position);
            des=Constant.spot_description.get(position);
            ((MyViewHolder)holder).category.setText(cat);
            ((MyViewHolder)holder).description.setText(des);
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(Constant.point=='a')
                {
                    String pos=String.valueOf(position);
                    Intent intent=new Intent(context,AttractionsDisplay.class);
                    intent.putExtra("index",pos);
                    context.startActivity(intent);
                }
                else if(Constant.point=='h')
                {
                    String url=Constant.url.get(position);
                    //Intent intent=new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                    Intent intent=new Intent(context,WebViewDisplay.class);
                    intent.putExtra("url",url);
                    context.startActivity(intent);
                }
                else if(Constant.point=='r' || Constant.point=='l' || Constant.point=='b')
                {

                    //String geoUri = "http://maps.google.com/maps?q=loc:" + latitude + "," + longitude;
                    String url=Constant.url.get(position);
                    Intent intent=new Intent(context,WebViewDisplay.class);
                    intent.putExtra("url",url);
                    //Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                    context.startActivity(intent);

                }
                else if(Constant.point=='m')
                {
                    String url=Constant.url.get(position);
                    Intent intent=new Intent(context, WebViewDisplay.class);
                    intent.putExtra("url",url);
                    context.startActivity(intent);
                }

            }
        });

    }

    @Override
    public int getItemCount() {
        return Constant.spot_name.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{

        TextView name,num,category,description;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            if(Constant.point=='s')
            {
                category=itemView.findViewById(R.id.spec_cat);
                description=itemView.findViewById(R.id.spec_des);
            }
            else
            {
                name=itemView.findViewById(R.id.atr_name);
                num=itemView.findViewById(R.id.atr_num);
            }



        }
    }
}
