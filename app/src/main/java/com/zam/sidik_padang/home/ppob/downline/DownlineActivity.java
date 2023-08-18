package com.zam.sidik_padang.home.ppob.downline;

import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;
import androidx.core.view.ViewCompat;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
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
 * Created by supriyadi on 7/29/17.
 */

public class DownlineActivity extends BaseLogedinActivity {

    private final String VOLLEY_TAG = getClass().getName();
    private List<Downline> list;
    private DownlineListAdapter adapter;
    private View progressbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_downline);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        ViewCompat.setTransitionName(toolbar, "transisi_downline");
        list = new ArrayList<>();
        adapter = new DownlineListAdapter(list);
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.activity_downline_RecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
        recyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        ((TextView) findViewById(R.id.item_downline_TextViewId)).setTypeface(Typeface.DEFAULT_BOLD);
        ((TextView) findViewById(R.id.item_downline_TextViewNama)).setTypeface(Typeface.DEFAULT_BOLD);
        progressbar = findViewById(R.id.activity_downline_Progressbar);
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadDataFromServer();
    }

    private void loadDataFromServer() {
        if (!Util.isInternetAvailible(this)) {
            Toast.makeText(this, R.string.no_internet_connection, Toast.LENGTH_SHORT).show();
            return;
        }
        progressbar.setVisibility(View.VISIBLE);
        String url = Config.URL_DOWNLINE + "aksi=1&userid=" + user.userid;
        debug(getClass(), "Load downline url=" + url);
        VolleyStringRequest request = new VolleyStringRequest(url, new VolleyStringRequest.Callback() {
            @Override
            public void onResponse(JsonObject jsonObject) {
                debug(getClass(), "Load downline response=" + jsonObject);
                progressbar.setVisibility(View.INVISIBLE);
                if (jsonObject.get("success").getAsBoolean()) {
                    JsonArray ja = jsonObject.get("downline").getAsJsonArray();
                    Downline downline;
                    Gson gson = new Gson();
                    list.clear();
                    for (JsonElement je : ja) {
                        downline = gson.fromJson(je, Downline.class);
                        list.add(downline);
                    }
                    adapter.notifyDataSetChanged();
                } else
                    Toast.makeText(DownlineActivity.this, jsonObject.get("message").getAsString(), Toast.LENGTH_SHORT).show();
            }
        });
        request.setTag(VOLLEY_TAG);
        VolleySingleton.getInstance(this).getRequestQueue().add(request);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        VolleySingleton.getInstance(this).getRequestQueue().cancelAll(VOLLEY_TAG);
    }
}
