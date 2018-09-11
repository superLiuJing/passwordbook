package com.iliujing.mypassword;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.iliujing.mypassword.constants.DefaultConstants;
import com.iliujing.mypassword.utils.CommonUtils;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.core.util.StrUtil;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {


    private SeekBar seekBar;
    private TextView passwordNumText;
    private CheckBox allowNum;
    private CheckBox allowStr;
    private CheckBox allowSpecial;
    private TextView output;
    private Button btn;
    private Button copyBtn;

    private int passwordNum = 8;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        initData();
    }

    private void initData(){
        seekBar = (SeekBar) findViewById(R.id.seekBar);
        seekBar.setProgress(passwordNum);
        passwordNumText = (TextView) findViewById(R.id.passwordNumText);
        allowNum = (CheckBox) findViewById(R.id.allowNumber);
        allowStr = (CheckBox) findViewById(R.id.allowStr);
        allowSpecial = (CheckBox) findViewById(R.id.allowSpecial);
        output = (TextView) findViewById(R.id.output);
        btn = (Button) findViewById(R.id.btn);
        copyBtn = (Button) findViewById(R.id.copyBtn);
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                passwordNum = i;
                passwordNumText.setText(String.valueOf(i));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setRandomPassword();
            }
        });
        copyBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String s = output.getText().toString();
                if(StrUtil.isNotBlank(s)) {
                    ClipboardManager myClipboard = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
                    ClipData data = ClipData.newPlainText("text", s);
                    myClipboard.setPrimaryClip(data);
                    Toast.makeText(MainActivity.this,"已复制到剪贴板",Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        setRandomPassword();
    }

    private void setRandomPassword(){
        StringBuilder builder = new StringBuilder();
        if(allowNum.isChecked()){
            builder.append(DefaultConstants.NUM);
        }
        if(allowSpecial.isChecked()){
            builder.append(DefaultConstants.SPEC);
        }
        if(allowStr.isChecked()){
            builder.append(DefaultConstants.STR);
        }
        output.setText(RandomUtil.randomString(builder.toString(),passwordNum));
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.pwd_book) {
            Intent intent = new Intent(this,PasswordActivity.class);
            startActivity(intent);
        } else if (id == R.id.add_account) {
            Intent intent = new Intent(this,AddPasswordActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_slideshow) {
            String securityData = CommonUtils.getSecurityData(MainActivity.this);
            if(securityData == null){
                Toast.makeText(MainActivity.this,"没有待导出数据",Toast.LENGTH_LONG).show();
            }else {
                ClipboardManager myClipboard = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
                ClipData data = ClipData.newPlainText("text", securityData);
                myClipboard.setPrimaryClip(data);
                Toast.makeText(MainActivity.this, "已复制到剪贴板", Toast.LENGTH_LONG).show();
            }
        } else if (id == R.id.nav_manage) {
            Intent intent = new Intent(MainActivity.this,ImportActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_share) {
            Toast.makeText(MainActivity.this, "功能还在开发中...", Toast.LENGTH_LONG).show();
        } else if (id == R.id.nav_send) {
            Toast.makeText(MainActivity.this, "功能还在开发中...", Toast.LENGTH_LONG).show();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
