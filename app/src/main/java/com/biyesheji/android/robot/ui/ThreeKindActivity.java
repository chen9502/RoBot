package com.biyesheji.android.robot.ui;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.biyesheji.android.robot.R;
import com.biyesheji.android.robot.socket.GsonList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ThreeKindActivity extends BaseActivity {

    @BindView(R.id.fragment_two_zuoZhuan)
    Button fragmentTwoZuoZhuan;
    @BindView(R.id.fragment_two_qianJing)
    Button fragmentTwoQianJing;
    @BindView(R.id.fragment_two_houTui)
    Button fragmentTwoHouTui;
    @BindView(R.id.fragment_two_youZhuan)
    Button fragmentTwoYouZhuan;
    @BindView(R.id.fragment_two_qianFangun)
    Button fragmentTwoQianFangun;
    @BindView(R.id.fragment_two_houFanGun)
    Button fragmentTwoHouFanGun;
    @BindView(R.id.fragment_two_qianPa)
    Button fragmentTwoQianPa;
    @BindView(R.id.fragment_two_houPa)
    Button fragmentTwoHouPa;
    @BindView(R.id.fragment_two_fuWoCheng)
    Button fragmentTwoFuWoCheng;
    @BindView(R.id.fragment_two_yangWoQiZuo)
    Button fragmentTwoYangWoQiZuo;
    @BindView(R.id.fragment_two_zuoPingYi)
    Button fragmentTwoZuoPingYi;
    @BindView(R.id.fragment_two_youPingYi)
    Button fragmentTwoYouPingYi;
    @BindView(R.id.fragment_two_zuoTiTui)
    Button fragmentTwoZuoTiTui;
    @BindView(R.id.fragment_two_youTiTui)
    Button fragmentTwoYouTiTui;
    @BindView(R.id.fragment_two_zuoJinLi)
    Button fragmentTwoZuoJinLi;
    @BindView(R.id.fragment_two_youJinLi)
    Button fragmentTwoYouJinLi;
    @BindView(R.id.fragment_two_qiaoXiYang)
    Button fragmentTwoQiaoXiYang;
    @BindView(R.id.fragment_two_qianShouGuanYin)
    Button fragmentTwoQianShouGuanYin;
    @BindView(R.id.fragment_two_jieWu)
    Button fragmentTwoJieWu;
    private TextView xingjinTv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_two);
        ButterKnife.bind(this);
    }

    @OnClick({R.id.fragment_two_qianFangun, R.id.fragment_two_houFanGun, R.id.fragment_two_qianPa, R.id.fragment_two_houPa, R.id.fragment_two_fuWoCheng, R.id.fragment_two_yangWoQiZuo, R.id.fragment_two_zuoPingYi, R.id.fragment_two_youPingYi, R.id.fragment_two_zuoTiTui, R.id.fragment_two_youTiTui, R.id.fragment_two_zuoJinLi, R.id.fragment_two_youJinLi, R.id.fragment_two_qiaoXiYang, R.id.fragment_two_qianShouGuanYin, R.id.fragment_two_jieWu})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.fragment_two_zuoZhuan:
                GsonList.gsonList("actioncontrol",255,"default","biaozhun","zuozhuan");
                break;
            case R.id.fragment_two_qianJing:
                GsonList.gsonList("actioncontrol",255,"default","biaozhun","qianjing");
                break;
            case R.id.fragment_two_houTui:
                GsonList.gsonList("actioncontrol",255,"default","biaozhun","houtui");
                break;
            case R.id.fragment_two_youZhuan:
                GsonList.gsonList("actioncontrol",255,"default","biaozhun","youzhuan");
                break;
            case R.id.fragment_two_qianFangun:
                GsonList.gsonList("actioncontrol",255,"default","biaozhun","qiangunfan");
                break;
            case R.id.fragment_two_houFanGun:
                GsonList.gsonList("actioncontrol",255,"default","biaozhun","hougunfan");
                break;
            case R.id.fragment_two_qianPa:
                GsonList.gsonList("actioncontrol",255,"default","biaozhun","qianpaxia");
                break;
            case R.id.fragment_two_houPa:
                GsonList.gsonList("actioncontrol",255,"default","biaozhun","houpaxia");
                break;
            case R.id.fragment_two_fuWoCheng:
                GsonList.gsonList("actioncontrol",255,"default","biaozhun","fuwocheng");
                break;
            case R.id.fragment_two_yangWoQiZuo:
                GsonList.gsonList("actioncontrol",255,"default","biaozhun","yangwoqizuo");
                break;
            case R.id.fragment_two_zuoPingYi:
                GsonList.gsonList("actioncontrol",255,"default","biaozhun","zoupingyi");
                break;
            case R.id.fragment_two_youPingYi:
                GsonList.gsonList("actioncontrol",255,"default","biaozhun","youpingyi");
                break;
            case R.id.fragment_two_zuoTiTui:
                GsonList.gsonList("actioncontrol",255,"default","biaozhun","zuotitui");
                break;
            case R.id.fragment_two_youTiTui:
                GsonList.gsonList("actioncontrol",255,"default","biaozhun","youtitui");
                break;
            case R.id.fragment_two_zuoJinLi:
                GsonList.gsonList("actioncontrol",255,"default","biaozhun","zuojingli");
                break;
            case R.id.fragment_two_youJinLi:
                GsonList.gsonList("actioncontrol",255,"default","biaozhun","youjingli");
                break;
            case R.id.fragment_two_qiaoXiYang:
                GsonList.gsonList("actioncontrol",255,"default","biaozhun","qiaoxiyang");
                break;
            case R.id.fragment_two_qianShouGuanYin:
                GsonList.gsonList("actioncontrol",255,"default","biaozhun","qianshouguanyin");
                break;
            case R.id.fragment_two_jieWu:
                GsonList.gsonList("actioncontrol",255,"default","biaozhun","jiewu");
                break;
        }
    }
}
