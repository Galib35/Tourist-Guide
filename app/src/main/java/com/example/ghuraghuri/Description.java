package com.example.ghuraghuri;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.annotation.SuppressLint;
import android.app.DownloadManager;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Objects;

public class Description extends AppCompatActivity {
    TextView place_name,description,cost,rating,temperature,feel,humidity,weatherStatus,weatherDescription;
    GridLayout gridLayout;
    ProgressBar progressBar;
    RatingBar ratingBar;
    Button go;
    ImageView weatherIcon;
    CardView weather;

    String place_id,name,plc_name,lat,lon,zip;

    FirebaseAuth auth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_description);

        place_name=findViewById(R.id.des_name);
        description=findViewById(R.id.des_description);
        cost=findViewById(R.id.des_tk);
        gridLayout=findViewById(R.id.grid);
        progressBar=findViewById(R.id.pr_des);
        rating=findViewById(R.id.rating_text);
        ratingBar=findViewById(R.id.rating_icon);
        go=findViewById(R.id.go_btn);
        temperature=findViewById(R.id.curr_temp);
        feel=findViewById(R.id.feel_temp);
        humidity=findViewById(R.id.humidity);
        weatherStatus=findViewById(R.id.weather_cond);
        weatherDescription=findViewById(R.id.weather_desc);
        weatherIcon=findViewById(R.id.weather_icon);
        weather=findViewById(R.id.weather_layout);


        place_id=getIntent().getStringExtra("id");
        auth=FirebaseAuth.getInstance();

        progressBar.setVisibility(View.VISIBLE);
        getInfo();

        setCardEvent(gridLayout);

        go.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String uid= Objects.requireNonNull(auth.getCurrentUser()).getUid();
                DatabaseReference ref=FirebaseDatabase.getInstance().getReference().child("User")
                        .child(uid).child("History");

                Calendar calendar=Calendar.getInstance();
                @SuppressLint("SimpleDateFormat") SimpleDateFormat simpleDateFormat=new SimpleDateFormat("d MMM, yyyy");

                String date=simpleDateFormat.format(calendar.getTime());

                HashMap<String,Object> map = new HashMap<>();

                map.put(place_id+"/name",name);
                map.put(place_id+"/date",date);

                ref.updateChildren(map).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful())
                        {
                            Intent intent=new Intent(Description.this,Plan.class);
                            intent.putExtra("plc_id",place_id);
                            startActivity(intent);
                        }
                        else
                        {
                            Toast.makeText(Description.this, Objects.requireNonNull(task.getException()).getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });

            }
        });



        weather.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(Description.this,WeatherDetails.class);
                intent.putExtra("lat",lat);
                intent.putExtra("lon",lon);
                startActivity(intent);
            }
        });
    }

    void getInfo()
    {

        readData(new callBack() {
            @Override
            public void onCallback(String nm, String des, String cst) {
                name=nm;
                place_name.setText(nm);
                description.setText(des);
                cst+=" TK";
                cost.setText(cst);
                progressBar.setVisibility(View.GONE);
                weatherInfo();
            }
        });

    }

    interface callBack
    {
        void onCallback(String nm,String des,String cst);
    }

    void readData(final callBack cb)
    {

        DatabaseReference ref= FirebaseDatabase.getInstance().getReference().child("Places").child(place_id);


        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String nm,des,cst;
                nm=String.valueOf(snapshot.child("Name").getValue());
                des=String.valueOf(snapshot.child("Description").getValue());
                cst=String.valueOf(snapshot.child("Cost").getValue());
                zip=String.valueOf(snapshot.child("Zip").getValue());

                Constant.totalRatings= Objects.requireNonNull(snapshot.child("Total ratings").getValue()).toString();
                float rt=Float.parseFloat(String.valueOf(snapshot.child("Ratings").getValue()));

                ratingBar.setRating(rt/5);
                Constant.rat= new DecimalFormat("##.#").format(rt);

                int x=Integer.parseInt(Constant.totalRatings);
                x=x-1;
                String txt;
                txt=Constant.rat+"/5"+" ("+String.valueOf(x)+")";
                rating.setText(txt);

                cb.onCallback(nm,des,cst);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    void setCardEvent(GridLayout layout)
    {
        int i;
        for(i=0;i<layout.getChildCount();i++)
        {
            CardView cardView=(CardView)layout.getChildAt(i);
            final int cnt=i;
            cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent;

                    switch (cnt)
                    {

                        case 0:
                            intent=new Intent(Description.this,Gallery.class);
                            intent.putExtra("plc_id",place_id);
                            startActivity(intent);
                            //Toast.makeText(Description.this, "Gallery Coming Soon", Toast.LENGTH_SHORT).show();
                            break;

                        case 1:
                            intent=new Intent(Description.this,ShowReview.class);
                            intent.putExtra("plc_id",place_id);
                            startActivity(intent);
                            //Toast.makeText(Description.this, "Rating Coming Soon", Toast.LENGTH_SHORT).show();
                            break;

                        case 2:
                            intent=new Intent(Description.this,Plan.class);
                            intent.putExtra("plc_id",place_id);
                            startActivity(intent);
                            //Toast.makeText(Description.this, "Tour Plan Coming Soon", Toast.LENGTH_SHORT).show();
                            break;

                        case 3:
                            Constant.point='a';
                            intent=new Intent(Description.this,MapsAttraction.class);
                            intent.putExtra("plc_id",place_id);
                            startActivity(intent);
                            break;

                        case 4:
                            Constant.point='h';
                            intent=new Intent(Description.this,MapsActivity.class);
                            intent.putExtra("plc_id",place_id);
                            intent.putExtra("plc_name",name);
                            startActivity(intent);
                            break;

                        case 5:
                            Constant.point='r';
                            intent=new Intent(Description.this,MapsActivity.class);
                            intent.putExtra("plc_id",place_id);
                            startActivity(intent);
                            break;

                        case 6:
                            Constant.point='b';
                            intent=new Intent(Description.this,BankDisplay.class);
                            intent.putExtra("plc_id",place_id);
                            startActivity(intent);
                            //Toast.makeText(Description.this, "ATM/Bank comming Soon", Toast.LENGTH_SHORT).show();
                            break;

                        case 7:
                            Constant.point='l';
                            intent=new Intent(Description.this,MapsHosPol.class);
                            intent.putExtra("plc_id",place_id);
                            startActivity(intent);
                            //Toast.makeText(Description.this, "Hospital Coming Soon", Toast.LENGTH_SHORT).show();
                            break;
                        case 8:
                            Constant.point='p';
                            intent=new Intent(Description.this,MapsHosPol.class);
                            intent.putExtra("plc_id",place_id);
                            startActivity(intent);
                            //Toast.makeText(Description.this, "Police station Coming Soon", Toast.LENGTH_SHORT).show();
                            break;
                        case 9:
                            Constant.point='s';
                            intent=new Intent(Description.this,SpecialDisplay.class);
                            intent.putExtra("plc_id",place_id);
                            startActivity(intent);
                            //Toast.makeText(Description.this, "Speciality Coming Soon", Toast.LENGTH_SHORT).show();
                            break;
                        case 10:
                            Constant.point='m';
                            intent=new Intent(Description.this,WebViewList.class);
                            intent.putExtra("plc_id",place_id);
                            startActivity(intent);
                            //Toast.makeText(Description.this, "Speciality Coming Soon", Toast.LENGTH_SHORT).show();
                            break;
                    }

                }
            });

        }
    }

    void weatherInfo(){
        String url="https://api.openweathermap.org/data/2.5/weather?zip="+zip+",BD&appid=9dd5ca196deda19d4cbf43066a21c4f2";
        RequestQueue queue= Volley.newRequestQueue(Description.this);

        JsonObjectRequest request=new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONObject object=response.getJSONObject("main");
                    String temp,feels,hum,icon;
                    StringBuilder status=new StringBuilder();
                    StringBuilder details=new StringBuilder();

                    temp=object.getString("temp");
                    feels=object.getString("feels_like");
                    hum=object.getString("humidity");

                    int t= (int) (Double.parseDouble(temp)-273.15);
                    temp=t+"°C";
                    temperature.setText(temp);
                    t= (int) (Double.parseDouble(feels)-273.15);
                    feels="Feels like: "+t+"°C";
                    feel.setText(feels);
                    hum="Humidity: "+hum+"%";
                    humidity.setText(hum);


                    JSONArray array=response.getJSONArray("weather");
                    for(int i=0;i<array.length();i++){
                        if(i>0)
                        {
                            status.append(", ");
                        }
                        JSONObject object2=array.getJSONObject(i);
                        status.append(object2.getString("main"));
                        details.append(object2.getString("description"));
                        /*if(object2.has("main")){
                            System.out.println(object2.getString("main"));
                        }*/


                    }
                    JSONObject object3=response.getJSONObject("coord");
                    lat=object3.getString("lat");
                    lon=object3.getString("lon");

                    icon=array.getJSONObject(0).getString("icon");
                    String iconUrl="https://openweathermap.org/img/wn/"+icon+"@2x.png";
                    Glide.with(Description.this).load(iconUrl).into(weatherIcon);
                    weatherStatus.setText(status);
                    weatherDescription.setText(details);
                    //weatherIcon.setImageResource(R.drawable.sunny);
                } catch (JSONException e) {
                    Toast.makeText(Description.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(Description.this, error.toString(), Toast.LENGTH_SHORT).show();
            }
        });
        queue.add(request);
    }


}