package com.zam.sidik_padang.home.dataternak.dibawahnya;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.AppCompatSpinner;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProvider;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import com.zam.sidik_padang.BaseLogedinActivity;
import com.zam.sidik_padang.R;
import com.zam.sidik_padang.api.State;
import com.zam.sidik_padang.home.dataternak.dibawahnya.vm.TambahViewModel;
import com.zam.sidik_padang.home.dataternak.dibawahnya.vm.bangsa.BangsaKambing;
import com.zam.sidik_padang.home.dataternak.dibawahnya.vm.bangsa.BangsaResponse;
import com.zam.sidik_padang.home.dataternak.dibawahnya.vm.bangsa.BangsaSapi;
import com.zam.sidik_padang.home.dataternak.dibawahnya.vm.gabungan.Data;
import com.zam.sidik_padang.home.dataternak.dibawahnya.vm.gabungan.GabunganResponse;
import com.zam.sidik_padang.home.dataternak.dibawahnya.vm.gabungan.JenisKambing;
import com.zam.sidik_padang.home.dataternak.dibawahnya.vm.gabungan.JenisKelamin;
import com.zam.sidik_padang.home.dataternak.dibawahnya.vm.gabungan.JenisSapi;
import com.zam.sidik_padang.home.dataternak.dibawahnya.vm.gabungan.MetodeKelahiran;
import com.zam.sidik_padang.home.dataternak.dibawahnya.vm.gabungan.NamaTernak;
import com.zam.sidik_padang.home.dataternak.dibawahnya.vm.induk.DataInduk;
import com.zam.sidik_padang.home.dataternak.dibawahnya.vm.induk.IndukResponse;
import com.zam.sidik_padang.home.dataternak.scanner.ScanDialogFragment;
import com.zam.sidik_padang.home.sklb.petugas.pemilikternak.PemilikTernakListActivity;
import com.zam.sidik_padang.home.tambahpeternak.SpinnerAdapter;
import com.zam.sidik_padang.util.Config;
import com.zam.sidik_padang.util.Util;
import com.zam.sidik_padang.util.customclasses.SpinnerListener;
import io.paperdb.Paper;

public class TambahDataTernakActivity
        extends BaseLogedinActivity
        implements View.OnClickListener, DatePickerDialog.OnDateSetListener, ScanDialogFragment.ScanListener {

    private static final int SCAN_MODE_INDUK = 1, SCAN_MODE_BAPAK = 2;
    private final String pref_saved_induk = "saved_induk_list";
    private final String pref_saved_bapak = "saved_bapak_list";

    private AppCompatSpinner spinnerJenisTernak, spinnerBangsaTernak;
    private View progressbarSpinnerNamaTernak, progressbarSpinnerJenisTernak, progressbarSpinnerBangsaTernak,
            progressbarSpinnerJenisKelamin, progressbarSpinnerPerkawinan, progressbarSpinnerTipeKelahiran,
            progressbarIndukList, progressbarBapakList;
    private View layoutIdKembaran, layoutEdittextRumpun, layoutSpinnerRumpun;
    private SpinnerAdapter adapterSpinnerNamaTernak, adapterSpinnerJenisTernak, adapterSpinnerBangsaTernak,
            adapterSpinnerJenisKelamin, adapterSpinnerMetodePerkawinan, adapterSpinnerTipeKelahiran;
    private EditText editTextTempatKelahiranTernak, editTextTanggalLahirTernk, editTextAsalTernak,
            editTextEartek, editTextPanjangLahir, editTextTinggiLahir, editTextLinkgkarDadaLahir,
            editTextIdKembaran, editTextBbLahir, editTextBbInduk, editTextRumpun;
    private TextView tvPemilikTernak;
    private AutoCompleteTextView autoCompliteTextViewIdInduk, autoCompleteTextViewBapak;
    private DataTernakArrayAdapter indukListAdapter, bapakListAdapter;
    private List<DataInduk> indukList, bapakList;
    private DataInduk indukDataTernak, bapakDataTernak;
    private Data dataGabungan;
    private List<Map<String, String>> listNamaTernak, listJenisTernak, listBangsaTernak, listJenisKelamin,
            listMetodePerkawinan, listTipeKelahiran;
    private int selectedNamaTernak = 0, selectedJenisTernak = 0, selectedBangsaTernak = 0, selectedJenisKelamin = 0,
            selectedMetodePerkawinan = 0, selectedTipeKelahiran = 0;
    private Calendar birthDayCalendar;
    private boolean dialogErrorNotLoaded = false;
    private boolean rumpunTidakAda = false;
    private int scanMode = SCAN_MODE_INDUK;
    private String kodePemilikTernak = "0";

    private TambahViewModel viewModel;
    private ProgressDialog dialogSend;

    private void showPageNotLoadedErrorDialog() {
        if (dialogErrorNotLoaded) return;
        dialogErrorNotLoaded = true;
        new AlertDialog.Builder(this)
                .setMessage(R.string.please_reload_the_page)
                .setPositiveButton(android.R.string.ok,
                        (dialog, which) -> {
                            setResult(RESULT_CANCELED);
                            finish();
                        })
                .show();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putInt("scan_mode", scanMode);
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle s) {
        if (s != null && s.containsKey("scan_mode")) scanMode = s.getInt("scan_mode");
        super.onRestoreInstanceState(s);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tambah_data_ternak);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back);
        toolbar.setNavigationOnClickListener(view -> finish());
        setResult(RESULT_CANCELED);
        viewModel = new ViewModelProvider(this).get(TambahViewModel.class);

        editTextTempatKelahiranTernak = (EditText) findViewById(R.id.activity_tambah_data_ternak_EditTextTempatLahirTernak);
        editTextTanggalLahirTernk = (EditText) findViewById(R.id.activity_tambah_data_ternak_EditTextTanggalLahirTernak);
        editTextAsalTernak = (EditText) findViewById(R.id.activity_tambah_data_ternak_EditTextAsalTernak);
        editTextEartek = (EditText) findViewById(R.id.activity_tambah_data_ternak_EditTextEartek);
        editTextPanjangLahir = (EditText) findViewById(R.id.activity_tambah_data_ternak_EditTexPanjangLahir);
        editTextTinggiLahir = (EditText) findViewById(R.id.activity_tambah_data_ternak_EditTexTinggiLahir);
        editTextLinkgkarDadaLahir = (EditText) findViewById(R.id.activity_tambah_data_ternak_EditTexLingkardadaLahir);
        editTextBbLahir = (EditText) findViewById(R.id.activity_tambah_data_ternak_EditTextBeratLahir);
        editTextBbInduk = (EditText) findViewById(R.id.activity_tambah_data_ternak_EditTextBeratInduk);
        editTextIdKembaran = (EditText) findViewById(R.id.activity_tambah_data_ternak_EditTextIdKembaran);
        autoCompliteTextViewIdInduk = (AutoCompleteTextView) findViewById(R.id.activity_tambah_data_ternak_EditTextIdInduk);
        autoCompleteTextViewBapak = (AutoCompleteTextView) findViewById(R.id.activity_tambah_data_ternak_EditTextIdBapak);
        editTextRumpun = (EditText) findViewById(R.id.activity_tambah_data_ternak_EditTextRumpun);
        layoutIdKembaran = findViewById(R.id.activity_tambah_data_ternak_layoutIdKembaran);
        layoutEdittextRumpun = findViewById(R.id.layoutEditTextRumpun);
        layoutSpinnerRumpun = findViewById(R.id.layoutSpinnerRumpun);
        layoutSpinnerRumpun.setVisibility(View.GONE);
        editTextTanggalLahirTernk.setOnClickListener(this);
        findViewById(R.id.activity_tambah_data_ternak_ButtonSimpan).setOnClickListener(this);
        tvPemilikTernak = findViewById(R.id.tvPemilikTernak);
        findViewById(R.id.viewPemilikTernak).setOnClickListener(this);


        AppCompatSpinner spinnerNamaTernak = (AppCompatSpinner) findViewById(R.id.activity_tambah_data_ternak_SpinnerNamaTernak);
        spinnerJenisTernak = (AppCompatSpinner) findViewById(R.id.activity_tambah_data_ternak_SpinnerJenisTernak);
        spinnerBangsaTernak = (AppCompatSpinner) findViewById(R.id.activity_tambah_data_ternak_SpinnerBangsaTernak);
        AppCompatSpinner spinnerJenisKelamin = (AppCompatSpinner) findViewById(R.id.activity_tambah_data_ternak_SpinnerJenisKelaminTernak);
        AppCompatSpinner spinnerMetodePerkawinan = (AppCompatSpinner) findViewById(R.id.activity_tambah_data_ternak_SpinnerHasilMetodePerkawinan);
        AppCompatSpinner spinnerTipeKelahiran = (AppCompatSpinner) findViewById(R.id.activity_tambah_data_ternak_SpinnerTipeKelahiran);

        spinnerJenisTernak.setEnabled(false);
        spinnerBangsaTernak.setEnabled(false);

        findViewById(R.id.imageViewScanBarcodeInduk).setOnClickListener(this);
        findViewById(R.id.imageViewScanBarcodeBapak).setOnClickListener(this);

        progressbarSpinnerNamaTernak = findViewById(R.id.activity_tambah_data_ternak_ProgressbarspinnerNamaTernak);
        progressbarSpinnerJenisTernak = findViewById(R.id.activity_tambah_data_ternak_ProgressbarspinnerJenisTernak);
        progressbarSpinnerBangsaTernak = findViewById(R.id.activity_tambah_data_ternak_ProgressbarspinnerBangsaTernak);
        progressbarSpinnerJenisKelamin = findViewById(R.id.activity_tambah_data_ternak_ProgressbarspinnerJenisKelaminTernak);
        progressbarSpinnerPerkawinan = findViewById(R.id.activity_tambah_data_ternak_ProgressbarspinnerHasilMetodePerkawinan);
        progressbarSpinnerTipeKelahiran = findViewById(R.id.activity_tambah_data_ternak_ProgressbarspinnerTipeKelahiran);
        progressbarIndukList = findViewById(R.id.activity_tambah_data_ternak_ProgressIndukList);
        progressbarBapakList = findViewById(R.id.activity_tambah_data_ternak_ProgressBapakList);


        listNamaTernak = new ArrayList<>();
        Map<String, String> map = new HashMap<>();
        map.put("id", "-1");
        map.put("nama", getString(R.string._pilih_kategori_ternak_));
        listNamaTernak.add(map);
        adapterSpinnerNamaTernak = new SpinnerAdapter(this, listNamaTernak);
        spinnerNamaTernak.setAdapter(adapterSpinnerNamaTernak);
        spinnerNamaTernak.setOnItemSelectedListener(new CustomSpinnerListener(spinnerNamaTernak.getId()));

        listJenisTernak = new ArrayList<>();
        map = new HashMap<>();
        map.put("id", "-1");
        map.put("nama", getString(R.string._select_jenis_ternak_));
        listJenisTernak.add(map);
        adapterSpinnerJenisTernak = new SpinnerAdapter(this, listJenisTernak);
        spinnerJenisTernak.setAdapter(adapterSpinnerJenisTernak);
        spinnerJenisTernak.setOnItemSelectedListener(new CustomSpinnerListener(spinnerJenisTernak.getId()));

        indukList = new ArrayList<>();
        indukListAdapter = new DataTernakArrayAdapter(this, indukList);
        autoCompliteTextViewIdInduk.setAdapter(indukListAdapter);
        autoCompliteTextViewIdInduk.setThreshold(1);
        autoCompliteTextViewIdInduk.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                checkIdIndukExist();
                setTextRumpun();
            }
        });

        bapakList = new ArrayList<>();
        bapakListAdapter = new DataTernakArrayAdapter(this, bapakList);
        autoCompleteTextViewBapak.setAdapter(bapakListAdapter);
        autoCompleteTextViewBapak.setThreshold(1);
        autoCompleteTextViewBapak.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                checkIdBapakExist();
                setTextRumpun();
            }
        });


        listBangsaTernak = new ArrayList<>();
        map = new HashMap<>();
        map.put("id", "-1");
        map.put("nama", getString(R.string._select_bangsa_ternak_));
        listBangsaTernak.add(map);
        adapterSpinnerBangsaTernak = new SpinnerAdapter(this, listBangsaTernak);
        spinnerBangsaTernak.setAdapter(adapterSpinnerBangsaTernak);
        spinnerBangsaTernak.setOnItemSelectedListener(new CustomSpinnerListener(spinnerBangsaTernak.getId()));

        listJenisKelamin = new ArrayList<>();
        map = new HashMap<>();
        map.put("id", "-1");
        map.put("nama", getString(R.string._select_jenis_kelamin_ternak_));
        listJenisKelamin.add(map);
        adapterSpinnerJenisKelamin = new SpinnerAdapter(this, listJenisKelamin);
        spinnerJenisKelamin.setAdapter(adapterSpinnerJenisKelamin);
        spinnerJenisKelamin.setOnItemSelectedListener(new CustomSpinnerListener(spinnerJenisKelamin.getId()));

        listMetodePerkawinan = new ArrayList<>();
        map = new HashMap<>();
        map.put("id", "-1");
        map.put("nama", getString(R.string._select_metode_perkawinan_));
        listMetodePerkawinan.add(map);
        adapterSpinnerMetodePerkawinan = new SpinnerAdapter(this, listMetodePerkawinan);
        spinnerMetodePerkawinan.setAdapter(adapterSpinnerMetodePerkawinan);
        spinnerMetodePerkawinan.setOnItemSelectedListener(new CustomSpinnerListener(spinnerMetodePerkawinan.getId()));

        listTipeKelahiran = new ArrayList<>();
        map = new HashMap<>();
        map.put("id", "-1");
        map.put("nama", getString(R.string._select_tipe_kelahiran_));
        listTipeKelahiran.add(map);
        map = new HashMap<>();
        map.put("id", "1");
        map.put("nama", "Tunggal");
        listTipeKelahiran.add(map);
        map = new HashMap<>();
        map.put("id", "2");
        map.put("nama", "Kembar");
        listTipeKelahiran.add(map);
        adapterSpinnerTipeKelahiran = new SpinnerAdapter(this, listTipeKelahiran);
        spinnerTipeKelahiran.setAdapter(adapterSpinnerTipeKelahiran);
        spinnerTipeKelahiran.setOnItemSelectedListener(new CustomSpinnerListener(spinnerTipeKelahiran.getId()));

        observe();
    }

    private void observe() {
        viewModel.getResponseGabungan().observe(this, gabunganResponse -> {
            if (gabunganResponse!=null) {
                if (gabunganResponse.state == State.LOADING) {
                    loadingGabungan(gabunganResponse.message);
                } else if (gabunganResponse.state == State.SUCCESS) {
                    onSuccessGabungan(gabunganResponse.data);
                } else {
                    onErrorGabungan(gabunganResponse.message);
                }
            }
        });

        viewModel.getResponseBangsa().observe(this, indukResponse -> {
            if (indukResponse!=null) {
                if (indukResponse.state == State.LOADING) {
                    loadingBangsa(indukResponse.message);
                } else if (indukResponse.state == State.SUCCESS && indukResponse.data != null) {
                    onSuccessBangsa(indukResponse.data);
                } else {
                    onErrorBangsa(indukResponse.message);
                }
            }
        });

        viewModel.getResponseInduk().observe(this, indukResponse -> {
            if (indukResponse!=null) {
                if (indukResponse.state == State.SUCCESS && indukResponse.data != null) {
                    onSuccessInduk(indukResponse.data);
                }
            }
        });

        viewModel.getResponseBapak().observe(this, indukResponse -> {
            if (indukResponse!=null) {
                if (indukResponse.state == State.SUCCESS && indukResponse.data != null) {
                    onSuccessBapak(indukResponse.data);
                }
            }
        });

        viewModel.getResponseSend().observe(this, baseResponse -> {
            if (baseResponse != null) {
                if (baseResponse.state == State.LOADING) {
                    onStartSend(baseResponse.message);
                } else if (baseResponse.state == State.SUCCESS) {
                    onSuccessSend(baseResponse.message);
                } else {
                    onErrorSend(baseResponse.message);
                }
            }
        });

        //loadListGabungan();
        String url = Config.URL_DATA_TERNAK;
        url += "?aksi=0&userid=" + user.userid;
        debug(getClass(), "Load data ternak gabungan from server url: " + url);
        viewModel.getGabungan(url);

        loadIndukList();
        loadBapakList();
    }

    private void setTextRumpun() {
        new Thread() {
            @Override
            public void run() {
                final String idInduk = autoCompliteTextViewIdInduk.getText().toString().trim();
                final String idBapak = autoCompleteTextViewBapak.getText().toString().trim();
                indukDataTernak = null;
                bapakDataTernak = null;
                for (DataInduk d : indukList)
                    if (d.getIdTernak().equalsIgnoreCase(idInduk)) {
                        indukDataTernak = d;
                        break;
                    }
                for (DataInduk d : bapakList)
                    if (d.getIdTernak().equalsIgnoreCase(idBapak)) {
                        bapakDataTernak = d;
                        break;
                    }

                String s = "";
                debug(getClass(), "setRumpun induk " + indukDataTernak + " bapak: " + bapakDataTernak);
                if (indukDataTernak != null && bapakDataTernak != null) {
                    s = bapakDataTernak.getBangsa() + " X " + indukDataTernak.getBangsa();
                } else if (indukDataTernak != null) {
                    s = indukDataTernak.getBangsa();
                } else if (bapakDataTernak != null) {
                    s = bapakDataTernak.getBangsa();
                }
                final String sFinal = s;
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        rumpunTidakAda = indukDataTernak == null && bapakDataTernak == null && !idInduk.isEmpty() && !idBapak.isEmpty();
                        if (rumpunTidakAda) {
                            layoutEdittextRumpun.setVisibility(View.GONE);
                            layoutSpinnerRumpun.setVisibility(View.VISIBLE);
                        } else {
                            editTextRumpun.setText(sFinal);
                            layoutEdittextRumpun.setVisibility(View.VISIBLE);
                            layoutSpinnerRumpun.setVisibility(View.GONE);
                        }
                        spinnerBangsaTernak.setEnabled(rumpunTidakAda);

                    }
                });
            }
        }.start();
    }

    private void checkIdBapakExist() {
        final String s = autoCompleteTextViewBapak.getText().toString().trim();
        if (s.isEmpty()) return;
        new Thread() {
            @Override
            public void run() {
                int total = 0;
                DataInduk d = null;
                for (int i = 0; i < bapakList.size(); i++) {
                    d = bapakList.get(i);
                    if (d.toString().startsWith(s)) {
                        total++;
                    }
                    if (total > 1) break;
                }

                final int finalTotal = total;
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (finalTotal == 0) autoCompleteTextViewBapak.setTextColor(Color.RED);
                        else if (finalTotal > 1)
                            autoCompleteTextViewBapak.setTextColor(Color.BLACK);
                        else {
                            autoCompleteTextViewBapak.setTextColor(Color.parseColor("#238A45"));

                        }
                    }
                });
            }
        }.start();
    }

    private void checkIdIndukExist() {
        final String s = autoCompliteTextViewIdInduk.getText().toString().trim();
        if (s.isEmpty()) return;
        new Thread() {
            @Override
            public void run() {
                int total = 0;
                DataInduk d = null;
                for (int i = 0; i < indukList.size(); i++) {
                    d = indukList.get(i);
                    if (d.toString().startsWith(s)) {
                        total++;
                    }
                    if (total > 1) break;
                }
                final int finalTotal = total;
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (finalTotal == 0) autoCompliteTextViewIdInduk.setTextColor(Color.RED);
                        else if (finalTotal > 1)
                            autoCompliteTextViewIdInduk.setTextColor(Color.BLACK);
                        else {
                            autoCompliteTextViewIdInduk.setTextColor(Color.parseColor("#238A45"));
                        }
                    }
                });
            }
        }.start();
    }

    private void loadingGabungan(String message) {
        progressbarSpinnerNamaTernak.setVisibility(View.VISIBLE);
        progressbarSpinnerJenisKelamin.setVisibility(View.VISIBLE);
        progressbarSpinnerTipeKelahiran.setVisibility(View.VISIBLE);
        progressbarSpinnerPerkawinan.setVisibility(View.VISIBLE);
    }

    private void onErrorGabungan(String message) {
        progressbarSpinnerNamaTernak.setVisibility(View.INVISIBLE);
        progressbarSpinnerJenisKelamin.setVisibility(View.INVISIBLE);
        progressbarSpinnerTipeKelahiran.setVisibility(View.INVISIBLE);
        progressbarSpinnerPerkawinan.setVisibility(View.INVISIBLE);

        debug(getClass(), "error_message: " + message);
        if (isResummed) showPageNotLoadedErrorDialog();
    }

    private void onSuccessGabungan(GabunganResponse response) {
        dataGabungan = response.getData();
        if (dataGabungan == null)  return;

        Map<String, String> map;
        for (NamaTernak namaTernak : dataGabungan.getNamaTernak()) {
            if (namaTernak == null) continue;
            map = new HashMap<>();
            map.put("id", namaTernak.getId());
            map.put("nama", namaTernak.getNama());
            listNamaTernak.add(map);
        }
        adapterSpinnerNamaTernak.notifyDataSetChanged();

        for (JenisKelamin jenisKelamin : dataGabungan.getJenisKelamin()) {
            if (jenisKelamin == null) continue;
            map = new HashMap<>();
            map.put("id", jenisKelamin.getIdKelamin());
            map.put("nama", jenisKelamin.getKelamin());
            listJenisKelamin.add(map);
        }
        adapterSpinnerJenisKelamin.notifyDataSetChanged();

        for (MetodeKelahiran metodeKelahiran : dataGabungan.getMetodeKelahiran()) {
            if (metodeKelahiran == null) continue;
            map = new HashMap<>();
            map.put("id", metodeKelahiran.getIdHasil());
            map.put("nama", metodeKelahiran.getNamaHasil());
            listMetodePerkawinan.add(map);
        }
        adapterSpinnerMetodePerkawinan.notifyDataSetChanged();

        progressbarSpinnerNamaTernak.setVisibility(View.INVISIBLE);
        progressbarSpinnerJenisKelamin.setVisibility(View.INVISIBLE);
        progressbarSpinnerTipeKelahiran.setVisibility(View.INVISIBLE);
        progressbarSpinnerPerkawinan.setVisibility(View.INVISIBLE);
    }

    private void loadIndukList() {
        IndukResponse indukResponse = Paper.book().read(pref_saved_induk);
        if (indukResponse!=null && indukResponse.getDataInduk() != null) {
            indukList.clear();
            indukList.addAll(indukResponse.getDataInduk());
            indukListAdapter.notifyDataSetChanged();
        } else if (Util.isInternetAvailible(this)) {
            progressbarIndukList.setVisibility(View.VISIBLE);
            String link = Config.URL_LIHAT_DATA_TERNAK + "?aksi=16&kelamin=2&userid=" + user.userid;
            debug(getClass(), "Load induk list. Link: " + link);
            viewModel.getInduk(link);
        }

        /*
        //String saved = sharedPreferences.getString(pref_saved, "");
        if (!saved.isEmpty() && saved.startsWith("{")) {
            IndukListApiResponse response = new Gson().fromJson(saved, IndukListApiResponse.class);
            indukList.clear();
            indukList.addAll(response.data_induk);
            indukListAdapter.notifyDataSetChanged();
        } else if (Util.isInternetAvailible(this)) {
            progressbarIndukList.setVisibility(View.VISIBLE);
        } else return;

        String link = Config.URL_LIHAT_DATA_TERNAK + "?aksi=16&kelamin=2&userid=" + user.userid;
        debug(getClass(), "Load induk list. Link: " + link);
        viewModel.getInduk(link);

        AndroidNetworking.get(link).setTag(VOLLEY_TAG).build().getAsString(new MyStringRequestListener() {
            @Override
            protected void response(String response) {
                debug(getClass(), "Load induk list response: " + response);
                progressbarIndukList.setVisibility(View.INVISIBLE);
                IndukListApiResponse apiResponse = new Gson().fromJson(response, IndukListApiResponse.class);
                if (apiResponse.success) {
                    sharedPreferences.edit().putString(pref_saved, response).apply();
                    indukList.clear();
                    indukList.addAll(apiResponse.data_induk);
                    indukListAdapter.notifyDataSetChanged();
                }
            }
        });
        debug(getClass(), " indukListAdapter size: " + indukListAdapter.getCount());
         */
    }

    private void onSuccessInduk(IndukResponse data) {
        debug(getClass(), "Load induk list response: " + data.toString());
        progressbarIndukList.setVisibility(View.INVISIBLE);
        //IndukListApiResponse apiResponse = new Gson().fromJson(response, IndukListApiResponse.class);
        //sharedPreferences.edit().putString(pref_saved, response).apply();
        Paper.book().write(pref_saved_induk, data);
        indukList.clear();
        indukList.addAll(data.getDataInduk());
        indukListAdapter.notifyDataSetChanged();
    }

    private void loadBapakList() {
        IndukResponse indukResponse = Paper.book().read(pref_saved_bapak);
        if (indukResponse != null && indukResponse.getDataInduk() != null) {
            bapakList.clear();
            bapakList.addAll(indukResponse.getDataInduk());
            bapakListAdapter.notifyDataSetChanged();
        } else if (Util.isInternetAvailible(this)) {
            progressbarBapakList.setVisibility(View.VISIBLE);
            String link = Config.URL_LIHAT_DATA_TERNAK + "?aksi=16&kelamin=1&userid=" + user.userid;
            debug(getClass(), "Load bapak list. Link: " + link);
            viewModel.getBapak(link);
        }
        /*
        //String saved = sharedPreferences.getString(pref_saved, "");
        if (!saved.isEmpty() && saved.startsWith("{")) {
            IndukListApiResponse response = new Gson().fromJson(saved, IndukListApiResponse.class);
            bapakList.clear();
            bapakList.addAll(response.data_induk);
            bapakListAdapter.notifyDataSetChanged();
        } else if (Util.isInternetAvailible(this)) {
            progressbarBapakList.setVisibility(View.VISIBLE);
        } else return;

        /*
        String link = Config.URL_LIHAT_DATA_TERNAK + "?aksi=16&kelamin=1&userid=" + user.userid;
        debug(getClass(), "Load bapak list. Link: " + link);
        viewModel.getBapak(link);

        AndroidNetworking.get(link).setTag(VOLLEY_TAG).build().getAsString(new MyStringRequestListener() {
            @Override
            protected void response(String response) {
                debug(getClass(), "Load bapak list response: " + response);
                progressbarBapakList.setVisibility(View.INVISIBLE);
                IndukListApiResponse apiResponse = new Gson().fromJson(response, IndukListApiResponse.class);
                if (apiResponse.success) {
                    sharedPreferences.edit().putString(pref_saved, response).apply();
                    bapakList.clear();
                    bapakList.addAll(apiResponse.data_induk);
                    bapakListAdapter.notifyDataSetChanged();
                }
            }
        });
        debug(getClass(), " bapak ListAdapter size: " + indukListAdapter.getCount());
         */
    }

    private void onSuccessBapak(IndukResponse data) {
        progressbarBapakList.setVisibility(View.INVISIBLE);
        //IndukListApiResponse apiResponse = new Gson().fromJson(response, IndukListApiResponse.class);
        //sharedPreferences.edit().putString(pref_saved, response).apply();
        Paper.book().write(pref_saved_bapak, data);
        bapakList.clear();
        bapakList.addAll(data.getDataInduk());
        bapakListAdapter.notifyDataSetChanged();
    }

    /** TODO REMOVE */
    /*
    private void loadListGabungan() {
        progressbarSpinnerNamaTernak.setVisibility(View.VISIBLE);
        progressbarSpinnerJenisKelamin.setVisibility(View.VISIBLE);
        progressbarSpinnerTipeKelahiran.setVisibility(View.VISIBLE);
        progressbarSpinnerPerkawinan.setVisibility(View.VISIBLE);

        String url = Config.URL_DATA_TERNAK;
        url += "?aksi=0&userid=" + user.userid;
        debug(getClass(), "Load data ternak gabungan from server url: " + url);
        viewModel.getGabungan(url);
        VolleyStringRequest request = new VolleyStringRequest(url, new VolleyStringRequest.Callback() {
            @Override
            public void onResponse(JsonObject jsonObject) {
                progressbarSpinnerNamaTernak.setVisibility(View.INVISIBLE);
                progressbarSpinnerJenisKelamin.setVisibility(View.INVISIBLE);
                progressbarSpinnerTipeKelahiran.setVisibility(View.INVISIBLE);
                progressbarSpinnerPerkawinan.setVisibility(View.INVISIBLE);

                debug(getClass(), "Retrieve data ternak gabungan onresponse " + jsonObject);
                if (jsonObject.get("success").getAsBoolean()) {
                    JsonElement je = jsonObject.get("data");
                    if (je == null) return;
                    jsonObjectGabungan = je.getAsJsonObject();
                    JsonArray arrayNamaTernak = jsonObjectGabungan.getAsJsonArray("nama_ternak");
                    Map<String, String> map;
                    for (JsonElement element : arrayNamaTernak) {
                        if (element == null) continue;
                        JsonObject jo = element.getAsJsonObject();
                        map = new HashMap<>();
                        map.put(Db.TABLE_PROVINSI_ID, jo.get(Db.TABLE_PROVINSI_ID).getAsString());
                        map.put(Db.TABLE_PROVINSI_NAMA, jo.get("nama").getAsString());
                        listNamaTernak.add(map);
                    }
                    adapterSpinnerNamaTernak.notifyDataSetChanged();

                    JsonArray arrayJenisKelamin = jsonObjectGabungan.getAsJsonArray("jenis_kelamin");
                    for (JsonElement element : arrayJenisKelamin) {
                        if (element == null) continue;
                        JsonObject jo = element.getAsJsonObject();
                        map = new HashMap<>();
                        map.put(Db.TABLE_PROVINSI_ID, jo.get("id_kelamin").getAsString());
                        map.put(Db.TABLE_PROVINSI_NAMA, jo.get("kelamin").getAsString());
                        listJenisKelamin.add(map);
                    }
                    adapterSpinnerJenisKelamin.notifyDataSetChanged();

                    JsonArray arraymetodePerkawinan = jsonObjectGabungan.getAsJsonArray("metode_kelahiran");
                    for (JsonElement element : arraymetodePerkawinan) {
                        if (element == null) continue;
                        JsonObject jo = element.getAsJsonObject();
                        map = new HashMap<>();
                        map.put(Db.TABLE_PROVINSI_ID, jo.get("id_hasil").getAsString());
                        map.put(Db.TABLE_PROVINSI_NAMA, jo.get("nama_hasil").getAsString());
                        listMetodePerkawinan.add(map);
                    }
                    adapterSpinnerMetodePerkawinan.notifyDataSetChanged();

                } else {
                    debug(getClass(), "error_message: " + jsonObject.get("message").getAsString());
                    if (isResummed) showPageNotLoadedErrorDialog();
                }

            }
        });

        request.setTag(VOLLEY_TAG);
        VolleySingleton.getInstance(this).getRequestQueue().add(request);
    }
     */

    @Override
    public void onClick(View p1) {
        final int id = p1.getId();
        if (id == R.id.activity_tambah_data_ternak_EditTextTanggalLahirTernak) {
            Calendar maxCalendar = Calendar.getInstance();
            Calendar minCalendar = Calendar.getInstance();
            minCalendar.set(Calendar.YEAR, minCalendar.get(Calendar.YEAR) - 10);
//			CalendarDay calendarDay = birthDayCalendar == null ? CalendarDay.from(maxCalendar) : CalendarDay.from(birthDayCalendar);
            DatePickerDialog dpd = DatePickerDialog.newInstance(this);
            dpd.showYearPickerFirst(true);
            dpd.setMinDate(minCalendar);
            dpd.setMaxDate(maxCalendar);
            dpd.setVersion(DatePickerDialog.Version.VERSION_2);
            dpd.show(getSupportFragmentManager(), "date_dialog");
//			SelectDateDialogFragment.getDialog(calendarDay, minCalendar.getTimeInMillis(), maxCalendar.getTimeInMillis()).show(getSupportFragmentManager(), "date");

        } else if (id == R.id.imageViewScanBarcodeInduk || id == R.id.imageViewScanBarcodeBapak) {
            /*
            IntentIntegrator integrator = new IntentIntegrator(this);
            integrator.setBeepEnabled(true);
            integrator.setOrientationLocked(false);
            integrator.setDesiredBarcodeFormats(IntentIntegrator.ALL_CODE_TYPES);
            integrator.initiateScan();
             */
            scanMode = id == R.id.imageViewScanBarcodeInduk ? SCAN_MODE_INDUK : SCAN_MODE_BAPAK;
            ScanDialogFragment.newInstance(this).show(getSupportFragmentManager(), "dialog");
        } else if (id == R.id.viewPemilikTernak) {
            startActivityForResult(new Intent(this, PemilikTernakListActivity.class), 200);
        } else if (id == R.id.activity_tambah_data_ternak_ButtonSimpan) {
            validateAndGo();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 200) {
            if (resultCode == RESULT_OK) {
                String nama = data.getStringExtra("nama");
                kodePemilikTernak = data.getStringExtra("kode");
                tvPemilikTernak.setText(nama);
            }
        } else if (requestCode == IntentIntegrator.REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
                if (result != null) {
                    String barcodeResult = result.getContents();
                    debug(getClass(), "barCodeResult: " + result);
                    if (scanMode == SCAN_MODE_INDUK)
                        autoCompliteTextViewIdInduk.setText(barcodeResult);
                    else autoCompleteTextViewBapak.setText(barcodeResult);
                }
            } else super.onActivityResult(requestCode, resultCode, data);
        } else
            super.onActivityResult(requestCode, resultCode, data);
    }

//	@Override
//	public void onCalendarSelected(CalendarDay calendarDay) {
//		birthDayCalendar = calendarDay.getCalendar();
//		editTextTanggalLahirTernk.setText(new SimpleDateFormat("EEEE d MMM yyyy", Locale.getDefault()).format(birthDayCalendar.getTime()));
//	}

    @Override
    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
        if (birthDayCalendar == null) birthDayCalendar = Calendar.getInstance();
        birthDayCalendar.set(year, monthOfYear, dayOfMonth);
        editTextTanggalLahirTernk.setText(new SimpleDateFormat("EEEE d MMM yyyy", Locale.getDefault()).format(birthDayCalendar.getTime()));
    }

    private void validateAndGo() {
        Uri.Builder uriBuilder = new Uri.Builder();
        String idEartek = editTextEartek.getText().toString().trim();
        if (idEartek.isEmpty()) {
            editTextEartek.setError(getString(R.string.this_field_is_mandatory));
            editTextEartek.requestFocus();
            return;
        }

        if (idEartek.contains(" ")) idEartek = idEartek.replaceAll(" ", "");
        uriBuilder.appendQueryParameter("eartek", Util.toUrlEncoded(idEartek));
        if (selectedNamaTernak == 0) {
            Toast.makeText(this, R.string.please_select_nama_ternak, Toast.LENGTH_SHORT).show();
            return;
        }
        uriBuilder.appendQueryParameter("nama_ternak", listNamaTernak.get(selectedNamaTernak).get("id"));

        if (selectedJenisTernak == 0) {
            Toast.makeText(this, R.string.please_select_jenis_ternak, Toast.LENGTH_SHORT).show();
            return;
        }
        uriBuilder.appendQueryParameter("jenis_ternak", listJenisTernak.get(selectedJenisTernak).get("id"));


        if (rumpunTidakAda) {
            if (selectedBangsaTernak == 0) {
                Toast.makeText(this, R.string.please_select_bangsa_ternak, Toast.LENGTH_SHORT).show();
                return;
            }
            uriBuilder.appendQueryParameter("bangsa_ternak", listBangsaTernak.get(selectedBangsaTernak).get("id"));
        } else {
            String s = "";
            if (indukDataTernak != null && bapakDataTernak != null) {
                s = bapakDataTernak.getIdBangsa() + "x" + indukDataTernak.getIdBangsa();
            } else if (indukDataTernak != null) {
                s = indukDataTernak.getIdBangsa();
            } else if (bapakDataTernak != null) {
                s = bapakDataTernak.getIdBangsa();
            }

            uriBuilder.appendQueryParameter("bangsa_ternak", s);
        }


        if (selectedJenisKelamin == 0) {
            Toast.makeText(this, R.string.please_select_jenis_kelamin_ternak, Toast.LENGTH_SHORT).show();
            return;
        }
        uriBuilder.appendQueryParameter("jenis_kelamin", listJenisKelamin.get(selectedJenisKelamin).get("id"));

        String tempatLahir = editTextTempatKelahiranTernak.getText().toString().trim();
        if (tempatLahir.isEmpty()) {
            editTextTempatKelahiranTernak.setError(getString(R.string.this_field_is_mandatory));
            editTextTempatKelahiranTernak.requestFocus();
            return;
        }
        uriBuilder.appendQueryParameter("tempat_lahir", Util.toUrlEncoded(tempatLahir));

        if (birthDayCalendar == null) {
            Toast.makeText(this, R.string.please_select_birthday, Toast.LENGTH_SHORT).show();
            return;
        }
        uriBuilder.appendQueryParameter("tanggal_lahir", new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(birthDayCalendar.getTime()));

        String panjangLahir = editTextPanjangLahir.getText().toString().trim();
        if (!panjangLahir.isEmpty()) uriBuilder.appendQueryParameter("panjang_lahir", panjangLahir);

        String tinggiLahir = editTextTinggiLahir.getText().toString().trim();
        if (!tinggiLahir.isEmpty()) uriBuilder.appendQueryParameter("tinggi_lahir", tinggiLahir);

        String lingkarDadaLahir = editTextLinkgkarDadaLahir.getText().toString().trim();
        if (!lingkarDadaLahir.isEmpty())
            uriBuilder.appendQueryParameter("lingkar_dada_lahir", lingkarDadaLahir);

        String bbLahir = editTextBbLahir.getText().toString().trim();
        if (!bbLahir.isEmpty()) uriBuilder.appendQueryParameter("bb_lahir", bbLahir);

        String bbInduk = editTextBbInduk.getText().toString().trim();
        if (!bbInduk.isEmpty()) uriBuilder.appendQueryParameter("bb_induk", bbInduk);

        String idInduk = autoCompliteTextViewIdInduk.getText().toString().trim();
        if (!idInduk.isEmpty()) uriBuilder.appendQueryParameter("induk", idInduk);

        String idBapak = autoCompleteTextViewBapak.getText().toString().trim();
        if (!idBapak.isEmpty()) uriBuilder.appendQueryParameter("bapak", idBapak);

        if (selectedMetodePerkawinan == 0) {
            Toast.makeText(this, R.string.please_select_metode_perkawinan, Toast.LENGTH_SHORT).show();
            return;
        }
        uriBuilder.appendQueryParameter("metode_perkawinan", listMetodePerkawinan.get(selectedMetodePerkawinan).get("id"));

        if (selectedTipeKelahiran == 0) {
            Toast.makeText(this, "Pilih tipe kelahiran", Toast.LENGTH_SHORT).show();
            return;
        }
        uriBuilder.appendQueryParameter("tipe_kelahiran", listTipeKelahiran.get(selectedTipeKelahiran).get("id"));

        if (selectedTipeKelahiran == 2) {
            String idKembaran = editTextIdKembaran.getText().toString().trim();
            if (idKembaran.contains(" ")) idKembaran = idKembaran.replaceAll(" ", "");
            if (!idKembaran.isEmpty()) uriBuilder.appendQueryParameter("id_kembaran", idKembaran);
        }

        String asalTernak = editTextAsalTernak.getText().toString().trim();
        if (asalTernak.isEmpty()) {
            editTextAsalTernak.setError(getString(R.string.this_field_is_mandatory));
            editTextAsalTernak.requestFocus();
            //recreate();
            return;
        }
        uriBuilder.appendQueryParameter("asal_ternak", Util.toUrlEncoded(asalTernak));
        uriBuilder.appendQueryParameter("pemilik_ternak", kodePemilikTernak);

        uriBuilder.appendQueryParameter("userid", user.userid);
        uriBuilder.appendQueryParameter("aksi", "1");

        String url = Config.URL_LIHAT_DATA_TERNAK + uriBuilder.toString();
        debug(getClass(), "Validate and go. Url: " + url);
        viewModel.sendDataTernak(url);
        /*
        final Dialog dialog = ProgressDialog.show(this, null, "Mengirim data...", true, false);
        VolleyStringRequest request = new VolleyStringRequest(url, new VolleyStringRequest.Callback() {
            @Override
            public void onResponse(JsonObject jsonObject) {
                dialog.dismiss();
                debug(getClass(), "Tambah ternak response " + jsonObject);
                String message = jsonObject.get("message").getAsString();
                if (jsonObject.get("success").getAsBoolean()) {
                    new AlertDialog.Builder(TambahDataTernakActivity.this).setMessage(message)
                            .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {

                                @Override
                                public void onClick(DialogInterface p1, int p2) {
                                    setResult(RESULT_OK);
                                    finish();
                                }
                            })
                            .show();
                } else Util.showDialog(TambahDataTernakActivity.this, message);
            }
        });
        request.setTag(VOLLEY_TAG);
        VolleySingleton.getInstance(this).getRequestQueue().add(request);
         */
    }

    private void onStartSend(String message) {
        if (dialogSend != null && dialogSend.isShowing()) dialogSend.dismiss();
        dialogSend = ProgressDialog.show(this, null, "Mengirim data...", true, false);

    }

    private void onErrorSend(String message) {
        if (dialogSend != null && dialogSend.isShowing()) dialogSend.dismiss();
        Util.showDialog(TambahDataTernakActivity.this, message);
    }

    private void onSuccessSend(String message) {
        if (dialogSend != null && dialogSend.isShowing()) dialogSend.dismiss();
        new AlertDialog.Builder(TambahDataTernakActivity.this).setMessage(message)
                .setPositiveButton(android.R.string.ok, (p1, p2) -> {
                    setResult(RESULT_OK);
                    finish();
                })
                .show();
    }

    @Override
    protected void onDestroy() {
        //VolleySingleton.getInstance(this).getRequestQueue().cancelAll(VOLLEY_TAG);
        viewModel.cancelCall();
        super.onDestroy();
    }

    private void loadListBangsaTernak(String jenisId) {
        if (!Util.isInternetAvailible(this)) {
            Toast.makeText(this, R.string.no_internet_connection, Toast.LENGTH_SHORT).show();
            return;
        }
        int aksi = selectedNamaTernak == 1 ? 5 : 4;
        String url = Config.URL_DATA_TERNAK + "?aksi=" + aksi + "&id_jenis=" + jenisId + "&userid=" + user.userid;
        debug(getClass(), "Load bangsa ternak url: " + url);
        viewModel.getBangsa(url);
        /*
        VolleyStringRequest request = new VolleyStringRequest(url, new VolleyStringRequest.Callback() {

            @Override
            public void onResponse(JsonObject jsonObject) {
                progressbarSpinnerBangsaTernak.setVisibility(View.INVISIBLE);
                debug(getClass(), "Load bangsa ternak onresponse: " + jsonObject);
                if (!jsonObject.get("success").getAsBoolean()) {
                    if (!dialogErrorNotLoaded)
                        showPageNotLoadedErrorDialog();
                    return;
                }

                JsonArray array = jsonObject.getAsJsonArray(selectedNamaTernak == 1 ? "bangsa_sapi" : "bangsa_kambing");
                for (JsonElement je : array) {
                    JsonObject jo = je.getAsJsonObject();
                    Map<String, String> map = new HashMap<>();
                    map.put("id", jo.get("id_bangsa").getAsString());
                    map.put("nama", jo.get("keterangan").getAsString());
                    listBangsaTernak.add(map);
                }
                adapterSpinnerBangsaTernak.notifyDataSetChanged();
            }
        });
        request.setTag(VOLLEY_TAG);
        VolleySingleton.getInstance(this).getRequestQueue().add(request);
         */
    }

    private void onSuccessBangsa(BangsaResponse data) {
        progressbarSpinnerBangsaTernak.setVisibility(View.INVISIBLE);
        if (selectedNamaTernak == 1) {
            for (BangsaSapi bangsaSapi : data.getBangsaSapi()) {
                Map<String, String> map = new HashMap<>();
                map.put("id", bangsaSapi.getIdBangsa());
                map.put("nama", bangsaSapi.getKeterangan());
                listBangsaTernak.add(map);
            }
        } else {
            for (BangsaKambing bangsaSapi : data.getBangsaKambing()) {
                Map<String, String> map = new HashMap<>();
                map.put("id", bangsaSapi.getIdBangsa());
                map.put("nama", bangsaSapi.getKeterangan());
                listBangsaTernak.add(map);
            }
        }
        adapterSpinnerBangsaTernak.notifyDataSetChanged();
    }

    private void onErrorBangsa(String message) {
        progressbarSpinnerBangsaTernak.setVisibility(View.INVISIBLE);
        if (!dialogErrorNotLoaded) showPageNotLoadedErrorDialog();
    }

    private void loadingBangsa(String message) {
        progressbarSpinnerBangsaTernak.setVisibility(View.VISIBLE);
    }

    @Override
    public void scanCallback(String result) {
        if (result != null) {
            debug(getClass(), "barCodeResult: " + result);
            if (scanMode == SCAN_MODE_INDUK) autoCompliteTextViewIdInduk.setText(result);
            else autoCompleteTextViewBapak.setText(result);
        }
    }

    private class CustomSpinnerListener extends SpinnerListener {
        private final int viewId;

        public CustomSpinnerListener(int viewId) {
            this.viewId = viewId;
        }

        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            if (viewId == R.id.activity_tambah_data_ternak_SpinnerNamaTernak) {
                boolean berubah = selectedNamaTernak != position;
                if (!berubah) return;
                selectedNamaTernak = position;
                if (position == 0) {
                    spinnerJenisTernak.setSelection(0);
                    spinnerBangsaTernak.setSelection(0);
                    spinnerJenisTernak.setEnabled(false);
                    spinnerBangsaTernak.setEnabled(false);
                    selectedBangsaTernak = 0;
                    selectedJenisTernak = 0;
                } else {
                    spinnerJenisTernak.setEnabled(true);
                    spinnerJenisTernak.setSelection(0);
                    if (listJenisTernak.size() > 1) listJenisTernak.subList(1, listJenisTernak.size()).clear();
                    if (position == 1) {
                        for (JenisSapi jenis : dataGabungan.getJenisSapi()) {
                            if (jenis == null) continue;
                            Map<String, String> map = new HashMap<>();
                            map.put("id", jenis.getIdJenis());
                            map.put("nama", jenis.getKeterangan());
                            listJenisTernak.add(map);
                        }
                    } else {
                        for (JenisKambing jenis : dataGabungan.getJenisKambing()) {
                            if (jenis == null) continue;
                            Map<String, String> map = new HashMap<>();
                            map.put("id", jenis.getIdJenis());
                            map.put("nama", jenis.getKeterangan());
                            listJenisTernak.add(map);
                        }
                    }
                    /*
                    String arrayKey = position == 1 ? "jenis_sapi" : "jenis_kambing";
                    JsonArray array = jsonObjectGabungan.getAsJsonArray(arrayKey);
                    for (JsonElement je : array) {
                        if (je == null) continue;
                        JsonObject jo = je.getAsJsonObject();
                        Map<String, String> map = new HashMap<>();
                        map.put("id", jo.get("id_jenis").getAsString());
                        map.put("nama", jo.get("keterangan").getAsString());
                        listJenisTernak.add(map);
                    }
                     */
                    adapterSpinnerJenisTernak.notifyDataSetChanged();
                }
            } else if (viewId == R.id.activity_tambah_data_ternak_SpinnerJenisTernak) {
                boolean berubah = selectedJenisTernak != position;
                selectedJenisTernak = position;
                spinnerBangsaTernak.setEnabled(position != 0);
                if (position == 0) {
                    spinnerBangsaTernak.setSelection(0);
                    selectedBangsaTernak = 0;
                } else if (berubah) {
                    if (listBangsaTernak.size() > 1) listBangsaTernak.subList(1, listBangsaTernak.size()).clear();
                    adapterSpinnerBangsaTernak.notifyDataSetChanged();
                    loadListBangsaTernak(listJenisTernak.get(position).get("id"));
                }
            } else if (viewId == R.id.activity_tambah_data_ternak_SpinnerBangsaTernak) {
                selectedBangsaTernak = position;
            } else if (viewId == R.id.activity_tambah_data_ternak_SpinnerJenisKelaminTernak) {
                selectedJenisKelamin = position;
            } else if (viewId == R.id.activity_tambah_data_ternak_SpinnerHasilMetodePerkawinan) {
                selectedMetodePerkawinan = position;
            } else if (viewId == R.id.activity_tambah_data_ternak_SpinnerTipeKelahiran) {
                selectedTipeKelahiran = position;
                layoutIdKembaran.setVisibility(position == 2 ? View.VISIBLE : View.GONE);
            }
        }

    }
}
