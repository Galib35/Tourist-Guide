package com.example.ghuraghuri;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.denzcoskun.imageslider.ImageSlider;
import com.denzcoskun.imageslider.models.SlideModel;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.smarteist.autoimageslider.SliderView;

import java.util.ArrayList;
import java.util.Objects;

public class AttractionsDisplay extends AppCompatActivity {

    TextView name,des,noImg;
    SliderView imageSlider;
    SliderAdapter adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attractions_display);

        name=findViewById(R.id.spt_dis_name);
        des=findViewById(R.id.spt_dis_des);
        imageSlider=findViewById(R.id.imgSlider);
        noImg=findViewById(R.id.no_img);

        String pos=getIntent().getStringExtra("index");
        int index=Integer.parseInt(pos);

        name.setText(Constant.spot_name.get(index));
        des.setText(Constant.spot_description.get(index));

        getImage(index);
    }

    void getImage(int index)
    {
        if(Constant.mp.isEmpty())
        {
            noImg.setVisibility(View.VISIBLE);
        }
        else
        {
            noImg.setVisibility(View.GONE);
            String key=String.valueOf(index+1);
            adapter=new SliderAdapter(this,key);
            imageSlider.setSliderAdapter(adapter);
        }


    }
}