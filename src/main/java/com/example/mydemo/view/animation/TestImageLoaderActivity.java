package com.example.mydemo.view.animation;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.example.mydemo.R;
import com.ldh.androidlib.image.ImageLoaderManager;
import com.ldh.androidlib.image.ImageLoaderOptions;

/**
 * Created by ldh on 2017/9/4.
 */

public class TestImageLoaderActivity extends AppCompatActivity {
    private ImageView img1, img2, img3;
    private String url = "http://img1.imgtn.bdimg.com/it/u=679805784,3150507797&fm=214&gp=0.jpg";
    private String urlGif = "http://img2.imgtn.bdimg.com/it/u=2938769139,1872984641&fm=214&gp=0.jpg";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_imageloader);
        img1 = (ImageView) findViewById(R.id.img_1);
        img2 = (ImageView) findViewById(R.id.img_2);
        img3 = (ImageView) findViewById(R.id.img_3);
        final Button btn1 = (Button) findViewById(R.id.btn1);
        final Button btn2 = (Button) findViewById(R.id.btn2);


        ImageLoaderManager.getInstance().showImage(img1, url);
        ImageLoaderOptions options = new ImageLoaderOptions.Builder(img2, url)
                .blurImage(true)
                .placeholder(R.mipmap.ic_launcher)
                .build();
        ImageLoaderManager.getInstance().showImage(options);

        ImageLoaderOptions options3 = new ImageLoaderOptions.Builder(img3, urlGif)
                .asGif(true)
                .build();
        ImageLoaderManager.getInstance().showImage(options3);

        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 测试图片加载的显示和隐藏
                ImageLoaderManager.getInstance().hideImage(img1, img1.getVisibility() == View.VISIBLE ? View.INVISIBLE : View.VISIBLE);
                btn1.setText(img1.getVisibility() == View.VISIBLE ? "隐藏上图" : "显示上图");

            }
        });
        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 测试高斯模糊的图片加载的显示和隐藏
                ImageLoaderManager.getInstance().hideImage(img2, img2.getVisibility() == View.VISIBLE ? View.INVISIBLE : View.VISIBLE);
                btn2.setText(img2.getVisibility() == View.VISIBLE ? "隐藏上图" : "显示上图");

            }
        });

        // 测试取消加载和恢复加载
        ImageLoaderManager.getInstance().pause(this);
        img1.postDelayed(new Runnable() {
            @Override
            public void run() {
                // 测试取消加载和恢复加载
                ImageLoaderManager.getInstance().resume(TestImageLoaderActivity.this);
            }
        }, 2000);
    }
}
