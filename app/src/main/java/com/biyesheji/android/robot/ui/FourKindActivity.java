package com.biyesheji.android.robot.ui;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.biyesheji.android.robot.R;
import com.biyesheji.android.robot.socket.GsonList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class FourKindActivity extends BaseActivity {

    @BindView(R.id.fragment_two_zuoZhuan)
    Button fragmentTwoZuoZhuan;
    @BindView(R.id.fragment_two_qianJing)
    Button fragmentTwoQianJing;
    @BindView(R.id.fragment_two_houTui)
    Button fragmentTwoHouTui;
    @BindView(R.id.fragment_two_youZhuan)
    Button fragmentTwoYouZhuan;
    private TextView yuandiTv;
    private LinearLayout yuandiOneLl;
    private LinearLayout yuandiTwoLl;
    private TextView zhitiTv;
    private LinearLayout zhitiLl;
    private TextView wudaoTv;
    private LinearLayout wudaoLl;
    private TextView otherTv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_two);
        ButterKnife.bind(this);

        initView();

        //让他消失
        miss();
    }

    private void miss() {
        yuandiTv.setVisibility(View.GONE);
        yuandiOneLl.setVisibility(View.GONE);
        yuandiTwoLl.setVisibility(View.GONE);
        zhitiTv.setVisibility(View.GONE);
        zhitiLl.setVisibility(View.GONE);
        wudaoTv.setVisibility(View.GONE);
        wudaoLl.setVisibility(View.GONE);
        otherTv.setVisibility(View.GONE);
    }

    private void initView() {
        yuandiTv = findViewById(R.id.two_yuandiTv);
        yuandiOneLl = findViewById(R.id.two_yuandiOneLl);
        yuandiTwoLl = findViewById(R.id.two_yuandiTwoLl);
        zhitiTv = findViewById(R.id.two_zhitiTv);
        zhitiLl = findViewById(R.id.two_zhitiLl);
        wudaoTv = findViewById(R.id.two_wudaoTv);
        wudaoLl = findViewById(R.id.two_wudaoLl);
        otherTv = findViewById(R.id.two_otherTv);
    }

    @OnClick({R.id.fragment_two_zuoZhuan, R.id.fragment_two_qianJing, R.id.fragment_two_houTui, R.id.fragment_two_youZhuan})
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
        }
    }
}
