package com.example.mydemo.widget.viewpagerfragment;

import android.os.Bundle;

import com.example.mydemo.MyListFragment;
import com.example.mydemo.R;
import com.example.mydemo.base.BaseViewPagerFragment;
import com.example.mydemo.widget.test.ViewPageFragmentAdapter;

/**
 * Created by ldh on 2016/7/7 0007.
 */
public class WidgetViewPagerFragment extends BaseViewPagerFragment {


    @Override
    protected void onSetupTabAdapter(ViewPageFragmentAdapter adapter) {
        String[] title = getResources().getStringArray(R.array.widget_viewpager_arrays);
        adapter.addTab(title[0], "new_widget", MyListFragment.class, getBundle());
        adapter.addTab(title[1], "hot_widget", MyListFragment.class, getBundle());
        adapter.addTab(title[2], "my_widget", MyListFragment.class, getBundle());
    }

    public Bundle getBundle() {
        Bundle bundle = new Bundle();
        return bundle;
    }
}
