package com.example.mydemo.view.webview;

/**
 * Created by ldh on 2016/5/17 0017.
 */

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.webkit.JavascriptInterface;
import android.webkit.JsResult;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;

import com.example.mydemo.R;

/**
 * Demonstrates how to embed a WebView in your activity. Also demonstrates how
 * to have javascript in the WebView call into the activity, and how the activity
 * can invoke javascript.
 * <p>
 * In this example, clicking on the android in the WebView will result in a call into
 * the activities code in {@link DemoJavaScriptInterface#clickOnAndroid()}. This code
 * will turn around and invoke javascript using the {@link WebView#loadUrl(String)}
 * method.
 * <p>
 * Obviously all of this could have been accomplished without calling into the activity
 * and then back into javascript, but this code is intended to show how to set up the
 * code paths for this sort of communication.
 *
 */
@SuppressLint("SetJavaScriptEnabled")
public class WebViewDemo2 extends Activity {

    private static final String LOG_TAG = "WebViewDemo";

    private WebView mWebView;

    private Handler mHandler = new Handler();

    @SuppressLint("JavascriptInterface")
    @Override
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);
        setContentView(R.layout.activity_webview2);
        mWebView = (WebView) findViewById(R.id.webview);

        WebSettings webSettings = mWebView.getSettings();
        webSettings.setSavePassword(false);
        webSettings.setSaveFormData(false);
        webSettings.setJavaScriptEnabled(true);
        webSettings.setSupportZoom(false);

        mWebView.setWebChromeClient(new MyWebChromeClient());

        mWebView.addJavascriptInterface(new DemoJavaScriptInterface(), "demo");

        mWebView.loadUrl("file:///android_asset/demo2.html");
    }

    final class DemoJavaScriptInterface {

        DemoJavaScriptInterface() {
            Log.i("aaaa", "create DemoJavaScriptInterface");
        }

        /**
         * This is not called on the UI thread. Post a runnable to invoke
         * loadUrl on the UI thread.
         */
        @JavascriptInterface
        public void clickOnAndroid() {
            mHandler.post(new Runnable() {
                public void run() {
                    mWebView.loadUrl("javascript:wave()");
                }
            });

        }
    }

    /**
     * Provides a hook for calling "alert" from javascript. Useful for
     * debugging your javascript.
     */
    final class MyWebChromeClient extends WebChromeClient {
        @Override
        public boolean onJsAlert(WebView view, String url, String message, JsResult result) {
            Log.d(LOG_TAG, message);
            result.confirm();
            return true;
        }
    }
}
