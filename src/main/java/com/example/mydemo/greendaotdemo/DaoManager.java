package com.example.mydemo.greendaotdemo;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.example.mydemo.BuildConfig;

import org.greenrobot.greendao.identityscope.IdentityScopeType;


/**
 * Created by ldh on 2017/7/18.
 */

public class DaoManager {
    private static final String DB_NAME = "videoupload.db";
    private Context sContext;
    private DaoMaster.DevOpenHelper sHelper;
    private DaoMaster sMaster;
    private DaoSession sSession;
    private SQLiteDatabase db;

    public void init(Context appContext) {

        //avoid null pointer exception
        if (appContext == null) return;

        //invalid state handle
        if (sHelper != null) {
            sContext = null;
            sHelper.close();
        }

        // init field
        sContext = appContext;
        try {
            sHelper = new DaoMaster.DevOpenHelper(appContext, DB_NAME, null);
            sMaster = new DaoMaster(sHelper.getWritableDatabase());
            sSession = sMaster.newSession();
        } catch (Exception e) {
            if (BuildConfig.DEBUG)
                Log.e("_upload_", (e != null ? e.getMessage() : ""));
        }
    }

    public DaoMaster.DevOpenHelper getHelper() {
        if (sHelper == null) {
            sHelper = new DaoMaster.DevOpenHelper(sContext, DB_NAME);
        }
        return sHelper;
    }

    public DaoMaster getMaster() {
        if (sMaster == null) {
            sMaster = new DaoMaster(getHelper().getWritableDatabase());
        }
        return sMaster;
    }

    public DaoSession getSession() {
        //查询出来的数据和数据库数据不一致 (可能是greenDAO缓存导致, 因此改成每次使用一个新的session来操作数据库)
        if (sSession == null) {
            sSession = getMaster().newSession(IdentityScopeType.None);
        }
        return sSession;

//        return getMaster().newSession(IdentityScopeType.None);
    }
}
