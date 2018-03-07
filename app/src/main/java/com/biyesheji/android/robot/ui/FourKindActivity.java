package com.biyesheji.android.robot.ui;

import android.support.design.widget.TabLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.biyesheji.android.robot.R;

public class FourKindActivity extends AppCompatActivity {

    private TextView yuandiTv;
    private LinearLayout yuandiOneLl;
    private LinearLayout yuandiTwoLl;
    private TextView zhitiTv;
    private LinearLayout zhitiLl;
    private TextView wudaoTv;
    private LinearLayout wudaoLl;
    private TextView otherTv;
    private Button otherBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_two);

        initView();

        //让他消失
        miss();
    }

    private void miss() {
        yuandiTv.setVisibility(View.INVISIBLE);
        yuandiOneLl.setVisibility(View.INVISIBLE);
        yuandiTwoLl.setVisibility(View.INVISIBLE);
        zhitiTv.setVisibility(View.INVISIBLE);
        zhitiLl.setVisibility(View.INVISIBLE);
        wudaoTv.setVisibility(View.INVISIBLE);
        wudaoLl.setVisibility(View.INVISIBLE);
        otherTv.setVisibility(View.INVISIBLE);
        otherBtn.setVisibility(View.INVISIBLE);
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
        otherBtn = findViewById(R.id.two_otherBtn);
    }
}
