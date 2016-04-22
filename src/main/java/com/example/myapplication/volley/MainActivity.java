package com.example.myapplication.volley;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
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
import com.example.myapplication.MyApplication;
import com.example.myapplication.R;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import static com.android.volley.Request.Method.GET;
import static com.android.volley.Request.Method.POST;

/**
 * Volley的使用
 *volley加载图片
 */
public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, View.OnClickListener {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        assignViews();
        final String imgUrl = "http://www.baidu.com/img/bdlogo.png";
        //volley第一种加载图片的方式
        useImageRequest(imgUrl);
        //volley第二种加载图片的方式
//        useImageLoader(imgUrl);
        //第三种volley加载图片的方式
        useNetWorkImageView(imgUrl);
    }

    /**
     * volley第三种加载图片的方式
     * @param imgUrl
     */
    private void useNetWorkImageView(String imgUrl) {
        ImageLoader loader = new ImageLoader(MyApplication.getHttpQueses(), new BitmapCache());
        networkImageView.setDefaultImageResId(R.mipmap.ic_launcher);
        networkImageView.setErrorImageResId(R.mipmap.ic_launcher);
        networkImageView.setImageUrl(imgUrl,loader);
    }

    /**
     * volley第二种加载图片的方式
     * @param imgUrl
     */
    private void useImageLoader(String imgUrl) {
        ImageLoader loader = new ImageLoader(MyApplication.getHttpQueses(), new BitmapCache());
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
        MyApplication.getHttpQueses().add(imageRequest);
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
                Toast.makeText(MainActivity.this, jsonObject.toString(), Toast.LENGTH_SHORT).show();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Toast.makeText(MainActivity.this, volleyError.toString(), Toast.LENGTH_SHORT).show();
            }
        });
        joRequest.setTag("jsonPost");
        MyApplication.getHttpQueses().add(joRequest);

    }

    private void volleyPostString() {

        StringRequest request=new StringRequest(POST, "http://apis.juhe.cn/mobile/get?", new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                Toast.makeText(MainActivity.this, s, Toast.LENGTH_SHORT).show();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Toast.makeText(MainActivity.this, volleyError.toString(), Toast.LENGTH_SHORT).show();
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
        MyApplication.getHttpQueses().add(request);

    }

    private void volleyJsonGet() {
        JsonObjectRequest requset = new JsonObjectRequest(GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject jsonObject) {
                Toast.makeText(MainActivity.this, jsonObject.toString(), Toast.LENGTH_SHORT).show();
                Log.d("TAG",jsonObject.toString());

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Toast.makeText(MainActivity.this, volleyError.toString(), Toast.LENGTH_SHORT).show();
                Log.e("TAG", volleyError.getMessage(), volleyError);
            }
        });
        requset.setTag("abcGet");
        MyApplication.getHttpQueses().add(requset);
    }

    String url = "http://apis.juhe.cn/mobile/get?phone=13429667914&key=335adcc4e891ba4e4be6d7534fd54c5d";
    String url2 = "http://www.ip138.com:8080/search.asp?action=mobile&mobile=18616598561";

    Response.Listener<String> listener = new Response.Listener<String>() {

        @Override
        public void onResponse(String s) {
            Toast.makeText(MainActivity.this, s, Toast.LENGTH_SHORT).show();
        }
    };
    Response.ErrorListener errorListener = new Response.ErrorListener() {

        @Override
        public void onErrorResponse(VolleyError volleyError) {
            Log.e("TAG", volleyError.getMessage(), volleyError);
            Toast.makeText(MainActivity.this, volleyError.toString(), Toast.LENGTH_SHORT).show();
        }
    };
    private void volley_get() {

        StringRequest stringRequest = new StringRequest(GET, url2, listener,
                errorListener);
        stringRequest.setTag("abcGet");
        MyApplication.getHttpQueses().add(stringRequest);
    }

    private void volletGet() {
        VolleyRequest.requsetGet(this, url, "abcGet", new VolleyInterface(this,VolleyInterface.mListener,VolleyInterface.mErrorListener ) {

            @Override
            public void onMySuccess(String result) {
                Toast.makeText(MainActivity.this, result.toString(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onMyError(VolleyError error) {
                Toast.makeText(MainActivity.this, error.toString(), Toast.LENGTH_SHORT).show();
            }
        });
    }
    @Override
    protected void onStop() {
        super.onStop();
        MyApplication.getHttpQueses().cancelAll("abcGet");
    }








    private void initView() {
        //初始化toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //初始化悬浮按钮
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        //初始化侧边栏
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            jsonVolleyPost();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
