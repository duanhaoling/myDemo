package com.example.mydemo.view.adapter;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.widget.AbsListView;

import com.example.mydemo.R;

import java.util.ArrayList;
import java.util.List;

public class MyListActivity extends AppCompatActivity implements View.OnTouchListener {

    private MyListView mListView;
    private List<Bean> mDatas;
    private MyAdapter myAdapter;
    private int actionBarHeight;
    private int mTouchSlop;
    private Animator mAnimator;

    private Toolbar mToolbar;
    private float mFirstY;
    private float mCurrentY;
    private int direction;
    private boolean mShow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);
        initDatas();
        initView();
    }

    private void initView() {
        mListView = (MyListView) findViewById(R.id.listview);
        mListView.setMaxOverDistance(this, 66);
        mListView.setAdapter(myAdapter);
        mShow = true;

        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);

        //获取toolbar的高度、
        TypedValue tv = new TypedValue();
        if (getTheme().resolveAttribute(android.R.attr.actionBarSize, tv, true)) {
            actionBarHeight = TypedValue.complexToDimensionPixelSize(tv.data, getResources().getDisplayMetrics());
        }
        View header = new View(this);
        header.setLayoutParams(new AbsListView.LayoutParams(AbsListView.LayoutParams.MATCH_PARENT, actionBarHeight));
        mListView.addHeaderView(header);
        mTouchSlop = ViewConfiguration.get(this).getScaledTouchSlop();
        mListView.setOnTouchListener(this);
    }

    private void toolbarAnim(int flag) {
        if (mAnimator != null && mAnimator.isRunning()) {
            mAnimator.cancel();
        }
        if (flag == 1) {
            mAnimator = ObjectAnimator.ofFloat(mToolbar, "translationY", mToolbar.getTranslationY(), 0);
        } else {
            mAnimator = ObjectAnimator.ofFloat(mToolbar, "translationY", mToolbar.getTranslationY(), -mToolbar.getHeight());
        }
        mAnimator.start();
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
        myAdapter = new MyAdapter(this, mDatas, R.layout.item_list);
    }


    @Override
    public boolean onTouch(View v, MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mFirstY = event.getY();
                break;
            case MotionEvent.ACTION_MOVE:
                mCurrentY = event.getY();
                if (mCurrentY - mFirstY > mTouchSlop) {
                    direction = 0;//down
                } else if (mFirstY - mCurrentY > mTouchSlop) {
                    direction = 1;//up
                }
                if (direction == 1) {
                    if (mShow) {
                        toolbarAnim(0);//hide
                        mShow = !mShow;
                    }
                } else if (direction == 0) {
                    if (!mShow) {
                        toolbarAnim(1);//show
                        mShow = !mShow;
                    }
                }
                break;
            case MotionEvent.ACTION_UP:
                break;
        }
        return false;
    }
}
