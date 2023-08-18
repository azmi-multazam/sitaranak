package com.zam.sidik_padang.util;

import android.util.Log;

import com.android.volley.NetworkResponse;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONException;
import org.json.JSONObject;

public class VolleyJsonObjectRequest extends JsonObjectRequest {


    public VolleyJsonObjectRequest(String link, JSONObject jsonPost, final Response.Listener<JSONObject> listener) {
        super(Method.POST, link, jsonPost, listener, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError p1) {

                Log.e(this.getClass().getName(), "" + p1);
                JSONObject jo = new JSONObject();
                try {
                    jo.put("message", p1.getMessage());
                } catch (JSONException e) {
                }
                listener.onResponse(jo);
            }
        });

    }

    @Override
    protected Response<JSONObject> parseNetworkResponse(NetworkResponse response) {
        try {
            String jsonString = new String(response.data,
                    HttpHeaderParser.parseCharset(response.headers, PROTOCOL_CHARSET));

            return Response.success(new JSONObject(jsonString),
                    HttpHeaderParser.parseCacheHeaders(response));
        } catch (Exception e) {
            // return Response.error(new ParseError(e));
            Log.e(this.getClass().getName(), "Server respon = " + new String(response.data));
            Log.e(this.getClass().getName(), e.getMessage());
            JSONObject jo = new JSONObject();

            return Response.success(jo, null);
        }
    }
}
