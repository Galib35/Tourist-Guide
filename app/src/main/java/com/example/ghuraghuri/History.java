package com.example.ghuraghuri;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DecimalFormat;
import java.util.Objects;

public class History extends AppCompatActivity {

    TextView nothing;
    RecyclerView recyclerView;
    FirebaseAuth auth;
    HistoryAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        nothing=findViewById(R.id.hist_noth);
        recyclerView=findViewById(R.id.hist_recView);
        auth=FirebaseAuth.getInstance();

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        getInfo();
    }

    void getInfo()
    {
        DatabaseReference ref= FirebaseDatabase.getInstance().getReference().child("User");

        String uid= Objects.requireNonNull(auth.getCurrentUser()).getUid();
        Constant.histDate.clear();
        Constant.histName.clear();

        ref.child(uid).child("History").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot snapshot1: snapshot.getChildren())
                {
                   Constant.histName.add(Objects.requireNonNull(snapshot1.child("name").getValue()).toString());
                   Constant.histDate.add(Objects.requireNonNull(snapshot1.child("date").getValue()).toString());
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
        if (Constant.histName.isEmpty())
        {
            nothing.setVisibility(View.VISIBLE);
        }
        else
        {
            nothing.setVisibility(View.GONE);
            adapter=new HistoryAdapter(this);
            recyclerView.setAdapter(adapter);

        }
    }
}