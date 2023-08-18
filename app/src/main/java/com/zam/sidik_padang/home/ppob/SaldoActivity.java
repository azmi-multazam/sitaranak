package com.zam.sidik_padang.home.ppob;

import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.widget.Toolbar;

import com.google.gson.JsonObject;
import com.google.zxing.BarcodeFormat;

import java.text.NumberFormat;

import com.zam.sidik_padang.BaseLogedinActivity;
import com.zam.sidik_padang.R;
import com.zam.sidik_padang.util.Config;
import com.zam.sidik_padang.util.Util;
import com.zam.sidik_padang.util.VolleySingleton;
import com.zam.sidik_padang.util.VolleyStringRequest;

public class SaldoActivity extends BaseLogedinActivity {

    private final String VOLLEY_TAG = getClass().getName();
    private TextView textViewSisaSaldo;
    private ImageView ivBarcode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_saldo);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("");
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        ivBarcode = (ImageView) findViewById(R.id.activity_ppob_ImageViewBarcode);
        ((TextView) findViewById(R.id.activity_ppob_TextViewNAmaMember)).setText(user.nama);
        textViewSisaSaldo = (TextView) findViewById(R.id.activity_ppob_TextViewSaldoMember);
    }

    @Override
    protected void onStart() {
        super.onStart();
        ivBarcode.postDelayed(() -> new LoadBarcode(ivBarcode), 500);
        if (user != null) loadSisaSaldo();
    }

    private void loadSisaSaldo() {
        if (!Util.isInternetAvailible(this)) return;
        String url = Config.URL_SALDO + "aksi=1&userid=" + user.userid;
        debug(getClass(), "Load saldo url=" + url);
        VolleyStringRequest request = new VolleyStringRequest(url, new VolleyStringRequest.Callback() {

            @Override
            public void onResponse(JsonObject jsonObject) {
                debug(getClass(), "Load saldo response=" + jsonObject);
                if (jsonObject.get("success").getAsBoolean()) {
                    textViewSisaSaldo.setText("Rp. " + NumberFormat.getInstance().format((jsonObject.get("saldo").getAsJsonArray().get(0).getAsJsonObject().get("saldo").getAsDouble())));
                }
					/*
					 {"saldo":[{"userid":"100002","saldo":69385}],
					 "success":true,"message":"Sukses"}
					*/

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

    private class LoadBarcode extends AsyncTask<Void, Void, Bitmap> {

        private ImageView imageView;
        private int viewWidth, viewHeight;

        LoadBarcode(ImageView imageView) {
            this.imageView = imageView;
            execute();
        }

        @Override
        protected void onPreExecute() {
            viewWidth = imageView.getWidth();
            viewHeight = imageView.getHeight();
        }

        @Override
        protected Bitmap doInBackground(Void... params) {
            try {
                return Util.encodeAsBitmap(user.userid, BarcodeFormat.QR_CODE, viewWidth, viewHeight);
            } catch (Exception e) {
                Log.e(getClass().getName(), e.getMessage());
                return null;
            }
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            if (bitmap != null) imageView.setImageBitmap(bitmap);
        }
    }

}