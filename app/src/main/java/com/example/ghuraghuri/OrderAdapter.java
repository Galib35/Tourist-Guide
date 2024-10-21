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

public class OrderAdapter extends RecyclerView.Adapter<OrderAdapter.OrderViewHolder>{
    Context context;
    Constant.onAccept acceptListener;
    Constant.onReject rejectListener;

    public OrderAdapter(Context context, Constant.onAccept acceptListener, Constant.onReject rejectListener) {
        this.context = context;
        this.acceptListener = acceptListener;
        this.rejectListener = rejectListener;
    }


    @NonNull
    @Override
    public OrderAdapter.OrderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(context).inflate(R.layout.order_item_design,parent,false);
        return new OrderViewHolder(v);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull OrderAdapter.OrderViewHolder holder, int position) {
        holder.date.setText(Constant.orderDate.get(position));
        holder.time.setText(Constant.orderTime.get(position));
        holder.id.setText(Constant.orderId.get(position));
        holder.name.setText(Constant.customerName.get(position));
        holder.phn.setText(Constant.customerPhoneNo.get(position));
        holder.total.setText(Constant.orderTotal.get(position)+"tk");
        holder.address.setText(Constant.customerAddress.get(position));

    }

    @Override
    public int getItemCount() {
        return Constant.orderId.size();
    }

    public static class OrderViewHolder extends RecyclerView.ViewHolder{
        TextView date,time,id,name,phn,total,address;
        Button accept,reject;
        public OrderViewHolder(@NonNull View itemView) {
            super(itemView);
            accept=itemView.findViewById(R.id.order_accept);
            reject=itemView.findViewById(R.id.order_reject);
            date=itemView.findViewById(R.id.order_date);
            time=itemView.findViewById(R.id.order_time);
            id=itemView.findViewById(R.id.order_id);
            name=itemView.findViewById(R.id.order_customer_name);
            phn=itemView.findViewById(R.id.customer_phn);
            total=itemView.findViewById(R.id.order_price);
            address=itemView.findViewById(R.id.customer_adrs);

        }
    }
}
