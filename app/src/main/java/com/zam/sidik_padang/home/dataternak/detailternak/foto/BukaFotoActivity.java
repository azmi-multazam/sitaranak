package com.zam.sidik_padang.home.dataternak.detailternak.foto;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.core.view.ViewCompat;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.google.gson.JsonObject;

import com.zam.sidik_padang.BaseLogedinActivity;
import com.zam.sidik_padang.R;
import com.zam.sidik_padang.util.Config;
import com.zam.sidik_padang.util.Util;
import com.zam.sidik_padang.util.VolleySingleton;
import com.zam.sidik_padang.util.VolleyStringRequest;

/**
 * Created by supriyadi on 10/5/17.
 */

public class BukaFotoActivity extends BaseLogedinActivity {

    public static final String EXTRA_FOTO_TERNAK = "extra_foto_ternak";
    private static final String VOLLEY_TAG = BukaFotoActivity.class.getName();
    private FotoTernak fotoTernak;
    private ProgressBar progressBar;

    @Override
    public void onSaveInstanceState(Bundle outState) {
        if (fotoTernak != null) outState.putSerializable(EXTRA_FOTO_TERNAK, fotoTernak);
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        if (savedInstanceState != null && savedInstanceState.containsKey(EXTRA_FOTO_TERNAK))
            fotoTernak = (FotoTernak) savedInstanceState.getSerializable(EXTRA_FOTO_TERNAK);
        super.onRestoreInstanceState(savedInstanceState);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_foto);
        setResult(RESULT_CANCELED);
        progressBar = (ProgressBar) findViewById(R.id.activity_upload_foto_Progressbar);
        progressBar.setIndeterminate(true);
        ImageView imageView = (ImageView) findViewById(R.id.activity_upload_foto_ImageView);
        ViewCompat.setTransitionName(imageView, "transisi_foto");
        Intent it = getIntent();
        if (it.hasExtra(EXTRA_FOTO_TERNAK))
            fotoTernak = (FotoTernak) it.getSerializableExtra(EXTRA_FOTO_TERNAK);
        else if (savedInstanceState != null && savedInstanceState.containsKey(EXTRA_FOTO_TERNAK))
            fotoTernak = (FotoTernak) savedInstanceState.getSerializable(EXTRA_FOTO_TERNAK);
        else {
            debug(getClass(), "Foto link=" + fotoTernak.gambar);
            return;
        }
        progressBar.setVisibility(View.VISIBLE);
        Glide.with(this)
                .load(fotoTernak.gambar)
                .listener(new RequestListener<String, GlideDrawable>() {
                    @Override
                    public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
                        progressBar.setVisibility(View.INVISIBLE);
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                        progressBar.setVisibility(View.INVISIBLE);
                        return false;
                    }
                })
                .into(imageView);
        View buttonDelete = findViewById(R.id.activity_upload_foto_ButtonDelete);
        buttonDelete.setVisibility(View.VISIBLE);
        buttonDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(BukaFotoActivity.this)
                        .setTitle("Konfirmasi")
                        .setMessage("Hapus foto ternak?")
                        .setNegativeButton("Batal", null)
                        .setPositiveButton("Hapus", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                hapusFoto();
                            }
                        })
                        .show();
            }
        });
    }

    private void hapusFoto() {
        if (!Util.isInternetAvailible(this)) {
            Toast.makeText(this, "Tidak ada koneksi internet", Toast.LENGTH_SHORT).show();
            return;
        }

        String url = Config.URL_GAMBAR_SAPI + "?aksi=3&userid=" + user.userid;
        url += "&id=" + fotoTernak.id;
        debug(getClass(), "Hapus foto url+=" + url);
        final Dialog d = ProgressDialog.show(this, null, "Menghapus foto...", true, false);
        VolleyStringRequest request = new VolleyStringRequest(url, new VolleyStringRequest.Callback() {
            @Override
            public void onResponse(JsonObject jsonObject) {
                d.dismiss();
                debug(getClass(), "Hapus foto response=" + jsonObject);
                if (jsonObject.get("success").getAsBoolean()) {
                    Intent intent = new Intent();
                    intent.putExtra(EXTRA_FOTO_TERNAK, fotoTernak);
                    setResult(RESULT_OK, intent);
                    Toast.makeText(BukaFotoActivity.this, "Berhasil dihapus", Toast.LENGTH_SHORT).show();
                    finish();
                }
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
