package com.zam.sidik_padang.roodiskusi.fcm;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Base64;
import android.util.Log;

import androidx.annotation.NonNull;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.StringRequestListener;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
//import com.google.firebase.firestore.DocumentSnapshot;
//import com.google.firebase.firestore.FirebaseFirestore;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

//import com.zam.sidik_padang.BuildConfig;
import com.zam.sidik_padang.roodiskusi.Group;
import com.zam.sidik_padang.roodiskusi.group.Message;
import com.zam.sidik_padang.util.User;
import com.zam.sidik_padang.util.Util;

public class Fcm {

    private static final String GARAM = "gaRaM";
    private static final String PREF_KEY = "k";
    private static Fcm instance = null;
    private SharedPreferences preferences;
    private User user;
    //private FirebaseFirestore firestore;
    private String apiKey = "";

    /*
    private Fcm(Context context) {
        preferences = PreferenceManager.getDefaultSharedPreferences(context);
        user = Util.getSavedUser(context);
        //firestore = FirebaseFirestore.getInstance();
    }


    public static Fcm getInstance(Context context) {
        if (instance == null) instance = new Fcm(context);
        return instance;
    }

    public void send(final Group group, final Message message) {
        if (apiKey.isEmpty()) {
            String saved = preferences.getString(PREF_KEY, "");
            if (!saved.isEmpty())
                apiKey = new String(Base64.decode(saved, Base64.DEFAULT)).replace(GARAM, "");
        }
        debug("send apikey: " + apiKey);
        if (apiKey.isEmpty()) {
            loadApiKey(group, message);
            return;
        }

        SuccesListener listener = new SuccesListener(new OnLoadTokensDone() {
            @Override
            public void onLoadDone(List<String> tokens) {
                debug("onLoadDone: " + tokens.toString());
                try {
                    sendMessage(tokens, message, group);
                } catch (JSONException e) {
                    debug("Error " + e.getMessage());
                    e.printStackTrace();
                }
            }
        });

        for (String uid : group.users.keySet()) {
            if (uid.equals(user.userid)) continue;
            //listener.totalWaiter++;
            //firestore.collection(User.USERS).document(uid).get().addOnSuccessListener(listener);
        }


    }

    private void sendMessage(List<String> tokens, Message message, Group group) throws JSONException {
        if (tokens.isEmpty()) return;
        Map<String, Object> postMap = new HashMap<>();
        if (tokens.size() == 1)
            postMap.put("to", tokens.get(0));
        else {
            JSONArray ja = new JSONArray();
            for (String s : tokens) ja.put(s);
            postMap.put("registration_ids", ja);
        }

        postMap.put("priority", "high");
        JSONObject postJson = new JSONObject(postMap);
        Map<String, String> dataMap = new HashMap<>();
        dataMap.put("userid", user.userid);
        dataMap.put("message", message.message);
        dataMap.put("group", new Gson().toJson(group));
        postJson.put("data", new JSONObject(dataMap));
        debug("Send Message: " + postJson);
        AndroidNetworking
                .post("https://fcm.googleapis.com/fcm/send")
                .addHeaders("Content-Type", "application/json")
                .addHeaders("Accept", "application/json")
                .addHeaders("Authorization", "key=" + apiKey)
                .addJSONObjectBody(postJson)
                .setPriority(Priority.HIGH)
                .build()
                .getAsString(new StringRequestListener() {
                    @Override
                    public void onResponse(String response) {
                        debug("fcm response: " + response);
                    }

                    @Override
                    public void onError(ANError anError) {
                        Log.e(getClass().getName(), anError.getMessage());
                        anError.printStackTrace();
                    }
                });

    }

    /*
    private void loadApiKey(final Group group, final Message message) {
        debug("Load apikey");
        firestore.collection("Common").document("FCM").get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                debug("load Api key complite. Success: " + task.isSuccessful());
                if (task.isSuccessful()) {
                    DocumentSnapshot ds = task.getResult();
                    if (ds != null && ds.exists()) {
                        apiKey = ds.getString("api_key");
                        String encoded = Base64.encodeToString((apiKey + GARAM).getBytes(), Base64.DEFAULT);
                        preferences.edit().putString(PREF_KEY, encoded).apply();
                        send(group, message);
                    } else debug("FCM API KEY NOT exist");
                }
            }
        });
    }
     */

    private void debug(String s) {
        Log.d(getClass().getName(), s);
        //if (BuildConfig.DEBUG) Log.e(getClass().getName(), s);
    }

    private interface OnLoadTokensDone {
        void onLoadDone(List<String> tokens);
    }

    /*
    private class SuccesListener implements OnSuccessListener<DocumentSnapshot> {

        int totalWaiter = 0;
        private List<String> tokens = new ArrayList<>();
        private OnLoadTokensDone listener;

        SuccesListener(OnLoadTokensDone listener) {
            this.listener = listener;
        }

        @Override
        public void onSuccess(DocumentSnapshot documentSnapshot) {
            debug("SuccesListener.OnSucces: " + documentSnapshot.toString());
            if (documentSnapshot.exists()) {
                User u = documentSnapshot.toObject(User.class);
                if (u != null && u.fcm != null && !u.fcm.isEmpty()) tokens.add(u.fcm);
            }

            totalWaiter--;
            if (totalWaiter <= 0) listener.onLoadDone(tokens);

        }
    }
     */

}
