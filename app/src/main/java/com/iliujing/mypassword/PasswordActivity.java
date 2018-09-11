package com.iliujing.mypassword;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.iliujing.mypassword.adapter.PasswordAdapter;
import com.iliujing.mypassword.constants.DefaultConstants;
import com.iliujing.mypassword.utils.CommonUtils;
import com.iliujing.mypassword.vo.PasswordItem;

import java.util.Collections;
import java.util.List;

import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.SecureUtil;
import cn.hutool.json.JSONUtil;

/**
 * Created by liuji on 2018/9/7.
 */

public class PasswordActivity extends AppCompatActivity {
    private ListView lv;
    private List<PasswordItem> list;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_password);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        lv = (ListView) findViewById(R.id.lv);
        initData();
    }
    private void initData() {
        list = CommonUtils.getList(this);
        PasswordAdapter adapter = new PasswordAdapter(this,list);
        lv.setAdapter(adapter);
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(PasswordActivity.this,AddPasswordActivity.class);
                intent.putExtra("index",position);
                startActivityForResult(intent,100);
            }
        });
        lv.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
                new AlertDialog.Builder(PasswordActivity.this).setTitle("警告")
                        .setMessage("是否确认删除该条记录?")
                        .setNegativeButton("取消",null)
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                list.remove(position);
                                ((PasswordAdapter)(lv.getAdapter())).notifyDataSetChanged();
                            }
                        }).create().show();
                return true;
            }

        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == 100){
            if(resultCode == Activity.RESULT_OK){
                list.clear();
                list.addAll(CommonUtils.getList(PasswordActivity.this));
                ((PasswordAdapter)(lv.getAdapter())).notifyDataSetChanged();
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.password, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == android.R.id.home)
        {
            finish();
            return true;
        }
        if(item.getItemId() == R.id.action_settings){
            Intent intent = new Intent(this,AddPasswordActivity.class);
            startActivityForResult(intent,100);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
