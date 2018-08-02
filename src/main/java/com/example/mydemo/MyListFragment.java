package com.example.mydemo;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.example.mydemo.view.adapter.MyListActivity;
import com.example.mydemo.view.animation.AnimationActivity;
import com.example.mydemo.fileprovider_client.FileProviderTestActivity;
import com.example.mydemo.greendaotdemo.DbTestActivity;
import com.example.mydemo.view.dragview.DragViewactivity;
import com.example.mydemo.view.material.CoordActivity;
import com.example.mydemo.view.popu.LiuyanActivity;
import com.example.mydemo.remoteview.ReceiveNotificationActivity;
import com.example.mydemo.rx.retrofit.NetTestActivity;
import com.example.mydemo.rx.rxjava.RxjavaTestActivity;
import com.example.mydemo.view.timer.TimerDemoActivity;
import com.example.mydemo.view.viewflipper.ViewFlipperActivity;
import com.example.mydemo.view.webview.WebViewDemo;
import com.example.mydemo.view.webview.WebViewDemo2;
import com.example.mydemo.viewpager.ViewPagerGuideActivity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by ldh on 2016/4/25 0025.
 */
public class MyListFragment extends ListFragment {

    private Context mContext;
    private final String[] ss = new String[]{
            "MyListActivity",
            "TimerDemoActivity",
            "NetTestActivity",
            "AnimationActivity",
            "WebViewDemo",
            "WebViewDemo2",
            "ViewFlipperActivity",
            "CoordActivity",
            "留言板",
            "Notification",
            "FileProviderTest",
            "RxjavaTest",
            "DatabaseTest",
            "优雅实现拖拽填空题",
            "viewPager展示图片",
            "viewFlipper展示图片"
    };


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        List<String> mDatas = new ArrayList<String>();
        mDatas.addAll(Arrays.asList(ss));

        mContext = getActivity();
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(mContext, android.R.layout.simple_list_item_1,
                android.R.id.text1, mDatas);
        setListAdapter(adapter);

        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        ListView listView = getListView();
        String itemAtPosition = (String) listView.getItemAtPosition(position);
        switch (position) {
            case 0:
                gotoActivity(MyListActivity.class);
                break;
            case 1:
                gotoActivity(TimerDemoActivity.class);
                break;
            case 2:
                gotoActivity(NetTestActivity.class);
                break;
            case 3:
                gotoActivity(AnimationActivity.class);
                break;
            case 4:
                gotoActivity(WebViewDemo.class);
                break;
            case 5:
                /* Intent intent1 = new Intent(Intent.ACTION_VIEW);
                intent1.setData(Uri.parse("http://www.bing.com"));
                startActivity(intent1);
                return;*/
                gotoActivity(WebViewDemo2.class);
                break;
            case 6:
                gotoActivity(ViewFlipperActivity.class);
                break;
            case 7:
                gotoActivity(CoordActivity.class);
                break;
            case 8:
                gotoActivity(LiuyanActivity.class);
                break;
            case 9:
                gotoActivity(ReceiveNotificationActivity.class);
                break;
            case 10:
                gotoActivity(FileProviderTestActivity.class);
                break;
            case 11:
                gotoActivity(RxjavaTestActivity.class);
                break;
            case 12:
                gotoActivity(DbTestActivity.class);
                break;
            case 13:
                gotoActivity(DragViewactivity.class);
                break;
            case 14:
                gotoActivity(ViewPagerGuideActivity.class);
                break;
            case 15:
                gotoActivity(ViewFlipperActivity.class);
                break;
            default:
                break;
        }
    }


    public void gotoActivity(Class<? extends Activity> clazz) {
        Intent intent = new Intent(mContext, clazz);
        mContext.startActivity(intent);
    }

    private void startActivity(String name) {
        //使用反射根据name打开对应的Activity

    }
}
