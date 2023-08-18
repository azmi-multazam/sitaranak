package com.zam.sidik_padang.home.koordinator;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
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
 * Created by supriyadi on 5/6/17.
 */

public class KoordinatorActivity extends BaseLogedinActivity
        implements KoordinatorListAdapter.OnKoordinatorDeleteButtonClickListener {
    private final String VOLLEY_TAG = getClass().getName();
    private KoordinatorListAdapter adapter;
    private List<Koordinator> list;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        retrieveListFromServer();
    }

    private void retrieveListFromServer() {
        String url = Config.URL_KOORDINATOR;
        url += "?userid=" + user.userid;
        url += "&aksi=3";
        final Dialog d = ProgressDialog.show(this, getString(R.string.please_wait), getString(R.string.loading_data), true, true);
        final VolleyStringRequest request = new VolleyStringRequest(url, new VolleyStringRequest.Callback() {
            @Override
            public void onResponse(JsonObject jsonObject) {
                d.dismiss();
                debug(getClass(), "getKoordinator onResponse " + jsonObject.get("message").getAsString());
                if (jsonObject.get("success").getAsBoolean()) {
                    setContentView(jsonObject);
                } else {
                    if (isResummed)
                        new AlertDialog.Builder(KoordinatorActivity.this).setMessage(jsonObject.get("message").getAsString())
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
        JsonElement je = jsonResult.get("kordinator_ternak");
        if (je == null) {
            debug(getClass(), " kordinator_ternak== null");
            new AlertDialog.Builder(KoordinatorActivity.this).setMessage(getString(R.string.an_error_ocurred))
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
            list.add(gson.fromJson(element, Koordinator.class));
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

        adapter = new KoordinatorListAdapter(list, this);
        RecyclerView rv = (RecyclerView) findViewById(R.id.activity_kelompok_ternak_RecyclerView);
        rv.setLayoutManager(new LinearLayoutManager(this));
        rv.setAdapter(adapter);
        findViewById(R.id.activity_kelompok_ternak_FAB).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //startActivityForResult(new Intent(KelompokTernakActivity.this, TambahKelompokTernakActivity.class), 123);
            }
        });
    }


    @Override
    public void onKoordinatorDeleteButtonClick(final int position) {
        new androidx.appcompat.app.AlertDialog.Builder(this)
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

        String url = Config.URL_KOORDINATOR;
        url += "?userid=" + user.userid;
        url += "&aksi=2";
        Koordinator koordinator = list.get(position);
        url += "&id=" + koordinator.id;

        debug(getClass(), "Delete koordinator: " + url);
        final Dialog d = ProgressDialog.show(this, getString(R.string.please_wait), "Deleting Koordinator " + koordinator.nama + "...", true, false);
        VolleyStringRequest request = new VolleyStringRequest(url, new VolleyStringRequest.Callback() {

            @Override
            public void onResponse(JsonObject jsonObject) {
                d.dismiss();
                debug(getClass(), "Delete koordinator, Onresponse: " + jsonObject);
                Toast.makeText(KoordinatorActivity.this, jsonObject.get("message").getAsString(), Toast.LENGTH_SHORT).show();
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

/*
http://e-rekording.com/android_api/kordinator.php?userid=KS1000002&aksi=3
{"kordinator_ternak":[
                      {"id":"7",
					   "iduser":"KS1000007",
					   "nama":"Hadi Purnomo",
					   "nama_kelompok":null,
					   "alamat_kelompok":null,
					   "Region":"JAWA TIMUR"
					   },
					   {"id":"8",
					    "iduser":"KS1000008",
					    "nama":"Yusuf Patra",
						"nama_kelompok":"KSI JABAR",
						"alamat_kelompok":"JABAR",
						"Region":"JAWA TIMUR"}
						,
						{"id":"9","iduser":"KS1000009","nama":"Chandra","nama_kelompok":null,"alamat_kelompok":null,"Region":"JAWA TIMUR"},{"id":"10","iduser":"KS1000010","nama":"Hari Suyatno","nama_kelompok":"KSI PUSAT","alamat_kelompok":"DKI","Region":"JAWA TIMUR"},{"id":"11","iduser":"KS1000011","nama":"Didik Dion Uje","nama_kelompok":null,"alamat_kelompok":null,"Region":"JAWA TIMUR"},{"id":"12","iduser":"KS1000012","nama":"Imam Sadzali","nama_kelompok":null,"alamat_kelompok":null,"Region":"JAWA TIMUR"},{"id":"13","iduser":"KS1000013","nama":"Sujatno","nama_kelompok":null,"alamat_kelompok":null,"Region":"JAWA TIMUR"},{"id":"14","iduser":"KS1000014","nama":"Mustika ","nama_kelompok":null,"alamat_kelompok":null,"Region":"JAWA TIMUR"},{"id":"15","iduser":"KS1000015","nama":"Andre","nama_kelompok":null,"alamat_kelompok":null,"Region":"JAWA TIMUR"},{"id":"16","iduser":"KS1000016","nama":"Dio Nurdin","nama_kelompok":null,"alamat_kelompok":null,"Region":"JAWA TIMUR"},{"id":"17","iduser":"KS1000017","nama":"Ach. Hofit","nama_kelompok":null,"alamat_kelompok":null,"Region":"JAWA TIMUR"},{"id":"18","iduser":"KS1000018","nama":"Feri Hertanto ","nama_kelompok":null,"alamat_kelompok":null,"Region":"JAWA TIMUR"},{"id":"19","iduser":"KS1000019","nama":"Heriyanto","nama_kelompok":null,"alamat_kelompok":null,"Region":"JAWA TIMUR"},{"id":"20","iduser":"KS1000020","nama":"Farid Ibrahim","nama_kelompok":null,"alamat_kelompok":null,"Region":"JAWA TIMUR"},{"id":"21","iduser":"KS1000021","nama":"Ruri","nama_kelompok":null,"alamat_kelompok":null,"Region":"JAWA TIMUR"},{"id":"22","iduser":"KS1000022","nama":"Samsul","nama_kelompok":null,"alamat_kelompok":null,"Region":"JAWA TIMUR"},{"id":"23","iduser":"KS1000023","nama":"Trisula","nama_kelompok":null,"alamat_kelompok":null,"Region":"JAWA TIMUR"},{"id":"24","iduser":"KS1000024","nama":"ISMAIL","nama_kelompok":null,"alamat_kelompok":null,"Region":"JAWA TIMUR"}],"success":true,"message":" berhasil"}
*/
