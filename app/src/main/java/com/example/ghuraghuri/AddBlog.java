package com.example.ghuraghuri;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Objects;

public class AddBlog extends AppCompatActivity {

    Button save;
    EditText title,details,date,location;
    RecyclerView recyclerView;
    ImageView thumbnailBtn,imageListBtn,thumbnailPlaceholder;

    String uName;
    ActivityResultLauncher<String> getThumb,getContent;
    Uri image,thumb;
    ImageAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_blog);

        title=findViewById(R.id.blog_title_add);
        details=findViewById(R.id.blog_descrp);
        date=findViewById(R.id.blog_date_pick);
        save=findViewById(R.id.save_blog_btn);
        location=findViewById(R.id.blog_location);
        thumbnailBtn=findViewById(R.id.browse1);
        thumbnailPlaceholder=findViewById(R.id.img_dis);
        imageListBtn=findViewById(R.id.browse2);
        recyclerView=findViewById(R.id.img_recView_blog);

        Date d=new Date();
        String currDate = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(d);
        date.setText(currDate);

        initializePicker();
        initializePickerThumb();
        adapter=new ImageAdapter(this);
        Constant.uris.clear();

        date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar calendar = Calendar.getInstance();
                int year = calendar.get(Calendar.YEAR);
                int month = calendar.get(Calendar.MONTH);
                int dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog datePickerDialog=new DatePickerDialog(AddBlog.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int selectedYear, int monthOfYear, int dayOfMonth) {
                        String selectedDate = dayOfMonth + "/" + (monthOfYear + 1) + "/" + selectedYear;
                        date.setText(selectedDate);

                    }
                }, year, month, dayOfMonth);
                datePickerDialog.show();
            }
        });

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(image==null | thumb==null){
                    Toast.makeText(AddBlog.this, "Please select images for Thumbnail and Gallery", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    Constant.uris.add(thumb);
                    getName();
                }

            }
        });

        thumbnailBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getThumb.launch("image/*");
            }
        });
        imageListBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getContent.launch("image/*");
            }
        });
    }

    void getName(){
        String uid= Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid();

        DatabaseReference ref2= FirebaseDatabase.getInstance().getReference()
                .child("User").child(uid);

        ref2.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                uName= Objects.requireNonNull(snapshot.child("Name").getValue()).toString();
                saveBlog(uid);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    void saveBlog(String uid){
        DatabaseReference ref= FirebaseDatabase.getInstance().getReference()
                .child("Blog");
        String bTitle,vId,bDate,loc,detail;
        bTitle=title.getText().toString().trim();
        //vId=videoId.getText().toString().trim();
        bDate=date.getText().toString().trim();
        loc=location.getText().toString().trim();
        detail=details.getText().toString().trim();

        if(!bTitle.isEmpty() && !bDate.isEmpty() && !loc.isEmpty()){
            String blogId=ref.push().getKey();

            HashMap<String,Object> map = new HashMap<>();
            //map.put("video id",vId);
            map.put("title",bTitle);
            map.put("date",bDate);
            map.put("blogger",uName);
            map.put("location",loc);
            map.put("uid",uid);
            map.put("details",detail);

            assert blogId != null;
            ProgressDialog progressDialog=new ProgressDialog(this);
            progressDialog.setMessage("Info Uploading...");
            progressDialog.setTitle("Basic Info");
            progressDialog.show();
            ref.child(blogId).updateChildren(map).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if(task.isSuccessful()){
                        progressDialog.dismiss();
                        Toast.makeText(AddBlog.this, "Info submitted successfully", Toast.LENGTH_SHORT).show();
                        saveImage(blogId);

                    }
                    else{
                        Toast.makeText(AddBlog.this, "Something went wrong", Toast.LENGTH_SHORT).show();
                    }
                }

            });
        }
        else {
            Toast.makeText(AddBlog.this, "Please fill up all fields", Toast.LENGTH_SHORT).show();
        }

    }

    void saveImage(String key){
        StorageReference storageReference= FirebaseStorage.getInstance().getReference().
                child("BlogImages");

        DatabaseReference ref1=FirebaseDatabase.getInstance().getReference()
                .child("Blog").child(key).child("Thumbnail");

        DatabaseReference ref2=FirebaseDatabase.getInstance().getReference()
                .child("Blog").child(key).child("Photos");

        ProgressDialog progressDialog=new ProgressDialog(this);
        progressDialog.setTitle("Image Uploader");
        //progressDialog.setMessage("Image Uploading...");
        int i;
        for(i=0;i<Constant.uris.size();i++)
        {
            progressDialog.show();

            String title=String.valueOf(i);
            int finalI = i;
            storageReference.child(key).child(title).putFile(Constant.uris.get(i)).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                    if(task.isSuccessful())
                    {
                        progressDialog.dismiss();
                        Toast.makeText(AddBlog.this, "Images uploaded", Toast.LENGTH_SHORT).show();
                        storageReference.child(key).child(title).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                String url=uri.toString();
                                ref2.child(title).setValue(url);

                                if(finalI ==Constant.uris.size()-1){
                                    ref1.setValue(url);
                                }
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(AddBlog.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                    else{
                        Toast.makeText(AddBlog.this, Objects.requireNonNull(task.getException()).toString(), Toast.LENGTH_SHORT).show();
                    }

                }
            }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onProgress(@NonNull UploadTask.TaskSnapshot snapshot) {
                    float percent= ((float)snapshot.getBytesTransferred()/snapshot.getTotalByteCount())*100;
                    progressDialog.setMessage("Uploaded: "+(int)percent+"%");
                }
            });
        }
    }

    void initializePicker()
    {
        getContent=registerForActivityResult(new ActivityResultContracts.GetContent(), uri-> {

            if(uri!=null)
            {
                image=uri;
                Constant.uris.add(image);
                //Constant.imgTitle.add(imgTitle.getText().toString());

                recyclerView.setLayoutManager(new LinearLayoutManager(AddBlog.this, RecyclerView.HORIZONTAL,false));
                recyclerView.setAdapter(adapter);
            }
            else
            {
                Toast.makeText(AddBlog.this, "No file selected", Toast.LENGTH_SHORT).show();
            }
        });
    }

    void initializePickerThumb()
    {
        getThumb=registerForActivityResult(new ActivityResultContracts.GetContent(), uri-> {

            if(uri!=null)
            {
                thumb=uri;
                thumbnailPlaceholder.setImageURI(thumb);
            }
            else
            {
                Toast.makeText(AddBlog.this, "No file selected", Toast.LENGTH_SHORT).show();
            }
        });
    }


}