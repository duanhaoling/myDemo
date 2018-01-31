package com.example.mydemo.view.webview;


import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Picture;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.webkit.JsPromptResult;
import android.webkit.JsResult;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.Toast;

import com.example.mydemo.R;
import com.example.mydemo.util.MyToast;

import java.io.FileOutputStream;
import java.io.IOException;

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

@SuppressLint("SetJavaScriptEnabled")
public class WebViewDemo extends AppCompatActivity {


    private WebView wv;
    private Button b1;
    private Button b2;
    private Button b3;

    @SuppressLint("JavascriptInterface")
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

        // 定义并绑定按钮单击监听器
        initEvents();

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
        WebSettings settings = wv.getSettings();
        settings.setJavaScriptEnabled(true);

        //增加对中文的支持
        settings.setDefaultTextEncodingName("GBK");

        //设置页面滚动条风格
        wv.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);//滚动条风格，为0指滚动条不占用空间，直接覆盖在网页上


        // 设置WebViewClient对象
        wv.setWebViewClient(wvc);

        // 设置setWebChromeClient对象
        wv.setWebChromeClient(wvcc);

        //加载网页
        wv.loadUrl("file:///android_asset/demo1.html");

        //添加js调用Android代码支持1
        wv.addJavascriptInterface(new JavaScriptInterface(this), "JavaScriptInterface");

        //添加js调用Android代码支持2
        wv.addJavascriptInterface(new JavaScriptInter() {
            @JavascriptInterface
            //此处一定要添加该注解，否则在4.1+系统上运行失败
            @Override
            public void onJsCallAndroid() {
                Toast.makeText(WebViewDemo.this, "Js调用安卓代码", Toast.LENGTH_SHORT).show();
            }

            @JavascriptInterface
            @Override
            public void callAndroidMethod(int a, float b, String c, boolean d) {
                if (d) {
                    String strMessage = "--" + (a + 1) + "--" + (b + 1) + "--" + c + "--" + d;
                    new AlertDialog.Builder(WebViewDemo.this).setTitle("callAndroidMethod")
                            .setMessage(strMessage).show();
                }
            }

            @JavascriptInterface
            @Override
            public void setJsonParamFromJS(String jsonString) {
                Toast.makeText(WebViewDemo.this, jsonString, Toast.LENGTH_SHORT).show();
            }


        }, "demo");

    }

    public interface JavaScriptInter {
        void onJsCallAndroid();

        void callAndroidMethod(int a, float b, String c, boolean d);

        void setJsonParamFromJS(String jsonString);

    }

    private void initActionBar() {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setDisplayShowHomeEnabled(true);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    // 创建WebViewClient对象
    WebViewClient wvc = new WebViewClient() {

        // 在点击请求的是链接是才会调用，重写此方法返回true表明点击网页里面的链接还是在当前的webview里跳转，
        // 不跳到浏览器那边。这个函数我们可以做很多操作，比如我们读取到某些特殊的URL，
        // 于是就可以不打开地址，取消这个操作，进行预先定义的其他操作，这对一个程序是非常必要的。
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            MyToast.showtoast(WebViewDemo.this, "WebViewClient.shouldOverrideUrlLoading");
            if (!TextUtils.isEmpty(url) && url.startsWith("call-app-method://clickShareButton")) {
                Toast.makeText(WebViewDemo.this, url, Toast.LENGTH_SHORT).show();
                return true;
            }
            //定义的协议在这里是没有大写的
            if (url.startsWith("getjsonparams")) {
                wv.loadUrl("javascript:window.demo.setJsonParamFromJS(getSafePayParam())");
                return true;
            }

            if (url.startsWith("http:") || url.startsWith("https:")) {
                return false;
            }
//            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
//            startActivity(intent);
//            // 使用自己的WebView组件来响应Url加载事件，而不是使用默认浏览器器加载页面
//            //  下面这一行保留的时候，原网页仍报错，新网页正常.所以注释掉后，也就没问题了
//            //   view.loadUrl(url);
//            // 记得消耗掉这个事件。给不知道的朋友再解释一下，Android中返回True的意思就是到此为止吧,事件就会不会冒泡传递了，我们称之为消耗掉
//            return true;
            return super.shouldOverrideUrlLoading(view, url);
        }

    };

    // 创建WebViewChromeClient
    WebChromeClient wvcc = new WebChromeClient() {
        //处理进度条
        @Override
        public void onProgressChanged(WebView view, int newProgress) {
            MyToast.showtoast(WebViewDemo.this, "正在加载中");
            if (newProgress == 100) {
                // 如果全部载入,隐藏进度对话框
//                handler.sendEmptyMessage(1);
            }
            super.onProgressChanged(view, newProgress);
        }

        @Override
        public void onReceivedTitle(WebView view, String title) {
            //可以用onReceivedTitle()方法修改网页标题
            WebViewDemo.this.setTitle(title);
            super.onReceivedTitle(view, title);
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

        // 处理Confirm事件
        @Override
        public boolean onJsConfirm(WebView view, String url, String message, final JsResult result) {

            Builder builder = new Builder(WebViewDemo.this);
            builder.setTitle("删除确认");

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
        public boolean onJsPrompt(WebView view, String url, String message, String defaultValue, JsPromptResult result) {
            // 看看默认的效果
            return super.onJsPrompt(view, url, message, defaultValue, result);
        }
    };

//    private Handler handler = new Handler() {
//        @Override
//        public void handleMessage(Message msg) {// 定义一个Handler，用于处理下载线程与UI间通讯
//            if (!Thread.currentThread().isInterrupted()) {
//                switch (msg.what) {
//                    case 0:
////                        pd.show();// 显示进度对话框
//                        break;
//                    case 1:
//                        pd.hide();// 隐藏进度对话框，不可使用dismiss()、cancel(),否则再次调用show()时，显示的对话框小圆圈不会动。
//                        break;
//                    default:
//                        break;
//                }
//            }
//            super.handleMessage(msg);
//        }
//    };

    private void initEvents() {
        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
// 加载URL assets目录下的内容可以用 "[url=file:///android_asset]file:///android_asset[/url]" 前缀

//                wv.loadUrl("[url=file:///android_asset/html/test1.html]file:///android_asset/html/test1.html[/url]");
//                wv.loadUrl("http://www.baidu.com");
                String color = "#00ee00";
                wv.loadUrl("Javascript:changeColor('" + color + "');");

            }

        });

// 定义并绑定按钮单击监听器

        b2.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
// 加载URL assets目录下的内容可以用 "[url=file:///android_asset]file:///android_asset[/url]" 前缀

//                wv.loadUrl("[url=file:///android_asset/html/test3.html]file:///android_asset/html/test3.html[/url]");
//                wv.loadUrl("http://www.sina.com");

                wv.loadUrl("javascript:window.demo.setJsonParamFromJS( getSafePayParam() )");

            }

        });

// 定义并绑定按钮单击监听器,网页截图逻辑

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
                    FileOutputStream fos = null;
                    try {
                        String fileName = "sdcard/" + System.currentTimeMillis() + ".png";
                        fos = new FileOutputStream(fileName);
                        if (fos != null) {
                            bmp.compress(Bitmap.CompressFormat.PNG, 90, fos);
                            fos.close();
                        }
                        Toast.makeText(getApplicationContext(), "截图成功，文件名是：" + fileName, Toast.LENGTH_SHORT).show();
                    } catch (Exception e) {
                        e.printStackTrace();
                    } finally {
                        try {
                            fos.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }

        });
    }


}
