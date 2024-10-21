package com.example.ghuraghuri;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.FullscreenListener;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.YouTubePlayerListener;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.options.IFramePlayerOptions;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView;

import kotlin.Unit;
import kotlin.jvm.functions.Function0;

public class VlogAdapter extends RecyclerView.Adapter<VlogAdapter.YouTubePlayerViewHolder> {
    Context context;
    FrameLayout full;
    Activity activity;
    RecyclerView recyclerView;

    public VlogAdapter(Context context) {
        this.context = context;
    }

    public VlogAdapter(Context context, FrameLayout full, Activity activity, RecyclerView recyclerView) {
        this.context = context;
        this.full = full;
        this.activity = activity;
        this.recyclerView = recyclerView;
    }

    @NonNull
    @Override
    public YouTubePlayerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(context).inflate(R.layout.vlog_tem_design,parent,false);
        return new YouTubePlayerViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull YouTubePlayerViewHolder holder, int position) {
       // holder.setVideoId(Constant.videoId.get(position),full,context,activity,recyclerView);
        holder.title.setText(Constant.videoTitle.get(position));
        holder.spot.setText(Constant.videoLocation.get(position));
        holder.author.setText(Constant.vlogger.get(position));
        holder.date.setText(Constant.videoDate.get(position));

        String currentVideoId=Constant.videoId.get(position);
        Constant.youTubeView=holder.youTubePlayerView;
        //System.out.println(Constant.videoId.get(position));

        YouTubePlayerListener listener=new AbstractYouTubePlayerListener() {
            @Override
            public void onReady(@NonNull YouTubePlayer youTubePlayer) {
                //player=youTubePlayer;
                Constant.player=youTubePlayer;
                if(!currentVideoId.isEmpty())
                {
                    youTubePlayer.cueVideo(currentVideoId,0);
                }

            }
        };

        IFramePlayerOptions iFramePlayerOptions = null;

        if(Constant.fromVlogList){
            iFramePlayerOptions = new IFramePlayerOptions.Builder()
                    .controls(1)
                    .fullscreen(1)
                    .autoplay(0)
                    .build();
        }
        else{
            iFramePlayerOptions = new IFramePlayerOptions.Builder()
                    .controls(1)
                    .autoplay(0)
                    .build();

        }


        holder.youTubePlayerView.initialize(listener,true,iFramePlayerOptions);

        holder.youTubePlayerView.addFullscreenListener(new FullscreenListener() {
            @Override
            public void onEnterFullscreen(@NonNull View fullscreenView, @NonNull Function0<Unit> exitFullscreen) {
                Constant.isFullscreen=true;
                holder.youTubePlayerView.setVisibility(View.GONE);
                full.setVisibility(View.VISIBLE);
                recyclerView.setVisibility(View.GONE);
                full.addView(fullscreenView);
                // Change to landscape mode programmatically
                activity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
                //SCREEN_ORIENTATION_LANDSCAPE
                View decorView = activity.getWindow().getDecorView();
                int uiOptions = View.SYSTEM_UI_FLAG_FULLSCREEN | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION;
                decorView.setSystemUiVisibility(uiOptions);


            }


            @Override
            public void onExitFullscreen() {
                Constant.isFullscreen=false;
                holder.youTubePlayerView.setVisibility(View.VISIBLE);
                full.setVisibility(View.GONE);
                recyclerView.setVisibility(View.VISIBLE);
                full.removeAllViews();
                // Change back to the default orientation (usually portrait)
                activity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED);
                View decorView = activity.getWindow().getDecorView();
                decorView.setSystemUiVisibility(0);

            }
        });

    }

    @Override
    public int getItemCount() {
        return Constant.videoId.size();
    }

    public static class YouTubePlayerViewHolder extends RecyclerView.ViewHolder{

        YouTubePlayerView youTubePlayerView;
        TextView title,spot,author,date;

        public YouTubePlayerViewHolder(@NonNull View itemView) {
            super(itemView);
            youTubePlayerView=itemView.findViewById(R.id.vlog_youtube_player_view);
            title=itemView.findViewById(R.id.vlog_title);
            spot=itemView.findViewById(R.id.vlog_spot_name);
            author=itemView.findViewById(R.id.vloger);
            date=itemView.findViewById(R.id.vlog_date);


        }


    }
}
