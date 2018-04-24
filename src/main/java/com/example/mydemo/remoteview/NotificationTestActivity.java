package com.example.mydemo.remoteview;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Process;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.RemoteViews;

import com.example.mydemo.R;
import com.example.mydemo.view.webview.WebViewDemo;
import com.example.mydemo.view.webview.WebViewDemo2;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by ldh on 2017/7/11.
 */
public class NotificationTestActivity extends AppCompatActivity {

    @Bind(R.id.btn_default)
    Button btnDefault;
    @Bind(R.id.btn_custom)
    Button btnCustom;

    NotificationManager manager;
    public static final String REMOTE_ACTION = "com.ldh.mydemo.remote.action.UPDATEVIEW";
    public static final String EXTRA_REMOTE_VIEWS = "com.ldh.mydemo.remoteviews";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification_test);
        ButterKnife.bind(this);
        manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
    }

    @Override
    public void onContentChanged() {
        super.onContentChanged();
    }

    public void onclick(View view) {
        switch (view.getId()) {
            case R.id.btn_default:
                sendDefaultNotificaiton();
                break;
            case R.id.btn_custom:
                sendCustomNotification();
                break;
            case R.id.btn_remote_weixin:
                updateRemoteActivity();
                break;
            default:
                break;
        }
    }

    private void updateRemoteActivity() {
        RemoteViews remoteViews = new RemoteViews(getPackageName(), R.layout.layout_notification);
        remoteViews.setTextViewText(R.id.msg, "msg from process:" + Process.myPid());
        remoteViews.setImageViewResource(R.id.icon, R.mipmap.boat);
        PendingIntent pendingIntent1 = PendingIntent.getActivity(this, 0, new Intent(this, WebViewDemo.class), PendingIntent.FLAG_UPDATE_CURRENT);
        PendingIntent pendingIntent2 = PendingIntent.getActivity(this, 0, new Intent(this, WebViewDemo2.class), PendingIntent.FLAG_UPDATE_CURRENT);
        remoteViews.setOnClickPendingIntent(R.id.tv_open_other, pendingIntent1);
        remoteViews.setOnClickPendingIntent(R.id.icon, pendingIntent2);
        Intent intentBroadcast = new Intent(REMOTE_ACTION);
        intentBroadcast.putExtra(EXTRA_REMOTE_VIEWS, remoteViews);
        sendBroadcast(intentBroadcast);
    }


    private void sendDefaultNotificaiton() {
        Intent intent = new Intent(this, WebViewDemo.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        Notification notification = new Notification.Builder(getApplicationContext())
                .setSmallIcon(R.mipmap.ic_launcher)
                .setTicker("hello world")
                .setContentTitle("title")
                .setContentText("text text")
                .setWhen(System.currentTimeMillis())
                .setAutoCancel(true)
                .setDefaults(Notification.DEFAULT_ALL)
                .setContentIntent(pendingIntent)
                .build();
        manager.notify(1, notification);
    }

    private void sendCustomNotification() {
        Intent intent = new Intent(this, WebViewDemo2.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        RemoteViews remoteViews = new RemoteViews(getPackageName(), R.layout.layout_notification);
        remoteViews.setOnClickPendingIntent(R.id.icon, pendingIntent);

        Notification notification = new Notification.Builder(getApplicationContext())
                .setSmallIcon(R.mipmap.ic_launcher)
                .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.mipmap.heye))
                .setTicker("hello world")
                .setWhen(System.currentTimeMillis())
                .setAutoCancel(true)
                .setDefaults(Notification.DEFAULT_ALL)
                .setContentIntent(pendingIntent)
                .build();
        notification.contentView = remoteViews;
        manager.notify(2, notification);
    }
}
