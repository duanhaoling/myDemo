package com.example.mydemo.view.adapter;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.widget.ListView;

/**
 * 具有弹性的listView
 * Created by ldh on 2016/4/14 0014.
 */
public class MyListView extends ListView {

    private int mMaxOverDistance;

    public int getMaxOverDistance() {
        return mMaxOverDistance;
    }

    /**
     * 通过屏幕的Density来计算,让不同分辨率的弹性距离基本一致
     * @param context
     * @param overDistance
     */
    public void setMaxOverDistance(Context context,int overDistance) {
        DisplayMetrics metrics = context.getResources().getDisplayMetrics();
        float density = metrics.density;
        this.mMaxOverDistance = (int) (density * overDistance);
    }

    public MyListView(Context context) {
        super(context);
    }

    public MyListView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MyListView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public MyListView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    protected boolean overScrollBy(int deltaX, int deltaY, int scrollX, int scrollY, int scrollRangeX, int scrollRangeY, int maxOverScrollX, int maxOverScrollY, boolean isTouchEvent) {
        return super.overScrollBy(deltaX, deltaY, scrollX, scrollY, scrollRangeX, scrollRangeY, maxOverScrollX,mMaxOverDistance, isTouchEvent);
    }
}
