package com.example.ghuraghuri;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.location.Address;
import android.location.Geocoder;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.example.ghuraghuri.databinding.ActivityMapsBinding;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private ActivityMapsBinding binding;

    RecyclerView recyclerView;
    TextView nothing,headline;
    ListAdapter adapter;
    ProgressBar progressBar;

    Geocoder geocoder;
    String placeName,place_id;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMapsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);

        mapFragment.getMapAsync(this);

        placeName=getIntent().getStringExtra("plc_name");
        place_id=getIntent().getStringExtra("plc_id");

        recyclerView=findViewById(R.id.ht_recView);
        nothing=findViewById(R.id.ht_noth);
        progressBar=findViewById(R.id.pr_ht);
        headline=findViewById(R.id.heading);

        progressBar.setVisibility(View.VISIBLE);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));



        geocoder=new Geocoder(this);

        getInfo();
        //getlatln();
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
    public void onMapReady(@NonNull GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in Sydney and move the camera
       // LatLng sydney = new LatLng(-34, 151);
       // mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
        //mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));




       /* try {
            System.out.println(Constant.spot_name.size());
            for(int i=0;i<Constant.spot_name.size();i++)
            {
                String name=Constant.spot_name.get(i);
                List<Address> addresses=geocoder.getFromLocationName(name,1);
                if(addresses.size()>0)
                {
                    Address address=addresses.get(0);
                    LatLng place=new LatLng(address.getLatitude(),address.getLongitude());
                    System.out.println(address);

                    //latitude=address.getLatitude();
                    //longitude=address.getLongitude();

                    // String url=getUtl(latitude,longitude);

                    //new PlaceTask().execute(url);

                    MarkerOptions markerOptions=new MarkerOptions()
                            .position(place)
                            .title(address.getLocality());

                    mMap.addMarker(markerOptions);
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(place,18));
                }
                else
                {
                    Toast.makeText(this, "Place not found", Toast.LENGTH_SHORT).show();
                }
            }


        } catch (IOException e) {
            e.printStackTrace();
        }*/


    }

    void getInfo()
    {
        String type = null;
        if(Constant.point=='h')
        {
            type="Hotel";
            headline.setText(type);
        }
        else if(Constant.point=='r')
        {
            type="Restaurant";
            headline.setText(type);
        }

        Constant.spot_name.clear();
        Constant.url.clear();
        //assert type != null;
        DatabaseReference ref= FirebaseDatabase.getInstance().getReference()
                .child("Places").child(place_id).child(type);

        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                for(DataSnapshot snapshot1:snapshot.getChildren())
                {
                    if(!Objects.equals(snapshot1.getKey(), "Count"))
                    {
                        if(Constant.point=='h')
                        {
                            Constant.spot_name.add(String.valueOf(snapshot1.child("Name").getValue()));
                            Constant.url.add(String.valueOf(snapshot1.child("URL").getValue()));
                        }
                        else
                        {
                            Constant.spot_name.add(String.valueOf(snapshot1.child("name").getValue()));
                            Constant.url.add(String.valueOf(snapshot1.child("url").getValue()));
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
            adapter=new ListAdapter(MapsActivity.this);
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
                    mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(place,13));
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

    /*String getUtl(double lat, double lon)
    {
        String type = null;
        if(Constant.point=='h')
        {
            type="hotel";
        }
        else if (Constant.point=='r')
        {
            type="restaurant";
        }
        else if (Constant.point=='l')
        {
            type="hospital";
        }

        StringBuilder builder = new StringBuilder("https://maps.googleapis.com/maps/api/place/nearbysearch/json?");
        builder.append("location=").append(lat).append(",").append(lon);
        builder.append("&radius=1000");
        builder.append("&type=").append(type);
        builder.append("&sensor=true");
        builder.append("&key=").append(getResources().getString(R.string.google_crash_reporting_api_key));
        Log.d("APIURL", builder.toString());

        return builder.toString();
    }*/

    /*private class PlaceTask extends AsyncTask<String,Integer,String> {
        @Override
        protected String doInBackground(String... strings) {
            String data=null;
            try {
                data=DownloadUr(strings[0]);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return data;
        }

        @Override
        protected void onPostExecute(String s) {
            new ParserTask().execute(s);
        }
    }*/

    /*String DownloadUr(String str) throws IOException {
        URL url=new URL(str);
        HttpURLConnection connection= (HttpURLConnection) url.openConnection();
        connection.connect();
        InputStream stream=connection.getInputStream();
        BufferedReader reader=new BufferedReader(new InputStreamReader(stream));
        StringBuilder builder=new StringBuilder();
        String line="";
        while ((line=reader.readLine()) !=null)
        {
            builder.append(line);
        }
        String data=builder.toString();
        reader.close();

        return data;
    }*/

    /*private class ParserTask extends AsyncTask<String,Integer,List<HashMap<String,String>>> {

        @Override
        protected List<HashMap<String, String>> doInBackground(String... strings) {
            JsonParser jsonParser=new JsonParser();
            List<HashMap<String,String>> maplist=null;
            JSONObject object=null;
            try {
                object=new JSONObject(strings[0]);
                maplist=jsonParser.parseResult(object);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return maplist;
        }

        @Override
        protected void onPostExecute(List<HashMap<String, String>> hashMaps) {
            mMap.clear();
            int i;
            for(i=0;i<hashMaps.size();i++)
            {
                HashMap<String,String> hashMapList=hashMaps.get(i);
                double lat=Double.parseDouble(Objects.requireNonNull(hashMapList.get("lat")));
                double lng=Double.parseDouble(Objects.requireNonNull(hashMapList.get("lng")));

               // System.out.println(lat);
                String name=hashMapList.get("name");
                LatLng latLng=new LatLng(lat,lng);
                MarkerOptions options=new MarkerOptions();
                options.position(latLng);
                options.title(name);
                mMap.addMarker(options);
               // mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng,10));
            }
        }
    }*/
}