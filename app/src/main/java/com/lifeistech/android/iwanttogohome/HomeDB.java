package com.lifeistech.android.iwanttogohome;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;

@Table(name = "home_table")
public class HomeDB extends Model{

    //家のID 1～4 最後に保存した家を保存するかもね
    @Column(name = "home_id")
    public Integer homeId;

    //家の名前 文字制限あり?
    @Column(name = "home_name")
    public String home_name;

    //緯度
    @Column(name = "latitude")
    public double latitude;

    //経度
    @Column(name = "longitude")
    public double longitude;

    //ルート出した総回数 実装まだ
    @Column(name = "NumberOFruns")
    public int numberOFruns;

    //文字列として呼ばれたら
    @Override
    public String toString(){
        return home_name;
    }
}
