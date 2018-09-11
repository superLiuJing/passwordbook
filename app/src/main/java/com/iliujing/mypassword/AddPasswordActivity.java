package com.iliujing.mypassword;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
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

/**
 * Created by liuji on 2018/9/7.
 */

public class AddPasswordActivity extends AppCompatActivity {
    EditText nameInput;
    EditText accountInput;
    EditText passwordInput;
    Button addBtn;
    private int idx;
    List<PasswordItem> list;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        idx = getIntent().getIntExtra("index", -1);
        setContentView(R.layout.activity_add_password);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        addBtn = (Button) findViewById(R.id.addBtn);
        nameInput = (EditText) findViewById(R.id.nameInput);
        accountInput = (EditText) findViewById(R.id.accountInput);
        passwordInput = (EditText) findViewById(R.id.passwordInput);
        list = CommonUtils.getList(AddPasswordActivity.this);
        if(idx > -1){
            nameInput.setText(list.get(idx).getName());
            passwordInput.setText(list.get(idx).getPassword());
            accountInput.setText(list.get(idx).getAccount());
        }

        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = nameInput.getText().toString();
                String account = accountInput.getText().toString();
                String password = passwordInput.getText().toString();
                if(StrUtil.isBlank(name) || StrUtil.isBlank(account) || StrUtil.isBlank(password)){
                    Toast.makeText(AddPasswordActivity.this,"请输入完整信息",Toast.LENGTH_LONG).show();
                    return;
                }

                PasswordItem item = new PasswordItem();
                item.setAccount(account);
                item.setName(name);
                item.setPassword(password);
                if(idx != -1){
                    list.remove(idx);
                    list.add(idx,item);
                }else {
                    list.add(0, item);
                }
                CommonUtils.setList(AddPasswordActivity.this,list);
                setResult(Activity.RESULT_OK);
                finish();
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == android.R.id.home)
        {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
