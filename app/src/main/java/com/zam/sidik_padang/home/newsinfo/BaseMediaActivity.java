package com.zam.sidik_padang.home.newsinfo;

import android.os.Bundle;
import android.view.View;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import com.zam.sidik_padang.BaseLogedinActivity;
import com.zam.sidik_padang.R;
import com.zam.sidik_padang.util.Config;
import com.zam.sidik_padang.util.Util;
import com.zam.sidik_padang.util.VolleySingleton;
import com.zam.sidik_padang.util.VolleyStringRequest;


public class BaseMediaActivity extends BaseLogedinActivity {
    private static final String PREF_MEDIA_RESPONSE = "media_response";
    private static final String TAG = BaseMediaActivity.class.getName();
    protected View textViewNoData;
    private View progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO: Implement this method
        super.onCreate(savedInstanceState);
        String savedResponse = sharedPreferences.getString(PREF_MEDIA_RESPONSE, "");

        onSetContentView();
        progressBar = findViewById(R.id.progressBar);
        textViewNoData = findViewById(R.id.textViewNoData);

        if (!savedResponse.isEmpty() && savedResponse.startsWith("{")) {
            MediaApiResponse response = new Gson().fromJson(savedResponse, MediaApiResponse.class);
            if (response.success) {
                onUpdateView(response);
            }
        }
        loadFromServer();

    }

    protected void onSetContentView() {

    }

    private void loadFromServer() {
        if (!Util.isInternetAvailible(this)) return;
        progressBar.setVisibility(View.VISIBLE);
        debug(getClass(), "Load media from server");
        String link = Config.URL_VIDEO + "?userid=" + user.userid;
        debug(getClass(), "Load media link: " + link);
        VolleyStringRequest request = new VolleyStringRequest(link, new VolleyStringRequest.Callback() {

            @Override
            public void onResponse(JsonObject jsonObject) {
                debug(getClass(), "Media response: " + jsonObject);
                progressBar.setVisibility(View.INVISIBLE);
                MediaApiResponse response = new Gson().fromJson(jsonObject, MediaApiResponse.class);
                if (response.success) {
                    sharedPreferences.edit().putString(PREF_MEDIA_RESPONSE, "" + jsonObject).apply();
                    onUpdateView(response);
                }
            }
        });
        request.setTag(TAG);
        VolleySingleton.getInstance(this).getRequestQueue().add(request);

    }


    protected void onUpdateView(MediaApiResponse response) {
        debug(getClass(), "updateView");
    }

    @Override
    protected void onDestroy() {
        VolleySingleton.getInstance(this).getRequestQueue().cancelAll(TAG);
        super.onDestroy();
    }


}
