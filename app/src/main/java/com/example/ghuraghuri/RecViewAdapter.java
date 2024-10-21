package com.example.ghuraghuri;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Objects;

public class RecViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    Context context;
    FirebaseAuth auth;


    public RecViewAdapter(Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(context).inflate(R.layout.places_design,parent,false);
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        String name=Constant.tmp_name.get(position);
        String rating=Constant.tmp_rating.get(position);
        String plc_tag=Constant.tmp_tag.get(position);

        ((MyViewHolder)holder).name.setText(name);
        ((MyViewHolder)holder).rating.setText(rating);
        ((MyViewHolder)holder).tag.setText(plc_tag);

        float rt=Float.parseFloat(rating);
        ((MyViewHolder)holder).star.setRating(rt);

        auth=FirebaseAuth.getInstance();
        String uid= Objects.requireNonNull(auth.getCurrentUser()).getUid();

        //System.out.println(Constant.key.get(0));

        if (Constant.key.contains(Constant.tmp_plc_id.get(holder.getAdapterPosition())))
        {
            //System.out.println("tmp"+Constant.tmp_plc_id.get(position));
            ((MyViewHolder)holder).fv_true.setVisibility(View.VISIBLE);
            ((MyViewHolder)holder).fv_false.setVisibility(View.INVISIBLE);
        }

        ((MyViewHolder)holder).fv_true.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                ((MyViewHolder)holder).fv_true.setVisibility(View.GONE);
                ((MyViewHolder)holder).fv_false.setVisibility(View.VISIBLE);

                String id=Constant.tmp_plc_id.get(position);

                DatabaseReference ref= FirebaseDatabase.getInstance().getReference().
                        child("User").child(uid).child("Favourite List").child(id);
                ref.removeValue();

            }
        });
        ((MyViewHolder)holder).fv_false.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                ((MyViewHolder)holder).fv_true.setVisibility(View.VISIBLE);
                ((MyViewHolder)holder).fv_false.setVisibility(View.GONE);

                //auth=FirebaseAuth.getInstance();

                String id=Constant.tmp_plc_id.get(position);
                String uid= Objects.requireNonNull(auth.getCurrentUser()).getUid();

                DatabaseReference ref= FirebaseDatabase.getInstance().getReference().
                        child("User").child(uid).child("Favourite List").child(id);
                ref.child("name").setValue(name);
                ref.child("tag").setValue(plc_tag);
                ref.child("rating").setValue(rating);

            }
        });

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(context,Description.class);
                intent.putExtra("id",Constant.tmp_plc_id.get(position));
                context.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return Constant.tmp_name.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{

        TextView name,rating,tag;
        RatingBar star;
        ImageView fv_false,fv_true;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            name=itemView.findViewById(R.id.plc_nm);
            rating=itemView.findViewById(R.id.plc_rating);
            tag=itemView.findViewById(R.id.plc_tag);
            star=itemView.findViewById(R.id.rt);
            fv_false=itemView.findViewById(R.id.fav_false);
            fv_true=itemView.findViewById(R.id.fav_true);
        }
    }
}
