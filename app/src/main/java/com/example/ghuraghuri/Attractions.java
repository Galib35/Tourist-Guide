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

public class Attractions extends AppCompatActivity {

    RecyclerView recyclerView;
    TextView nothing;
    ListAdapter adapter;
    ProgressBar progressBar;

    String place_id;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attractions);

        place_id=getIntent().getStringExtra("plc_id");
        recyclerView=findViewById(R.id.atr_recView);
        nothing=findViewById(R.id.atr_noth);
        progressBar=findViewById(R.id.pr_attr);

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        progressBar.setVisibility(View.VISIBLE);

        getAttractions(new callBack() {
            @Override
            public void onCallback(ArrayList<String> name, ArrayList<String> des) {
                Constant.spot_name.clear();
                Constant.spot_description.clear();

                Constant.spot_name.addAll(name);
                Constant.spot_description.addAll(des);

                if(Constant.spot_name.isEmpty())
                {
                    nothing.setVisibility(View.VISIBLE);
                }
                else
                {
                    nothing.setVisibility(View.GONE);
                    adapter=new ListAdapter(Attractions.this);
                    recyclerView.setAdapter(adapter);
                }
                progressBar.setVisibility(View.GONE);
            }
        });
    }

    interface callBack
    {
        void onCallback(ArrayList<String> name,
                        ArrayList<String> des);
    }

    void getAttractions(final callBack cb)
    {
        Constant.mp.clear();
        DatabaseReference ref= FirebaseDatabase.getInstance().getReference()
                .child("Places").child(place_id).child("Tourist Spots");

        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                ArrayList<String> name=new ArrayList<>();
                ArrayList<String> des=new ArrayList<>();

                for(DataSnapshot snapshot1:snapshot.getChildren())
                {
                    if(!Objects.equals(snapshot1.getKey(), "Count"))
                    {
                        name.add(String.valueOf(snapshot1.child("Name").getValue()));
                        des.add(String.valueOf(snapshot1.child("Description").getValue()));

                        if (snapshot1.child("Images").exists())
                        {
                            ArrayList<String > imgurl=new ArrayList<>();
                            for (DataSnapshot snapshot2:snapshot1.child("Images").getChildren())
                            {
                                imgurl.add(Objects.requireNonNull(snapshot2.getValue()).toString());
                            }
                            Constant.mp.put(snapshot1.getKey(),imgurl);
                        }
                    }

                }
                cb.onCallback(name,des);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}