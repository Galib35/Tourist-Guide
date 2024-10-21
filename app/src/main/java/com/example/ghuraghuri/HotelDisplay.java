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

import java.util.ArrayList;
import java.util.Objects;

public class HotelDisplay extends AppCompatActivity {

    RecyclerView recyclerView;
    TextView nothing;
    ListAdapter adapter;
    ProgressBar progressBar;

    String place_id;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hotel_display);

        place_id=getIntent().getStringExtra("plc_id");
        recyclerView=findViewById(R.id.ht_recView);
        nothing=findViewById(R.id.ht_noth);
        progressBar=findViewById(R.id.pr_ht);

        progressBar.setVisibility(View.VISIBLE);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        getHotelInfo(new callBack() {
            @Override
            public void onCallback(ArrayList<String> name, ArrayList<String> url) {
                Constant.spot_name.clear();
                Constant.spot_description.clear();

                Constant.spot_name.addAll(name);
                Constant.spot_description.addAll(url);

                if(Constant.spot_name.isEmpty())
                {
                    nothing.setVisibility(View.VISIBLE);
                }
                else
                {
                    nothing.setVisibility(View.GONE);
                    adapter=new ListAdapter(HotelDisplay.this);
                    recyclerView.setAdapter(adapter);
                }
                progressBar.setVisibility(View.GONE);
            }
        });
    }

    interface callBack
    {
        void onCallback(ArrayList<String> name,
                        ArrayList<String> url);
    }

    void getHotelInfo(final callBack cb)
    {
        DatabaseReference ref= FirebaseDatabase.getInstance().getReference()
                .child("Places").child(place_id).child("Hotel");

        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                ArrayList<String> name=new ArrayList<>();
                ArrayList<String> url=new ArrayList<>();

                for(DataSnapshot snapshot1:snapshot.getChildren())
                {
                    if(!Objects.equals(snapshot1.getKey(), "Count"))
                    {
                        name.add(String.valueOf(snapshot1.child("Name").getValue()));
                        url.add(String.valueOf(snapshot1.child("URL").getValue()));
                    }

                }
                cb.onCallback(name,url);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}