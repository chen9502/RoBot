package com.biyesheji.android.robot.entity;

import org.xutils.db.annotation.Column;
import org.xutils.db.annotation.Table;

/**
 * Created by Administrator on 2018/3/9.
 */

@Table(name = "robot_user")
public class UserAccount {
    @Column(name = "id",isId = true)
    private  int id;
    @Column(name = "userName")
    private String userName;
    @Column(name = "pwd")
    private String passWord;
    @Column(name = "authority")
    private String authority;

    public UserAccount() {
    }

    public UserAccount(int id, String userName, String passWord, String authority) {
        this.id = id;
        this.userName = userName;
        this.passWord = passWord;
        this.authority = authority;
    }

    public UserAccount(String userName, String passWord, String authority) {
        this.userName = userName;
        this.passWord = passWord;
        this.authority = authority;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassWord() {
        return passWord;
    }

    public void setPassWord(String passWord) {
        this.passWord = passWord;
    }

    public String getAuthority() {
        return authority;
    }

    public void setAuthority(String authority) {
        this.authority = authority;
    }
}
