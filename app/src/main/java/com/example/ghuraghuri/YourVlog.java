package com.example.ghuraghuri;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Objects;

public class YourVlog extends AppCompatActivity {
    ProgressBar progressBar;
    Button add,save;
    Dialog dialog;
    EditText title,videoId,date,location;
    TextView nothing;
    RecyclerView recyclerView;
    VlogAdapter adapter;

    String uName;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_your_vlog);

        progressBar=findViewById(R.id.vlog_pr);
        add=findViewById(R.id.add_vlog_btn);
        nothing=findViewById(R.id.uvlog_noth);
        recyclerView=findViewById(R.id.uvlog_recView);

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        adapter=new VlogAdapter(this);
        progressBar.setVisibility(View.VISIBLE);

        Constant.fromVlogList=false;

        dialog=new Dialog(this);
        dialog.setContentView(R.layout.add_vlog_dialog);

        WindowManager.LayoutParams lp=new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());
        lp.width=WindowManager.LayoutParams.MATCH_PARENT;
        lp.height=WindowManager.LayoutParams.WRAP_CONTENT;
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
        dialog.getWindow().setAttributes(lp);

        title=dialog.findViewById(R.id.vlog_title_add);
        videoId=dialog.findViewById(R.id.vlog_id_add);
        date=dialog.findViewById(R.id.vlog_date_pick);
        save=dialog.findViewById(R.id.save_vlog_btn);
        location=dialog.findViewById(R.id.vlog_location_add);


        date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar calendar = Calendar.getInstance();
                int year = calendar.get(Calendar.YEAR);
                int month = calendar.get(Calendar.MONTH);
                int dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog datePickerDialog=new DatePickerDialog(YourVlog.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int selectedYear, int monthOfYear, int dayOfMonth) {
                        String selectedDate = dayOfMonth + "/" + (monthOfYear + 1) + "/" + selectedYear;
                        date.setText(selectedDate);

                    }
                }, year, month, dayOfMonth);
                datePickerDialog.show();
            }
        });
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.show();
                Date d=new Date();
                String currDate = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(d);
                date.setText(currDate);
            }
        });

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getName();
            }
        });

        getViedos();


    }

    void getName(){
        String uid= Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid();

        DatabaseReference ref2=FirebaseDatabase.getInstance().getReference()
                .child("User").child(uid);

        ref2.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                uName= Objects.requireNonNull(snapshot.child("Name").getValue()).toString();
                saveVlog(uid);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    void saveVlog(String uid){
        DatabaseReference ref= FirebaseDatabase.getInstance().getReference()
                .child("Vlog");
        String vTitle,vId,vDate,loc;
        vTitle=title.getText().toString().trim();
        vId=videoId.getText().toString().trim();
        vDate=date.getText().toString().trim();
        loc=location.getText().toString().trim();

        if(!vTitle.isEmpty() && !vId.isEmpty() && !vDate.isEmpty() && !loc.isEmpty()){
            String vlogId=ref.push().getKey();
            HashMap<String,Object> map = new HashMap<>();
            map.put("video id",vId);
            map.put("title",vTitle);
            map.put("date",vDate);
            map.put("vlogger",uName);
            map.put("location",loc);
            map.put("uid",uid);

            assert vlogId != null;
            ref.child(vlogId).updateChildren(map).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if(task.isSuccessful()){
                        Toast.makeText(YourVlog.this, "Info submitted successfully", Toast.LENGTH_SHORT).show();
                        title.setText("");
                        videoId.setText("");
                        dialog.dismiss();
                    }
                    else{
                        Toast.makeText(YourVlog.this, "Something went wrong", Toast.LENGTH_SHORT).show();
                    }
                }

            });
        }
        else {
            Toast.makeText(YourVlog.this, "Please fill up all fields", Toast.LENGTH_SHORT).show();
        }

    }

    void getViedos(){
        DatabaseReference ref= FirebaseDatabase.getInstance().getReference()
                .child("Vlog");
        Constant.videoTitle.clear();
        Constant.videoId.clear();
        Constant.videoDate.clear();
        Constant.vlogger.clear();
        Constant.videoLocation.clear();

        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot snapshot1:snapshot.getChildren()){
                    String uid= Objects.requireNonNull(snapshot1.child("uid").getValue()).toString();
                    if(Constant.curr_uid.equals(uid))
                    {
                        Constant.videoTitle.add(Objects.requireNonNull(snapshot1.child("title").getValue()).toString());
                        Constant.videoId.add(Objects.requireNonNull(snapshot1.child("video id").getValue()).toString());
                        Constant.videoDate.add(Objects.requireNonNull(snapshot1.child("date").getValue()).toString());
                        Constant.vlogger.add(Objects.requireNonNull(snapshot1.child("vlogger").getValue()).toString());
                        Constant.videoLocation.add(Objects.requireNonNull(snapshot1.child("location").getValue()).toString());
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
        if(Constant.videoId.isEmpty()){
            nothing.setVisibility(View.VISIBLE);
        }
        else
        {
            nothing.setVisibility(View.GONE);
            recyclerView.setAdapter(adapter);
        }
        progressBar.setVisibility(View.INVISIBLE);
    }
}