package com.example.ghuraghuri;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
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

public class Gallery extends AppCompatActivity {

    ProgressBar progressBar;
    TextView nothing;
    RecyclerView recyclerView;
    GalleryAdapter adapter;

    String place_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gallery);

        place_id=getIntent().getStringExtra("plc_id");

        progressBar=findViewById(R.id.pr_gallery);
        nothing=findViewById(R.id.gallery_noth);
        recyclerView=findViewById(R.id.gallery_recView);

        progressBar.setVisibility(View.VISIBLE);
        recyclerView.setHasFixedSize(true);


        getImage();
    }

    void getImage()
    {
        Constant.imgTitle.clear();
        Constant.imgUrl.clear();
        DatabaseReference ref= FirebaseDatabase.getInstance().getReference()
                .child("Places").child(place_id).child("Gallery");

        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                for(DataSnapshot snapshot1:snapshot.getChildren())
                {
                    String title,url;
                    title= snapshot1.getKey();
                    url= Objects.requireNonNull(snapshot1.getValue()).toString();

                    Constant.imgUrl.add(url);
                    Constant.imgTitle.add(title);
                }
                setImage();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    void setImage()
    {
        if(Constant.imgUrl.isEmpty())
        {
            nothing.setVisibility(View.VISIBLE);
        }
        else
        {
            nothing.setVisibility(View.GONE);
            adapter=new GalleryAdapter(this);
            recyclerView.setLayoutManager(new GridLayoutManager(this,3));
            recyclerView.setAdapter(adapter);
        }
        progressBar.setVisibility(View.INVISIBLE);
    }
}