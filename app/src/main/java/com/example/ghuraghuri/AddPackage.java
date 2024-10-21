package com.example.ghuraghuri;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
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

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Objects;

public class AddPackage extends AppCompatActivity {
    EditText packageTitle,location,price,duration,touristCapacity,journeyDate,segmentTile,segmentDetails;
    ImageView thumbnailBtn,imageListBtn,thumbnailPlaceholder;
    Button saveInfo,saveSegment,another;
    RecyclerView recyclerView;
    RadioGroup radioGroup;
    ImageButton add;

    ActivityResultLauncher<String> getThumb,getContent;
    Uri image,thumb;
    String key="";
    String uid,uName;

    ImageAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_package);

        packageTitle=findViewById(R.id.package_name);
        location=findViewById(R.id.pcg_location);
        price=findViewById(R.id.pcg_cost);
        duration=findViewById(R.id.pcg_duration);
        touristCapacity=findViewById(R.id.pcg_member);
        journeyDate=findViewById(R.id.pcg_journey_adte);
        thumbnailBtn=findViewById(R.id.pcg_thumb);
        thumbnailPlaceholder=findViewById(R.id.thumb_placeholder);
        imageListBtn=findViewById(R.id.pcg_Image_upload_btn);
        recyclerView=findViewById(R.id.pcg_img_recView);
        saveInfo=findViewById(R.id.pcg_info_save_btn);
        saveSegment=findViewById(R.id.pcg_segment_save_btn);
        another=findViewById(R.id.pcg_another);
        segmentTile=findViewById(R.id.pcg_segment_title);
        segmentDetails=findViewById(R.id.pcg_details);
        radioGroup=findViewById(R.id.radio_grp_pcg);
        add=findViewById(R.id.add_package);

        packageTitle.addTextChangedListener(watcher);
        location.addTextChangedListener(watcher);
        price.addTextChangedListener(watcher);
        duration.addTextChangedListener(watcher);
        touristCapacity.addTextChangedListener(watcher);

        segmentTile.addTextChangedListener(watcher2);
        segmentDetails.addTextChangedListener(watcher2);

        initializePicker();
        initializePickerThumb();
        adapter=new ImageAdapter(this);

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=getIntent();
                finish();
                startActivity(intent);
            }
        });

        saveInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(image==null | thumb==null){
                    Toast.makeText(AddPackage.this, "Please select images for Thumbnail and Gallery", Toast.LENGTH_SHORT).show();
                }
                else if(journeyDate.getText().toString().trim().equals("Select date")){
                    Toast.makeText(AddPackage.this, "Please select a date", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    Constant.uris.add(thumb);
                    //saveData();
                    getAgName();
                }

            }
        });

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                RadioButton radioButton=radioGroup.findViewById(i);
                String txt=radioButton.getText().toString();

                if(txt.equals("Fixed")){
                    journeyDate.setText("Select date");
                    Calendar calendar = Calendar.getInstance();
                    int year = calendar.get(Calendar.YEAR);
                    int month = calendar.get(Calendar.MONTH);
                    int dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);

                    DatePickerDialog datePickerDialog=new DatePickerDialog(AddPackage.this, new DatePickerDialog.OnDateSetListener() {
                        @Override
                        public void onDateSet(DatePicker datePicker, int selectedYear, int monthOfYear, int dayOfMonth) {
                            String selectedDate = dayOfMonth + "/" + (monthOfYear + 1) + "/" + selectedYear;
                            journeyDate.setText(selectedDate);

                        }
                    }, year, month, dayOfMonth);
                    datePickerDialog.show();
                }
                else
                {
                    journeyDate.setText("Negotiable");
                }
            }
        });

        Constant.uris.clear();
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

        saveSegment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveSegmentData();

                //testModel();
            }
        });
        another.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                segmentDetails.getText().clear();
                segmentDetails.clearFocus();
                segmentTile.getText().clear();
                segmentTile.clearFocus();

                another.setEnabled(false);
                saveSegment.setEnabled(false);
            }
        });


    }

    TextWatcher watcher=new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            String title,loc,cost,dur,capacity;
            title=packageTitle.getText().toString().trim();
            loc=location.getText().toString().trim();
            cost=price.getText().toString().trim();
            dur=duration.getText().toString().trim();
            capacity=touristCapacity.getText().toString().trim();

            saveInfo.setEnabled(!title.isEmpty() && !loc.isEmpty() && !cost.isEmpty() && !dur.isEmpty() && !capacity.isEmpty());

        }

        @Override
        public void afterTextChanged(Editable editable) {

        }
    };

    TextWatcher watcher2=new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            String title,details;
            title=segmentTile.getText().toString().trim();
            details=segmentDetails.getText().toString().trim();
            saveSegment.setEnabled(!title.isEmpty() && !details.isEmpty());
        }

        @Override
        public void afterTextChanged(Editable editable) {

        }
    };

    void saveData(){
        DatabaseReference ref= FirebaseDatabase.getInstance().getReference()
                .child("Package");

        HashMap<String,Object> map=new HashMap<>();

        String title,loc,cost,dur,capacity,journey;
        title=packageTitle.getText().toString().trim();
        loc=location.getText().toString().trim();
        cost=price.getText().toString().trim();
        dur=duration.getText().toString().trim();
        capacity=touristCapacity.getText().toString().trim();
        journey=journeyDate.getText().toString().trim();

        map.put("package name",title);
        map.put("location",loc);
        map.put("cost",cost);
        map.put("duration",dur);
        map.put("capacity",capacity);
        map.put("journey date",journey);
        map.put("agency id",uid);
        map.put("agency name",uName);

        key=ref.push().getKey();
        assert key != null;
        ProgressDialog progressDialog=new ProgressDialog(this);
        progressDialog.setMessage("Info Uploading...");
        progressDialog.setTitle("Basic Info");
        progressDialog.show();
        ref.child(key).updateChildren(map).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    progressDialog.dismiss();
                    Toast.makeText(AddPackage.this, "info submitted", Toast.LENGTH_SHORT).show();
                    saveImage(key);
                }
                else
                {
                    key="";
                    Toast.makeText(AddPackage.this, Objects.requireNonNull(task.getException()).toString(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    void getAgName(){
        uid= Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid();
        DatabaseReference ref=FirebaseDatabase.getInstance().getReference()
                .child("Agency").child(uid);
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                uName= Objects.requireNonNull(snapshot.child("Agency Name").getValue()).toString();
                saveData();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    void saveSegmentData(){
        DatabaseReference ref= FirebaseDatabase.getInstance().getReference()
                .child("Package");

        String title,details;
        title=segmentTile.getText().toString().trim();
        details=segmentDetails.getText().toString().trim();

        if(key.isEmpty()){
            Toast.makeText(this, "Please save basic info first.", Toast.LENGTH_SHORT).show();
        }
        else
        {
            ProgressDialog progressDialog=new ProgressDialog(this);
            progressDialog.setMessage("Info Uploading...");
            progressDialog.setTitle(title);
            progressDialog.show();
            ref.child(key).child("segment").child(title).setValue(details).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if(task.isSuccessful()){
                        progressDialog.dismiss();
                        another.setEnabled(true);
                        Toast.makeText(AddPackage.this, "Info saved", Toast.LENGTH_SHORT).show();
                    }
                    else
                    {
                        Toast.makeText(AddPackage.this, Objects.requireNonNull(task.getException()).getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }

    void testModel(){
        DatabaseReference reference=FirebaseDatabase.getInstance().getReference().child("node");

        ArrayList<TestModel> dataList=new ArrayList<>();
        HashMap<String, String> colors1=new HashMap<>();
        colors1.put("cl1","red");
        colors1.put("cl2","green");
        HashMap<String, String> colors2=new HashMap<>();
        colors1.put("cl1","blue");
        colors1.put("cl2","yellow");

        dataList.add(new TestModel("John", 25, colors1));
        dataList.add(new TestModel("Jane", 30, colors2));

        for (TestModel data : dataList) {
            // Generate a unique key for each data entry
            String key = reference.push().getKey();

            // Set the data under the generated key
            reference.child(key).setValue(data);
        }

    }

    void saveImage(String key){
        StorageReference storageReference= FirebaseStorage.getInstance().getReference().
                child("PackageImages");

        DatabaseReference ref1=FirebaseDatabase.getInstance().getReference()
                .child("Package").child(key).child("Thumbnail");

        DatabaseReference ref2=FirebaseDatabase.getInstance().getReference()
                .child("Package").child(key).child("Photos");

        ProgressDialog progressDialog=new ProgressDialog(this);
        progressDialog.setTitle("Image Uploader");
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
                        Toast.makeText(AddPackage.this, "Images uploaded", Toast.LENGTH_SHORT).show();
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
                                Toast.makeText(AddPackage.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                    else{
                        Toast.makeText(AddPackage.this, Objects.requireNonNull(task.getException()).toString(), Toast.LENGTH_SHORT).show();
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

                recyclerView.setLayoutManager(new LinearLayoutManager(AddPackage.this,RecyclerView.HORIZONTAL,false));
                recyclerView.setAdapter(adapter);
            }
            else
            {
                Toast.makeText(AddPackage.this, "No file selected", Toast.LENGTH_SHORT).show();
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
                Toast.makeText(AddPackage.this, "No file selected", Toast.LENGTH_SHORT).show();
            }
        });
    }
}