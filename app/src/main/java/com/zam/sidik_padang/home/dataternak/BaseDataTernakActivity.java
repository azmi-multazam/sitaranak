package com.zam.sidik_padang.home.dataternak;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;

import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.zam.sidik_padang.BaseLogedinActivity;
import com.zam.sidik_padang.home.dataternak.detailternak.UpdateStatusTernakActivity;

public abstract class BaseDataTernakActivity extends BaseLogedinActivity {

    private boolean receiverTerdaftar = false;
    private LocalBroadcastManager broadcastManager;

    private final BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            debug(getClass(), "Onreceive action=" + intent.getAction());
            if (intent.getAction().equals(UpdateStatusTernakActivity.ACTION_STATUS_TERNAK_BERUBAH))
                onReceiveChanged();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (!receiverTerdaftar) {
            receiverTerdaftar = true;
            IntentFilter intent = new IntentFilter();
            intent.addAction(UpdateStatusTernakActivity.ACTION_STATUS_TERNAK_BERUBAH);
            broadcastManager = LocalBroadcastManager.getInstance(this);
            broadcastManager.registerReceiver(receiver, intent);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (receiverTerdaftar) {
            receiverTerdaftar = false;
            broadcastManager.unregisterReceiver(receiver);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    protected abstract void onReceiveChanged();
}
