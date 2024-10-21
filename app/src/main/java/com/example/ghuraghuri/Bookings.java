package com.example.ghuraghuri;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Objects;

public class Bookings extends AppCompatActivity implements Constant.onAccept, Constant.onReject{

    RecyclerView recyclerView;
    BookingAdapter adapter;
    TextView nothing;
    ProgressBar progressBar;

    ArrayList<String> userId=new ArrayList<>();
    ArrayList<String> packageId=new ArrayList<>();
    ArrayList<String> bookingId=new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bookings);
        recyclerView=findViewById(R.id.booking_recView);
        nothing=findViewById(R.id.noth_booking);
        progressBar=findViewById(R.id.pr_booking);

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        adapter=new BookingAdapter(this,this,this);

        progressBar.setVisibility(View.VISIBLE);
        getBookingInfo();

    }

    void getBookingInfo(){
        DatabaseReference ref= FirebaseDatabase.getInstance().getReference()
                .child("Booking");
        Constant.packageName.clear();
        Constant.packageLocation.clear();
        Constant.packageDuration.clear();
        Constant.packageCost.clear();
        Constant.touristName.clear();
        Constant.touristContact.clear();
        Constant.touristMember.clear();
        Constant.date.clear();
        Constant.time.clear();
        Constant.journeyDate.clear();
        Constant.packageStatus.clear();
        Constant.touristCapacity.clear();

        userId.clear();
        packageId.clear();
        bookingId.clear();

        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot snapshot1:snapshot.getChildren()){
                    String status=Objects.requireNonNull(snapshot1.child("status").getValue()).toString();
                    if(status.equals("Pending")){
                        bookingId.add(snapshot1.getKey());
                        userId.add(Objects.requireNonNull(snapshot1.child("user_id").getValue()).toString());
                        packageId.add(Objects.requireNonNull(snapshot1.child("package_id").getValue()).toString());
                        Constant.journeyDate.add(Objects.requireNonNull(snapshot1.child("journey_date").getValue()).toString());
                        Constant.touristMember.add(Objects.requireNonNull(snapshot1.child("tourist_number").getValue()).toString());
                        Constant.date.add(Objects.requireNonNull(snapshot1.child("date").getValue()).toString());
                        Constant.time.add(Objects.requireNonNull(snapshot1.child("time").getValue()).toString());
                    }

                    //Constant.packageStatus.add(Objects.requireNonNull(snapshot1.child("status").getValue()).toString());
                }
                getUserInfo();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    void getUserInfo(){
        DatabaseReference ref=FirebaseDatabase.getInstance().getReference()
                .child("User");
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(String id:userId){
                    //System.out.println("id1: "+id);
                    for (DataSnapshot snapshot1:snapshot.getChildren()){
                        String id2=snapshot1.getKey();
                        //System.out.println("id2: "+id2);
                        if(id.equals(id2)){
                            System.out.println("ok");
                            Constant.touristName.add(Objects.requireNonNull(snapshot1.child("Name").getValue()).toString());
                            Constant.touristContact.add(Objects.requireNonNull(snapshot1.child("Contact_no").getValue()).toString());
                            //System.out.println(Objects.requireNonNull(snapshot1.child("Name").getValue()));
                            break;
                        }
                    }
                }

                getPackageInfo();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    void getPackageInfo(){
        DatabaseReference ref=FirebaseDatabase.getInstance().getReference()
                .child("Package");
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(String id: packageId){
                    for (DataSnapshot snapshot1:snapshot.getChildren()){
                        String id2=snapshot1.getKey();
                        if(id.equals(id2)){
                            Constant.packageName.add(Objects.requireNonNull(snapshot1.child("package name").getValue()).toString());
                            Constant.packageCost.add(Objects.requireNonNull(snapshot1.child("cost").getValue()).toString());
                            Constant.packageLocation.add(Objects.requireNonNull(snapshot1.child("location").getValue()).toString());
                            Constant.packageDuration.add(Objects.requireNonNull(snapshot1.child("duration").getValue()).toString());
                            Constant.touristCapacity.add(Objects.requireNonNull(snapshot1.child("capacity").getValue()).toString());
                            break;
                        }
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
        //System.out.println(Constant.touristName.size());
        if(packageId.isEmpty()){
            nothing.setVisibility(View.VISIBLE);
        }
        else {
            nothing.setVisibility(View.GONE);
            recyclerView.setAdapter(adapter);
        }
        progressBar.setVisibility(View.GONE);
    }

    @Override
    public void onAccept(int position) {
        DatabaseReference ref= FirebaseDatabase.getInstance().getReference()
                .child("Package").child(packageId.get(position));

        DatabaseReference ref2= FirebaseDatabase.getInstance().getReference()
                .child("Booking").child(bookingId.get(position));

        DatabaseReference ref3=FirebaseDatabase.getInstance().getReference()
                .child("User").child(userId.get(position)).child("Booking").child(bookingId.get(position));

        int touristNumber=Integer.parseInt(Constant.touristMember.get(position));
        int touristCapacity=Integer.parseInt(Constant.touristCapacity.get(position));

        int updatedValue=touristCapacity-touristNumber;

        ref.child("capacity").setValue(updatedValue);
        ref2.child("status").setValue("Accepted");
        ref3.child("status").setValue("Accepted");
        ref3.child("message").setValue("Happy tour.");

        removeFromList(position);

        Toast.makeText(Bookings.this, "Booking accepted", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onReject(int position) {
        DatabaseReference ref2= FirebaseDatabase.getInstance().getReference()
                .child("Booking").child(bookingId.get(position));

        DatabaseReference ref3=FirebaseDatabase.getInstance().getReference()
                .child("User").child(userId.get(position)).child("Booking").child(bookingId.get(position));

        ref2.child("status").setValue("Rejected");
        ref3.child("status").setValue("Rejected");
        ref3.child("message").setValue("show cause");

        removeFromList(position);
        Toast.makeText(Bookings.this, "Booking rejected", Toast.LENGTH_SHORT).show();

    }

    void removeFromList(int position){
        Constant.packageName.remove(position);
        Constant.packageLocation.remove(position);
        Constant.packageDuration.remove(position);
        Constant.packageCost.remove(position);
        Constant.touristName.remove(position);
        Constant.touristContact.remove(position);
        Constant.touristMember.remove(position);
        Constant.date.remove(position);
        Constant.time.remove(position);
        Constant.journeyDate.remove(position);
        //Constant.packageStatus.remove(position);
        Constant.touristCapacity.remove(position);

        userId.remove(position);
        packageId.remove(position);
        bookingId.remove(position);
        adapter.notifyItemRemoved(position);
        adapter.notifyItemRangeChanged(position, adapter.getItemCount());
    }
}