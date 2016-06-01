package com.example.mydemo;

import android.content.Context;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.example.mydemo.base.BaseApplication;

/**
 * Created by ldh on 2016/3/31 0031.
 */
public class AppContext extends BaseApplication{

    private static RequestQueue queues;
    private static AppContext instance;
    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        queues = Volley.newRequestQueue(this);
    }

    public static RequestQueue getHttpQueses(){
        return queues;
    }

    public static Context getInstance() {
        return instance;
    }
}
