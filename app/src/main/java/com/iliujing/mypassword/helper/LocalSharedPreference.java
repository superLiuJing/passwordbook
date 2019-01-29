package com.iliujing.mypassword.helper;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;

public class LocalSharedPreference {

    public final String dataKeyName = "data";
    public final String IVKeyName = "IV";
    private SharedPreferences preferences;

    public LocalSharedPreference(Context context) {
        preferences = context.getSharedPreferences("sample", Activity.MODE_PRIVATE);
    }

    public String getData(String keyName) {
        //同样，在读取SharedPreferences数据前要实例化出一个SharedPreferences对象
        return preferences.getString(keyName, "");
    }

    public boolean storeData(String key, String data) {
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(key, data);
        return editor.commit();
    }

    public boolean containsKey(String key) {
        return !TextUtils.isEmpty(getData(key));
    }
}
