package com.example.ghuraghuri;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.Calendar;
import java.util.Date;

public class Agency extends AppCompatActivity {

    CardView addPackage,booking,packageList;
    ImageView settings;
    RelativeLayout settingLayout;
    TextView account,logOut;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agency);

        addPackage=findViewById(R.id.add_pcg);
        booking=findViewById(R.id.pcg_boobking);
        packageList=findViewById(R.id.pcg_list);
        settings=findViewById(R.id.settings);
        settingLayout=findViewById(R.id.settings_layout);
        account=findViewById(R.id.agency_account);
        logOut=findViewById(R.id.agency_logout);

        settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (settingLayout.getVisibility()==View.VISIBLE){
                    settingLayout.setVisibility(View.GONE);
                }
                else if (settingLayout.getVisibility()==View.GONE){
                    settingLayout.setVisibility(View.VISIBLE);
                }

            }
        });

        logOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences preferences=getSharedPreferences("checkbox",MODE_PRIVATE);
                SharedPreferences.Editor edit=preferences.edit();
                edit.putBoolean("rem_ag",false);
                edit.apply();

                Intent intent=new Intent(Agency.this,SignIn.class);
                intent.putExtra("from_agency",true);
                startActivity(intent);
            }
        });

        addPackage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(Agency.this,AddPackage.class);
                startActivity(intent);
            }
        });

        packageList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(Agency.this,PackageListUser.class);
                startActivity(intent);
            }
        });

        booking.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(Agency.this,Bookings.class);
                startActivity(intent);
            }
        });
    }

    @Override
    public void onBackPressed() {
        finishAffinity();
    }
}