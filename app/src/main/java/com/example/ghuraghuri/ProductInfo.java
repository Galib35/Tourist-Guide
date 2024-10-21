package com.example.ghuraghuri;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
import com.smarteist.autoimageslider.IndicatorView.animation.type.IndicatorAnimationType;
import com.smarteist.autoimageslider.SliderAnimations;
import com.smarteist.autoimageslider.SliderView;

import java.util.HashMap;
import java.util.Locale;
import java.util.Objects;

public class ProductInfo extends AppCompatActivity {
    SliderView slide;
    TextView name,price,rating,features,details,quantity,remain,total;
    Button addTocart,buyNow,next;
    ImageView increase,decrease;
    EditText specify,address,contact;
    LinearLayout buyingInfo;

    String key,uName,uid;
    ProductImageAdapter adapter;
    int index;

    Dialog dialog;
    int qnt=1;
    int maxQnt;
    long basePrice;
    boolean buy=false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_info);

        key=getIntent().getStringExtra("product_id");
        index= getIntent().getIntExtra("product_index",0);

        slide=findViewById(R.id.imgSlider_pdct_details);
        name=findViewById(R.id.pdct_name_info);
        price=findViewById(R.id.pdct_price_info);
        rating=findViewById(R.id.pdct_rate_info);
        features=findViewById(R.id.feature_detail);
        details=findViewById(R.id.desc_detail);
        addTocart=findViewById(R.id.add_to_cart);
        buyNow=findViewById(R.id.buy_now);
        remain=findViewById(R.id.rem_pdct);

        adapter=new ProductImageAdapter(this);
        slide.setIndicatorAnimation(IndicatorAnimationType.WORM);
        slide.setSliderTransformAnimation(SliderAnimations.DEPTHTRANSFORMATION);

        dialog=new Dialog(this);
        dialog.setContentView(R.layout.quantity);

        WindowManager.LayoutParams lp=new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());
        lp.width=WindowManager.LayoutParams.MATCH_PARENT;
        lp.height=WindowManager.LayoutParams.WRAP_CONTENT;
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
        dialog.getWindow().setAttributes(lp);

        increase=dialog.findViewById(R.id.plus);
        decrease=dialog.findViewById(R.id.minus);
        quantity=dialog.findViewById(R.id.quantity);
        specify=dialog.findViewById(R.id.specification);
        next=dialog.findViewById(R.id.dialog_btn);
        total=dialog.findViewById(R.id.pdct_price_dialog);
        buyingInfo=dialog.findViewById(R.id.buy_layout);
        address=dialog.findViewById(R.id.address_edt);
        contact=dialog.findViewById(R.id.contact_edt);

        maxQnt=Integer.parseInt(Constant.productQuantity.get(index));
        basePrice=Long.parseLong(Constant.productPrice.get(index));

        if(maxQnt<=5 && maxQnt!=0){
            String txt="Only "+ maxQnt +" products available";
            remain.setText(txt);
            remain.setVisibility(View.VISIBLE);

        }
        else if(maxQnt==0){
            String txt="Not Available";
            remain.setText(txt);
            remain.setVisibility(View.VISIBLE);

        }
        getImage();

        addTocart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.show();
                buyingInfo.setVisibility(View.GONE);
                next.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                        buy=false;
                        getInfo();
                    }
                });
            }
        });
        buyNow.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onClick(View view) {
                dialog.show();
                buyingInfo.setVisibility(View.VISIBLE);
                next.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                        buy=true;
                        getInfo();
                    }
                });
            }
        });
        decrease.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                qnt--;
                if(qnt==0){
                    qnt=1;
                }
                long res=basePrice*qnt;
                quantity.setText(String.valueOf(qnt));
                String cost= res +" tk";
                total.setText(cost);
            }
        });
        increase.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(qnt!=maxQnt)
                {
                    qnt++;
                }

                quantity.setText(String.valueOf(qnt));
                long res=basePrice*qnt;
                quantity.setText(String.valueOf(qnt));
                String cost= res +" tk";
                total.setText(cost);

            }
        });


    }

    void getImage()
    {
        DatabaseReference ref= FirebaseDatabase.getInstance().getReference()
                .child("Product").child(key).child("Photos");
        Constant.productImg.clear();
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot img:snapshot.getChildren()){
                    Constant.productImg.add(Objects.requireNonNull(img.getValue()).toString());
                }
                setInfo();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    void setInfo(){
        slide.setSliderAdapter(adapter);

        String rate=Constant.productRating.get(index)+"/5";
        String cost=Constant.productPrice.get(index)+"tk";
        name.setText(Constant.productName.get(index));
        rating.setText(rate);
        price.setText(cost);
        features.setText(Constant.productFeatures.get(index));
        details.setText(Constant.productDescription.get(index));
    }

    void getInfo(){
        uid= Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid();

        DatabaseReference ref2=FirebaseDatabase.getInstance().getReference()
                .child("User").child(uid).child("Name");


        ref2.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                uName = Objects.requireNonNull(snapshot.getValue()).toString();
                saveInfo();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }

    void saveInfo(){
        DatabaseReference ref=FirebaseDatabase.getInstance().getReference();

        HashMap<String,Object> map = new HashMap<>();
        String adrs,cntNo;
        adrs=address.getText().toString().trim();
        cntNo=contact.getText().toString().trim();

        String pName,price,pQuantity,subTotal,pSpecify;
        pName=Constant.productName.get(index);
        price=Constant.productPrice.get(index);
        pQuantity=quantity.getText().toString().trim();
        subTotal=total.getText().toString().trim();
        subTotal=subTotal.replaceAll("tk","");
        subTotal=subTotal.trim();
        pSpecify=specify.getText().toString().trim();


        if(buy){
            String orderid=ref.push().getKey();
            assert orderid != null;
            ref.child("Orders").child(orderid).child("userName").setValue(uName);
            ref.child("Orders").child(orderid).child("address").setValue(adrs);
            ref.child("Orders").child(orderid).child("contact").setValue(cntNo);
            ref.child("Orders").child(orderid).child("status").setValue("Pending");
            ref=ref.child("Orders").child(orderid).child(key);

        }
        else
        {
            ref=ref.child("Cart").child(uid).child(key);
        }

        map.put("name",pName);
        map.put("price",price);
        map.put("quantity",pQuantity);
        map.put("subtotal",subTotal);

        if(pSpecify.isEmpty()){
            pSpecify="Nothing";
        }
        map.put("specification",pSpecify);


        ref.updateChildren(map).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    if(!buy){
                        Toast.makeText(ProductInfo.this, "Added to cart", Toast.LENGTH_SHORT).show();
                    }
                    else {
                        Toast.makeText(ProductInfo.this, "Your order has been submitted\nCheck status in order history", Toast.LENGTH_SHORT).show();
                    }

                }
            }
        });
    }

}