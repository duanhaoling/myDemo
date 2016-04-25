package com.example.myapplication;

import android.app.ListFragment;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.example.myapplication.adapter.MyListActivity;
import com.example.myapplication.timer.TimerDemoActivity;
import com.example.myapplication.volley.VolleyActivity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by ldh on 2016/4/25 0025.
 */
public class MyListFragment extends ListFragment {

    private Context mContext;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        List<String> mDatas = new ArrayList<String>();
        String[] ss=new String[]{"MyListActivity","TimerDemoActivity","VolleyActivity"};
        mDatas.addAll(Arrays.asList(ss));

        mContext = getActivity();
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(mContext, android.R.layout.simple_list_item_1,
                android.R.id.text1, mDatas);
        setListAdapter(adapter);

        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        ListView listView = getListView();
        String itemAtPosition = (String) listView.getItemAtPosition(position);
        Intent intent = new Intent();
        switch (position) {
            case 0:
                intent.setClass(mContext, MyListActivity.class);
                break;
            case 1:
                intent.setClass(mContext, TimerDemoActivity.class);
                break;
            case 2:
                intent.setClass(mContext, VolleyActivity.class);
                break;
        }
        mContext.startActivity(intent);
        super.onListItemClick(l, v, position, id);
    }

    private void startActivity(String name) {
        //使用反射根据name打开对应的Activity

    }
}
