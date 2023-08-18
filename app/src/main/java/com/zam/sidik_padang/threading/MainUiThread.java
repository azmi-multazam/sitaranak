package com.zam.sidik_padang.threading;

import android.os.Handler;
import android.os.Looper;

/**
 * A singleton wrapper around {@link Handler} that helps to post work to the main UI thread.
 */
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
