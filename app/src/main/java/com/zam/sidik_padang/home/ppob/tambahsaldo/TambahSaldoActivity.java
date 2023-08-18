package com.zam.sidik_padang.home.ppob.tambahsaldo;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.widget.AppCompatSpinner;
import androidx.appcompat.widget.Toolbar;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import com.zam.sidik_padang.BaseLogedinActivity;
import com.zam.sidik_padang.R;
import com.zam.sidik_padang.home.ppob.history.deposit.HistoryDeposit;
import com.zam.sidik_padang.util.Config;
import com.zam.sidik_padang.util.HttpUrlConnectionApi;
import com.zam.sidik_padang.util.Util;
import com.zam.sidik_padang.util.VolleySingleton;
import com.zam.sidik_padang.util.VolleyStringRequest;
import com.zam.sidik_padang.util.customclasses.SpinnerAdapter;
import com.zam.sidik_padang.util.customclasses.SpinnerListener;

/**
 * Created by supriyadi on 7/30/17.
 */

public class TambahSaldoActivity extends BaseLogedinActivity {

    private static final String PREF_LAST_DETAIL_BANK = "last_detail_bank";
    private final String VOLLEY_TAG = getClass().getName();
    List<Object> listPilihBank;
    SpinnerAdapter spinnerAdapterPilihBank;
    private TextView textViewDetailBank, textViewNominal, textViewBerita;
    private int selectedBAnk = 0;
    private EditText editTextPin;
    private SharedPreferences pref;
    private boolean sedangLoadPilihanBank = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tambah_saldo);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        //ViewCompat.setTransitionName(toolbar, "transisi_tambah_saldo");
        pref = PreferenceManager.getDefaultSharedPreferences(this);
        textViewDetailBank = (TextView) findViewById(R.id.activity_tambah_saldo_TextViewDetailBank);
        textViewNominal = (TextView) findViewById(R.id.activity_tambah_saldo_EditTextNominal);
        textViewBerita = (TextView) findViewById(R.id.activity_tambah_saldo_EditTextBerita);
        listPilihBank = new ArrayList<>();
        listPilihBank.add("-pilih bank-");
        spinnerAdapterPilihBank = new SpinnerAdapter(getResources(), listPilihBank);
        AppCompatSpinner spinner = (AppCompatSpinner) findViewById(R.id.activity_tambah_saldo_SpinnerPilihBank);
        spinner.setAdapter(spinnerAdapterPilihBank);
        editTextPin = (EditText) findViewById(R.id.activity_tambah_saldo_EditTextPin);
        spinner.setOnItemSelectedListener(new SpinnerListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                textViewDetailBank.setText("");
                selectedBAnk = position;
                editTextPin.setEnabled(position != 0);
            }
        });
        findViewById(R.id.activity_tambah_saldo_ButtonRequest).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadDetailBAnk();
            }
        });

        loadPilihanBankDaripenyimpanan();

    }

    private void cekLastDetailBank() {
        if (!Util.isInternetAvailible(this)) return;
		/*
		 http://bungara.com/api/history.php?userid=100001&aksi=7&dtfrom=2017-07-01&dtto=2017-07-11
		 */
        Calendar calToday = Calendar.getInstance(Locale.getDefault());
        calToday.set(Calendar.HOUR_OF_DAY, 0);
        calToday.set(Calendar.MINUTE, 0);
        calToday.set(Calendar.MILLISECOND, 0);
        calToday.set(Calendar.SECOND, 0);

        SimpleDateFormat formater = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        String url = Config.URL_HISTORY + "userid=" + user.userid +
                "&aksi=7";
        url += "&dtto=" + formater.format(calToday.getTime());
        url += "&dtfrom=" + formater.format(calToday.getTimeInMillis() - 1000 * 60 * 60 * 24);
        // 2017-07-01&dtto=2017-07-11

        debug(getClass(), "Load deposit url=" + url);
        VolleyStringRequest request = new VolleyStringRequest(url, new VolleyStringRequest.Callback() {

            @Override
            public void onResponse(JsonObject jsonObject) {

                debug(getClass(), "Load History deposit response=" + jsonObject);
                if (jsonObject.get("success").getAsBoolean()) {
                    JsonArray ja = jsonObject.get("history_deposit").getAsJsonArray();
                    boolean sudahRequest = false;
                    Gson gson = new Gson();
                    for (JsonElement je : ja) {
                        HistoryDeposit hd = gson.fromJson(je, HistoryDeposit.class);
                        if (hd.status.equalsIgnoreCase("waiting")) sudahRequest = true;
                    }
                    if (sudahRequest)
                        textViewDetailBank.setText(pref.getString(PREF_LAST_DETAIL_BANK, ""));

                }
            }
        });
        request.setTag(VOLLEY_TAG);

        VolleySingleton.getInstance(this).getRequestQueue().add(request);


    }

    private void loadPilihanBankDaripenyimpanan() {
        String jsonArrayString = PreferenceManager.getDefaultSharedPreferences(this).getString(Config.PREF_PILIHAN_BANK, "");
        if (jsonArrayString.isEmpty()) return;
        JsonArray jsonArray = new Gson().fromJson(jsonArrayString, JsonArray.class);
        for (int i = listPilihBank.size() - 1; i > 0; i--) listPilihBank.remove(i);
        for (JsonElement je : jsonArray) {
            JsonObject jo = je.getAsJsonObject();
            listPilihBank.add(jo.get("bank").getAsString());
        }
        spinnerAdapterPilihBank.notifyDataSetChanged();
    }

    @Override
    protected void onResume() {
        super.onResume();
        cekLastDetailBank();
        loadPilihanBankDariServer();
    }

    private void loadPilihanBankDariServer() {
        if (!Util.isInternetAvailible(this) || sedangLoadPilihanBank) return;
        sedangLoadPilihanBank = true;
        final View progressBar = findViewById(R.id.activity_tambah_saldo_ProgressbarspinnerPilihBank);
        progressBar.setVisibility(View.VISIBLE);
        String url = Config.URL_BANK + "aksi=1&userid=" + user.userid;
        debug(getClass(), "Load pilihan bank url=" + url);
        HttpUrlConnectionApi.get(url, new HttpUrlConnectionApi.Callback() {
            @Override
            public void onResponse(JsonObject jsonObject) {
                debug(getClass(), "Load pilihan bank onResponse= " + jsonObject);
                sedangLoadPilihanBank = false;
                progressBar.setVisibility(View.INVISIBLE);
                if (jsonObject.get("success").getAsBoolean()) {
                    for (int i = listPilihBank.size() - 1; i > 0; i--) listPilihBank.remove(i);
                    JsonArray array = jsonObject.get("data_bank").getAsJsonArray();
                    PreferenceManager.getDefaultSharedPreferences(TambahSaldoActivity.this).edit().putString(Config.PREF_PILIHAN_BANK, array.toString()).apply();
                    for (JsonElement je : array)
                        listPilihBank.add(je.getAsJsonObject().get("bank").getAsString());
                    spinnerAdapterPilihBank.notifyDataSetChanged();
                } else
                    Toast.makeText(TambahSaldoActivity.this, jsonObject.get("message").getAsString(), Toast.LENGTH_SHORT).show();
            }
        });
		/*
		VolleyStringRequest request = new VolleyStringRequest(url, new VolleyStringRequest.Callback() {
				@Override
				public void onResponse(JsonObject jsonObject)
				{
					debug(getClass(), "Load pilihan bank onResponse= " + jsonObject);
					sedangLoadPilihanBank = false;
					progressBar.setVisibility(View.INVISIBLE);
					if (jsonObject.get("success").getAsBoolean())
					{
						for (int i = listPilihBank.size() - 1; i > 0; i--) listPilihBank.remove(i);
						JsonArray array = jsonObject.get("data_bank").getAsJsonArray();
						PreferenceManager.getDefaultSharedPreferences(TambahSaldoActivity.this).edit().putString(Config.PREF_PILIHAN_BANK, array.toString()).apply();
						for (JsonElement je : array)
							listPilihBank.add(je.getAsJsonObject().get("bank").getAsString());
						spinnerAdapterPilihBank.notifyDataSetChanged();
					}
					else
						Toast.makeText(TambahSaldoActivity.this, jsonObject.get("message").getAsString(), Toast.LENGTH_SHORT).show();
				}
			});

		request.setTag(VOLLEY_TAG);
		VolleySingleton.getInstance(this).getRequestQueue().add(request);*/
    }

    private void loadDetailBAnk() {
        String nominal = textViewNominal.getText().toString().trim();
        if (nominal.isEmpty()) {
            textViewNominal.setError("Harus diisi");
            return;
        }

        if (selectedBAnk == 0) {
            Toast.makeText(this, "Bank wajib dipilih", Toast.LENGTH_SHORT).show();
            return;
        }

        String pin = editTextPin.getText().toString().trim();
        if (pin.isEmpty()) {
            editTextPin.setError("Harus diisi");
            return;
        }

        if (!Util.isInternetAvailible(this)) {
            Toast.makeText(this, R.string.no_internet_connection, Toast.LENGTH_SHORT).show();
            return;
        }
        String url = Config.URL_DEPOSIT + "aksi=2&userid=" + user.userid
                + "&nominal=" + nominal
                + "&bank=" + listPilihBank.get(selectedBAnk);
        url += "&berita=" + Util.toUrlEncoded(textViewBerita.getText().toString().trim());
        url += "&token=" + pin;
        debug(getClass(), "Request deposit.Url=" + url);
        final Dialog dialog = ProgressDialog.show(this, null, "Harap tunggu", true, true);
        final VolleyStringRequest request = new VolleyStringRequest(url, new VolleyStringRequest.Callback() {
            @Override
            public void onResponse(JsonObject jsonObject) {
                debug(getClass(), "Request deposit response= " + jsonObject);
                dialog.dismiss();
                if (jsonObject.get("success").getAsBoolean()) {
                    Calendar cal = Calendar.getInstance(Locale.getDefault());
                    cal.setTimeInMillis(cal.getTimeInMillis() + 1000 * 60 * 60 * 24);
                    String batasWaktu = "\nBatas waktu sampai:\n" + new SimpleDateFormat("EEEE, d-MMM-yy, h:mm", Locale.getDefault()).format(cal.getTime());
                    textViewDetailBank.setText(jsonObject.get("message").getAsString() + batasWaktu);
                    pref.edit().putString(PREF_LAST_DETAIL_BANK, jsonObject.get("message").getAsString() + batasWaktu).apply();
                    ((ViewGroup) findViewById(R.id.activity_tambah_saldo_LinearLayout)).removeAllViews();
                    getSupportFragmentManager().beginTransaction().replace(R.id.activity_tambah_saldo_LinearLayout, DetailTransferFragment.getInstance(jsonObject.get("message").getAsString())).commitAllowingStateLoss();
                } else
                    Toast.makeText(TambahSaldoActivity.this, jsonObject.get("message").getAsString(), Toast.LENGTH_SHORT).show();

            }
        });

        dialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                request.cancel();
            }
        });
        request.setTag(VOLLEY_TAG);
        VolleySingleton.getInstance(this).getRequestQueue().add(request);

    }
}
