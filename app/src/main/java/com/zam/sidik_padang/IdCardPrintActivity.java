package com.zam.sidik_padang;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
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
import androidx.core.content.FileProvider;
import androidx.core.os.EnvironmentCompat;

import com.bumptech.glide.Glide;
import com.google.zxing.BarcodeFormat;

import java.io.File;
import java.lang.ref.WeakReference;
import java.util.List;

import com.zam.sidik_padang.util.Util;
import com.zam.sidik_padang.util.customclasses.PrintPreviewLayout;

/**
 * Created by supriyadi on 3/20/18.
 */

public class IdCardPrintActivity extends BasePrintActivity {

    private ImageView imagBarcode;
    private PrintPreviewLayout printPreviewLayout;
    private View buttonPrint, progressBar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_print_idcard);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        imagBarcode = (ImageView) findViewById(R.id.imageBarcode);
        progressBar = findViewById(R.id.progressBar);
        buttonPrint = findViewById(R.id.button);
        buttonPrint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                print();
            }
        });

        printPreviewLayout = (PrintPreviewLayout) findViewById(R.id.printPreviewLayout);

        Intent intent = getIntent();

        ((TextView) findViewById(R.id.textNama)).setText(user.nama);
        ((TextView) findViewById(R.id.textId)).setText(user.userid);
        ((TextView) findViewById(R.id.textLevel)).setText(user.level);
        ((TextView) findViewById(R.id.textAlamat)).setText(intent.getStringExtra("kota"));
        ((TextView) findViewById(R.id.textTanggalLahir)).setText(intent.getStringExtra("tgl_lahir"));
        ((TextView) findViewById(R.id.textTanggalDaftar)).setText(intent.getStringExtra("tgl_daftar"));
        findViewById(R.id.imageKsi).setVisibility(intent.getStringExtra("id_kelompok_ternak").equalsIgnoreCase("0") ? View.GONE : View.VISIBLE);
        if (user.foto != null && !user.foto.isEmpty())
            Glide.with(this).load(user.foto).placeholder(R.drawable.ic_account_circle_white)
                    .into(((ImageView) findViewById(R.id.imageViewFoto)));

        imagBarcode.postDelayed(new Runnable() {
            @Override
            public void run() {
                new LoadBarcode(IdCardPrintActivity.this).execute();
            }
        }, 200);
    }

    private void print() {
        debug(getClass(), "Save bitmap");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
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
        final File file = new File(folder, user.userid + "_ID_CARD.jpg");

        buttonPrint.setVisibility(View.INVISIBLE);
        progressBar.setVisibility(View.VISIBLE);
        printPreviewLayout.saveBitmap(file.getAbsolutePath(), new PrintPreviewLayout.OnBitmapSavedListener() {
            @Override
            public void onBitmapSaved(boolean success) {
                printPreviewLayout.setDrawingCacheEnabled(false);
                buttonPrint.setVisibility(View.VISIBLE);
                progressBar.setVisibility(View.INVISIBLE);
                Toast.makeText(IdCardPrintActivity.this, "Bitmap saved", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(Intent.ACTION_SEND);
//				intent.setDataAndType(Uri.fromFile(file), "*/*");
                intent.setType("image/*");
                //intent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(file));
                intent.putExtra(Intent.EXTRA_STREAM, getUri(file));
                debug(getClass(), "file size " + file.length());
//				intent.setPackage("com.android.bluetooth");
                PackageManager pm = getPackageManager();
                List<ResolveInfo> apps = pm.queryIntentActivities(intent, 0);
                debug(getClass(), "Bitmap saved. Apps cunts: " + apps.size());
                if (apps.size() == 0) {
                    Toast.makeText(IdCardPrintActivity.this, "Terjadi kesalahan 231", Toast.LENGTH_SHORT).show();
                } else {
                    for (ResolveInfo info : apps) {
                        String packageName = info.activityInfo.packageName;
                        if (packageName.equalsIgnoreCase("com.android.bluetooth")) {
                            intent.setClassName(packageName, info.activityInfo.name);

                            break;
                        }
                    }
                    debug(getClass(), "starting intent: " + intent);
                    IdCardPrintActivity.this.startActivity(Intent.createChooser(intent, "Pilih Bluetooth"));
                }
            }
        });
    }

    private Uri getUri(File file) {
        return FileProvider.getUriForFile(getApplicationContext(), getPackageName() + ".provider", file);
        //return FileProvider.getUriForFile(this, BuildConfig.APPLICATION_ID + ".provider", file);

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

    private static class LoadBarcode extends AsyncTask<Void, Void, Bitmap> {

        private WeakReference<IdCardPrintActivity> weakReference;
        private int viewWidth, viewHeight;

        LoadBarcode(IdCardPrintActivity activity) {
            this.weakReference = new WeakReference<>(activity);
        }

        @Override
        protected void onPreExecute() {
            IdCardPrintActivity activity = weakReference.get();
            if (activity != null) {
                viewWidth = activity.imagBarcode.getWidth();
                viewHeight = activity.imagBarcode.getHeight();
            }
        }

        @Override
        protected Bitmap doInBackground(Void... params) {
            try {
                IdCardPrintActivity activity = weakReference.get();
                if (activity == null) {
                    Log.e(getClass().getName(), "activity null. Exiting...");
                    return null;
                }
                return Util.encodeAsBitmap(activity.user.userid, BarcodeFormat.QR_CODE, viewWidth, viewHeight);
            } catch (Exception e) {
                Log.e(getClass().getName(), e.getMessage());
                return null;
            }
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            IdCardPrintActivity activity = weakReference.get();
            if (bitmap != null && activity != null) activity.imagBarcode.setImageBitmap(bitmap);
        }
    }
}
