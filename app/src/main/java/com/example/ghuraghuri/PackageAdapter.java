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

public class PackageAdapter extends RecyclerView.Adapter<PackageAdapter.PackageViewHolder> {

    Context context;

    public PackageAdapter(Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    public PackageAdapter.PackageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(context).inflate(R.layout.package_item,parent,false);
        return new PackageViewHolder(v);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull PackageAdapter.PackageViewHolder holder, @SuppressLint("RecyclerView") int position) {
        Glide.with(context).load(Constant.packageThumbnail.get(position)).into(holder.thumbnail);
        holder.name.setText(Constant.packageName.get(position));
        holder.location.setText(Constant.packageLocation.get(position));
        holder.cost.setText(Constant.packageCost.get(position)+"tk");
        holder.ag_name.setText(Constant.packageAgencyName.get(position));
        holder.duration.setText(Constant.packageDuration.get(position));

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(context,PackageDisplay.class);
                intent.putExtra("ind",position);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return Constant.packageId.size();
    }

    public static class PackageViewHolder extends RecyclerView.ViewHolder{
        ImageView thumbnail;
        TextView name,location,cost,ag_name,duration;
        public PackageViewHolder(@NonNull View itemView) {
            super(itemView);
            thumbnail=itemView.findViewById(R.id.pcg_Img);
            name=itemView.findViewById(R.id.pcg_name);
            cost=itemView.findViewById(R.id.pcg_price);
            ag_name=itemView.findViewById(R.id.pcg_agency_name);
            location=itemView.findViewById(R.id.pcg_loc);
            duration=itemView.findViewById(R.id.pcg_dur);
        }
    }
}
