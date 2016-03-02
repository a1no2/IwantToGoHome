package com.lifeistech.android.iwanttogohome;

import android.content.Intent;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationManager;
import android.os.Handler;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.activeandroid.query.Select;
import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.directions.route.AbstractRouting;
import com.directions.route.Route;
import com.directions.route.RouteException;
import com.directions.route.Routing;
import com.directions.route.RoutingListener;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class GoHomeMapsActivity extends FragmentActivity implements OnMapReadyCallback, RoutingListener {
    private GoogleMap googleMap;
    MarkerOptions options;
    Location loc;
    private ArrayList<Polyline> polylines;
    Routing routing;

    TextView time_text;
    int homeId;
    double homeLatitude, homeLongitude;     //いな説じー
    double nowLatitude, nowLongitude;       //らいふょ
    String homeName_str;
    LatLng start, end;

    int time_goHome;
    private Handler handler;
    private Runnable setTimeText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_go_home_maps);

        Intent i = getIntent();
        homeId = i.getIntExtra("id", -1);

        //DB
        List<HomeDB> list = new Select().from(HomeDB.class).where("home_id = ?", homeId).execute();
        for (HomeDB homeDB : list) {
            end = new LatLng(homeDB.latitude, homeDB.longitude);
            homeName_str = homeDB.home_name;
        }

        //Map関係
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        options = new MarkerOptions();

        //ルート関係
        polylines = new ArrayList<>();


        time_text = (TextView)findViewById(R.id.time_text);






    }


    @Override
    public void onMapReady(GoogleMap map) {
        googleMap = map;

        //設定の取得
        UiSettings settings = googleMap.getUiSettings();
        settings.setZoomControlsEnabled(true);
        try {
            googleMap.setMyLocationEnabled(true);
        } catch (SecurityException e) {
        }

        getRoute();


    }

    private void getRoute() {

        LocationManager locman
                = (LocationManager) this.getSystemService(this.LOCATION_SERVICE);
        try {
            googleMap.setMyLocationEnabled(true);
            loc = locman.getLastKnownLocation("gps");
            start = new LatLng(loc.getLatitude(), loc.getLongitude());
//            nowLatitude = loc.getLatitude();        //緯度
//            nowLongitude = loc.getLongitude();      //経
        } catch (SecurityException e) {
            Log.d("現在地取得", e.toString());
        }

        options.position(start);
        googleMap.addMarker(options);
        options.position(end);
        options.title(homeName_str);
        googleMap.addMarker(options);


        //ピンの場所まで移動のアニメーション
        CameraPosition Camera = new CameraPosition.Builder()
                .target(start).zoom(12.0f)
                .bearing(0).tilt(25).build();
        googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(Camera));


        //ルート
        routing = new Routing.Builder()
                .travelMode(Routing.TravelMode.WALKING)
                .withListener(this)
                .waypoints(start, end)
                .build();
        routing.execute();





    }



    //ルート関係
    @Override
    public void onRoutingFailure(RouteException e) {
        Log.d("無理じゃ", e.toString());
        e.printStackTrace();

    }

    @Override
    public void onRoutingStart() {
        Log.d("start", "はじまる");

    }

    @Override
    public void onRoutingSuccess(ArrayList<Route> route, int shortestRouteIndex) {

        time_goHome = route.get(0).getDurationValue();        //移動時間(秒
//        Toast.makeText(GoHomeMapsActivity.this,time + "" ,Toast.LENGTH_LONG).show();


        Log.d("aa","ss");
        //現在時刻
//        Time time = new Time();
        Date nowTime = new Date(System.currentTimeMillis() + (time_goHome * 1000));
        DateFormat formatter = new SimpleDateFormat("MM月dd日 HH時mm分\nには " + homeName_str + " に");

        // フォーマット
        final String nowText = formatter.format(nowTime);
//        Toast.makeText(GoHomeMapsActivity.this,nowText + "" ,Toast.LENGTH_LONG).show();





        Log.d("帰宅所要時間", time_goHome + "");

        int h, m, s;
        String time_str = "";
        if (time_goHome > 3600) {
            h = time_goHome / 3600;
            time_str += h + "時間";
            time_goHome -= (h * 3600);
        }
        if (time_goHome > 60) {
            m = time_goHome / 60;
            time_str += m + "分";
            time_goHome -= (m * 60);
        }
        time_str += time_goHome + "秒";
        s = time_goHome;
//        Toast.makeText(GoHomeMapsActivity.this, time_str, Toast.LENGTH_LONG).show();

        time_text.setText(nowText + "");


        handler = new Handler();
        setTimeText = new Runnable() {
            public void run() {

                time_text.setVisibility(View.VISIBLE);
                YoYo.with(Techniques.SlideInDown)
                        .duration(1000)
                        .playOn(time_text);


            }
        };

        handler.removeCallbacks(setTimeText);
        handler.postDelayed(setTimeText, 2000);






        //線
        polylines = new ArrayList<>();
        for (int i = 0; i < route.size(); i++) {
            PolylineOptions polyOptions = new PolylineOptions();
            polyOptions.width(10 + i * 3);
            polyOptions.color(Color.BLUE);
            polyOptions.addAll(route.get(i).getPoints());
            Polyline polyline = googleMap.addPolyline(polyOptions);
            polylines.add(polyline);

        }

    }

    @Override
    public void onRoutingCancelled() {
        Log.d("cancel", "しゅごい");
    }
}
