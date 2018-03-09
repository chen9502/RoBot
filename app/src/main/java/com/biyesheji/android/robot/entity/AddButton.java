package com.biyesheji.android.robot.entity;

/**
 * Created by Administrator on 2017/5/5 0005.
 */

public class AddButton {

    int databaseid;
    String mingcheng;
    String bianhao;

    public AddButton(int databaseid, String mingcheng, String bianhao) {
        this.databaseid = databaseid;
        this.mingcheng = mingcheng;
        this.bianhao = bianhao;
    }

    public int getId() {
        return databaseid;
    }

    public void setId(int id) {
        this.databaseid = id;
    }

    public String getMingcheng() {
        return mingcheng;
    }

    public void setMingcheng(String mingcheng) {
        this.mingcheng = mingcheng;
    }

    public String getBianhao() {
        return bianhao;
    }

    public void setBianhao(String bianhao) {
        this.bianhao = bianhao;
    }
}
