package com.example.ghuraghuri;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Objects;

public class CartRecAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{
    Context context;
    int maxQnt;

    public CartRecAdapter(Context context) {
        this.context = context;
    }



    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(context).inflate(R.layout.cart_list,parent,false);
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        String qntx,cost,sub;
        qntx=Constant.productQuantity.get(position)+"x";
        cost=Constant.productPrice.get(position)+"tk";
        sub="Subtotal:"+Constant.productSubtotal.get(position)+"tk";
        ((MyViewHolder)holder).name.setText(Constant.productName.get(position));
        ((MyViewHolder)holder).quantityx.setText(qntx);
        ((MyViewHolder)holder).quantity.setText(Constant.productQuantity.get(position));
        ((MyViewHolder)holder).price.setText(cost);
        ((MyViewHolder)holder).subtotal.setText(sub);

        //System.out.println(Constant.thumbnail.get(0));
        Glide.with(context).load(Constant.thumbnail.get(position)).into(((MyViewHolder)holder).thumb);

        ((MyViewHolder)holder).increase.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int pos=holder.getAdapterPosition();
                int qnt=Integer.parseInt(Constant.cartQuantity.get(pos));
                maxQnt=Integer.parseInt(Constant.maxQuantity.get(pos));

                System.out.println(qnt);
                if(qnt!=maxQnt)
                {
                    qnt++;
                }
                Constant.cartQuantity.set(pos,String.valueOf(qnt));
                setData(holder,pos);


            }
        });

        ((MyViewHolder)holder).decrease.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int pos=holder.getAdapterPosition();
                int qnt=Integer.parseInt(Constant.cartQuantity.get(pos));
                System.out.println(qnt);
                qnt--;
                if(qnt==0){
                    qnt=1;
                }
                Constant.cartQuantity.set(pos,String.valueOf(qnt));
                setData(holder,pos);
            }
        });

    }

    @Override
    public int getItemCount() {
        return Constant.productName.size();
    }

    void setData(@NonNull RecyclerView.ViewHolder holder, int pos){
        int qnt=Integer.parseInt(Constant.cartQuantity.get(pos));
        long basePrice=Long.parseLong(Constant.productPrice.get(pos));
        long res=basePrice*qnt;
        String changeTotal="Subtotal:"+res+"tk";
        String changeQntx=qnt+"x";
        ((MyViewHolder)holder).subtotal.setText(changeTotal);
        ((MyViewHolder)holder).quantityx.setText(changeQntx);
        ((MyViewHolder)holder).quantity.setText(Constant.cartQuantity.get(pos));
        Constant.productSubtotal.set(pos, String.valueOf(res));
        updateDatabase(pos,res,Constant.cartQuantity.get(pos));
    }

    void updateDatabase(int pos,long subtl, String qnt){
        String uid= Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid();
        DatabaseReference ref= FirebaseDatabase.getInstance().getReference()
                .child("Cart").child(uid).child(Constant.productId.get(pos));

        HashMap<String,Object> map=new HashMap<>();
        map.put("quantity", qnt);
        map.put("subtotal",subtl +"tk");

        ref.updateChildren(map);
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{

        TextView name,quantityx,quantity,price,subtotal;
        ImageView thumb,increase,decrease;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            name=itemView.findViewById(R.id.pdct_name_cart);
            quantityx=itemView.findViewById(R.id.amount_cart);
            quantity=itemView.findViewById(R.id.quantity_cart);
            price=itemView.findViewById(R.id.price_cart);
            subtotal=itemView.findViewById(R.id.subtotal_cart);
            thumb=itemView.findViewById(R.id.pdct_img_cart);
            increase=itemView.findViewById(R.id.plus);
            decrease=itemView.findViewById(R.id.minus);
        }
    }

}
