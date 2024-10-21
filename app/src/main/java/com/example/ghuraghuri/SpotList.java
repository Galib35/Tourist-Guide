package com.example.ghuraghuri;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Objects;

public class SpotList extends AppCompatActivity {

    RecyclerView recyclerView;
    RecViewAdapter adapter;
    TextView nothing;
    ProgressBar progressBar;
    EditText search;
    RadioGroup radio;

    FirebaseAuth auth;
    String filterBy="Spot";
    ArrayList<String> spotAddress=new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_spot_list);

        nothing=findViewById(R.id.noth);
        progressBar=findViewById(R.id.pr_main);
        search =findViewById(R.id.search_edt);
        radio=findViewById(R.id.filter_group);

        auth=FirebaseAuth.getInstance();

        recyclerView=findViewById(R.id.recView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        adapter=new RecViewAdapter(SpotList.this);

        progressBar.setVisibility(View.VISIBLE);

        radio.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                RadioButton button=radioGroup.findViewById(i);
                filterBy=button.getText().toString().trim();
            }
        });

        readData(new callBack() {
            @Override
            public void onCallback(ArrayList<String> plc_name, ArrayList<String> rating, ArrayList<String> tag) {

                Constant.tmp_name.clear();
                Constant.tmp_rating.clear();
                Constant.tmp_tag.clear();

                Constant.plc_name.clear();
                Constant.rating.clear();
                Constant.tag.clear();


                Constant.tmp_name.addAll(plc_name);
                Constant.tmp_rating.addAll(rating);
                Constant.tmp_tag.addAll(tag);

                Constant.plc_name.addAll(plc_name);
                Constant.rating.addAll(rating);
                Constant.tag.addAll(tag);

                if (Constant.tmp_name.isEmpty())
                {
                    nothing.setVisibility(View.VISIBLE);
                }
                else
                {
                    nothing.setVisibility(View.GONE);

                    recyclerView.setAdapter(adapter);
                }
                progressBar.setVisibility(View.GONE);


            }
        });

        readfav();

        search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                String text=editable.toString().toLowerCase();
                filter(text);

            }
        });
    }

    interface callBack
    {

        void onCallback(ArrayList<String> plc_name,
                        ArrayList<String > rating,
                        ArrayList<String> tag
        );
    }

    void readData(final callBack cb)
    {

        DatabaseReference ref= FirebaseDatabase.getInstance().getReference().child("Places");
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                ArrayList<String> plc_name=new ArrayList<>();
                ArrayList<String > rating=new ArrayList<>();
                ArrayList<String> tag=new ArrayList<>();

                Constant.plc_id.clear();
                Constant.tmp_plc_id.clear();
                for(DataSnapshot dataSnapshot:snapshot.getChildren())
                {
                    if(dataSnapshot.exists())
                    {
                        plc_name.add(String.valueOf(dataSnapshot.child("Name").getValue()));
                        //rating.add(String.valueOf(dataSnapshot.child("Ratings").getValue()));
                        float rt=Float.parseFloat(String.valueOf(dataSnapshot.child("Ratings").getValue()));
                        String ans = new DecimalFormat("##.#").format(rt);
                        rating.add(ans);
                        tag.add(String.valueOf(dataSnapshot.child("Tag").getValue()));
                        spotAddress.add(String.valueOf(dataSnapshot.child("address").getValue()));
                        Constant.plc_id.add(dataSnapshot.getKey());
                        Constant.tmp_plc_id.add(dataSnapshot.getKey());
                    }
                }

                cb.onCallback(plc_name,rating,tag);


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @SuppressLint("NotifyDataSetChanged")
    void filter(String s)
    {
        ArrayList<String> temp;

        Constant.tmp_name.clear();
        Constant.tmp_rating.clear();
        Constant.tmp_plc_id.clear();
        Constant.tmp_tag.clear();

        if(filterBy.equals("Spot")){
            temp = new ArrayList<>(Constant.plc_name);
            int ii;
            int[] mp = new int[Constant.plc_name.size() + 1];
            for(ii=0;ii<temp.size();ii++)
            {
                if(temp.get(ii).toLowerCase().contains(s))
                {
                    Constant.tmp_plc_id.add(Constant.plc_id.get(ii));
                    Constant.tmp_name.add(Constant.plc_name.get(ii));
                    Constant.tmp_rating.add(Constant.rating.get(ii));
                    Constant.tmp_tag.add(Constant.tag.get(ii));

                    mp[ii] = 1;
                }



            }
            int k;
            for(k=0;k<temp.size();k++){


                String s2=temp.get(k);
                String s1=s;

                s2=s2.toLowerCase();
                s1=s1.toLowerCase();

                int n=s.length();
                int m=s2.length();


                int[][] dp = new int[n + 1][m + 1];
                for (int i = 0; i <= n; i++) {
                    dp[i][0] = i;
                }
                for (int j = 0; j <= m; j++) {
                    dp[0][j] = j;
                }
                for (int i = 1; i <= n; i++) {
                    for (int j = 1; j <= m; j++) {
                        if (s1.charAt(i - 1) == s2.charAt(j - 1)) {
                            dp[i][j] = dp[i - 1][j - 1];
                        } else {
                            dp[i][j] = 1 + Math.min(Math.min(dp[i - 1][j], dp[i][j - 1]), dp[i - 1][j - 1]);
                        }
                    }
                }

                int num_of_operations;

                num_of_operations=m-dp[n][m];

                int word_matching = ((num_of_operations)*100)/m;

                System.out.println(word_matching);

                //  enter matching percentage as percentage

                int percentage=30;

                if (word_matching > percentage && mp[k]==0) {
                    Constant.tmp_plc_id.add(Constant.plc_id.get(k));
                    Constant.tmp_name.add(Constant.plc_name.get(k));
                    Constant.tmp_rating.add(Constant.rating.get(k));
                    Constant.tmp_tag.add(Constant.tag.get(k));
                }
            }
        }
        else if(filterBy.equals("Tag")){
            temp = new ArrayList<>(Constant.tag);

            int i;
            for(i=0;i<temp.size();i++)
            {
                if(temp.get(i).toLowerCase().contains(s))
                {
                    Constant.tmp_plc_id.add(Constant.plc_id.get(i));
                    Constant.tmp_name.add(Constant.plc_name.get(i));
                    Constant.tmp_rating.add(Constant.rating.get(i));
                    Constant.tmp_tag.add(Constant.tag.get(i));
                }

            }

        }
        else {
            int i;
            for(i=0;i<spotAddress.size();i++)
            {
                if(spotAddress.get(i).toLowerCase().contains(s))
                {
                    Constant.tmp_plc_id.add(Constant.plc_id.get(i));
                    Constant.tmp_name.add(Constant.plc_name.get(i));
                    Constant.tmp_rating.add(Constant.rating.get(i));
                    Constant.tmp_tag.add(Constant.tag.get(i));
                }

            }
        }
        adapter.notifyDataSetChanged();


        /*Constant.tmp_name.clear();
        Constant.tmp_rating.clear();
        Constant.tmp_plc_id.clear();
        Constant.tmp_tag.clear();

        int i;
        for(i=0;i<temp.size();i++)
        {
            if(temp.get(i).toLowerCase().contains(s))
            {
                Constant.tmp_plc_id.add(Constant.plc_id.get(i));
                Constant.tmp_name.add(Constant.plc_name.get(i));
                Constant.tmp_rating.add(Constant.rating.get(i));
                Constant.tmp_tag.add(Constant.tag.get(i));
            }

        }
        adapter.notifyDataSetChanged();*/

        if (Constant.tmp_name.isEmpty())
        {
            nothing.setVisibility(View.VISIBLE);
        }
        else
        {
            nothing.setVisibility(View.GONE);
        }
    }

    void readfav()
    {
        Constant.key.clear();
        FirebaseAuth auth=FirebaseAuth.getInstance();
        String uid= Objects.requireNonNull(auth.getCurrentUser()).getUid();
        DatabaseReference ref= FirebaseDatabase.getInstance().getReference().
                child("User").child(uid);

        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.child("Favourite List").exists())
                {
                    for (DataSnapshot snapshot1:snapshot.child("Favourite List").getChildren())
                    {
                        Constant.key.add(snapshot1.getKey());
                        //System.out.println(snapshot1.getKey());

                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }


}