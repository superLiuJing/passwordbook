package com.iliujing.mypassword.helper;

public interface SimpleAuthenticationCallback {
    void onAuthenticationSucceeded(String value);
    void onAuthenticationFail();
}
