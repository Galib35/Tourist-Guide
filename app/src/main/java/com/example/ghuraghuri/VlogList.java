package com.example.ghuraghuri;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.PlayerConstants;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.FullscreenListener;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.YouTubePlayerListener;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.options.IFramePlayerOptions;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView;

import java.util.Objects;

import kotlin.Unit;
import kotlin.jvm.functions.Function0;

public class VlogList extends AppCompatActivity {

    FrameLayout full;
    RecyclerView recyclerView;
    VlogAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vlog_list);

        //youTubePlayerView=findViewById(R.id.youtube_player_view);
        full=findViewById(R.id.full_screen_view_container);
        recyclerView=findViewById(R.id.vlog_recView);

        adapter=new VlogAdapter(this,full,this,recyclerView);

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        Constant.fromVlogList=true;


        getVideo();



    }

    void getVideo(){
        DatabaseReference ref= FirebaseDatabase.getInstance().getReference()
                .child("Vlog");
        Constant.videoTitle.clear();
        Constant.videoId.clear();
        Constant.videoDate.clear();
        Constant.vlogger.clear();
        Constant.videoLocation.clear();

        ref.addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot snapshot1:snapshot.getChildren()){
                    Constant.videoTitle.add(Objects.requireNonNull(snapshot1.child("title").getValue()).toString());
                    Constant.videoId.add(Objects.requireNonNull(snapshot1.child("video id").getValue()).toString());
                    Constant.videoDate.add(Objects.requireNonNull(snapshot1.child("date").getValue()).toString());
                    Constant.vlogger.add(Objects.requireNonNull(snapshot1.child("vlogger").getValue()).toString());
                    Constant.videoLocation.add(Objects.requireNonNull(snapshot1.child("location").getValue()).toString());
                }
                setValue();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    void setValue(){
        System.out.println(Constant.videoId.size());
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void onBackPressed() {
        if (Constant.isFullscreen){
            //Constant.player.toggleFullscreen();
            Constant.isFullscreen=false;
            Constant.youTubeView.setVisibility(View.VISIBLE);
            full.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);
            full.removeAllViews();
            // Change back to the default orientation (usually portrait)
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED);
            View decorView = getWindow().getDecorView();
            decorView.setSystemUiVisibility(0);
        }
        else
        {
            super.onBackPressed();
        }

    }
}