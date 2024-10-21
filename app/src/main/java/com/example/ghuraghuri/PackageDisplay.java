package com.example.ghuraghuri;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
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
import com.smarteist.autoimageslider.IndicatorView.animation.type.IndicatorAnimationType;
import com.smarteist.autoimageslider.SliderAnimations;
import com.smarteist.autoimageslider.SliderView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Objects;

public class PackageDisplay extends AppCompatActivity {

    TextView title,location,price,duration,email,mobile,whatsapp,agencyName;
    EditText journeyDate,touristNo;
    Button book;
    SliderView slide;
    LinearLayout container;

    String key;
    ProductImageAdapter adapter;
    int index;
    ArrayList<String> packageTitle=new ArrayList<>();
    ArrayList<String> packageDetails=new ArrayList<>();

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_package_display);

        title=findViewById(R.id.pcg_name);
        location=findViewById(R.id.pcg_loc);
        price=findViewById(R.id.pcg_price);
        duration=findViewById(R.id.pcg_dur);
        email=findViewById(R.id.email_pcg);
        mobile=findViewById(R.id.mobile_no);
        whatsapp=findViewById(R.id.whatsapp_no);
        journeyDate=findViewById(R.id.pcg_journey_date);
        book=findViewById(R.id.book_btn);
        agencyName=findViewById(R.id.pcg_agency_name);
        slide=findViewById(R.id.imgSlider_pcg_dis);
        container=findViewById(R.id.container);
        touristNo=findViewById(R.id.pcg_tourist_no);

        index= getIntent().getIntExtra("ind",0);
        key=Constant.packageId.get(index);
        //System.out.println(key);
        adapter=new ProductImageAdapter(this);

        slide.setIndicatorAnimation(IndicatorAnimationType.WORM);
        slide.setSliderTransformAnimation(SliderAnimations.DEPTHTRANSFORMATION);

        getImage();
        getContactInfo();
        getSegment();
        setInfo();

        System.out.println(Constant.role);

        if(Constant.role.equals("User")){
            book.setVisibility(View.VISIBLE);
        }
        else {
            book.setVisibility(View.GONE);
        }

        if(Constant.journeyDate.get(index).equals("Negotiable")){
           journeyDate.setEnabled(true);
           journeyDate.setText("Select Date");
        }
        else
        {
            journeyDate.setEnabled(false);
            journeyDate.setText(Constant.journeyDate.get(index));
        }

        journeyDate.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                Calendar calendar = Calendar.getInstance();
                int year = calendar.get(Calendar.YEAR);
                int month = calendar.get(Calendar.MONTH);
                int dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);


                DatePickerDialog datePickerDialog=new DatePickerDialog(PackageDisplay.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int selectedYear, int monthOfYear, int dayOfMonth) {
                        String selectedDate = dayOfMonth + "/" + (monthOfYear + 1) + "/" + selectedYear;
                        journeyDate.setText(selectedDate);

                    }
                }, year, month, dayOfMonth);
                datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
                datePickerDialog.show();
            }
        });

        book.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String uid= Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid();
                DatabaseReference ref=FirebaseDatabase.getInstance().getReference()
                        .child("Booking");
                DatabaseReference ref2=FirebaseDatabase.getInstance().getReference()
                        .child("User").child(uid).child("Booking");
                String bookId=ref.push().getKey();

                String jr_date=journeyDate.getText().toString().trim();
                String num=touristNo.getText().toString().trim();

                if(jr_date.equals("Select Date")){
                    Toast.makeText(PackageDisplay.this, "Please select journey date", Toast.LENGTH_SHORT).show();
                }
                else if(num.isEmpty())
                {
                    Toast.makeText(PackageDisplay.this, "Please select tourist number", Toast.LENGTH_SHORT).show();
                }
                else {
                    HashMap<String, Object> map=new HashMap<>();

                    Date date= Calendar.getInstance().getTime();
                    SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());
                    SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm", Locale.getDefault());

                    // Format the current date and time
                    String formattedDate = dateFormat.format(date);
                    String formattedTime = timeFormat.format(date);
                    //System.out.println(formattedDateTime);

                    map.put("user_id",uid);
                    map.put("package_id",key);
                    map.put("journey_date",jr_date);
                    map.put("tourist_number",num);
                    map.put("status","Pending");
                    map.put("date",formattedDate);
                    map.put("time",formattedTime);

                    assert bookId != null;
                    ref.child(bookId).updateChildren(map).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful()){
                                Toast.makeText(PackageDisplay.this, "Your request is submitted", Toast.LENGTH_SHORT).show();
                            }
                            else {
                                Toast.makeText(PackageDisplay.this, Objects.requireNonNull(task.getException()).getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    });

                    HashMap<String,Object> map2=new HashMap<>();
                    map2.put("status","Pending");
                    map2.put("message","Please wait for confirmation of agency.");

                    ref2.child(bookId).updateChildren(map2).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()){
                                Toast.makeText(PackageDisplay.this, "Please wait for confirmation of agency.", Toast.LENGTH_SHORT).show();
                            }
                            else {
                                Toast.makeText(PackageDisplay.this, Objects.requireNonNull(task.getException()).getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }




            }
        });

        touristNo.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                String num;
                num=touristNo.getText().toString().trim();
                int n;
                if(!num.isEmpty()){
                    n=Integer.parseInt(num);
                    if(n<=0){
                        touristNo.setText("1");
                    }
                }
                

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }

    void getImage(){

        DatabaseReference ref= FirebaseDatabase.getInstance().getReference()
                .child("Package").child(key).child("Photos");

        Constant.productImg.clear();

        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot img:snapshot.getChildren()){
                    Constant.productImg.add(Objects.requireNonNull(img.getValue()).toString());
                    //System.out.println(Objects.requireNonNull(img.getValue()));
                }
                slide.setSliderAdapter(adapter);
                slide.startAutoCycle();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    void getContactInfo(){
        String uid = Constant.packageAgencyId.get(index);
        DatabaseReference ref= FirebaseDatabase.getInstance().getReference()
                .child("Agency").child(uid);


        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                email.setText("Email: "+ Objects.requireNonNull(snapshot.child("Email").getValue()));
                mobile.setText("Phone No: "+ Objects.requireNonNull(snapshot.child("Contact no").getValue()));
                whatsapp.setText("Whatsapp: "+ Objects.requireNonNull(snapshot.child("Whatsapp").getValue()));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    void getSegment(){
        DatabaseReference ref= FirebaseDatabase.getInstance().getReference()
                .child("Package").child(key).child("segment");

        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot snapshot1: snapshot.getChildren()){
                    packageTitle.add(snapshot1.getKey());
                    packageDetails.add(Objects.requireNonNull(snapshot1.getValue()).toString());
                }
                setSegment();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @SuppressLint("SetTextI18n")
    void setInfo(){
        title.setText(Constant.packageName.get(index));
        location.setText(Constant.packageLocation.get(index));
        price.setText(Constant.packageCost.get(index)+"tk");
        duration.setText(Constant.packageDuration.get(index));
        agencyName.setText(Constant.packageAgencyName.get(index));

    }

    void setSegment(){
        for(int i=0;i<packageTitle.size();i++){

            CardView cardView=new CardView(this);

            cardView.setLayoutParams(new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
            ));
            ViewGroup.MarginLayoutParams cardViewMarginParams = (ViewGroup.MarginLayoutParams) cardView.getLayoutParams();
            cardViewMarginParams.setMargins(0, 0, 0, 40);
            cardView.requestLayout();

            cardView.setCardElevation(10);
            cardView.setRadius(15);


            LinearLayout linearLayout = new LinearLayout(this);
            linearLayout.setLayoutParams(new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.MATCH_PARENT
            ));
            linearLayout.setOrientation(LinearLayout.VERTICAL);
            linearLayout.setBackgroundResource(R.drawable.places_background);

            TextView textView = new TextView(this);
            textView.setLayoutParams(new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
            ));

            textView.setTextColor(getResources().getColor(R.color.black));
            textView.setTextSize(20);
            textView.setTypeface(null, Typeface.BOLD);

            LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) textView.getLayoutParams();
            params.setMargins(10,10,10,10);
            textView.setLayoutParams(params);

            textView.setText(packageTitle.get(i));

            TextView textView2 = new TextView(this);
            textView2.setLayoutParams(new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
            ));

            textView2.setTextColor(getResources().getColor(R.color.black));
            textView2.setTextSize(18);


            LinearLayout.LayoutParams params2 = (LinearLayout.LayoutParams) textView.getLayoutParams();
            params2.setMargins(10,0,10,10);
            textView2.setLayoutParams(params);

            textView2.setText(packageDetails.get(i));

            linearLayout.addView(textView);
            linearLayout.addView(textView2);

            cardView.addView(linearLayout);
            container.addView(cardView);
        }
    }
}