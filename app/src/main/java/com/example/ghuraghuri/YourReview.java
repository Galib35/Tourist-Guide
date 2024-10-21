package com.example.ghuraghuri;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Objects;

public class YourReview extends AppCompatActivity {

    TextView nothing;
    RecyclerView recyclerView;
    FirebaseAuth auth;
    RatingAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_your_review);

        nothing=findViewById(R.id.urev_noth);
        recyclerView=findViewById(R.id.urev_recView);
        auth=FirebaseAuth.getInstance();

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        getInfo();
    }

    void getInfo()
    {
        Constant.uName.clear();
        Constant.rev.clear();
        Constant.urating.clear();
        Constant.udate.clear();
        String uid= Objects.requireNonNull(auth.getCurrentUser()).getUid();
        DatabaseReference ref= FirebaseDatabase.getInstance().getReference()
                .child("User").child(uid);
        ref.child("Reviews").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot snapshot1:snapshot.getChildren())
                {
                    Constant.uName.add(Objects.requireNonNull(snapshot1.child("name").getValue()).toString());
                    Constant.udate.add(Objects.requireNonNull(snapshot1.child("date").getValue()).toString());
                    Constant.urating.add(Objects.requireNonNull(snapshot1.child("rating").getValue()).toString());
                    if(snapshot1.child("review").exists())
                    {
                        Constant.rev.add(Objects.requireNonNull(snapshot1.child("review").getValue()).toString());
                    }
                }
                setInfo();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @SuppressLint("NotifyDataSetChanged")
    void setInfo()
    {
        if(!Constant.uName.isEmpty())
        {
            nothing.setVisibility(View.GONE);
            adapter=new RatingAdapter(this);
            recyclerView.setAdapter(adapter);
            adapter.notifyDataSetChanged();
        }
        else
        {
            nothing.setVisibility(View.VISIBLE);
            Toast.makeText(this, "No review is given", Toast.LENGTH_SHORT).show();
        }
    }
}