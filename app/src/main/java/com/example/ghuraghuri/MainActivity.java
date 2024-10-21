package com.example.ghuraghuri;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.smarteist.autoimageslider.IndicatorView.animation.type.IndicatorAnimationType;
import com.smarteist.autoimageslider.SliderAnimations;
import com.smarteist.autoimageslider.SliderView;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

public class MainActivity extends AppCompatActivity {

    ImageView menu_img,chatbot;
    TextView nothing;
    DrawerLayout drawerLayout;
    NavigationView navigationView;
    ProgressBar progressBar;
    SliderView slide;
    RelativeLayout spot, shop, cart, vlog, tourPackage,blog;
    RecyclerView recyclerView;
    RecViewAdapter adapter;

    FusedLocationProviderClient location_client;
    private static final int MY_PERMISSIONS_REQUEST_LOCATION = 2;
    FirebaseAuth auth;

    String uid;
    boolean granted = false;
    String locality="",division="";
    ArrayList<String> spotAddress=new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        menu_img = findViewById(R.id.menu);
        drawerLayout = findViewById(R.id.drawer);
        navigationView = findViewById(R.id.nav);
        progressBar = findViewById(R.id.pr_main);
        slide = findViewById(R.id.imgSliderHome);
        spot = findViewById(R.id.spot_click);
        shop = findViewById(R.id.shop_click);
        cart = findViewById(R.id.cart_click);
        vlog = findViewById(R.id.vlog_layout);
        blog = findViewById(R.id.blog_main);
        tourPackage = findViewById(R.id.tour_pcg);
        recyclerView=findViewById(R.id.recView_home);
        nothing=findViewById(R.id.noth);
        chatbot=findViewById(R.id.bot);

        auth = FirebaseAuth.getInstance();

        Constant.curr_uid = Objects.requireNonNull(auth.getCurrentUser()).getUid();


        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter=new RecViewAdapter(MainActivity.this);

        recyclerView.setAdapter(adapter);

        location_client = LocationServices.getFusedLocationProviderClient(this);

        checkLocationPermission();
        getUserLocation();


        //home activity images
        int[] images = {R.drawable.test1,
                R.drawable.test2,
                R.drawable.test3};

        ImageSliderAdapter imageSliderAdapter = new ImageSliderAdapter(images, this);
        slide.setSliderAdapter(imageSliderAdapter);
        slide.setIndicatorAnimation(IndicatorAnimationType.WORM);
        slide.setSliderTransformAnimation(SliderAnimations.DEPTHTRANSFORMATION);
        slide.startAutoCycle();

        spot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, SpotList.class);
                startActivity(intent);
            }
        });
        shop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, ProductList.class);
                startActivity(intent);
            }
        });
        cart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, Cart.class);
                startActivity(intent);
            }
        });
        vlog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, VlogList.class);
                startActivity(intent);
            }
        });
        tourPackage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, PackageListUser.class);
                startActivity(intent);
            }
        });

        blog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, BlogList.class);
                startActivity(intent);
            }
        });

        chatbot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(MainActivity.this,ChatBot.class);
                startActivity(intent);
            }
        });


        //checkUser();
        menu_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!drawerLayout.isDrawerOpen(GravityCompat.START)) {
                    drawerLayout.openDrawer(GravityCompat.START);
                    navigationView.bringToFront();
                } else {
                    drawerLayout.closeDrawer(GravityCompat.START);
                }

            }
        });

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Intent intent;
                switch (item.getItemId()) {

                    case R.id.acc:
                        intent = new Intent(MainActivity.this, Account.class);
                        startActivity(intent);
                        //Toast.makeText(MainActivity.this, "Account", Toast.LENGTH_SHORT).show();
                        break;

                   /* case R.id.adm:
                        intent=new Intent(MainActivity.this,Admin.class);
                        startActivity(intent);
                        break;*/

                    case R.id.fv_list:
                        intent = new Intent(MainActivity.this, FavouriteList.class);
                        startActivity(intent);
                        break;

                    case R.id.log:
                        SharedPreferences preferences = getSharedPreferences("checkbox", MODE_PRIVATE);
                        SharedPreferences.Editor edit = preferences.edit();
                        edit.putBoolean("rem", false);
                        edit.apply();

                        intent = new Intent(MainActivity.this, SignIn.class);
                        intent.putExtra("from_main", true);
                        startActivity(intent);
                        break;
                    case R.id.hist:
                        intent = new Intent(MainActivity.this, History.class);
                        startActivity(intent);
                        break;
                    case R.id.rev:
                        intent = new Intent(MainActivity.this, YourReview.class);
                        startActivity(intent);
                        break;

                    case R.id.sugg:
                        intent = new Intent(MainActivity.this, Suggestion.class);
                        startActivity(intent);
                        break;
                    case R.id.vlog:
                        intent = new Intent(MainActivity.this, YourVlog.class);
                        startActivity(intent);
                        break;

                    case R.id.blog:
                        intent = new Intent(MainActivity.this, YourBlog.class);
                        startActivity(intent);
                        break;

                }
                return false;
            }
        });


    }

    private void getUserLocation() {
        System.out.println("ok");
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            location_client.getLastLocation().addOnSuccessListener(new OnSuccessListener<Location>() {
                @Override
                public void onSuccess(Location location) {
                    try {
                        if(location!=null){
                            Geocoder geocoder=new Geocoder(MainActivity.this,Locale.getDefault());
                            List<Address> addresses=geocoder.getFromLocation(location.getLatitude(),location.getLongitude(),1);

                            locality=addresses.get(0).getLocality().toLowerCase();
                            division=addresses.get(0).getAdminArea().toLowerCase();
                            division=division.replace("division","").trim();
                            getSpotLocation();
                            /*System.out.println("lat: "+addresses.get(0).getLatitude());
                            System.out.println("lon: "+addresses.get(0).getLongitude());
                            System.out.println("adrsline: "+addresses.get(0).getAddressLine(0));
                            System.out.println("locality: "+addresses.get(0).getLocality());
                            System.out.println("country: "+addresses.get(0).getCountryName());
                            System.out.println("adminarea: "+addresses.get(0).getAdminArea());
                            System.out.println("sublocality: "+addresses.get(0).getSubLocality());
                            System.out.println("extra: "+addresses.get(0).getExtras());*/
                        }
                    }
                    catch (IOException e){
                        Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
        else{
            checkLocationPermission();
        }
    }

    private void getSpotLocation() {
        if(!locality.isEmpty() && !division.isEmpty()){
            Constant.plc_name.clear();
            Constant.rating.clear();
            Constant.tag.clear();

            Constant.plc_id.clear();
            //Constant.tmp_plc_id.clear();

            DatabaseReference ref= FirebaseDatabase.getInstance().getReference().child("Places");
            ref.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {

                    for(DataSnapshot dataSnapshot:snapshot.getChildren())
                    {
                        if(dataSnapshot.exists())
                        {
                            Constant.plc_name.add(String.valueOf(dataSnapshot.child("Name").getValue()));
                            float rt=Float.parseFloat(String.valueOf(dataSnapshot.child("Ratings").getValue()));
                            String ans = new DecimalFormat("##.#").format(rt);
                            Constant.rating.add(ans);
                            //Constant.tmp_rating.add(ans);
                            Constant.tag.add(String.valueOf(dataSnapshot.child("Tag").getValue()));
                            spotAddress.add(String.valueOf(dataSnapshot.child("address").getValue()));
                            //Constant.tmp_tag.add(String.valueOf(dataSnapshot.child("Tag").getValue()));
                            Constant.plc_id.add(dataSnapshot.getKey());
                            //Constant.tmp_plc_id.add(dataSnapshot.getKey());
                        }
                    }
                    findAndSetData();
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    void findAndSetData(){
        Constant.tmp_name.clear();
        Constant.tmp_rating.clear();
        Constant.tmp_plc_id.clear();
        Constant.tmp_tag.clear();
        System.out.println(locality);
        for(int i=0;i<spotAddress.size();i++){
            if(spotAddress.get(i).toLowerCase().contains(locality)){
                Constant.tmp_plc_id.add(Constant.plc_id.get(i));
                Constant.tmp_name.add(Constant.plc_name.get(i));
                Constant.tmp_rating.add(Constant.rating.get(i));
                Constant.tmp_tag.add(Constant.tag.get(i));
                //System.out.println(Constant.tmp_name.get(i));
            }
            else if(spotAddress.get(i).toLowerCase().contains(division)){
                Constant.tmp_plc_id.add(Constant.plc_id.get(i));
                Constant.tmp_name.add(Constant.plc_name.get(i));
                Constant.tmp_rating.add(Constant.rating.get(i));
                Constant.tmp_tag.add(Constant.tag.get(i));
            }
        }

        /*if(!locality.equals(division)){
            for(int i=0;i<spotAddress.size();i++){
                if(spotAddress.get(i).toLowerCase().contains(division)){
                    Constant.tmp_plc_id.add(Constant.plc_id.get(i));
                    Constant.tmp_name.add(Constant.plc_name.get(i));
                    Constant.tmp_rating.add(Constant.rating.get(i));
                    Constant.tmp_tag.add(Constant.tag.get(i));
                }
            }
        }*/

        if (Constant.tmp_name.isEmpty())
        {
            nothing.setVisibility(View.VISIBLE);
        }
        else
        {
            nothing.setVisibility(View.GONE);
            adapter.notifyDataSetChanged();
        }
    }
    /*void checkUser(){
        uid= Objects.requireNonNull(auth.getCurrentUser()).getUid();
        Menu menu=navigationView.getMenu();
        MenuItem menuItem=menu.findItem(R.id.adm);

        DatabaseReference ref2= FirebaseDatabase.getInstance().getReference().child("Admin");
        ref2.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Constant.admin_uid=String.valueOf(snapshot.getValue());

                if(Constant.admin_uid.equals(uid))
                {
                    menuItem.setVisible(true);
                }
                else
                {
                    menuItem.setVisible(false);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });



    }*/


    void checkLocationPermission(){
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
                        != PackageManager.PERMISSION_GRANTED)
        {
            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(
                    this,android.Manifest.permission.ACCESS_FINE_LOCATION))
            {
                new AlertDialog.Builder(this)
                        .setTitle("Allow location permission")
                        .setMessage("Allow permission to get recommendation")
                        .setPositiveButton("ok", (dialogInterface, i) ->
                                ActivityCompat.requestPermissions(
                                        MainActivity.this,
                                        new String[]{
                                                Manifest.permission.ACCESS_FINE_LOCATION,
                                                Manifest.permission.ACCESS_COARSE_LOCATION
                                        },
                                        MY_PERMISSIONS_REQUEST_LOCATION))
                        .setCancelable(false)
                        .setNegativeButton("cancel", (dialogInterface, i) -> {
                            granted=false;
                            Toast.makeText(this, "Permission denied", Toast.LENGTH_SHORT).show();
                        })
                        .show();
            }
            else {
                // No explanation needed, we can request the permission.
                ActivityCompat.requestPermissions(
                        MainActivity.this,
                        new String[]{
                                Manifest.permission.ACCESS_FINE_LOCATION,
                                Manifest.permission.ACCESS_COARSE_LOCATION
                        },
                        MY_PERMISSIONS_REQUEST_LOCATION );
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == MY_PERMISSIONS_REQUEST_LOCATION) {
            // If request is cancelled, the result arrays are empty.
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {//granted
                if (ContextCompat.checkSelfPermission(this,
                        android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED &&
                        ContextCompat.checkSelfPermission(this,
                                Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(this, "Permission granted", Toast.LENGTH_SHORT).show();
                    granted=true;
                }
            }
            else {//permission rejected
                granted=false;
                Toast.makeText(this, "Allow permission to get tour recommendation", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void onBackPressed() {
        finishAffinity();
    }
}