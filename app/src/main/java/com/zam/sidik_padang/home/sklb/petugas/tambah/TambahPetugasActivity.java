package com.zam.sidik_padang.home.sklb.petugas.tambah;

import android.Manifest;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.AppCompatSpinner;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.res.ResourcesCompat;
import androidx.lifecycle.ViewModelProvider;
import androidx.loader.content.CursorLoader;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
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
import com.zam.sidik_padang.databinding.ActivityTambahPetugasBinding;
import com.zam.sidik_padang.home.sklb.petugas.vm.Petugas;
import com.zam.sidik_padang.home.sklb.petugas.vm.PetugasSource;
import com.zam.sidik_padang.home.sklb.petugas.vm.PetugasViewModel;
import com.zam.sidik_padang.home.sklb.petugas.vm.desa.Desa;
import com.zam.sidik_padang.home.sklb.petugas.vm.kab.Kabupaten;
import com.zam.sidik_padang.home.sklb.petugas.vm.kec.Kecamatan;
import com.zam.sidik_padang.profilku.model.StatusAgama;
import com.zam.sidik_padang.util.Config;
import com.zam.sidik_padang.util.Db;
import com.zam.sidik_padang.util.UploadAsyncTask;
import com.zam.sidik_padang.util.Util;
import com.zam.sidik_padang.util.VolleySingleton;
import com.zam.sidik_padang.util.customclasses.SpinnerListener;

public class TambahPetugasActivity extends BaseLogedinActivity
        implements View.OnClickListener {

    private final String VOLLEY_TAG = getClass().getName();
    private ActivityTambahPetugasBinding binding;
    private Dialog dialog;

    private EditText editTextNama, editTextEmail, editTextHp, editTextAlamat,
            //editTextKodePos,
            editTextTempatLahir, editTextTanggalLahir;

    private AppCompatSpinner spinnerKabupaten;
    private AppCompatSpinner spinnerKecamatan;
    private AppCompatSpinner spinnerDesa;

    private SpinnerAdapter adapterSpinnerProvinsi, adapterSpinnerKabupaten,
            adapterSpinnerKecamatan, adapterSpinnerDesa, adapterSpinnerJenisKelamin;
    private List<Map<String, String>> listProvinsi, listKabupaten, listKecamatan, listDesa, listJenisKelamin;
    private View progressBarSpinnerKabupaten, progressBarSpinnerKecamatan, progressBarSpinnerDesa;

    private ImageView imageViewFoto, imgAddFoto;
    private TextView textViewFoto;
    private String fotoLink = "";
    private ProgressBar progressBarFoto;

    private int selectedKabupaten = 0;
    private int selectedKecamatan = 0;
    private int selectedDesa = 0;
    private int selectedJenisKelamin = 0;

    private Calendar birthDayCalendar;

    private String lastLoadedKabupatenCode = "";
    private String lastLoadedKecamatanCode = "";
    private String lastLoadedDesaCode = "";

    private PetugasViewModel viewModel;
    private String tempKec = "", tempKab = "";
    boolean edit = false;
    private Petugas petugas;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityTambahPetugasBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        Toolbar toolbar = binding.toolbar;
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back);
        toolbar.setNavigationOnClickListener(view -> onBackPressed());
        edit = getIntent().hasExtra("data");
        String tt = edit ? "Edit Petugas" : "Tambah Petugas";
        getSupportActionBar().setTitle(tt);

        viewModel = new ViewModelProvider(this).get(PetugasViewModel.class);

        imageViewFoto = binding.ImageViewFoto;
        imgAddFoto = binding.imgAddFoto;
        textViewFoto = binding.TextViewFoto;
        progressBarFoto = binding.ProgressbarFoto;

        editTextNama = binding.EditTextName;
        editTextHp = binding.EditTextHP;
        editTextEmail = binding.EditTextEmail;
        editTextAlamat = binding.EditTextAddress;
        editTextTempatLahir = binding.EditTextTempatLahir;
        editTextTanggalLahir = binding.EditTextBirthday;

        AppCompatSpinner spinnerProvinsi = binding.SpinnerProvinsi;
        spinnerKabupaten = binding.SpinnerKabupaten;
        spinnerKecamatan = binding.SpinnerKecamatan;
        spinnerDesa = binding.SpinnerDesa;

        AppCompatSpinner spinnerJenisKelamin = binding.SpinnerGender;

        progressBarSpinnerKabupaten = binding.ProgressbarspinnerKabupaten;
        progressBarSpinnerKecamatan = binding.ProgressbarspinnerKecamatan;
        progressBarSpinnerDesa = binding.ProgressbarspinnerDesa;

        imgAddFoto.setOnClickListener(this);
        editTextTanggalLahir.setOnClickListener(this);
        binding.ButtonSimpan.setOnClickListener(this);

        listProvinsi = new ArrayList<>();
        Map<String, String> map = new HashMap<>();
        map.put(Db.TABLE_PROVINSI_ID, "12920");
        map.put(Db.TABLE_PROVINSI_NAMA, "SUMATERA BARAT");
        listProvinsi.add(map);
        adapterSpinnerProvinsi = new SpinnerAdapter(this, listProvinsi);
        spinnerProvinsi.setAdapter(adapterSpinnerProvinsi);
        spinnerProvinsi.setOnItemSelectedListener(new CustomSpinnerListener(spinnerProvinsi.getId()));
        spinnerProvinsi.setEnabled(true);

        listKabupaten = new ArrayList<>();
        map = new HashMap<>();
        map.put(Db.TABLE_PROVINSI_ID, "-1");
        map.put(Db.TABLE_PROVINSI_NAMA, getString(R.string._select_kabupaten_));
        listKabupaten.add(map);
        adapterSpinnerKabupaten = new SpinnerAdapter(this, listKabupaten);
        spinnerKabupaten.setAdapter(adapterSpinnerKabupaten);
        spinnerKabupaten.setOnItemSelectedListener(new CustomSpinnerListener(spinnerKabupaten.getId()));
        spinnerKabupaten.setEnabled(true);

        listKecamatan = new ArrayList<>();
        map = new HashMap<>();
        map.put(Db.TABLE_PROVINSI_ID, "-1");
        map.put(Db.TABLE_PROVINSI_NAMA, getString(R.string._select_kecamatan_));
        listKecamatan.add(map);
        adapterSpinnerKecamatan = new SpinnerAdapter(this, listKecamatan);
        spinnerKecamatan.setAdapter(adapterSpinnerKecamatan);
        spinnerKecamatan.setOnItemSelectedListener(new CustomSpinnerListener(spinnerKecamatan.getId()));
        spinnerKecamatan.setEnabled(true);

        listDesa = new ArrayList<>();
        map = new HashMap<>();
        map.put(Db.TABLE_PROVINSI_ID, "-1");
        map.put(Db.TABLE_PROVINSI_NAMA, getString(R.string._select_desa_));
        listDesa.add(map);
        adapterSpinnerDesa = new SpinnerAdapter(this, listDesa);
        spinnerDesa.setAdapter(adapterSpinnerDesa);
        spinnerDesa.setOnItemSelectedListener(new CustomSpinnerListener(spinnerDesa.getId()));
        spinnerDesa.setEnabled(false);

        listJenisKelamin = new ArrayList<>();
        map = new HashMap<>();
        map.put("id", "-1");
        map.put("nama", getString(R.string._select_gender_));
        listJenisKelamin.add(map);
        Map<String, String> map1 = new HashMap<>();
        map1.put("id", "1");
        map1.put("nama", "Laki-laki");
        listJenisKelamin.add(map1);
        Map<String, String> map2 = new HashMap<>();
        map2.put("id", "2");
        map2.put("nama", "Perempuan");
        listJenisKelamin.add(map2);

        adapterSpinnerJenisKelamin = new SpinnerAdapter(this, listJenisKelamin);
        spinnerJenisKelamin.setAdapter(adapterSpinnerJenisKelamin);
        spinnerJenisKelamin.setOnItemSelectedListener(new CustomSpinnerListener(spinnerJenisKelamin.getId()));

        if (edit) {
            petugas = new Gson().fromJson(getIntent().getStringExtra("data"), Petugas.class);
            editTextNama.setText(petugas.getNama());
            editTextHp.setText(petugas.getHp());
            if (petugas.getEmail() != null){
                editTextEmail.setVisibility(View.VISIBLE);
                editTextEmail.setText(petugas.getEmail());
            } else {
                editTextEmail.setVisibility(View.GONE);
            }
            editTextAlamat.setText(petugas.getAlamat());
            if (petugas.getEmail() != null){
                editTextTempatLahir.setVisibility(View.VISIBLE);
                editTextTempatLahir.setText(petugas.getTempatLahir());
            } else {
                editTextTempatLahir.setVisibility(View.GONE);
            }
        }
        observeViewModel();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        debug(getClass(), "onRequestPermission result");
        if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) launchPickPhotoIntent();

    }

    private void launchPickPhotoIntent() {
        Intent pickPhotoIntent = new Intent(Intent.ACTION_PICK);
        pickPhotoIntent.setType("image/*");
        startActivityForResult(pickPhotoIntent, 100);
    }

    private void observeViewModel() {
        viewModel.getResponseKab().observe(this, response -> {
            if (response != null) {
                if (response.state == State.SUCCESS && response.success && response.data != null) {
                    progressBarSpinnerKabupaten.setVisibility(View.INVISIBLE);
                    debug(getClass(), response.message);
                    setListKabupaten(response.data.getKabupaten());
                } else if (response.state == State.LOADING) {
                    progressBarSpinnerKabupaten.setVisibility(View.VISIBLE);
                    debug(getClass(), response.message);
                } else {
                    progressBarSpinnerKabupaten.setVisibility(View.INVISIBLE);
                    debug(getClass(), "error_message: " + response.message);
                }
            }
        });

        viewModel.getResponseKec().observe(this, response -> {
            if (response != null) {
                if (response.state == State.SUCCESS && response.success && response.data != null) {
                    progressBarSpinnerKecamatan.setVisibility(View.INVISIBLE);
                    debug(getClass(), response.message);
                    setListKecamatan(response.data.getKecamatan());
                } else if (response.state == State.LOADING) {
                    progressBarSpinnerKecamatan.setVisibility(View.VISIBLE);
                    debug(getClass(), response.message);
                } else {
                    progressBarSpinnerKecamatan.setVisibility(View.INVISIBLE);
                    debug(getClass(), "error_message: " + response.message);
                }
            }
        });

        viewModel.getResponseDesa().observe(this, response -> {
            if (response != null) {
                if (response.state == State.SUCCESS && response.success && response.data != null) {
                    progressBarSpinnerDesa.setVisibility(View.INVISIBLE);
                    debug(getClass(), response.message);
                    setListDesa(response.data.getDesa());
                } else if (response.state == State.LOADING) {
                    progressBarSpinnerDesa.setVisibility(View.VISIBLE);
                    debug(getClass(), response.message);
                } else {
                    progressBarSpinnerDesa.setVisibility(View.INVISIBLE);
                    debug(getClass(), "error_message: " + response.message);
                }
            }
        });

        viewModel.getResponseAdd().observe(this, response -> {
            if (response != null) {
                hideProgress();
                if (response.state == State.SUCCESS && response.success && response.data != null) {
                    new AlertDialog.Builder(this)
                            //.setTitle("")
                            .setMessage(response.message)
                            .setPositiveButton("Yes", (dialog, which) -> {
                                PetugasSource.getInstance().setPetugasUpdated(true);
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
        loadKabupatenFromServer("12920");
    }

    private void setListKabupaten(List<Kabupaten> kabupaten) {
        Map<String, String> map;
        if (listKabupaten.size() > 1) {
            listKabupaten.subList(1, listKabupaten.size()).clear();
        }

        for (Kabupaten kab : kabupaten) {
            if (kab == null) continue;
            map = new HashMap<>();
            map.put("id", kab.getId());
            map.put("nama", kab.getNama());
            listKabupaten.add(map);
        }

        if (edit && petugas.getKabupaten() != null) {
            spinnerKabupaten.setSelection(getSelectedSpinnerKabupaten());
        }

        adapterSpinnerKabupaten.notifyDataSetChanged();
        lastLoadedKabupatenCode = "12920";
    }

    private void setListKecamatan(List<Kecamatan> kecamatan) {
        Map<String, String> map;
        if (listKecamatan.size() > 1) {
            listKecamatan.subList(1, listKecamatan.size()).clear();
        }

        for (Kecamatan kec : kecamatan) {
            if (kec == null) continue;
            map = new HashMap<>();
            map.put("id", kec.getId());
            map.put("nama", kec.getNama());
            listKecamatan.add(map);
        }

        if (edit && petugas.getKecamatan() != null) {
            spinnerKecamatan.setSelection(getSelectedSpinnerKecamatan());
        }

        adapterSpinnerKecamatan.notifyDataSetChanged();
        lastLoadedKecamatanCode = tempKab;
    }

    private int getSelectedSpinnerKabupaten() {
        for (int i = 1; i < listKabupaten.size(); i++) {
            if (petugas.getKabupaten().equals(listKabupaten.get(i).get("nama"))) {
                return i;
            }
        }
        return 0;
    }

    private int getSelectedSpinnerKecamatan() {
        for (int i = 1; i < listKecamatan.size(); i++) {
            if (petugas.getKecamatan().equals(listKecamatan.get(i).get("nama"))) {
                return i;
            }
        }
        return 0;
    }

    private void setListDesa(List<Desa> desa) {
        Map<String, String> map;
        if (listDesa.size() > 1) {
            listDesa.subList(1, listDesa.size()).clear();
        }

        for (Desa des : desa) {
            if (des == null) continue;
            map = new HashMap<>();
            map.put("id", des.getId());
            map.put("nama", des.getNama());
            listDesa.add(map);
        }

        if (edit && petugas.getDesa() != null) {
            spinnerDesa.setSelection(getSelectedSpinnerDesa());
        }

        adapterSpinnerDesa.notifyDataSetChanged();
        lastLoadedDesaCode = tempKec;
    }

    private int getSelectedSpinnerDesa() {
        for (int i = 1; i < listDesa.size(); i++) {
            if (petugas.getDesa().equals(listDesa.get(i).get("nama"))) {
                return i;
            }
        }
        return 0;
    }

    @Override
    public void onClick(View p1) {
        int id = p1.getId();
        if (id == R.id.EditTextBirthday) {
            Calendar calendar = Calendar.getInstance();
            DatePickerDialog datePicker = new DatePickerDialog(this, (view, year, month, dayOfMonth) -> {
                birthDayCalendar = Calendar.getInstance();
                birthDayCalendar.set(Calendar.YEAR, year);
                birthDayCalendar.set(Calendar.MONTH, month);
                birthDayCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                editTextTanggalLahir.setText(new SimpleDateFormat("EEEE, d MMM yyyy", Locale.getDefault()).format(birthDayCalendar.getTime()));
            }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
            datePicker.show();
        } else if (id == R.id.ButtonSimpan) {
            validateAndGo();
        } else if (id == R.id.imgAddFoto) {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                launchPickPhotoIntent();
            } else {
                if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_EXTERNAL_STORAGE)) {
                    new AlertDialog.Builder(this).setMessage(R.string.need_to_access_media_storage).setPositiveButton(android.R.string.ok,
                            (dialog, which) -> ActivityCompat.requestPermissions(TambahPetugasActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 12)).show();
                } else
                    ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 12);
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == 100) {
                Uri uriHasil = data.getData();
                String stringHasil = getDir(uriHasil);
                textViewFoto.setText(getString(R.string.uploading, stringHasil));
                debug(getClass(), "ambil path dari uri. Hasil=" + stringHasil);

                if (stringHasil.contains("/")) {
                    BitmapFactory.Options option = new BitmapFactory.Options();
                    option.inJustDecodeBounds = true;
                    BitmapFactory.decodeFile(stringHasil, option);
                    int skala = 1;
                    boolean land = option.outWidth > option.outHeight;
                    int panjang = 640, pendek = 480;
                    if (land) {
                        if (option.outWidth > panjang && option.outHeight > pendek) {
                            skala = Math.min(option.outWidth / panjang, option.outHeight / pendek);
                        }
                    } else {
                        if (option.outWidth > pendek && option.outHeight > panjang) {
                            skala = Math.min(option.outWidth / pendek, option.outHeight / panjang);
                        }
                    }
                    option.inJustDecodeBounds = false;
                    option.inSampleSize = skala;
                    Bitmap bm = BitmapFactory.decodeFile(stringHasil, option);
                    File fBaru = new File(getCacheDir(), "foto_peternak.jpg");
                    if (fBaru.exists()) fBaru.delete();
                    try {
                        fBaru.createNewFile();
                        OutputStream os = new FileOutputStream(fBaru);
                        bm.compress(Bitmap.CompressFormat.JPEG, 90, os);
                        os.flush();
                        os.close();
                    } catch (IOException e) {
                        Log.e(getClass().getName(), e.getMessage());
                        return;
                    }
                    imageViewFoto.setImageBitmap(bm);
                    debug(getClass(), "loaded image width: " + bm.getWidth() + " height: " + bm.getHeight());
                    uploadGambar(fBaru.getAbsolutePath());
                    debug(getClass(), "fBaru: " + fBaru.getAbsolutePath());
                } else {
                    debug(getClass(), "Ambil path dari uri error. Hasil=" + stringHasil + " Uri=" + uriHasil + " IntentData=" + data);
                }
            }
        }
    }

    private String getDir(Uri uri) {
        Cursor cursor = new CursorLoader(this, uri, new String[]{"_data"}, (String) null, (String[]) null, (String) null).loadInBackground();
        int n = cursor.getColumnIndexOrThrow("_data");
        cursor.moveToFirst();
        return cursor.getString(n);
    }

    private void uploadGambar(String filePath) {
        if (!Util.isInternetAvailible(this)) return;
        HashMap<String, String> param = new HashMap<>();
        param.put("userid", user.userid);
        param.put("aksi", "1");
        //String[] splited = filePath.split("/");
        //param.put("ao", splited[splited.length - 1]);
        new UploadAsyncTask(Config.URL_TAMBAH_GAMBAR, filePath, param, new UploadAsyncTask.Callback() {

            @Override
            public void onStart() {
                progressBarFoto.setMax(100);
                progressBarFoto.setVisibility(View.VISIBLE);
                progressBarFoto.setProgress(4);
                debug(getClass(), "upload onstart");
            }

            @Override
            public void onProgressUpdate(int progressKb, int totalKb) {
                progressBarFoto.setProgress(progressKb * 100 / totalKb);
                debug(getClass(), "upload onProgress " + progressKb + "/" + totalKb);
            }

            @Override
            public void onEnd(JsonObject je) {
                debug(getClass(), "upload onEnd " + je);
                int text = R.string.upload_failed;
                Drawable drawable = null;

                if (je.get("success").getAsBoolean()) {
                    text = R.string.uploaded;
                    drawable = ResourcesCompat.getDrawable(getResources(), R.drawable.ic_check, getTheme());
                    int rb = (int) (getResources().getDisplayMetrics().density * 24f);
                    if (null != drawable) drawable.setBounds(0, 0, rb, rb);
                    fotoLink = je.get("link").getAsString();
                }

                textViewFoto.setCompoundDrawablesRelative(drawable, null, null, null);
                textViewFoto.setText(text);
                progressBarFoto.setVisibility(View.INVISIBLE);
            }
        });
    }

    private void showProgress(String title, String msg) {
        dialog = ProgressDialog.show(this, title, msg, true, false);
    }

    private void hideProgress() {
        if (dialog != null && dialog.isShowing()) dialog.dismiss();
    }

    private void validateAndGo() {
        Map<String, String> param = new HashMap<>();
        param.put("userid", user.userid);
        if (edit) {
            param.put("aksi", "2");
            param.put("id", petugas.getId());
        } else {
            param.put("aksi", "1");
        }
        param.put("foto", fotoLink);

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

        param.put("hp", hp);

        param.put("provinsi", "12920");

        if (selectedKabupaten == 0) {
            Util.showDialog(this, getString(R.string.please_select_kabupaten));
            return;
        }
        param.put("kabupaten", listKabupaten.get(selectedKabupaten).get("id"));

        if (selectedKecamatan == 0) {
            Util.showDialog(this, getString(R.string.please_select_kecamatan));
            return;
        }
        param.put("kecamatan", listKecamatan.get(selectedKecamatan).get("id"));

        if (selectedDesa == 0) {
            Util.showDialog(this, getString(R.string.please_select_desa));
            return;
        }
        param.put("desa", listDesa.get(selectedDesa).get("id"));

        param.put("id_jenis_kelamin", (selectedJenisKelamin == 0 ? "1" : listJenisKelamin.get(selectedJenisKelamin).get("id")));
        param.put("id_agama", "0");

        param.put("kode_pos", "0");
        param.put("alamat", editTextAlamat.getText().toString().trim());
        param.put("tanggal_lahir", (birthDayCalendar == null ? "" : new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(birthDayCalendar.getTime())));

        if (edit) {
            if (editTextEmail.getVisibility() == View.VISIBLE) {
                String email = editTextEmail.getText().toString().trim();
                param.put("email", email);
            }
            if (editTextTempatLahir.getVisibility() == View.VISIBLE) {
                param.put("tempat_lahir", editTextTempatLahir.getText().toString().trim());
            }
        } else {
            String email = editTextEmail.getText().toString().trim();
            param.put("email", email);
            param.put("tempat_lahir", editTextTempatLahir.getText().toString().trim());
        }

        viewModel.tambahPetugas(param);
    }

    private String encode(String src) {
        try {
            return URLEncoder.encode(src, "utf-8");
        } catch (UnsupportedEncodingException e) {
            debug(getClass(), e.getMessage());
            return src;
        }
    }

    private void loadKabupatenFromServer(final String s) {
        if (!Util.isInternetAvailible(this)) return;
        if (s.equals(lastLoadedKabupatenCode) && listKabupaten.size() > 1) return;

        Map<String, String> paramKab = new HashMap<>();
        paramKab.put("wilayah", "kabupaten");
        paramKab.put("code", s);
        paramKab.put("userid", user.userid);

        viewModel.loadListKabupaten(paramKab);
    }

    private void loadKecamatanFromServer(final String s) {
        if (!Util.isInternetAvailible(this)) return;
        if (s.equals(lastLoadedKecamatanCode) && listKecamatan.size() > 1) return;

        Map<String, String> paramKec = new HashMap<>();
        paramKec.put("wilayah", "kecamatan");
        paramKec.put("code", s);
        paramKec.put("userid", user.userid);

        viewModel.loadListKecamatan(paramKec);
    }

    private void loadDesaFromServer(final String s) {
        if (!Util.isInternetAvailible(this)) return;
        if (s.equals(lastLoadedDesaCode) && listDesa.size() > 1) return;
        Map<String, String> param = new HashMap<>();
        param.put("wilayah", "desa");
        param.put("code", s);
        param.put("userid", user.userid);
        viewModel.loadListDesa(param);
    }

    @Override
    protected void onDestroy() {
        VolleySingleton.getInstance(this).getRequestQueue().cancelAll(VOLLEY_TAG);
        super.onDestroy();
    }

    private void clearForm(ViewGroup group) {
        for (int i = 0, count = group.getChildCount(); i < count; ++i) {
            View view = group.getChildAt(i);
            if (view instanceof EditText) {
                ((EditText) view).setText("");
            }

            if (view instanceof ViewGroup && (((ViewGroup) view).getChildCount() > 0))
                clearForm((ViewGroup) view);
        }
    }

    private class CustomSpinnerListener extends SpinnerListener {
        private final int viewId;

        public CustomSpinnerListener(int viewId) {
            this.viewId = viewId;
        }

        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            if (viewId == R.id.SpinnerKabupaten) {
                selectedKabupaten = position;
                if (position == 0) {
                    spinnerKecamatan.setEnabled(false);
                    spinnerDesa.setEnabled(false);
                } else {
                    spinnerKecamatan.setEnabled(true);
                    tempKab = listKabupaten.get(position).get(Db.TABLE_PROVINSI_ID);
                    loadKecamatanFromServer(listKabupaten.get(position).get(Db.TABLE_PROVINSI_ID));
                }
            } else if (viewId == R.id.SpinnerKecamatan) {
                selectedKecamatan = position;
                if (position == 0) {
                    spinnerDesa.setEnabled(false);
                } else {
                    spinnerDesa.setEnabled(true);
                    tempKec = listKecamatan.get(position).get(Db.TABLE_PROVINSI_ID);
                    loadDesaFromServer(listKecamatan.get(position).get(Db.TABLE_PROVINSI_ID));
                }
            } else if (viewId == R.id.SpinnerDesa) {
                selectedDesa = position;
            } else if (viewId == R.id.SpinnerGender) {
                selectedJenisKelamin = position;
            }

        }
    }

}


