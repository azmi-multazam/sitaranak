package com.zam.sidik_padang.home.dataternak.detailternak.datarecording;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.Toolbar;

import com.google.gson.JsonObject;
import com.prolificinteractive.materialcalendarview.CalendarDay;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import com.zam.sidik_padang.BaseLogedinActivity;
import com.zam.sidik_padang.R;
import com.zam.sidik_padang.home.dataternak.detailternak.DetailTernakActivity;
import com.zam.sidik_padang.util.Config;
import com.zam.sidik_padang.util.SelectDateDialogFragment;
import com.zam.sidik_padang.util.Util;
import com.zam.sidik_padang.util.VolleySingleton;
import com.zam.sidik_padang.util.VolleyStringRequest;

/**
 * Created by supriyadi on 9/26/17.
 */

public class TambahRecordingActivity extends BaseLogedinActivity
        implements View.OnClickListener, SelectDateDialogFragment.OnCalendarSelectedListener {

    public static final String EXTRA_DATA_RECORDING_ITEM = "data_recording_item";
    private static final String VOLLEY_TAG = TambahRecordingActivity.class.getName();
    private String idTernak = "", idRecording = "";
    private EditText editTextTanggal, editTextBeratBadan, editTextPanjangBadan, editTextTinggiBadan, editTextLingkarDada;
    private CalendarDay tanggal;
    private boolean edit = false;

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        if (!idTernak.isEmpty()) outState.putString(DetailTernakActivity.ID_TERNAK, idTernak);
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        if (savedInstanceState != null && savedInstanceState.containsKey(DetailTernakActivity.ID_TERNAK))
            idTernak = savedInstanceState.getString(DetailTernakActivity.ID_TERNAK);
        super.onRestoreInstanceState(savedInstanceState);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setResult(RESULT_CANCELED);
        setContentView(R.layout.activity_tambah_recording);
        Intent intent = getIntent();
        if (intent.hasExtra(DetailTernakActivity.ID_TERNAK))
            idTernak = intent.getStringExtra(DetailTernakActivity.ID_TERNAK);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setSubtitle(idTernak);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        editTextTanggal = (EditText) findViewById(R.id.activity_tambah_recording_EditTextBirthday);
        editTextTanggal.setOnClickListener(this);
        editTextBeratBadan = (EditText) findViewById(R.id.activity_tambah_recording_EditTextBeratBadan);
        editTextTinggiBadan = (EditText) findViewById(R.id.activity_tambah_recording_EditTinggiBadan);
        editTextPanjangBadan = (EditText) findViewById(R.id.activity_tambah_recording_EditPanjangBadan);
        editTextLingkarDada = (EditText) findViewById(R.id.activity_tambah_recording_EditLingkarDada);
        findViewById(R.id.activity_tambah_recording_ButtonSimpan).setOnClickListener(this);

        if (intent.hasExtra(EXTRA_DATA_RECORDING_ITEM)) {
            edit = true;
            DataRecordingItem data = intent.getParcelableExtra(EXTRA_DATA_RECORDING_ITEM);
            idRecording = data.id;
            String stgl = convertTanggal(data.tanggal);
            String[] splitedTanggal = stgl.split("-");
            tanggal = CalendarDay.from(Integer.parseInt(splitedTanggal[0]), Integer.parseInt(splitedTanggal[1]) - 1, Integer.parseInt(splitedTanggal[2]));
			/*
			if (data.tanggal.contains("-")) {
				String[] splitedTanggal = stgl.split("-");
				tanggal = CalendarDay.from(Integer.parseInt(splitedTanggal[0]), Integer.parseInt(splitedTanggal[1])-1, Integer.parseInt(splitedTanggal[2]));
			} else {
				tanggal = CalendarDay.today();
			}
			 */
            editTextBeratBadan.setText(data.berat_badan);
            editTextPanjangBadan.setText(data.panjang_badan);
            editTextTinggiBadan.setText(data.tinggi_badan);
            editTextLingkarDada.setText(data.lingkar_dada);
        } else tanggal = CalendarDay.today();
        onCalendarSelected(tanggal);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.activity_tambah_recording_EditTextBirthday) {
            SelectDateDialogFragment.getDialog(tanggal).show(getSupportFragmentManager(), "dialog");
        } else if (id == R.id.activity_tambah_recording_ButtonSimpan) {
            saveData();
        }
    }


    @Override
    public void onCalendarSelected(CalendarDay calendarDay) {
        tanggal = calendarDay;
        editTextTanggal.setText(new SimpleDateFormat("d MMMM yyyy", Locale.getDefault()).format(tanggal.getDate()));
    }

    private void saveData() {

        String bb = editTextBeratBadan.getText().toString().trim();
        String pb = editTextPanjangBadan.getText().toString().trim();
        String tb = editTextTinggiBadan.getText().toString().trim();
        String ld = editTextLingkarDada.getText().toString().trim();
        if (bb.isEmpty() && pb.isEmpty() && tb.isEmpty() && ld.isEmpty()) {
            new AlertDialog.Builder(this).setMessage("Masukkan minimal satu data").setPositiveButton(android.R.string.ok, null).show();
            return;
        }

        if (!Util.isInternetAvailible(this)) {
            Toast.makeText(this, "Tidak ada koneksi internet", Toast.LENGTH_SHORT).show();
            return;
        }
        String url = Config.URL_LIHAT_DATA_TERNAK + "?id_ternak=" + idTernak;
        url += "&aksi=" + (edit ? "9" : "8");
        url += "&id=" + idRecording;
        url += "&userid=" + user.userid;
        url += "&tanggal=" + new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(tanggal.getDate());
        url += "&bb=" + bb;
        url += "&pb=" + pb;
        url += "&tb=" + tb;
        url += "&ld=" + ld;
        debug(getClass(), "Simpan recording url= " + url);
        final Dialog dialog = ProgressDialog.show(this, null, "Harap tunggu", true, false);
        VolleyStringRequest request = new VolleyStringRequest(url, new VolleyStringRequest.Callback() {
            @Override
            public void onResponse(final JsonObject jsonObject) {
                debug(getClass(), "Simpan recording response= " + jsonObject);
                dialog.dismiss();
                new AlertDialog.Builder(TambahRecordingActivity.this)
                        .setMessage(jsonObject.get("message").getAsString())

                        .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                if (jsonObject.get("success").getAsBoolean()) {
                                    setResult(RESULT_OK);
                                    finish();
                                }
                            }
                        }).show();
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

    private String convertTanggal(String tanggal) {
        String[] res = tanggal.split(" ");
        switch (res[1]) {
            case "Jan":
                return res[2] + "-1-" + res[0];
            case "Feb":
                return res[2] + "-2-" + res[0];
            case "Mar":
                return res[2] + "-3-" + res[0];
            case "Apr":
                return res[2] + "-4-" + res[0];
            case "Mei":
                return res[2] + "-5-" + res[0];
            case "Jun":
                return res[2] + "-6-" + res[0];
            case "Jul":
                return res[2] + "-7-" + res[0];
            case "Agu":
                return res[2] + "-8-" + res[0];
            case "Sep":
                return res[2] + "-9-" + res[0];
            case "Okt":
                return res[2] + "-10-" + res[0];
            case "Nov":
                return res[2] + "-11-" + res[0];
            case "Des":
                return res[2] + "-12-" + res[0];
            default:
                return getToday();
        }
    }

    private String getToday() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        Date date = new Date();
        return dateFormat.format(date);
    }
}
