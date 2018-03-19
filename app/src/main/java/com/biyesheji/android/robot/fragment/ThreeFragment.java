package com.biyesheji.android.robot.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.biyesheji.android.robot.R;
import com.biyesheji.android.robot.socket.GsonList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * A simple {@link Fragment} subclass.
 */
public class ThreeFragment extends BaseFragment {


    @BindView(R.id.fragment_three_zengDaVic)
    Button fragmentThreeZengDaVic;
    @BindView(R.id.fragment_three_jianXiaoVic)
    Button fragmentThreeJianXiaoVic;
    @BindView(R.id.fragment_three_kaiQiVic)
    Button fragmentThreeKaiQiVic;
    @BindView(R.id.fragment_three_zanTingVic)
    Button fragmentThreeZanTingVic;
    @BindView(R.id.fragment_three_guanBiVic)
    Button fragmentThreeGuanBiVic;
    Unbinder unbinder;

    public ThreeFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_three, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @OnClick({R.id.fragment_three_zengDaVic, R.id.fragment_three_jianXiaoVic, R.id.fragment_three_kaiQiVic, R.id.fragment_three_zanTingVic, R.id.fragment_three_guanBiVic})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.fragment_three_zengDaVic:
                GsonList.gsonList("yingliangtiaozheng",1,"ADJUST_RAISE","0xff","0xff");
                break;
            case R.id.fragment_three_jianXiaoVic:
                GsonList.gsonList("yingliangtiaozheng",-1,"ADJUST_LOWER","0xff","0xff");
                break;
            case R.id.fragment_three_kaiQiVic:
                GsonList.gsonList("actioncontrol",255,"default","setting","voicecmdkai");
                break;
            case R.id.fragment_three_zanTingVic:
                GsonList.gsonList("actioncontrol",255,"default","setting","voicecmdguan");
                break;
            case R.id.fragment_three_guanBiVic:
                GsonList.gsonList("actioncontrol",255,"default","setting","voicecmdguan");
                break;
        }
    }
}
