package com.example.mydemo.net.volley;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.NetworkImageView;
import com.android.volley.toolbox.StringRequest;
import com.example.mydemo.BuildConfig;
import com.example.mydemo.AppContext;
import com.example.mydemo.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import static com.android.volley.Request.Method.GET;
import static com.android.volley.Request.Method.POST;

/**
 * Created by ldh on 2016/4/25 0025.
 */
public class VolleyActivity extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_volley);
        initView();

        final String imgUrl = "http://www.baidu.com/img/bdlogo.png";
        //volley第一种加载图片的方式
        useImageRequest(imgUrl);
        //volley第二种加载图片的方式
//        useImageLoader(imgUrl);
        //第三种volley加载图片的方式
        useNetWorkImageView(imgUrl);
    }

    private void initView() {
        assignViews();
    }

    /**
     * volley第三种加载图片的方式
     * @param imgUrl
     */
    private void useNetWorkImageView(String imgUrl) {
        ImageLoader loader = new ImageLoader(AppContext.getHttpQueses(), new BitmapCache());
        networkImageView.setDefaultImageResId(R.mipmap.ic_launcher);
        networkImageView.setErrorImageResId(R.mipmap.ic_launcher);
        networkImageView.setImageUrl(imgUrl,loader);
    }

    /**
     * volley第二种加载图片的方式
     * @param imgUrl
     */
    private void useImageLoader(String imgUrl) {
        ImageLoader loader = new ImageLoader(AppContext.getHttpQueses(), new BitmapCache());
        ImageLoader.ImageListener listener = ImageLoader.getImageListener(imageView, R.mipmap.ic_launcher, R.mipmap.ic_launcher);
        loader.get(imgUrl, listener);
    }

    /**
     * volley第一种加载图片的方式
     * @param imgUrl
     */
    private void useImageRequest(String imgUrl) {
        ImageRequest imageRequest = new ImageRequest(imgUrl, new Response.Listener<Bitmap>() {
            @Override
            public void onResponse(Bitmap bitmap) {
                imageView.setImageBitmap(bitmap);
            }
        }, 0, 0, Bitmap.Config.ARGB_8888, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                imageView.setImageResource(R.mipmap.ic_launcher);
            }
        });
        imageRequest.setTag("Image");
        AppContext.getHttpQueses().add(imageRequest);
    }

    private Button bt1;
    private Button bt2;
    private Button bt3;
    private WebView wv;
    private ImageView imageView;
    private NetworkImageView networkImageView;

    private void assignViews() {
        bt1 = (Button) findViewById(R.id.bt_1);
        bt2 = (Button) findViewById(R.id.bt_2);
        bt3 = (Button) findViewById(R.id.bt_3);
        wv = (WebView) findViewById(R.id.wv);
        imageView = (ImageView) findViewById(R.id.img);
        networkImageView = (NetworkImageView) findViewById(R.id.net_image);

        bt1.setOnClickListener(this);
        bt2.setOnClickListener(this);
        bt3.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bt_1:
                volleyJsonGet();
//                volley_get();
                return;
            case R.id.bt_2:
//                volleyJsonGet();
                volletGet();
                break;
            case R.id.bt_3:
                volleyPostString();
                break;
            default:
                break;
        }
    }

    private void jsonVolleyPost(){
        Map<String, String> map = new HashMap<>();
        map.put("phone","13429667914");
        map.put("key", "335adcc4e891ba4e4be6d7534fd54c5d");
        JSONObject jsonObject = new JSONObject(map);

        JsonObjectRequest joRequest=new JsonObjectRequest(Request.Method.POST, "http://apis.juhe.cn/mobile/get?", jsonObject, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject jsonObject) {
                Toast.makeText(VolleyActivity.this, jsonObject.toString(), Toast.LENGTH_SHORT).show();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Toast.makeText(VolleyActivity.this, volleyError.toString(), Toast.LENGTH_SHORT).show();
            }
        });
        joRequest.setTag("jsonPost");
        AppContext.getHttpQueses().add(joRequest);

    }

    private void volleyPostString() {

        StringRequest request=new StringRequest(POST, "http://apis.juhe.cn/mobile/get?", new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
//                Toast.makeText(VolleyActivity.this, s, Toast.LENGTH_SHORT).show();
                Log.d("Volley", s);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Toast.makeText(VolleyActivity.this, volleyError.toString(), Toast.LENGTH_SHORT).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> hashmap = new HashMap<>();
                hashmap.put("phone","13429667914");
                hashmap.put("key", "335adcc4e891ba4e4be6d7534fd54c5d");
                return hashmap;
            }
        };

        request.setTag("abcPost");
        AppContext.getHttpQueses().add(request);

    }

    private void volleyJsonGet() {
        JsonObjectRequest requset = new JsonObjectRequest(GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject jsonObject) {
//                Toast.makeText(VolleyActivity.this, jsonObject.toString(), Toast.LENGTH_SHORT).show();
                Log.d("Volley", jsonObject.toString());
                //获取jsonObject的数据方式一，找不到返回空字符串
                if (BuildConfig.DEBUG) Log.d("VolleyActivity", jsonObject.optString("error_code"));
                //方式二，找不到抛异常
                try {
                    Toast.makeText(VolleyActivity.this, jsonObject.getString("reason"), Toast.LENGTH_SHORT).show();
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                Log.d("TAG", jsonObject.toString());

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Toast.makeText(VolleyActivity.this, volleyError.toString(), Toast.LENGTH_SHORT).show();
                Log.e("TAG", volleyError.getMessage(), volleyError);
            }
        });
        requset.setTag("abcGet");
        AppContext.getHttpQueses().add(requset);
    }

    String url = "http://apis.juhe.cn/mobile/get?phone=13429667914&key=335adcc4e891ba4e4be6d7534fd54c5d";
    String url2 = "http://www.ip138.com:8080/search.asp?action=mobile&mobile=18616598561";

    Response.Listener<String> listener = new Response.Listener<String>() {

        @Override
        public void onResponse(String s) {
            Toast.makeText(VolleyActivity.this, s, Toast.LENGTH_SHORT).show();
        }
    };
    Response.ErrorListener errorListener = new Response.ErrorListener() {

        @Override
        public void onErrorResponse(VolleyError volleyError) {
            Log.e("TAG", volleyError.getMessage(), volleyError);
            Toast.makeText(VolleyActivity.this, volleyError.toString(), Toast.LENGTH_SHORT).show();
        }
    };
    private void volley_get() {

        StringRequest stringRequest = new StringRequest(GET, url2, listener,
                errorListener);
        stringRequest.setTag("abcGet");
        AppContext.getHttpQueses().add(stringRequest);
    }

    private void volletGet() {
        VolleyRequest.requsetGet(this, url, "abcGet", new VolleyInterface(this,VolleyInterface.mListener,VolleyInterface.mErrorListener ) {

            @Override
            public void onMySuccess(String result) {
//                Toast.makeText(VolleyActivity.this, result.toString(), Toast.LENGTH_SHORT).show();
                Log.d("volley", result.toString());
            }

            @Override
            public void onMyError(VolleyError error) {
                Toast.makeText(VolleyActivity.this, error.toString(), Toast.LENGTH_SHORT).show();
            }
        });
    }
    @Override
    protected void onStop() {
        super.onStop();
        AppContext.getHttpQueses().cancelAll("abcGet");
    }
}
