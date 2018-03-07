package com.biyesheji.android.robot.ui;


import android.content.Intent;
import android.support.v4.app.FragmentManager;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TableLayout;

import com.biyesheji.android.robot.R;
import com.biyesheji.android.robot.adapter.OneKindAdapter;
import com.biyesheji.android.robot.fragment.OneFragment;
import com.biyesheji.android.robot.fragment.ThreeFragment;
import com.biyesheji.android.robot.fragment.TwoFragment;

import java.util.ArrayList;
import java.util.List;

public class OneKindActivity extends AppCompatActivity {

    private TabLayout tabLayout;
    private ViewPager viewPager;
    private List<Fragment> fragments;
    private List<String> titles;
    private OneKindAdapter oneKindAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_one_kind);

        tabLayout = findViewById(R.id.activity_one_kind_tablayout);
        viewPager = findViewById(R.id.activity_one_kind_viewPager);
        fragments = new ArrayList<>();
        titles = new ArrayList<>();

        titles.add("个性动作模式");
        titles.add("标准动作模式");
        titles.add("语音模式");

        fragments.add(new TwoFragment());
        fragments.add(new OneFragment());
        fragments.add(new ThreeFragment());

        FragmentManager fm = getSupportFragmentManager();

        oneKindAdapter = new OneKindAdapter(fm,fragments,titles);
        viewPager.setAdapter(oneKindAdapter);
        viewPager.setCurrentItem(0);
        tabLayout.setupWithViewPager(viewPager);

    }

}