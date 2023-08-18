package com.zam.sidik_padang.home.ppob.history.pulsa;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.text.NumberFormat;

import com.zam.sidik_padang.BaseLogedinActivity;
import com.zam.sidik_padang.R;
import com.zam.sidik_padang.util.Config;
import com.zam.sidik_padang.util.Util;
import com.zam.sidik_padang.util.VolleySingleton;
import com.zam.sidik_padang.util.VolleyStringRequest;

public class DetailTrxPulsaActivity extends BaseLogedinActivity {
    private final String VOLLEY_TAG = this.getClass().getName();

    String historyId = "";

    private View progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO: Implement this method
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_trx_pulsa);
        Toolbar toolbar = findViewById(R.id.activity_detail_trx_pulsa_Toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        toolbar.setNavigationOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View p1) {
                finish();
            }
        });
        Intent it = getIntent();
        if (it.hasExtra("history_id")) historyId = it.getStringExtra("history_id");
        progressBar = findViewById(R.id.activity_detail_trx_pulsa_ProgressBar);
    }

    @Override
    protected void onStart() {
        // TODO: Implement this method
        super.onStart();
        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                loadFromServer();
            }
        }, 200);
    }


    private void loadFromServer() {
        if (!Util.isInternetAvailible(this) || historyId.isEmpty()) return;
        String url = Config.URL_HISTORY + "aksi=2&userid=" + user.userid + "&id=" + historyId;
        debug(getClass(), "load trx pulsa detail. Url=" + url);
        progressBar.setVisibility(View.VISIBLE);
        VolleyStringRequest request = new VolleyStringRequest(url, new VolleyStringRequest.Callback() {

            @Override
            public void onResponse(JsonObject jsonObject) {
                progressBar.setVisibility(View.INVISIBLE);
                debug(getClass(), "Load detail trx pulsa response=" + jsonObject);
                if (jsonObject.get("success").getAsBoolean()) {
                    JsonArray ja = jsonObject.get("detail_pulsa").getAsJsonArray();
                    if (ja.size() < 1) return;
                    JsonObject jo = ja.get(0).getAsJsonObject();

                    ((TextView) findViewById(R.id.activity_detail_trx_pulsa_TextViewBerita)).setText(": " + jo.get("berita").getAsString());
                    ((TextView) findViewById(R.id.activity_detail_trx_pulsa_TextViewHarga)).setText(": Rp. " + NumberFormat.getInstance().format(jo.get("harga").getAsLong()));
                    ((TextView) findViewById(R.id.activity_detail_trx_pulsa_TextViewTanggal)).setText(": " + jo.get("tanggal").getAsString());
                    ((TextView) findViewById(R.id.activity_detail_trx_pulsa_TextViewStatus)).setText(": " + jo.get("status").getAsString());
                    ((TextView) findViewById(R.id.activity_detail_trx_pulsa_TextViewProduk)).setText(": " + jo.get("produk").getAsString());

                } else {
                    Toast.makeText(DetailTrxPulsaActivity.this, jsonObject.get("message").getAsString(), Toast.LENGTH_SHORT).show();
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




/*
	 http://bungara.com/api/history.php?userid=100003&aksi=2&id=689

{"detail_pulsa":[{"id":"688",
				  "tanggal":"2017-07-11 10:19:39",
				  "produk":"S10 081396641249 Trx Via Android",
				  "berita":"0041001554170419",
				  "harga":"10895",
				  "status":"Sukses"}],
				  
				  "success":true,"message":"berhasil"}
*/
