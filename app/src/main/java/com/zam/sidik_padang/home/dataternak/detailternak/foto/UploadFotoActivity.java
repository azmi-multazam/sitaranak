package com.zam.sidik_padang.home.dataternak.detailternak.foto;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.loader.content.CursorLoader;

import com.google.gson.JsonObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;

import com.zam.sidik_padang.BaseLogedinActivity;
import com.zam.sidik_padang.R;
import com.zam.sidik_padang.util.Config;
import com.zam.sidik_padang.util.UploadAsyncTask;
import com.zam.sidik_padang.util.Util;
import com.zam.sidik_padang.util.VolleySingleton;
import com.zam.sidik_padang.util.VolleyStringRequest;

public class UploadFotoActivity extends BaseLogedinActivity {

    private static final int REQUEST_KODE = 13;
    private static final String VOLLEY_TAG = UploadFotoActivity.class.getName();
    private ImageView imageView;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_foto);
        imageView = (ImageView) findViewById(R.id.activity_upload_foto_ImageView);
        progressBar = (ProgressBar) findViewById(R.id.activity_upload_foto_Progressbar);
        pickImage();
        setResult(RESULT_CANCELED);
    }

    private void pickImage() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                launchPickPhotoIntent();
            } else {
                if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_EXTERNAL_STORAGE)) {
                    new AlertDialog.Builder(this).setMessage(R.string.need_to_access_media_storage).setPositiveButton(android.R.string.ok,
                            new DialogInterface.OnClickListener() {
                                @SuppressLint("InlinedApi")
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    ActivityCompat.requestPermissions(UploadFotoActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 12);
                                }
                            }).show();
                } else
                    ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 12);
            }
        } else launchPickPhotoIntent();
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
                    imageView.setImageBitmap(bm);
                    debug(getClass(), "loaded image width: " + bm.getWidth() + " height: " + bm.getHeight());
                    uploadGambar(fBaru.getAbsolutePath());
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

    private void uploadGambar(final String filePath) {
        if (!Util.isInternetAvailible(this)) {
            Toast.makeText(this, "Tidak ada koneksi internet", Toast.LENGTH_SHORT).show();
            return;
        }
        HashMap<String, String> param = new HashMap<>();
        param.put("userid", user.userid);
        param.put("aksi", "1");
        String[] splited = filePath.split("/");
        param.put("ao", splited[splited.length - 1]);
        new UploadAsyncTask(Config.URL_TAMBAH_GAMBAR, filePath, param, new UploadAsyncTask.Callback() {

            @Override
            public void onStart() {
                progressBar.setMax(100);
                progressBar.setVisibility(View.VISIBLE);
                progressBar.setProgress(4);
                debug(getClass(), "upload onstart");
            }

            @Override
            public void onProgressUpdate(int progressKb, int totalKb) {
                progressBar.setProgress(progressKb * 100 / totalKb);
                debug(getClass(), "upload onProgress " + progressKb + "/" + totalKb);
            }

            @Override
            public void onEnd(JsonObject je) {
                debug(getClass(), "upload onEnd " + je);
                if (je.get("success").getAsBoolean()) {
                    updateDb(je.get("link").getAsString());
                } else {
                    Toast.makeText(UploadFotoActivity.this, "Upload gagal", Toast.LENGTH_SHORT).show();
                    finish();
                }
            }
        });

    }

    private void updateDb(String link) {
        if (!Util.isInternetAvailible(this)) {
            Toast.makeText(this, "Tidak ada koneksi internet", Toast.LENGTH_SHORT).show();
            finish();
        }
        String url = Config.URL_GAMBAR_SAPI + "?aksi=2&userid=" + user.userid;
        Intent it = getIntent();
        if (it.hasExtra("id_ternak")) url += "&id_ternak=" + it.getStringExtra("id_ternak");
        url += "&gambar=" + link;
        debug(getClass(), "Tambah foto url=" + url);
        final Dialog d = ProgressDialog.show(this, null, "Harap tunggu...", true, false);
        VolleyStringRequest request = new VolleyStringRequest(url, new VolleyStringRequest.Callback() {
            @Override
            public void onResponse(JsonObject jsonObject) {
                debug(getClass(), "Tambah foto response=" + jsonObject);
                d.dismiss();
                if (jsonObject.get("success").getAsBoolean()) {
                    setResult(RESULT_OK);
                    Toast.makeText(UploadFotoActivity.this, "Berhasil", Toast.LENGTH_SHORT).show();
                    finish();
                } else
                    Toast.makeText(UploadFotoActivity.this, "Terjadi kesalahan", Toast.LENGTH_SHORT).show();
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
