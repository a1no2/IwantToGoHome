package com.lifeistech.android.iwanttogohome;

import android.content.DialogInterface;
import android.content.Intent;
import android.location.Location;
import android.location.LocationManager;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.activeandroid.query.Delete;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class RegistrationMapsActivity extends FragmentActivity implements OnMapReadyCallback, View.OnClickListener, GoogleMap.OnMapClickListener {
    private GoogleMap googleMap;
    MarkerOptions options;

    Button getCL_btn, registration_btn;
    int btnId;                  //登録するボタンの場所
    Location loc;               //現在地のマネージャ
    double latitude = 91;       //現在地の緯度
    double longitude = 181;     //      　経度
    String homeName_str = "";  //家の名前
//    boolean homeName_bool;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration_maps);

        Intent i = getIntent();
        btnId = i.getIntExtra("id", -1);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        options = new MarkerOptions();
        googleMap = mapFragment.getMap();
        googleMap.setOnMapClickListener(this);


        getCL_btn = (Button) findViewById(R.id.getCurrentLocation_btn);
        getCL_btn.setOnClickListener(this);
        registration_btn = (Button) findViewById(R.id.registration_btn);
        registration_btn.setOnClickListener(this);

    }

    @Override
    public void onMapReady(GoogleMap map) {
        googleMap = map;

        //設定の取得
        UiSettings settings = googleMap.getUiSettings();
        settings.setZoomControlsEnabled(true);

        //日本 全体からスタートさせる
        try {
            googleMap.setMyLocationEnabled(true);
            googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(37.015468, 135.852307), 4.0f));

        } catch (SecurityException e) {

        }


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            //現在地取得ボタン
            case R.id.getCurrentLocation_btn:
                LocationManager locman
                        = (LocationManager) this.getSystemService(this.LOCATION_SERVICE);
                try {
                    loc = locman.getLastKnownLocation("gps");

                    if (loc == null){
                        Toast.makeText(RegistrationMapsActivity.this,"現在地が取れていなので\n左上の現在地取得ボタンを押してください",Toast.LENGTH_LONG).show();
                        Toast.makeText(RegistrationMapsActivity.this,"また、位置情報をオンにしてください",Toast.LENGTH_LONG).show();
                    } else {
                        latitude = loc.getLatitude();//緯度
                        longitude = loc.getLongitude();//経度

                        //ピン置く
                        googleMap.clear();      //前のピン消す
                        LatLng now = new LatLng(latitude, longitude);
                        googleMap.addMarker(new MarkerOptions().position(now));
                        googleMap.moveCamera(CameraUpdateFactory.newLatLng(now));

                        //ピンの場所まで移動のアニメーション
                        CameraPosition nowCP = new CameraPosition.Builder()
                                .target(now).zoom(17.0f)
                                .bearing(0).tilt(25).build();
                        googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(nowCP));
                    }
                } catch (SecurityException e) {
                    Log.d("現在地取得", e.toString());
                }


                break;


            //登録ボタン
            case R.id.registration_btn:

                //場所の指定がされているか
                if (latitude < 91 && longitude < 181) {
                    getHomeName();      //家の名前をダイアログ出して聞く
                    //聞いたら保存
                } else {
                    Toast.makeText(this, "家の場所が指定されていません", Toast.LENGTH_SHORT).show();
                }


                break;
        }

    }


    private void getHomeName() {
        //ダイアログで家の名前を聞く
        final EditText editText = new EditText(RegistrationMapsActivity.this);
        new AlertDialog.Builder(RegistrationMapsActivity.this)
                .setIcon(android.R.drawable.ic_menu_edit)
                .setTitle("帰る場所の\n名前を入力してください")
                .setView(editText)
                .setPositiveButton("決定", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        homeName_str = editText.getText().toString();

                        if (homeName_str.equals("")) {       //家の名前が空なら
                            Toast.makeText(
                                    RegistrationMapsActivity.this,
                                    "家の名前を入力してください",
                                    Toast.LENGTH_SHORT).show();
                        } else {
                            //DBに保存
                            new Delete().from(HomeDB.class).where("home_id = ?", btnId).execute();
                            HomeDB homeDB = new HomeDB();
                            homeDB.homeId = btnId;              //表示するボタンの位置
                            homeDB.home_name = homeName_str;    //家の名前
                            homeDB.latitude = latitude;         //緯度
                            homeDB.longitude = longitude;       //経度
                            homeDB.numberOFruns = 0;            //帰りたいボタンを押した数　未実装
                            homeDB.save();
                            Toast.makeText(RegistrationMapsActivity.this, homeName_str + "\nが登録されました", Toast.LENGTH_SHORT).show();
                            finish();

                        }
                    }
                })
                .setNegativeButton("キャンセル", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        homeName_str = "";
                    }
                }).show();
    }


    @Override
    public void onMapClick(LatLng latLng) {
        googleMap.clear();      //ぴんのさくじょー

//        googleMap.addMarker(new MarkerOptions().position(latLng));
        //この一行なんなんなんあん　ピンが中央にくるようにずれてくれる見たい
//        googleMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
        options.position(latLng);
        googleMap.addMarker(options);

        longitude = latLng.longitude;
        latitude = latLng.latitude;
    }
}
