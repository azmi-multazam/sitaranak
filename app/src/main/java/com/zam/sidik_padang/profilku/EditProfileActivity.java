package com.zam.sidik_padang.profilku;

import android.Manifest;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
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
import android.util.Patterns;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.AppCompatSpinner;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.res.ResourcesCompat;
import androidx.loader.content.CursorLoader;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import com.zam.sidik_padang.BaseLogedinActivity;
import com.zam.sidik_padang.R;
import com.zam.sidik_padang.SelectRegionActivity;
import com.zam.sidik_padang.home.selectregion.Region;
import com.zam.sidik_padang.profilku.model.JenisKomoditas;
import com.zam.sidik_padang.profilku.model.JenisUsaha;
import com.zam.sidik_padang.profilku.model.LihatProfil;
import com.zam.sidik_padang.profilku.model.ProdukTernak;
import com.zam.sidik_padang.profilku.model.ProfileResponse;
import com.zam.sidik_padang.profilku.model.StatusAgama;
import com.zam.sidik_padang.util.Config;
import com.zam.sidik_padang.util.SelectDateDialogFragment;
import com.zam.sidik_padang.util.UploadAsyncTask;
import com.zam.sidik_padang.util.User;
import com.zam.sidik_padang.util.Util;
import com.zam.sidik_padang.util.VolleySingleton;
import com.zam.sidik_padang.util.VolleyStringRequest;
import io.paperdb.Paper;

import static com.zam.sidik_padang.util.Config.PREF_PROFIL_TERSIMPAN;

public class EditProfileActivity extends BaseLogedinActivity implements View.OnClickListener,
        SelectDateDialogFragment.OnCalendarSelectedListener {

    public static final String EXTRA_PROFILE_API_RESULT = "extra_profile_api_result";
    private final String VOLLEY_TAG = getClass().getName();
    private LihatProfil profile;
    //private ProfileApiResult profileApiResult;
    private ProfileResponse profileApiResult;
    private ImageView imageViewFoto;
    private String fotoLink = "";
    private TextView textViewFoto;
    private ProgressBar progressBarFoto;
    private EditText editTextNama, editTextTanggalLahir, editTextKtp, editTextProv, editTextKab,
            editTextKec, editTextDesa;
    //editTextLevel, editTextKelompok, editTextKelompokTernak;

    private EditText editTextNamaUsaha,/* editTextStatus,*/
            editTextTelepon, editTextEmail;

    private AppCompatSpinner spinnerAgama, spinnerKomoditas, spinnerJenisUsaha, spinnerProdukPenjualan;
    private View rowNamaOlahan;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_edit_profile_new);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(v -> onBackPressed());

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        /*
        Intent intent = getIntent();
        setResult(RESULT_CANCELED);
        if (!intent.hasExtra(EXTRA_PROFILE_API_RESULT)) {
            new AlertDialog.Builder(this).setMessage("Terjadi kesalahan")
                    .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            finish();
                        }
                    }).show();
            return;
        }
         */
        //profileApiResult = (ProfileApiResult) intent.getSerializableExtra(EXTRA_PROFILE_API_RESULT);
        profileApiResult = Paper.book().read(PREF_PROFIL_TERSIMPAN);
        profile = profileApiResult.getLihatProfil().get(0);
        imageViewFoto = findViewById(R.id.content_edit_profil_ImageViewFoto);
        if (!user.foto.isEmpty())
            Glide.with(this).load(user.foto).placeholder(R.drawable.ic_account_circle_white).into(imageViewFoto);
        //imageViewFoto.setOnClickListener(this);
        findViewById(R.id.content_edit_profil_addFoto).setOnClickListener(this);
        textViewFoto = findViewById(R.id.content_edit_profil_TextViewFoto);
        progressBarFoto = findViewById(R.id.content_edit_profil_ProgressbarFoto);
        //FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        //fab.setOnClickListener(this);
        editTextNama = findViewById(R.id.content_edit_profile_EditTextNama);
        editTextNama.setText(user.nama);
        spinnerAgama = findViewById(R.id.content_edit_profile_SpinnerAgama);
        List<Object> pilihan = new ArrayList<>();
        pilihan.add("-Pilih agama-");
        for (StatusAgama statusAgama : profileApiResult.getStatusAgama()) {
            pilihan.add(statusAgama.getStatus());
        }
        spinnerAgama.setAdapter(new PilihanSpinnerAdapter(this, pilihan));
        if (profile.getIdAgama() != null) for (int i = 1; i < pilihan.size(); i++) {
            for (StatusAgama statusAgama : profileApiResult.getStatusAgama()) {
                if (profile.getIdAgama().equals(statusAgama.getId())) {
                    spinnerAgama.setSelection(i);
                    break;
                }
            }
        }
        spinnerAgama.setOnItemSelectedListener(new SpinnerListener(spinnerAgama.getId()));

        spinnerKomoditas = findViewById(R.id.content_edit_profile_SpinnerKomoditas);
        pilihan = new ArrayList<>();
        pilihan.add("-Pilih Komoditas-");
        for (JenisKomoditas jenisKomoditas : profileApiResult.getJenisKomoditas()) {
            pilihan.add(jenisKomoditas.getJenis());
        }
        spinnerKomoditas.setAdapter(new PilihanSpinnerAdapter(this, pilihan));
        if (profile.getIdKomoditas() != null) for (int i = 1; i < pilihan.size(); i++) {
            for (JenisKomoditas jenisKomoditas : profileApiResult.getJenisKomoditas()) {
                if (profile.getIdKomoditas().equals(jenisKomoditas.getId())) {
                    spinnerKomoditas.setSelection(i);
                    break;
                }
            }
        }
        spinnerKomoditas.setOnItemSelectedListener(new SpinnerListener(spinnerKomoditas.getId()));

        spinnerJenisUsaha = findViewById(R.id.content_edit_profile_SpinnerJenisUsaha);
        pilihan = new ArrayList<>();
        pilihan.add("-Pilih Jenis Usaha-");
        for (JenisUsaha jenisUsaha : profileApiResult.getJenisUsaha()) {
            pilihan.add(jenisUsaha.getJenis());
        }
        spinnerJenisUsaha.setAdapter(new PilihanSpinnerAdapter(this, pilihan));
        if (profile.getIdJenisUsaha() != null) for (int i = 1; i < pilihan.size(); i++) {
            for (JenisUsaha jenisUsaha : profileApiResult.getJenisUsaha()) {
                if (profile.getIdJenisUsaha().equals(jenisUsaha.getId())) {
                    spinnerJenisUsaha.setSelection(i);
                    break;
                }
            }
        }
        spinnerJenisUsaha.setOnItemSelectedListener(new SpinnerListener(spinnerJenisUsaha.getId()));

        spinnerProdukPenjualan = findViewById(R.id.content_edit_profile_SpinnerProdukPenjualan);
        pilihan = new ArrayList<>();
        pilihan.add("-Pilih Produk Penjualan-");
        for (ProdukTernak produkTernak : profileApiResult.getProdukTernak()) {
            pilihan.add(produkTernak.getProduk());
        }
        spinnerProdukPenjualan.setAdapter(new PilihanSpinnerAdapter(this, pilihan));
        if (profile.getIdProdukPenjualan() != null) for (int i = 1; i < pilihan.size(); i++) {
            for (ProdukTernak produkTernak : profileApiResult.getProdukTernak()) {
                if (profile.getIdProdukPenjualan().equals(produkTernak.getId())) {
                    spinnerProdukPenjualan.setSelection(i);
                    break;
                }
            }
        }
        spinnerProdukPenjualan.setOnItemSelectedListener(new SpinnerListener(spinnerProdukPenjualan.getId()));

        editTextTanggalLahir = findViewById(R.id.content_edit_profile_EditTexTanggalLahir);
        editTextTanggalLahir.setText(profile.getTanggalLahir());
        editTextTanggalLahir.setOnClickListener(this);
        editTextKtp = findViewById(R.id.content_edit_profile_EditTextKtp);
        editTextKtp.setText(profile.getKtp());
        editTextProv = findViewById(R.id.content_edit_profile_EditTextProvinsi);
        editTextProv.setText(profile.getProvinsi());
        editTextProv.setOnClickListener(this);
        editTextKab = findViewById(R.id.content_edit_profile_EditTextKabupaten);
        editTextKab.setText(profile.getKabupaten());
        editTextKab.setOnClickListener(this);
        editTextKec = findViewById(R.id.content_edit_profile_EditTextKecamatan);
        editTextKec.setText(profile.getKecamatan());
        editTextKec.setOnClickListener(this);
        editTextDesa = findViewById(R.id.content_edit_profile_EditTextDesa);
        editTextDesa.setText(profile.getDesa());
        editTextDesa.setOnClickListener(this);
        /*
        editTextLevel = findViewById(R.id.content_edit_profile_EditTextLevel);
        editTextLevel.setText(user.level);
        editTextLevel.setFocusable(false);
        editTextKelompok = findViewById(R.id.content_edit_profile_EditTextKelompok);
        editTextKelompok.setText(profile.getKelompok());
        editTextKelompok.setFocusable(false);
        editTextKelompokTernak = findViewById(R.id.content_edit_profile_EditTextKelompokTernak);
        editTextKelompokTernak.setText(profile.getKelompokTernak());
        editTextKelompokTernak.setFocusable(false);
         */
        //data usaha
        editTextNamaUsaha = findViewById(R.id.content_edit_profile_EditTextNamaUsaha);
        editTextNamaUsaha.setText(profile.getNamaUsaha());
//		editTextStatus = (EditText) findViewById(R.id.content_edit_profile_EditTextStatus);
//		editTextStatus.setText(user.);
        editTextTelepon = findViewById(R.id.content_edit_profile_EditTextTelepon);
        editTextTelepon.setText(profile.getTelepon());
        editTextEmail = findViewById(R.id.content_edit_profile_EditTextEmail);
        editTextEmail.setText(profile.getEmail());
        rowNamaOlahan = findViewById(R.id.content_edit_profil_RowNamaProdukOlahan);
        rowNamaOlahan.setVisibility((profile.getOlahan() != null && profile.getOlahan().toLowerCase().contains("olahan")) ? View.VISIBLE : View.GONE);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.simpan, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.menu_simpan) {
            validate();
        }
        return true;
    }

    private void validate() {
        if (!Util.isInternetAvailible(this)) {
            Toast.makeText(this, "Tidak ada koneksi internet", Toast.LENGTH_SHORT).show();
            return;
        }

        String url = Config.URL_PROFILE + "?aksi=1&userid=" + user.userid;
        user.nama = editTextNama.getText().toString().trim();
        if (user.nama.isEmpty()) {
            editTextNama.setError("Harus diisi");
            editTextNama.requestFocus();
            return;
        }
        url += "&nama=" + Util.toUrlEncoded(user.nama);
        if (!profile.getTanggalLahir().isEmpty())
            url += "&tanggal_lahir=" + profile.getTanggalLahir();

        if (profile.getIdAgama() != null)
            url += "&id_agama=" + profile.getIdAgama();
        if (profile.getIdKomoditas() != null)
            url += "&id_komoditas=" + profile.getIdKomoditas();

        if (profile.getIdJenisUsaha() != null)
            url += "&id_jenis_usaha=" + profile.getIdJenisUsaha();
        if (profile.getIdProdukPenjualan() != null) {
            url += "&id_produk_penjualan=" + profile.getIdProdukPenjualan();
            if (!profile.getIdProdukPenjualan().isEmpty())
                for (ProdukTernak pt : profileApiResult.getProdukTernak())
                    if (pt.getId().equals(profile.getIdProdukPenjualan()) && pt.getProduk().toLowerCase().contains("olahan")) {
                        TextView olahanView = rowNamaOlahan.findViewById(R.id.content_edit_profile_EditTextNamaProdukOlahan);
                        String olahanString = olahanView.getText().toString().trim();
                        if (olahanString.isEmpty()) {
                            olahanView.setError("Masukan nama produk olahan");
                            olahanView.requestFocus();
                            return;
                        } else url += "&olahan=" + Util.toUrlEncoded(olahanString);
                        break;
                    }


        }

        if (!fotoLink.isEmpty()) url += "&foto=" + fotoLink;

        String ktp = editTextKtp.getText().toString().trim();
        if (!ktp.isEmpty()) url += "&ktp=" + ktp;
        url += "&id_provinsi=" + profile.getIdProvinsi();
        if (profile.getIdKabupaten().isEmpty()) {
            Util.showDialog(this, "Kabupaten harus dipilih");
            return;
        }
        url += "&id_kabupaten=" + profile.getIdKabupaten();
        if (profile.getIdKecamatan().isEmpty()) {
            Util.showDialog(this, "Kecamatan harus dipilih");
            return;
        }
        url += "&id_kecamatan=" + profile.getIdKecamatan();
        if (profile.getIdDesa().isEmpty()) {
            Util.showDialog(this, "Desa harus dipilih");
            return;
        }
        url += "&id_desa=" + profile.getIdDesa();

        profile.setNamaUsaha(editTextNamaUsaha.getText().toString().trim());
        if (!profile.getNamaUsaha().isEmpty())
            url += "&nama_usaha=" + Util.toUrlEncoded(profile.getNamaUsaha());
        profile.setTelepon(editTextTelepon.getText().toString().trim());
        if (!profile.getTelepon().isEmpty()) {
            if (Patterns.PHONE.matcher(profile.getTelepon()).matches()) {
                url += "&telepon=" + Util.toUrlEncoded(profile.getTelepon());
            } else {
                Util.showDialog(this, "Nomor telepon tidak valid");
                editTextTelepon.setError("Nomor tidak valid");
                return;
            }
        }
        profile.setEmail(editTextEmail.getText().toString().trim());
        if (!profile.getEmail().isEmpty()) {
            if (Patterns.EMAIL_ADDRESS.matcher(profile.getEmail()).matches()) {
                url += "&email=" + Util.toUrlEncoded(profile.getEmail());
            } else {
                Util.showDialog(this, "Alamat email tidak valid");
                editTextEmail.setError("Email tidak valid");
                return;
            }
        }

        dialogKonfirmasi(url);

    }

    private void dialogKonfirmasi(final String url) {
        new AlertDialog.Builder(this)
                .setTitle("Konfirmasi")
                .setMessage("Simpan perubahan?")
                .setPositiveButton("Simpan", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        simpanKeServer(url);
                    }
                })
                .show();
    }

    private void simpanKeServer(String url) {
        final Dialog dialog = ProgressDialog.show(this, null, "Menyimpan data...", true, false);
        debug(getClass(), "Simpan profil url=" + url);
        VolleyStringRequest request = new VolleyStringRequest(url, new VolleyStringRequest.Callback() {
            @Override
            public void onResponse(JsonObject jsonObject) {
                dialog.dismiss();
                debug(getClass(), "Simpan profile response " + jsonObject);
                if (jsonObject.get("success").getAsBoolean()) {
                    Toast.makeText(EditProfileActivity.this, "Berhasil", Toast.LENGTH_SHORT).show();
                    setResult(RESULT_OK);
                    if (!fotoLink.isEmpty()) {
                        JsonObject jsonUser = new Gson().fromJson(sharedPreferences.getString(Config.PREF_USER_DETAIL_JSON, ""), JsonElement.class).getAsJsonObject();
                        jsonUser.addProperty("foto", fotoLink);
                        sharedPreferences.edit().putString(Config.PREF_USER_DETAIL_JSON, jsonUser.toString()).commit();
                        user = new Gson().fromJson(jsonUser, User.class);
                    }


                    finish();
                } else
                    Util.showDialog(EditProfileActivity.this, jsonObject.get("message").getAsString());
            }
        });
        request.setTag(VOLLEY_TAG);
        VolleySingleton.getInstance(this).getRequestQueue().add(request);
    }


    @Override
    public void onClick(View v) {
        int id = v.getId();
		/*if (id == R.id.fab) {
			validate();
		} else */
        if (id == R.id.content_edit_profil_addFoto) {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                launchPickPhotoIntent();
            } else {
                if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_EXTERNAL_STORAGE)) {
                    new AlertDialog.Builder(this).setMessage(R.string.need_to_access_media_storage).setPositiveButton(android.R.string.ok,
                            (dialog, which) -> ActivityCompat.requestPermissions(EditProfileActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 12)).show();
                } else
                    ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 12);
            }
        } else if (id == R.id.content_edit_profile_EditTextProvinsi) {
            Intent it = new Intent(this, SelectRegionActivity.class);
            it.putExtra(SelectRegionActivity.EXTRA_WILAYAH_MODE, "provinsi");
            it.putExtra(SelectRegionActivity.EXTRA_WILAYAH_CODE, "0");
            startActivityForResult(it, 1);
        } else if (id == R.id.content_edit_profile_EditTextKabupaten) {
            Intent it = new Intent(this, SelectRegionActivity.class);
            it.putExtra(SelectRegionActivity.EXTRA_WILAYAH_MODE, "kabupaten");
            it.putExtra(SelectRegionActivity.EXTRA_WILAYAH_CODE, profile.getIdProvinsi());
            startActivityForResult(it, 2);
        } else if (id == R.id.content_edit_profile_EditTextKecamatan) {
            Intent it = new Intent(this, SelectRegionActivity.class);
            it.putExtra(SelectRegionActivity.EXTRA_WILAYAH_MODE, "kecamatan");
            it.putExtra(SelectRegionActivity.EXTRA_WILAYAH_CODE, profile.getIdKabupaten());
            startActivityForResult(it, 3);
        } else if (id == R.id.content_edit_profile_EditTextDesa) {
            Intent it = new Intent(this, SelectRegionActivity.class);
            it.putExtra(SelectRegionActivity.EXTRA_WILAYAH_MODE, "desa");
            it.putExtra(SelectRegionActivity.EXTRA_WILAYAH_CODE, profile.getIdKecamatan());
            startActivityForResult(it, 4);
        } else if (id == R.id.content_edit_profile_EditTexTanggalLahir) {
            CalendarDay userDate = null;

            if (!profile.getTanggalLahir().isEmpty()) {
                String separator = profile.getTanggalLahir().contains("-") ? "-" : "/";
                String[] splited = profile.getTanggalLahir().split(separator);
                if (splited.length == 3) {
                    userDate = CalendarDay.from(Integer.parseInt(splited[2]), Integer.parseInt(splited[1]) - 1, Integer.parseInt(splited[0]));
                }
            }
            Calendar calendarMin = Calendar.getInstance();
            calendarMin.set(Calendar.YEAR, calendarMin.get(Calendar.YEAR) - 48);
            Calendar calendarMax = Calendar.getInstance();
            calendarMax.set(Calendar.YEAR, calendarMax.get(Calendar.YEAR) - 27);
//			SelectDateDialogFragment.getDialog(userDate == null ? CalendarDay.from(calendarMax) : userDate, calendarMin.getTimeInMillis(), calendarMax.getTimeInMillis())
//					.show(getSupportFragmentManager(), "dialog");
            DatePickerDialog dpd = DatePickerDialog.newInstance(new DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
                    String date = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(CalendarDay.from(year, monthOfYear, dayOfMonth).getCalendar().getTimeInMillis());
                    editTextTanggalLahir.setText(date);
                    profile.setTanggalLahir(date);
                }
            }, userDate == null ? calendarMax : userDate.getCalendar());
            dpd.setVersion(DatePickerDialog.Version.VERSION_2);
            dpd.showYearPickerFirst(true);
            dpd.show(getSupportFragmentManager(), "Tanggal");
        }
    }

    @Override
    public void onCalendarSelected(CalendarDay calendarDay) {
        String date = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(calendarDay.getDate());
        editTextTanggalLahir.setText(date);
        profile.setTanggalLahir(date);
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
        startActivityForResult(pickPhotoIntent, 13);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == 13) {
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
            } else if (requestCode == 1) {
                Region region = (Region) data.getSerializableExtra(SelectRegionActivity.EXTRA_WILAYAH);
                editTextProv.setText(region.nama);
                if (profile.getIdProvinsi().equals(region.id)) return;
                profile.setIdProvinsi(region.id);
                editTextKab.setText("");
                profile.setIdKabupaten("");
                editTextKec.setText("");
                profile.setIdKecamatan("");
                editTextDesa.setText("");
                profile.setIdDesa("");
                debug(getClass(), "Wilayah:" + region.id);
            } else if (requestCode == 2) {
                Region region = (Region) data.getSerializableExtra(SelectRegionActivity.EXTRA_WILAYAH);
                editTextKab.setText(region.nama);
                if (profile.getIdKabupaten().equals(region.id)) return;
                profile.setIdKabupaten(region.id);
                editTextKec.setText("");
                profile.setIdKecamatan("");
                editTextDesa.setText("");
                profile.setIdDesa("");
            } else if (requestCode == 3) {
                Region region = (Region) data.getSerializableExtra(SelectRegionActivity.EXTRA_WILAYAH);
                editTextKec.setText(region.nama);
                if (profile.getIdKecamatan().equals(region.id)) return;
                profile.setIdKecamatan(region.id);
                editTextDesa.setText("");
                profile.setIdDesa("");
            } else if (requestCode == 4) {
                Region region = (Region) data.getSerializableExtra(SelectRegionActivity.EXTRA_WILAYAH);
                editTextDesa.setText(region.nama);
                profile.setIdDesa(region.id);
            }
        }
    }

    private void uploadGambar(String filePath) {
        if (!Util.isInternetAvailible(this)) return;
        HashMap<String, String> param = new HashMap<>();
        param.put("userid", user.userid);
        param.put("aksi", "2");
        String[] splited = filePath.split("/");
        param.put("ao", splited[splited.length - 1]);
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

    private String getDir(Uri uri) {
        Cursor cursor = new CursorLoader(this, uri, new String[]{"_data"}, null, null, null).loadInBackground();
        int n = cursor.getColumnIndexOrThrow("_data");
        cursor.moveToFirst();
        return cursor.getString(n);
    }

    @Override
    protected void onDestroy() {
        VolleySingleton.getInstance(this).getRequestQueue().cancelAll(VOLLEY_TAG);
        super.onDestroy();
    }

    private class SpinnerListener implements AdapterView.OnItemSelectedListener {
        int viewId;

        public SpinnerListener(int id) {
            this.viewId = id;
        }

        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

            if (position == 0) return;
            if (viewId == R.id.content_edit_profile_SpinnerAgama)
                profile.setIdAgama(String.valueOf(profileApiResult.getStatusAgama().get(position - 1).getId()));
            else if (viewId == R.id.content_edit_profile_SpinnerKomoditas)
                profile.setIdKomoditas(String.valueOf(profileApiResult.getJenisKomoditas().get(position - 1).getId()));
            else if (viewId == R.id.content_edit_profile_SpinnerJenisUsaha)
                profile.setIdJenisUsaha(String.valueOf(profileApiResult.getJenisUsaha().get(position - 1).getId()));
            else if (viewId == R.id.content_edit_profile_SpinnerProdukPenjualan) {
                ProdukTernak pt = profileApiResult.getProdukTernak().get(position - 1);
                profile.setIdProdukPenjualan(String.valueOf(pt.getId()));
                if (pt.getProduk().toLowerCase().contains("olahan")) {
                    rowNamaOlahan.setVisibility(View.VISIBLE);
                    ((TextView) rowNamaOlahan.findViewById(R.id.content_edit_profile_EditTextNamaProdukOlahan))
                            .setText(profile.getOlahan() != null ? profile.getOlahan() : "");
                } else {
                    rowNamaOlahan.setVisibility(View.GONE);
                    profile.setOlahan(null);
                }
            }
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {

        }
    }

}
