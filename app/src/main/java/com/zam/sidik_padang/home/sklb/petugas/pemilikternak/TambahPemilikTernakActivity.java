package com.zam.sidik_padang.home.sklb.petugas.pemilikternak;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.widget.EditText;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProvider;

import com.google.gson.Gson;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

import com.zam.sidik_padang.BaseLogedinActivity;
import com.zam.sidik_padang.R;
import com.zam.sidik_padang.api.State;
import com.zam.sidik_padang.databinding.ActivityTambahPemilikTernakBinding;
import com.zam.sidik_padang.home.sklb.petugas.vm.PetugasSource;
import com.zam.sidik_padang.home.sklb.petugas.vm.PetugasViewModel;
import com.zam.sidik_padang.home.sklb.petugas.vm.pemilik.PemilikTernak;
import com.zam.sidik_padang.util.Util;

public class TambahPemilikTernakActivity extends BaseLogedinActivity {

    private ActivityTambahPemilikTernakBinding binding;
    private PetugasViewModel viewModel;
    private PemilikTernak pemilik;
    private Dialog dialog;

    private EditText editTextNama, editTextHp, editTextAlamat;
    private AppCompatButton btnSimpan;

    private boolean edit = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityTambahPemilikTernakBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        Toolbar toolbar = binding.toolbar;
        setSupportActionBar(toolbar);
        edit = getIntent().hasExtra("data");
        String tt = edit ? "Edit Pemilik" : "Tambah Pemilik";
        getSupportActionBar().setTitle(tt);
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back);
        toolbar.setNavigationOnClickListener(view -> {
            setResult(RESULT_CANCELED);
            finish();
        });

        editTextNama = binding.etNama;
        editTextHp = binding.etHp;
        editTextAlamat = binding.etAlamat;
        btnSimpan = binding.btnSimpan;
        btnSimpan.setOnClickListener(v -> sendData());

        if (edit) {
            pemilik = new Gson().fromJson(getIntent().getStringExtra("data"), PemilikTernak.class);
            editTextNama.setText(pemilik.getNama());
            editTextHp.setText(pemilik.getHp());
            editTextAlamat.setText(pemilik.getAlamat());
        }
        viewModel = new ViewModelProvider(this).get(PetugasViewModel.class);
        observeViewModel();
    }

    private void observeViewModel() {
        viewModel.getResponseAddPemilik().observe(this, response -> {
            if (response != null) {
                hideProgress();
                if (response.state == State.SUCCESS && response.success && response.data != null) {
                    new AlertDialog.Builder(this)
                            //.setTitle("")
                            .setMessage(response.message)
                            .setPositiveButton("Yes", (dialog, which) -> {
                                PetugasSource.getInstance().setPemilikUpdated(true);
                                onBackPressed();
                            })
                            //.setNegativeButton("Tambah Baru", null)
                            .show();
                } else if (response.state == State.LOADING) {
                    showProgress("", response.message);
                } else {
                    Util.showDialog(this, response.message);
                }
            }
        });
    }

    private void showProgress(String title, String msg) {
        dialog = ProgressDialog.show(this, title, msg, true, false);
    }

    private void hideProgress() {
        if (dialog != null && dialog.isShowing()) dialog.dismiss();
    }

    private void sendData() {
        Map<String, String> param = new HashMap<>();
        param.put("userid", user.userid);
        if (edit) {
            param.put("aksi", "2");
            param.put("id", pemilik.getId());
        } else {
            param.put("aksi", "1");
        }

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

        param.put("nama", nama);

        String hp = editTextHp.getText().toString().trim();
        if (hp.isEmpty()) {
            editTextHp.setError(getString(R.string.this_field_is_mandatory));
            editTextHp.requestFocus();
            return;
        } else if (hp.length() < 10 || hp.contains(" ") || !(hp.startsWith("0") || hp.startsWith("+"))) {
            editTextHp.setError(getString(R.string.invalid_phone_number));
            editTextHp.requestFocus();
            return;
        }

        param.put("hp", encode(hp));

        param.put("alamat", editTextAlamat.getText().toString().trim());
        viewModel.tambahPemilik(param);
    }

    private String encode(String src) {
        try {
            return URLEncoder.encode(src, "utf-8");
        } catch (UnsupportedEncodingException e) {
            debug(getClass(), e.getMessage());
            return src;
        }
    }

}