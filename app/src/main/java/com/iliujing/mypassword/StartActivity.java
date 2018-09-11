package com.iliujing.mypassword;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.iliujing.mypassword.constants.DefaultConstants;
import com.iliujing.mypassword.utils.CommonUtils;
import com.iliujing.mypassword.vo.PasswordItem;

import java.util.ArrayList;

import cn.hutool.core.util.StrUtil;

/**
 * Created by liuji on 2018/9/8.
 */

public class StartActivity extends Activity {
    EditText inputText;
    Button btn;
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
                                        jumpToMainActivity();
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
    }
    private void jumpToMainActivity(){
        Intent intent = new Intent(this,MainActivity.class);
        startActivity(intent);
        finish();
    }
}
