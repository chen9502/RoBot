package com.biyesheji.android.robot.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.biyesheji.android.robot.R;

public class MainActivity extends BaseActivity {

    private Button one;
    private Button two;

    private String kind;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //获取传过来的用户类型
        Intent intent = getIntent();
        kind = intent.getStringExtra("kind");



        one = findViewById(R.id.activity_main_oneBtn);

        one.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                /**
                 * 这里的设想是点击蓝色框框，跳出个popupwindow，里面蓝牙蓝牙连接,当连接成功后执行下面的操作
                 * 这个业务逻辑先不写,假设连接成功，先跳转
                 */

                switch (kind){
                    case "1":
                        Intent intent = new Intent(MainActivity.this,OneKindActivity.class);
                        startActivity(intent);
                        break;
                    case "2":
                        Intent intent1 = new Intent(MainActivity.this,TwoKindActivity.class);
                        startActivity(intent1);
                        break;
                    case "3":
                        Intent intent2 = new Intent(MainActivity.this,ThreeKindActivity.class);
                        startActivity(intent2);
                        break;
                    case "4":
                        Intent intent3 = new Intent(MainActivity.this,FourKindActivity.class);
                        startActivity(intent3);
                        break;
                    case "5":
                        Intent intent4 = new Intent(MainActivity.this,FiveKindActivity.class);
                        startActivity(intent4);
                        break;
                }
            }
        });

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
