package com.example.ghuraghuri;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;

public class Cart extends AppCompatActivity {
    ProgressBar progressBar;
    TextView nothing,total,confirmTotal;
    RecyclerView recyclerView;
    CartRecAdapter adapter;
    Button proceed,confirm;
    EditText address,contact;

    Dialog dialog;
    String uid,uName,totalCostOrder;
    ArrayList<String> thumb=new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);
        progressBar=findViewById(R.id.pr_cart);
        nothing=findViewById(R.id.cart_noth);
        recyclerView=findViewById(R.id.cart_recView);
        total=findViewById(R.id.total_cart);
        proceed=findViewById(R.id.proceed_btn);

        progressBar.setVisibility(View.VISIBLE);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        adapter=new CartRecAdapter(Cart.this);

        getInfo();
        totalPrice();

        dialog=new Dialog(this);
        dialog.setContentView(R.layout.invoice);

        WindowManager.LayoutParams lp=new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());
        lp.width=WindowManager.LayoutParams.MATCH_PARENT;
        lp.height=WindowManager.LayoutParams.WRAP_CONTENT;
        lp.gravity= Gravity.BOTTOM;
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
        dialog.getWindow().setAttributes(lp);

        address=dialog.findViewById(R.id.address_edt);
        contact=dialog.findViewById(R.id.contact_edt);
        confirmTotal=dialog.findViewById(R.id.total_price_dialog);
        confirm=dialog.findViewById(R.id.confirm_dialog_btn);

        proceed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.show();
                long totalCost=0;
                for(int i=0;i<Constant.productSubtotal.size();i++){
                    String tk=Constant.productSubtotal.get(i).replace("tk","").trim();
                    totalCost+=Long.parseLong(tk);
                }
                String totaltxt= totalCost +"tk";
                totalCostOrder=String.valueOf(totalCost);
                confirmTotal.setText(totaltxt);
            }
        });

        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String adrs,contactNo;
                adrs=address.getText().toString().trim();
                contactNo=contact.getText().toString().trim();

                if(!adrs.isEmpty() && !contactNo.isEmpty()){
                    dialog.dismiss();

                    saveOrder(adrs,contactNo);
                }
                else
                {
                    Toast.makeText(Cart.this, "Please Fill Up All Input Fields", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    void getInfo(){
        uid= Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid();
        DatabaseReference ref= FirebaseDatabase.getInstance().getReference()
                .child("Cart").child(uid);

        Constant.productName.clear();
        Constant.productQuantity.clear();
        Constant.productPrice.clear();
        Constant.productSubtotal.clear();
        Constant.productId.clear();
        Constant.productSpecification.clear();

        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){

                    for(DataSnapshot snapshot1:snapshot.getChildren()){

                        Constant.productName.add(Objects.requireNonNull(snapshot1.child("name").getValue()).toString());
                        Constant.productPrice.add(Objects.requireNonNull(snapshot1.child("price").getValue()).toString());
                        Constant.productQuantity.add(Objects.requireNonNull(snapshot1.child("quantity").getValue()).toString());
                        Constant.productSubtotal.add(Objects.requireNonNull(snapshot1.child("subtotal").getValue()).toString());
                        Constant.cartQuantity.add(Objects.requireNonNull(snapshot1.child("quantity").getValue()).toString());
                        Constant.productSpecification.add(Objects.requireNonNull(snapshot1.child("specification").getValue()).toString());

                        Constant.productId.add(snapshot1.getKey());

                    }
                    getThumb();

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        DatabaseReference ref2=FirebaseDatabase.getInstance().getReference()
                .child("User").child(uid).child("Name");
        ref2.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                uName = Objects.requireNonNull(snapshot.getValue()).toString();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    void getThumb(){
        DatabaseReference ref2=FirebaseDatabase.getInstance().getReference()
                .child("Product");

        Constant.maxQuantity.clear();
        Constant.thumbnail.clear();
        ref2.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot2) {
                for(DataSnapshot ds:snapshot2.getChildren()){
                    if(Constant.productId.contains(ds.getKey()))
                    {
                        Constant.thumbnail.add(Objects.requireNonNull(ds.child("Thumbnail").getValue()).toString());
                        Constant.maxQuantity.add(Objects.requireNonNull(ds.child("quantity").getValue()).toString());
                    }

                }
                setInfo();


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }

    void setInfo(){
        System.out.println(Constant.thumbnail.size());
        if(Constant.productName.isEmpty()){
            nothing.setVisibility(View.VISIBLE);
        }
        else
        {
            nothing.setVisibility(View.GONE);
            recyclerView.setAdapter(adapter);
        }
        progressBar.setVisibility(View.INVISIBLE);
    }

    void totalPrice(){
        uid= Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid();
        DatabaseReference ref= FirebaseDatabase.getInstance().getReference()
                .child("Cart").child(uid);

        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                long totalCost=0;
                String price;
                for(DataSnapshot snapshot1:snapshot.getChildren()){
                    price=Objects.requireNonNull(snapshot1.child("subtotal").getValue()).toString();
                    price=price.replaceAll("tk","");
                    price=price.trim();
                    long prc=Long.parseLong(price);
                    totalCost+=prc;
                }
                String str="Total: "+totalCost+"tk";
                total.setText(str);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }



    void saveOrder(String adrs, String contactNo){
        DatabaseReference ref=FirebaseDatabase.getInstance().getReference();
        DatabaseReference ref2=FirebaseDatabase.getInstance().getReference()
                .child("Product");
        String orderid=ref.push().getKey();

        Date date= Calendar.getInstance().getTime();
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());
        SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm", Locale.getDefault());

        String formattedDate = dateFormat.format(date);
        String formattedTime = timeFormat.format(date);

        assert orderid != null;
        ref=ref.child("Orders").child(orderid);

        ref.child("userName").setValue(uName);
        ref.child("address").setValue(adrs);
        ref.child("contact").setValue(contactNo);
        ref.child("status").setValue("Pending");
        ref.child("date").setValue(formattedDate);
        ref.child("time").setValue(formattedTime);
        ref.child("total").setValue(totalCostOrder);

        ArrayList<HashMap<String,Object> >orderList=new ArrayList<>();

        for(int i=0;i<Constant.productId.size();i++){
            //ref=ref.child(Constant.productId.get(i));
            HashMap<String,Object> map = new HashMap<>();
            String pName,price,pQuantity,subTotal,pSpecify;

            pName=Constant.productName.get(i);
            price=Constant.productPrice.get(i);
            pQuantity=Constant.cartQuantity.get(i);
            subTotal=Constant.productSubtotal.get(i);
            pSpecify=Constant.productSpecification.get(i);

            int rem;
            rem=Integer.parseInt(Constant.maxQuantity.get(i))-Integer.parseInt(Constant.cartQuantity.get(i));

            //map.put("address",adrs);
            //map.put("contact",contactNo);
            //map.put("status","Pending");
            map.put("name",pName);
            map.put("price",price);
            map.put("quantity",pQuantity);
            map.put("subtotal",subTotal);
            map.put("specification",pSpecify);

            /*map.put(Constant.productId.get(i)+"/name",pName);
            map.put(Constant.productId.get(i)+"/price",price);
            map.put(Constant.productId.get(i)+"/quantity",pQuantity);
            map.put(Constant.productId.get(i)+"/subtotal",subTotal);
            map.put(Constant.productId.get(i)+"/specification",pSpecify);*/

            //orderList.add(map);

            int finalI = i;
            ref.child(Constant.productId.get(i)).updateChildren(map).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if(!task.isSuccessful())
                    {
                        Toast.makeText(Cart.this, "Something went wrong!", Toast.LENGTH_SHORT).show();
                    }
                    else {
                        ref2.child(Constant.productId.get(finalI)).child("quantity").setValue(rem);
                        Toast.makeText(Cart.this, "Your order has been submitted\nCheck status in order history", Toast.LENGTH_SHORT).show();
                    }

                }
            });

        }
        /*ref.updateChildren((Map<String, Object>) orderList).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful())
                {
                    //ref2.child(Constant.productId.get(finalI)).child("quantity").setValue(rem);
                    Toast.makeText(Cart.this, "Your order has been submitted\nCheck status in order history", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    Toast.makeText(Cart.this, Objects.requireNonNull(task.getException()).getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });*/
    }

}