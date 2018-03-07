package com.biyesheji.android.robot.ui;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.biyesheji.android.robot.R;

public class LoginActivity extends AppCompatActivity {

    private EditText oneEdt;
    private EditText twoEdt;
    private String zhanghao;
    private String mima;

    private String kind;

    private Button makeSureBtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        oneEdt = findViewById(R.id.activity_login_oneEdt);
        twoEdt = findViewById(R.id.activity_login_twoEdt);
        makeSureBtn = findViewById(R.id.activity_login_makeSureBtn);


        //对输入账号监听
        oneEdt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                zhanghao = oneEdt.getText().toString();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        //对密码进行监听
        twoEdt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                mima = twoEdt.getText().toString();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        makeSureBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //这个判断是要改的，我现在是只要不是空就行
                if (zhanghao != null || mima != null){
                    switch (zhanghao){
                        case "1":
                            kind = "1";
                            break;
                        case "2":
                            kind = "2";
                            break;
                        case "3":
                            kind = "3";
                            break;
                        case "4":
                            kind = "4";
                            break;
                        case "5":
                            kind = "5";
                            break;
                    }
                    Intent intent = new Intent(LoginActivity.this,MainActivity.class);
                    intent.putExtra("kind",kind);
                    startActivity(intent);
                }
            }
        });
    }
}
