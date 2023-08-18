package com.zam.sidik_padang.home.dataternak.dibawahnya;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.List;

import com.zam.sidik_padang.R;
import com.zam.sidik_padang.home.basesearchdata.BaseSearchDataActivity;
import com.zam.sidik_padang.home.dataternak.detailternak.DetailTernakActivity;
import com.zam.sidik_padang.home.dataternak.detailternak.UpdateStatusTernakActivity;
import com.zam.sidik_padang.home.dataternak.rangking.RangkingActivity;
import com.zam.sidik_padang.util.Config;
import com.zam.sidik_padang.util.Util;
import com.zam.sidik_padang.util.VolleySingleton;
import com.zam.sidik_padang.util.VolleyStringRequest;

/**
 * Created by supriyadi on 5/6/17.
 */

public class DataTernakDibawahnyaActivity extends BaseSearchDataActivity
        implements DataTernakListAdapter.OnTernakItemClickListener {

    private final String VOLLEY_TAG = getClass().getName();


    protected String searchUrl;
    private final BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            debug(getClass(), "Onreceive action=" + intent.getAction());
            if (intent.getAction().equals(UpdateStatusTernakActivity.ACTION_STATUS_TERNAK_BERUBAH))
                searchData(searchUrl, false);
        }
    };
    private List<DataTernak> list;
    private DataTernakListAdapter adapter;
    private boolean receiverTerdaftar = false;
    private LocalBroadcastManager broadcastManager;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setTitle(R.string.data_ternak_dibawahnya);
        searchUrl = Config.URL_LIHAT_DATA_TERNAK + "?userid=" + user.userid;
        list = new ArrayList<>();
        adapter = new DataTernakListAdapter(list, this);
        rv.setAdapter(adapter);
        if (!receiverTerdaftar) {
            receiverTerdaftar = true;
            IntentFilter intent = new IntentFilter();
            intent.addAction(UpdateStatusTernakActivity.ACTION_STATUS_TERNAK_BERUBAH);
            broadcastManager = LocalBroadcastManager.getInstance(this);
            broadcastManager.registerReceiver(receiver, intent);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_rangking, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.menu_ranging) {
            startActivity(new Intent(this, RangkingActivity.class));
        }
        return true;
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.activity_base_search_data_FAB) {
            startActivityForResult(new Intent(this, TambahDataTernakActivity.class), 3);
        } else
            super.onClick(v);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            searchData(searchUrl, false);
        } else
            super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void onResponseSuccess(JsonObject jsonObject, boolean fromScanner) {
        //Toast.makeText(this, jsonObject.toString(), Toast.LENGTH_SHORT).show();
        //if(isResummed)	new AlertDialog.Builder(this).setMessage(jsonObject.toString()).show();
        debug(getClass(), "onResponseSuccess: " + jsonObject.toString());
        if (jsonObject.has("data_ternak")) {
            JsonElement jsonElement = jsonObject.get("data_ternak");
            if (jsonElement == null) {
                Toast.makeText(this, R.string.an_error_ocurred, Toast.LENGTH_SHORT).show();
                return;
            }

            JsonArray ja = jsonElement.getAsJsonArray();
            list.clear();
            DataTernak dataTernak;
            Gson gson = new Gson();
            for (JsonElement j : ja) {
                dataTernak = gson.fromJson(j, DataTernak.class);
                list.add(dataTernak);
            }
        }

        adapter.notifyDataSetChanged();
        if (fromScanner && list.size() == 1) {
            Intent intent = new Intent(this, DetailTernakActivity.class);
            intent.putExtra(DetailTernakActivity.ID_TERNAK, list.get(0).id_ternak);
            intent.putExtra("eartag", "-");
            startActivity(intent);
        }

        if (jsonObject.has("total_sapi")) {
            JsonElement totalElement = jsonObject.get("total_sapi");
            if (totalElement != null) {
                TextView textTotal = (TextView) findViewById(R.id.activity_base_search_data_TextViewTotal);
                textTotal.setText("Total " + totalElement.getAsString());
                textTotal.setVisibility(View.VISIBLE);
            }
        }
    }

    @Override
    protected void onUserIdEntered(String userId, boolean fromScanner) {
        searchUrl = Config.URL_LIHAT_DATA_TERNAK + "?userid=" + user.userid + "&aksi=3&id=" + userId;
        searchData(searchUrl, fromScanner);
    }

    @Override
    protected void onRegionKodEntered(String regionKode) {
        searchUrl = Config.URL_LIHAT_DATA_TERNAK + "?userid=" + user.userid + "&aksi=3&kode=" + regionKode;
        searchData(searchUrl, false);

    }


    @Override
    public void OnDataTernakItemClick(final int position) {
        Intent intent = new Intent(this, DetailTernakActivity.class);
        intent.putExtra(DetailTernakActivity.ID_TERNAK, list.get(position).id_ternak);
        startActivity(intent);
    }

    @Override
    public void onItemDeleteButtonClickListener(final int listPosition) {
        new androidx.appcompat.app.AlertDialog.Builder(this)
                .setTitle(R.string.delete_)
                .setMessage(getString(R.string.data_ternak_, list.get(listPosition).nama) + " - " + list.get(listPosition).id_ternak)
                .setPositiveButton(R.string.delete, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        deleteDataternak(listPosition);
                    }
                })
                .setNegativeButton(android.R.string.cancel, null)
                .show();
    }


    private void deleteDataternak(final int listPosition) {
        if (!Util.isInternetAvailible(this)) {
            Util.noInternetDialog(this);
            return;
        }
        String url = Config.URL_LIHAT_DATA_TERNAK + "?userid=" + user.userid + "&aksi=2&id=" + list.get(listPosition).id;
        debug(getClass(), "Delete data ternak url: " + url);
        final Dialog dialog = ProgressDialog.show(this, null, getString(R.string.please_wait), true, false);
        VolleyStringRequest request = new VolleyStringRequest(url, new VolleyStringRequest.Callback() {
            @Override
            public void onResponse(JsonObject jsonObject) {
                dialog.dismiss();
                debug(getClass(), "Delete Data ternak, Onresponse: " + jsonObject);
                Toast.makeText(DataTernakDibawahnyaActivity.this, jsonObject.get("message").getAsString(), Toast.LENGTH_SHORT).show();
                if (jsonObject.get("success").getAsBoolean()) {
                    list.remove(listPosition);
                    adapter.notifyDataSetChanged();
                }
            }
        });

        request.setTag(VOLLEY_TAG);
        VolleySingleton.getInstance(this).getRequestQueue().add(request);

    }

    @Override
    protected void onDestroy() {
        VolleySingleton.getInstance(this).getRequestQueue().cancelAll(VOLLEY_TAG);
        if (receiverTerdaftar) {
            receiverTerdaftar = false;
            broadcastManager.unregisterReceiver(receiver);
        }
        super.onDestroy();
    }
}
