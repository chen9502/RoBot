package com.biyesheji.android.robot.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.biyesheji.android.robot.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class OneFragment extends Fragment {

    private Button lianxvdongzuoBtn;
    private Button ganenBtn;
    private Button changxiufeiwuBtn;
    private Button feixiangBtn;
    private Button diziguiBtn;
    private Button yougongfanBtn;
    private Button daoliBtn;
    private Button moshiqiehuanBtn;

    public OneFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_one, container, false);

        //初始化控件
        initView(view);

        return view;
    }

    private void initView(View v) {
        lianxvdongzuoBtn = v.findViewById(R.id.one_lianxvBtn);
        ganenBtn = v.findViewById(R.id.one_ganenBtn);
        changxiufeiwuBtn = v.findViewById(R.id.one_changxiufeiwuBtn);
        feixiangBtn = v.findViewById(R.id.one_feixiangBtn);
        diziguiBtn = v.findViewById(R.id.one_diziguiBtn);
        yougongfanBtn = v.findViewById(R.id.one_yougongfanBtn);
        daoliBtn = v.findViewById(R.id.one_daoliBtn);
        changxiufeiwuBtn = v.findViewById(R.id.one_changxiufeiwuBtn);
    }

}
