package com.example.ghuraghuri;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.smarteist.autoimageslider.SliderViewAdapter;

public class ImageSliderAdapter extends SliderViewAdapter<ImageSliderAdapter.SliderAdapterVH> {

    int [] images;
    Context context;

    public ImageSliderAdapter(int[] images, Context context) {
        this.images = images;
        this.context = context;
    }

    @Override
    public ImageSliderAdapter.SliderAdapterVH onCreateViewHolder(ViewGroup parent) {
        View v= LayoutInflater.from(context).inflate(R.layout.testing,parent,false);
        return new SliderAdapterVH(v);
    }

    @Override
    public void onBindViewHolder(ImageSliderAdapter.SliderAdapterVH viewHolder, int position) {

        viewHolder.img.setImageResource(images[position]);

    }

    @Override
    public int getCount() {
        return images.length;
    }

    public static class SliderAdapterVH extends SliderViewAdapter.ViewHolder{
        ImageView img;

        public SliderAdapterVH(View itemView) {
            super(itemView);
            img=itemView.findViewById(R.id.slider_img);
        }
    }
}
