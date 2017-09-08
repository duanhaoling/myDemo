package com.example.mydemo.net.volleydemo;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.example.mydemo.AppContext;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by ldh on 2016/4/18 0018.
 */
public class MyVolley {
    private Context mContext;
    private RequestQueue mQueue = AppContext.getHttpQueses();


    //get请求
    private void loadGetStr(String url) {

        StringRequest srReq = new StringRequest(Request.Method.GET, url,
                new StrListener(), new StrErrListener()) {

            protected final String TYPE_UTF8_CHARSET = "charset=UTF-8";

            // 重写parseNetworkResponse方法改变返回头参数解决乱码问题
            // 主要是看服务器编码，如果服务器编码不是UTF-8的话那么就需要自己转换，反之则不需要
            @Override
            protected Response<String> parseNetworkResponse(
                    NetworkResponse response) {
                try {
                    String type = response.headers.get("HTTP.CONTENT_TYPE");
                    if (type == null) {
                        type = TYPE_UTF8_CHARSET;
                        response.headers.put("HTTP.CONTENT_TYPE", type);
                    } else if (!type.contains("UTF-8")) {
                        type += ";" + TYPE_UTF8_CHARSET;
                        response.headers.put("HTTP.CONTENT_TYPE", type);
                    }
                } catch (Exception e) {
                }
                return super.parseNetworkResponse(response);
            }
        };
        srReq.setShouldCache(true); // 控制是否缓存
        startVolley(srReq);
    }


    // post请求
    private void loadPostJson(String url) {
        // 第二个参数说明:
        // Constructor which defaults to GET if jsonRequest is null, POST
        // otherwise.
        // 默认情况下设成null为get方法,否则为post方法。
        JsonObjectRequest srReq = new JsonObjectRequest(url, null,
                new JsonListener(), new StrErrListener()) {

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = new HashMap<String, String>();
                map.put("w", "2459115");
                map.put("u", "f");
                return map;
            }
        };
        srReq.setShouldCache(false); // 控制是否缓存
        startVolley(srReq);
    }


    /**
     * 第三第四个参数分别用于指定允许图片最大的宽度和高度，如果指定的网络图片的宽度或高度大于这里的最大值，则会对图片进行压缩，
     * 指定成0的话就表示不管图片有多大，都不会进行压缩。
     *
     * param url
     *            图片地址
     * param listener
     * param maxWidth
     *            指定允许图片最大的宽度
     * param maxHeight
     *            指定允许图片最大的高度
     * param decodeConfig
     *            指定图片的颜色属性，Bitmap.Config下的几个常量.
     * param errorListener
     */
    private void getImageRequest(final ImageView iv, String url) {
        ImageRequest imReq = new ImageRequest(url, new Response.Listener<Bitmap>() {

            @Override
            public void onResponse(Bitmap arg0) {
                iv.setImageBitmap(arg0);
            }
        }, 60, 60, Bitmap.Config.ARGB_8888, new StrErrListener());
        startVolley(imReq);
    }

    // Str请求成功回调
    private class StrListener implements Response.Listener<String> {

        @Override
        public void onResponse(String arg0) {
            Log.e("tag", arg0);

        }

    }
    // 共用失败回调
    private class StrErrListener implements Response.ErrorListener {

        @Override
        public void onErrorResponse(VolleyError arg0) {
            Toast.makeText(mContext,
                    "VolleyErrorHelper.getMessage(arg0, mContext)",
                    Toast.LENGTH_LONG).show();
        }

    }

    private class JsonListener implements Response.Listener<JSONObject> {
        @Override
        public void onResponse(JSONObject jsonObject) {

        }
    }

    // Gson请求成功回调
    private class GsonListener implements Response.Listener<ErrorRsp> {

        @Override
        public void onResponse(ErrorRsp arg0) {
            Toast.makeText(mContext, arg0.toString(), Toast.LENGTH_LONG).show();
        }

    }

    // 添加及开始请求
    private void startVolley(Request req) {
        req.setTag("My Tag");
        // 设置超时时间
         req.setRetryPolicy(new DefaultRetryPolicy(20 * 1000, 1, 1.0f));
        // 将请求加入队列
        mQueue.add(req);
        // 开始发起请求
        mQueue.start();
    }

}
