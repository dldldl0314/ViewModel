package com.zzp.viewmodel;

import android.annotation.SuppressLint;
import android.app.Application;
import android.content.Context;

import androidx.room.Room;

import com.tencent.mmkv.MMKV;
import com.zzp.viewmodel.db.AppDatabase;
import com.zzp.viewmodel.network.NetworkApi;
import com.zzp.viewmodel.utils.MVUtils;

/**
 * 自定义 Application
 * @author llw
 */
public class BaseApplication extends Application {

    @SuppressLint("StaticFieldLeak")
    public static Context context;
    //数据库
    public static AppDatabase db;


    @Override
    public void onCreate() {
        super.onCreate();
        //初始化
        NetworkApi.init(new NetworkRequiredInfo(this));
        context = getApplicationContext();
        //MMKV初始化
        MMKV.initialize(this);
        //MVUtils初始化
        MVUtils.getInstance();
        //创建本地数据库
        db = AppDatabase.getInstance(this);


    }

    public static Context getContext() {
        return context;
    }
    public static AppDatabase getDb(){
        return db;
    }

}
