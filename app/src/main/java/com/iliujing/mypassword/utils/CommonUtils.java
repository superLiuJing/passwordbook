package com.iliujing.mypassword.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Base64;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.iliujing.mypassword.constants.DefaultConstants;
import com.iliujing.mypassword.vo.PasswordItem;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.CryptoException;
import cn.hutool.crypto.SecureUtil;

/**
 * Created by liuji on 2018/9/7.
 */

public class CommonUtils {
    public static List<PasswordItem> getList(Context context) throws CryptoException {
        List<PasswordItem> list = null;
        SharedPreferences sp = context.getSharedPreferences(DefaultConstants.DB_NAME, Context.MODE_PRIVATE);
        String info = sp.getString(DefaultConstants.DB_ITEM_NAME, null);
        return getListByText(info);
    }

    public static List<PasswordItem> getListByText(String info) throws CryptoException{
        List<PasswordItem> list;
        if (StrUtil.isBlank(info)) {
            list = new ArrayList<>();
        } else {
            String s = SecureUtil.aes(DefaultConstants.getKey())
                    .decryptStrFromBase64(info, DefaultConstants.DB_CHARSET);
            Gson gson = new Gson();

            list = gson.fromJson(s,new TypeToken<List<PasswordItem>>() {}.getType());
        }
        return list;
    }

    public static void setList(Context context,List<PasswordItem> list) throws CryptoException{
        if(list == null){
            return;
        }
        Gson gson = new Gson();
        String s = gson.toJson(list);
        String result = SecureUtil.aes(DefaultConstants.getKey())
                .encryptBase64(s, DefaultConstants.DB_CHARSET.name());
        SharedPreferences sp = context.getSharedPreferences(DefaultConstants.DB_NAME, Context.MODE_PRIVATE);
        sp.edit().putString(DefaultConstants.DB_ITEM_NAME,result).apply();
    }

    public static String getSecurityData(Context context){
        SharedPreferences sp = context.getSharedPreferences(DefaultConstants.DB_NAME, Context.MODE_PRIVATE);
        return sp.getString(DefaultConstants.DB_ITEM_NAME, null);
    }
    public static boolean validPwd(Context context,String pwd){
        DefaultConstants.setKey(pwd);
        try {
            getList(context);
        }catch (Exception e){
            return false;
        }
        return true;
    }

    public static boolean isFirstIn(Context context){
        SharedPreferences sp = context.getSharedPreferences(DefaultConstants.DB_NAME, Context.MODE_PRIVATE);
        String info = sp.getString(DefaultConstants.DB_ITEM_NAME, null);
        if(info == null){
            return true;
        }
        return false;
    }
}
