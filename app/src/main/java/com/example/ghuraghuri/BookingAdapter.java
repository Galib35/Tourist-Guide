package com.example.ghuraghuri;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class BookingAdapter extends RecyclerView.Adapter<BookingAdapter.BookingViewHolder>{

    Context context;
    Constant.onAccept acceptListener;
    Constant.onReject rejectListener;

    public BookingAdapter(Context context, Constant.onAccept acceptListener, Constant.onReject rejectListener) {
        this.context = context;
        this.acceptListener = acceptListener;
        this.rejectListener = rejectListener;
    }

    @NonNull
    @Override
    public BookingAdapter.BookingViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(context).inflate(R.layout.booking_item_design,parent,false);
        return new BookingViewHolder(v);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull BookingAdapter.BookingViewHolder holder, int position) {
        holder.pcgTitle.setText(Constant.packageName.get(position));
        holder.pcgLoc.setText(Constant.packageLocation.get(position));
        holder.pcgCost.setText(Constant.packageCost.get(position)+"tk");
        holder.pcgDur.setText(Constant.packageDuration.get(position));
        holder.tName.setText(Constant.touristName.get(position));
        holder.contact.setText(Constant.touristContact.get(position));
        holder.bkTime.setText(Constant.time.get(position));
        holder.bkDate.setText(Constant.date.get(position));
        holder.member.setText(Constant.touristMember.get(position)+" Persons");
        holder.jrdate.setText("Tour date: "+Constant.journeyDate.get(position));
        holder.capacity.setText("Capacity: "+Constant.touristCapacity.get(position));

        holder.accept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (acceptListener != null) {
                    acceptListener.onAccept(holder.getAdapterPosition());
                }
            }
        });

        holder.reject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(rejectListener!=null){
                    rejectListener.onReject(holder.getAdapterPosition());
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return Constant.packageName.size();
    }

    public static class BookingViewHolder extends RecyclerView.ViewHolder{
        TextView bkDate,bkTime,pcgTitle,pcgLoc,pcgDur,pcgCost,tName,contact,member,jrdate,capacity;
        Button accept,reject;
        public BookingViewHolder(@NonNull View itemView) {
            super(itemView);
            bkDate=itemView.findViewById(R.id.booking_date);
            bkTime=itemView.findViewById(R.id.booking_time);
            pcgTitle=itemView.findViewById(R.id.pcg_name);
            pcgLoc=itemView.findViewById(R.id.pcg_loc);
            pcgDur=itemView.findViewById(R.id.pcg_dur);
            pcgCost=itemView.findViewById(R.id.pcg_price);
            tName=itemView.findViewById(R.id.t_name);
            contact=itemView.findViewById(R.id.t_contact);
            member=itemView.findViewById(R.id.t_member);
            accept=itemView.findViewById(R.id.booking_accept);
            reject=itemView.findViewById(R.id.booking_reject);
            jrdate=itemView.findViewById(R.id.jr_date);
            capacity=itemView.findViewById(R.id.tr_capacity);
        }
    }
}
