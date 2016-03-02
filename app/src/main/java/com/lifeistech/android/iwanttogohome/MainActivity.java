package com.lifeistech.android.iwanttogohome;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.activeandroid.query.Select;
import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Random;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, View.OnLongClickListener {
    Button home_btn1, home_btn2, home_btn3, home_btn4;
    Button setting_btn;
    TextView kae_text;

    Handler yureText_Hand;
    Runnable yureText_Runna;
    int[] tapCount;
    String[] setKAERU;
    boolean RubberBand_bool;

    Handler end_hand;
    Runnable end_runnab;

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


        //帰 を揺らすだけ
        kae_text = (TextView) findViewById(R.id.kae_text);
        kae_text.setOnClickListener(this);
        yureText_Hand = new Handler();
        yureText_Runna = new Runnable() {
            @Override
            public void run() {
                if (RubberBand_bool) {
                    YoYo.with(Techniques.Shake)
                            .duration(800)
                            .playOn(kae_text);

                    Random ran = new Random();
                    yureText_Hand.postDelayed(yureText_Runna, ((ran.nextInt(10) + 5) * 1000));
                }
            }
        };
        yureText_Hand.removeCallbacks(yureText_Runna);
        yureText_Hand.postDelayed(yureText_Runna, 5000);

        RubberBand_bool = true;

        //タップ回数、10で[0]リセットかけて+1、[1]が5で+1
        tapCount = new int[]{0, 0, 0};
        setKAERU = new String[]{"帰", "帰帰", "帰帰帰", "帰\n帰帰帰", "帰\n帰帰帰\n帰"};

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


            case R.id.kae_text:


                if (RubberBand_bool) {
                    YoYo.with(Techniques.RubberBand)
                            .duration(300)
                            .playOn(kae_text);
                    tapCount[0]++;
                    hueruyo();
                }

                break;
        }
    }

    private void goHomeGmap_intent(int id) {
        Intent i = new Intent(MainActivity.this, GoHomeMapsActivity.class);
        i.putExtra("id", id);
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
        if (home_btn1.getText().equals("---")) {
            home_btn1.setEnabled(false);
        } else {
            home_btn1.setEnabled(true);
        }
        if (home_btn2.getText().equals("---")) {
            home_btn2.setEnabled(false);
        } else {
            home_btn2.setEnabled(true);
        }
        if (home_btn3.getText().equals("---")) {
            home_btn3.setEnabled(false);
        } else {
            home_btn3.setEnabled(true);
        }
        if (home_btn4.getText().equals("---")) {
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
        }
    }


    private void hueruyo() {

        //10回タップしたら増える
        if (tapCount[0] == 10 && tapCount[1] < 4 && tapCount[2] < 2) {
            tapCount[0] = 0;
            tapCount[1]++;
            kae_text.setText(setKAERU[tapCount[1]]);

            //50回でサイズ+
        } else if (tapCount[0] == 10 && tapCount[1] == 4 && tapCount[2] == 0) {
            tapCount[0] = 0;
            tapCount[1] = 0;
            tapCount[2]++;
            kae_text.setText(setKAERU[0]);
            kae_text.setTextSize(60);

            //2回目の50タップでサイズ+　もう増えない
        } else if (tapCount[0] == 10 && tapCount[1] == 4 && tapCount[2] == 1) {
            tapCount[2]++;
            kae_text.setText(setKAERU[0]);
            kae_text.setTextSize(100);

            //55 + 66 タップでアプリを落とす
        } else if (tapCount[0] == 30 && tapCount[2] == 2) {
            RubberBand_bool = false;
            kae_text.setTextSize(44);
            kae_text.setText("秘儀 アプリ落とし");
            YoYo.with(Techniques.FadeIn)
                    .duration(3000)
                    .playOn(kae_text);
            end_hand = new Handler();
            end_runnab = new Runnable() {
                @Override
                public void run() {
                    int[] i = new int[]{1,1};
                    i[2] = 0;

                }
            };
            end_hand.removeCallbacks(end_runnab);
            end_hand.postDelayed(end_runnab, 5000);

        }


    }


}
