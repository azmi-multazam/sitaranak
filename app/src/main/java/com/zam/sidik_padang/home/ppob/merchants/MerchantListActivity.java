package com.zam.sidik_padang.home.ppob.merchants;

import android.os.Bundle;
import android.view.View;

import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.List;

import com.zam.sidik_padang.BaseLogedinActivity;
import com.zam.sidik_padang.R;
import com.zam.sidik_padang.util.Config;
import com.zam.sidik_padang.util.Util;
import com.zam.sidik_padang.util.VolleySingleton;
import com.zam.sidik_padang.util.VolleyStringRequest;

/**
 * Created by supriyadi on 2/18/18.
 */

public class MerchantListActivity extends BaseLogedinActivity {

    private static final String VOLLEY_TAG = MerchantListActivity.class.getName();
    private static final String PREF_RESPONSE = "list_merchant_response";
    private View textNoData, progressBar;
    private MerchantListAdapter adapter;
    private List<Merchant> list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_merchant);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        textNoData = findViewById(R.id.textView);
        progressBar = findViewById(R.id.progressBar);
        RecyclerView rv = (RecyclerView) findViewById(R.id.recyclerView);
        rv.setLayoutManager(new LinearLayoutManager(this));
        list = new ArrayList<>();
        adapter = new MerchantListAdapter(list);
        rv.setAdapter(adapter);

    }


    private void loadFromServer() {
        if (!Util.isInternetAvailible(this)) return;
        progressBar.setVisibility(View.VISIBLE);
        String link = Config.URL_MERCHANT_LIST + "aksi=1&userid=" + user.userid;
        debug(getClass(), "Load list merchant url: " + link);
        VolleyStringRequest request = new VolleyStringRequest(link, new VolleyStringRequest.Callback() {
            @Override
            public void onResponse(JsonObject jsonObject) {
                debug(getClass(), "Load list merchatnt response: " + jsonObject);
                progressBar.setVisibility(View.INVISIBLE);
                if (jsonObject.get("success").getAsBoolean()) {
                    sharedPreferences.edit().putString(PREF_RESPONSE, jsonObject.toString()).apply();
                    updateList(jsonObject);
                }
            }
        });

        request.setTag(VOLLEY_TAG);
        VolleySingleton.getInstance(this).getRequestQueue().add(request);
    }

    private void updateList(JsonObject jsonObject) {
        if (jsonObject == null) return;
        JsonElement arrayElement = jsonObject.get("list_merchent");
        if (arrayElement != null && arrayElement.isJsonArray()) {
            Merchant merchant;
            Gson gson = new Gson();
            list.clear();
            for (JsonElement element : arrayElement.getAsJsonArray()) {
                merchant = gson.fromJson(element, Merchant.class);
                list.add(merchant);
            }
            adapter.notifyDataSetChanged();
            textNoData.setVisibility(list.size() == 0 ? View.VISIBLE : View.INVISIBLE);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (Util.isInternetAvailible(this)) loadFromServer();
        String savedResponse = sharedPreferences.getString(PREF_RESPONSE, "");
        if (savedResponse.isEmpty() || !savedResponse.startsWith("{")) return;
        JsonElement element = new Gson().fromJson(savedResponse, JsonElement.class);
        if (element != null && element.isJsonObject()) updateList(element.getAsJsonObject());
    }

    @Override
    protected void onDestroy() {
        VolleySingleton.getInstance(this).getRequestQueue().cancelAll(VOLLEY_TAG);
        super.onDestroy();
    }
}
