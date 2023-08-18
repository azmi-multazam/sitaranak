package com.zam.sidik_padang.home.ppob.iklanmerchant.iklanumum;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.Toolbar;

import com.androidnetworking.AndroidNetworking;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;

import com.zam.sidik_padang.BaseLogedinActivity;
import com.zam.sidik_padang.R;
import com.zam.sidik_padang.util.Config;
import com.zam.sidik_padang.util.HttpUrlConnectionApi;
import com.zam.sidik_padang.util.Util;
import com.zam.sidik_padang.util.VolleySingleton;

import static com.zam.sidik_padang.home.ppob.iklanmerchant.iklanumum.IklanUmumListAdapter.EXTRA_IKLAN_UMUM;

/**
 * Created by supriyadi on 3/11/18.
 */

public class TrxMerchantActivity extends BaseLogedinActivity {


    private static final String TAG = TrxMerchantActivity.class.getName();
    private IklanUmum iklanUmum;
    private View progressBar;
    private Gson gson;
    private LoadTrxResponse.Hasil hasil;
    private TextView textViewPengiklan, textViewKode, textViewToko, textViewHarga, textViewPotongan, textViewTotal, textViewToken;
    private View layout;

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        if (iklanUmum != null) outState.putSerializable(EXTRA_IKLAN_UMUM, iklanUmum);
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        if (savedInstanceState != null && savedInstanceState.containsKey(EXTRA_IKLAN_UMUM)) {
            iklanUmum = (IklanUmum) savedInstanceState.getSerializable(EXTRA_IKLAN_UMUM);
        }
        super.onRestoreInstanceState(savedInstanceState);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trx_merchant);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        if (savedInstanceState != null && savedInstanceState.containsKey(EXTRA_IKLAN_UMUM)) {
            iklanUmum = (IklanUmum) savedInstanceState.getSerializable(EXTRA_IKLAN_UMUM);
        } else {
            Intent it = getIntent();
            if (it.hasExtra(EXTRA_IKLAN_UMUM))
                iklanUmum = (IklanUmum) it.getSerializableExtra(EXTRA_IKLAN_UMUM);
        }

        if (iklanUmum == null) {
            Toast.makeText(this, "Terjadi kesalahan 1333", Toast.LENGTH_SHORT).show();
            return;
        }
        toolbar.setSubtitle(iklanUmum.toko);
        layout = findViewById(R.id.layout);
        progressBar = findViewById(R.id.progressBar);
        textViewPengiklan = (TextView) findViewById(R.id.textViewPengiklan);
        textViewKode = (TextView) findViewById(R.id.textViewKode);
        textViewToko = (TextView) findViewById(R.id.textViewToko);
        textViewHarga = (TextView) findViewById(R.id.textViewHarga);
        textViewPotongan = (TextView) findViewById(R.id.textViewPotongan);
        textViewTotal = (TextView) findViewById(R.id.textViewTotal);
        textViewToken = (TextView) findViewById(R.id.editText);
        gson = new Gson();

        findViewById(R.id.button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                prosesPembelian();
            }
        });


        loadTransaksi();

    }

    private void loadTransaksi() {
        if (!Util.isInternetAvailible(this)) {
            Toast.makeText(this, "Tidak ada akses internet", Toast.LENGTH_SHORT).show();
            return;
        }

        String link = Config.URL_TRX_MERCHANT + "aksi=1&userid=" + user.userid;
        link += "&id_merchent=" + iklanUmum.id;
        debug(getClass(), "Load trx merchent. link: " + link);
        progressBar.setVisibility(View.VISIBLE);
//		final VolleyStringRequest request = new VolleyStringRequest(link, new VolleyStringRequest.Callback() {
//			@Override
//			public void onResponse(JsonObject jsonObject) {
//				progressBar.setVisibility(View.INVISIBLE);
//				debug(getClass(), "Load trx response: " + jsonObject);
//				LoadTrxResponse response = gson.fromJson(jsonObject, LoadTrxResponse.class);
//				if (response != null) {
//					if (!response.success) {
//						Toast.makeText(TrxMerchantActivity.this, response.message, Toast.LENGTH_SHORT).show();
//					} else {
//						if (response.trx_merchent.size() == 0) {
//							Toast.makeText(TrxMerchantActivity.this, "Data tidak ditemukan", Toast.LENGTH_SHORT).show();
//							return;
//						}
//						hasil = response.trx_merchent.get(0);
//						textViewPengiklan.setText(hasil.nama);
//						textViewKode.setText(hasil.kode);
//						textViewToko.setText(hasil.toko);
//
//						NumberFormat format = NumberFormat.getInstance();
//						try {
//							textViewHarga.setText("Rp " + format.format(Long.parseLong(hasil.harga)));
//							textViewPotongan.setText("Rp " + format.format(hasil.potongan));
//							textViewTotal.setText("Rp " + format.format(Long.parseLong(hasil.total)));
//							layout.setVisibility(View.VISIBLE);
//						} catch (Exception e) {
//							Log.e(getClass().getName(), "" + e.getMessage());
//						}
//					}
//				} else {
//					Toast.makeText(TrxMerchantActivity.this, "Terjadi kesalahan 9123", Toast.LENGTH_SHORT).show();
//				}
//			}
//		});
//		request.setTag(TAG);
//		VolleySingleton.getInstance(this).getRequestQueue().add(request);
        HttpUrlConnectionApi.get(link, new HttpUrlConnectionApi.Callback() {
            @Override
            public void onResponse(JsonObject jsonObject) {
                progressBar.setVisibility(View.INVISIBLE);
                debug(getClass(), "Load trx response: " + jsonObject);
                LoadTrxResponse response = gson.fromJson(jsonObject, LoadTrxResponse.class);
                if (response != null) {
                    if (!response.success) {
                        Toast.makeText(TrxMerchantActivity.this, response.message, Toast.LENGTH_SHORT).show();
                    } else {
                        if (response.trx_merchent.size() == 0) {
                            Toast.makeText(TrxMerchantActivity.this, "Data tidak ditemukan", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        hasil = response.trx_merchent.get(0);
                        textViewPengiklan.setText(hasil.nama);
                        textViewKode.setText(hasil.kode);
                        textViewToko.setText(hasil.toko);

                        NumberFormat format = NumberFormat.getInstance();
                        try {
                            textViewHarga.setText("Rp " + format.format(Long.parseLong(hasil.harga)));
                            textViewPotongan.setText("Rp " + format.format(hasil.potongan));
                            textViewTotal.setText("Rp " + format.format(Long.parseLong(hasil.total)));
                            layout.setVisibility(View.VISIBLE);
                        } catch (Exception e) {
                            Log.e(getClass().getName(), "" + e.getMessage());
                        }
                    }
                } else {
                    Toast.makeText(TrxMerchantActivity.this, "Terjadi kesalahan 9123", Toast.LENGTH_SHORT).show();
                }
            }
        });
//
//		AndroidNetworking.get(link).setTag(TAG).build().getAsObject(LoadTrxResponse.class, new ParsedRequestListener() {
//			@Override
//			public void onResponse(Object response) {
//				progressBar.setVisibility(View.INVISIBLE);
//				debug(getClass(), "Load trx response: " + response);
//				if (!((LoadTrxResponse)response).success) {
//					Toast.makeText(TrxMerchantActivity.this, ((LoadTrxResponse)response).message, Toast.LENGTH_SHORT).show();
//				} else {
//					if (((LoadTrxResponse)response).trx_merchent.size() == 0) {
//						Toast.makeText(TrxMerchantActivity.this, "Data tidak ditemukan", Toast.LENGTH_SHORT).show();
//						return;
//					}
//					hasil = ((LoadTrxResponse)response).trx_merchent.get(0);
//					textViewPengiklan.setText(hasil.nama);
//					textViewKode.setText(hasil.kode);
//					textViewToko.setText(hasil.toko);
//
//					NumberFormat format = NumberFormat.getInstance();
//					try {
//						textViewHarga.setText("Rp " + format.format(Long.parseLong(hasil.harga)));
//						textViewPotongan.setText("Rp " + format.format(hasil.potongan));
//						textViewTotal.setText("Rp " + format.format(Long.parseLong(hasil.total)));
//						layout.setVisibility(View.VISIBLE);
//					} catch (Exception e) {
//						Log.e(getClass().getName(), "" + e.getMessage());
//					}
//				}
//			}
//
//			@Override
//			public void onError(ANError anError) {
//				progressBar.setVisibility(View.INVISIBLE);
//				debug(getClass(), "Load trx response error: " + anError.getMessage());
//				if (isResummed)
//					Toast.makeText(TrxMerchantActivity.this, anError.getErrorDetail(), Toast.LENGTH_SHORT).show();
//			}
//		});
    }

    private void prosesPembelian() {
        String token = textViewToken.getText().toString().trim();
        if (token.isEmpty()) {
            textViewToken.setError("Masukkan token");
            textViewToken.requestFocus();
            return;
        }

        if (!Util.isInternetAvailible(this)) {
            Toast.makeText(this, "Tidak ada koneksi internet", Toast.LENGTH_SHORT).show();
            return;
        }

        String link = Config.URL_TRX_MERCHANT + "aksi=2&userid=" + user.userid;
        link += "&id_merchent=" + (hasil.id_merchent == null ? iklanUmum.id : hasil.id_merchent);
        link += "&kode=" + hasil.kode;
        link += "&total=" + hasil.total;
        link += "&harga=" + hasil.harga;
        link += "&potongan=" + hasil.potongan;
        link += "&token=" + token;
        debug(getClass(), "Proses trx merchant. Link: " + link);
        final Dialog dialog = Util.showProgressDialog(this, "Memproses transaksi...", false);
//		VolleyStringRequest request = new VolleyStringRequest(link, new VolleyStringRequest.Callback() {
//			@Override
//			public void onResponse(final JsonObject jsonObject) {
//				debug(getClass(), "Proses trx response " + jsonObject);
//				dialog.dismiss();
//				if (isResummed) new AlertDialog.Builder(TrxMerchantActivity.this)
//						.setMessage(jsonObject.get("message").getAsString())
//						.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
//							@Override
//							public void onClick(DialogInterface dialogInterface, int i) {
//								if (jsonObject.get("success").getAsBoolean()) finish();
//							}
//						})
//						.show();
//
//			}
//		});
//
//		request.setTag(TAG);
//		VolleySingleton.getInstance(this).getRequestQueue().add(request);
        HttpUrlConnectionApi.get(link, new HttpUrlConnectionApi.Callback() {
            @Override
            public void onResponse(final JsonObject jsonObject) {
                debug(getClass(), "Proses trx response " + jsonObject);
                dialog.dismiss();
                if (isResummed) new AlertDialog.Builder(TrxMerchantActivity.this)
                        .setMessage(jsonObject.get("message").getAsString())
                        .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                if (jsonObject.get("success").getAsBoolean()) finish();
                            }
                        })
                        .show();
            }
        });
    }


    @Override
    protected void onDestroy() {
        VolleySingleton.getInstance(this).getRequestQueue().cancelAll(TAG);
        AndroidNetworking.cancel(TAG);
        super.onDestroy();
    }

    public static class LoadTrxResponse {
        public boolean success = false;
        public String message = "Terjadi kesalahan 875";
        public List<Hasil> trx_merchent = new ArrayList<>();

        @Override
        public String toString() {
            return new Gson().toJson(this);
        }

        public static class Hasil {
            public String id_merchent, kode, toko, harga, total, nama;
            public long potongan = 0;

            @Override
            public String toString() {
                return new Gson().toJson(this);
            }
        }
    }
}
