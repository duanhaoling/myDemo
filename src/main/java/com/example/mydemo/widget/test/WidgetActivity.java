package com.example.mydemo.widget.test;

import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import com.example.mydemo.R;
import com.example.mydemo.widget.PagerSlidingTabStrip;

public  class WidgetActivity extends AppCompatActivity {
    protected PagerSlidingTabStrip mTabStrip;
    protected ViewPager mViewPager;
    protected ViewPageFragmentAdapter mTabsAdapter;
//    protected EmptyLayout mErrorLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_widget);
        mTabStrip = (PagerSlidingTabStrip)findViewById(R.id.pager_tabstrip);
        mViewPager = (ViewPager)findViewById(R.id.pager);

        mTabsAdapter = new ViewPageFragmentAdapter(getSupportFragmentManager(), mTabStrip, mViewPager);
        setScreenPageLimit();
        onSetupTabAdapter(mTabsAdapter);

        if (savedInstanceState != null) {
            int pos = savedInstanceState.getInt("position");
            mViewPager.setCurrentItem(pos,true);
        }

    }

    protected void onSetupTabAdapter(ViewPageFragmentAdapter mTabsAdapter) {
    }

    protected void setScreenPageLimit() {
    }

    @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
        if (outState != null && mViewPager != null) {
            outState.putInt("position",mViewPager.getCurrentItem());
        }
        super.onSaveInstanceState(outState, outPersistentState);
    }
}
