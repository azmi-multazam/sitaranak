package com.zam.sidik_padang.home.ppob.transfersaldo;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.Toolbar;

import com.google.gson.JsonObject;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import java.text.NumberFormat;

import com.zam.sidik_padang.BaseLogedinActivity;
import com.zam.sidik_padang.R;
import com.zam.sidik_padang.util.Config;
import com.zam.sidik_padang.util.Util;
import com.zam.sidik_padang.util.VolleySingleton;
import com.zam.sidik_padang.util.VolleyStringRequest;

public class TransferSaldoActivity extends BaseLogedinActivity {

    private static final String VOLLEY_TAG = TransferSaldoActivity.class.getName();
    private EditText editTextIdTujuan, editTextNominal, editTextPin;
    private TextView textUserIdCheck;
    private View progressbarCheck;

    private boolean isChecking = false;
    private boolean isValidId = false;
    private Handler handler = new Handler();
    private VolleyStringRequest checkRequest;
    private JsonObject jsonCheck;

    private final Runnable cekRunnable = new Runnable() {

        @Override
        public void run() {
            if (checkRequest != null && !checkRequest.isCanceled()) checkRequest.cancel();
            String userId = editTextIdTujuan.getText().toString().trim();
            if (userId.isEmpty() || !Util.isInternetAvailible(TransferSaldoActivity.this)) return;
            progressbarCheck.setVisibility(View.VISIBLE);
            textUserIdCheck.setText("Mengecek member...");
            textUserIdCheck.setTextColor(Color.parseColor("#808080"));
            textUserIdCheck.setVisibility(View.VISIBLE);
            String url = Config.URL_CEK_MEMBER;
            url += "userid=" + userId;
            //url+="&token="+pin;
            url += "&aksi=1";
            //url+="&idmember="+idTujuan;
            debug(getClass(), "Cek member url=" + url);
            checkRequest = new VolleyStringRequest(url, new VolleyStringRequest.Callback() {

                @Override
                public void onResponse(JsonObject jsonObject) {
                    progressbarCheck.setVisibility(View.INVISIBLE);
                    debug(getClass(), "Cek member response: " + jsonObject);
                    isChecking = false;
                    if (jsonObject.get("success").getAsBoolean()) {
                        jsonCheck = jsonObject;
                        isValidId = true;
                        textUserIdCheck.setTextColor(Color.GREEN);
                        textUserIdCheck.setText(jsonObject.get("member").getAsJsonArray().get(0).getAsJsonObject().get("nama").getAsString());
                    } else {
                        isValidId = false;
                        textUserIdCheck.setTextColor(Color.RED);
                        textUserIdCheck.setText("Tidak ditemukan");
                    }

                }
            });


            checkRequest.setTag(VOLLEY_TAG);
            VolleySingleton.getInstance(TransferSaldoActivity.this).getRequestQueue().add(checkRequest);

        }
    };

    private final Runnable checkingWaitRunnable = new Runnable() {
        private Dialog d;

        @Override
        public void run() {
            if (isChecking) {
                handler.postDelayed(this, 300);
                if (d == null)
                    d = ProgressDialog.show(TransferSaldoActivity.this, null, "Mengecek ID tujuan..m", true, true);
                if (!d.isShowing()) d.show();
            } else {
                if (d != null && d.isShowing()) d.dismiss();
                if (isValidId) {

                    dialogkonfirmasi(jsonCheck);
                } else {
                    editTextIdTujuan.setError("ID member tidak ditemukan");
                    editTextIdTujuan.requestFocus();
                }
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO: Implement this method
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transfer_saldo);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        //ViewCompat.setTransitionName(toolbar, "transisi_transfer_saldo");
        editTextIdTujuan = (EditText) findViewById(R.id.activity_transfer_saldo_EditTextIdTujuan);
        editTextIdTujuan.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence p1, int p2, int p3, int p4) {
                isValidId = false;
            }

            @Override
            public void onTextChanged(CharSequence p1, int p2, int p3, int p4) {
                // TODO: Implement this method
            }

            @Override
            public void afterTextChanged(Editable p1) {
                handler.removeCallbacks(cekRunnable);
                handler.postDelayed(cekRunnable, 700);
            }
        });
        editTextNominal = (EditText) findViewById(R.id.activity_transfer_saldo_EditTextNominal);
        editTextPin = (EditText) findViewById(R.id.activity_transfer_saldo_EditTextPin);
        textUserIdCheck = (TextView) findViewById(R.id.activity_transfer_saldo_TextViewCheck);
        progressbarCheck = findViewById(R.id.activity_transfer_saldo_ProgressBarCheck);
        findViewById(R.id.activity_transfer_saldo_ButtonKirim).setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View p1) {
                kirim();
            }
        });

        findViewById(R.id.activity_transfer_saldo_ImageViewCamera).setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View p1) {
                IntentIntegrator integrator = new IntentIntegrator(TransferSaldoActivity.this);
                integrator.setBeepEnabled(true);
                integrator.setOrientationLocked(false);
//			integrator.setBarcodeImageEnabled(true);
                integrator.setDesiredBarcodeFormats(IntentIntegrator.ALL_CODE_TYPES);
                integrator.initiateScan();
            }
        });
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == IntentIntegrator.REQUEST_CODE) {
            IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
            if (result != null) {
                String barcodeResult = result.getContents();
                editTextIdTujuan.setText(barcodeResult);
                debug(getClass(), "barCodeResult: " + result);

            }

        } else
            super.onActivityResult(requestCode, resultCode, data);

    }


    private void kirim() {
        String idTujuan = editTextIdTujuan.getText().toString().trim();
        if (idTujuan.isEmpty()) {
            editTextIdTujuan.setError("Harus diisi");
            editTextIdTujuan.requestFocus();
            return;
        }
        if (idTujuan.contains(" ")) {
            editTextIdTujuan.setError("ID tujuan tidak valid");
            editTextIdTujuan.requestFocus();
            return;
        }

        String nominal = editTextNominal.getText().toString().trim();
        if (nominal.isEmpty()) {
            editTextNominal.setError("Harus diisi");
            editTextNominal.requestFocus();
            return;
        }

        String pin = editTextPin.getText().toString().trim();
        if (pin.isEmpty()) {
            editTextPin.setError("Harus diisi");
            editTextPin.requestFocus();
            return;
        }
        if (!Util.isInternetAvailible(this)) {
            Toast.makeText(this, R.string.no_internet_connection, Toast.LENGTH_SHORT).show();
            return;
        }
        handler.post(checkingWaitRunnable);
    }

    private void dialogkonfirmasi(JsonObject jsonCek) {
        boolean cekSukses = jsonCek.get("success").getAsBoolean();
        AlertDialog.Builder b = new AlertDialog.Builder(this);
        if (cekSukses) {
            b.setTitle("Konfirmasi");
            b.setMessage("Transfer saldo sebesar Rp. "
                    + NumberFormat.getInstance().format(Double.parseDouble(editTextNominal.getText().toString().trim()))
                    + "\nKepada " + jsonCek.get("member").getAsJsonArray().get(0).getAsJsonObject().get("nama").getAsString()
            );
            b.setNegativeButton("Batal", null);
            b.setPositiveButton("Transfer", new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface p1, int p2) {
                    prosesTransfer();
                }
            });
        } else {
            b.setMessage(jsonCek.get("message").getAsString());
        }

        b.show();
    }

    private void prosesTransfer() {
        if (!Util.isInternetAvailible(this)) {
            Toast.makeText(this, R.string.no_internet_connection, Toast.LENGTH_SHORT).show();
            return;
        }
        String url = Config.URL_DEPOSIT;
        url += "aksi=4";
        url += "&userid=" + user.userid;
        url += "&token=" + editTextPin.getText().toString().trim();
        url += "&idmember=" + editTextIdTujuan.getText().toString().trim();
        url += "&nominal=" + editTextNominal.getText().toString().trim();
        final Dialog d = ProgressDialog.show(this, null, "Harap tunggu", true, false);
        VolleyStringRequest request = new VolleyStringRequest(url, new VolleyStringRequest.Callback() {

            @Override
            public void onResponse(final JsonObject jsonObject) {
                debug(getClass(), "Transfer reaponse: " + jsonObject);
                d.dismiss();
                new AlertDialog.Builder(TransferSaldoActivity.this)
                        .setMessage(jsonObject.get("message").getAsString())
                        .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                if (jsonObject.get("success").getAsBoolean()) finish();
                            }
                        })
                        .show();
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
