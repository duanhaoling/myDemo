package com.example.myapplication.webview;


import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Picture;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.JsPromptResult;
import android.webkit.JsResult;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.Toast;

import com.example.myapplication.util.MyUtils;
import com.example.myapplication.R;

import java.io.FileOutputStream;

/**
 * 在WebView的设计中，不是什么任务都由WebView类完成的，辅助的类完全其它辅助性的工作，WebViewy主要负责解析、渲染。
 * WebViewClient就是辅助WebView处理各种通知、请求事件的，具体来说包括：
 * onLoadResource、onPageStart、onPageFinish、onReceiveError、onReceivedHttpAuthRequest；
 * <p/>
 * WebChromeClient是辅助WebView处理Javascript的对话框，网站图标，网站title，加载进度等
 * onCloseWindow(关 闭WebView)、onCreateWindow()、onJsAlert (WebView上alert是弹不出来东西的，
 * 需要定制你的WebChromeClient处理弹出)、onJsPrompt、 onJsConfirm、onProgressChanged、onReceivedIcon、onReceivedTitle；
 * <p/>
 * Created by ldh on 2016/4/1 0001.
 */


public class WebViewDemo extends AppCompatActivity {


    private WebView wv;
    private Button b1;
    private Button b2;
    private Button b3;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_webview);
        initActionBar();

// 定义UI组件
        b1 = (Button) findViewById(R.id.bt_1);
        b2 = (Button) findViewById(R.id.bt_2);
        b3 = (Button) findViewById(R.id.bt_3);
        wv = (WebView) findViewById(R.id.wv);

// 覆盖默认后退按钮的作用，替换成WebView里的查看历史页面
        wv.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_DOWN) {
                    if ((keyCode == KeyEvent.KEYCODE_BACK) && wv.canGoBack()) {
                        wv.goBack();
                        return true;
                    }
                }
                return false;
            }

        });

// 设置支持Javascript

        wv.getSettings().setJavaScriptEnabled(true);

// 设置WebViewClient对象

        wv.setWebViewClient(wvc);

// 设置setWebChromeClient对象

        wv.setWebChromeClient(wvcc);

        wv.loadUrl("file:///android_asset/demo.html");

// 定义并绑定按钮单击监听器

        initEvents();

    }

    private void initActionBar() {
        ActionBar actionBar =getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setDisplayShowHomeEnabled(true);
        }
    }

// 创建WebViewClient对象


    WebViewClient wvc = new WebViewClient() {

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {

            MyUtils.showtoast(WebViewDemo.this,"WebViewClient.shouldOverrideUrlLoading");

// 使用自己的WebView组件来响应Url加载事件，而不是使用默认浏览器器加载页面

            wv.loadUrl(url);

// 记得消耗掉这个事件。给不知道的朋友再解释一下，Android中返回True的意思就是到此为止吧,事件就会不会冒泡传递了，我们称之为消耗掉

            return true;

        }

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {

            MyUtils.showtoast(WebViewDemo.this,"WebViewClient.onPageStarted");

            super.onPageStarted(view, url, favicon);

        }

        @Override
        public void onPageFinished(WebView view, String url) {
            MyUtils.showtoast(WebViewDemo.this,"WebViewClient.onPageFinished");

            super.onPageFinished(view, url);

        }


        @Override
        public void onLoadResource(WebView view, String url) {

            MyUtils.showtoast(WebViewDemo.this,"WebViewClient.onLoadResource");

            super.onLoadResource(view, url);

        }

    };

// 创建WebViewChromeClient


    WebChromeClient wvcc = new WebChromeClient() {
        //处理进度条
        @Override
        public void onProgressChanged(WebView view, int newProgress) {
            MyUtils.showtoast(WebViewDemo.this,"正在加载中");
            super.onProgressChanged(view, newProgress);
        }
        // 处理Alert事件

        @Override
        public boolean onJsAlert(WebView view, String url, String message, final JsResult result) {

// 构建一个Builder来显示网页中的alert对话框

            Builder builder = new Builder(WebViewDemo.this);

            builder.setTitle("计算1+2的值");

            builder.setMessage(message);

            builder.setPositiveButton(android.R.string.ok, new AlertDialog.OnClickListener() {

                @Override
                public void onClick(DialogInterface dialog, int which) {

                    result.confirm();

                }

            });

            builder.setCancelable(false);

            builder.create();

            builder.show();

            return true;


        }

        @Override
        public void onReceivedTitle(WebView view, String title) {
            //可以用onReceivedTitle()方法修改网页标题
            WebViewDemo.this.setTitle(title);

            super.onReceivedTitle(view, title);

        }

// 处理Confirm事件

        @Override
        public boolean onJsConfirm(WebView view, String url, String message, final JsResult result) {

            Builder builder = new Builder(WebViewDemo.this);

            builder.setTitle("删除确认");

            builder.setMessage(message);

            builder.setPositiveButton(android.R.string.ok, new AlertDialog.OnClickListener() {

                @Override
                public void onClick(DialogInterface dialog, int which) {

                    result.confirm();

                }

            });

            builder.setNeutralButton(android.R.string.cancel, new AlertDialog.OnClickListener() {


                @Override
                public void onClick(DialogInterface dialog, int which) {

                    result.cancel();

                }

            });

            builder.setCancelable(false);

            builder.create();

            builder.show();

            return true;

        }

// 处理提示事件

        @Override
        public boolean onJsPrompt(WebView view, String url, String message, String defaultValue,

                                  JsPromptResult result) {

// 看看默认的效果
            return super.onJsPrompt(view, url, message, defaultValue, result);
        }
    };

    private void initEvents() {
        b1.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {


// 加载URL assets目录下的内容可以用 "[url=file:///android_asset]file:///android_asset[/url]" 前缀

//                wv.loadUrl("[url=file:///android_asset/html/test1.html]file:///android_asset/html/test1.html[/url]");
                wv.loadUrl("http://www.baidu.com");

            }

        });

// 定义并绑定按钮单击监听器

        b2.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {


// 加载URL assets目录下的内容可以用 "[url=file:///android_asset]file:///android_asset[/url]" 前缀

//                wv.loadUrl("[url=file:///android_asset/html/test3.html]file:///android_asset/html/test3.html[/url]");
                wv.loadUrl("http://www.sina.com");
            }

        });

// 定义并绑定按钮单击监听器

        b3.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                Picture pic = wv.capturePicture();

                int width = pic.getWidth();

                int height = pic.getHeight();

                if (width > 0 && height > 0) {

                    Bitmap bmp = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);

                    Canvas canvas = new Canvas(bmp);

                    pic.draw(canvas);

                    try {

                        String fileName = "sdcard/" + System.currentTimeMillis() + ".png";

                        FileOutputStream fos = new FileOutputStream(fileName);

                        if (fos != null) {

                            bmp.compress(Bitmap.CompressFormat.PNG, 90, fos);

                            fos.close();

                        }

                        Toast.makeText(getApplicationContext(), "截图成功，文件名是：" + fileName, Toast.LENGTH_SHORT).show();

                    } catch (Exception e) {

                        e.printStackTrace();


                    }

                }

            }

        });
    }


}
