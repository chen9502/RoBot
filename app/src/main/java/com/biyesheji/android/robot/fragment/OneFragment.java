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
public class OneFragment extends BaseFragment {


    @BindView(R.id.one_lianxvBtn)
    Button oneLianxvBtn;
    @BindView(R.id.one_ganenBtn)
    Button oneGanenBtn;
    @BindView(R.id.one_changxiufeiwuBtn)
    Button oneChangxiufeiwuBtn;
    @BindView(R.id.one_feixiangBtn)
    Button oneFeixiangBtn;
    @BindView(R.id.one_diziguiBtn)
    Button oneDiziguiBtn;
    @BindView(R.id.one_yougongfanBtn)
    Button oneYougongfanBtn;
    @BindView(R.id.one_daoliBtn)
    Button oneDaoliBtn;
    @BindView(R.id.one_fragment_other)
    Button oneOther;
    Unbinder unbinder;

    public OneFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_one, container, false);


        unbinder = ButterKnife.bind(this, view);
        return view;
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @OnClick({R.id.one_lianxvBtn, R.id.one_ganenBtn, R.id.one_changxiufeiwuBtn, R.id.one_feixiangBtn, R.id.one_diziguiBtn, R.id.one_yougongfanBtn, R.id.one_daoliBtn})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.one_lianxvBtn:
//                GsonList.gsonList("actioncontrol",Integer.parseInt(bianhao),"default","gexin","default");
                break;
            case R.id.one_ganenBtn:
                break;
            case R.id.one_changxiufeiwuBtn:
                break;
            case R.id.one_feixiangBtn:
                break;
            case R.id.one_diziguiBtn:
                break;
            case R.id.one_yougongfanBtn:
                break;
            case R.id.one_daoliBtn:
                GsonList.gsonList("actioncontrol",255,"default","biaozhun","daoli");
                break;
        }
    }
}
