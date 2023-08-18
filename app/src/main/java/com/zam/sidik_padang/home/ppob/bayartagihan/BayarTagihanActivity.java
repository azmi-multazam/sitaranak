package com.zam.sidik_padang.home.ppob.bayartagihan;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.widget.AppCompatSpinner;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.List;

import com.zam.sidik_padang.BaseLogedinActivity;
import com.zam.sidik_padang.R;
import com.zam.sidik_padang.home.ppob.history.tagihan.HistoryTagihan;
import com.zam.sidik_padang.home.ppob.history.tagihan.HistoryTagihanAdapter;
import com.zam.sidik_padang.util.Config;
import com.zam.sidik_padang.util.Util;
import com.zam.sidik_padang.util.VolleySingleton;
import com.zam.sidik_padang.util.VolleyStringRequest;
import com.zam.sidik_padang.util.customclasses.SpinnerAdapter;
import com.zam.sidik_padang.util.data.ApiHelper;
import com.zam.sidik_padang.util.data.DataHelper;
import com.zam.sidik_padang.util.data.JenisPembayaran;

public class BayarTagihanActivity extends BaseLogedinActivity
        implements HistoryTagihanAdapter.OnItemButtonClickListener {
    private static final String VOLLEY_TAG = BayarTagihanActivity.class.getName();
    private List<Object> listJenisPembayaran;
    private SpinnerAdapter spinnerAdapter;
    private int selectedJenisPembayaran = 0;
    private TextView textNomorKontrak, textNomorHp, textPin;
    private Dialog dialog;
    private boolean isResummed = false;
    private List<HistoryTagihan> listHistoryTagihan;
    private HistoryTagihanAdapter adapterHistoryTagian;
    private View progressbar;

    @Override
    protected void onResume() {
        super.onResume();
        isResummed = true;
        loadHistory();
    }

    @Override
    protected void onPause() {
        isResummed = false;
        super.onPause();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO: Implement this method
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bayar_tagihan);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        textNomorKontrak = (EditText) findViewById(R.id.activity_bayar_tagihan_EditTextNomorKontrak);
        textNomorHp = (EditText) findViewById(R.id.activity_bayar_tagihan_EditTextNomorHp);
        textPin = (EditText) findViewById(R.id.activity_bayar_tagihan_EditTextPin);
        listJenisPembayaran = new ArrayList<>();
        listJenisPembayaran.add("-pilih jenis pembayaran-");
        listJenisPembayaran.addAll(DataHelper.getDataHelper(this).getAllJenisPembayaran());
        spinnerAdapter = new SpinnerAdapter(getResources(), listJenisPembayaran);
        AppCompatSpinner spinner = (AppCompatSpinner) findViewById(R.id.activity_bayar_tagihan_SpinnerJenisPembayaran);
        spinner.setAdapter(spinnerAdapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedJenisPembayaran = position;
                textPin.setEnabled(position != 0);
                textNomorKontrak.setEnabled(position != 0);
                textNomorHp.setEnabled(position != 0);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        findViewById(R.id.activity_bayar_tagihan_ButtonLanjutkan).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validateForm();
            }
        });

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.activity_bayar_tagihan_RecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        listHistoryTagihan = new ArrayList<>();
        adapterHistoryTagian = new HistoryTagihanAdapter(listHistoryTagihan, this);
        recyclerView.setAdapter(adapterHistoryTagian);

        progressbar = findViewById(R.id.activity_bayar_tagihan_Progressbar);
        loadListDariLokal();

        loadJenisPembayaranDAriServer();

    }

    private void loadJenisPembayaranDAriServer() {
        if (!Util.isInternetAvailible(this)) {
            if (listJenisPembayaran.size() < 2)
                Toast.makeText(this, R.string.no_internet_connection, Toast.LENGTH_SHORT).show();
            return;
        }
        if (listJenisPembayaran.size() < 2) {
            dialog = ProgressDialog.show(this, null, "Memuat data...", true, true);
        }
        ApiHelper.getHelper(this).getJenisPembayaran(new ApiHelper.OnDoneListener() {
            @Override
            public void onDone(boolean success) {
                if (dialog != null && dialog.isShowing()) dialog.dismiss();
                if (isResummed) loadListDariLokal();
            }
        });
    }

    private void loadListDariLokal() {
        for (int i = listJenisPembayaran.size() - 1; i > 0; i--) listJenisPembayaran.remove(i);
        listJenisPembayaran.addAll(DataHelper.getDataHelper(this).getAllJenisPembayaran());
        spinnerAdapter.notifyDataSetChanged();
    }

    private void validateForm() {
        if (selectedJenisPembayaran == 0) {
            Toast.makeText(this, "Pilih jenis pembayaran", Toast.LENGTH_SHORT).show();
            return;
        }

        String nomorKontrak = textNomorKontrak.getText().toString().trim();
        if (nomorKontrak.isEmpty()) {
            textNomorKontrak.setError("Harus diisi");
            return;
        }

        String nomorHp = textNomorHp.getText().toString().trim();
        if (nomorHp.isEmpty()) {
            textNomorHp.setError("Harus diisi");
            return;
        }
        nomorHp = nomorHp.replaceAll("-", "");
        nomorHp = nomorHp.replaceAll(" ", "");

        String pin = textPin.getText().toString().trim();
        if (pin.isEmpty()) {
            textPin.setError("Harus diisi");
            return;
        }

        if (!Util.isInternetAvailible(this)) {
            Toast.makeText(this, R.string.no_internet_connection, Toast.LENGTH_SHORT).show();
            return;
        }

        String url = Config.URL_PROSES + "aksi=3&userid=" + user.userid;
        url += "&token=" + pin;
        url += "&kode=" + ((JenisPembayaran) listJenisPembayaran.get(selectedJenisPembayaran)).code;
        url += "&hp=" + nomorHp;
        url += "&idpel=" + nomorKontrak;
        debug(getClass(), "cek tagihan url " + url);
        /*http://bungara.com/api/proses.php?userid=100017&token=1318&aksi=3&kode=PLN&hp=085276940070&idpel=09876677766*/
        final Dialog d = ProgressDialog.show(this, null, "Mohon tunggu", true, true);
        final VolleyStringRequest request = new VolleyStringRequest(url, new VolleyStringRequest.Callback() {
            @Override
            public void onResponse(JsonObject jsonObject) {
                d.dismiss();
                debug(getClass(), "cek tagihan onresponse " + jsonObject);
                if (jsonObject.get("success").getAsBoolean()) {
//					if (isResummed) new AlertDialog.Builder(BayarTagihanActivity.this)
//							.setMessage(jsonObject.toString()).show();
                    loadHistory();
                } else if (isResummed) {
                    Util.showDialog(BayarTagihanActivity.this, jsonObject.get("message").getAsString());
                }
            }
        });

        d.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                request.cancel();
            }
        });

        request.setTag(VOLLEY_TAG);
        VolleySingleton.getInstance(this).getRequestQueue().add(request);
    }

    private void loadHistory() {
        if (!Util.isInternetAvailible(this)) {
            Toast.makeText(this, R.string.no_internet_connection, Toast.LENGTH_SHORT).show();
            return;
        }

        String url = Config.URL_HISTORY
                + "userid=" + user.userid
                + "&aksi=3";
//				+ "&dtfrom=" + dateFormatForRequest.format(calendarMulai.getTime())
//				+ "&dtto=" + dateFormatForRequest.format(calendarSampai.getTime());

        debug(getClass(), "Load history tagihan url=" + url);
        progressbar.setVisibility(View.VISIBLE);
        VolleyStringRequest request = new VolleyStringRequest(url, new VolleyStringRequest.Callback() {

            @Override
            public void onResponse(JsonObject jsonObject) {
                progressbar.setVisibility(View.GONE);
                debug(getClass(), "Load History tagihan response=" + jsonObject);
                if (jsonObject.get("success").getAsBoolean()) {
                    JsonArray ja = jsonObject.get("history_tagihan").getAsJsonArray();
                    listHistoryTagihan.clear();
                    Gson gson = new Gson();
                    for (JsonElement je : ja)
                        if (!je.toString().toLowerCase().contains("sukses"))
                            listHistoryTagihan.add(gson.fromJson(je, HistoryTagihan.class));
                    adapterHistoryTagian.notifyDataSetChanged();

                } else if (isResummed)
                    Toast.makeText(BayarTagihanActivity.this, jsonObject.get("message").getAsString(), Toast.LENGTH_SHORT).show();
            }
        });
        request.setTag(VOLLEY_TAG);

        VolleySingleton.getInstance(BayarTagihanActivity.this).getRequestQueue().add(request);

    }


    @Override
    public void onItemButtonClickCekUlang(int position) {
        ApiHelper.cekUlangTagian(BayarTagihanActivity.this, listHistoryTagihan.get(position).id, new ApiHelper.OnDoneListener() {
            @Override
            public void onDone(boolean success) {
                if (success) loadHistory();
            }
        });
    }

    @Override
    public void onItemButtonClickBayar(int position) {
        ApiHelper.bayarTagihan(this, listHistoryTagihan.get(position).id, new ApiHelper.OnDoneListener() {
            @Override
            public void onDone(boolean success) {
                if (success) loadHistory();
            }
        });
    }
}
