package com.lifeistech.android.iwanttogohome;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.activeandroid.query.Select;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, View.OnLongClickListener {
    Button home_btn1, home_btn2, home_btn3, home_btn4;
    Button setting_btn;
    TextView kae_text;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



        home_btn1 = (Button) findViewById(R.id.home_btn1);
        home_btn1.setOnClickListener(this);
        home_btn1.setOnLongClickListener(this);
        home_btn2 = (Button) findViewById(R.id.home_btn2);
        home_btn2.setOnClickListener(this);
        home_btn2.setOnLongClickListener(this);
        home_btn3 = (Button) findViewById(R.id.home_btn3);
        home_btn3.setOnClickListener(this);
        home_btn3.setOnLongClickListener(this);
        home_btn4 = (Button) findViewById(R.id.home_btn4);
        home_btn4.setOnClickListener(this);
        home_btn4.setOnLongClickListener(this);
        setting_btn = (Button) findViewById(R.id.setting_btn);
        setting_btn.setOnClickListener(this);




    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.home_btn1:
                goHomeGmap_intent(1);
                break;
            case R.id.home_btn2:
                goHomeGmap_intent(2);
                break;
            case R.id.home_btn3:
                goHomeGmap_intent(3);
                break;
            case R.id.home_btn4:
                goHomeGmap_intent(4);
                break;

            case R.id.setting_btn:
                Intent i = new Intent(MainActivity.this, SettingActivity.class);
                startActivity(i);
                break;
        }
    }

    private void goHomeGmap_intent(int id){
        Intent i = new Intent(MainActivity.this,GoHomeMapsActivity.class);
        i.putExtra("id",id);
        startActivity(i);
    }


    @Override
    public boolean onLongClick(View v) {
        return true;
    }


    //この画面に戻ってきたら(ry
    public void onResume() {
        super.onResume();

        home_btn1.setText("---");
        home_btn2.setText("---");
        home_btn3.setText("---");
        home_btn4.setText("---");
        setHomeName();

        //家が未登録だと押せない
        //他にいい書き方ある説が濃厚
        if (home_btn1.getText().equals("---")){
            home_btn1.setEnabled(false);
        } else {
            home_btn1.setEnabled(true);
        }
        if (home_btn2.getText().equals("---")){
            home_btn2.setEnabled(false);
        } else {
            home_btn2.setEnabled(true);
        }
        if (home_btn3.getText().equals("---")){
            home_btn3.setEnabled(false);
        } else {
            home_btn3.setEnabled(true);
        }
        if (home_btn4.getText().equals("---")){
            home_btn4.setEnabled(false);
        } else {
            home_btn4.setEnabled(true);
        }


    }

    private void setHomeName() {
        List<HomeDB> list = new Select().from(HomeDB.class).execute();
        for (HomeDB db : list) {
            switch (db.homeId) {
                case 1:
                    home_btn1.setText(db.home_name);
                    break;
                case 2:
                    home_btn2.setText(db.home_name);
                    break;
                case 3:
                    home_btn3.setText(db.home_name);
                    break;
                case 4:
                    home_btn4.setText(db.home_name);
                    break;
            }
//            Log.e("DB",db.home_name);
        }
    }


}
