package com.zam.sidik_padang.home.ppob.berita;

import android.os.Bundle;
import android.view.View;
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
 * Created by supriyadi on 8/11/17.
 */

public class BeritaActivity extends BaseLogedinActivity
        implements BeritaAdapter.OnBeritaItemClickListener {

    private static final String VOLLEY_TEAG = BeritaActivity.class.getName();
    private List<Berita> list;
    private BeritaAdapter adapter;
    private View progressbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_berita);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        ViewCompat.setTransitionName(toolbar, "transisi_berita");
        progressbar = findViewById(R.id.activity_berita_Progressbar);
        list = new ArrayList<>();
        adapter = new BeritaAdapter(list, this);
        RecyclerView recyclerView = findViewById(R.id.activity_berita_RecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
        recyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadBeritaDariServer();
    }

    private void loadBeritaDariServer() {
        if (!Util.isInternetAvailible(this)) {
            Toast.makeText(this, R.string.no_internet_connection, Toast.LENGTH_SHORT).show();
            return;
        }

        String url = Config.URL_BERITA_PPOB + "aksi=1";
        progressbar.setVisibility(View.VISIBLE);
        VolleyStringRequest request = new VolleyStringRequest(url, new VolleyStringRequest.Callback() {
            @Override
            public void onResponse(JsonObject jsonObject) {
                debug(getClass(), "load berita. " + jsonObject);
                progressbar.setVisibility(View.INVISIBLE);
                if (jsonObject.get("success").getAsBoolean()) {
                    JsonArray array = jsonObject.get("berita").getAsJsonArray();
                    list.clear();
                    Gson gson = new Gson();
                    for (JsonElement element : array) {
                        Berita berita = gson.fromJson(element, Berita.class);
                        list.add(berita);
                    }
                    adapter.notifyDataSetChanged();
                }
            }
        });
        request.setTag(VOLLEY_TEAG);
        VolleySingleton.getInstance(this).getRequestQueue().add(request);
    }


    @Override
    public void onBeritaItemClick(int p) {
        list.get(p).collapsed = !list.get(p).collapsed;
        if (!list.get(p).collapsed) list.get(p).hits++;
        adapter.notifyItemChanged(p);
    }


    @Override
    protected void onDestroy() {
        VolleySingleton.getInstance(this).getRequestQueue().cancelAll(VOLLEY_TEAG);
        super.onDestroy();
    }
}
