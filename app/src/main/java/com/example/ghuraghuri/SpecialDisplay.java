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

public class SpecialDisplay extends AppCompatActivity {

    RecyclerView recyclerView;
    TextView nothing;
    ListAdapter adapter;
    ProgressBar progressBar;

    String place_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_special_display);

        place_id=getIntent().getStringExtra("plc_id");
        nothing=findViewById(R.id.sp_noth);
        recyclerView=findViewById(R.id.sp_recView);
        progressBar=findViewById(R.id.pr_spec);

        progressBar.setVisibility(View.VISIBLE);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        getInfo();
    }

    void getInfo()
    {
        Constant.spot_name.clear();
        Constant.spot_description.clear();

        DatabaseReference ref= FirebaseDatabase.getInstance().getReference()
                .child("Places").child(place_id).child("Speciality");
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                for(DataSnapshot dataSnapshot:snapshot.getChildren())
                {
                    if(!Objects.equals(dataSnapshot.getKey(), "Count"))
                    {
                        Constant.spot_name.add(String.valueOf(dataSnapshot.child("category").getValue()));
                        Constant.spot_description.add(String.valueOf((dataSnapshot.child("description").getValue())));

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
            adapter=new ListAdapter(SpecialDisplay.this);
            recyclerView.setAdapter(adapter);
        }
        progressBar.setVisibility(View.GONE);
    }
}