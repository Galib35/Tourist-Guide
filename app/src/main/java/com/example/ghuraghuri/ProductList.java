package com.example.ghuraghuri;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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

public class ProductList extends AppCompatActivity {
    ProgressBar progressBar;
    TextView nothing;
    RecyclerView recyclerView;

    ProductListAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_list);

        progressBar=findViewById(R.id.pr_pdct);
        nothing=findViewById(R.id.noth);
        recyclerView=findViewById(R.id.pdct_recView);

        progressBar.setVisibility(View.VISIBLE);
        recyclerView.setHasFixedSize(true);

        adapter=new ProductListAdapter(this);
        recyclerView.setLayoutManager(new GridLayoutManager(this,2));

        getInfo();
    }
    void getInfo(){
        DatabaseReference ref= FirebaseDatabase.getInstance().getReference()
                .child("Product");

        Constant.productKey.clear();
        Constant.productName.clear();
        Constant.productFeatures.clear();
        Constant.productDescription.clear();
        Constant.productPrice.clear();
        Constant.productRating.clear();
        Constant.thumbnail.clear();
        Constant.productQuantity.clear();

        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot data:snapshot.getChildren())
                {
                    Constant.productKey.add(data.getKey());
                    Constant.productName.add(Objects.requireNonNull(data.child("product_name").getValue()).toString());
                    Constant.productPrice.add(Objects.requireNonNull(data.child("price").getValue()).toString());
                    Constant.productDescription.add(Objects.requireNonNull(data.child("description").getValue()).toString());
                    Constant.productFeatures.add(Objects.requireNonNull(data.child("features").getValue()).toString());
                    Constant.productRating.add(Objects.requireNonNull(data.child("rating").getValue()).toString());
                    Constant.thumbnail.add(Objects.requireNonNull(data.child("Thumbnail").getValue()).toString());
                    Constant.productQuantity.add(Objects.requireNonNull(data.child("quantity").getValue()).toString());
                }
                setData();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    void setData(){
        if(Constant.productName.isEmpty()){
            nothing.setVisibility(View.VISIBLE);
        }
        else
        {
            nothing.setVisibility(View.GONE);
            recyclerView.setAdapter(adapter);
        }
        progressBar.setVisibility(View.INVISIBLE);
    }
}