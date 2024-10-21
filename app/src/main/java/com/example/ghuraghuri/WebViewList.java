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

public class WebViewList extends AppCompatActivity {

    RecyclerView recyclerView;
    TextView nothing;
    ListAdapter adapter;
    ProgressBar progressBar;

    String place_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_view_list);

        place_id=getIntent().getStringExtra("plc_id");

        recyclerView=findViewById(R.id.more_recView);
        nothing=findViewById(R.id.more_noth);
        progressBar=findViewById(R.id.pr_more);

        progressBar.setVisibility(View.VISIBLE);

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        getInfo();
    }

    void getInfo(){
        Constant.spot_name.clear();
        Constant.url.clear();
        DatabaseReference ref= FirebaseDatabase.getInstance().getReference()
                .child("Places").child(place_id).child("More");
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    for (DataSnapshot snapshot1:snapshot.getChildren()){
                        Constant.spot_name.add(snapshot1.getKey());
                        Constant.url.add(Objects.requireNonNull(snapshot1.getValue()).toString());
                    }

                }
                setInfo();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    void setInfo(){
        if(Constant.spot_name.isEmpty())
        {
            nothing.setVisibility(View.VISIBLE);
        }
        else
        {
            nothing.setVisibility(View.GONE);
            adapter=new ListAdapter(WebViewList.this);
            recyclerView.setAdapter(adapter);
        }
        progressBar.setVisibility(View.GONE);
    }
}