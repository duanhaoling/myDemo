package com.example.myapplication;

import android.app.Application;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

/**
 * Created by ldh on 2016/3/31 0031.
 */
public class MyApplication extends Application{

    private static RequestQueue queues;

    @Override
    public void onCreate() {
        super.onCreate();
        queues = Volley.newRequestQueue(this);
    }

    public static RequestQueue getHttpQueses(){
        return queues;
    }

}
