package com.biyesheji.android.robot.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.biyesheji.android.robot.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * A simple {@link Fragment} subclass.
 */
public class TwoFragment extends BaseFragment {


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
    @BindView(R.id.fragment_two_moShiQieHuan)
    Button fragmentTwoMoShiQieHuan;
    Unbinder unbinder;

    public TwoFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View inflate = inflater.inflate(R.layout.fragment_two, container, false);


        unbinder = ButterKnife.bind(this, inflate);
        return inflate;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @OnClick({R.id.fragment_two_zuoZhuan, R.id.fragment_two_qianJing, R.id.fragment_two_houTui, R.id.fragment_two_youZhuan, R.id.two_xingjinLl, R.id.fragment_two_qianFangun, R.id.fragment_two_houFanGun, R.id.fragment_two_qianPa, R.id.fragment_two_houPa, R.id.fragment_two_fuWoCheng, R.id.fragment_two_yangWoQiZuo, R.id.fragment_two_zuoPingYi, R.id.fragment_two_youPingYi, R.id.fragment_two_zuoTiTui, R.id.fragment_two_youTiTui, R.id.fragment_two_zuoJinLi, R.id.fragment_two_youJinLi, R.id.fragment_two_qiaoXiYang, R.id.fragment_two_qianShouGuanYin, R.id.fragment_two_jieWu, R.id.fragment_two_moShiQieHuan})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.fragment_two_zuoZhuan:
                break;
            case R.id.fragment_two_qianJing:
                break;
            case R.id.fragment_two_houTui:
                break;
            case R.id.fragment_two_youZhuan:
                break;
            case R.id.two_xingjinLl:
                break;
            case R.id.fragment_two_qianFangun:
                break;
            case R.id.fragment_two_houFanGun:
                break;
            case R.id.fragment_two_qianPa:
                break;
            case R.id.fragment_two_houPa:
                break;
            case R.id.fragment_two_fuWoCheng:
                break;
            case R.id.fragment_two_yangWoQiZuo:
                break;
            case R.id.fragment_two_zuoPingYi:
                break;
            case R.id.fragment_two_youPingYi:
                break;
            case R.id.fragment_two_zuoTiTui:
                break;
            case R.id.fragment_two_youTiTui:
                break;
            case R.id.fragment_two_zuoJinLi:
                break;
            case R.id.fragment_two_youJinLi:
                break;
            case R.id.fragment_two_qiaoXiYang:
                break;
            case R.id.fragment_two_qianShouGuanYin:
                break;
            case R.id.fragment_two_jieWu:
                break;
            case R.id.fragment_two_moShiQieHuan:
                break;
        }
    }
}
