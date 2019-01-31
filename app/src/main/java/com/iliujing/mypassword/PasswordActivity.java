package com.iliujing.mypassword;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;

import com.iliujing.mypassword.adapter.PasswordAdapter;
import com.iliujing.mypassword.utils.CommonUtils;
import com.iliujing.mypassword.vo.PasswordItem;

import java.util.ArrayList;
import java.util.List;

import cn.hutool.core.util.StrUtil;

/**
 * Created by liuji on 2018/9/7.
 */

public class PasswordActivity extends AppCompatActivity {
    private ListView lv;
    private EditText searchInput;
    private List<PasswordItem> list;
    PasswordAdapter adapter;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_password);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        lv = findViewById(R.id.lv);
        initData();
    }

    private void addListHeader(){
        searchInput = (EditText) LayoutInflater.from(this).inflate(R.layout.search_input,null);
        searchInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) {
                String txt = s.toString();
                List<PasswordItem> tmp ;
                if(StrUtil.isBlank(txt)){
                    tmp = new ArrayList<>(list);
                }else {
                    tmp = new ArrayList<>(list.size());
                    for (PasswordItem item : list) {
                        if (item.getName().contains(txt)) {
                            tmp.add(item);
                        }
                    }
                }
                adapter.updateList(tmp);
            }
        });
        lv.addHeaderView(searchInput);
    }
    private void initData() {
        addListHeader();
        list = CommonUtils.getList(this);
        adapter = new PasswordAdapter(this,list);
        lv.setAdapter(adapter);
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(position == 0){return;}
                Intent intent = new Intent(PasswordActivity.this,AddPasswordActivity.class);
                intent.putExtra("index",position - 1);
                startActivityForResult(intent,100);
            }
        });
        lv.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
                if(position == 0){return true;}
                new AlertDialog.Builder(PasswordActivity.this).setTitle("警告")
                        .setMessage("是否确认删除该条记录?")
                        .setNegativeButton("取消",null)
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                list.remove(position - 1);
                                adapter.notifyDataSetChanged();
                                CommonUtils.setList(PasswordActivity.this,list);
                            }
                        }).create().show();
                return true;
            }

        });
        List<String> data = new ArrayList<>(list.size());
        if(list.size() != 0){
            for (PasswordItem item:list) {
                data.add(item.getName());
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == 100){
            if(resultCode == Activity.RESULT_OK){
                list.clear();
                list.addAll(CommonUtils.getList(PasswordActivity.this));
                adapter.notifyDataSetChanged();
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
