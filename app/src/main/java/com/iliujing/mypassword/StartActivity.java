package com.iliujing.mypassword;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.KeyguardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.hardware.fingerprint.FingerprintManager;
import android.os.Bundle;
import android.os.CancellationSignal;
import android.security.keystore.KeyProperties;
import android.support.annotation.Nullable;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.iliujing.mypassword.constants.DefaultConstants;
import com.iliujing.mypassword.helper.BiometricSecurityHelper;
import com.iliujing.mypassword.helper.FingerprintHelper;
import com.iliujing.mypassword.helper.SimpleAuthenticationCallback;
import com.iliujing.mypassword.utils.CommonUtils;
import com.iliujing.mypassword.view.BiometricPromptDialog;
import com.iliujing.mypassword.vo.PasswordItem;

import java.util.ArrayList;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;

import cn.hutool.core.util.StrUtil;

/**
 * Created by liuji on 2018/9/8.
 */

public class StartActivity extends Activity implements SimpleAuthenticationCallback {
    EditText inputText;
    Button btn;
    BiometricSecurityHelper fingerprintHelper;
    BiometricPromptDialog dialog;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
        inputText = (EditText) findViewById(R.id.inputText);
        btn = (Button) findViewById(R.id.btn);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String pwd = inputText.getText().toString();
                if(StrUtil.isBlank(pwd) || pwd.length() < 6){
                    new AlertDialog.Builder(StartActivity.this)
                            .setTitle("警告")
                            .setMessage("密码位数不少于6位")
                            .setNegativeButton("我知道了", null).create().show();
                }else{
                    if(CommonUtils.isFirstIn(StartActivity.this)){
                        new AlertDialog.Builder(StartActivity.this)
                                .setTitle("警告")
                                .setMessage("密码只有一次设置机会，不可找回！")
                                .setPositiveButton("我记住了", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        DefaultConstants.setKey(pwd);
                                        CommonUtils.setList(StartActivity.this,new ArrayList<PasswordItem>());
                                        fingerprintHelper.setPurpose(KeyProperties.PURPOSE_ENCRYPT);
                                        fingerprintHelper.setData(pwd);
                                        int i = fingerprintHelper.checkFingerprintAvailable(StartActivity.this);
                                        if(i != 1
                                                || !fingerprintHelper.authenticate()){
                                            new AlertDialog.Builder(StartActivity.this).setTitle("警告")
                                                    .setMessage("指纹加密失败,下次进入验证密码")
                                                    .setNegativeButton("我知道了",null).create().show();
                                        }else{
                                            StartActivity.this.dialog.show();
                                        }
                                    }
                                }).setNegativeButton("再看看", null).create().show();
                    }else{
                        if(CommonUtils.validPwd(StartActivity.this,pwd)){
                            jumpToMainActivity();
                        }else {
                            new AlertDialog.Builder(StartActivity.this)
                                    .setTitle("警告")
                                    .setMessage("密码验证失败，无法进入")
                                    .setNegativeButton("我知道了", null).create().show();
                        }
                    }
                }
            }
        });
        fingerprintHelper = new BiometricSecurityHelper(this);
        fingerprintHelper.setCallback(this);
        fingerprintHelper.generateKey();
        dialog = new BiometricPromptDialog(StartActivity.this, new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fingerprintHelper.stopAuthenticate();
            }
        });
        if(!CommonUtils.isFirstIn(StartActivity.this) && fingerprintHelper.containsToken()) {
            fingerprintHelper.setPurpose(KeyProperties.PURPOSE_DECRYPT);
            if (!fingerprintHelper.authenticate()) {
                new AlertDialog.Builder(StartActivity.this).setTitle("警告")
                        .setMessage("指纹解密失败,请输入密码进入")
                        .setNegativeButton("我知道了", null).create().show();
            }else{
                dialog.show();
            }
        }
    }

    @Override
    public void finish() {
        super.finish();
        fingerprintHelper.stopAuthenticate();
    }

    private void jumpToMainActivity(){
        Intent intent = new Intent(this,MainActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void onAuthenticationSucceeded(String value) {
        if(dialog.isShowing()){
            dialog.dismiss();
        }
        if(StrUtil.isBlank(value)){
            jumpToMainActivity();
        }else{
            if(CommonUtils.validPwd(StartActivity.this,value)){
                jumpToMainActivity();
            }else {
                new AlertDialog.Builder(StartActivity.this)
                        .setTitle("警告")
                        .setMessage("密码验证失败，无法进入")
                        .setNegativeButton("我知道了", null).create().show();
            }
        }
    }

    @Override
    public void onAuthenticationFail() {
        if(dialog.isShowing()){
            dialog.dismiss();
        }
        Toast.makeText(this,"指纹验证失败，请输入密码进入",Toast.LENGTH_LONG).show();
    }
}
