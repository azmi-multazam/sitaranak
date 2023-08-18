package com.zam.sidik_padang.util;

import android.util.Log;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

//import com.zam.sidik_padang.BuildConfig;

public class VolleyStringRequest extends StringRequest {

    private static JsonObject defaultResponseJsonObject;


    public VolleyStringRequest(String url, final Callback callback) {
        super(Method.GET, url, new Response.Listener<String>() {

            @Override
            public void onResponse(String p1) {
                if (p1 == null || !p1.startsWith("{")) {
                    Log.e(getClass().getName(), "OnResponse: " + p1);
                    if (defaultResponseJsonObject == null) {
                        defaultResponseJsonObject = new JsonObject();
                        defaultResponseJsonObject.addProperty("success", false);
                        defaultResponseJsonObject.addProperty("message", "Terjadi kesalahan 90281");
                    }
                    callback.onResponse(defaultResponseJsonObject);
                    return;
                }
                Gson gson = new Gson();
                JsonElement je = gson.fromJson(p1, JsonElement.class);
                if (je != null) {
                    callback.onResponse(je.getAsJsonObject());
                } else {
                    if (defaultResponseJsonObject == null) {
                        defaultResponseJsonObject = new JsonObject();
                        defaultResponseJsonObject.addProperty("success", false);
                        defaultResponseJsonObject.addProperty("message", "Terjadi kesalahan 90281");
                    }
                    callback.onResponse(defaultResponseJsonObject);
                }
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError p1) {
                if (defaultResponseJsonObject == null) {
                    defaultResponseJsonObject = new JsonObject();
                    defaultResponseJsonObject.addProperty("success", false);

                }
                String errorMessage = p1.getMessage() == null ? "Check your internet connnection" : p1.getMessage();
                defaultResponseJsonObject.addProperty("message", (errorMessage.contains("host") || errorMessage.contains("out")) ? "Check your internet connection" : errorMessage);
                callback.onResponse(defaultResponseJsonObject);

            }
        });
        setShouldCache(false);
        setRetryPolicy(new DefaultRetryPolicy(20000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
    }

    private void debug(String s) {
        Log.d(getClass().getName(), s);
        //if (BuildConfig.DEBUG) Log.e(getClass().getName(), s);
    }

    public static interface Callback {
        void onResponse(JsonObject jsonObject);
    }
}
