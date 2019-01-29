package com.iliujing.mypassword.constants;

import java.nio.charset.Charset;

import cn.hutool.crypto.SecureUtil;

/**
 * Created by liuji on 2018/9/7.
 */

public class DefaultConstants {
    private DefaultConstants() {
    }

    public static final String STR = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
    public static final String NUM = "0123456789";
    public static final String SPEC = "!@#$%^&*();',./~[]{}|:<>?";

    public static final String DB_NAME = "db";
    public static final String DB_ITEM_NAME = "item";
    public static final String DB_KEY = "key";
    public static final Charset DB_CHARSET = Charset.forName("UTF-8");

    private static String key;
    private static final int KEY_LENGTH = 32;

    public static byte[] getKey() {
        byte[] bytes = key.getBytes(DB_CHARSET);
        byte[] result = new byte[KEY_LENGTH];
        for(int i = 0;i<result.length;i++){
            result[i] = bytes[i%(bytes.length)];
        }
        return result;
    }

    public static void setKey(String key) {
        DefaultConstants.key = key;
    }
}
