package com.zam.sidik_padang.home.ppob.settings;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.AppCompatSpinner;
import androidx.appcompat.widget.Toolbar;

import com.google.gson.JsonObject;

import java.util.Arrays;
import java.util.List;

import com.zam.sidik_padang.BaseLogedinActivity;
import com.zam.sidik_padang.R;
import com.zam.sidik_padang.util.Config;
import com.zam.sidik_padang.util.Util;
import com.zam.sidik_padang.util.VolleySingleton;
import com.zam.sidik_padang.util.VolleyStringRequest;
import com.zam.sidik_padang.util.customclasses.SpinnerAdapter;

public class SettingsActivity extends BaseLogedinActivity {

    private final String VOLLEY_TAG = SettingsActivity.class.getName();
    private TextView textViewKodeLama, textViewKodeBaru1, textViewKodeBaru2;
    private EditText editTextKodeLama, editTextKodeBaru1, editTextKodeBaru2;
    private int selected = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO: Implement this method
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setSubtitle("Ubah PIN dan kata sandi");
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        textViewKodeLama = (TextView) findViewById(R.id.activity_settings_TextViewKodeLama);
        textViewKodeBaru1 = (TextView) findViewById(R.id.activity_settings_TextViewKodeBaru1);
        textViewKodeBaru2 = (TextView) findViewById(R.id.activity_settings_TextViewKodeBaru2);
        editTextKodeLama = (EditText) findViewById(R.id.activity_settings_EditTextKodeLAma);
        editTextKodeBaru1 = (EditText) findViewById(R.id.activity_settings_EditTextKodeBaru1);
        editTextKodeBaru2 = (EditText) findViewById(R.id.activity_settings_EditTextKodeBaru2);

        List<Object> list = Arrays.asList(new Object[]{"Ganti PIN"/*, "Ganti Kata Sandi"*/});
        AppCompatSpinner spinner = (AppCompatSpinner) findViewById(R.id.activity_settings_Spinner);
        spinner.setAdapter(new SpinnerAdapter(getResources(), list));
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selected = position;
                if (selected == 0) {
                    textViewKodeLama.setText("PIN lama");
                    textViewKodeBaru1.setText("PIN baru");
                    textViewKodeBaru2.setText("Ulangi PIN baru");
                    editTextKodeBaru1.setHint("PIN baru");
                    editTextKodeBaru2.setHint("Ulangi PIN baru");
                    editTextKodeLama.setHint("PIN lama");
                } else {
                    textViewKodeLama.setText("Sandi lama");
                    textViewKodeBaru1.setText("Sandi baru");
                    textViewKodeBaru2.setText("Ulang Sandi baru");
                    editTextKodeBaru1.setHint("Sandi baru");
                    editTextKodeBaru2.setHint("Ulangi Sandi baru");
                    editTextKodeLama.setHint("Sandi lama");
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        findViewById(R.id.activity_settings_ButtonUbah).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                prosesKeServer();
            }
        });
    }

    private void prosesKeServer() {
        String kodeLama = editTextKodeLama.getText().toString().trim();
        if (kodeLama.isEmpty()) {
            editTextKodeLama.setError("Harus diisi");
            return;
        }
        String kodeBaru1 = editTextKodeBaru1.getText().toString().trim();
        if (kodeBaru1.isEmpty()) {
            editTextKodeBaru1.setError("Harus diisi");
            return;
        }
        String kodeBaru2 = editTextKodeBaru2.getText().toString().trim();
        if (kodeBaru2.isEmpty()) {
            editTextKodeBaru2.setError("Harus diisi");
            return;
        }
        if (!kodeBaru2.equals(kodeBaru1)) {
            Toast.makeText(this, "Kode baru pertama dan kedua harus sama", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!Util.isInternetAvailible(this)) {
            Toast.makeText(this, R.string.no_internet_connection, Toast.LENGTH_SHORT).show();
            return;
        }
        String url = Config.URL_SETING + "userid=" + user.userid;
        if (selected == 0) {
            url += "&aksi=2&token=" + kodeLama + "&tokenbaru1=" + kodeBaru1 + "&tokenbaru2=" + kodeBaru2;
        } else {
            url += "&aksi=1&pwlama=" + kodeLama + "&pwbaru1=" + kodeBaru1 + "&pwbaru2=" + kodeBaru2;
        }

        final Dialog d = ProgressDialog.show(this, null, "Mohon tunggu...", true, false);
        VolleyStringRequest request = new VolleyStringRequest(url, new VolleyStringRequest.Callback() {
            @Override
            public void onResponse(final JsonObject jsonObject) {
                debug(getClass(), "Ubah kode response=" + jsonObject);
                d.dismiss();
                new AlertDialog.Builder(SettingsActivity.this).setMessage(jsonObject.get("message").getAsString())
                        .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
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

    @Override
    protected void onDestroy() {
        VolleySingleton.getInstance(this).getRequestQueue().cancelAll(VOLLEY_TAG);
        super.onDestroy();
    }
}
