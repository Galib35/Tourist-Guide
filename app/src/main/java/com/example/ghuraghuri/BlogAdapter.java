package com.example.ghuraghuri;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

public class BlogAdapter extends RecyclerView.Adapter<BlogAdapter.BlogViewHolder>{
    Context context;

    public BlogAdapter(Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    public BlogAdapter.BlogViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(context).inflate(R.layout.blog_item_design,parent,false);
        return new BlogViewHolder(v);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull BlogAdapter.BlogViewHolder holder, @SuppressLint("RecyclerView") int position) {
        Glide.with(context).load(Constant.blogThumb.get(position)).into(holder.thumbnail);

        holder.name.setText(Constant.blogTitle.get(position));
        holder.location.setText(Constant.blogLocation.get(position));
        holder.blogger_name.setText("-By "+Constant.bloggerName.get(position));
        holder.date.setText(Constant.blogDate.get(position));

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(context,BlogDisplay.class);
                intent.putExtra("blog_index",position);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return Constant.blogId.size();
    }

    public static class BlogViewHolder extends RecyclerView.ViewHolder{
        ImageView thumbnail;
        TextView name,location,blogger_name,date;
        public BlogViewHolder(@NonNull View itemView) {
            super(itemView);
            thumbnail=itemView.findViewById(R.id.blog_Img);
            name=itemView.findViewById(R.id.blog_title);
            blogger_name=itemView.findViewById(R.id.blogger);
            location=itemView.findViewById(R.id.blog_loc);
            date=itemView.findViewById(R.id.blog_date);

        }
    }
}
