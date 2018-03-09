package com.biyesheji.android.robot.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.biyesheji.android.robot.MyApp;
import com.biyesheji.android.robot.R;
import com.biyesheji.android.robot.entity.UserAccount;

import org.xutils.DbManager;
import org.xutils.ex.DbException;
import org.xutils.x;

import java.util.List;

public class LoginActivity extends BaseActivity {

    private EditText oneEdt;
    private EditText twoEdt;
    private String zhanghao;
    private String mima;

    private String kind;
    private UserAccount ua;
    private Button makeSureBtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        oneEdt = findViewById(R.id.activity_login_oneEdt);
        twoEdt = findViewById(R.id.activity_login_twoEdt);
        makeSureBtn = findViewById(R.id.activity_login_makeSureBtn);



        makeSureBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //这个判断是要改的，我现在是只要不是空就行

                /**
                 * 登陆，判断账号
                 */
                zhanghao  = oneEdt.getText().toString();
                mima = twoEdt.getText().toString();
                if (zhanghao != null || mima != null){
                    //验证不为null后，循环判断账号和数据库中的账号是否匹配，数据库暂时不写，先用假数据代替

                    DbManager db = x.getDb(MyApp.getApp().getDaoConfig());
                    List<UserAccount> all;
                    try {
                        all = db.findAll(UserAccount.class);
                        for (int i = 0; i < all.size(); i++) {
                            UserAccount account = all.get(i);
                            if(account.getUserName().equals(zhanghao)&&account.getPassWord().equals(mima)){
                                ua=account;
                                break;
                            }
                        }
                    } catch (DbException e) {
                        e.printStackTrace();
                        Toast.makeText(LoginActivity.this, "数据库炸了", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    if(ua!=null){
                        switch (ua.getAuthority()){
                            case "1":
                                startActivity(new Intent(LoginActivity.this,OneKindActivity.class));
                                finish();
                                break;
                            case "2":
                                startActivity(new Intent(LoginActivity.this,TwoKindActivity.class));
                                finish();
                                break;
                            case "3":
                                startActivity(new Intent(LoginActivity.this,ThreeKindActivity.class));
                                finish();
                                break;
                            case "4":
                                startActivity(new Intent(LoginActivity.this,FourKindActivity.class));
                                finish();
                                break;
                            case "5":
                                startActivity(new Intent(LoginActivity.this,FiveKindActivity.class));
                                finish();
                                break;
                        }
                    }else{
                        Toast.makeText(LoginActivity.this, "请检查账号和密码是否输入正确", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }
}
