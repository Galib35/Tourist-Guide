package com.example.ghuraghuri;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class WeatherDetails extends AppCompatActivity {
    RecyclerView recyclerView;

    WeatherAdapter adapter;
    String lat,lon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather_details);

        recyclerView=findViewById(R.id.weather_recview);

        lat= getIntent().getStringExtra("lat");
        lon=getIntent().getStringExtra("lon");

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));


        moreWeatherInfo();

    }

    void moreWeatherInfo(){
        String url="https://api.openweathermap.org/data/2.5/forecast?lat="+lat+"&lon="+lon+"&appid=9dd5ca196deda19d4cbf43066a21c4f2";
        RequestQueue queue= Volley.newRequestQueue(WeatherDetails.this);
        Constant.temperature.clear();
        Constant.feelsLike.clear();
        Constant.humidity.clear();
        Constant.wind.clear();
        Constant.rain.clear();
        Constant.weatherStatus.clear();
        Constant.weatherDescription.clear();
        Constant.weatherIcon.clear();
        JsonObjectRequest request=new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {

                    JSONArray array=response.getJSONArray("list");
                    for(int i=0;i<array.length();i++){
                        JSONObject object=array.getJSONObject(i);
                        JSONObject object2=object.getJSONObject("main");

                        String temp,feels,hum,icon;
                        StringBuilder status=new StringBuilder();
                        StringBuilder details=new StringBuilder();

                        temp=object2.getString("temp");
                        feels=object2.getString("feels_like");
                        hum=object2.getString("humidity");

                        int t= (int) (Double.parseDouble(temp)-273.15);

                        Constant.temperature.add(String.valueOf(t));

                        t= (int) (Double.parseDouble(feels)-273.15);

                        Constant.feelsLike.add(String.valueOf(t));
                        Constant.humidity.add(hum);

                        JSONArray weatherArray=object.getJSONArray("weather");
                        String str;
                        for(int j=0;j<weatherArray.length();j++){
                            if(j>0)
                            {
                                status.append(", ");
                            }
                            JSONObject object3=weatherArray.getJSONObject(j);
                            status.append(object3.getString("main"));
                            details.append(object3.getString("description"));
                        }

                        System.out.println(status);
                        Constant.weatherStatus.add(status);
                        Constant.weatherDescription.add(details);

                        icon=weatherArray.getJSONObject(0).getString("icon");
                        String iconUrl="https://openweathermap.org/img/wn/"+icon+"@2x.png";
                        Constant.weatherIcon.add(iconUrl);

                        JSONObject object4=object.getJSONObject("wind");
                        String speed=object4.getString("speed");
                        Constant.wind.add(speed);

                        if(object.has("rain")){
                            //Constant.rain.add(object.getJSONObject("rain").getString("3h"));
                            if(object.getJSONObject("rain").has("3h")){
                                Constant.rain.add(object.getJSONObject("rain").getString("3h"));
                            }
                            else {
                                Constant.rain.add(object.getJSONObject("rain").getString("1h"));
                            }
                        }
                        else
                        {
                            Constant.rain.add("0 mm");
                        }

                        String dateTime=object.getString("dt_txt");
                        @SuppressLint("SimpleDateFormat") Date dateTime2=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(dateTime);
                        assert dateTime2 != null;
                        @SuppressLint("SimpleDateFormat") String date=new SimpleDateFormat("yyyy-MM-dd").format(dateTime2);
                        @SuppressLint("SimpleDateFormat") String time=new SimpleDateFormat("HH").format(dateTime2);
                        System.out.println(date);


                        int tm=Integer.parseInt(time);
                        System.out.println(tm);
                        if(tm==0)
                        {
                            time="12 AM";
                        }
                        else if(tm==12){
                            time=tm+" PM";
                        }
                        else if(tm>12){
                            tm=tm-12;
                            time=tm+" PM";
                        }
                        else
                        {
                            time=tm+" AM";
                        }
                        Constant.weatherDate.add(date);
                        Constant.weatherTime.add(time);

                    }
                    adapter=new WeatherAdapter(WeatherDetails.this);
                    recyclerView.setAdapter(adapter);
                } catch (JSONException | ParseException e) {
                    Toast.makeText(WeatherDetails.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(WeatherDetails.this, error.toString(), Toast.LENGTH_SHORT).show();
            }
        });
        queue.add(request);

    }


}