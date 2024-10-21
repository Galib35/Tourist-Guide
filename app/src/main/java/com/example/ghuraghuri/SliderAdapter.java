package com.example.ghuraghuri;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.smarteist.autoimageslider.SliderViewAdapter;

import java.util.Objects;

public class SliderAdapter extends SliderViewAdapter<SliderAdapter.SliderAdapterVH> {
    Context context;
    String key;

    public SliderAdapter(Context context,String key) {
        this.context = context;
        this.key=key;
    }

    @Override
    public SliderAdapter.SliderAdapterVH onCreateViewHolder(ViewGroup parent) {
        View v=LayoutInflater.from(context).inflate(R.layout.slider_item,parent,false);
        return new SliderAdapterVH(v);
    }

    @Override
    public void onBindViewHolder(SliderAdapter.SliderAdapterVH viewHolder, int position) {

        String url= Objects.requireNonNull(Constant.mp.get(key)).get(position);
        Glide.with(viewHolder.itemView).load(url).into(viewHolder.img);
    }

    @Override
    public int getCount() {
        return Objects.requireNonNull(Constant.mp.get(key)).size();
    }

    public static class SliderAdapterVH extends SliderViewAdapter.ViewHolder {
        ImageView img;

        public SliderAdapterVH(View itemView) {
            super(itemView);

            img=itemView.findViewById(R.id.img_slider_item);

        }
    }
}
