package com.zam.sidik_padang.home.dataternak.vm;

import android.os.Handler;
import android.os.Looper;

public class MainUiThread {

    private static MainUiThread mainUiThread;

    private final Handler handler;

    private MainUiThread() {
        handler = new Handler(Looper.getMainLooper());
    }

    public static synchronized MainUiThread getInstance() {
        if (mainUiThread == null) {
            mainUiThread = new MainUiThread();
        }
        return mainUiThread;
    }

    public void post(Runnable runnable) {
        handler.post(runnable);
    }

}
