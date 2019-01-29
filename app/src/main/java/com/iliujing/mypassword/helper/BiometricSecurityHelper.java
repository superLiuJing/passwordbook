package com.iliujing.mypassword.helper;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;

import com.iliujing.mypassword.helper.biometric.BiometricPromptHelper;

@TargetApi(Build.VERSION_CODES.P)
public class BiometricSecurityHelper {

    private FingerprintHelper fingerprintHelper;
    private BiometricPromptHelper biometricPromptHelper;
    public BiometricSecurityHelper(Context context){
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.P){
            // 生物识别
            biometricPromptHelper = new BiometricPromptHelper(context);
        }else if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            // 指纹识别
            fingerprintHelper = new FingerprintHelper(context);
        }
    }

    public void setCallback(SimpleAuthenticationCallback callback) {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.P){
            // 生物识别
            biometricPromptHelper.setCallback(callback);
        }else if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            // 指纹识别
            fingerprintHelper.setCallback(callback);
        }
    }

    public void generateKey(){
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.P){
            // 生物识别
            biometricPromptHelper.generateKey();
        }else if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            // 指纹识别
            fingerprintHelper.generateKey();
        }
    }

    public boolean containsToken() {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.P){
            // 生物识别
            return biometricPromptHelper.containsToken();
        }else if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            // 指纹识别
            return fingerprintHelper.containsToken();
        }
        return false;
    }

    public void setPurpose(int purpose){
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.P){
            // 生物识别
            biometricPromptHelper.setPurpose(purpose);
        }else if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            // 指纹识别
            fingerprintHelper.setPurpose(purpose);
        }
    }

    public void setData(String data){
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.P){
            // 生物识别
            biometricPromptHelper.setData(data);
        }else if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            // 指纹识别
            fingerprintHelper.setData(data);
        }
    }

    public boolean authenticate(){
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.P){
            // 生物识别
            return biometricPromptHelper.authenticate();
        }else if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            // 指纹识别
            return fingerprintHelper.authenticate();
        }
        return false;
    }

    public void stopAuthenticate() {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.P){
            // 生物识别
            biometricPromptHelper.stopAuthenticate();
        }else if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            // 指纹识别
            fingerprintHelper.stopAuthenticate();
        }
    }

    public int checkFingerprintAvailable(Context ctx) {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.P){
            // 生物识别
            return 1;
        }else if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            // 指纹识别
            return fingerprintHelper.checkFingerprintAvailable(ctx);
        }
        return -1;
    }

}
