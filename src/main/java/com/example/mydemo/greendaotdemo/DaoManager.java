package com.example.mydemo.greendaotdemo;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import org.greenrobot.greendao.identityscope.IdentityScopeType;

/**
 * Created by ldh on 2017/7/18.
 */

public class DaoManager {
    private static final String DB_NAME = "videoupload.db";
    private static Context sContext;
    private static DaoMaster.DevOpenHelper sHelper;
    private static DaoMaster sMaster;
    private static DaoSession sSession;
    private SQLiteDatabase db;

    private DaoManager() {
        //no instance
    }

    public static void init(Context appContext) {

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
            Log.e("_upload_", (e != null ? e.getMessage() : ""));
        }
    }

    public static DaoMaster.DevOpenHelper getHelper() {
        if (sHelper == null) {
            sHelper = new DaoMaster.DevOpenHelper(sContext, DB_NAME);
        }
        return sHelper;
    }

    public static DaoMaster getMaster() {
        if (sMaster == null) {
            sMaster = new DaoMaster(getHelper().getWritableDatabase());
        }
        return sMaster;
    }

    public static DaoSession getSession() {
        //查询出来的数据和数据库数据不一致 (可能是greenDAO缓存导致, 因此改成每次使用一个新的session来操作数据库)
        if (sSession == null) {
            sSession = getMaster().newSession(IdentityScopeType.None);
        }
        return sSession;

//        return getMaster().newSession(IdentityScopeType.None);
    }
}
