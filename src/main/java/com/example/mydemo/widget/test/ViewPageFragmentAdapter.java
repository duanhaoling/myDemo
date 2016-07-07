package com.example.mydemo.widget.test;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.example.mydemo.R;
import com.example.mydemo.widget.PagerSlidingTabStrip;

import java.util.ArrayList;

@SuppressLint("Recycle")
public class ViewPageFragmentAdapter extends FragmentStatePagerAdapter {

    protected PagerSlidingTabStrip mPagerStrip;
    private final ViewPager mViewPager;
    private final ArrayList<ViewPageInfo> mTabs = new ArrayList<>();
    private final Context mContext;


    public ViewPageFragmentAdapter(FragmentManager fm,PagerSlidingTabStrip pagerStrip, ViewPager viewPager) {
        super(fm);
        mContext = viewPager.getContext();
        mPagerStrip = pagerStrip;
        mViewPager = viewPager;
        mViewPager.setAdapter(this);
        mPagerStrip.setViewPager(mViewPager);
    }

    public void addTab(String title, String tag, Class<?> clss, Bundle args) {
        ViewPageInfo pageInfo = new ViewPageInfo(title, tag, clss, args);
        addFragment(pageInfo);
    }

    public void addAllTab(ArrayList<ViewPageInfo> mTabs) {
        for (ViewPageInfo viewPageInfo : mTabs) {
            addFragment(viewPageInfo);
        }
    }
    private void addFragment(ViewPageInfo pageInfo) {

        if (pageInfo == null) {
            return;
        }
        // 加入tab title
        View v = LayoutInflater.from(mContext).inflate(R.layout.base_viewpage_fragment_tab_item,null,false);
        TextView title = (TextView) v.findViewById(R.id.tab_title);
        title.setText(pageInfo.title);
        mPagerStrip.addTab(v);

        mTabs.add(pageInfo);
        notifyDataSetChanged();
    }

    /**
     * 移除第一个Tab
     */
    public void remove() {
        remove(0);
    }

    /**
     * 移除一个Tab
     * @param index
     *     备注：如果index小于0，则从第一个开始删 如果大于tab的数量值则从最后一个开始删除
     */
    private void remove(int index) {
        if (mTabs.isEmpty()) return;
        if (index < 0) {
            index = 0;
        }
        if (index >= mTabs.size()) {
            index = mTabs.size() - 1;
        }
        mTabs.remove(index);
        mPagerStrip.removeTab(index);
        notifyDataSetChanged();
    }

    /**
     * 移除所有的Tab
     */
    private void removeAll() {
        if (mTabs.isEmpty()) {
            return;
        }
        mPagerStrip.removeAllTab();
        mTabs.clear();
        notifyDataSetChanged();
    }

    @Override
    public Fragment getItem(int position) {
        ViewPageInfo info = mTabs.get(position);
        return Fragment.instantiate(mContext, info.clss.getName(), info.args);
    }

    @Override
    public int getCount() {
        return mTabs.size();
    }

    @Override
    public int getItemPosition(Object object) {
        return PagerAdapter.POSITION_NONE;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return mTabs.get(position).title;
    }
}