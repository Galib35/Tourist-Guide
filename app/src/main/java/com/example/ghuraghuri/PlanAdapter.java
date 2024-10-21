package com.example.ghuraghuri;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class PlanAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    Context context;

    public PlanAdapter(Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(context).inflate(R.layout.plan_design,parent,false);
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        String plc,td,stp;
        plc=Constant.where.get(position);
        td=Constant.todo.get(position);
        stp=Constant.step.get(position);

        ((MyViewHolder)holder).step.setText(stp);
        ((MyViewHolder)holder).todo.setText(td);
        ((MyViewHolder)holder).where.setText(plc);

        ((MyViewHolder)holder).plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((MyViewHolder)holder).plus.setVisibility(View.INVISIBLE);
                ((MyViewHolder)holder).minus.setVisibility(View.VISIBLE);
                ((MyViewHolder)holder).layout.setVisibility(View.VISIBLE);

            }
        });

        ((MyViewHolder)holder).minus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((MyViewHolder)holder).plus.setVisibility(View.VISIBLE);
                ((MyViewHolder)holder).minus.setVisibility(View.INVISIBLE);
                ((MyViewHolder)holder).layout.setVisibility(View.GONE);

            }
        });

        ((MyViewHolder)holder).done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((MyViewHolder)holder).check.setVisibility(View.VISIBLE);
                ((MyViewHolder)holder).undo.setVisibility(View.VISIBLE);
                ((MyViewHolder)holder).done.setVisibility(View.INVISIBLE);
                Constant.stepCount++;
            }
        });

        ((MyViewHolder)holder).undo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((MyViewHolder)holder).check.setVisibility(View.INVISIBLE);
                ((MyViewHolder)holder).undo.setVisibility(View.INVISIBLE);
                ((MyViewHolder)holder).done.setVisibility(View.VISIBLE);
                Constant.stepCount--;
            }
        });

    }

    @Override
    public int getItemCount() {
        return Constant.step.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{

        TextView step,todo,where;
        ImageView plus,minus,check;
        Button done,undo;
        LinearLayout layout;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            step=itemView.findViewById(R.id.stp_txt);
            todo=itemView.findViewById(R.id.todo_txt);
            layout=itemView.findViewById(R.id.todo_layout);
            plus=itemView.findViewById(R.id.pls);
            minus=itemView.findViewById(R.id.mns);
            check=itemView.findViewById(R.id.check_box);
            done=itemView.findViewById(R.id.done_btn);
            undo=itemView.findViewById(R.id.undo_btn);
            where=itemView.findViewById(R.id.whr);


        }
    }
}
