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

public class RestaurantDisplay extends AppCompatActivity {

    RecyclerView recyclerView;
    TextView nothing;
    ListAdapter adapter;
    ProgressBar progressBar;

    String place_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restaurant_display);

        place_id=getIntent().getStringExtra("plc_id");
        recyclerView=findViewById(R.id.rt_recView);
        nothing=findViewById(R.id.rt_noth);
        progressBar=findViewById(R.id.pr_rst);

        progressBar.setVisibility(View.VISIBLE);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        getRestaurantInfo(new callBack() {
            @Override
            public void onCallback(ArrayList<String> name, ArrayList<String> lat) {

                Constant.spot_name.clear();
                //Constant.spot_description.clear();
                Constant.url.clear();
                //Constant.longitude.clear();

                Constant.spot_name.addAll(name);
                Constant.url.addAll(lat);
                //Constant.longitude.addAll(longi);
                //Constant.spot_description.addAll(url);

                if(Constant.spot_name.isEmpty())
                {
                    nothing.setVisibility(View.VISIBLE);
                }
                else
                {
                    nothing.setVisibility(View.GONE);
                    adapter=new ListAdapter(RestaurantDisplay.this);
                    recyclerView.setAdapter(adapter);
                }
                progressBar.setVisibility(View.GONE);

            }
        });
    }

    interface callBack
    {
        void onCallback(ArrayList<String> name,
                        ArrayList<String> lat
                        );
    }

    void getRestaurantInfo(final callBack cb)
    {
        DatabaseReference ref= FirebaseDatabase.getInstance().getReference()
                .child("Places").child(place_id).child("Restaurant");

        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                ArrayList<String> name=new ArrayList<>();
                ArrayList<String> lat=new ArrayList<>();
                //ArrayList<String> longi=new ArrayList<>();

                for(DataSnapshot snapshot1:snapshot.getChildren())
                {
                    if(!Objects.equals(snapshot1.getKey(), "Count"))
                    {
                        name.add(String.valueOf(snapshot1.child("name").getValue()));
                        //url.add(String.valueOf(snapshot1.child("URL").getValue()));
                        lat.add(String.valueOf((snapshot1.child("url").getValue())));
                        //longi.add(String.valueOf((snapshot1.child("longitude").getValue())));
                    }

                }
                cb.onCallback(name,lat);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}