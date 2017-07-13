package com.example.mydemo.remoteview;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.os.SystemClock;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.Toast;

import com.example.mydemo.R;

/**
 * Created by ldh on 2017/7/11.
 * 所有的方法都在onReceive方法中调用的，对应了不同的action
 *
 * 添加小部件
 * 07-12 10:38:01.443 23398-23398/? I/MyAppWidgetProvider: onReceive : action = android.appwidget.action.APPWIDGET_ENABLED
 * 07-12 10:38:01.443 23398-23398/? I/MyAppWidgetProvider: onUpdate
 * 07-12 10:38:01.453 23398-23398/? I/MyAppWidgetProvider: counter = 1
 * 07-12 10:38:01.453 23398-23398/? I/MyAppWidgetProvider: appWidgetId = 7
 * 07-12 10:38:01.463 23398-23398/? I/MyAppWidgetProvider: onReceive : action = android.appwidget.action.APPWIDGET_UPDATE
 * 点击小部件
 * 07-12 10:38:12.704 23398-23398/? I/MyAppWidgetProvider: onReceive : action = com.ryg.chapter_5.action.CLICK
 * 删除小部件
 * 07-12 10:38:37.619 23398-23398/? I/MyAppWidgetProvider: onReceive : action = android.appwidget.action.APPWIDGET_DELETED
 * 07-12 10:38:37.619 23398-23398/? I/MyAppWidgetProvider: onReceive : action = android.appwidget.action.APPWIDGET_DISABLED
 */

public class MyAppWidgetProvider extends AppWidgetProvider {
    public static final String TAG = "MyAppWidgetProvider";
    public static final String CLICK_ACTION = "com.ryg.chapter_5.action.CLICK";

    public MyAppWidgetProvider() {
        super();
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        super.onReceive(context, intent);
        Log.i(TAG, "onReceive : action = " + intent.getAction());
        //这里判断是自己的action，做自己的事情 ，处理点击事件
        if (intent.getAction().equals(CLICK_ACTION)) {
            Toast.makeText(context, "clicked it", Toast.LENGTH_SHORT).show();

            new Thread() {
                @Override
                public void run() {
                    Bitmap srcBitmap = BitmapFactory.decodeResource(context.getResources(), R.mipmap.usine);
                    AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
                    for (int i = 0; i < 37; i++) {
                        float degree = (i * 10) % 360;
                        RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.widget);
                        remoteViews.setImageViewBitmap(R.id.imageView1, rotateBitmap(context, srcBitmap, degree));

                        //重复设置，可删去
//                        Intent intentClick = new Intent();
//                        intentClick.setAction(CLICK_ACTION);
//                        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, intentClick, 0);
//                        remoteViews.setOnClickPendingIntent(R.id.imageView1, pendingIntent);

                        appWidgetManager.updateAppWidget(new ComponentName(context, MyAppWidgetProvider.class), remoteViews);
                        SystemClock.sleep(30);
                    }
                }
            }.start();
        }
    }

    /**
     * 每次桌面小部件更新都调用一次该方法
     * 在这里初始化窗口小部件
     * @param context
     * @param appWidgetManager
     * @param appWidgetIds
     */
    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        super.onUpdate(context, appWidgetManager, appWidgetIds);
        Log.i(TAG, "onUpdate");

        int counter = appWidgetIds.length;
        Log.i(TAG, "counter = " + counter);
        for (int i = 0; i < counter; i++) {
            int appWidgetId = appWidgetIds[i];
            onWidgetUpdate(context, appWidgetManager, appWidgetId);
        }
    }

    /**
     * 桌面小部件更新
     *
     * @param context
     * @param appWidgetManager
     * @param appWidgetId
     */
    private void onWidgetUpdate(Context context, AppWidgetManager appWidgetManager, int appWidgetId) {
        Log.i(TAG, "appWidgetId = " + appWidgetId);
        RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.widget);
        //桌面小部件 单击事件发布的intent广播
        Intent intentClick = new Intent();
        intentClick.setAction(CLICK_ACTION);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, intentClick, 0);
        remoteViews.setOnClickPendingIntent(R.id.imageView1, pendingIntent);
        appWidgetManager.updateAppWidget(appWidgetId, remoteViews);
    }

    private Bitmap rotateBitmap(Context context, Bitmap srcBitmap, float degree) {
        Matrix matrix = new Matrix();
        matrix.reset();
        matrix.setRotate(degree);
        Bitmap tmpBitmap = Bitmap.createBitmap(srcBitmap, 0, 0, srcBitmap.getWidth(), srcBitmap.getHeight(), matrix, true);
        return tmpBitmap;
    }
}
