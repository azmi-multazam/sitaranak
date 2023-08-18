package com.zam.sidik_padang.home.ppob.komisi;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;
import androidx.core.view.ViewCompat;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.util.List;

import com.zam.sidik_padang.BaseLogedinActivity;
import com.zam.sidik_padang.R;
import com.zam.sidik_padang.util.Config;
import com.zam.sidik_padang.util.Util;
import com.zam.sidik_padang.util.VolleySingleton;
import com.zam.sidik_padang.util.VolleyStringRequest;

/**
 * Created by supriyadi on 7/30/17.
 */

public class KoperasiActivity extends BaseLogedinActivity {

    private final String VOLLEY_TAG = getClass().getName();
    private List<Komisi> list;
    private KomisiLisAdapter adapter;
    private View progressbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_koperasi);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        ViewCompat.setTransitionName(toolbar, "transisi_komisi");
//		list = new ArrayList<>();
//		adapter = new KomisiLisAdapter(list);
//		progressbar = findViewById(R.id.activity_komisi_trx_downline_Progressbar);
//		RecyclerView recyclerView = (RecyclerView) findViewById(R.id.activity_komisi_trx_downline_RecyclerView);
//		recyclerView.setLayoutManager(new LinearLayoutManager(this));
//		recyclerView.setAdapter(adapter);
//		recyclerView.addItemDecoration(new DividerItemDecoration(this,DividerItemDecoration.VERTICAL));
    }

    @Override
    protected void onResume() {
        super.onResume();

//		loadDataFromServer();
    }

    private void loadDataFromServer() {
        if (!Util.isInternetAvailible(this)) {
            Toast.makeText(this, R.string.no_internet_connection, Toast.LENGTH_SHORT).show();
            return;
        }
        progressbar.setVisibility(View.VISIBLE);
        String url = Config.URL_DOWNLINE + "aksi=2&userid=" + user.userid;
        debug(getClass(), "Load Komisi trx url=" + url);
        VolleyStringRequest request = new VolleyStringRequest(url, new VolleyStringRequest.Callback() {
            @Override
            public void onResponse(JsonObject jsonObject) {
                debug(getClass(), "Load Komisi response=" + jsonObject);
                progressbar.setVisibility(View.INVISIBLE);
                if (jsonObject.get("success").getAsBoolean()) {
                    JsonArray ja = jsonObject.get("komisi").getAsJsonArray();
                    Komisi komisi;
                    Gson gson = new Gson();
                    list.clear();
                    for (JsonElement je : ja) {
                        komisi = gson.fromJson(je, Komisi.class);
                        list.add(komisi);
                    }
                    adapter.notifyDataSetChanged();
                } else
                    Toast.makeText(KoperasiActivity.this, jsonObject.get("message").getAsString(), Toast.LENGTH_SHORT).show();
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
