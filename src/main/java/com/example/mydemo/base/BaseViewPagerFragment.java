package com.example.mydemo.base;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.mydemo.R;
import com.example.mydemo.widget.EmptyLayout;
import com.example.mydemo.widget.PagerSlidingTabStrip;
import com.example.mydemo.widget.test.ViewPageFragmentAdapter;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by ldh on 2016/5/24 0024.
 */
public abstract class BaseViewPagerFragment extends BaseFragment {
    @Bind(R.id.pager_tabstrip)
    protected PagerSlidingTabStrip mTabStrip;
    @Bind(R.id.pager)
    protected ViewPager mViewPager;
    @Bind(R.id.error_layout)
    protected EmptyLayout mErrorLayout;
    protected ViewPageFragmentAdapter mTabsAdapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.base_viewpage_fragment, null);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, getActivity());
        mTabsAdapter = new ViewPageFragmentAdapter(getActivity().getSupportFragmentManager(), mTabStrip, mViewPager);
        setScreenPageLimit();
        onSetupTabAdapter(mTabsAdapter);
        if (savedInstanceState != null) {
            int pos = savedInstanceState.getInt("position");
            mViewPager.setCurrentItem(pos, true);
        }
    }

    protected abstract void onSetupTabAdapter(ViewPageFragmentAdapter adapter);

    protected void setScreenPageLimit() {
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        //No call for super(). Bug on API Level > 11.
        if (outState != null && mViewPager != null) {
            outState.putInt("position", mViewPager.getCurrentItem());
        }
//        super.onSaveInstanceState(outState);
    }

}
