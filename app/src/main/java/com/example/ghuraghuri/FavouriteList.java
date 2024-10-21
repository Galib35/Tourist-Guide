package com.example.ghuraghuri;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Objects;

public class FavouriteList extends AppCompatActivity {

    TextView nothing;
    RecyclerView recyclerView;
    FirebaseAuth auth;
    FavouriteAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favourite_list);

        nothing=findViewById(R.id.fv_noth);
        recyclerView=findViewById(R.id.fv_recView);
        auth=FirebaseAuth.getInstance();

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        getInfo();

    }
    void getInfo()
    {
        Constant.fvname.clear();
        Constant.fvTag.clear();
        Constant.fvRating.clear();

        String uid= Objects.requireNonNull(auth.getCurrentUser()).getUid();
        DatabaseReference ref= FirebaseDatabase.getInstance().getReference().
                child("User").child(uid).child("Favourite List");

        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists())
                {
                    for(DataSnapshot snapshot1:snapshot.getChildren())
                    {
                        Constant.fvname.add(Objects.requireNonNull(snapshot1.child("name").getValue()).toString());
                        Constant.fvTag.add(Objects.requireNonNull(snapshot1.child("tag").getValue()).toString());
                        Constant.fvRating.add(Objects.requireNonNull(snapshot1.child("rating").getValue()).toString());
                        Constant.fvPlaceId.add(Objects.requireNonNull(snapshot1.getKey()));

                        System.out.println(Constant.fvPlaceId.get(0));


                    }
                    setInfo();

                }
                else
                {
                    setInfo();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @SuppressLint("NotifyDataSetChanged")
    void setInfo()
    {

        if (Constant.fvname.isEmpty())
        {
            nothing.setVisibility(View.VISIBLE);
        }
        else
        {
            nothing.setVisibility(View.GONE);
            adapter=new FavouriteAdapter(this);
            recyclerView.setAdapter(adapter);
            adapter.notifyDataSetChanged();
        }
    }
}