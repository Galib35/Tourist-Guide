package com.example.ghuraghuri;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class RatingAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    Context context;

    public RatingAdapter(Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(context).inflate(R.layout.review_design,parent,false);
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {



        String date=Constant.udate.get(position);
        date=" - "+date;
        ((MyViewHolder)holder).date.setText(date);
        if(position<Constant.rev.size())
        {
            ((MyViewHolder)holder).review.setText(Constant.rev.get(position));
        }
        ((MyViewHolder)holder).name.setText(Constant.uName.get(position));

        float rt=Float.parseFloat(Constant.urating.get(position));

        ((MyViewHolder)holder).ratingBar.setRating(rt);




    }

    @Override
    public int getItemCount() {
        return Constant.uName.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{

        TextView name,date,review;
        RatingBar ratingBar;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            name=itemView.findViewById(R.id.name_rev);
            date=itemView.findViewById(R.id.date);
            review=itemView.findViewById(R.id.rev_show);
            ratingBar=itemView.findViewById(R.id.rt_rev);

        }
    }
}
