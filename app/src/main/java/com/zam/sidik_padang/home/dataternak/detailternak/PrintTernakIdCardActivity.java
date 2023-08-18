package com.zam.sidik_padang.home.dataternak.detailternak;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.os.EnvironmentCompat;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.zxing.BarcodeFormat;

import java.io.File;
import java.lang.ref.WeakReference;
import java.util.List;

import com.zam.sidik_padang.BaseLogedinActivity;
import com.zam.sidik_padang.R;
import com.zam.sidik_padang.home.dataternak.detailternak.foto.FotoTernak;
import com.zam.sidik_padang.util.Config;
import com.zam.sidik_padang.util.HttpUrlConnectionApi;
import com.zam.sidik_padang.util.Util;
import com.zam.sidik_padang.util.customclasses.PrintPreviewLayout;

/**
 * Created by supriyadi on 4/3/18.
 */

public class PrintTernakIdCardActivity extends BaseLogedinActivity {

    private String idTernak;
    private ImageView imagBarcode;
    private PrintPreviewLayout printPreviewLayout;
    private View buttonPrint, progressBar;
    private SharedPreferences pref;

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (idTernak != null) outState.putString("idTernak", idTernak);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        if (savedInstanceState != null && savedInstanceState.containsKey("idTernak"))
            idTernak = savedInstanceState.getString("idTernak");
        super.onRestoreInstanceState(savedInstanceState);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_print_ternak_idcard);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Print ID Card Ternak");
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        if (savedInstanceState != null && savedInstanceState.containsKey("idTernak"))
            idTernak = savedInstanceState.getString("idTernak");
        else {
            Intent it = getIntent();
            if (it.hasExtra("idTernak")) idTernak = it.getStringExtra("idTernak");
        }

        if (idTernak == null) {
            Toast.makeText(this, "Terjadi kesalahan 8274", Toast.LENGTH_SHORT).show();
            return;
        }
        pref = PreferenceManager.getDefaultSharedPreferences(this);
        printPreviewLayout = (PrintPreviewLayout) findViewById(R.id.printPreviewLayout);
        imagBarcode = (ImageView) findViewById(R.id.imageBarcode);
        progressBar = findViewById(R.id.progressBar);
        buttonPrint = findViewById(R.id.button);
        buttonPrint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                print();
            }
        });


        imagBarcode.postDelayed(new Runnable() {
            @Override
            public void run() {
                new LoadBarcode(PrintTernakIdCardActivity.this).execute();
            }
        }, 200);

        String tersimpan = pref.getString(Config.PREF_FOTO_TERNAK_ALL_TERSIMPAN + idTernak, "");
        if (!tersimpan.isEmpty())
            bindFotoTernak(new Gson().fromJson(tersimpan, JsonElement.class).getAsJsonArray());
        loadDetailTernak(((TextView) findViewById(R.id.textViewNomorIrtek)));
        loadFotoFromServer();
        ((TextView) findViewById(R.id.textViewIdTernak)).setText(idTernak);
    }

    private void loadDetailTernak(final TextView textView) {
        if (!Util.isInternetAvailible(this)) {
            Toast.makeText(this, "Tidak ada koneksi internet", Toast.LENGTH_SHORT).show();
            return;
        }
        String url = Config.URL_LIHAT_DATA_TERNAK + "?aksi=5&userid=" + user.userid + "&id_ternak=" + idTernak;
        findViewById(R.id.progressBar).setVisibility(View.VISIBLE);
        debug(getClass(), "Load detail ternak url=" + url);
        HttpUrlConnectionApi.get(url, new HttpUrlConnectionApi.Callback() {
            @Override
            public void onResponse(JsonObject jsonObject) {
                debug(getClass(), "Load detail ternak onresponse " + jsonObject);
                findViewById(R.id.progressBar).setVisibility(View.INVISIBLE);
                if (jsonObject.get("success").getAsBoolean()) {
                    JsonElement arrayElemen = jsonObject.get("keterangan");
                    if (!arrayElemen.isJsonNull() && arrayElemen.isJsonArray()) {
                        JsonArray ja = arrayElemen.getAsJsonArray();
                        if (ja.size() > 0) {
                            try {
                                JsonObject object = ja.get(0).getAsJsonObject();
                                textView.setText(object.get("no_irtek").getAsString());
                                ((TextView) findViewById(R.id.textViewNamaPemilik)).setText(object.get("nama_pemilik").getAsString());
                            } catch (Exception e) {
                                Toast.makeText(PrintTernakIdCardActivity.this, "Terjadi kesalahan", Toast.LENGTH_SHORT).show();
                                Log.e(getClass().getName(), e.getMessage());
                            }
                        }
                    }
                } else
                    Toast.makeText(PrintTernakIdCardActivity.this, jsonObject.get("message").getAsString(), Toast.LENGTH_SHORT).show();

            }
        });
    }

    private void print() {
        debug(getClass(), "Save bitmap");
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            debug(getClass(), "Permission not granted");
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 3);
            return;
        }
        File sdcard = Environment.getExternalStorageDirectory().getAbsoluteFile();
        if (!EnvironmentCompat.getStorageState(sdcard).equals(Environment.MEDIA_MOUNTED)) {
            Toast.makeText(this, "Kesalahan saat mengakses penyimpanan", Toast.LENGTH_SHORT).show();
            return;
        }

        File folder = new File(sdcard, "Sidik");
        if (!folder.exists()) folder.mkdir();
        final File file = new File(folder, idTernak + "_ID_CARD.jpg");

        buttonPrint.setVisibility(View.INVISIBLE);
        progressBar.setVisibility(View.VISIBLE);
        printPreviewLayout.saveBitmap(file.getAbsolutePath(), new PrintPreviewLayout.OnBitmapSavedListener() {
            @Override
            public void onBitmapSaved(boolean success) {
                printPreviewLayout.setDrawingCacheEnabled(false);
                buttonPrint.setVisibility(View.VISIBLE);
                progressBar.setVisibility(View.INVISIBLE);
                Toast.makeText(PrintTernakIdCardActivity.this, "Bitmap saved", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(Intent.ACTION_SEND);
//				intent.setDataAndType(Uri.fromFile(file), "*/*");
                intent.setType("image/*");
                intent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(file));
                debug(getClass(), "file size " + file.length());
//				intent.setPackage("com.android.bluetooth");
                PackageManager pm = getPackageManager();
                List<ResolveInfo> apps = pm.queryIntentActivities(intent, 0);
                debug(getClass(), "Bitmap saved. Apps cunts: " + apps.size());
                if (apps.size() == 0) {
                    Toast.makeText(PrintTernakIdCardActivity.this, "Terjadi kesalahan 231", Toast.LENGTH_SHORT).show();
                } else {
                    for (ResolveInfo info : apps) {
                        String packageName = info.activityInfo.packageName;
                        if (packageName.equalsIgnoreCase("com.android.bluetooth")) {
                            intent.setClassName(packageName, info.activityInfo.name);

                            break;
                        }
                    }
                    debug(getClass(), "starting intent: " + intent);
                    PrintTernakIdCardActivity.this.startActivity(Intent.createChooser(intent, "Pilih Bluetooth"));
                }

            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        debug(getClass(), "onRequest permission result");
        if (requestCode == 3) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                print();
            }
        }
    }

    private void loadFotoFromServer() {
        if (!Util.isInternetAvailible(this) || idTernak.isEmpty()) return;
        String url = Config.URL_GAMBAR_SAPI + "?aksi=1&userid=" + user.userid;
        url += "&id_ternak=" + idTernak;
        debug(getClass(), "Load foto ternak url=" + url);
        HttpUrlConnectionApi.get(url, new HttpUrlConnectionApi.Callback() {
            @Override
            public void onResponse(JsonObject jsonObject) {
                debug(getClass(), "Load fotoresponse=" + jsonObject);
                if (jsonObject.get("success").getAsBoolean()) {
                    JsonElement je = jsonObject.get("lihat_gambar_ternak");
                    if (je != null) {
                        JsonArray ja = je.getAsJsonArray();
                        bindFotoTernak(ja);
                        pref.edit().putString(Config.PREF_FOTO_TERNAK_ALL_TERSIMPAN + idTernak, ja.toString()).apply();
                    }
                }
            }
        });
    }

    private void bindFotoTernak(JsonArray jsonArray) {
        if (jsonArray == null || jsonArray.size() < 1) return;
        FotoTernak ft = new Gson().fromJson(jsonArray.get(0), FotoTernak.class);
        if (isResummed)
            Glide.with(this).load(ft.gambar).placeholder(R.mipmap.ic_launcher).into(((ImageView) findViewById(R.id.imageViewFoto)));
    }

    private static class LoadBarcode extends AsyncTask<Void, Void, Bitmap> {

        private WeakReference<PrintTernakIdCardActivity> weakReference;
        private int viewWidth, viewHeight;

        LoadBarcode(PrintTernakIdCardActivity activity) {
            this.weakReference = new WeakReference<>(activity);
        }

        @Override
        protected void onPreExecute() {
            PrintTernakIdCardActivity activity = weakReference.get();
            if (activity != null) {
                viewWidth = activity.imagBarcode.getWidth();
                viewHeight = activity.imagBarcode.getHeight();
            }
        }

        @Override
        protected Bitmap doInBackground(Void... params) {
            try {
                PrintTernakIdCardActivity activity = weakReference.get();
                if (activity == null) {
                    Log.e(getClass().getName(), "activity null. Exiting...");
                    return null;
                }
                return Util.encodeAsBitmap(activity.idTernak, BarcodeFormat.CODE_128, viewWidth, viewHeight);
            } catch (Exception e) {
                Log.e(getClass().getName(), e.getMessage());
                return null;
            }
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            PrintTernakIdCardActivity activity = weakReference.get();
            if (bitmap != null && activity != null) activity.imagBarcode.setImageBitmap(bitmap);
        }
    }

}
