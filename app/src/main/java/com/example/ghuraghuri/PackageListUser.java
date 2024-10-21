package com.example.ghuraghuri;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class PackageListUser extends AppCompatActivity {
    RecyclerView recyclerView;
    PackageAdapter adapter;
    TextView nothing;
    ProgressBar progressBar;
    EditText search;

    FirebaseAuth auth;
    ArrayList<String> agId=new ArrayList<>();
    String agUid="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_package_list_user);

        progressBar=findViewById(R.id.pr_pcg_user);
        nothing=findViewById(R.id.noth_pcg);
        recyclerView=findViewById(R.id.pcg_recView);
        search=findViewById(R.id.pcg_search_edt);

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        adapter=new PackageAdapter(this);
        progressBar.setVisibility(View.VISIBLE);

        if(Constant.role.equals("User")){
            getPackageInfo();
        }
        else{
            agUid= Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid();
            getPackageInfoAgency();
        }

    }
    void getPackageInfo(){
        DatabaseReference ref= FirebaseDatabase.getInstance().getReference()
                .child("Package");

        Constant.packageName.clear();
        Constant.packageCost.clear();
        Constant.packageLocation.clear();
        Constant.packageDuration.clear();
        Constant.journeyDate.clear();
        Constant.touristCapacity.clear();
        Constant.packageThumbnail.clear();
        Constant.packageId.clear();
        Constant.packageAgencyName.clear();
        Constant.packageAgencyId.clear();
        //Constant.packageSegment.clear();
        //Constant.packageAgencyName.clear();


        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot snapshot1:snapshot.getChildren()){
                    Constant.packageId.add(snapshot1.getKey());
                    Constant.packageName.add(Objects.requireNonNull(snapshot1.child("package name").getValue()).toString());
                    Constant.packageCost.add(Objects.requireNonNull(snapshot1.child("cost").getValue()).toString());
                    Constant.packageLocation.add(Objects.requireNonNull(snapshot1.child("location").getValue()).toString());
                    Constant.packageDuration.add(Objects.requireNonNull(snapshot1.child("duration").getValue()).toString());
                    Constant.journeyDate.add(Objects.requireNonNull(snapshot1.child("journey date").getValue()).toString());
                    Constant.touristCapacity.add(Objects.requireNonNull(snapshot1.child("capacity").getValue()).toString());
                    Constant.packageThumbnail.add(Objects.requireNonNull(snapshot1.child("Thumbnail").getValue()).toString());
                    Constant.packageAgencyName.add(Objects.requireNonNull(snapshot1.child("agency name").getValue()).toString());
                    Constant.packageAgencyId.add(Objects.requireNonNull(snapshot1.child("agency id").getValue()).toString());
                    //agId.add(Objects.requireNonNull(snapshot1.child("agency id").getValue()).toString());

                    /*HashMap<String, String> map=new HashMap<>();
                    if(snapshot1.child("segment").exists()){
                        for (DataSnapshot snapshot2:snapshot1.child("segment").getChildren()){
                            map.put(snapshot2.getKey(), Objects.requireNonNull(snapshot2.getValue()).toString());
                        }
                    }
                    Constant.packageSegment.add(map);*/

                }
                setAdapter();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    void getPackageInfoAgency(){
        DatabaseReference ref= FirebaseDatabase.getInstance().getReference()
                .child("Package");

        Constant.packageName.clear();
        Constant.packageCost.clear();
        Constant.packageLocation.clear();
        Constant.packageDuration.clear();
        Constant.journeyDate.clear();
        Constant.touristCapacity.clear();
        Constant.packageThumbnail.clear();
        Constant.packageId.clear();
        Constant.packageAgencyName.clear();
        Constant.packageAgencyId.clear();
        //Constant.packageSegment.clear();
        //Constant.packageAgencyName.clear();


        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot snapshot1:snapshot.getChildren()){
                    String id=Objects.requireNonNull(snapshot1.child("agency id").getValue()).toString();
                    if(id.equals(agUid)){
                        Constant.packageId.add(snapshot1.getKey());
                        Constant.packageName.add(Objects.requireNonNull(snapshot1.child("package name").getValue()).toString());
                        Constant.packageCost.add(Objects.requireNonNull(snapshot1.child("cost").getValue()).toString());
                        Constant.packageLocation.add(Objects.requireNonNull(snapshot1.child("location").getValue()).toString());
                        Constant.packageDuration.add(Objects.requireNonNull(snapshot1.child("duration").getValue()).toString());
                        Constant.journeyDate.add(Objects.requireNonNull(snapshot1.child("journey date").getValue()).toString());
                        Constant.touristCapacity.add(Objects.requireNonNull(snapshot1.child("capacity").getValue()).toString());
                        Constant.packageThumbnail.add(Objects.requireNonNull(snapshot1.child("Thumbnail").getValue()).toString());
                        Constant.packageAgencyName.add(Objects.requireNonNull(snapshot1.child("agency name").getValue()).toString());
                        Constant.packageAgencyId.add(Objects.requireNonNull(snapshot1.child("agency id").getValue()).toString());
                    }

                }
                setAdapter();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    void setAdapter(){
        if(Constant.packageId.size()==0){
            nothing.setVisibility(View.VISIBLE);
        }
        else
        {
            nothing.setVisibility(View.GONE);
            progressBar.setVisibility(View.GONE);
            recyclerView.setAdapter(adapter);
        }

    }

}