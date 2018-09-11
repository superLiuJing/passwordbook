package com.iliujing.mypassword.vo;

import java.io.Serializable;

/**
 * Created by liuji on 2018/9/7.
 */

public class PasswordItem implements Serializable {
    private String name;
    private String account;
    private String password;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
