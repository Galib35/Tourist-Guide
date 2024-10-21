package com.example.ghuraghuri;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

public class WeatherAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    Context context;

    public WeatherAdapter(Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(context).inflate(R.layout.wether_element,parent,false);
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        String str=Constant.temperature.get(position)+"°C";
        ((MyViewHolder)holder).temperature.setText(str);
        str="Feels like: "+Constant.feelsLike.get(position)+"°C";
        ((MyViewHolder)holder).feel.setText(str);
        str="Humidity: "+Constant.humidity.get(position)+"%";
        ((MyViewHolder)holder).hum.setText(str);
        str="Wind: "+Constant.wind.get(position)+" m/s";
        ((MyViewHolder)holder).wind.setText(str);
        ((MyViewHolder)holder).status.setText(Constant.weatherStatus.get(position));
        ((MyViewHolder)holder).details.setText(Constant.weatherDescription.get(position));
        ((MyViewHolder)holder).date.setText(Constant.weatherDate.get(position));
        ((MyViewHolder)holder).time.setText(Constant.weatherTime.get(position));

        Glide.with(context).load(Constant.weatherIcon.get(position)).into(((MyViewHolder)holder).image);

    }

    @Override
    public int getItemCount() {
        return Constant.temperature.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{

        ImageView image;
        TextView temperature,feel,hum,wind,rain,status,time,date,details;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            image=itemView.findViewById(R.id.weather_img);
            temperature=itemView.findViewById(R.id.temp);
            status=itemView.findViewById(R.id.weather_cond);
            time=itemView.findViewById(R.id.weather_time);
            feel=itemView.findViewById(R.id.feel_temp);
            hum=itemView.findViewById(R.id.humidity);
            wind=itemView.findViewById(R.id.wind);
            rain=itemView.findViewById(R.id.rain);
            date=itemView.findViewById(R.id.weather_date);
            details=itemView.findViewById(R.id.weather_details);
        }
    }
}
