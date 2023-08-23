package com.zam.sidik_padang.login;

import android.Manifest;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.AppCompatSpinner;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.res.ResourcesCompat;
import androidx.loader.content.CursorLoader;
import androidx.preference.PreferenceManager;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import com.zam.sidik_padang.BaseActivity;
import com.zam.sidik_padang.MainActivity;
import com.zam.sidik_padang.R;
import com.zam.sidik_padang.util.Config;
import com.zam.sidik_padang.util.Db;
import com.zam.sidik_padang.util.UploadAsyncTask;
import com.zam.sidik_padang.util.Util;
import com.zam.sidik_padang.util.VolleySingleton;
import com.zam.sidik_padang.util.VolleyStringRequest;
import com.zam.sidik_padang.util.customclasses.SpinnerListener;


//import android.app.DatePickerDialog;

/**
 * Created by supriyadi on 5/18/17.
 * 0858 7126 5935
 */

public class DaftarStep3Activity extends BaseActivity
        implements View.OnClickListener {

    private static final String USER_ID = "ks1000005";
    private final String VOLLEY_TAG = getClass().getName();
    private EditText editTextNama,
            //editTextKtp, editTextHp, editTextKodePos, editTextTempatLahir,
            editTextEmail, editTextAlamat, editTextTanggalLahir,
                    editTextPekerjaan, editTextIbuKandung, editTextNamaUsaha,
                    editTextLokasiUsaha, editTextHpPeternakan, editTextEmailPeternakan, editTextNamaOlahan;

    private AppCompatSpinner spinnerKabupaten;
    private AppCompatSpinner spinnerKecamatan;
    private AppCompatSpinner spinnerDesa;

    private TextView textViewFoto;
    private String fotoLink = "";

    private View frameSpinnerNamaKelompokTernak, frameEditTextNamaOlahan,
            layout_ktp;

    private SpinnerDaftarAdapter
            adapterSpinnerProvinsi, adapterSpinnerKabupaten,
            adapterSpinnerKecamatan, adapterSpinnerDesa,
            //adapterSpinnerStatusPernikahan,
            adapterSpinnerJenisKelamin, adapterSpinnerAgama, adapterSpinnerLevel,
            adapterSpinnerStatusPeternakan, adapterSpinnerKelompok,
            adapterSpinnerNamaKelompok, adapterSpinnerJenisKomoditas, adapterSpinnerJenisUsaha,
            adapterSpinnerProdukPenjualan;

    private List<Map<String, String>> listProvinsi, listKabupaten, listKecamatan, listDesa,
            //listStatusPernikahan,
            listJenisKelamin, listAgama, listLevel,
            listStatusPeternakan, listKelompok, listNamaKelompok, listJenisKomoditas, listJenisUsaha,
            listProdukPenjualan;

    private View progressBarSpinnerProvinsi, progressBarSpinnerKabupaten, progressBarSpinnerKecamatan,
            progressBarSpinnerDesa,
            //progressBarSpinnerStatusPernikahan,
            progressBarSpinnerJenisKelamin,
            progressBarSpinnerAgama, progressBarSpinnerStatusPeternakan, progressBarSpinnerKelompok,
            progressBarSpinnerNamaKelompok, progressBarSpinnerJenisKomoditas, progressBarSpinnerJenisUsaha,
            progressBarSpinnerProdukPenjualan;

    private int selectedProvinsi = 0, selectedKabupaten = 0, selectedKecamatan = 0, selectedDesa = 0,
            //selectedStatusPernikahan = 0,
            selectedJenisKelamin = 0, selectedAgama = 0, selectedLevel = 0,
            selectedStatusPeternakan = 0, selectedKelompok = 0, selectedNamaKelompok = 0,
            selectedJenisKomoditas, selectedJenisUsaha = 0, selectedProdukPenjualan = 0;

    private Calendar birthDayCalendar;
    private ImageView imageViewFoto, imgAddFoto;
    private ProgressBar progressBarFoto;
    private boolean daftar = false;

    private boolean dialogErrorNotLoaded = false;

    private SharedPreferences pref;
    private String lastLoadedKabupatenCode = "";
    private VolleyStringRequest requestKabupaten;
    private String lastLoadedKecamatanCode = "";
    private VolleyStringRequest requestKecamatan;

    private String lastLoadedDesaCode = "";

    private VolleyStringRequest requestDesa;

    private void showPageNotLoadedErrorDialog() {
        if (dialogErrorNotLoaded) return;
        dialogErrorNotLoaded = true;
        new AlertDialog.Builder(this).setMessage(R.string.please_reload_the_page).setPositiveButton(android.R.string.ok,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        setResult(RESULT_CANCELED);
                        finish();
                    }
                }).show();
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tambah_peternak);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setResult(RESULT_CANCELED);
                finish();
            }
        });
        setResult(RESULT_CANCELED);
        pref = PreferenceManager.getDefaultSharedPreferences(this);
        findViewById(R.id.layout_nomor_hp).setVisibility(View.GONE);
        findViewById(R.id.layout_level).setVisibility(View.GONE);
        findViewById(R.id.layout_kode_pos).setVisibility(View.GONE);
        findViewById(R.id.layout_pernikahan).setVisibility(View.GONE);
        findViewById(R.id.layout_tempat_lahir).setVisibility(View.GONE);
        editTextNama = (EditText) findViewById(R.id.activity_tambah_peternak_EditTextName);
        //editTextKtp = (EditText) findViewById(R.id.activity_tambah_peternak_EditTextKTP);
        findViewById(R.id.layout_no_ktp).setVisibility(View.GONE);
		//editTextHp = (EditText) findViewById(R.id.activity_tambah_peternak_EditTextHP);
        editTextEmail = (EditText) findViewById(R.id.activity_tambah_peternak_EditTextEmail);
        //editTextKodePos = (EditText) findViewById(R.id.activity_tambah_peternak_EditTextPostCode);
        editTextAlamat = (EditText) findViewById(R.id.activity_tambah_peternak_EditTextAddress);
        //editTextTempatLahir = (EditText) findViewById(R.id.activity_tambah_peternak_EditTextTempatLahir);
        editTextTanggalLahir = (EditText) findViewById(R.id.activity_tambah_peternak_EditTextBirthday);
        editTextPekerjaan = (EditText) findViewById(R.id.activity_tambah_peternak_EditTextEmployment);
        editTextIbuKandung = (EditText) findViewById(R.id.activity_tambah_peternak_EditTextMother);
        editTextNamaUsaha = (EditText) findViewById(R.id.activity_tambah_peternak_EditTextNamaUsaha);
        editTextLokasiUsaha = (EditText) findViewById(R.id.activity_tambah_peternak_EditTextLokasiUsaha);
        editTextHpPeternakan = (EditText) findViewById(R.id.activity_tambah_peternak_EditTextHpPeternakan);
        editTextEmailPeternakan = (EditText) findViewById(R.id.activity_tambah_peternak_EditTextEmailPeternakan);
        editTextNamaOlahan = (EditText) findViewById(R.id.activity_tambah_peternak_EditTextNamaOlahan);

        AppCompatSpinner spinnerProvinsi = (AppCompatSpinner) findViewById(R.id.activity_tambah_peternak_SpinnerProvinsi);
        spinnerKabupaten = (AppCompatSpinner) findViewById(R.id.activity_tambah_peternak_SpinnerKaBupaten);
        spinnerKecamatan = (AppCompatSpinner) findViewById(R.id.activity_tambah_peternak_SpinnerKecamatan);
        spinnerDesa = (AppCompatSpinner) findViewById(R.id.activity_tambah_peternak_SpinnerDesa);
        //AppCompatSpinner spinnerStatusPernikahan = (AppCompatSpinner) findViewById(R.id.activity_tambah_peternak_SpinnerPernikahan);
        AppCompatSpinner spinnerJenisKelamin = (AppCompatSpinner) findViewById(R.id.activity_tambah_peternak_SpinnerGender);
        AppCompatSpinner spinnerAgama = (AppCompatSpinner) findViewById(R.id.activity_tambah_peternak_SpinnerReligion);
        AppCompatSpinner spinnerLevel = (AppCompatSpinner) findViewById(R.id.activity_tambah_peternak_SpinnerLevel);

        AppCompatSpinner spinnerStatusPeternakan = (AppCompatSpinner) findViewById(R.id.activity_tambah_peternak_SpinnerStatusPeternakan);
        AppCompatSpinner spinnerKelompok = (AppCompatSpinner) findViewById(R.id.activity_tambah_peternak_SpinnerKelompok);
        AppCompatSpinner spinnerNamaKelompok = (AppCompatSpinner) findViewById(R.id.activity_tambah_peternak_SpinnerNamaKelompok);
        AppCompatSpinner spinnerJenisKomoditas = (AppCompatSpinner) findViewById(R.id.activity_tambah_peternak_SpinnerJenisKomoditas);
        AppCompatSpinner spinnerJenisUsaha = (AppCompatSpinner) findViewById(R.id.activity_tambah_peternak_SpinnerJenisUsaha);
        AppCompatSpinner spinnerProdukPenjualan = (AppCompatSpinner) findViewById(R.id.activity_tambah_peternak_SpinnerProdukPenjualan);

        progressBarSpinnerProvinsi = findViewById(R.id.activity_tambah_peternak_ProgressbarspinnerProvinsi);
        progressBarSpinnerKabupaten = findViewById(R.id.activity_tambah_peternak_ProgressbarspinnerKabupaten);
        progressBarSpinnerKecamatan = findViewById(R.id.activity_tambah_peternak_ProgressbarspinnerKecamatan);
        progressBarSpinnerDesa = findViewById(R.id.activity_tambah_peternak_ProgressbarspinnerDesa);
        //progressBarSpinnerStatusPernikahan = findViewById(R.id.activity_tambah_peternak_ProgressbarspinnerPernikahan);
        progressBarSpinnerJenisKelamin = findViewById(R.id.activity_tambah_peternak_ProgressbarspinnerJenisKelamin);
        progressBarSpinnerAgama = findViewById(R.id.activity_tambah_peternak_ProgressbarspinnerReligion);
        progressBarSpinnerStatusPeternakan = findViewById(R.id.activity_tambah_peternak_ProgressbarspinnerStatusPeternakan);
        progressBarSpinnerKelompok = findViewById(R.id.activity_tambah_peternak_ProgressbarspinnerKelompok);
        progressBarSpinnerNamaKelompok = findViewById(R.id.activity_tambah_peternak_ProgressbarspinnerNamaKelompok);
        progressBarSpinnerJenisKomoditas = findViewById(R.id.activity_tambah_peternak_ProgressbarspinnerJenisKomoditas);
        progressBarSpinnerJenisUsaha = findViewById(R.id.activity_tambah_peternak_ProgressbarspinnerJenisUsaha);
        progressBarSpinnerProdukPenjualan = findViewById(R.id.activity_tambah_peternak_ProgressbarspinnerProdukPenjualan);
        progressBarFoto = (ProgressBar) findViewById(R.id.activity_tambah_peternak_ProgressbarFoto);

        frameSpinnerNamaKelompokTernak = findViewById(R.id.activity_tambah_peternak_FrameSpinnerNamaKelompokTernak);
        frameEditTextNamaOlahan = findViewById(R.id.activity_tambah_peternak_FrameEditTextNamaOlahan);

        imageViewFoto = (ImageView) findViewById(R.id.activity_tambah_peternak_ImageViewFoto);
        imgAddFoto = (ImageView) findViewById(R.id.imgAddFoto);
        imgAddFoto.setOnClickListener(this);
        textViewFoto = (TextView) findViewById(R.id.activity_tambah_peternak_TextViewFoto);


        findViewById(R.id.activity_tambah_peternak_ButtonSimpan).setOnClickListener(this);
        editTextTanggalLahir.setOnClickListener(this);

        listProvinsi = new ArrayList<>();
        Map<String, String> map = new HashMap<>();
        //map.put(Db.TABLE_PROVINSI_ID, "-1");
        //map.put(Db.TABLE_PROVINSI_NAMA, getString(R.string._select_provinsi_));
        map.put(Db.TABLE_PROVINSI_ID, "12920");
        map.put(Db.TABLE_PROVINSI_NAMA, "SUMATERA BARAT");
        listProvinsi.add(map);
        adapterSpinnerProvinsi = new SpinnerDaftarAdapter(this, listProvinsi);
        spinnerProvinsi.setAdapter(adapterSpinnerProvinsi);
        spinnerProvinsi.setOnItemSelectedListener(new CustomSpinnerListener(spinnerProvinsi.getId()));
        spinnerProvinsi.setEnabled(true);

        listKabupaten = new ArrayList<>();
        map = new HashMap<>();
        map.put(Db.TABLE_PROVINSI_ID, "-1");
        map.put(Db.TABLE_PROVINSI_NAMA, getString(R.string._select_kabupaten_));
        listKabupaten.add(map);
        adapterSpinnerKabupaten = new SpinnerDaftarAdapter(this, listKabupaten);
        spinnerKabupaten.setAdapter(adapterSpinnerKabupaten);
        spinnerKabupaten.setOnItemSelectedListener(new CustomSpinnerListener(spinnerKabupaten.getId()));
        spinnerKabupaten.setEnabled(true);

        listKecamatan = new ArrayList<>();
        map = new HashMap<>();
        map.put(Db.TABLE_PROVINSI_ID, "-1");
        map.put(Db.TABLE_PROVINSI_NAMA, getString(R.string._select_kecamatan_));
        listKecamatan.add(map);
        adapterSpinnerKecamatan = new SpinnerDaftarAdapter(this, listKecamatan);
        spinnerKecamatan.setAdapter(adapterSpinnerKecamatan);
        spinnerKecamatan.setOnItemSelectedListener(new CustomSpinnerListener(spinnerKecamatan.getId()));
        spinnerKecamatan.setEnabled(true);

        listDesa = new ArrayList<>();
        map = new HashMap<>();
        map.put(Db.TABLE_PROVINSI_ID, "-1");
        map.put(Db.TABLE_PROVINSI_NAMA, getString(R.string._select_desa_));
        listDesa.add(map);
        adapterSpinnerDesa = new SpinnerDaftarAdapter(this, listDesa);
        spinnerDesa.setAdapter(adapterSpinnerDesa);
        spinnerDesa.setOnItemSelectedListener(new CustomSpinnerListener(spinnerDesa.getId()));
        spinnerDesa.setEnabled(false);

        /*
        listStatusPernikahan = new ArrayList<>();
        map = new HashMap<>();
        map.put(Db.TABLE_PROVINSI_ID, "-1");
        map.put(Db.TABLE_PROVINSI_NAMA, getString(R.string._select_status_pernikahan_));
        listStatusPernikahan.add(map);
        adapterSpinnerStatusPernikahan = new SpinnerDaftarAdapter(this, listStatusPernikahan);
        spinnerStatusPernikahan.setAdapter(adapterSpinnerStatusPernikahan);
        spinnerStatusPernikahan.setOnItemSelectedListener(new CustomSpinnerListener(spinnerStatusPernikahan.getId()));
         */

        listJenisKelamin = new ArrayList<>();
        map = new HashMap<>();
        map.put(Db.TABLE_PROVINSI_ID, "-1");
        map.put(Db.TABLE_PROVINSI_NAMA, getString(R.string._select_gender_));
        listJenisKelamin.add(map);
        adapterSpinnerJenisKelamin = new SpinnerDaftarAdapter(this, listJenisKelamin);
        spinnerJenisKelamin.setAdapter(adapterSpinnerJenisKelamin);
        spinnerJenisKelamin.setOnItemSelectedListener(new CustomSpinnerListener(spinnerJenisKelamin.getId()));

        listAgama = new ArrayList<>();
        map = new HashMap<>();
        map.put(Db.TABLE_PROVINSI_ID, "-1");
        map.put(Db.TABLE_PROVINSI_NAMA, getString(R.string._select_religion_));
        listAgama.add(map);
        adapterSpinnerAgama = new SpinnerDaftarAdapter(this, listAgama);
        spinnerAgama.setAdapter(adapterSpinnerAgama);
        spinnerAgama.setOnItemSelectedListener(new CustomSpinnerListener(spinnerAgama.getId()));

        listStatusPeternakan = new ArrayList<>();
        map = new HashMap<>();
        map.put(Db.TABLE_PROVINSI_ID, "-1");
        map.put(Db.TABLE_PROVINSI_NAMA, getString(R.string._select_status_peternakan_));
        listStatusPeternakan.add(map);
        adapterSpinnerStatusPeternakan = new SpinnerDaftarAdapter(this, listStatusPeternakan);
        spinnerStatusPeternakan.setAdapter(adapterSpinnerStatusPeternakan);
        spinnerStatusPeternakan.setOnItemSelectedListener(new CustomSpinnerListener(spinnerStatusPeternakan.getId()));

        listKelompok = new ArrayList<>();
        map = new HashMap<>();
        map.put(Db.TABLE_PROVINSI_ID, "-1");
        map.put(Db.TABLE_PROVINSI_NAMA, getString(R.string._select_kelompok_));
        listKelompok.add(map);
        adapterSpinnerKelompok = new SpinnerDaftarAdapter(this, listKelompok);
        spinnerKelompok.setAdapter(adapterSpinnerKelompok);
        spinnerKelompok.setOnItemSelectedListener(new CustomSpinnerListener(spinnerKelompok.getId()));

        listNamaKelompok = new ArrayList<>();
        map = new HashMap<>();
        map.put(Db.TABLE_PROVINSI_ID, "-1");
        map.put(Db.TABLE_PROVINSI_NAMA, getString(R.string._select_nama_kelompok_));
        listNamaKelompok.add(map);
        adapterSpinnerNamaKelompok = new SpinnerDaftarAdapter(this, listNamaKelompok);
        spinnerNamaKelompok.setAdapter(adapterSpinnerNamaKelompok);
        spinnerNamaKelompok.setOnItemSelectedListener(new CustomSpinnerListener(spinnerNamaKelompok.getId()));

        listJenisKomoditas = new ArrayList<>();
        map = new HashMap<>();
        map.put(Db.TABLE_PROVINSI_ID, "-1");
        map.put(Db.TABLE_PROVINSI_NAMA, getString(R.string._select_jenis_komoditas_));
        listJenisKomoditas.add(map);
        adapterSpinnerJenisKomoditas = new SpinnerDaftarAdapter(this, listJenisKomoditas);
        spinnerJenisKomoditas.setAdapter(adapterSpinnerJenisKomoditas);
        spinnerJenisKomoditas.setOnItemSelectedListener(new CustomSpinnerListener(spinnerJenisKomoditas.getId()));

        listJenisUsaha = new ArrayList<>();
        map = new HashMap<>();
        map.put(Db.TABLE_PROVINSI_ID, "-1");
        map.put(Db.TABLE_PROVINSI_NAMA, getString(R.string._select_jenis_usaha_));
        listJenisUsaha.add(map);
        adapterSpinnerJenisUsaha = new SpinnerDaftarAdapter(this, listJenisUsaha);
        spinnerJenisUsaha.setAdapter(adapterSpinnerJenisUsaha);
        spinnerJenisUsaha.setOnItemSelectedListener(new CustomSpinnerListener(spinnerJenisUsaha.getId()));


        listProdukPenjualan = new ArrayList<>();
        map = new HashMap<>();
        map.put(Db.TABLE_PROVINSI_ID, "-1");
        map.put(Db.TABLE_PROVINSI_NAMA, getString(R.string._select_produk_penjualan_));
        listProdukPenjualan.add(map);
        adapterSpinnerProdukPenjualan = new SpinnerDaftarAdapter(this, listProdukPenjualan);
        spinnerProdukPenjualan.setAdapter(adapterSpinnerProdukPenjualan);
        spinnerProdukPenjualan.setOnItemSelectedListener(new CustomSpinnerListener(spinnerProdukPenjualan.getId()));
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    loadList();
                } catch (InterruptedException e) {
                    Log.e(getClass().getName(), e.getMessage());
                }
            }

            private void loadList() throws InterruptedException {
                Thread.sleep(500);
                runOnUiThread(new Runnable() {
                    public void run() {
                        loadKabupatenFromServer("12920");
                    }
                });
            }
        }).start();
        loadListGabungan();
    }

    private void loadListGabungan() {
        if (!Util.isInternetAvailible(this)) {
            Util.showDialog(this, "Masalah koneksi internet");
            return;
        }

        String url = Config.URL_GABUNGAN;
        final Dialog d = ProgressDialog.show(this, null, "Memuat halaman...", true, false);
        VolleyStringRequest request = new VolleyStringRequest(url, new VolleyStringRequest.Callback() {
            @Override
            public void onResponse(JsonObject jsonObject) {
                d.dismiss();
                debug(getClass(), "Load gabungan response=" + jsonObject);
                if (jsonObject.get("success").getAsBoolean()) {
                    bindView(jsonObject);
                } else
                    Toast.makeText(DaftarStep3Activity.this, jsonObject.get("message").getAsString(), Toast.LENGTH_SHORT).show();
            }
        });
        request.setTag(VOLLEY_TAG);
        VolleySingleton.getInstance(this).getRequestQueue().add(request);
    }

    private void bindView(JsonObject jsonObject) {
        if (listProdukPenjualan.size() < 2) {
            JsonElement je = jsonObject.get("produk_ternak");
            if (je == null) return;
            JsonArray array = je.getAsJsonArray();
            Map<String, String> map;
            for (JsonElement element : array) {
                if (element == null) continue;
                JsonObject jo = element.getAsJsonObject();
                map = new HashMap<>();
                map.put(Db.TABLE_PROVINSI_ID, jo.get(Db.TABLE_PROVINSI_ID).getAsString());
                map.put(Db.TABLE_PROVINSI_NAMA, jo.get("produk").getAsString());
                listProdukPenjualan.add(map);
            }
            adapterSpinnerJenisUsaha.notifyDataSetChanged();
        }

        if (listJenisUsaha.size() < 2) {
            JsonElement je = jsonObject.get("jenis_usaha");
            if (je == null) return;
            JsonArray array = je.getAsJsonArray();
            Map<String, String> map;
            for (JsonElement element : array) {
                if (element == null) continue;
                JsonObject jo = element.getAsJsonObject();
                map = new HashMap<>();
                map.put(Db.TABLE_PROVINSI_ID, jo.get(Db.TABLE_PROVINSI_ID).getAsString());
                map.put(Db.TABLE_PROVINSI_NAMA, jo.get("jenis").getAsString());
                listJenisUsaha.add(map);
            }
            adapterSpinnerJenisUsaha.notifyDataSetChanged();
        }

        if (listJenisKomoditas.size() < 2) {
            JsonElement je = jsonObject.get("jenis_komoditas");
            if (je == null) return;
            JsonArray array = je.getAsJsonArray();
            Map<String, String> map;
            for (JsonElement element : array) {
                if (element == null) continue;
                JsonObject jo = element.getAsJsonObject();
                map = new HashMap<>();
                map.put(Db.TABLE_PROVINSI_ID, jo.get(Db.TABLE_PROVINSI_ID).getAsString());
                map.put(Db.TABLE_PROVINSI_NAMA, jo.get("jenis").getAsString());
                listJenisKomoditas.add(map);
            }
            adapterSpinnerJenisKomoditas.notifyDataSetChanged();
        }

        if (listKelompok.size() < 2) {
            JsonElement je = jsonObject.get("kelompok");
            if (je == null) return;
            JsonArray array = je.getAsJsonArray();
            Map<String, String> map;
            for (JsonElement element : array) {
                if (element == null) continue;
                JsonObject jo = element.getAsJsonObject();
                map = new HashMap<>();
                map.put(Db.TABLE_PROVINSI_ID, jo.get(Db.TABLE_PROVINSI_ID).getAsString());
                map.put(Db.TABLE_PROVINSI_NAMA, jo.get("keterangan").getAsString());
                listKelompok.add(map);
            }
            adapterSpinnerKelompok.notifyDataSetChanged();
        }

        if (listNamaKelompok.size() < 2) {
            JsonElement je = jsonObject.get("kelompok_ternak");
            if (je != null) {
                for (int i = listNamaKelompok.size() - 1; i > 0; i--)
                    listNamaKelompok.remove(i);
                JsonArray array = je.getAsJsonArray();
                Map<String, String> map;
                for (JsonElement element : array) {
                    if (element == null) continue;
                    JsonObject jo = element.getAsJsonObject();
                    map = new HashMap<>();
                    map.put(Db.TABLE_PROVINSI_ID, jo.get(Db.TABLE_PROVINSI_ID).getAsString());
                    map.put(Db.TABLE_PROVINSI_NAMA, jo.get("nama_kelompok").getAsString());
                    listNamaKelompok.add(map);
                }
                adapterSpinnerNamaKelompok.notifyDataSetChanged();
            }
        }

        if (listStatusPeternakan.size() < 2) {
            JsonElement je = jsonObject.get("status_peternakan");
            if (je == null) return;
            JsonArray array = je.getAsJsonArray();
            Map<String, String> map;
            for (JsonElement element : array) {
                if (element == null) continue;
                JsonObject jo = element.getAsJsonObject();
                map = new HashMap<>();
                map.put(Db.TABLE_PROVINSI_ID, jo.get(Db.TABLE_PROVINSI_ID).getAsString());
                map.put(Db.TABLE_PROVINSI_NAMA, jo.get("status").getAsString());
                listStatusPeternakan.add(map);
            }
            adapterSpinnerStatusPeternakan.notifyDataSetChanged();
        }

        if (listJenisKelamin.size() < 2) {
            JsonElement je = jsonObject.get("status_agama");
            if (je == null) return;
            JsonArray array = je.getAsJsonArray();
            Map<String, String> map;
            for (JsonElement element : array) {
                if (element == null) continue;
                JsonObject jo = element.getAsJsonObject();
                map = new HashMap<>();
                map.put(Db.TABLE_PROVINSI_ID, jo.get(Db.TABLE_PROVINSI_ID).getAsString());
                map.put(Db.TABLE_PROVINSI_NAMA, jo.get("status").getAsString());
                listAgama.add(map);
            }
            adapterSpinnerAgama.notifyDataSetChanged();
        }

        if (listJenisKelamin.size() < 2) {
            JsonElement je = jsonObject.get("Kelamin");
            if (je == null) return;
            JsonArray array = je.getAsJsonArray();
            Map<String, String> map;
            for (JsonElement element : array) {
                if (element == null) continue;
                JsonObject jo = element.getAsJsonObject();
                map = new HashMap<>();
                map.put(Db.TABLE_PROVINSI_ID, jo.get(Db.TABLE_PROVINSI_ID).getAsString());
                map.put(Db.TABLE_PROVINSI_NAMA, jo.get("kelamin").getAsString());
                listJenisKelamin.add(map);
            }
            adapterSpinnerJenisKelamin.notifyDataSetChanged();
        }
        /*
        if (listStatusPernikahan.size() < 2) {
            JsonElement je = jsonObject.get("status_pernikahan");
            if (je == null) return;
            JsonArray array = je.getAsJsonArray();
            Map<String, String> map;
            for (JsonElement element : array) {
                if (element == null) continue;
                JsonObject jo = element.getAsJsonObject();
                map = new HashMap<>();
                map.put(Db.TABLE_PROVINSI_ID, jo.get(Db.TABLE_PROVINSI_ID).getAsString());
                map.put(Db.TABLE_PROVINSI_NAMA, jo.get("status").getAsString());
                listStatusPernikahan.add(map);
            }
            adapterSpinnerStatusPernikahan.notifyDataSetChanged();
        }
         */


    }

    private void loadListNamaKelompok() {
        if (listNamaKelompok.size() > 1 || !Util.isInternetAvailible(this)) return;
        if (frameSpinnerNamaKelompokTernak.getVisibility() == View.VISIBLE)
            progressBarSpinnerNamaKelompok.setVisibility(View.VISIBLE);

        String url = Config.URL_NAMA_KELOMPOK;
        url += "?userid=" + USER_ID;
        debug(getClass(), "Load nama kelompok from server url: " + url);

        VolleyStringRequest request = new VolleyStringRequest(url, new VolleyStringRequest.Callback() {

            @Override
            public void onResponse(JsonObject jsonObject) {
                if (frameSpinnerNamaKelompokTernak.getVisibility() == View.VISIBLE)
                    progressBarSpinnerNamaKelompok.setVisibility(View.INVISIBLE);
                debug(getClass(), "Retrieve nama kelompok onresponse " + jsonObject);
                if (jsonObject.get("success").getAsBoolean()) {
                    JsonElement je = jsonObject.get("kelompok_ternak");
                    if (je == null) return;
                    for (int i = listNamaKelompok.size() - 1; i > 0; i--)
                        listNamaKelompok.remove(i);
                    JsonArray array = je.getAsJsonArray();
                    Map<String, String> map;
                    for (JsonElement element : array) {
                        if (element == null) continue;
                        JsonObject jo = element.getAsJsonObject();
                        map = new HashMap<>();
                        map.put(Db.TABLE_PROVINSI_ID, jo.get(Db.TABLE_PROVINSI_ID).getAsString());
                        map.put(Db.TABLE_PROVINSI_NAMA, jo.get("nama_kelompok").getAsString());
                        listNamaKelompok.add(map);
                    }
                    adapterSpinnerNamaKelompok.notifyDataSetChanged();
                } else {
                    debug(getClass(), "error_message: " + jsonObject.get("message").getAsString());
                }

            }
        });

        request.setTag(VOLLEY_TAG);
        VolleySingleton.getInstance(this).getRequestQueue().add(request);
    }

    private void loadProvinsiList() {
        if (listProvinsi.size() > 1) return;
        listProvinsi.addAll(Db.getInstance(this).getAllProvinsi());
        adapterSpinnerProvinsi.notifyDataSetChanged();
        if (listProvinsi.size() > 1) return;
        if (!Util.isInternetAvailible(this)) return;

        progressBarSpinnerProvinsi.setVisibility(View.VISIBLE);

        String url = Config.URL_NAMA_DAERAH;
        url += "?wilayah=provinsi" +
                "&code=0";
        url += "&userid=" + USER_ID;
        debug(getClass(), "Load provinsi from server url " + url);

        VolleyStringRequest request = new VolleyStringRequest(url, new VolleyStringRequest.Callback() {

            @Override
            public void onResponse(JsonObject jsonObject) {
                progressBarSpinnerProvinsi.setVisibility(View.INVISIBLE);

                debug(getClass(), "Retrieve provinsi onresponse");
                if (jsonObject.get("success").getAsBoolean()) {
                    JsonElement je = jsonObject.get("provinsi");
                    if (je == null) return;
                    JsonArray array = je.getAsJsonArray();
                    Map<String, String> map;
                    for (JsonElement element : array) {
                        if (element == null) continue;
                        JsonObject jo = element.getAsJsonObject();
                        map = new HashMap<>();
                        map.put(Db.TABLE_PROVINSI_ID, jo.get(Db.TABLE_PROVINSI_ID).getAsString());
                        map.put(Db.TABLE_PROVINSI_NAMA, jo.get(Db.TABLE_PROVINSI_NAMA).getAsString());
                        listProvinsi.add(map);
                    }
                    adapterSpinnerProvinsi.notifyDataSetChanged();
                    Db.getInstance(DaftarStep3Activity.this).saveprovinsi(array);
                } else {
                    debug(getClass(), "error_message: " + jsonObject.get("message").getAsString());
                    if (isResummed) showPageNotLoadedErrorDialog();
                }

            }
        });

        request.setTag(VOLLEY_TAG);
        VolleySingleton.getInstance(this).getRequestQueue().add(request);

    }

    @Override
    public void onClick(View p1) {
        int id = p1.getId();
        if (id == R.id.activity_tambah_peternak_EditTextBirthday) {
            Calendar calendar = Calendar.getInstance();
//			DatePickerDialog datePicker = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
//					@Override
//					public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
//						birthDayCalendar = Calendar.getInstance();
//						birthDayCalendar.set(Calendar.YEAR, year);
//						birthDayCalendar.set(Calendar.MONTH, month);
//						birthDayCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
//						editTextTanggalLahir.setText(new SimpleDateFormat("EEEE d M yyyy", Locale.getDefault()).format(birthDayCalendar.getTime()));
//					}
//				}, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
//			datePicker.show();
            DatePickerDialog dpd = DatePickerDialog.newInstance(new DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
                    birthDayCalendar = Calendar.getInstance();
                    birthDayCalendar.set(Calendar.YEAR, year);
                    birthDayCalendar.set(Calendar.MONTH, monthOfYear);
                    birthDayCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                    editTextTanggalLahir.setText(new SimpleDateFormat("EEEE d M yyyy", Locale.getDefault()).format(birthDayCalendar.getTime()));

                }
            }, calendar);
            dpd.showYearPickerFirst(true);
            dpd.setMaxDate(calendar);
            dpd.setVersion(DatePickerDialog.Version.VERSION_2);
            dpd.show(getSupportFragmentManager(), "dialog");


        } else if (id == R.id.activity_tambah_peternak_ButtonSimpan) {
            validateAndGo();
        } else if (id == R.id.imgAddFoto) {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                launchPickPhotoIntent();
            } else {
                if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_EXTERNAL_STORAGE)) {
                    new AlertDialog.Builder(this).setMessage(R.string.need_to_access_media_storage).setPositiveButton(android.R.string.ok,
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    ActivityCompat.requestPermissions(DaftarStep3Activity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 12);
                                }
                            }).show();
                } else
                    ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 12);
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        debug(getClass(), "onRequestPermission result");
        if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) launchPickPhotoIntent();
    }

    private void launchPickPhotoIntent() {
        Intent pickPhotoIntent = new Intent(Intent.ACTION_PICK);
        pickPhotoIntent.setType("image/*");
        startActivityForResult(pickPhotoIntent, 13);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == 13) {
            Uri uriHasil = data.getData();
            String stringHasil = getDir(uriHasil);
            textViewFoto.setText(getString(R.string.uploading, stringHasil));
            debug(getClass(), "ambil path dari uri. Hasil=" + stringHasil);

            if (stringHasil.contains("/")) {

                BitmapFactory.Options option = new BitmapFactory.Options();
                option.inJustDecodeBounds = true;
                BitmapFactory.decodeFile(stringHasil, option);
                int skala = 1;
                boolean land = option.outWidth > option.outHeight;
                int panjang = 640, pendek = 480;
                if (land) {
                    if (option.outWidth > panjang && option.outHeight > pendek) {
                        skala = Math.min(option.outWidth / panjang, option.outHeight / pendek);
                    }
                } else {
                    if (option.outWidth > pendek && option.outHeight > panjang) {
                        skala = Math.min(option.outWidth / pendek, option.outHeight / panjang);
                    }
                }
                option.inJustDecodeBounds = false;
                option.inSampleSize = skala;
                Bitmap bm = BitmapFactory.decodeFile(stringHasil, option);
                File fBaru = new File(getCacheDir(), "foto_peternak.jpg");
                if (fBaru.exists()) fBaru.delete();
                try {
                    fBaru.createNewFile();
                    OutputStream os = new FileOutputStream(fBaru);
                    bm.compress(Bitmap.CompressFormat.JPEG, 90, os);
                    os.flush();
                    os.close();
                } catch (IOException e) {
                    Log.e(getClass().getName(), e.getMessage());
                    return;
                }
                imageViewFoto.setImageBitmap(bm);
                debug(getClass(), "loaded image width: " + bm.getWidth() + " height: " + bm.getHeight());
                uploadGambar(fBaru.getAbsolutePath());
                debug(getClass(), "fBaru: " + fBaru.getAbsolutePath());
            } else {
                debug(getClass(), "Ambil path dari uri error. Hasil=" + stringHasil + " Uri=" + uriHasil + " IntentData=" + data);
            }
        }
    }

    private void uploadGambar(String filePath) {
        if (!Util.isInternetAvailible(this)) return;
        HashMap<String, String> param = new HashMap<>();
        param.put("userid", USER_ID);
        param.put("aksi", "2");
        String[] splited = filePath.split("/");
        param.put("ao", splited[splited.length - 1]);
        new UploadAsyncTask(Config.URL_TAMBAH_GAMBAR, filePath, param, new UploadAsyncTask.Callback() {

            @Override
            public void onStart() {
                progressBarFoto.setMax(100);
                progressBarFoto.setVisibility(View.VISIBLE);
                progressBarFoto.setProgress(4);
                debug(getClass(), "upload onstart");
            }

            @Override
            public void onProgressUpdate(int progressKb, int totalKb) {
                progressBarFoto.setProgress(progressKb * 100 / totalKb);
                debug(getClass(), "upload onProgress " + progressKb + "/" + totalKb);
            }

            @Override
            public void onEnd(JsonObject je) {
                debug(getClass(), "upload onEnd " + je);
                int text = R.string.upload_failed;
                Drawable drawable = null;

                if (je.get("success").getAsBoolean()) {
                    text = R.string.uploaded;
                    drawable = ResourcesCompat.getDrawable(getResources(), R.drawable.ic_check, getTheme());
                    int rb = (int) (getResources().getDisplayMetrics().density * 24f);
                    if (null != drawable) drawable.setBounds(0, 0, rb, rb);
                    fotoLink = je.get("link").getAsString();
                }

                textViewFoto.setCompoundDrawablesRelative(drawable, null, null, null);
                textViewFoto.setText(text);
                progressBarFoto.setVisibility(View.INVISIBLE);
            }
        });

    }

    private String getDir(Uri uri) {
        Cursor cursor = new CursorLoader(this, uri, new String[]{"_data"}, (String) null, (String[]) null, (String) null).loadInBackground();
        int n = cursor.getColumnIndexOrThrow("_data");
        cursor.moveToFirst();
        return cursor.getString(n);
    }

    private void validateAndGo() {
        String url = Config.URL_DAFTAR + "?aksi=3&userid=" + USER_ID;

        String nama = editTextNama.getText().toString().trim();
        if (nama.isEmpty()) {
            editTextNama.setError(getString(R.string.this_field_is_mandatory));
            editTextNama.requestFocus();
            return;
        } else if (nama.length() < 4) {
            editTextNama.setError(getString(R.string.please_enter_valid_name));
            editTextNama.requestFocus();
            return;
        }

        url += "&nama=" + encode(nama);

        /*
        String ktp = editTextKtp.getText().toString().trim();
        if (ktp.isEmpty()) {
            editTextKtp.setError(getString(R.string.this_field_is_mandatory));
            editTextKtp.requestFocus();
            return;
        } else if (ktp.length() < 10 || ktp.contains(" ")) {
            editTextKtp.setError(getString(R.string.invalid_id_card));
            editTextKtp.requestFocus();
            return;
        }

        url += "&ktp=" + encode(ktp);
         */
        url += "&hp=" + pref.getString(Config.PREF_NOMOR_HP, "");

        String email = editTextEmail.getText().toString().trim();
        url += "&email=" + encode(email);
        url += "&provinsi=12920";

        if (selectedKabupaten == 0) {
            Util.showDialog(this, getString(R.string.please_select_kabupaten));
            return;
        }
        url += "&kabupaten=" + listKabupaten.get(selectedKabupaten).get("id");

        if (selectedKecamatan == 0) {
            Util.showDialog(this, getString(R.string.please_select_kecamatan));
            return;
        }
        url += "&kecamatan=" + listKecamatan.get(selectedKecamatan).get("id");

        if (selectedDesa == 0) {
            Util.showDialog(this, getString(R.string.please_select_desa));
            return;
        }
        url += "&desa=" + listDesa.get(selectedDesa).get("id");

        //url += "&kode_pos=" + encode(editTextKodePos.getText().toString().trim());
        url += "&alamat=" + encode(editTextAlamat.getText().toString().trim());
        //url += "&status_pernikahan=" + (selectedStatusPernikahan == 0 ? "" : listStatusPernikahan.get(selectedStatusPernikahan).get("id"));
        //url += "&tempat_lahir=" + encode(editTextTempatLahir.getText().toString().trim());
        url += "&tanggal_lahir=" + (birthDayCalendar == null ? "" : new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(birthDayCalendar.getTime()));
        url += "&id_jenis_kelamin=" + (selectedJenisKelamin == 0 ? "" : listJenisKelamin.get(selectedJenisKelamin).get("id"));
        url += "&id_agama=" + (selectedAgama == 0 ? "" : listAgama.get(selectedAgama).get("id"));
        url += "&pekerjaan=" + encode(editTextPekerjaan.getText().toString().trim());
        url += "&ibu=" + encode(editTextIbuKandung.getText().toString().trim());
        url += "&level=" + (selectedLevel == 0 ? "" : listLevel.get(selectedLevel).get("id"));

        url += "&nama_usaha=" + encode(editTextNamaUsaha.getText().toString().trim());

        if (selectedStatusPeternakan == 0) {
            Util.showDialog(this, getString(R.string.please_select_status_peternakan));
            return;
        }
        url += "&status_peternakan=" + listStatusPeternakan.get(selectedStatusPeternakan).get("id");

        if (selectedKelompok == 0) {
            Util.showDialog(this, getString(R.string.please_select_individu_or_kelompok));
            return;
        }
        url += "&kelompok=" + listKelompok.get(selectedKelompok).get("id");
        if (selectedKelompok == 2) {
            if (selectedNamaKelompok == 0) {
                Util.showDialog(this, getString(R.string.please_select_nama_kelompok));
                return;
            }
            url += "&kelompok_ternak=" + listNamaKelompok.get(selectedNamaKelompok).get("id");
        }

        String lokasi_usaha = editTextLokasiUsaha.getText().toString().trim();
        if (lokasi_usaha.isEmpty()) {
            editTextLokasiUsaha.setError(getString(R.string.this_field_is_mandatory));
            editTextLokasiUsaha.requestFocus();
            return;
        }
        url += "&lokasi_usaha=" + encode(lokasi_usaha);
        String hpPeternakan = editTextHpPeternakan.getText().toString().trim();
        url += "&hp_peternakan=" + (hpPeternakan.isEmpty() ? "" : encode(hpPeternakan));

        url += "&email_peternakan=" + editTextEmailPeternakan.getText().toString().trim();

        if (selectedJenisKomoditas == 0) {
            Util.showDialog(this, getString(R.string.please_select_jenis_komoditas));
            return;
        }
        url += "&jenis_komoditas=" + listJenisKomoditas.get(selectedJenisKomoditas).get("id");
        if (selectedJenisUsaha == 0) {
            Util.showDialog(this, getString(R.string.please_select_jenis_usaha));
            return;
        }
        url += "&jenis_usaha=" + listJenisUsaha.get(selectedJenisUsaha).get("id");

        url += "&produk_penjualan=" + (selectedProdukPenjualan == 0 ? "" : listProdukPenjualan.get(selectedProdukPenjualan).get("id"));
        if (selectedProdukPenjualan == 4)
            url += "&nama_olahan=" + encode(editTextNamaOlahan.getText().toString().trim());
        url += "&foto=" + fotoLink;

        debug(getClass(), "Validate and go. Url: " + url);

        final Dialog dialog = ProgressDialog.show(this, getString(R.string.please_wait), getString(R.string.sending_data), true, false);
        VolleyStringRequest request = new VolleyStringRequest(url, new VolleyStringRequest.Callback() {
            @Override
            public void onResponse(JsonObject jsonObject) {
                dialog.dismiss();
                debug(getClass(), "tambah peternak onresponse: " + jsonObject);
                if (jsonObject.get("success").getAsBoolean()) {
                    JsonArray ja = jsonObject.get("member").getAsJsonArray();
                    if (ja.size() > 0) {
                        setResult(RESULT_OK);
                        SharedPreferences.Editor editor = pref.edit();
                        editor.remove(Config.PREF_WAKTU_DAFTAR);
                        editor.remove(Config.PREF_NOMOR_CONFIRMED);
                        editor.apply();
                        JsonObject jsonMember = ja.get(0).getAsJsonObject();
                        Util.saveUserDetail(DaftarStep3Activity.this, jsonMember);
                        Toast.makeText(DaftarStep3Activity.this, jsonObject.get("message").getAsString(), Toast.LENGTH_SHORT).show();
                        Intent it = new Intent(DaftarStep3Activity.this, MainActivity.class);
                        it.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        it.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                        startActivity(it);
                        finish();
                    }
                } else {

                    if (isResummed) {
                        JsonElement je = jsonObject.get("message");
                        Util.showDialog(DaftarStep3Activity.this, je == null ? getString(R.string.an_error_ocurred) : je.getAsString());
                    }
                }
            }
        });


        request.setTag(VOLLEY_TAG);
        VolleySingleton.getInstance(this).getRequestQueue().add(request);
    }

    private String encode(String src) {
        try {
            return URLEncoder.encode(src, "utf-8");
        } catch (UnsupportedEncodingException e) {
            debug(getClass(), e.getMessage());
            return src;
        }
    }

    private void loadKabupatenFromServer(final String s) {
        if (!Util.isInternetAvailible(this)) return;
        if (s.equals(lastLoadedKabupatenCode) && listKabupaten.size() > 1) return;
        progressBarSpinnerKabupaten.setVisibility(View.VISIBLE);

        if (requestKabupaten != null) requestKabupaten.cancel();

        String url = Config.URL_NAMA_DAERAH;
        url += "?wilayah=kabupaten" +
                "&code=" + s;
        url += "&userid=" + USER_ID;
        debug(getClass(), "Load kabupaten from server url " + url);

        requestKabupaten = new VolleyStringRequest(url, new VolleyStringRequest.Callback() {

            @Override
            public void onResponse(JsonObject jsonObject) {
                progressBarSpinnerKabupaten.setVisibility(View.INVISIBLE);

                debug(getClass(), "Retrieve kabupaten onresponse " + jsonObject.get("message").getAsString());
                if (jsonObject.get("success").getAsBoolean()) {
                    JsonElement je = jsonObject.get("kabupaten");
                    if (je == null) return;
                    JsonArray array = je.getAsJsonArray();
                    Map<String, String> map;
                    for (int i = listKabupaten.size() - 1; i > 0; i--) listKabupaten.remove(i);

                    for (JsonElement element : array) {
                        if (element == null) continue;
                        JsonObject jo = element.getAsJsonObject();
                        map = new HashMap<>();
                        map.put(Db.TABLE_PROVINSI_ID, jo.get(Db.TABLE_PROVINSI_ID).getAsString());
                        map.put(Db.TABLE_PROVINSI_NAMA, jo.get(Db.TABLE_PROVINSI_NAMA).getAsString());
                        listKabupaten.add(map);
                    }
                    adapterSpinnerKabupaten.notifyDataSetChanged();
                    lastLoadedKabupatenCode = s;
                } else {
                    debug(getClass(), "error_message: " + jsonObject.get("message").getAsString());
                }
            }
        });

        requestKabupaten.setTag(VOLLEY_TAG);
        VolleySingleton.getInstance(this).getRequestQueue().add(requestKabupaten);
    }

    private void loadKecamatanFromServer(final String s) {
        if (!Util.isInternetAvailible(this)) return;
        if (s.equals(lastLoadedKecamatanCode) && listKecamatan.size() > 1) return;
        progressBarSpinnerKecamatan.setVisibility(View.VISIBLE);
        String url = Config.URL_NAMA_DAERAH;
        url += "?wilayah=kecamatan" +
                "&code=" + s;
        url += "&userid=" + USER_ID;
        debug(getClass(), "Load kecamatan from server url " + url);
        if (requestKecamatan != null) requestKecamatan.cancel();
        requestKecamatan = new VolleyStringRequest(url, new VolleyStringRequest.Callback() {

            @Override
            public void onResponse(JsonObject jsonObject) {
                progressBarSpinnerKecamatan.setVisibility(View.INVISIBLE);

                debug(getClass(), "Retrieve kecamatan onresponse " + jsonObject.get("message").getAsString());
                if (jsonObject.get("success").getAsBoolean()) {
                    JsonElement je = jsonObject.get("kecamatan");
                    if (je == null) return;
                    JsonArray array = je.getAsJsonArray();
                    Map<String, String> map;
                    for (int i = listKecamatan.size() - 1; i > 0; i--) listKecamatan.remove(i);

                    for (JsonElement element : array) {
                        if (element == null) continue;
                        JsonObject jo = element.getAsJsonObject();
                        map = new HashMap<>();
                        map.put(Db.TABLE_PROVINSI_ID, jo.get(Db.TABLE_PROVINSI_ID).getAsString());
                        map.put(Db.TABLE_PROVINSI_NAMA, jo.get(Db.TABLE_PROVINSI_NAMA).getAsString());
                        listKecamatan.add(map);
                    }
                    adapterSpinnerKecamatan.notifyDataSetChanged();
                    lastLoadedKecamatanCode = s;
                } else {
                    debug(getClass(), "error_message: " + jsonObject.get("message").getAsString());
                }

            }
        });

        requestKecamatan.setTag(VOLLEY_TAG);
        VolleySingleton.getInstance(this).getRequestQueue().add(requestKecamatan);
    }

    private void loadDesaFromServer(final String s) {
        if (!Util.isInternetAvailible(this)) return;
        if (s.equals(lastLoadedDesaCode) && listDesa.size() > 1) return;
        progressBarSpinnerDesa.setVisibility(View.VISIBLE);
        String url = Config.URL_NAMA_DAERAH;
        url += "?wilayah=desa" +
                "&code=" + s;
        url += "&userid=" + USER_ID;
        debug(getClass(), "Load desa from server url " + url);
        if (requestDesa != null) requestDesa.cancel();
        requestDesa = new VolleyStringRequest(url, new VolleyStringRequest.Callback() {

            @Override
            public void onResponse(JsonObject jsonObject) {
                progressBarSpinnerDesa.setVisibility(View.INVISIBLE);
                debug(getClass(), "Retrieve desa onresponse " + jsonObject.get("message").getAsString());
                if (jsonObject.get("success").getAsBoolean()) {
                    JsonElement je = jsonObject.get("desa");
                    if (je == null) return;
                    JsonArray array = je.getAsJsonArray();
                    Map<String, String> map;
                    for (int i = listDesa.size() - 1; i > 0; i--) listDesa.remove(i);

                    for (JsonElement element : array) {
                        if (element == null) continue;
                        JsonObject jo = element.getAsJsonObject();
                        map = new HashMap<>();
                        map.put(Db.TABLE_PROVINSI_ID, jo.get(Db.TABLE_PROVINSI_ID).getAsString());
                        map.put(Db.TABLE_PROVINSI_NAMA, jo.get(Db.TABLE_PROVINSI_NAMA).getAsString());
                        listDesa.add(map);
                    }
                    adapterSpinnerDesa.notifyDataSetChanged();
                    lastLoadedDesaCode = s;
                } else {
                    debug(getClass(), "error_message: " + jsonObject.get("message").getAsString());
                }

            }
        });

        requestDesa.setTag(VOLLEY_TAG);
        VolleySingleton.getInstance(this).getRequestQueue().add(requestDesa);
    }

    @Override
    protected void onDestroy() {
        VolleySingleton.getInstance(this).getRequestQueue().cancelAll(VOLLEY_TAG);
        super.onDestroy();
    }

    @Override
    public void onBackPressed() {
        new AlertDialog.Builder(this)
                .setMessage("Proses pendaftaran belum selesai")
                .setNegativeButton("Batalkan", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(DaftarStep3Activity.this).edit();
                        editor.remove(Config.PREF_NOMOR_HP);
                        editor.remove(Config.PREF_WAKTU_DAFTAR);
                        editor.apply();
                        Intent intent = new Intent(DaftarStep3Activity.this, LoginActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                    }
                })
                .setPositiveButton("Lanjutkan", null)
                .show();
    }

    private class CustomSpinnerListener extends SpinnerListener {
        private final int viewId;

        public CustomSpinnerListener(int viewId) {
            this.viewId = viewId;
        }

        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
			/*
			if (viewId == R.id.activity_tambah_peternak_SpinnerProvinsi) {
				selectedProvinsi = position;
				if (position < 0) {
					spinnerKabupaten.setEnabled(false);
					spinnerKecamatan.setEnabled(false);
					spinnerDesa.setEnabled(false);
				} else {
					spinnerKabupaten.setEnabled(true);
					//loadKabupatenFromServer(listProvinsi.get(position).get(Db.TABLE_PROVINSI_ID));
				}
			} else 			 */
            if (viewId == R.id.activity_tambah_peternak_SpinnerKaBupaten) {
				selectedKabupaten = position;
				if (position < 0) {
					spinnerKecamatan.setEnabled(false);
					spinnerDesa.setEnabled(false);
				} else {
					spinnerKecamatan.setEnabled(true);
					loadKecamatanFromServer(listKabupaten.get(position).get(Db.TABLE_PROVINSI_ID));
				}
			} else if (viewId == R.id.activity_tambah_peternak_SpinnerKecamatan) {
                selectedKecamatan = position;
                if (position == 0) {
                    spinnerDesa.setEnabled(false);
                } else {
                    spinnerDesa.setEnabled(true);
                    loadDesaFromServer(listKecamatan.get(position).get(Db.TABLE_PROVINSI_ID));
                }
            } else if (viewId == R.id.activity_tambah_peternak_SpinnerDesa) {
                selectedDesa = position;
            //} else if (viewId == R.id.activity_tambah_peternak_SpinnerPernikahan) {
            //    selectedStatusPernikahan = position;
            } else if (viewId == R.id.activity_tambah_peternak_SpinnerGender) {
                selectedJenisKelamin = position;
            } else if (viewId == R.id.activity_tambah_peternak_SpinnerReligion) {
                selectedAgama = position;
            } else if (viewId == R.id.activity_tambah_peternak_SpinnerLevel) {
                selectedLevel = position;
            } else if (viewId == R.id.activity_tambah_peternak_SpinnerStatusPeternakan) {
                selectedStatusPeternakan = position;
            } else if (viewId == R.id.activity_tambah_peternak_SpinnerKelompok) {
                selectedKelompok = position;
                if (position == 2) {
                    frameSpinnerNamaKelompokTernak.setVisibility(View.VISIBLE);
//					loadListNamaKelompok();
                } else frameSpinnerNamaKelompokTernak.setVisibility(View.GONE);
            } else if (viewId == R.id.activity_tambah_peternak_SpinnerNamaKelompok) {
                selectedNamaKelompok = position;
            } else if (viewId == R.id.activity_tambah_peternak_SpinnerJenisKomoditas) {
                selectedJenisKomoditas = position;
            } else if (viewId == R.id.activity_tambah_peternak_SpinnerJenisUsaha) {
                selectedJenisUsaha = position;
            } else if (viewId == R.id.activity_tambah_peternak_SpinnerProdukPenjualan) {
                selectedProdukPenjualan = position;
                if (position == 4) {
                    frameEditTextNamaOlahan.setVisibility(View.VISIBLE);

                } else frameEditTextNamaOlahan.setVisibility(View.GONE);
            }

        }
    }
}


