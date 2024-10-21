package com.example.ghuraghuri;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.example.ghuraghuri.databinding.ActivityMapsAttractionBinding;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class MapsAttraction extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private ActivityMapsAttractionBinding binding;

    RecyclerView recyclerView;
    TextView nothing;
    ListAdapter adapter;
    ProgressBar progressBar;
    Geocoder geocoder;

    String place_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

     binding = ActivityMapsAttractionBinding.inflate(getLayoutInflater());
     setContentView(binding.getRoot());

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        place_id=getIntent().getStringExtra("plc_id");
        recyclerView=findViewById(R.id.atr_recView);
        nothing=findViewById(R.id.atr_noth);
        progressBar=findViewById(R.id.pr_attr);

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        progressBar.setVisibility(View.VISIBLE);

        geocoder=new Geocoder(this);

        getInfo();
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in Sydney and move the camera
        /*LatLng sydney = new LatLng(-34, 151);
        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));*/
    }

    void getInfo()
    {
        Constant.spot_name.clear();
        Constant.spot_description.clear();
        Constant.mp.clear();

        DatabaseReference ref= FirebaseDatabase.getInstance().getReference()
                .child("Places").child(place_id).child("Tourist Spots");

        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                ArrayList<String> name=new ArrayList<>();
                ArrayList<String> des=new ArrayList<>();

                for(DataSnapshot snapshot1:snapshot.getChildren())
                {
                    if(!Objects.equals(snapshot1.getKey(), "Count"))
                    {
                        Constant.spot_name.add(String.valueOf(snapshot1.child("Name").getValue()));
                        Constant.spot_description.add(String.valueOf(snapshot1.child("Description").getValue()));

                        if (snapshot1.child("Images").exists())
                        {
                            ArrayList<String > imgurl=new ArrayList<>();
                            for (DataSnapshot snapshot2:snapshot1.child("Images").getChildren())
                            {
                                imgurl.add(Objects.requireNonNull(snapshot2.getValue()).toString());
                            }
                            Constant.mp.put(snapshot1.getKey(),imgurl);
                        }
                    }

                }

                setInfo();


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    void setInfo()
    {
        if(Constant.spot_name.isEmpty())
        {
            nothing.setVisibility(View.VISIBLE);
        }
        else
        {
            nothing.setVisibility(View.GONE);
            adapter=new ListAdapter(MapsAttraction.this);
            recyclerView.setAdapter(adapter);
        }

        for(int i=0;i<Constant.spot_name.size();i++)
        {

            try {
                String name=Constant.spot_name.get(i);
                List<Address> addresses=geocoder.getFromLocationName(name,1);

                if(addresses.size()>0)
                {
                    Address address=addresses.get(0);
                    LatLng place=new LatLng(address.getLatitude(),address.getLongitude());


                    MarkerOptions markerOptions=new MarkerOptions()
                            .position(place)
                            .title(name);

                    // mMap=new GoogleMap();
                    mMap.addMarker(markerOptions);
                    mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(place,10));
                }
                else
                {
                    Toast.makeText(this, "Place not found", Toast.LENGTH_SHORT).show();
                }

            } catch (IOException e) {
                e.printStackTrace();
            }

        }
        progressBar.setVisibility(View.INVISIBLE);

    }
}