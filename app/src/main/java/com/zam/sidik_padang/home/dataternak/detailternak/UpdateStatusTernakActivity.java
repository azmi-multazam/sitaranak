package com.zam.sidik_padang.home.dataternak.detailternak;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.Toolbar;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import com.zam.sidik_padang.BaseLogedinActivity;
import com.zam.sidik_padang.R;
import com.zam.sidik_padang.util.Config;
import com.zam.sidik_padang.util.HttpUrlConnectionApi;
import com.zam.sidik_padang.util.Util;

public class UpdateStatusTernakActivity extends BaseLogedinActivity {

    public static final String EXTRA_KONDISI = "extra_kondisi";
    public static final String GSON_KONDISI = "gson_kondisi";
    public static final String ACTION_STATUS_TERNAK_BERUBAH = "action_status_ternak_berubah";
    private static final String VOLLEY_TAG = UpdateStatusTernakActivity.class.getName();
    private Kondisi kondisi;
    private String idTernak;
    private TextView textStatus, textKeterangan;

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        if (kondisi != null) {
            outState.putParcelable(EXTRA_KONDISI, kondisi);
            outState.putString(DetailTernakActivity.ID_TERNAK, idTernak);
        }
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setResult(RESULT_CANCELED);
        Intent it = getIntent();
        if (it.hasExtra(GSON_KONDISI)) {
            kondisi = new Gson().fromJson(it.getStringExtra(GSON_KONDISI), Kondisi.class);
            idTernak = it.getStringExtra(DetailTernakActivity.ID_TERNAK);
        } else if (it.hasExtra(EXTRA_KONDISI)) {
            kondisi = it.getParcelableExtra(EXTRA_KONDISI);
            idTernak = it.getStringExtra(DetailTernakActivity.ID_TERNAK);
        } else if (savedInstanceState != null &&
                (savedInstanceState.containsKey(EXTRA_KONDISI) || savedInstanceState.containsKey(GSON_KONDISI))) {
            kondisi = savedInstanceState.getParcelable(EXTRA_KONDISI);
            idTernak = savedInstanceState.getString(DetailTernakActivity.ID_TERNAK);
        } else {
            new AlertDialog.Builder(this).setMessage("Terjadikesalahan")
                    .setPositiveButton("Tutup", (dialog, which) -> finish())
                    .show();
            return;
        }

        setContentView(R.layout.activity_update_status_ternak);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        textStatus = (TextView) findViewById(R.id.activity_update_status_ternak_TextView);
        textKeterangan = (TextView) findViewById(R.id.activity_update_status_ternak_EditText);
        textStatus.setText(String.format(getString(R.string.update_status_ternak_menjadi), idTernak, kondisi.nama_kondisi));
        textStatus.setTextColor(Color.parseColor(kondisi.warna));
        findViewById(R.id.activity_update_status_ternak_Button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                update();
            }
        });
    }

    private void update() {
        if (!Util.isInternetAvailible(this)) {
            Toast.makeText(this, "Tidak ada koneksi internet", Toast.LENGTH_SHORT).show();
            return;
        }

        String url = Config.URL_LIHAT_DATA_TERNAK + "?aksi=13&userid=" + user.userid;
        url += "&id_ternak=" + idTernak;
        url += "&kondisi_ternak=" + kondisi.id;
        url += "&keterangan=" + Util.toUrlEncoded(textKeterangan.getText().toString().trim());
        debug(getClass(), "Mengubah status url=" + url);
        final ProgressDialog d = ProgressDialog.show(this, null, "Mengirim data...", true, false);
        HttpUrlConnectionApi.get(url, new HttpUrlConnectionApi.Callback() {
            @Override
            public void onResponse(JsonObject jsonObject) {
                debug(getClass(), "Mengubah status response=" + jsonObject);
                d.dismiss();
                AlertDialog.Builder b = new AlertDialog.Builder(UpdateStatusTernakActivity.this);
                b.setMessage(jsonObject.get("message").getAsString());
                if (jsonObject.get("success").getAsBoolean())
                    b.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            setResult(RESULT_OK);
                            LocalBroadcastManager.getInstance(UpdateStatusTernakActivity.this)
                                    .sendBroadcast(new Intent(ACTION_STATUS_TERNAK_BERUBAH));
                            finish();
                        }
                    });
                b.setCancelable(false);
                if (isResummed) b.show();
            }
        });
    }
}
