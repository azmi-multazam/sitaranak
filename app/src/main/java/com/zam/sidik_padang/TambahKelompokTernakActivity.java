package com.zam.sidik_padang;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;

import com.google.gson.JsonObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import com.zam.sidik_padang.home.selectregion.Region;
import com.zam.sidik_padang.util.Config;
import com.zam.sidik_padang.util.Util;
import com.zam.sidik_padang.util.VolleySingleton;
import com.zam.sidik_padang.util.VolleyStringRequest;

/**
 * Created by supriyadi on 5/7/17.
 */


public class TambahKelompokTernakActivity extends BaseLogedinActivity
        implements View.OnClickListener {

    private final String VOLLEY_TAG = getClass().getName();
    private EditText editTextNamaKelompok, editTextAlamatKelompok, editTextAlamatKorwil;
    private Region selectedRegion;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tambah_kelompok_ternak);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        editTextNamaKelompok = (EditText) findViewById(R.id.activity_tambah_kelompok_ternak_EditTextNamaKelompok);
        editTextAlamatKelompok = (EditText) findViewById(R.id.activity_tambah_kelompok_ternak_EditTextAlamat);
        editTextAlamatKorwil = (EditText) findViewById(R.id.activity_tambah_kelompok_ternak_EditTextAlamatKorwiil);
        editTextAlamatKelompok.setOnClickListener(this);
        findViewById(R.id.activity_tambah_kelompok_ternak_ButtonAdd).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.activity_tambah_kelompok_ternak_ButtonAdd) {
            try {
                tambah();
            } catch (UnsupportedEncodingException e) {
                debug(getClass(), e.getMessage());
                Toast.makeText(this, R.string.an_error_ocurred, Toast.LENGTH_SHORT).show();
            }
        } else if (id == R.id.activity_tambah_kelompok_ternak_EditTextAlamat) {
            Intent intent = new Intent(this, SelectRegionActivity.class);
            intent.putExtra(SelectRegionActivity.EXTRA_WILAYAH_MODE, "provinsi");
            intent.putExtra(SelectRegionActivity.EXTRA_WILAYAH_CODE, "0");
            startActivityForResult(intent, 2);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 2 && resultCode == RESULT_OK) {
            selectedRegion = (Region) data.getSerializableExtra(SelectRegionActivity.EXTRA_WILAYAH);
            editTextAlamatKelompok.setText(selectedRegion.nama);
        }
    }


    private void tambah() throws UnsupportedEncodingException {
        if (!Util.isInternetAvailible(this)) {
            Util.noInternetDialog(this);
            return;
        }
        String nama = editTextNamaKelompok.getText().toString().trim();
        if (nama.isEmpty()) {
            editTextNamaKelompok.requestFocus();
            editTextNamaKelompok.setError(getString(R.string.this_field_is_mandatory));
            return;
        } else {
            nama = URLEncoder.encode(nama, "utf-8");
        }
        if (selectedRegion == null || editTextAlamatKelompok.getText().toString().trim().isEmpty()) {
            Toast.makeText(this, R.string.please_select_provinsi, Toast.LENGTH_SHORT).show();
            return;
        }

        String alamat = editTextAlamatKorwil.getText().toString().trim();
        if (alamat.isEmpty()) {
            editTextAlamatKorwil.requestFocus();
            editTextAlamatKorwil.setError(getString(R.string.this_field_is_mandatory));
            return;
        } else {
            alamat = URLEncoder.encode(alamat, "utf-8");
        }
		
		/*
		 http://e-rekording.com/android_api/add_kelompok_ternak.php?userid=KS1000002&aksi=1&nama=A&alamat=Palembang&provinsi=SUMSEL
		*/
        String url = Config.URL_KELOMPOK_TERNAK;
        url += "?userid=" + user.userid;
        url += "&aksi=1";
        url += "&nama=" + nama;
        url += "&provinsi=" + URLEncoder.encode(selectedRegion.nama, "utf-8");
        url += "&alamat=" + alamat;

        debug(getClass(), "tambah kelompok ternak url: " + url);
        final Dialog d = ProgressDialog.show(this, getString(R.string.please_wait), getString(R.string.sending_data), true, false);
        final VolleyStringRequest request = new VolleyStringRequest(url, new VolleyStringRequest.Callback() {
            @Override
            public void onResponse(JsonObject jsonObject) {
                d.dismiss();
                String message = jsonObject.get("message").getAsString();
                debug(getClass(), "T ambah kelompok peternak onResponse. " + message);
                if (jsonObject.get("success").getAsBoolean()) {
                    setResult(RESULT_OK);
                    Toast.makeText(TambahKelompokTernakActivity.this, message, Toast.LENGTH_SHORT).show();
                    finish();
                } else if (isResummed) {
                    Util.showDialog(TambahKelompokTernakActivity.this, message);
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
