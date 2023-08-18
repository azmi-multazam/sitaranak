package com.zam.sidik_padang.login;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.gson.JsonObject;

import com.zam.sidik_padang.BaseActivity;
import com.zam.sidik_padang.R;
import com.zam.sidik_padang.util.Config;
import com.zam.sidik_padang.util.Util;
import com.zam.sidik_padang.util.VolleySingleton;
import com.zam.sidik_padang.util.VolleyStringRequest;


public class RegisterActivity extends BaseActivity {
    private static final String VOLLEY_TAG = RegisterActivity.class.getName();
    private EditText editText;
    private View progressbar, buttonDaftar;
    private SharedPreferences pref;

    @Override
    protected void onResume() {
        super.onResume();
        long waktuTersimpan = pref.getLong(Config.PREF_WAKTU_DAFTAR, 0);
        if (System.currentTimeMillis() - waktuTersimpan < 1000 * 60 * 10) {
            startActivity(new Intent(this, KonfirmasiPendaftaranActivity.class));
            finish();
        } else {
            SharedPreferences.Editor editor = pref.edit();
            editor.remove(Config.PREF_NOMOR_HP);
            editor.remove(Config.PREF_NOMOR_CONFIRMED);
            editor.apply();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO: Implement this method
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        editText = (EditText) findViewById(R.id.activity_register_TextInputEditText);
        progressbar = findViewById(R.id.activity_register_ProgressBar);
        buttonDaftar = findViewById(R.id.activity_register_Button);
        buttonDaftar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validateForm();
            }
        });
        pref = PreferenceManager.getDefaultSharedPreferences(this);
    }

    private void validateForm() {
        final String hp = editText.getText().toString().trim();
        if (hp.isEmpty()) {
            editText.setError("Harus diisi");
            editText.requestFocus();
            return;
        }

        if (hp.length() < 10) {
            editText.setError("Nomor HP tidak valid");
            editText.requestFocus();
            return;
        }

        if (!Util.isInternetAvailible(this)) {
            Toast.makeText(this, R.string.no_internet_connection, Toast.LENGTH_SHORT).show();
            return;
        }

        String url = Config.URL_DAFTAR + "?aksi=1&hp=" + hp;
        progressbar.setVisibility(View.VISIBLE);
        buttonDaftar.setVisibility(View.INVISIBLE);
        VolleyStringRequest requet = new VolleyStringRequest(url, new VolleyStringRequest.Callback() {
            @Override
            public void onResponse(JsonObject jsonObject) {
                progressbar.setVisibility(View.INVISIBLE);
                buttonDaftar.setVisibility(View.VISIBLE);

                if (jsonObject.get("success").getAsBoolean()) {
                    SharedPreferences.Editor editor = pref.edit();
                    editor.putLong(Config.PREF_WAKTU_DAFTAR, System.currentTimeMillis());
                    editor.putString(Config.PREF_NOMOR_HP, hp);
                    editor.apply();
                    startActivity(new Intent(RegisterActivity.this, KonfirmasiPendaftaranActivity.class));
                } else {
                    Util.showDialog(RegisterActivity.this, jsonObject.get("message").getAsString());
                }
            }
        });

        requet.setTag(VOLLEY_TAG);
        VolleySingleton.getInstance(this).getRequestQueue().add(requet);
    }

    @Override
    protected void onDestroy() {
        VolleySingleton.getInstance(this).getRequestQueue().cancelAll(VOLLEY_TAG);
        super.onDestroy();
    }
}