package com.zam.sidik_padang.home.datapeternak;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.List;

import com.zam.sidik_padang.R;
import com.zam.sidik_padang.home.basesearchdata.BaseSearchDataActivity;
import com.zam.sidik_padang.home.koordinator.KoordinatorListAdapter;
import com.zam.sidik_padang.home.tambahpeternak.TambahPeternakActivity;
import com.zam.sidik_padang.util.Config;
import com.zam.sidik_padang.util.Util;
import com.zam.sidik_padang.util.VolleySingleton;
import com.zam.sidik_padang.util.VolleyStringRequest;

/**
 * Created by supriyadi on 5/6/17.
 */

public class DataPeternakActivity extends BaseSearchDataActivity
        implements KoordinatorListAdapter.OnKoordinatorDeleteButtonClickListener {

    private final String VOLLEY_TAG = getClass().getName();
    private List<Peternak> list;
    private PeternakListAdapter adapter;
    private String searchUrl;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setTitle(R.string.data_peternak);
        list = new ArrayList<>();
        adapter = new PeternakListAdapter(list, this);
        rv.setAdapter(adapter);
        if (searchUrl == null)
            searchUrl = Config.URL_LIHAT_DATA_PETERNAK + "?userid=" + user.userid + "&aksi=3";
        searchData(searchUrl + "&id=" + user.userid, false);
    }

    @Override
    protected void onUserIdEntered(String userId, boolean fromScanner) {
        searchData(searchUrl + "&id=" + userId, false);
    }

    @Override
    protected void onRegionKodEntered(String regionKode) {
        searchData(searchUrl + "&kode=" + regionKode, false);
        list.clear();
        adapter.notifyDataSetChanged();
    }

    @Override
    protected void onResponseSuccess(JsonObject jsonObject, boolean fromScanner) {
        JsonElement je = jsonObject.get("data_peternak");
        if (je == null) {
            debug(getClass(), " data_peternak== null");
            if (isResummed)
                new AlertDialog.Builder(DataPeternakActivity.this).setMessage(getString(R.string.an_error_ocurred))
                        .setCancelable(false)
                        .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                finish();
                            }
                        }).show();
            return;
        }

        JsonArray ja = je.getAsJsonArray();
        Gson gson = new Gson();
        list.clear();
        for (JsonElement element : ja) {
            list.add(gson.fromJson(element, Peternak.class));
        }
        adapter.notifyDataSetChanged();
        if (list.size() == 0)
            Toast.makeText(this, R.string.no_data_found, Toast.LENGTH_SHORT).show();

    }

    @Override
    public void onKoordinatorDeleteButtonClick(final int position) {
        new androidx.appcompat.app.AlertDialog.Builder(this)
                .setTitle(R.string.delete_)
                .setMessage(list.get(position).nama_kelompok)
                .setPositiveButton(R.string.delete, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        deleteDataPeternak(position);
                    }
                })
                .setNegativeButton(android.R.string.cancel, null)
                .show();
    }

    private void deleteDataPeternak(final int position) {
        if (!Util.isInternetAvailible(this)) {
            Toast.makeText(this, R.string.no_internet_connection, Toast.LENGTH_SHORT).show();
            return;
        }

        String url = Config.URL_LIHAT_DATA_PETERNAK;
        url += "?userid=" + user.userid;
        url += "&aksi=2";
        Peternak p = list.get(position);
        url += "&id=" + p.id;

        debug(getClass(), "Delete Peternak: " + url);
        final Dialog d = ProgressDialog.show(this, getString(R.string.please_wait), "Deleting Data peternak " + p.nama + "...", true, false);
        VolleyStringRequest request = new VolleyStringRequest(url, new VolleyStringRequest.Callback() {

            @Override
            public void onResponse(JsonObject jsonObject) {
                d.dismiss();
                debug(getClass(), "Delete Data peternak, Onresponse: " + jsonObject);
                Toast.makeText(DataPeternakActivity.this, jsonObject.get("message").getAsString(), Toast.LENGTH_SHORT).show();
                if (jsonObject.get("success").getAsBoolean()) {
                    list.remove(position);
                    adapter.notifyDataSetChanged();
                }
            }
        });

        request.setTag(VOLLEY_TAG);
        VolleySingleton.getInstance(this).addToRequestQueue(request);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.activity_base_search_data_FAB) {
            Intent it = new Intent(this, TambahPeternakActivity.class);
            startActivityForResult(it, 2);
        } else super.onClick(v);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        debug(getClass(), "onActivity result data: " + data);
        if (requestCode == 2 && resultCode == RESULT_OK && data.hasExtra("extra_userid")) {
            onUserIdEntered(data.getStringExtra("extra_userid"), false);
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}
