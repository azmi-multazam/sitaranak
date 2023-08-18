package com.zam.sidik_padang.roodiskusi.fcm;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.util.Log;

import androidx.core.app.NotificationCompat;

//import com.google.firebase.messaging.FirebaseMessagingService;
//import com.google.firebase.messaging.RemoteMessage;
import com.google.gson.Gson;

import java.util.Map;

//import com.zam.sidik_padang.BuildConfig;
import com.zam.sidik_padang.R;
import com.zam.sidik_padang.roodiskusi.Common;
import com.zam.sidik_padang.roodiskusi.Group;
import com.zam.sidik_padang.roodiskusi.group.OpenGroupActivity;
import com.zam.sidik_padang.util.User;
import com.zam.sidik_padang.util.Util;


/*
public class MyFirebaseMessagingService extends FirebaseMessagingService {


    @Override
    public void onMessageReceived(RemoteMessage rm) {
        debug("onMessageReceived. data size: " + rm.getData().size());
        super.onMessageReceived(rm);
        if (rm.getData().size() > 0) {
            sendNotification(rm.getData());
        }
    }

    private void sendNotification(Map<String, String> data) {
        debug("Send Notification. Data: " + data);
        User loginUser = Util.getSavedUser(this);
        if (loginUser == null) return;
        Group group = new Gson().fromJson(data.get("group"), Group.class);
        if (OpenGroupActivity.CURRENT_GROUP_ID.equals(group.id)) return;

        Intent intent = new Intent(this, OpenGroupActivity.class);
        intent.putExtra(Common.EXTRA_GROUP, group);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 , intent,
                PendingIntent.FLAG_ONE_SHOT | PendingIntent.FLAG_IMMUTABLE);

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        String chanelId = "obrolan";
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(chanelId, "Obrolan", NotificationManager.IMPORTANCE_DEFAULT);
            channel.setDescription("Notifikasi ketika ada pesan obrolan baru");
            assert notificationManager != null;
            notificationManager.createNotificationChannel(channel);
        }

        String notifTitle = group.name;
        if (notifTitle == null || notifTitle.isEmpty()) {
            if (group.users.size() == 2) {
                for (String uid : group.users.keySet()) {
                    if (!uid.equals(loginUser.userid)) {
                        Map<String, Object> userMap = (Map<String, Object>) group.users.get(uid);
                        if (userMap != null) notifTitle = (String) userMap.get("name");
                    }
                }
            }
        }

        if (notifTitle == null || notifTitle.isEmpty()) notifTitle = getString(R.string.app_name);
        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this, chanelId)
                .setSmallIcon(R.drawable.ic_forum)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setContentTitle(notifTitle)
                .setContentText(data.get("message"))
                .setAutoCancel(true)
                .setShowWhen(true)
                .setSound(defaultSoundUri)
                .setVibrate(new long[]{200, 300, 500})
                .setContentIntent(pendingIntent);

        notificationManager.notify(Common.GROUP_NOTIF_ID, notificationBuilder.build());
    }


    private void debug(String s) {
        Log.d(getClass().getName(), s);
        //if (BuildConfig.DEBUG) Log.e(getClass().getName(), s);
    }
}
*/
