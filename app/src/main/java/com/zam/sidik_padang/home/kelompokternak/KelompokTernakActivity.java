package com.zam.sidik_padang.home.kelompokternak;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
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
 * Created by supriyadi on 5/6/17.
 */

public class KelompokTernakActivity extends BaseLogedinActivity
        implements KelompokTernakListAdapter.OnDeleteButtonClickListener {

    private final String VOLLEY_TAG = getClass().getName();
    private KelompokTernakListAdapter adapter;
    private List<KelompokTernak> list;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        retrieveListFromServer();
    }

    private void retrieveListFromServer() {
        String url = Config.URL_ALL_NAMA_KELOMPOK_TERNAK;
        url += "?userid=" + user.userid;

        final Dialog d = ProgressDialog.show(this, getString(R.string.please_wait), getString(R.string.loading_data), true, true);
        final VolleyStringRequest request = new VolleyStringRequest(url, new VolleyStringRequest.Callback() {
            @Override
            public void onResponse(JsonObject jsonObject) {
                d.dismiss();
                debug(getClass(), "Retrieve kelompok ternak onResponse");
                if (jsonObject.get("success").getAsBoolean()) {
                    setContentView(jsonObject);
                } else {
                    if (isResummed)
                        new AlertDialog.Builder(KelompokTernakActivity.this).setMessage(jsonObject.get("message").getAsString())
                                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        finish();
                                    }
                                }).show();
                }
            }
        });

        request.setTag(VOLLEY_TAG);

        d.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                request.cancel();
                finish();
            }
        });

        VolleySingleton.getInstance(this).getRequestQueue().add(request);

    }


    private void setContentView(JsonObject jsonResult) {
        JsonElement je = jsonResult.get("kelompok_ternak");
        if (je == null) {
            debug(getClass(), "kelompok_ternak == null");
            new AlertDialog.Builder(KelompokTernakActivity.this).setMessage(getString(R.string.an_error_ocurred))
                    .setCancelable(false)
                    .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            finish();
                        }
                    }).show();
            return;
        }

        list = new ArrayList<>();
        JsonArray ja = je.getAsJsonArray();
        Gson gson = new Gson();
        for (JsonElement element : ja) {
            list.add(gson.fromJson(element, KelompokTernak.class));
        }

        setContentView(R.layout.activity_kelompok_ternak);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        adapter = new KelompokTernakListAdapter(list, this);
        RecyclerView rv = (RecyclerView) findViewById(R.id.activity_kelompok_ternak_RecyclerView);
        rv.setLayoutManager(new LinearLayoutManager(this));
        rv.setAdapter(adapter);
        findViewById(R.id.activity_kelompok_ternak_FAB).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(new Intent(KelompokTernakActivity.this, TambahKelompokTernakActivity.class), 123);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 123 && resultCode == RESULT_OK) {
            retrieveListFromServer();
        }
    }

    @Override
    protected void onDestroy() {
        VolleySingleton.getInstance(this).getRequestQueue().cancelAll(VOLLEY_TAG);
        super.onDestroy();
    }

    @Override
    public void onDeleteItemClick(final int position) {
        new AlertDialog.Builder(this)
                .setTitle(R.string.delete_)
                .setMessage(list.get(position).nama_kelompok)
                .setPositiveButton(R.string.delete, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        deleteKelompokTernak(position);
                    }
                })
                .setNegativeButton(android.R.string.cancel, null)
                .show();
    }

    private void deleteKelompokTernak(final int position) {
        if (!Util.isInternetAvailible(this)) {
            Toast.makeText(this, R.string.no_internet_connection, Toast.LENGTH_SHORT).show();
            return;
        }
        String url = Config.URL_KELOMPOK_TERNAK;
        url += "?userid=" + user.userid;
        url += "&aksi=2";
        KelompokTernak kelompok = list.get(position);
        url += "&id=" + kelompok.id_kelompok;
        debug(getClass(), "Delet kelompok ternak URL: " + url);
        final Dialog d = ProgressDialog.show(this, getString(R.string.please_wait), "Deleting " + kelompok.nama_kelompok + "...", true, false);
        VolleyStringRequest request = new VolleyStringRequest(url, new VolleyStringRequest.Callback() {

            @Override
            public void onResponse(JsonObject jsonObject) {
                d.dismiss();
                debug(getClass(), "Delete kelompok ternak, Onresponse: " + jsonObject);
                Toast.makeText(KelompokTernakActivity.this, jsonObject.get("message").getAsString(), Toast.LENGTH_SHORT).show();
                if (jsonObject.get("success").getAsBoolean()) {
                    list.remove(position);
                    adapter.notifyDataSetChanged();
                }
            }
        });

        request.setTag(VOLLEY_TAG);
        VolleySingleton.getInstance(this).addToRequestQueue(request);
    }

}
