package com.example.mydemo;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.example.mydemo.base.BaseApplication;
import com.example.mydemo.greendaotdemo.DaoMaster;
import com.example.mydemo.greendaotdemo.DaoSession;
import com.facebook.stetho.Stetho;
import com.ldh.androidlib.image.ImageLoaderManager;

import org.greenrobot.greendao.database.Database;

/**
 * Created by ldh on 2016/3/31 0031.
 */
public class AppContext extends BaseApplication {

    private static RequestQueue queues;
    private static AppContext instance;


    private DaoMaster.DevOpenHelper mHelper;
    private SQLiteDatabase db;
    private DaoMaster mDaoMaster;
    private DaoSession mDaoSession;

    @Override
    public void onCreate() {
        super.onCreate();
        //方便数据库查看
        Stetho.initializeWithDefaults(this);
        instance = this;
        queues = Volley.newRequestQueue(this);
        //初始化图片加载框架
        ImageLoaderManager.getInstance().init(this);
        setDatabase();
    }

    public static RequestQueue getHttpQueses() {
        return queues;
    }

    public static Context getInstance() {
        return instance;
    }

    /**
     * 设置greenDao
     */

    private void setDatabase() {

        // 通过DaoMaster 的内部类 DevOpenHelper，你可以得到一个便利的SQLiteOpenHelper 对象。
        // 可能你已经注意到了，你并不需要去编写「CREATE TABLE」这样的 SQL 语句，因为greenDAO 已经帮你做了。
        // 注意：默认的DaoMaster.DevOpenHelper 会在数据库升级时，删除所有的表，意味着这将导致数据的丢失。
        // 所以，在正式的项目中，你还应该做一层封装，来实现数据库的安全升级。
        mHelper = new DaoMaster.DevOpenHelper(this, "notes-db", null) {
            @Override
            public void onUpgrade(Database db, int oldVersion, int newVersion) {
                super.onUpgrade(db, oldVersion, newVersion);
            }
        };
        db = mHelper.getWritableDatabase();
        // 注意：该数据库连接属于DaoMaster，所以多个 Session 指的是相同的数据库连接。
        mDaoMaster = new DaoMaster(db);
        mDaoSession = mDaoMaster.newSession();
    }

    public DaoSession getDaoSession() {
        return mDaoSession;
    }


    public SQLiteDatabase getDb() {
        return db;
    }

}
