package com.zam.sidik_padang.home.dataternak.rangking;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
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
 * Created by supriyadi on 9/10/17.
 */

public class RangkingActivity extends BaseLogedinActivity {

    private static final String VOLLEY_TAG = RangkingActivity.class.getName();
    private List<RangkingItem> list;
    private RangkingListAdapter adapter;
    private View progressbar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rangking);
        Toolbar toolbar = (Toolbar) findViewById(R.id.activity_rangking_Toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        toolbar.setSubtitle(user.nama + " - " + user.userid);
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.activity_rangking_RecyclerView);
//		recyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
//		TextView textViewIdTernak = (TextView) findViewById(R.id.item_rangking_TextViewIdTernak);
//		textViewIdTernak.setTypeface(Typeface.DEFAULT_BOLD);
//		TextView textViewBeratLahir = (TextView) findViewById(R.id.item_rangking_TextViewBeratLahir);
//		textViewBeratLahir.setTypeface(Typeface.DEFAULT_BOLD);
//		((View) textViewBeratLahir.getParent()).setBackgroundColor(Color.TRANSPARENT);
        list = new ArrayList<>();
        adapter = new RangkingListAdapter(list);
        recyclerView.setAdapter(adapter);
        progressbar = findViewById(R.id.activity_rangking_Progressbar);
    }

    @Override
    protected void onStart() {
        super.onStart();
        loadData();
    }

    private void loadData() {
        if (!Util.isInternetAvailible(this)) {
            Toast.makeText(this, "Tidak ada koneksi internet", Toast.LENGTH_SHORT).show();
            return;
        }

        String url = Config.URL_LIHAT_DATA_TERNAK + "?aksi=7&userid=" + user.userid;
        debug(getClass(), "Load Rangking url=" + url);
        progressbar.setVisibility(View.VISIBLE);
        VolleyStringRequest request = new VolleyStringRequest(url, new VolleyStringRequest.Callback() {
            @Override
            public void onResponse(JsonObject jsonObject) {
                debug(getClass(), "Load rangking response=" + jsonObject);
                progressbar.setVisibility(View.INVISIBLE);
                JsonElement je = jsonObject.get("success");
                if (je.isJsonNull()) return;
                Gson gson = new Gson();
                RangkingItem rangkingItem;
                if (je.getAsBoolean()) {
                    list.clear();
                    JsonArray ja = jsonObject.get("rangking").getAsJsonArray();
                    for (JsonElement element : ja) {
                        rangkingItem = gson.fromJson(element, RangkingItem.class);
                        list.add(rangkingItem);
                    }
                    adapter.notifyDataSetChanged();
                } else {
                    Toast.makeText(RangkingActivity.this, jsonObject.get("message").getAsString(), Toast.LENGTH_SHORT).show();
                }
            }
        });
        request.setTag(VOLLEY_TAG);
        VolleySingleton.getInstance(this).getRequestQueue().add(request);
    }

    @Override
    protected void onDestroy() {
        VolleySingleton.getInstance(this).getRequestQueue().cancelAll(VOLLEY_TAG);
        super.onDestroy();
    }
}
