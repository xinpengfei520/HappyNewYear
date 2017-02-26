package com.junmei.mylove.updatelover;

import android.app.Application;

import org.xutils.x;

/**
 * Created by junmei on 2016/10/6.
 */
public class MyLoveApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        x.Ext.init(this);
        x.Ext.setDebug(true);
    }
}
