package com.zam.sidik_padang.home.ppob.isipulsa;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.AppCompatSpinner;
import androidx.appcompat.widget.Toolbar;

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
import com.zam.sidik_padang.util.customclasses.SpinnerAdapter;
import com.zam.sidik_padang.util.customclasses.SpinnerListener;
import com.zam.sidik_padang.util.data.ApiHelper;
import com.zam.sidik_padang.util.data.DataHelper;
import com.zam.sidik_padang.util.data.ProdukPulsa;

public class IsiPulsaActivity extends BaseLogedinActivity {

    private final String VOLLEY_TAG = getClass().getName();
    private SpinnerAdapter adapterSpinnerJenisVoucher;
    private List<Object> listJenisVoucher;
    private int selectedProdukPulsa = 0;
    private AppCompatSpinner spinnerProdukPilihan;
    private SpinnerAdapter adapterSpinnerProdukPilihan;
    private List<Object> listProdukPilihan;
    private int selectedProdukPilihan = 0;
    private AppCompatSpinner spinnerNomorTransaksi;
    private List<Object> listNomorTransaksi;
    private SpinnerAdapter adapterSpinnerNomorTransaksi;
    private int nomorTransaksi = 0;
    private EditText edittextNomorHp, editTextPin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_isi_pulsa);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        listJenisVoucher = new ArrayList<>();
        listJenisVoucher.add(new ProdukPulsa("- pilih jenis voucher -"));
        listJenisVoucher.addAll(DataHelper.getDataHelper(IsiPulsaActivity.this).getAllProdukPulsa());
        adapterSpinnerJenisVoucher = new SpinnerAdapter(getResources(), (List<Object>) listJenisVoucher);
        AppCompatSpinner spinner = (AppCompatSpinner) findViewById(R.id.activity_isi_pulsa_SpinnerJenisVoucher);
        spinner.setAdapter(adapterSpinnerJenisVoucher);
        ApiHelper apiHelper = ApiHelper.getHelper(this);


        if (!Util.isInternetAvailible(this)) return;
        apiHelper.getJenisProdukPulsa(new ApiHelper.OnDoneListener() {

            @Override
            public void onDone(boolean success) {
                if (success) {
                    for (int i = listJenisVoucher.size() - 1; i > 0; i--)
                        listJenisVoucher.remove(i);
                    listJenisVoucher.addAll(DataHelper.getDataHelper(IsiPulsaActivity.this).getAllProdukPulsa());
                    adapterSpinnerJenisVoucher.notifyDataSetChanged();
                }
            }
        });
        spinnerProdukPilihan = (AppCompatSpinner) findViewById(R.id.activity_isi_pulsa_SpinnerProduk);
        listProdukPilihan = new ArrayList<>();
        listProdukPilihan.add(new ProdukPilihan());
        adapterSpinnerProdukPilihan = new SpinnerAdapter(getResources(), listProdukPilihan);
        spinnerProdukPilihan.setAdapter(adapterSpinnerProdukPilihan);
        edittextNomorHp = (EditText) findViewById(R.id.activity_isi_pulsa_EditTextNomorHp);
        editTextPin = (EditText) findViewById(R.id.activity_isi_pulsa_EditTextPin);
        spinnerNomorTransaksi = (AppCompatSpinner) findViewById(R.id.activity_isi_pulsa_SpinnerNomorTransaksi);
        listNomorTransaksi = new ArrayList<>();
        listNomorTransaksi.add("1");
        listNomorTransaksi.add("2");
        listNomorTransaksi.add("3");
        listNomorTransaksi.add("4");
        adapterSpinnerNomorTransaksi = new SpinnerAdapter(getResources(), listNomorTransaksi);
        spinnerNomorTransaksi.setAdapter(adapterSpinnerNomorTransaksi);

        spinnerNomorTransaksi.setOnItemSelectedListener(new MySpinnerListener(spinnerNomorTransaksi.getId()));
        spinnerProdukPilihan.setOnItemSelectedListener(new MySpinnerListener(spinnerProdukPilihan.getId()));

        spinner.setOnItemSelectedListener(new MySpinnerListener(R.id.activity_isi_pulsa_SpinnerJenisVoucher));
        edittextNomorHp.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence p1, int p2, int p3, int p4) {
                // TODO: Implement this method
            }

            @Override
            public void onTextChanged(CharSequence p1, int p2, int p3, int p4) {
                // TODO: Implement this method
            }

            @Override
            public void afterTextChanged(Editable p1) {
                String hp = p1.toString().trim();
                spinnerNomorTransaksi.setEnabled(hp.length() > 10);
                editTextPin.setEnabled(hp.length() > 10);
            }
        });

        findViewById(R.id.activity_isi_pulsa_ButtonBeli).setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View p1) {
                dialogKonfirmasi();
            }
        });
    }

    private void loadProdukPilihan() {
        if (selectedProdukPulsa == 0 || !Util.isInternetAvailible(this)) return;
        String url = Config.URL_PPOB + "userid=" + user.userid + "&aksi=2&code=" + ((ProdukPulsa) listJenisVoucher.get(selectedProdukPulsa)).code;
        debug(getClass(), "load produk pilihan url= " + url);
        final View pb = findViewById(R.id.activity_isi_pulsa_ProgressbarspinnerProduk);
        pb.setVisibility(View.VISIBLE);
        VolleyStringRequest request = new VolleyStringRequest(url, new VolleyStringRequest.Callback() {

            @Override
            public void onResponse(JsonObject jsonObject) {
                pb.setVisibility(View.INVISIBLE);
                debug(getClass(), "Load produk pilihan response= " + jsonObject);
                if (jsonObject.get("success").getAsBoolean()) {
                    for (int i = listProdukPilihan.size() - 1; i > 0; i--)
                        listProdukPilihan.remove(i);
                    JsonArray jsonArray = jsonObject.get("produk_pilihan").getAsJsonArray();
                    Gson gson = new Gson();
                    for (JsonElement element : jsonArray) {
                        listProdukPilihan.add(gson.fromJson(element, ProdukPilihan.class));
                    }
                    adapterSpinnerProdukPilihan.notifyDataSetChanged();
                }
            }
        });
        request.setTag(VOLLEY_TAG);
        VolleySingleton.getInstance(this).getRequestQueue().add(request);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (user != null) {
            ActionBar ab = getSupportActionBar();
            if (ab != null) ab.setSubtitle(user.nama);
        }
    }

    private void dialogKonfirmasi() {
        if (!editTextPin.isEnabled()) return;
        new AlertDialog.Builder(this)
                .setTitle("Pembelian")
                .setMessage("Pastikan nomor HP sudah benar" +
                        "\n\n" + listProdukPilihan.get(selectedProdukPilihan).toString() +
                        "\n\nHP: " + edittextNomorHp.getText().toString())
                .setPositiveButton("Beli", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface p1, int p2) {
                        prosesPembelian();
                    }
                })
                .show();
    }

    @Override
    protected void onDestroy() {
        VolleySingleton.getInstance(this).getRequestQueue().cancelAll(VOLLEY_TAG);
        super.onDestroy();
    }

    private void prosesPembelian() {
        if (!Util.isInternetAvailible(this)) {
            Util.noInternetDialog(this);
            return;
        }
        String url = Config.URL_PROSES +
                "aksi=1&userid=" + user.userid +
                "&token=" + editTextPin.getText().toString().trim() +
                "&kode=" + ((ProdukPulsa) listJenisVoucher.get(selectedProdukPulsa)).code +
                "&kodetrx=" + ((ProdukPilihan) listProdukPilihan.get(selectedProdukPilihan)).codetrx +
                "&trxke=" + (nomorTransaksi + 1);
        String hp = edittextNomorHp.getText().toString().trim();
        hp = hp.replaceAll(" ", "");
        hp = hp.replaceAll("-", "");
        url += "&hp=" + hp;
		/*
		 http://bungara.com/api/proses.php?userid=king&token=2410&aksi=1&kode=S&kodetrx=s10&hp=085276940070
		*/
        editTextPin.setText("");
        debug(getClass(), "Proses beli pulsa url= " + url);
        final Dialog d = ProgressDialog.show(this, null, "Harap tunggu", true, false);
        VolleyStringRequest request = new VolleyStringRequest(url, new VolleyStringRequest.Callback() {

            @Override
            public void onResponse(final JsonObject jsonObject) {
                d.dismiss();
                debug(getClass(), "Proses pembelian response= " + jsonObject);
                if (isResummed) new AlertDialog.Builder(IsiPulsaActivity.this)
                        .setMessage(jsonObject.get("message").getAsString()).setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                if (jsonObject.get("success").getAsBoolean()) finish();
                            }
                        }).show();

            }
        });
        request.setTag(VOLLEY_TAG);
        VolleySingleton.getInstance(this).getRequestQueue().add(request);

    }

    private class MySpinnerListener extends SpinnerListener {
        int viewId;

        public MySpinnerListener(int viewId) {
            this.viewId = viewId;
        }

        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            if (viewId == R.id.activity_isi_pulsa_SpinnerJenisVoucher) {
                selectedProdukPulsa = position;
                if (selectedProdukPulsa == 0) {
                    spinnerProdukPilihan.setSelection(0);
                    spinnerProdukPilihan.setEnabled(false);
                } else {
                    spinnerProdukPilihan.setSelection(0, true);
                    for (int i = listProdukPilihan.size() - 1; i > 0; i--)
                        listProdukPilihan.remove(i);
                    adapterSpinnerProdukPilihan.notifyDataSetChanged();
                    spinnerProdukPilihan.setEnabled(true);
                    loadProdukPilihan();
                }
            } else if (viewId == R.id.activity_isi_pulsa_SpinnerProduk) {
                selectedProdukPilihan = position;
                if (selectedProdukPulsa == 0) {
                    edittextNomorHp.setEnabled(false);
                    edittextNomorHp.setText("");
                } else {
                    edittextNomorHp.setEnabled(true);
                    edittextNomorHp.requestFocus();
                }
            } else if (viewId == R.id.activity_isi_pulsa_SpinnerNomorTransaksi) {
                nomorTransaksi = position;
            }
        }


    }

}
