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

public class FiveKindActivity extends BaseActivity {

    @BindView(R.id.fragment_two_qiaoXiYang)
    Button fragmentTwoQiaoXiYang;
    @BindView(R.id.fragment_two_qianShouGuanYin)
    Button fragmentTwoQianShouGuanYin;
    @BindView(R.id.fragment_two_jieWu)
    Button fragmentTwoJieWu;
    private TextView xingjinTv;
    private LinearLayout xingjinLl;
    private TextView yuandiTv;
    private LinearLayout yuandiOneLl;
    private LinearLayout yuandiTwoLl;
    private TextView zhitiTv;
    private LinearLayout zhitiLl;
    private TextView otherTv;
    private Button otherBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_two);
        ButterKnife.bind(this);

        initView();

        //消失
        miss();
    }

    private void miss() {
        xingjinTv.setVisibility(View.GONE);
        xingjinLl.setVisibility(View.GONE);
        yuandiTv.setVisibility(View.GONE);
        yuandiOneLl.setVisibility(View.GONE);
        yuandiTwoLl.setVisibility(View.GONE);
        zhitiTv.setVisibility(View.GONE);
        zhitiLl.setVisibility(View.GONE);
        otherTv.setVisibility(View.GONE);
        otherBtn.setVisibility(View.GONE);
    }

    private void initView() {
        xingjinTv = findViewById(R.id.two_xingjinTv);
        xingjinLl = findViewById(R.id.two_xingjinLl);
        yuandiTv = findViewById(R.id.two_yuandiTv);
        yuandiOneLl = findViewById(R.id.two_yuandiOneLl);
        yuandiTwoLl = findViewById(R.id.two_yuandiTwoLl);
        zhitiTv = findViewById(R.id.two_zhitiTv);
        zhitiLl = findViewById(R.id.two_zhitiLl);
        otherTv = findViewById(R.id.two_otherTv);

    }

    @OnClick({R.id.fragment_two_qiaoXiYang, R.id.fragment_two_qianShouGuanYin, R.id.fragment_two_jieWu})
    public void onViewClicked(View view) {
        switch (view.getId()) {
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
