package com.example.ghuraghuri;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
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

public class BankDisplay extends AppCompatActivity {
    RecyclerView recyclerView;
    TextView nothing;
    ListAdapter adapter;
    ProgressBar progressBar;

    String place_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bank_display);

        place_id=getIntent().getStringExtra("plc_id");
        nothing=findViewById(R.id.bank_noth);
        recyclerView=findViewById(R.id.bank_recView);
        progressBar=findViewById(R.id.pr_bank);

        progressBar.setVisibility(View.VISIBLE);

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        getInfo();
    }

    void getInfo()
    {
        Constant.spot_name.clear();
        Constant.url.clear();

        DatabaseReference ref= FirebaseDatabase.getInstance().getReference()
                .child("Places").child(place_id).child("Bank or Atm");
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                for(DataSnapshot dataSnapshot:snapshot.getChildren())
                {
                    if(!Objects.equals(dataSnapshot.getKey(), "Count"))
                    {
                        Constant.spot_name.add(String.valueOf(dataSnapshot.child("name").getValue()));
                        Constant.url.add(String.valueOf((dataSnapshot.child("url").getValue())));

                    }
                }
                setInfo();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    void setInfo()
    {
        if(Constant.spot_name.isEmpty())
        {
            nothing.setVisibility(View.VISIBLE);
        }
        else
        {
            nothing.setVisibility(View.GONE);
            adapter=new ListAdapter(BankDisplay.this);
            recyclerView.setAdapter(adapter);
        }
        progressBar.setVisibility(View.GONE);
    }
}