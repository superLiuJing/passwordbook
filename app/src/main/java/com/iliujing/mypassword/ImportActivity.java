package com.iliujing.mypassword;

import android.annotation.TargetApi;
import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.iliujing.mypassword.utils.CommonUtils;
import com.iliujing.mypassword.vo.PasswordItem;

import java.util.List;

import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.CryptoException;

/**
 * Created by liuji on 2018/9/8.
 */

public class ImportActivity extends AppCompatActivity {
    EditText securityText;
    Button importBtn;
    List<PasswordItem> list;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_import);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        securityText = (EditText) findViewById(R.id.securityText);
        importBtn = (Button) findViewById(R.id.importBtn);
        importBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String s = securityText.getText().toString();
                if (StrUtil.isNotBlank(s)) {
                    try {
                        list = CommonUtils.getListByText(s);
                    } catch (CryptoException e) {
                        Toast.makeText(ImportActivity.this, "密文解析失败", Toast.LENGTH_LONG).show();
                        return;
                    }
                    new AlertDialog.Builder(ImportActivity.this).setTitle("信息").setMessage("请选择数据添加方式").setNegativeButton("取消", null)
                            .setNeutralButton("替换到当前", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    CommonUtils.setList(ImportActivity.this, list);
                                    finish();
                                }
                            }).setPositiveButton("添加到当前", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            List<PasswordItem> tmpList = CommonUtils.getList(ImportActivity.this);
                            tmpList.addAll(list);
                            CommonUtils.setList(ImportActivity.this, tmpList);
                            finish();
                        }
                    }).create().show();
                } else {
                    Toast.makeText(ImportActivity.this, "密文不能为空", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
