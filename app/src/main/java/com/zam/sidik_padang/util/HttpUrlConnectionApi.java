package com.zam.sidik_padang.util;

import android.os.AsyncTask;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;
import java.util.Set;

//import com.zam.sidik_padang.BuildConfig;

/**
 * Created by supriyadi on 10/8/17.
 */

public class HttpUrlConnectionApi {

    public static void get(String url, Callback callback) {
        new PanggilServer(url, null, callback);
    }

    public static void post(String url, Map<String, String> params, Callback callback) {
        new PanggilServer(url, params, callback);
    }

    public interface Callback {
        void onResponse(JsonObject jsonObject);
    }

    private static class PanggilServer extends AsyncTask<Void, Void, JsonObject> {
        private String link;
        private Callback callback;
        private Map<String, String> params;
        private JsonObject hasil;


        public PanggilServer(String link, Map<String, String> params, Callback callback/*, int id*/) {
//			debug("Panggil server baru");
            hasil = new JsonObject();
            hasil.addProperty("success", false);
            hasil.addProperty("message", "Masalah jaringan");
            this.link = link;
            this.params = params;
            this.callback = callback;
            this.execute();
        }

        @Override
        protected JsonObject doInBackground(Void... params) {
            return myDoinBackground();
        }

        private synchronized JsonObject myDoinBackground() {

            if (isCancelled()) return hasil;
            try {
//				debug("membuat koneksi");
                URL url = new URL(link);
                HttpURLConnection koneksi = (HttpURLConnection) url.openConnection();
                koneksi.setRequestMethod(params != null && params.size() > 0 ? "POST" : "GET");
                koneksi.setDoInput(true);
                koneksi.setDoOutput(true);
                koneksi.setConnectTimeout(20000);
                koneksi.setReadTimeout(20000);
                OutputStream os = koneksi.getOutputStream();
                if (params != null && params.size() > 0) {
                    Set<String> keys = params.keySet();
                    String result = "";
                    boolean first = true;
                    for (String str : keys) {
                        if (first) first = false;
                        else result += "&";
                        result += str + "=" + Util.toUrlEncoded(params.get(str));
                    }
                    BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(os));
                    bw.write(result);
                }
                os.flush();
                os.close();
                if (isCancelled()) return hasil;
                InputStream is = koneksi.getInputStream();

                String responString = null;
                BufferedReader reader = new BufferedReader(new InputStreamReader(is));
                StringBuilder builder = new StringBuilder();
                String line = null;
                while ((line = reader.readLine()) != null) {
                    builder.append(line);
                }
                responString = builder.toString();
                debug(responString);
                if (responString.startsWith("{")) {
                    hasil = new Gson().fromJson(responString, JsonElement.class).getAsJsonObject();
                }
                is.close();
                return hasil;
            } catch (Exception e) {
                if (e.getMessage() != null) Log.e(PanggilServer.class.getName(), e.getMessage());
                return hasil;
            }
        }

        @Override
        protected void onPostExecute(JsonObject jsonObject) {
            debug("onPost Execute");
            if (callback != null) callback.onResponse(jsonObject);
        }

        @Override
        protected void onCancelled() {
            debug("onCancelled");
            callback.onResponse(hasil);
        }

        void debug(String s) {
            Log.d(getClass().getName(), s);
            //if (BuildConfig.DEBUG) Log.e(getClass().getName(), s);
        }
    }


}
