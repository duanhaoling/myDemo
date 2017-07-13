package com.example.mydemo.remoteview;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RemoteViews;

import com.example.mydemo.R;


public class ReceiveNotificationActivity extends AppCompatActivity {

    private static String TAG = "ReceiveNotificationActivity";

    public static final String REMOTE_ACTION = "com.ldh.mydemo.remote.action.UPDATEVIEW";
    public static final String EXTRA_REMOTE_VIEWS = "com.ldh.mydemo.remoteviews";

    private LinearLayout mRemoteViewsContent;
    private BroadcastReceiver mRemoteViewsReceiver = new BroadcastReceiver() {

        @SuppressLint("LongLogTag")
        @Override
        public void onReceive(Context context, Intent intent) {
            Log.i(TAG, "onReceive : action = " + intent.getAction());
            RemoteViews remoteViews = intent.getParcelableExtra(EXTRA_REMOTE_VIEWS);
            //收到的remoteViews一直为null
            if (remoteViews != null) {
                updateUI(remoteViews);
            }
        }


    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_receive_notification);
        initView();
//        TAG = getClass().getSimpleName();
    }


    @Override
    protected void onDestroy() {
        unregisterReceiver(mRemoteViewsReceiver);
        super.onDestroy();
    }

    private void initView() {
        mRemoteViewsContent = (LinearLayout) findViewById(R.id.ll_remote_views_content);
        IntentFilter filter = new IntentFilter(REMOTE_ACTION);
        registerReceiver(mRemoteViewsReceiver, filter);

    }

    private void updateUI(RemoteViews remoteViews) {
        View view = remoteViews.apply(this, mRemoteViewsContent);
        mRemoteViewsContent.addView(view);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuItem menuItem = menu.add(0, 0x100, 0, "发送通知");
        MenuItemCompat.setShowAsAction(menuItem, MenuItemCompat.SHOW_AS_ACTION_ALWAYS);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case 0x100:
                Intent intent = new Intent(this, NotificationTestActivity.class);
                startActivity(intent);
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
