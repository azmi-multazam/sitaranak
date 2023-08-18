package com.zam.sidik_padang;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import com.zam.sidik_padang.home.selectregion.Region;
import com.zam.sidik_padang.home.selectregion.RegionListAdapter;
import com.zam.sidik_padang.util.Config;
import com.zam.sidik_padang.util.VolleySingleton;
import com.zam.sidik_padang.util.VolleyStringRequest;


public class SelectRegionActivity extends BaseLogedinActivity
        implements RegionListAdapter.OnRegionItemClickListener {


    public static final String EXTRA_WILAYAH_MODE = "extra_wilayah_mode";
    public static final String EXTRA_WILAYAH_CODE = "extra_wilayah_code";
    public static final String EXTRA_WILAYAH = "extra_wilayah";
    private final String VOLLEY_TAG = getClass().getName();
    private List<Region> uiList;
    private RegionListAdapter adapter;
    private String wilayahMode = "provinsi";
    private String wilayahCode = "0";
    private String titleBar = "";

    //"id":"12920","nama":"SUMATERA BARAT"
    private static final String[] codeProvinsi = {"12920", "SUMATERA BARAT"};

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putString(EXTRA_WILAYAH_CODE, wilayahCode);
        outState.putString(EXTRA_WILAYAH_MODE, wilayahMode);
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        if (savedInstanceState.containsKey(EXTRA_WILAYAH_MODE)) {
            wilayahCode = savedInstanceState.getString(EXTRA_WILAYAH_CODE);
            wilayahMode = savedInstanceState.getString(EXTRA_WILAYAH_MODE);
        }
        super.onRestoreInstanceState(savedInstanceState);

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO: Implement this method
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_region);
        Intent intent = getIntent();
        if (intent != null && intent.hasExtra(EXTRA_WILAYAH_CODE)) {
            wilayahMode = intent.getStringExtra(EXTRA_WILAYAH_MODE);
            wilayahCode = intent.getStringExtra(EXTRA_WILAYAH_CODE);
            String url = Config.URL_NAMA_DAERAH;
            url += "?wilayah=" + wilayahMode.toLowerCase() +
                    "&code=" + wilayahCode;
            url += "&userid=" + user.userid;

            debug(getClass(), wilayahMode);

            titleBar = "Pilih " + wilayahMode;
            uiList = new ArrayList<>();
            if (Objects.equals(wilayahMode.toLowerCase(), "provinsi")) {
                uiList.add(new Region(codeProvinsi[0], codeProvinsi[1]));
                setContent();
            } else {
                retrieveProvinsiesFromServer(url);
            }
        }
    }

    private void setContent() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.post(() -> toolbar.setTitle(titleBar));
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        RecyclerView rv = (RecyclerView) findViewById(R.id.activity_select_region_RecyclerView);
        rv.setLayoutManager(new LinearLayoutManager(this));
        rv.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        adapter = new RegionListAdapter(uiList, this);
        rv.setAdapter(adapter);
    }


    private void retrieveProvinsiesFromServer(String url) {

        debug(getClass(), "Retrieve region from server. Url: " + url);
        final Dialog d = ProgressDialog.show(this, getString(R.string.please_wait), getString(R.string.loading_data), true, true);
        final VolleyStringRequest request = new VolleyStringRequest(url, new VolleyStringRequest.Callback() {

            @Override
            public void onResponse(JsonObject jsonObject) {
                d.dismiss();
                debug(getClass(), "Retrieve region onresponse");
                if (jsonObject.get("success").getAsBoolean()) {
                    setContentView(jsonObject);
                } else {
                    debug(getClass(), "error_message: " + jsonObject.get("message").getAsString());
                    if (isResummed) {

                        new AlertDialog.Builder(SelectRegionActivity.this)
                                .setMessage(R.string.an_error_ocurred)
                                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {

                                    @Override
                                    public void onClick(DialogInterface p1, int p2) {
                                        finish();
                                    }
                                })
                                .show();
                    }
                }

            }
        });
        d.setOnCancelListener(new DialogInterface.OnCancelListener() {

            @Override
            public void onCancel(DialogInterface p1) {
                request.cancel();
            }
        });
        request.setTag(VOLLEY_TAG);
        VolleySingleton.getInstance(this).getRequestQueue().add(request);
    }


    private void setContentView(JsonObject jsonResult) {
        JsonElement je = jsonResult.get(wilayahMode);
        if (je == null) {
            new AlertDialog.Builder(this)
                    .setMessage(R.string.an_error_ocurred)
                    .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface p1, int p2) {
                            setResult(Activity.RESULT_CANCELED);
                            finish();
                        }
                    })
                    .show();
            return;
        }
        uiList = new ArrayList<>();
        List<Region> allRegionList = new ArrayList<>();
        Gson gson = new Gson();
        JsonArray jsonArray = je.getAsJsonArray();
        for (JsonElement element : jsonArray) {
            allRegionList.add(gson.fromJson(element, Region.class));
        }

        uiList.addAll(allRegionList);
        adapter = new RegionListAdapter(uiList, this);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle(titleBar);
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        RecyclerView rv = (RecyclerView) findViewById(R.id.activity_select_region_RecyclerView);
        rv.setLayoutManager(new LinearLayoutManager(this));
        rv.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        rv.setAdapter(adapter);

    }

    @Override
    protected void onDestroy() {
        VolleySingleton.getInstance(this).getRequestQueue().cancelAll(VOLLEY_TAG);
        super.onDestroy();
    }


    @Override
    public void onProvinsiItemClick(int position) {
        Intent it = new Intent();
        it.putExtra(EXTRA_WILAYAH, uiList.get(position));
        setResult(RESULT_OK, it);
        finish();
    }

    @Override
    public void onBackPressed() {
        setResult(RESULT_CANCELED);
        super.onBackPressed();
    }


}
