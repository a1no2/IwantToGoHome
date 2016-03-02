package com.lifeistech.android.iwanttogohome;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.activeandroid.query.Select;
import com.activeandroid.query.Delete;

import java.util.List;

public class SettingActivity extends AppCompatActivity implements View.OnClickListener, View.OnLongClickListener {
    Button home_btn1, home_btn2, home_btn3, home_btn4;
    int btnID;
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        toolbar = (Toolbar) findViewById(R.id.toolbar_setting);
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(true);


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
    }


    //この画面に戻ってきたら(ry
    public void onResume() {
        super.onResume();

        home_btn1.setText("---");
        home_btn2.setText("---");
        home_btn3.setText("---");
        home_btn4.setText("---");
        setHomeName();


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
            Log.e("DB", db.home_name);
        }
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.home_btn1:
                gmapIntent(1);
                break;
            case R.id.home_btn2:
                gmapIntent(2);
                break;
            case R.id.home_btn3:
                gmapIntent(3);
                break;
            case R.id.home_btn4:
                gmapIntent(4);
                break;

        }
    }

    private void gmapIntent(int id) {
        Intent i = new Intent(SettingActivity.this, RegistrationMapsActivity.class);
        i.putExtra("id", id);
        startActivity(i);

    }


    //長押しで削除
    @Override
    public boolean onLongClick(View v) {
        btnID = v.getId();
        String text = "---";

        switch (btnID) {
            case R.id.home_btn1:
                text = home_btn1.getText().toString();
                break;
            case R.id.home_btn2:
                text = home_btn2.getText().toString();
                break;
            case R.id.home_btn3:
                text = home_btn3.getText().toString();
                break;
            case R.id.home_btn4:
                text = home_btn4.getText().toString();
                break;
        }

        //そのスロットが空なら何もしない
        if (text.equals("---")) {
        } else {
            AlertDialog.Builder dialog = new AlertDialog.Builder(this);
            dialog.setTitle("削除");
            dialog.setMessage("このスロットを空にしても\nよろしいですか？");
            //肯定の処理、削除
            dialog.setPositiveButton("はい", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    home_delete(btnID);     //削除のメソッド
                }
            });
            //否定の処理
            dialog.setNegativeButton("いいえ", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                }
            });
            dialog.show();
        }


        return true;
    }

    //削除のメソッド 貰ったIDの段を削除
    private void home_delete(int id) {
        switch (id) {
            case R.id.home_btn1:
                new Delete().from(HomeDB.class).where("home_id = ?", 1).execute();
                home_btn1.setText("---");
                break;
            case R.id.home_btn2:
                new Delete().from(HomeDB.class).where("home_id = ?", 2).execute();
                home_btn2.setText("---");
                break;
            case R.id.home_btn3:
                new Delete().from(HomeDB.class).where("home_id = ?", 3).execute();
                home_btn3.setText("---");
                break;
            case R.id.home_btn4:
                new Delete().from(HomeDB.class).where("home_id = ?", 4).execute();
                home_btn4.setText("---");
                break;
        }
        Toast.makeText(this, "削除しました", Toast.LENGTH_SHORT).show();
    }


    //つーるばー
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_setting, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        switch (item.getItemId()){
            case android.R.id.home:
                finish();
                break;

        }


        return true;
    }

}
