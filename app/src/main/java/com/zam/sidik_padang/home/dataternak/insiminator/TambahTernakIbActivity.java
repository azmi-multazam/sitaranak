package com.zam.sidik_padang.home.dataternak.insiminator;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.AppCompatSpinner;
import androidx.appcompat.widget.Toolbar;

import com.androidnetworking.AndroidNetworking;
import com.google.gson.Gson;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import com.zam.sidik_padang.BaseApiResponse;
import com.zam.sidik_padang.BaseLogedinActivity;
import com.zam.sidik_padang.R;
import com.zam.sidik_padang.util.Config;
import com.zam.sidik_padang.util.MyStringRequestListener;
import com.zam.sidik_padang.util.SelectDateDialogFragment;
import com.zam.sidik_padang.util.Util;
import com.zam.sidik_padang.util.customclasses.SpinnerAdapter;

//import com.prolificinteractive.materialcalendarview.OnDateSelectedListener;
//import com.zam.sidik_padang.util.DatePickerDialog;

/**
 * Created by supriyadi on 4/22/18.
 */

public class TambahTernakIbActivity extends BaseLogedinActivity
        implements View.OnClickListener, SelectDateDialogFragment.OnCalendarSelectedListener, DatePickerDialog.OnDateSetListener {

    private static final String TAG = TambahTernakIbActivity.class.getName();
    private static final String PREF_RUMPUN_KONDISI = "rumpun_kondisi";

    private TextView textIdEartag, textIdTernak, textIdPeternak, textNamaPeternak,
            textNomorStraw, textTanggalIb, textKeterangan;

    private View imageScanBarcode, progressBarIdternak, progressBarSpinnerRumpun, progressBarSpinnerKondisi;
    private Calendar tanggalIbCalendar;
    private List<Object> rumpuns, kondisis;
    private SpinnerAdapter rumpunSpinnerAdapter, kondisiSpinnerAdapter;
    private int selectedRumpun = 0, selectedKondisi = 0;
    private AppCompatSpinner spinnerRumpun, spinnerKondisi;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tambah_ternak_ib);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        textIdEartag = (TextView) findViewById(R.id.activity_tambah_data_ternak_ib_EditTextEartag);
        textIdTernak = (TextView) findViewById(R.id.activity_tambah_data_ternak_ib_EditTextIdTernak);
        imageScanBarcode = findViewById(R.id.imageViewScanBarcode);
        imageScanBarcode.setOnClickListener(this);
        textIdPeternak = (TextView) findViewById(R.id.activity_tambah_data_ternak_ib_EditTextIDPeternak);
        textNamaPeternak = (TextView) findViewById(R.id.activity_tambah_data_ternak_ib_EditTextNamaPeternak);
        textNomorStraw = (TextView) findViewById(R.id.activity_tambah_data_ternak_ib_EditTextNomorStraw);
        textTanggalIb = (TextView) findViewById(R.id.activity_tambah_data_ternak_ib_EditTextTanggalIb);
        textTanggalIb.setOnClickListener(this);
        textKeterangan = (TextView) findViewById(R.id.activity_tambah_data_ternak_ib_EditTextKeterangan);
        progressBarSpinnerRumpun = findViewById(R.id.activity_tambah_data_ternak_ib_ProgressbarspinnerRumpun);
        progressBarSpinnerKondisi = findViewById(R.id.activity_tambah_data_ternak_ib_ProgressbarspinnerKondisi);
        progressBarIdternak = findViewById(R.id.progressBarIdTernak);
        findViewById(R.id.activity_tambah_data_ternak_ib_ButtonSimpan).setOnClickListener(this);
        spinnerRumpun = (AppCompatSpinner) findViewById(R.id.activity_tambah_data_ternak_ib_spinnerRumpun);
        rumpuns = new ArrayList<>();
        rumpuns.add("-Pilih rumpun-");
        rumpunSpinnerAdapter = new SpinnerAdapter(getResources(), rumpuns);
        spinnerRumpun.setAdapter(rumpunSpinnerAdapter);
        spinnerRumpun.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                selectedRumpun = i;
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        spinnerKondisi = (AppCompatSpinner) findViewById(R.id.activity_tambah_data_ternak_ib_SpinnerKondisi);
        kondisis = new ArrayList<>();
        kondisis.add("-Pilih kondisi-");
        kondisiSpinnerAdapter = new SpinnerAdapter(getResources(), kondisis);
        spinnerKondisi.setAdapter(kondisiSpinnerAdapter);
        spinnerKondisi.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                selectedKondisi = i;
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        String savedRumpunKondisi = sharedPreferences.getString(PREF_RUMPUN_KONDISI, "");
        if (!savedRumpunKondisi.isEmpty() && savedRumpunKondisi.startsWith("{")) {
            RumpunKondisiResponse response = new Gson().fromJson(savedRumpunKondisi, RumpunKondisiResponse.class);
            if (response != null) bindRumpunKondisiSpinner(response);
        }
        lodRumpunKondisiFromServer();
    }

    private void bindRumpunKondisiSpinner(RumpunKondisiResponse response) {
        for (int i = rumpuns.size() - 1; i > 0; i--) {
            rumpuns.remove(i);
        }
        rumpuns.addAll(response.rumpun_sapi);
        rumpunSpinnerAdapter.notifyDataSetChanged();

        for (int i = kondisis.size() - 1; i > 0; i--) {
            kondisis.remove(i);
        }
        kondisis.addAll(response.kondisi_ternak);
        kondisiSpinnerAdapter.notifyDataSetChanged();
    }


    private void lodRumpunKondisiFromServer() {
        if (!Util.isInternetAvailible(this)) {
            Toast.makeText(this, "Tidak ada internet", Toast.LENGTH_SHORT).show();
            return;
        }
        String link = Config.URL_TAMBAH_IB + "?aksi=4&userid=" + user.userid;
        debug("getRumpun kondisi link: " + link);
        progressBarSpinnerKondisi.setVisibility(View.VISIBLE);
        progressBarSpinnerRumpun.setVisibility(View.VISIBLE);

        AndroidNetworking.get(link).setTag(TAG).build().getAsString(new MyStringRequestListener() {
            @Override
            protected void response(String response) {
                progressBarSpinnerKondisi.setVisibility(View.INVISIBLE);
                progressBarSpinnerRumpun.setVisibility(View.INVISIBLE);
                debug("getRumpun kondisi response: " + response);
                RumpunKondisiResponse rkr = new Gson().fromJson(response, RumpunKondisiResponse.class);
                if (rkr.success) {
                    sharedPreferences.edit().putString(PREF_RUMPUN_KONDISI, response).apply();
                    bindRumpunKondisiSpinner(rkr);
                }
            }
        });
    }


    @Override
    public void onClick(View view) {
        int id = view.getId();
        if (id == R.id.imageViewScanBarcode) {
            IntentIntegrator integrator = new IntentIntegrator(this);
            integrator.setBeepEnabled(true);
            integrator.setOrientationLocked(false);
//			integrator.setBarcodeImageEnabled(true);
            integrator.setDesiredBarcodeFormats(IntentIntegrator.ALL_CODE_TYPES);
            integrator.initiateScan();
        } else if (id == R.id.activity_tambah_data_ternak_ib_EditTextTanggalIb) {
            Calendar maxCalendar = Calendar.getInstance();
            Calendar minCalendar = Calendar.getInstance();
            minCalendar.set(Calendar.YEAR, minCalendar.get(Calendar.YEAR) - 10);
            CalendarDay calendarDay = tanggalIbCalendar == null ? CalendarDay.from(maxCalendar) : CalendarDay.from(tanggalIbCalendar);
//			SelectDateDialogFragment.getDialog(calendarDay, minCalendar.getTimeInMillis(), maxCalendar.getTimeInMillis()).show(getSupportFragmentManager(), "date");
//			DatePickerDialog.getInstance(CalendarDay.from(minCalendar), calendarDay, CalendarDay.from(maxCalendar)).show(getSupportFragmentManager(), "Dialog");
            DatePickerDialog dpd = DatePickerDialog.newInstance(this, calendarDay.getCalendar());
            dpd.setMinDate(minCalendar);
            dpd.setMaxDate(maxCalendar);
            dpd.setVersion(DatePickerDialog.Version.VERSION_2);
            dpd.show(getSupportFragmentManager(), "dialog");
        } else if (id == R.id.activity_tambah_data_ternak_ib_ButtonSimpan) {
            validate();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            if (requestCode == IntentIntegrator.REQUEST_CODE) {
                IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
                if (result != null) {
                    String barcodeResult = result.getContents();
                    textIdTernak.setText(barcodeResult);
                    debug(getClass(), "barCodeResult: " + result);
                    if (barcodeResult != null) {
                        searchTernak();
                    }
                }
            } else super.onActivityResult(requestCode, resultCode, data);
        } else
            super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
        if (tanggalIbCalendar == null) tanggalIbCalendar = Calendar.getInstance();
        tanggalIbCalendar.set(Calendar.YEAR, year);
        tanggalIbCalendar.set(Calendar.MONTH, monthOfYear);
        tanggalIbCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
        textTanggalIb.setText(new SimpleDateFormat("EEEE d MMM yyyy", Locale.getDefault()).format(tanggalIbCalendar.getTime()));
    }

    private void searchTernak() {
        if (!Util.isInternetAvailible(this)) {
            Toast.makeText(this, "Tidak ada internet", Toast.LENGTH_SHORT).show();
            return;
        }
        progressBarIdternak.setVisibility(View.VISIBLE);
        String link = Config.URL_TAMBAH_IB + "?aksi=6&userid=" + user.userid + "&id_ternak=" + textIdTernak.getText().toString().trim();
        debug("Search ternak link: " + link);
        AndroidNetworking.get(link)
                .setTag(TAG)
                .build()
                .getAsString(new MyStringRequestListener() {
                    @Override
                    protected void response(String response) {
                        progressBarIdternak.setVisibility(View.INVISIBLE);
                        debug("Search ternak ib response: " + response);
                        SearchResponse searchResponse = new Gson().fromJson(response, SearchResponse.class);
                        if (searchResponse.success && searchResponse.data_ternak.size() > 0) {
                            TernakIb ternakIb = searchResponse.data_ternak.get(0);
                            textIdPeternak.setText(ternakIb.id_pemilik);
                            textNamaPeternak.setText(ternakIb.nama);
                            for (int i = 1; i < rumpuns.size(); i++) {
                                if (((RumpunKondisiResponse.Rumpun) rumpuns.get(i)).id_bangsa.equals(ternakIb.id_bangsa)) {
                                    spinnerRumpun.setSelection(i);
                                    break;
                                }

                            }
                        }
                    }
                });
    }

    @Override
    public void onCalendarSelected(CalendarDay calendarDay) {
        tanggalIbCalendar = calendarDay.getCalendar();
        textTanggalIb.setText(new SimpleDateFormat("EEEE d MMM yyyy", Locale.getDefault()).format(tanggalIbCalendar.getTime()));
    }

    private void validate() {
        String eartag = textIdEartag.getText().toString().trim();
        if (eartag.isEmpty()) {
            textIdEartag.setError("Harus diisi");
            textIdEartag.requestFocus();
            return;
        }
        String idTernak = textIdTernak.getText().toString().trim();
        if (idTernak.isEmpty()) {
            textIdTernak.setError("Harus diisi");
            textIdTernak.requestFocus();
            return;
        }

        if (selectedRumpun == 0) {
            Toast.makeText(this, "Rumpun ternak harus dipilih", Toast.LENGTH_SHORT).show();
            return;
        }


        String idPeternak = textIdPeternak.getText().toString().trim();
        if (idPeternak.isEmpty()) {
            textIdPeternak.setError("Harus diisi");
            textIdPeternak.requestFocus();
            return;
        }

        String namaPeternak = textNamaPeternak.getText().toString().trim();
        if (namaPeternak.isEmpty()) {
            textNamaPeternak.setError("Harus diisi");
            textNamaPeternak.requestFocus();
            return;
        }


        String noStraw = textNomorStraw.getText().toString().trim();
        if (noStraw.isEmpty()) {
            textNomorStraw.setError("Harus diisi");
            textNomorStraw.requestFocus();
            return;
        }

        if (tanggalIbCalendar == null) {
            textTanggalIb.setError("Harus diisi");
            textTanggalIb.requestFocus();
            return;
        }

        if (selectedKondisi == 0) {
            Toast.makeText(this, "Kondisi ternak harus dipilih", Toast.LENGTH_SHORT).show();
            return;
        }

        String link = Config.URL_TAMBAH_IB + "?aksi=1&userid=" + user.userid;
        link += "&id_ternak=" + Util.toUrlEncoded(idTernak);
        link += "&eartag=" + Util.toUrlEncoded(eartag);
        link += "&jenis_ternak=" + ((RumpunKondisiResponse.Rumpun) rumpuns.get(selectedKondisi)).id_bangsa;
        link += "&id_pemilik=" + Util.toUrlEncoded(idPeternak);
        link += "&pemilik=" + Util.toUrlEncoded(namaPeternak);
        link += "&no_straw=" + Util.toUrlEncoded(noStraw);
        link += "&kondisi=" + ((RumpunKondisiResponse.Kondisi) kondisis.get(selectedKondisi)).id_kondisi;
        link += "&tanggal_ib=" + new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(tanggalIbCalendar.getTime());
        link += "&keterangan=" + Util.toUrlEncoded(textKeterangan.getText().toString().trim());
        debug("Tambah ternak Ib link: " + link);
        final Dialog d = Util.showProgressDialog(this, "Harap tunggu", false);
        AndroidNetworking.get(link).setTag(TAG).build()
                .getAsString(new MyStringRequestListener() {
                    @Override
                    protected void response(String response) {
                        d.dismiss();
                        debug("Tambah ternak Ib Response: " + response);
                        BaseApiResponse apiResponse = new Gson().fromJson(response, BaseApiResponse.class);
                        if (apiResponse.success) {
                            setResult(RESULT_OK);
                            Toast.makeText(TambahTernakIbActivity.this, "Berhasil", Toast.LENGTH_SHORT).show();
                            finish();
                        } else {
                            if (isResummed) new AlertDialog.Builder(TambahTernakIbActivity.this)
                                    .setTitle("Terjadi Kesalahan")
                                    .setMessage(apiResponse.message)
                                    .setPositiveButton(android.R.string.ok, null).show();

                        }
                    }
                });
    }
	/*@Override
		public void onDateSelected(@NonNull MaterialCalendarView widget, @NonNull CalendarDay calendarDay, boolean selected) {
			tanggalIbCalendar = calendarDay.getCalendar();
			textTanggalIb.setText(new SimpleDateFormat("EEEE d MMM yyyy", Locale.getDefault()).format(tanggalIbCalendar.getTime()));
		}
	*/

    @Override
    protected void onDestroy() {
        AndroidNetworking.cancel(TAG);
        super.onDestroy();
    }

    private void debug(String msgs) {
        super.debug(getClass(), msgs);
    }

    private class SearchResponse extends BaseApiResponse {
        public List<TernakIb> data_ternak = new ArrayList<>();
    }
}
