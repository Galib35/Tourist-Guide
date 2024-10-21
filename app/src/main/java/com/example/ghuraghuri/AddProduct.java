package com.example.ghuraghuri;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContract;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class AddProduct extends AppCompatActivity {

    EditText name,quantity,price,feature,description;
    ImageView browseThumb,browsePhotos,imageDisplay;
    RecyclerView recyclerView;
    Button save;

    ActivityResultLauncher<String> getThumb,getContent;
    Uri image,thumb;
    ImageAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_product);

        name=findViewById(R.id.pdct_name);
        quantity=findViewById(R.id.quantity_pdct);
        price=findViewById(R.id.price_pdct);
        feature=findViewById(R.id.feature_pdct);
        description=findViewById(R.id.description_pdct);
        browseThumb=findViewById(R.id.browse1);
        browsePhotos=findViewById(R.id.browse2);
        imageDisplay=findViewById(R.id.img_dis);
        recyclerView=findViewById(R.id.img_recView_pdct);
        save=findViewById(R.id.save_btn_pdct);

        adapter=new ImageAdapter(AddProduct.this);
        Constant.uris.clear();
        initializePickerThumb();
        initializePicker();

        //textwatcher
        name.addTextChangedListener(watcher);
        quantity.addTextChangedListener(watcher);
        price.addTextChangedListener(watcher);
        feature.addTextChangedListener(watcher);
        description.addTextChangedListener(watcher);


        browseThumb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getThumb.launch("image/*");
            }
        });
        browsePhotos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getContent.launch("image/*");
            }
        });

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(imageDisplay.getDrawable()==null || Constant.uris.size()==0){
                    Toast.makeText(AddProduct.this, "Please, add Images", Toast.LENGTH_SHORT).show();
                }
                else{
                    Constant.uris.add(thumb);
                    saveInfo();
                }
            }
        });


    }

    TextWatcher watcher=new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            String pdctName,qnt,prc,desc,feat;
            pdctName=name.getText().toString().trim();
            qnt=quantity.getText().toString().trim();
            prc=price.getText().toString().trim();
            desc=description.getText().toString().trim();
            feat=feature.getText().toString().trim();

            save.setEnabled(!pdctName.isEmpty() && !qnt.isEmpty() && !prc.isEmpty() && !desc.isEmpty() && !feat.isEmpty());

        }

        @Override
        public void afterTextChanged(Editable editable) {

        }
    };

    void initializePickerThumb()
    {
        getThumb=registerForActivityResult(new ActivityResultContracts.GetContent(), uri-> {

            if(uri!=null)
            {
                thumb=uri;
                imageDisplay.setImageURI(thumb);
            }
            else
            {
                Toast.makeText(AddProduct.this, "No file selected", Toast.LENGTH_SHORT).show();
            }
        });
    }
    void initializePicker()
    {
        getContent=registerForActivityResult(new ActivityResultContracts.GetContent(),uri-> {

            if(uri!=null)
            {
                image=uri;
                Constant.uris.add(image);

                recyclerView.setLayoutManager(new LinearLayoutManager(AddProduct.this,RecyclerView.HORIZONTAL,false));
                recyclerView.setAdapter(adapter);
            }
            else
            {
                Toast.makeText(AddProduct.this, "No file selected", Toast.LENGTH_SHORT).show();
            }
        });
    }

    void saveInfo(){
        String pdctName,qnt,prc,desc,feat;
        pdctName=name.getText().toString().trim();
        qnt=quantity.getText().toString().trim();
        prc=price.getText().toString().trim();
        desc=description.getText().toString().trim();
        feat=feature.getText().toString().trim();

        DatabaseReference ref= FirebaseDatabase.getInstance().getReference()
                .child("Product");
        ProductModel model=new ProductModel(pdctName,qnt,prc,feat,desc);
        String key= ref.push().getKey();
        assert key != null;
        ref.child(key).setValue(model).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful())
                {
                    saveProductImage(key);
                }
            }
        });

    }

    void saveProductImage(String key){
        StorageReference storageReference= FirebaseStorage.getInstance().getReference().
                child("ProductImages");

        DatabaseReference ref1=FirebaseDatabase.getInstance().getReference()
                .child("Product").child(key).child("Thumbnail");

        DatabaseReference ref2=FirebaseDatabase.getInstance().getReference()
                .child("Product").child(key).child("Photos");


       /* storageReference.child("Thumbnail").child(key).putFile(thumb).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                if(task.isSuccessful()){
                    progressDialog.dismiss();
                    storageReference.child("Thumbnail").child(key).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            String url=uri.toString();
                            ref1.setValue(url);
                            Toast.makeText(AddProduct.this, "Thumbnail uploaded", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onProgress(@NonNull UploadTask.TaskSnapshot snapshot) {
                float percent= ((float)snapshot.getBytesTransferred()/snapshot.getTotalByteCount())*100;
                progressDialog.setMessage("Uploaded: "+(int)percent);
            }
        });*/

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
                        storageReference.child(key).child(title).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                String url=uri.toString();
                                ref2.child(title).setValue(url);

                                if(finalI ==Constant.uris.size()-1){
                                    ref1.setValue(url);
                                }
                            }
                        });
                    }

                }
            }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onProgress(@NonNull UploadTask.TaskSnapshot snapshot) {
                    float percent= ((float)snapshot.getBytesTransferred()/snapshot.getTotalByteCount())*100;
                    progressDialog.setMessage("Uploaded: "+(int)percent);
                }
            });
        }
    }
}