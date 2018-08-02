package com.example.mydemo.viewpager;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import com.example.mydemo.R;

public class ViewPagerGuideActivity extends AppCompatActivity {
    private final int[] images = {R.mipmap.w1, R.mipmap.wb, R.mipmap.a3};
    private ViewPager viewPager;
    private ViewPagerDemoAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_pager_demo);
        initView();
        initData();
    }

    private void initView() {
        viewPager = findViewById(R.id.view_pager);
    }

    private void initData() {
        adapter = new ViewPagerDemoAdapter(this);
        viewPager.setAdapter(adapter);
        for (int i = 0; i < images.length; i++) {
            adapter.addImage(images[i]);
        }
    }


}
