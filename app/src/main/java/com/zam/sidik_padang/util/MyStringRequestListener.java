package com.zam.sidik_padang.util;

import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.StringRequestListener;
import com.google.gson.Gson;

import com.zam.sidik_padang.BaseApiResponse;

/**
 * Created by supriyadi on 4/24/18.
 */

public abstract class MyStringRequestListener implements StringRequestListener {

    private BaseApiResponse baseApiResponse;
    private Gson gson;

    public MyStringRequestListener() {
        baseApiResponse = new BaseApiResponse();
        baseApiResponse.success = false;
        baseApiResponse.message = "Terjadi kesalahan 5361";
        gson = new Gson();

    }

    @Override
    public void onResponse(String response) {
        if (response != null && response.startsWith("{")) {
            response(response);
        } else {
            baseApiResponse.message = "onResponse: " + response;
            response(gson.toJson(baseApiResponse));
        }
    }

    @Override
    public void onError(ANError anError) {
        baseApiResponse.message = "onError: " + anError.getMessage();
        response(gson.toJson(baseApiResponse));
    }

    protected abstract void response(String response);
}
