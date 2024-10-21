package com.example.ghuraghuri;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Objects;

public class OrderList extends AppCompatActivity implements Constant.onAccept, Constant.onReject{
    RecyclerView recyclerView;
    OrderAdapter adapter;
    TextView nothing;
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_list);

        recyclerView=findViewById(R.id.order_recView);
        nothing=findViewById(R.id.noth_order);
        progressBar=findViewById(R.id.pr_order);

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        adapter=new OrderAdapter(this,this,this);
        getInfo();

        progressBar.setVisibility(View.VISIBLE);
    }

    void getInfo(){
        DatabaseReference ref= FirebaseDatabase.getInstance().getReference()
                .child("Orders");

        Constant.orderId.clear();
        Constant.orderDate.clear();
        Constant.orderTime.clear();
        Constant.orderTotal.clear();
        Constant.customerName.clear();
        Constant.customerPhoneNo.clear();
        Constant.customerAddress.clear();

        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot snapshot1:snapshot.getChildren()){
                    Constant.orderId.add(snapshot1.getKey());
                    Constant.orderDate.add(Objects.requireNonNull(snapshot1.child("date").getValue()).toString());
                    Constant.orderTime.add(Objects.requireNonNull(snapshot1.child("time").getValue()).toString());
                    Constant.orderTotal.add(Objects.requireNonNull(snapshot1.child("total").getValue()).toString());
                    Constant.customerName.add(Objects.requireNonNull(snapshot1.child("userName").getValue()).toString());
                    Constant.customerPhoneNo.add(Objects.requireNonNull(snapshot1.child("contact").getValue()).toString());
                    Constant.customerAddress.add(Objects.requireNonNull(snapshot1.child("address").getValue()).toString());
                }
                setInfo();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    void setInfo(){
        if(Constant.orderId.isEmpty()){
            nothing.setVisibility(View.VISIBLE);
        }
        else {
            nothing.setVisibility(View.GONE);
            recyclerView.setAdapter(adapter);
        }
        progressBar.setVisibility(View.GONE);
    }


    @Override
    public void onAccept(int position) {

    }

    @Override
    public void onReject(int position) {

    }
}