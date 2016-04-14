package com.example.myapplication.adapter;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.example.myapplication.R;

import java.util.ArrayList;
import java.util.List;

public class ListActivity extends AppCompatActivity {

    private MyListView mListView;
    private List<Bean> mDatas;
    private MyAdapter myAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);
        initDatas();
        initView();
    }

    private void initView() {
        mListView = (MyListView) findViewById(R.id.listview);
        mListView.setMaxOverDistance(this,66);
        mListView.setAdapter(myAdapter);
    }

    private void initDatas() {
        mDatas = new ArrayList<>();
        Bean bean = new Bean("Android新技能Get1", "Android打造万能的ListView和GridView适配器", "2016-6-6", "10010");
        mDatas.add(bean);
        bean = new Bean("Android新技能Get2", "Android打造万能的ListView和GridView适配器", "2016-6-6", "10010");
        mDatas.add(bean);
        bean = new Bean("Android新技能Get3", "Android打造万能的ListView和GridView适配器", "2016-6-6", "10010");
        mDatas.add(bean);
        bean = new Bean("Android新技能Get4", "Android打造万能的ListView和GridView适配器", "2016-6-6", "10010");
        mDatas.add(bean);
        bean = new Bean("Android新技能Get5", "Android打造万能的ListView和GridView适配器", "2016-6-6", "10010");
        mDatas.add(bean);
        bean = new Bean("Android新技能Get6", "Android打造万能的ListView和GridView适配器", "2016-6-6", "10010");
        mDatas.add(bean);
        bean = new Bean("Android新技能Get7", "Android打造万能的ListView和GridView适配器", "2016-6-6", "10010");
        mDatas.add(bean);
        bean = new Bean("Android新技能Get8", "Android打造万能的ListView和GridView适配器", "2016-6-6", "10010");
        mDatas.add(bean);
        bean = new Bean("Android新技能Get9", "Android打造万能的ListView和GridView适配器", "2016-6-6", "10010");
        mDatas.add(bean);
        bean = new Bean("Android新技能Get10", "Android打造万能的ListView和GridView适配器", "2016-6-6", "10010");
        mDatas.add(bean);
        myAdapter = new MyAdapter(this, mDatas,R.layout.item_list);
    }
}
