package com.zam.sidik_padang.login;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.google.gson.JsonObject;

import com.zam.sidik_padang.BaseActivity;
import com.zam.sidik_padang.R;
import com.zam.sidik_padang.util.Config;
import com.zam.sidik_padang.util.Util;
import com.zam.sidik_padang.util.VolleySingleton;
import com.zam.sidik_padang.util.VolleyStringRequest;


/**
 * Created by supriyadi on 9/4/17.
 */

public class KonfirmasiPendaftaranActivity extends BaseActivity
        implements View.OnClickListener {
    private static final String VOLLEY_TAG = KonfirmasiPendaftaranActivity.class.getName();
    private View buttonLanjutkan, progressbar, buttonKirimUlangKode;
    private EditText editText;
    private TextView textViewTimer;
    private SharedPreferences pref;
    private long waktuPengirimanKode = 0;

    private final Runnable runnableTimer = new Runnable() {
        @Override
        public void run() {
            long time = waktuPengirimanKode + 1000 * 60 * 10 - System.currentTimeMillis();
            if (time < 0) {
                time = 0;
            }

            textViewTimer.setText(miliDetikToString(time));
            if (time > 0) {
                textViewTimer.postDelayed(runnableTimer, 200);
            } else buttonKirimUlangKode.setVisibility(View.VISIBLE);
        }
    };

    private boolean canceled = false;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 22) canceled = true;
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_konfirmasi_pendaftaran);
        buttonLanjutkan = findViewById(R.id.activity_konfirmasi_pendaftaran_Button);
        buttonKirimUlangKode = findViewById(R.id.activity_konfirmasi_pendaftaran_ButtonKirimUlangKode);
        progressbar = findViewById(R.id.activity_konfirmasi_pendaftaran_ProgressBar);
        editText = (EditText) findViewById(R.id.activity_konfirmasi_pendaftaran_TextInputEditText);
        textViewTimer = (TextView) findViewById(R.id.activity_konfirmasi_pendaftaran_TextViewTimer);
        buttonLanjutkan.setOnClickListener(this);
        buttonKirimUlangKode.setOnClickListener(this);

        pref = PreferenceManager.getDefaultSharedPreferences(this);
        waktuPengirimanKode = pref.getLong(Config.PREF_WAKTU_DAFTAR, 0);
        textViewTimer.post(runnableTimer);
    }


    @Override
    protected void onResume() {
        super.onResume();
        if (!canceled && pref.getBoolean(Config.PREF_NOMOR_CONFIRMED, false) && !pref.getString(Config.PREF_NOMOR_HP, "").isEmpty()) {
            startActivityForResult(new Intent(this, DaftarStep3Activity.class), 22);
            finish();
        }
    }

    private String miliDetikToString(long time) {
        String menitString = "0";
        String detikString = "00";
        if (time > 0) {
            int satumenit = 1000 * 60;
            int jumlahMenit = time > satumenit ? (int) (time / satumenit) : 0;
            int detik = (int) (time - jumlahMenit * satumenit) / 1000;
            menitString = String.valueOf(jumlahMenit);
            detikString = detik < 10 ? "0" + String.valueOf(detik) : String.valueOf(detik);
        }
        return menitString + ":" + detikString;
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.activity_konfirmasi_pendaftaran_ButtonKirimUlangKode) {
            kirimUlangKode();
        } else if (id == R.id.activity_konfirmasi_pendaftaran_Button) {
            validateCode();
        }
    }

    private void validateCode() {
        String code = editText.getText().toString().trim();
        if (code.isEmpty()) {
            editText.setError("Harus diisi");
            editText.requestFocus();
            return;
        }

        if (code.contains(" ")) {
            editText.setError("Pastikan kode sudah benar");
            editText.requestFocus();
            return;
        }
        progressbar.setVisibility(View.VISIBLE);
        buttonLanjutkan.setVisibility(View.INVISIBLE);
        String url = Config.URL_DAFTAR + "?aksi=2&kode=" + code;
        VolleyStringRequest request = new VolleyStringRequest(url, new VolleyStringRequest.Callback() {
            @Override
            public void onResponse(JsonObject jsonObject) {
                progressbar.setVisibility(View.INVISIBLE);
                buttonLanjutkan.setVisibility(View.VISIBLE);

                if (jsonObject.get("success").getAsBoolean()) {
                    Toast.makeText(KonfirmasiPendaftaranActivity.this, jsonObject.get("message").getAsString(), Toast.LENGTH_SHORT).show();
                    startActivityForResult(new Intent(KonfirmasiPendaftaranActivity.this, DaftarStep3Activity.class), 22);
                    pref.edit().putBoolean(Config.PREF_NOMOR_CONFIRMED, true).apply();
                    finish();
                } else
                    Util.showDialog(KonfirmasiPendaftaranActivity.this, jsonObject.get("message").getAsString());
            }
        });
        request.setTag(VOLLEY_TAG);
        VolleySingleton.getInstance(this).getRequestQueue().add(request);

    }

    private void kirimUlangKode() {
        String hp = pref.getString(Config.PREF_NOMOR_HP, "");
        if (hp.isEmpty()) {
            Util.showDialog(this, "Terjadi kesalahan.\nSilakan daftarkan ulang nomor HP anda");
            pref.edit().remove(Config.PREF_WAKTU_DAFTAR).apply();
            return;
        }
        String url = Config.URL_DAFTAR + "aksi=1&hp=" + hp;
        debug(getClass(), "Kirim ulang kode url=" + url);
        progressbar.setVisibility(View.VISIBLE);
        buttonKirimUlangKode.setVisibility(View.INVISIBLE);
        buttonLanjutkan.setVisibility(View.INVISIBLE);
        VolleyStringRequest requet = new VolleyStringRequest(url, new VolleyStringRequest.Callback() {
            @Override
            public void onResponse(JsonObject jsonObject) {
                debug(getClass(), "Kirim ulang kode response=" + jsonObject);
                progressbar.setVisibility(View.INVISIBLE);
                buttonLanjutkan.setVisibility(View.VISIBLE);

                if (jsonObject.get("success").getAsBoolean()) {
                    pref.edit().putLong(Config.PREF_WAKTU_DAFTAR, System.currentTimeMillis()).apply();
                    waktuPengirimanKode = System.currentTimeMillis();
                    textViewTimer.removeCallbacks(runnableTimer);
                    textViewTimer.post(runnableTimer);
                } else {
                    Util.showDialog(KonfirmasiPendaftaranActivity.this, jsonObject.get("message").getAsString());
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
