package com.example.mydemo.downloadmanager;

import android.app.DownloadManager;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Environment;
import android.os.IBinder;
import android.support.annotation.Nullable;

import java.io.File;

/**
 * Created by ldh on 2016/8/11 0011.
 * <p>
 * 简书 使用DownloadManager在service中下载并安装apk
 */
public class DownloadService extends Service {

    private DownloadManager dm;
    private long enqueue;
    private BroadcastReceiver receiver;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        receiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                Intent intent1 = new Intent(Intent.ACTION_VIEW);
                intent1.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent1.setDataAndType(Uri.fromFile(new File(Environment.getExternalStorageDirectory() + "/download/myApp.apk")),
                        "application/vnd.android.package-archive");
                startActivity(intent);
                //在监听到下载的文件后，会调用stopSelf自动关闭该service
                stopSelf();
            }
        };
        registerReceiver(receiver, new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE));
        startDownload();
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        unregisterReceiver(receiver);
        super.onDestroy();
    }

    private void startDownload() {
        dm = (DownloadManager) getSystemService(DOWNLOAD_SERVICE);
        DownloadManager.Request request = new DownloadManager.Request(Uri.parse("http://d.koudai.com/com.koudai.weishop/1000f/weishop_1000f.apk"));
        //移动网络情况下是否允许漫游
        request.setAllowedOverRoaming(false);

        request.setMimeType("application/vnd.android.package-archive");
        //设置下载的路径
        request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, "myApp.apk");
        enqueue = dm.enqueue(request);
    }

}
