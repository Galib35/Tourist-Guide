package com.example.ghuraghuri;

import android.net.Uri;

import com.denzcoskun.imageslider.models.SlideModel;
import com.google.firebase.auth.FirebaseAuth;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

public class Constant {
    public static char ch='n';
    public static char point='n';
    public static char error='n';
    public static String curr_uid= "";
    public static String totalRatings=null;
    public static String rat=null;
    public static String role=null;
    public static boolean fromVlogList=false;
    public static boolean isFullscreen=false;
    public static YouTubePlayer player;
    public static YouTubePlayerView youTubeView;
   /* public static String uName=null;
    public static String rev=null;
    public static String urating= null;
    public static String udate=null;*/


    public static int totalSteps=2;

    public static ArrayList<String> tmp_name=new ArrayList<>();
    public static ArrayList<String> tmp_rating=new ArrayList<>();
    public static ArrayList<String>tmp_tag=new ArrayList<>();
    public static ArrayList<String> tmp_plc_id=new ArrayList<>();

    public static ArrayList<String> plc_name=new ArrayList<>();
    public static ArrayList<String> rating=new ArrayList<>();
    public static ArrayList<String >tag=new ArrayList<>();
    public static ArrayList<String> plc_id=new ArrayList<>();
    public static ArrayList<String> key=new ArrayList<>();


    public static ArrayList<String> spot_name=new ArrayList<>();
    public static ArrayList<String> spot_description=new ArrayList<>();
    public static ArrayList<String> url=new ArrayList<>();
    public static ArrayList<String> phone=new ArrayList<>();
    public static ArrayList<String> uName=new ArrayList<>();
    public static ArrayList<String> rev=new ArrayList<>();
    public static ArrayList<String> urating=new ArrayList<>();
    public static ArrayList<String> udate=new ArrayList<>();

    public static ArrayList<Uri> uris=new ArrayList<>();
    public static ArrayList<String> imgTitle=new ArrayList<>();
    public static ArrayList<String> imgUrl=new ArrayList<>();

    public static HashMap<String,ArrayList<String> > mp=new HashMap<>();

   // public static ArrayList<String> longitude=new ArrayList<>();
    public static ArrayList<String> search_name=new ArrayList<>();
    public static ArrayList<String> attraction=new ArrayList<>();

    public static ArrayList<String> histName=new ArrayList<>();
    public static ArrayList<String> histDate=new ArrayList<>();

    public static ArrayList<String> fvname=new ArrayList<>();
    public static ArrayList<String> fvTag=new ArrayList<>();
    public static ArrayList<String> fvRating=new ArrayList<>();
    public static ArrayList<String> fvPlaceId=new ArrayList<>();

    public static ArrayList<String> where=new ArrayList<>();
    public static ArrayList<String> todo=new ArrayList<>();
    public static ArrayList<String> step=new ArrayList<>();

    public static ArrayList<String> productKey=new ArrayList<>();
    public static ArrayList<String> thumbnail=new ArrayList<>();
    public static ArrayList<String> productRating=new ArrayList<>();
    public static ArrayList<String> productPrice=new ArrayList<>();
    public static ArrayList<String> productDescription=new ArrayList<>();
    public static ArrayList<String> productFeatures=new ArrayList<>();
    public static ArrayList<String> productName=new ArrayList<>();
    public static ArrayList<String> productImg=new ArrayList<>();
    public static ArrayList<String> productQuantity=new ArrayList<>();
    public static ArrayList<String> productSubtotal=new ArrayList<>();
    public static ArrayList<String> productSpecification=new ArrayList<>();
    public static ArrayList<String> maxQuantity=new ArrayList<>();
    public static ArrayList<String> cartQuantity=new ArrayList<>();
    public static ArrayList<String> productId=new ArrayList<>();

    //for weather
    public static ArrayList<String> temperature=new ArrayList<>();
    public static ArrayList<String> feelsLike=new ArrayList<>();
    public static ArrayList<String> humidity=new ArrayList<>();
    public static ArrayList<String> wind=new ArrayList<>();
    public static ArrayList<String> rain=new ArrayList<>();
    public static ArrayList<StringBuilder> weatherStatus=new ArrayList<>();
    public static ArrayList<StringBuilder> weatherDescription=new ArrayList<>();
    public static ArrayList<String> weatherIcon=new ArrayList<>();
    public static ArrayList<String> weatherDate=new ArrayList<>();
    public static ArrayList<String> weatherTime=new ArrayList<>();


    public static ArrayList<String> videoTitle=new ArrayList<>();
    public static ArrayList<String> videoId=new ArrayList<>();
    public static ArrayList<String> videoDate=new ArrayList<>();
    public static ArrayList<String> vlogger=new ArrayList<>();
    public static ArrayList<String> videoLocation=new ArrayList<>();


    public static ArrayList<String> packageName=new ArrayList<>();
    public static ArrayList<String> packageCost=new ArrayList<>();
    public static ArrayList<String> packageLocation=new ArrayList<>();
    public static ArrayList<String> packageDuration=new ArrayList<>();
    public static ArrayList<String> packageAgencyName=new ArrayList<>();
    public static ArrayList<String> journeyDate=new ArrayList<>();
    public static ArrayList<String> touristCapacity=new ArrayList<>();
    public static ArrayList<String> packageThumbnail=new ArrayList<>();
    public static ArrayList<String> packageId=new ArrayList<>();
    public static ArrayList<String> packageAgencyId=new ArrayList<>();
    //public static ArrayList<HashMap<String,String> > packageSegment=new ArrayList<>();


    public static ArrayList<String> touristName=new ArrayList<>();
    public static ArrayList<String> touristContact=new ArrayList<>();
    public static ArrayList<String> touristMember=new ArrayList<>();
    public static ArrayList<String> date=new ArrayList<>();
    public static ArrayList<String> time=new ArrayList<>();
    public static ArrayList<String> packageStatus=new ArrayList<>();

    public static ArrayList<String> blogTitle=new ArrayList<>();
    public static ArrayList<String> blogLocation=new ArrayList<>();
    public static ArrayList<String> blogDetails=new ArrayList<>();
    public static ArrayList<String> blogDate=new ArrayList<>();
    public static ArrayList<String> bloggerName=new ArrayList<>();
    public static ArrayList<String> blogThumb=new ArrayList<>();
    public static ArrayList<String> blogId=new ArrayList<>();

    public static ArrayList<String> orderId=new ArrayList<>();
    public static ArrayList<String> orderDate=new ArrayList<>();
    public static ArrayList<String> orderTime=new ArrayList<>();
    public static ArrayList<String> customerName=new ArrayList<>();
    public static ArrayList<String> orderTotal=new ArrayList<>();
    public static ArrayList<String> customerPhoneNo=new ArrayList<>();
    public static ArrayList<String> customerAddress=new ArrayList<>();




    public static int stepCount=0;


    public interface onAccept{
        void onAccept(int position);
    }

    public interface onReject{
        void onReject(int position);
    }


}
