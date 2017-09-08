package com.example.mydemo.net.volley;

import android.content.Context;

import com.android.volley.Response;
import com.android.volley.VolleyError;

/**
 * Created by ldh on 2016/4/6 0006.
 */
public abstract class VolleyInterface {

    public Context mContext;
    public static Response.Listener<String> mListener;
    public static Response.ErrorListener mErrorListener;


    public VolleyInterface(Context context, Response.Listener<String> listener, Response.ErrorListener errorListener) {
        this.mContext = context;
        this.mListener = listener;
        this.mErrorListener = errorListener;
    }

    public Response.Listener<String> loadingListener() {
        mListener=new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                //在这里做一些全局配置
                onMySuccess(s);
            }
        };
        return mListener;
    }

    public Response.ErrorListener errorListener() {
        mErrorListener=new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                //全局配置
                onMyError(volleyError);
            }
        };
        return mErrorListener;
    }

    public abstract void onMySuccess(String result);
    public abstract void onMyError(VolleyError error);

}
