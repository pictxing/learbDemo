package com.superfuns.healthcode.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.superfuns.healthcode.App;

public class Sptools {

    /**
     * 保存在手机里面的文件名
     */
    private static final String FILE_NAME = "CONFIG";

    /**
     * 保存数据的方法，我们需要拿到保存数据的具体类型，然后根据类型调用不同的保存方法
     *
     * @param key
     * @param value
     */
    public static void setValue(String key, Object value) {
        if (value == null) {
            return;
        }

        String type = value.getClass().getSimpleName();

        SharedPreferences sp = App.getContext().getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();

        if ("String".equals(type)) {
            editor.putString(key, (String) value);
        } else if ("Integer".equals(type)) {
            editor.putInt(key, (Integer) value);
        } else if ("Boolean".equals(type)) {
            editor.putBoolean(key, (Boolean) value);
        } else if ("Float".equals(type)) {
            editor.putFloat(key, (Float) value);
        } else if ("Long".equals(type)) {
            editor.putLong(key, (Long) value);
        }

        editor.commit();
    }


    /**
     * 得到保存数据的方法，我们根据默认值得到保存的数据的具体类型，然后调用相对于的方法获取值
     *
     * @param key
     * @param defaultValue
     * @return
     */
    public static Object getValue(String key, Object defaultValue) {

        String type = defaultValue.getClass().getSimpleName();
        SharedPreferences sp = App.getContext().getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE);

        if ("String".equals(type)) {
            return sp.getString(key, (String) defaultValue);
        } else if ("Integer".equals(type)) {
            return sp.getInt(key, (Integer) defaultValue);
        } else if ("Boolean".equals(type)) {
            return sp.getBoolean(key, (Boolean) defaultValue);
        } else if ("Float".equals(type)) {
            return sp.getFloat(key, (Float) defaultValue);
        } else if ("Long".equals(type)) {
            return sp.getLong(key, (Long) defaultValue);
        }

        return defaultValue;
    }
}
