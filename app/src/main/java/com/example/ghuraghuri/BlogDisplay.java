package com.example.ghuraghuri;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.database.snapshot.Index;
import com.smarteist.autoimageslider.IndicatorView.animation.type.IndicatorAnimationType;
import com.smarteist.autoimageslider.SliderAnimations;
import com.smarteist.autoimageslider.SliderView;

import java.util.Objects;

public class BlogDisplay extends AppCompatActivity {
    SliderView slide;
    TextView title,name,details,date,location;

    ProductImageAdapter adapter;
    String key;
    int index;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_blog_display);

        slide=findViewById(R.id.imgSlider_blog_details);
        title=findViewById(R.id.blog_title);
        name=findViewById(R.id.blogger);
        details=findViewById(R.id.blog_description);
        date=findViewById(R.id.blog_date);
        location=findViewById(R.id.blog_loc);

        adapter=new ProductImageAdapter(this);
        slide.setIndicatorAnimation(IndicatorAnimationType.WORM);
        slide.setSliderTransformAnimation(SliderAnimations.DEPTHTRANSFORMATION);

        index= getIntent().getIntExtra("blog_index",0);

        key=Constant.blogId.get(index);
        getImage();

    }

    void getImage()
    {
        DatabaseReference ref= FirebaseDatabase.getInstance().getReference()
                .child("Blog").child(key).child("Photos");
        Constant.productImg.clear();
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot img:snapshot.getChildren()){
                    Constant.productImg.add(Objects.requireNonNull(img.getValue()).toString());
                }
                setInfo();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @SuppressLint("SetTextI18n")
    void setInfo(){
        slide.setSliderAdapter(adapter);
        slide.startAutoCycle();

        title.setText(Constant.blogTitle.get(index));
        location.setText(Constant.blogLocation.get(index));
        name.setText("-By "+Constant.bloggerName.get(index));
        date.setText(Constant.blogDate.get(index));
        details.setText(Constant.blogDetails.get(index));
    }

}