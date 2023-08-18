package com.zam.sidik_padang.home.ppob.iklanmerchant.iklanumum;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.CursorLoader;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.AppCompatSpinner;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.zam.sidik_padang.BaseLogedinActivity;
import com.zam.sidik_padang.R;
import com.zam.sidik_padang.home.ppob.iklanmerchant.BaseIklanListFragment;
import com.zam.sidik_padang.home.ppob.iklanmerchant.Category;
import com.zam.sidik_padang.util.Config;
import com.zam.sidik_padang.util.Util;
import com.zam.sidik_padang.util.VolleySingleton;
import com.zam.sidik_padang.util.VolleyStringRequest;
import com.zam.sidik_padang.util.customclasses.SpinnerAdapter;

import static com.zam.sidik_padang.home.ppob.iklanmerchant.BaseIklanListFragment.PREF_SAVED_CATEGORIES;

/**
 * Created by supriyadi on 3/11/18.
 */

public class TambahIklanActivity extends BaseLogedinActivity
        implements View.OnClickListener, AdapterView.OnItemSelectedListener {

    private static final String TAG = TambahIklanActivity.class.getName();
    private static final int REQUEST_KODE = 13;
    protected Gson gson;
    private int selectedKategory = 0;
    private SpinnerAdapter spinnerAdapter;
    private List<Object> categoryList;
    private View progressBarSpinner;
    private SharedPreferences sharedPreferences;
    private ImageView imageView;
    private File gambar;
    private TextView textViewToko, textViewHarga, textViewKeterangan;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setResult(RESULT_CANCELED);
        setContentView(R.layout.activity_tambah_iklan);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        textViewToko = (TextView) findViewById(R.id.editTextToko);
        textViewHarga = (TextView) findViewById(R.id.editTextHarga);
        textViewKeterangan = (TextView) findViewById(R.id.editTextKeterangan);

        findViewById(R.id.button).setOnClickListener(this);
        imageView = (ImageView) findViewById(R.id.imageView);
        imageView.setOnClickListener(this);
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        gson = new Gson();
        progressBarSpinner = findViewById(R.id.progressBarSpinner);
        categoryList = new ArrayList<>();
        categoryList.add("-Pilih Kategori-");
        spinnerAdapter = new SpinnerAdapter(getResources(), categoryList);
        AppCompatSpinner spinner = (AppCompatSpinner) findViewById(R.id.spinner);
        spinner.setAdapter(spinnerAdapter);
        spinner.setSelection(selectedKategory);
        spinner.setOnItemSelectedListener(this);
        loadCategories();
        String saved = sharedPreferences.getString(PREF_SAVED_CATEGORIES, null);
        if (saved != null && saved.startsWith("{")) {
            BaseIklanListFragment.CategoriesResponse response = gson.fromJson(saved, BaseIklanListFragment.CategoriesResponse.class);
            if (response != null) updateSpinnerList(response);
        }
    }

    private void updateSpinnerList(BaseIklanListFragment.CategoriesResponse response) {
        categoryList.clear();
        categoryList.add("-Pilih Kategori-");
        categoryList.addAll(response.kategori_merchent);
        spinnerAdapter.notifyDataSetChanged();
        if (progressBarSpinner.getVisibility() == View.VISIBLE)
            progressBarSpinner.setVisibility(View.GONE);

    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        if (id == R.id.imageView) {
            pickImage();
        } else if (id == R.id.button) {
            kirimIklan();
        }

    }

    @Override
    public void onItemSelected(AdapterView<?> p1, View p2, int p3, long p4) {
        selectedKategory = p3;
//		Category category=selectedKategory==0?null: (Category)categoryList.get(selectedKategory);
//		onCategorySelected(category);
    }

    @Override
    public void onNothingSelected(AdapterView<?> p1) {
        // TODO: Implement this method
    }

    private void loadCategories() {
        if (!Util.isInternetAvailible(this)) return;

        String link = Config.URL_MERCHANT_LIST + "aksi=2&userid=" + user.userid;
        debug(getClass(), "Load iklan categories. Link: " + link);
        progressBarSpinner.setVisibility(View.VISIBLE);
        VolleyStringRequest request = new VolleyStringRequest(link, new VolleyStringRequest.Callback() {

            @Override
            public void onResponse(JsonObject jsonObject) {
                progressBarSpinner.setVisibility(View.GONE);
                debug(getClass(), "Load categori iklan response: " + jsonObject);
                BaseIklanListFragment.CategoriesResponse response = gson.fromJson(jsonObject, BaseIklanListFragment.CategoriesResponse.class);
                if (response == null) return;
                if (response.success) {
                    updateSpinnerList(response);
                    sharedPreferences.edit().putString(PREF_SAVED_CATEGORIES, jsonObject.toString()).apply();
                } else {
                    if (isResummed)
                        Toast.makeText(TambahIklanActivity.this, response.message, Toast.LENGTH_SHORT).show();
                }
            }
        });

        request.setTag(TAG);
        VolleySingleton.getInstance(this).getRequestQueue().add(request);
    }

    @Override
    protected void onDestroy() {
        VolleySingleton.getInstance(this).getRequestQueue().cancelAll(TAG);
        super.onDestroy();
    }


    private void pickImage() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
            launchPickPhotoIntent();
        } else {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_EXTERNAL_STORAGE)) {
                new AlertDialog.Builder(this).setMessage("Ambil gambar dari gallery?").setPositiveButton(android.R.string.ok,
                        new DialogInterface.OnClickListener() {
                            @SuppressLint("InlinedApi")
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                ActivityCompat.requestPermissions(TambahIklanActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 12);
                            }
                        }).show();
            } else
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 12);
        }
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
        startActivityForResult(pickPhotoIntent, REQUEST_KODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_KODE) {
            if (resultCode == RESULT_OK) {
                Uri uriHasil = data.getData();
                String stringHasil = getDir(uriHasil);
                debug(getClass(), "ambil path dari uri. Hasil=" + stringHasil);

                if (stringHasil.contains("/")) {

                    BitmapFactory.Options option = new BitmapFactory.Options();
                    option.inJustDecodeBounds = true;
                    BitmapFactory.decodeFile(stringHasil, option);
                    int skala = 1;
                    boolean land = option.outWidth > option.outHeight;
                    int panjang = 1024, pendek = 720;
                    if (land) {
                        if (option.outWidth > panjang && option.outHeight > pendek) {
                            skala = Math.min(option.outWidth / panjang, option.outHeight / pendek);
                        }
                    } else {
                        if (option.outWidth > pendek && option.outHeight > panjang) {
                            skala = Math.min(option.outWidth / pendek, option.outHeight / panjang);
                        }
                    }
                    debug(getClass(), "loaded image asli width: " + option.outWidth + " height: " + option.outHeight);

                    option.inJustDecodeBounds = false;
                    option.inSampleSize = skala;
                    Bitmap bm = BitmapFactory.decodeFile(stringHasil, option);
                    File fBaru = new File(getCacheDir(), "foto_" + user.userid + "_" + System.currentTimeMillis() + ".jpg");
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
                    imageView.setImageBitmap(bm);
                    debug(getClass(), "loaded image width: " + bm.getWidth() + " height: " + bm.getHeight());
                    //uploadGambar(fBaru.getAbsolutePath());
                    gambar = fBaru;
                    debug(getClass(), "fBaru: " + fBaru.getAbsolutePath());
                } else {
                    finish();
                    debug(getClass(), "Ambil path dari uri error. Hasil=" + stringHasil + " Uri=" + uriHasil + " IntentData=" + data);
                }
            } else {
                finish();
            }
        } else
            super.onActivityResult(requestCode, resultCode, data);
    }

    private String getDir(Uri uri) {
        Cursor cursor = new CursorLoader(this, uri, new String[]{"_data"}, (String) null, (String[]) null, (String) null).loadInBackground();
        int n = cursor.getColumnIndexOrThrow("_data");
        cursor.moveToFirst();
        return cursor.getString(n);
    }

    private void kirimIklan() {
        if (gambar == null || !gambar.exists()) {
            Toast.makeText(this, "Gambar harus disertakan", Toast.LENGTH_SHORT).show();
            return;
        }
        String toko = textViewToko.getText().toString().trim();
        if (toko.isEmpty()) {
            textViewToko.requestFocus();
            textViewToko.setError("Harus diisi");
            return;
        }

        String harga = textViewHarga.getText().toString().trim();
        if (harga.isEmpty()) {
            textViewHarga.requestFocus();
            textViewHarga.setText("Harus diisi");
            return;
        }

        if (selectedKategory == 0) {
            Toast.makeText(this, "Kategori harus dipilih", Toast.LENGTH_SHORT).show();
            return;
        }

        String keterangan = textViewKeterangan.getText().toString().trim();
        if (keterangan.isEmpty()) {
            textViewKeterangan.requestFocus();
            textViewKeterangan.setError("Harus diisi");
            return;
        }

        String link = Config.URL_MERCHANT_LIST + "aksi=6&userid=" + user.userid;
        link += "&toko=" + Util.toUrlEncoded(toko);
        link += "&harga=" + harga;
        link += "&kategori_merchent=" + ((Category) (categoryList.get(selectedKategory))).id_kategori;
        link += "&keterangan=" + Util.toUrlEncoded(keterangan);
        link += "&gambar=" + gambar.getName();

        if (!Util.isInternetAvailible(this)) {
            Toast.makeText(this, "Tidak ada koneksi internet", Toast.LENGTH_SHORT).show();
            return;
        }
		/*
		 list_mercent.php?aksi=6&userid=MB1000005&toko=berkah&harga=20000&kategori_merchent=2&keterangan=barang ready&gambar=foto.jpg
		*/
        HashMap<String, String> param = new HashMap<>();
        param.put("userid", user.userid);
        param.put("aksi", "6");
        param.put("toko", toko);
        param.put("harga", harga);
        param.put("kategori_merchent", ((Category) (categoryList.get(selectedKategory))).id_kategori);
        param.put("keterangan", keterangan);
        param.put("gambar", gambar.getName());
        debug(getClass(), "Tambah iklan. Link: " + link);
        final Dialog d = Util.showProgressDialog(this, "Harap tunggu", false);
        final ProgressBar progressBar = (ProgressBar) d.findViewById(R.id.progressBar);
        new UploadAsyncTask(link, gambar.getAbsolutePath(), param, new UploadAsyncTask.Callback() {
            @Override
            public void onStart() {
                debug(getClass(), "upload onstart");
                progressBar.post(new Runnable() {
                    @Override
                    public void run() {
                        progressBar.setIndeterminate(false);
                        progressBar.setProgress(0);
                        progressBar.setMax(100);
                    }
                });
            }

            @Override
            public void onProgressUpdate(int progressKb, int totalKb) {
                debug(getClass(), "upload onProgress " + progressKb + "/" + totalKb);
                progressBar.setProgress(progressKb * 100 / totalKb);
            }

            @Override
            public void onEnd(JsonObject je) {
                d.dismiss();
                debug(getClass(), "upload onEnd " + je);
                if (je.get("success").getAsBoolean()) {
                    Toast.makeText(TambahIklanActivity.this, "Berhasil", Toast.LENGTH_SHORT).show();
                    setResult(RESULT_OK);
                    finish();
                } else {
                    Toast.makeText(TambahIklanActivity.this, je.get("message").getAsString(), Toast.LENGTH_SHORT).show();

                }
            }
        });

    }


}
