package com.zam.sidik_padang.home.ppob.tokenpln;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.AppCompatSpinner;
import androidx.appcompat.widget.Toolbar;

import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.Arrays;
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

public class TokenPlnActivity extends BaseLogedinActivity {
    private final String VOLLEY_TAG = this.getClass().getName();
    private List<Object> listProdukPln;
    private int selectedProdukPln = 0;
    private AppCompatSpinner spinnerPilihProduk;
    private SpinnerAdapter adapterSpinnerPilihProduk;

    private EditText editTextIdPln, editTextHp, editTextPin;

    private View buttonBeli;

    private int nomorUrut = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO: Implement this method
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_token_pln);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View p1) {
                finish();
            }
        });

        listProdukPln = new ArrayList<>();
        listProdukPln.add("-pilih produk PLN-");
        listProdukPln.addAll(DataHelper.getDataHelper(this).getAllProdukPLN());
        adapterSpinnerPilihProduk = new SpinnerAdapter(getResources(), listProdukPln);
        spinnerPilihProduk = (AppCompatSpinner) findViewById(R.id.activity_token_pln_SpinnerPilihProduk);
        spinnerPilihProduk.setAdapter(adapterSpinnerPilihProduk);
        ApiHelper.getHelper(this).getProdukPLNs(new ApiHelper.OnDoneListener() {

            @Override
            public void onDone(boolean success) {
                if (success)
                    for (int i = listProdukPln.size() - 1; i > 0; i--) listProdukPln.remove(i);
                listProdukPln.addAll(DataHelper.getDataHelper(TokenPlnActivity.this).getAllProdukPLN());
                adapterSpinnerPilihProduk.notifyDataSetChanged();
            }
        });

        AppCompatSpinner spinnerNomorUrut = (AppCompatSpinner) findViewById(R.id.activity_token_pln_SpinnerNomorTransaksi);
        List<Object> nomors = new ArrayList<>();
        nomors.addAll(Arrays.asList(new String[]{"1", "2", "3", "4"}));
        spinnerNomorUrut.setAdapter(new SpinnerAdapter(getResources(), nomors));
        editTextIdPln = (EditText) findViewById(R.id.activity_token_pln_EditTextNomorIdPln);
        editTextHp = (EditText) findViewById(R.id.activity_token_pln_EditTextNomorHp);
        editTextPin = (EditText) findViewById(R.id.activity_token_pln_EditTextPin);
        buttonBeli = findViewById(R.id.activity_token_pln_ButtonBeli);
        buttonBeli.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View p1) {
                dialogKonfirmasi();
            }
        });

        spinnerPilihProduk.setOnItemSelectedListener(new MySpinnerListener(spinnerPilihProduk.getId()));
        spinnerNomorUrut.setOnItemSelectedListener(new MySpinnerListener(spinnerNomorUrut.getId()));
    }

    private void dialogKonfirmasi() {
        if (editTextIdPln.getText().toString().trim().isEmpty()) {
            editTextIdPln.setError("Harus diisi");
            editTextIdPln.requestFocus();
            return;
        }
        if (editTextPin.getText().toString().trim().isEmpty()) {
            editTextPin.setError("Harus diisi");
            editTextPin.requestFocus();
            return;
        }

        new AlertDialog.Builder(this)
                .setTitle("Pembelian")
                .setMessage("Pastikan nomor ID PLN sudah benar" +
                        "\n\n" + ((ProdukPLN) listProdukPln.get(selectedProdukPln)).toString() +
                        "\n\nID PLN: " + editTextIdPln.getText().toString().trim() +
                        "\n\nHP: " + editTextHp.getText().toString())
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
                "aksi=2&userid=" + user.userid +
                "&token=" + editTextPin.getText().toString().trim() +
                "&kode=pln" +
                "&kodetrx=" + ((ProdukPLN) listProdukPln.get(selectedProdukPln)).codetrx +
                "&idpel=" + editTextIdPln.getText().toString().trim() +
                "&trxke=" + (nomorUrut + 1);
        String hp = editTextHp.getText().toString().trim();
        hp = hp.replaceAll(" ", "");
        hp = hp.replaceAll("-", "");
        url += "&hp=" + hp;
		/*
		 http://bungara.com/api/proses.php?userid=king&token=2410&aksi=2&kode=PLN&kodetrx=PLN20&hp=085276940070&idpel=09876677766

		 */
        editTextPin.setText("");
        debug(getClass(), "Proses beli pln url= " + url);
        final Dialog d = ProgressDialog.show(this, null, "Harap tunggu", true, false);
        VolleyStringRequest request = new VolleyStringRequest(url, new VolleyStringRequest.Callback() {

            @Override
            public void onResponse(final JsonObject jsonObject) {
                d.dismiss();
                debug(getClass(), "Proses pembelian response= " + jsonObject);
                if (isResummed) new AlertDialog.Builder(TokenPlnActivity.this)
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
        private int viewId;

        public MySpinnerListener(int viewId) {
            this.viewId = viewId;
        }

        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            if (viewId == R.id.activity_token_pln_SpinnerPilihProduk) {
                selectedProdukPln = position;
                if (position == 0) {
                    editTextIdPln.setEnabled(false);
                    editTextHp.setEnabled(false);
                    editTextPin.setEnabled(false);
                    //buttonBeli.setEnabled(false);
                } else {
                    editTextIdPln.setEnabled(true);
                    editTextHp.setEnabled(true);
                    editTextPin.setEnabled(true);
                    //buttonBeli.setEnabled(true);
                }
            } else if (viewId == R.id.activity_token_pln_SpinnerNomorTransaksi) {
                nomorUrut = position;
            }

        }


    }

}
