package com.example.ghuraghuri;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Objects;

public class Plan extends AppCompatActivity {

    Dialog dialog;
    RecyclerView recyclerView;
    Button complete,not_now,submit;
    RatingBar rating;
    EditText review;
    PlanAdapter adapter;
    TextView nothing;

    int stepCount=0;
    String place_id,name;
    float given_rt,plc_rating;
    int total_rt,rev_count;
    String uid;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_plan);

        place_id=getIntent().getStringExtra("plc_id");

        recyclerView=findViewById(R.id.plan_recView);
        complete=findViewById(R.id.cmplt_btn);
        nothing=findViewById(R.id.plan_noth);

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        dialog=new Dialog(this);
        dialog.setContentView(R.layout.rating_layout);
        dialog.setCancelable(false);

        WindowManager.LayoutParams lp=new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());
        lp.width=WindowManager.LayoutParams.MATCH_PARENT;
        lp.height=WindowManager.LayoutParams.WRAP_CONTENT;
        dialog.getWindow().setAttributes(lp);


        complete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(Constant.stepCount>0)
                {
                    dialog.show();
                    getDialogueData();
                }
                else
                {
                    Toast.makeText(Plan.this, "Please complete at least one step", Toast.LENGTH_SHORT).show();
                }

            }
        });

        not_now=dialog.findViewById(R.id.not_btn_dlg);
        not_now.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        getPlan();

    }
    void getDialogueData()
    {
        rating=dialog.findViewById(R.id.rt_dlg);
        review=dialog.findViewById(R.id.rev_dlg);
        submit=dialog.findViewById(R.id.submit_btn_dlg);


        rating.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float v, boolean b) {
                plc_rating=v;
                //System.out.println(plc_rating);
                //submit.setEnabled(true);
            }
        });


        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                FirebaseAuth auth=FirebaseAuth.getInstance();
                uid= Objects.requireNonNull(auth.getCurrentUser()).getUid();

                DatabaseReference reference= FirebaseDatabase.getInstance().getReference().child("Places").child(place_id);
                DatabaseReference ref3=FirebaseDatabase.getInstance().getReference().child("User").child(uid);
                if (plc_rating!=0)
                {
                    reference.addListenerForSingleValueEvent(new ValueEventListener() {
                        String given_rating;
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            given_rating=Objects.requireNonNull(snapshot.child("Ratings").getValue()).toString();
                            given_rt=Float.parseFloat(given_rating);
                            total_rt=Integer.parseInt(Objects.requireNonNull(snapshot.child("Total ratings").getValue()).toString());

                            total_rt++;
                            // System.out.println(plc_rating);
                            float calc_rating=(plc_rating+(given_rt*(total_rt-1)))/(total_rt);
                            reference.child("Ratings").setValue(String.valueOf(calc_rating));
                            reference.child("Total ratings").setValue(String.valueOf(total_rt));
                            name= Objects.requireNonNull(snapshot.child("Name").getValue()).toString();
                            ref3.child("Reviews").child(place_id).child("name").setValue(name);


                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                }




                String rev=review.getText().toString().trim();


                DatabaseReference ref2=FirebaseDatabase.getInstance().getReference().child("User").child(uid);
                if(plc_rating==0)
                {
                    Toast.makeText(Plan.this, "Please give rating", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    if(!rev.isEmpty())
                    {

                        reference.child("Reviews").child(uid).child("review").setValue(rev);
                        ref2.child("Reviews").child(place_id).child("review").setValue(rev);
                    }
                    Calendar calendar=Calendar.getInstance();
                    @SuppressLint("SimpleDateFormat") SimpleDateFormat simpleDateFormat=new SimpleDateFormat("d MMM, yyyy");

                    String date=simpleDateFormat.format(calendar.getTime());
                    reference.child("Reviews").child(uid).child("date").setValue(date);
                    reference.child("Reviews").child(uid).child("rating").setValue(plc_rating);

                    ref2.child("Reviews").child(place_id).child("date").setValue(date);
                    ref2.child("Reviews").child(place_id).child("rating").setValue(plc_rating);


                    Toast.makeText(Plan.this, "Thanks for your opinion", Toast.LENGTH_SHORT).show();
                    //rating.setRating(0);
                    //review.setText(null);

                    ref2.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            reference.child("Reviews").child(uid).child("name").setValue(snapshot.child("Name").getValue());
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                    dialog.dismiss();
                }






            }

        });





    }

    void getPlan()
    {
        Constant.where.clear();
        Constant.todo.clear();
        Constant.step.clear();
        Constant.stepCount=0;

        DatabaseReference ref= FirebaseDatabase.getInstance().getReference()
                .child("Places").child(place_id).child("Tour Plan");

        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot snapshot1: snapshot.getChildren())
                {
                    Constant.step.add(snapshot1.getKey());
                    Constant.where.add(Objects.requireNonNull(snapshot1.child("place").getValue()).toString());
                    Constant.todo.add(Objects.requireNonNull(snapshot1.child("todo").getValue()).toString());
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
        if(Constant.step.isEmpty())
        {
            nothing.setVisibility(View.VISIBLE);
        }
        else
        {
            nothing.setVisibility(View.GONE);
            adapter=new PlanAdapter(this);
            recyclerView.setAdapter(adapter);
            adapter.notifyDataSetChanged();
        }

    }
    /*void readReviewCount()
    {
        DatabaseReference ref=FirebaseDatabase.getInstance().getReference().child("Places").child(place_id).child("Reviews");
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(!snapshot.exists())
                {
                    rev_count=0;
                }
                else
                {
                    String cnt= Objects.requireNonNull(snapshot.child("Count").getValue()).toString();
                    rev_count=Integer.parseInt(cnt);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }*/
}