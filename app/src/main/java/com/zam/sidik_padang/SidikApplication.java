package com.zam.sidik_padang;

import androidx.multidex.MultiDexApplication;

import io.paperdb.Paper;

public class SidikApplication extends MultiDexApplication {

    private static final String TAG = SidikApplication.class.getSimpleName();

    @Override
    public void onCreate() {
        super.onCreate();
        Paper.init(getApplicationContext());
    }
}
