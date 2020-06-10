package com.example.weather;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import java.util.Set;

public class citycard_data {       //实现轻量级的数据存储

    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;

    Context context;

    public citycard_data(Context c, String name)
    {
        context = c;
        sharedPreferences = context.getSharedPreferences(name, Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
    }

    public void putValue(String key, Set<String> value)
    {
        editor.putStringSet(key, value);
        editor.commit();
    }

    public Set<String> getvalue(String key)
    {
        return  sharedPreferences.getStringSet(key, null);
    }

    public void putWeaValue(String key, String value)  //键值对是城市——天气
    {
        editor.putString(key, value);
        editor.commit();
    }

    public String getWeavalue(String key)
    {
        return  sharedPreferences.getString(key, null);
    }

    boolean isEmptyWea(String key)
    {
        return sharedPreferences.getString(key, null) == null;
    }

    boolean isEmpty()
    {
        return sharedPreferences.getStringSet("city", null) == null;
    }
}
