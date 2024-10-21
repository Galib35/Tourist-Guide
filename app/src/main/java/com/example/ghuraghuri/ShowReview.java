package com.example.ghuraghuri;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Objects;

public class ShowReview extends AppCompatActivity {
    TextView rating,ratingCount;
    RatingBar ratingBar;
    RecyclerView recyclerView;
    RatingAdapter adapter;

    String place_id;
    ArrayList<String> uid=new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_review);

        rating=findViewById(R.id.rt_cng);
        ratingBar=findViewById(R.id.rt_bar);
        recyclerView=findViewById(R.id.rt_recView);
        ratingCount=findViewById(R.id.rt_count);

        place_id=getIntent().getStringExtra("plc_id");
        //Toast.makeText(this, place_id, Toast.LENGTH_SHORT).show();

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));


        rating.setText(Constant.rat);
        int x=Integer.parseInt(Constant.totalRatings);
        x=x-1;

        String str=String.valueOf(x)+" Ratings";
        ratingCount.setText(str);

        ratingBar.setRating(Float.parseFloat(Constant.rat));
        getInfo();

    }

    void getInfo()
    {
        Constant.rev.clear();
        Constant.urating.clear();
        Constant.udate.clear();
        Constant.uName.clear();
        //uid.clear();
        DatabaseReference ref= FirebaseDatabase.getInstance().getReference().child("Places")
                .child(place_id).child("Reviews");

        DatabaseReference ref2= FirebaseDatabase.getInstance().getReference().child("User");

        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {



                for(DataSnapshot snapshot1:snapshot.getChildren())
                {
                    String rev,rat,dt,nm;
                    if(snapshot1.child("review").exists())
                    {
                        rev= Objects.requireNonNull(snapshot1.child("review").getValue()).toString();
                        Constant.rev.add(rev);
                    }
                    dt= Objects.requireNonNull(snapshot1.child("date").getValue()).toString();

                    float rt=Float.parseFloat(String.valueOf(snapshot1.child("rating").getValue()));
                    rat = new DecimalFormat("##.#").format(rt);

                    Constant.udate.add(dt);
                    Constant.urating.add(rat);

                    nm= Objects.requireNonNull(snapshot1.child("name").getValue()).toString();
                    Constant.uName.add(nm);
                    //Toast.makeText(ShowReview.this, Constant.rev.get(0), Toast.LENGTH_SHORT).show();
                    //uid.add(snapshot1.getKey());
                    //Toast.makeText(ShowReview.this, uid.get(0), Toast.LENGTH_SHORT).show();

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
        //Toast.makeText(this, String.valueOf(Constant.rev.size()), Toast.LENGTH_SHORT).show();
        if(!Constant.uName.isEmpty())
        {
            adapter=new RatingAdapter(ShowReview.this);
            recyclerView.setAdapter(adapter);
            adapter.notifyDataSetChanged();
        }
        else
        {
            Toast.makeText(this, "No review is given", Toast.LENGTH_SHORT).show();
        }



    }
}