package com.example.ghuraghuri;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContract;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.WindowManager;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
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
import java.util.HashMap;
import java.util.Objects;

public class Admin extends AppCompatActivity {

    Button add_btn,update_btn,save1,save_spot,save_ht,save_rest,add_spot,add_ht,add_rest,save_hospital,save_police,
            save_bank,save_spec,add_hospital,add_police,add_bank,add_spec,browse,moreInfoSave,moreInfoAnother;
    LinearLayout add_view,update_view;
    CardView p1;
    RelativeLayout layout1;
    ImageView imageView,upload;
    RecyclerView recyclerView;

    TextInputLayout rate_edt;
    EditText place_id,place_name,place_des,cost,rating,spt_name,tag,spt_des,ht_name,ht_url,rst_name,rst_latitude,
            hospital_name,hospital_latitude,bank_name,bank_latitude,hospitalPhn,policePhn,spotAddress,zipCode,
            police_name,police_latitude,spec_category,spec_des,imgTitle,websiteName,websiteUrl;
    Dialog dialog;

    String id_code=null;
    Boolean saved_id=false;

    ActivityResultLauncher<String> getContent;
    Uri image;

    ImageAdapter adapter;
    //ArrayList<Uri> uris=new ArrayList<>();

    int ht_count=0,spt_count=0,rest_count=0,hosp_count=0,pol_count=0,bank_count=0,spec_count=0,more_count=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);

        add_btn=findViewById(R.id.add);
        update_btn=findViewById(R.id.update);
        add_view=findViewById(R.id.add_v);
        update_view=findViewById(R.id.update_v);

        place_id=findViewById(R.id.plc_id);
        place_name=findViewById(R.id.plc_name);
        place_des=findViewById(R.id.plc_descrp);
        cost=findViewById(R.id.add_cost);
        rating=findViewById(R.id.add_rating);
        rate_edt=findViewById(R.id.rt_edt);
        spt_name=findViewById(R.id.spot_name);
        spt_des=findViewById(R.id.spot_descrp);
        ht_name=findViewById(R.id.hotel_name);
        ht_url=findViewById(R.id.hotel_url);
        rst_name=findViewById(R.id.rest_name);
        rst_latitude=findViewById(R.id.rest_lat);
        //rst_longitude=findViewById(R.id.rest_long);
        hospital_name=findViewById(R.id.hosp_name);
        hospital_latitude=findViewById(R.id.hosp_lat);
        //hospital_longitude=findViewById(R.id.hosp_long);
        bank_name=findViewById(R.id.bank_name);
        bank_latitude=findViewById(R.id.bank_lat);
       // bank_longitude=findViewById(R.id.bank_long);
        police_name=findViewById(R.id.pol_name);
        police_latitude=findViewById(R.id.pol_lat);
        //police_longitude=findViewById(R.id.pol_long);
        spec_category=findViewById(R.id.sp_category);
        spec_des=findViewById(R.id.sp_descrp);
        tag=findViewById(R.id.tg);
        hospitalPhn=findViewById(R.id.hosp_phn);
        policePhn=findViewById(R.id.pol_phn);
        websiteName=findViewById(R.id.more_info_name);
        websiteUrl=findViewById(R.id.more_info_url);
        spotAddress=findViewById(R.id.spot_address);
        zipCode=findViewById(R.id.zip_code);

        save1=findViewById(R.id.plc_save_btn);
        save_spot=findViewById(R.id.spot_save_btn);
        save_ht=findViewById(R.id.hotel_save_btn);
        save_rest=findViewById(R.id.rest_save_btn);
        save_hospital=findViewById(R.id.hosp_save_btn);
        save_bank=findViewById(R.id.bank_save_btn);
        save_police=findViewById(R.id.pol_save_btn);
        save_spec=findViewById(R.id.sp_save_btn);
        moreInfoSave=findViewById(R.id.more_info_save_btn);
        moreInfoAnother=findViewById(R.id.more_info_another);

        add_spot=findViewById(R.id.spot_add_another);
        add_ht=findViewById(R.id.hotel_another);
        add_rest=findViewById(R.id.rest_another_btn);
        add_hospital=findViewById(R.id.hosp_another_btn);
        add_bank=findViewById(R.id.bank_another_btn);
        add_police=findViewById(R.id.pol_another_btn);
        add_spec=findViewById(R.id.sp_another_btn);
        upload=findViewById(R.id.spot_upload_btn);

        p1=findViewById(R.id.part1);
        layout1=findViewById(R.id.l1);
        recyclerView=findViewById(R.id.img_recView);

        adapter=new ImageAdapter(Admin.this);

        dialog=new Dialog(this);
        dialog.setContentView(R.layout.image_layout);

        WindowManager.LayoutParams lp=new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());
        lp.width=WindowManager.LayoutParams.MATCH_PARENT;
        lp.height=WindowManager.LayoutParams.WRAP_CONTENT;
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
        dialog.getWindow().setAttributes(lp);

        imgTitle=dialog.findViewById(R.id.title_dlg);
        browse=dialog.findViewById(R.id.browse_btn);

        initializePicker();


        getInformation();
        add_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent=getIntent();
                finish();
                startActivity(intent);


            }
        });

        update_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               // add_view.setVisibility(View.INVISIBLE);
                //update_view.setVisibility(View.VISIBLE);
                if(id_code!=null && saved_id)
                {
                    Intent intent=new Intent(Admin.this,TourPlan.class);
                    intent.putExtra("plc_id",id_code);
                    startActivity(intent);
                }
                else
                {
                    Toast.makeText(Admin.this, "Please save place information first", Toast.LENGTH_SHORT).show();
                }
                //layout1.setVisibility(View.GONE);
            }
        });

        websiteName.addTextChangedListener(watcher);
        websiteUrl.addTextChangedListener(watcher);
        spotAddress.addTextChangedListener(watcher);
        zipCode.addTextChangedListener(watcher);



    }



    void getInformation()
    {
        readCode(new callback() {
            @Override
            public void onCllBack(String ID) {
                place_id.setText(ID);
                id_code=ID;


            }
        });


        place_name.addTextChangedListener(watcher);
        place_des.addTextChangedListener(watcher);
        cost.addTextChangedListener(watcher);
        rating.addTextChangedListener(watcher);
        tag.addTextChangedListener(watcher);

        Constant.imgTitle.clear();
        Constant.uris.clear();

        upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.show();

                imgTitle.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                    }

                    @Override
                    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                        browse.setEnabled(!imgTitle.getText().toString().isEmpty());

                    }

                    @Override
                    public void afterTextChanged(Editable editable) {

                    }
                });

                browse.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        getContent.launch("image/*");
                        dialog.dismiss();
                    }
                });

            }
        });

        save1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                savePlaceInfo();
                Toast.makeText(Admin.this, "Information saved", Toast.LENGTH_SHORT).show();
            }
        });



        spt_name.addTextChangedListener(watcher);
        spt_des.addTextChangedListener(watcher);

        save_spot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(id_code!=null && saved_id)
                {
                    readsptCount(new callBack2() {
                        @Override
                        public void onCallback2() {
                            saveSpotInfo();
                        }
                    });

                    //Toast.makeText(Admin.this, "Information saved", Toast.LENGTH_SHORT).show();
                    add_spot.setEnabled(true);
                }
                else
                {
                    Toast.makeText(Admin.this, "No id found", Toast.LENGTH_SHORT).show();
                }

            }
        });
        add_spot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                spt_name.getText().clear();
                spt_des.getText().clear();
                add_spot.setEnabled(false);
                int size=Constant.uris.size();
                Constant.uris.clear();
                Constant.imgTitle.clear();
                adapter.notifyItemRangeRemoved(0,size);
            }
        });

        ht_name.addTextChangedListener(watcher);
        ht_url.addTextChangedListener(watcher);

        save_ht.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(id_code!=null && saved_id)
                {
                    readHtCount(new callBack2() {
                        @Override
                        public void onCallback2() {
                            saveHotelInfo();
                        }
                    });


                    Toast.makeText(Admin.this, "Information saved", Toast.LENGTH_SHORT).show();
                    add_ht.setEnabled(true);
                }
                else
                {
                    Toast.makeText(Admin.this, "No id found", Toast.LENGTH_SHORT).show();
                }


            }
        });
        add_ht.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ht_name.getText().clear();
                ht_url.getText().clear();
                add_ht.setEnabled(false);
            }
        });

        rst_name.addTextChangedListener(watcher);
        rst_latitude.addTextChangedListener(watcher);
        //rst_longitude.addTextChangedListener(watcher);

        save_rest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(id_code!=null && saved_id)
                {

                    readRestCount(new callBack2() {
                        @Override
                        public void onCallback2() {
                            saveRestaurantInfo();
                        }
                    });


                }

                else
                {
                    Toast.makeText(Admin.this, "No id found", Toast.LENGTH_SHORT).show();
                }


            }
        });
        add_rest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                rst_name.getText().clear();
                rst_latitude.getText().clear();
                //rst_longitude.getText().clear();
                //rst_url.getText().clear();
                add_rest.setEnabled(false);
            }
        });

        hospital_name.addTextChangedListener(watcher);
        hospital_latitude.addTextChangedListener(watcher);
        hospitalPhn.addTextChangedListener(watcher);
        //hospital_longitude.addTextChangedListener(watcher);

        save_hospital.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(id_code!=null && saved_id)
                {

                   readHospCount();
                   saveHospInfo();

                }
            }
        });
        add_hospital.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                hospital_name.getText().clear();
                hospital_latitude.getText().clear();
                hospitalPhn.getText().clear();
                //hospital_longitude.getText().clear();

                add_hospital.setEnabled(false);
            }
        });

        bank_name.addTextChangedListener(watcher);
        bank_latitude.addTextChangedListener(watcher);
        //bank_longitude.addTextChangedListener(watcher);

        save_bank.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(id_code!=null && saved_id)
                {
                    readBankCount();
                    saveBankInfo();

                }
            }
        });
        add_bank.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bank_name.getText().clear();
                bank_latitude.getText().clear();
               // bank_longitude.getText().clear();

                add_bank.setEnabled(false);
            }
        });

        police_name.addTextChangedListener(watcher);
        police_latitude.addTextChangedListener(watcher);
        policePhn.addTextChangedListener(watcher);
       // police_longitude.addTextChangedListener(watcher);

        save_police.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(id_code!=null && saved_id)
                {
                    readPolCount();
                    savePolInfo();

                }
            }
        });
        add_police.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                police_name.getText().clear();
                police_latitude.getText().clear();
                policePhn.getText().clear();
                //police_longitude.getText().clear();

                add_police.setEnabled(false);
            }
        });

        spec_category.addTextChangedListener(watcher);
        spec_des.addTextChangedListener(watcher);

        save_spec.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(id_code!=null && saved_id)
                {
                    readSpecCount();
                    saveSpecInfo();

                }

            }
        });
        add_spec.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                spec_category.getText().clear();
                spec_des.getText().clear();
                //police_longitude.getText().clear();

                add_spec.setEnabled(false);
            }
        });

        moreInfoSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(id_code!=null && saved_id)
                {
                   saveMoreInfo();
                }
                else
                {
                    Toast.makeText(Admin.this, "No id found", Toast.LENGTH_SHORT).show();
                }


            }
        });

        moreInfoAnother.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                websiteName.getText().clear();
                websiteUrl.getText().clear();
                websiteName.clearFocus();
                websiteUrl.clearFocus();
                moreInfoAnother.setEnabled(false);
            }
        });


    }

    TextWatcher watcher=new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            String name,des,cst,rate,plc_tag,zip,address;
            name=place_name.getText().toString().trim();
            des=place_des.getText().toString().trim();
            cst=cost.getText().toString().trim();
            rate=rating.getText().toString().trim();
            plc_tag=tag.getText().toString().trim();
            zip=zipCode.getText().toString().trim();
            address=spotAddress.getText().toString().trim();


            boolean rt_ok=false;
            rate_edt.setError(null);

            if(!rate.isEmpty())
            {
                float rt=Float.parseFloat(rate);
                if(rt<=5)
                {
                    rt_ok=true;
                }
                else
                {
                    rate_edt.setError("Ratings can't be greater than 5");
                }
            }

            save1.setEnabled(!name.isEmpty() && !des.isEmpty() && !cst.isEmpty() && !rate.isEmpty() && !plc_tag.isEmpty()
                    && !zip.isEmpty() && !address.isEmpty() && rt_ok);



            //attraction
            String description;
            name=spt_name.getText().toString().trim();
            description=spt_des.getText().toString().trim();
            save_spot.setEnabled(!name.isEmpty() && !description.isEmpty());

            //hotel
            String url;
            name=ht_name.getText().toString().trim();
            url=ht_url.getText().toString().trim();

            save_ht.setEnabled(!name.isEmpty() && !url.isEmpty());

            //restaurant
            String latitude,longitude;
            name=rst_name.getText().toString().trim();
            latitude=rst_latitude.getText().toString().trim();
            //longitude=rst_longitude.getText().toString().trim();
            save_rest.setEnabled(!name.isEmpty() && !latitude.isEmpty());

            //hospital
            name=hospital_name.getText().toString().trim();
            latitude=hospital_latitude.getText().toString().trim();
            String phn=hospitalPhn.getText().toString().trim();
            //longitude=hospital_longitude.getText().toString().trim();
            save_hospital.setEnabled(!name.isEmpty() && !latitude.isEmpty() && !phn.isEmpty());

            //bank/atm
            name=bank_name.getText().toString().trim();
            latitude=bank_latitude.getText().toString().trim();
            //longitude=bank_longitude.getText().toString().trim();
            save_bank.setEnabled(!name.isEmpty() && !latitude.isEmpty());

            //police station
            name=police_name.getText().toString().trim();
            latitude=police_latitude.getText().toString().trim();
            phn=policePhn.getText().toString().trim();
            //longitude=police_longitude.getText().toString().trim();
            save_police.setEnabled(!name.isEmpty() && !latitude.isEmpty() && !phn.isEmpty());

            //Speciality
            String cat;
            cat=spec_category.getText().toString().trim();
            description=spec_des.getText().toString().trim();
            save_spec.setEnabled(!cat.isEmpty() && !description.isEmpty());

            //more info
            name=websiteName.getText().toString().trim();
            url=websiteUrl.getText().toString().trim();
            moreInfoSave.setEnabled(!name.isEmpty() && !url.isEmpty());

        }

        @Override
        public void afterTextChanged(Editable editable) {

        }
    };



    interface callback
    {
        void  onCllBack(String ID);
    }

    void readCode(final callback cb)
    {
        DatabaseReference ref= FirebaseDatabase.getInstance().getReference().child("Place Code");

        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String id;
                int code;
                id=String.valueOf(snapshot.getValue());
                code=Integer.parseInt(id);
                code++;
                id=String.valueOf(code);
                cb.onCllBack(id);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    interface callBack2
    {
        void onCallback2();
    }

    void readHtCount(final callBack2 cb)
    {
        DatabaseReference ref=FirebaseDatabase.getInstance().getReference()
                .child("Places").child(id_code).child("Hotel");
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if(!snapshot.exists())
                {
                    ht_count=0;
                }

                else
                {

                    String cnt= Objects.requireNonNull(snapshot.child("Count").getValue()).toString();
                    ht_count=Integer.parseInt(cnt);
                }
                cb.onCallback2();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    void readsptCount(final callBack2 cb)
    {
        DatabaseReference ref=FirebaseDatabase.getInstance().getReference()
                .child("Places").child(id_code).child("Tourist Spots");
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if(!snapshot.exists())
                {
                    spt_count=0;
                }

                else
                {

                    String cnt= Objects.requireNonNull(snapshot.child("Count").getValue()).toString();
                    spt_count=Integer.parseInt(cnt);
                }
                cb.onCallback2();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    void readRestCount(final callBack2 cb)
    {
        DatabaseReference ref=FirebaseDatabase.getInstance().getReference()
                .child("Places").child(id_code).child("Restaurant");
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if(!snapshot.exists())
                {
                    rest_count=0;
                }

                else
                {

                    String cnt= Objects.requireNonNull(snapshot.child("Count").getValue()).toString();
                    rest_count=Integer.parseInt(cnt);
                }
                cb.onCallback2();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    void readHospCount()
    {
        DatabaseReference ref=FirebaseDatabase.getInstance().getReference()
                .child("Places").child(id_code).child("Hospital");

        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if(!snapshot.exists())
                {
                    hosp_count=0;
                }

                else
                {

                    String cnt= Objects.requireNonNull(snapshot.child("Count").getValue()).toString();
                    hosp_count=Integer.parseInt(cnt);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    void readBankCount()
    {
        DatabaseReference ref=FirebaseDatabase.getInstance().getReference()
                .child("Places").child(id_code).child("Bank or Atm");

        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if(!snapshot.exists())
                {
                    bank_count=0;
                }

                else
                {

                    String cnt= Objects.requireNonNull(snapshot.child("Count").getValue()).toString();
                    bank_count=Integer.parseInt(cnt);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    void readPolCount()
    {
        DatabaseReference ref=FirebaseDatabase.getInstance().getReference()
                .child("Places").child(id_code).child("Police Station");

        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if(!snapshot.exists())
                {
                    pol_count=0;
                }

                else
                {

                    String cnt= Objects.requireNonNull(snapshot.child("Count").getValue()).toString();
                    pol_count=Integer.parseInt(cnt);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    void readSpecCount()
    {
        DatabaseReference ref=FirebaseDatabase.getInstance().getReference()
                .child("Places").child(id_code).child("Speciality");

        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if(!snapshot.exists())
                {
                    spec_count=0;
                }

                else
                {

                    String cnt= Objects.requireNonNull(snapshot.child("Count").getValue()).toString();
                    spec_count=Integer.parseInt(cnt);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }




    void savePlaceInfo()
     {
        String name,des,cst,rate,plc_tag,zip,address;
        name=place_name.getText().toString().trim();
        des=place_des.getText().toString().trim();
        cst=cost.getText().toString().trim();
        rate=rating.getText().toString().trim();
        plc_tag=tag.getText().toString().trim();
        zip=zipCode.getText().toString().trim();
        address=spotAddress.getText().toString().trim();

        DatabaseReference ref=FirebaseDatabase.getInstance().getReference()
                .child("Places").child(id_code);
        //ref.setValue(id);
        ref.child("Name").setValue(name);
        ref.child("Description").setValue(des);
        ref.child("Cost").setValue(cst);
        ref.child("Ratings").setValue(rate);
        ref.child("Tag").setValue(plc_tag);
        ref.child("Total ratings").setValue("1");
        ref.child("Zip").setValue(zip);
        ref.child("address").setValue(address);


        DatabaseReference ref2=FirebaseDatabase.getInstance().getReference().child("Place Code");
        ref2.setValue(id_code);
        saved_id=true;

    }

    void saveSpotInfo()
    {
        String name,des;
        name=spt_name.getText().toString().trim();
        des=spt_des.getText().toString().trim();

        spt_count++;
        String spt_id=String.valueOf(spt_count);
       // Log.e("----", "saveSpotInfo: ");
        DatabaseReference ref=FirebaseDatabase.getInstance().getReference()
                .child("Places").child(id_code).child("Tourist Spots");

        HashMap<String,Object> map = new HashMap<>();
        map.put("Count",spt_count+"");
        map.put(spt_id+"/Name",name);
        map.put(spt_id+"/Description",des);



        ref.updateChildren(map).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful())
                {
                    Toast.makeText(Admin.this, "Information Uploaded", Toast.LENGTH_SHORT).show();
                    //progressDialog.dismiss();
                    saveImage(spt_id);
                }
            }
        });





    }

    void saveImage(String sptid)
    {
        StorageReference storageReference= FirebaseStorage.getInstance().getReference().
                child("Gallery");

        DatabaseReference ref2=FirebaseDatabase.getInstance().getReference()
                .child("Places").child(id_code).child("Tourist Spots").child(sptid).child("Images");

        DatabaseReference ref3=FirebaseDatabase.getInstance().getReference()
                .child("Places").child(id_code).child("Gallery");

        int i;
        for(i=0;i<Constant.uris.size();i++)
        {
            ProgressDialog progressDialog=new ProgressDialog(this);
            progressDialog.setTitle("Image Uploader");
            progressDialog.show();

            String title=Constant.imgTitle.get(i);
            storageReference.child(title).putFile(Constant.uris.get(i)).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                            if(task.isSuccessful())
                            {
                                progressDialog.dismiss();
                                storageReference.child(title).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                    @Override
                                    public void onSuccess(Uri uri) {

                                        String url=uri.toString();
                                        //System.out.println(url);
                                        ref2.child(title).setValue(url);
                                        ref3.child(title).setValue(url);
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(Admin.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }
                            else
                            {
                                Toast.makeText(Admin.this, Objects.requireNonNull(task.getException()).getMessage(), Toast.LENGTH_SHORT).show();
                            }

                        }
                    })
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(@NonNull UploadTask.TaskSnapshot snapshot) {
                            float percent= ((float)snapshot.getBytesTransferred()/snapshot.getTotalByteCount())*100;
                            progressDialog.setMessage("Uploaded: "+(int)percent);
                        }
                    });
        }
    }

    void saveHotelInfo()
    {
        String name,url;
        name=ht_name.getText().toString().trim();
        url=ht_url.getText().toString().trim();
        ht_count++;
        String ht_id=String.valueOf(ht_count);

        DatabaseReference ref=FirebaseDatabase.getInstance().getReference()
                .child("Places").child(id_code).child("Hotel");

        ref.child("Count").setValue(ht_count);
        ref.child(ht_id).child("Name").setValue(name);
        ref.child(ht_id).child("URL").setValue(url);
    }

    void saveRestaurantInfo()
    {
        String name,latitude,longitude;
        name=rst_name.getText().toString().trim();
        latitude=rst_latitude.getText().toString().trim();
        //longitude=rst_longitude.getText().toString().trim();

        rest_count++;
        String rest_id=String.valueOf(rest_count);

        DatabaseReference ref=FirebaseDatabase.getInstance().getReference()
                .child("Places").child(id_code).child("Restaurant");

        ref.child("Count").setValue(rest_count);

        SaveInfo saveInfo=new SaveInfo();
        saveInfo.setName(name);
        saveInfo.setURL(latitude);
       // saveInfo.setLongitude(longitude);
        ref.child(rest_id).setValue(saveInfo).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful())
                {
                    Toast.makeText(Admin.this, "Information saved", Toast.LENGTH_SHORT).show();
                    add_rest.setEnabled(true);
                }
                else
                {
                    Toast.makeText(Admin.this, Objects.requireNonNull(task.getException()).getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    void saveHospInfo()
    {
        String name,latitude,phn;
        name=hospital_name.getText().toString().trim();
        latitude=hospital_latitude.getText().toString().trim();
        phn=hospitalPhn.getText().toString().trim();
        //longitude=hospital_longitude.getText().toString().trim();

        hosp_count++;
        String hosp_id=String.valueOf(hosp_count);

        DatabaseReference ref=FirebaseDatabase.getInstance().getReference()
                .child("Places").child(id_code).child("Hospital");

        ref.child("Count").setValue(hosp_count);

        HosPolData saveInfo=new HosPolData();
        saveInfo.setName(name);
        saveInfo.setURL(latitude);
        saveInfo.setPhone(phn);
        //saveInfo.setLongitude(longitude);
        //ref.child(hosp_id).child("phone").setValue(phn);
        ref.child(hosp_id).setValue(saveInfo).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful())
                {
                    Toast.makeText(Admin.this, "Information saved", Toast.LENGTH_SHORT).show();
                    add_hospital.setEnabled(true);
                }
                else
                {
                    Toast.makeText(Admin.this, Objects.requireNonNull(task.getException()).getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    void saveBankInfo()
    {
        String name,latitude,longitude;
        name=bank_name.getText().toString().trim();
        latitude=bank_latitude.getText().toString().trim();
        //longitude=bank_longitude.getText().toString().trim();

        bank_count++;
        String bank_id=String.valueOf(bank_count);

        DatabaseReference ref=FirebaseDatabase.getInstance().getReference()
                .child("Places").child(id_code).child("Bank or Atm");

        ref.child("Count").setValue(bank_count);

        SaveInfo saveInfo=new SaveInfo();
        saveInfo.setName(name);
        saveInfo.setURL(latitude);
        //saveInfo.setLongitude(longitude);
        ref.child(bank_id).setValue(saveInfo).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful())
                {
                    Toast.makeText(Admin.this, "Information saved", Toast.LENGTH_SHORT).show();
                    add_bank.setEnabled(true);
                }
                else
                {
                    Toast.makeText(Admin.this, Objects.requireNonNull(task.getException()).getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    void savePolInfo()
    {
        String name,latitude,phn;
        name=police_name.getText().toString().trim();
        latitude=police_latitude.getText().toString().trim();
        phn=policePhn.getText().toString().trim();
        //longitude=police_longitude.getText().toString().trim();

        pol_count++;
        String pol_id=String.valueOf(pol_count);

        DatabaseReference ref=FirebaseDatabase.getInstance().getReference()
                .child("Places").child(id_code).child("Police Station");

        ref.child("Count").setValue(pol_count);

        HosPolData saveInfo=new HosPolData();
        saveInfo.setName(name);
        saveInfo.setURL(latitude);
        saveInfo.setPhone(phn);
        //saveInfo.setLongitude(longitude);

        //ref.child(pol_id).child("phone").setValue(phn);
        ref.child(pol_id).setValue(saveInfo).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful())
                {
                    Toast.makeText(Admin.this, "Information saved", Toast.LENGTH_SHORT).show();
                    add_police.setEnabled(true);
                }
                else
                {
                    Toast.makeText(Admin.this, Objects.requireNonNull(task.getException()).getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    void saveSpecInfo()
    {
        String cat,des;
        cat=spec_category.getText().toString().trim();
        des=spec_des.getText().toString().trim();

        spec_count++;
        String spec_id=String.valueOf(spec_count);


        DatabaseReference ref=FirebaseDatabase.getInstance().getReference()
                .child("Places").child(id_code).child("Speciality");

        ref.child("Count").setValue(spec_count);

        SpecialInfo specialInfo=new SpecialInfo();
        specialInfo.setCategory(cat);
        specialInfo.setDescription(des);

        ref.child(spec_id).setValue(specialInfo).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful())
                {
                    Toast.makeText(Admin.this, "Information saved", Toast.LENGTH_SHORT).show();
                    add_spec.setEnabled(true);
                }
                else
                {
                    Toast.makeText(Admin.this, Objects.requireNonNull(task.getException()).getMessage(), Toast.LENGTH_SHORT).show();
                }

            }
        });

    }

    void saveMoreInfo(){
        String name,url;
        name=websiteName.getText().toString().trim();
        url=websiteUrl.getText().toString().trim();

        DatabaseReference ref=FirebaseDatabase.getInstance().getReference()
                .child("Places").child(id_code).child("More");

        //ref.child("Count").setValue(more_count);
        ref.child(name).setValue(url);

        Toast.makeText(Admin.this, "Information saved", Toast.LENGTH_SHORT).show();
        moreInfoAnother.setEnabled(true);
    }

    void initializePicker()
    {
        getContent=registerForActivityResult(new ActivityResultContracts.GetContent(),uri-> {

            if(uri!=null)
            {
                image=uri;
                Constant.uris.add(image);
                Constant.imgTitle.add(imgTitle.getText().toString());

                recyclerView.setLayoutManager(new LinearLayoutManager(Admin.this,RecyclerView.HORIZONTAL,false));
                recyclerView.setAdapter(adapter);
            }
            else
            {
                Toast.makeText(Admin.this, "No file selected", Toast.LENGTH_SHORT).show();
            }
        });
    }

    String getFileExtention()
    {
        ContentResolver cr=getContentResolver();
        MimeTypeMap mime=MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cr.getType(image));
    }
}