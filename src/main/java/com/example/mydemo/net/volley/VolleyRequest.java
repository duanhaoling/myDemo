package com.example.mydemo.net.volley;

import android.content.Context;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.toolbox.StringRequest;
import com.example.mydemo.AppContext;

import java.util.Map;

/**
 * volley 的StringRequest的简单封装
 * Created by ldh on 2016/4/6 0006.
 */
public class VolleyRequest {
    public static StringRequest stringRequest;
    public static Context context;

    public static void requsetGet(Context context,String url,String tag,VolleyInterface vif) {
        AppContext.getHttpQueses().cancelAll(tag);
        stringRequest = new StringRequest(Request.Method.GET, url, vif.loadingListener(), vif.errorListener());
        stringRequest.setTag(tag);
        AppContext.getHttpQueses().add(stringRequest);
        AppContext.getHttpQueses().start();
    }


    public static void requsetPost(Context context, String url, String tag, final Map<String, String> params, VolleyInterface vif) {
        AppContext.getHttpQueses().cancelAll(tag);
        stringRequest = new StringRequest(url, vif.loadingListener(), vif.errorListener()){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                return params;
            }
        };
        stringRequest.setTag(tag);
        AppContext.getHttpQueses().add(stringRequest);
        AppContext.getHttpQueses().start();
    }
}
