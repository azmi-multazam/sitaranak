package com.zam.sidik_padang.home.ppob;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.widget.Toolbar;

import com.google.gson.JsonObject;
import com.google.zxing.BarcodeFormat;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;

import com.zam.sidik_padang.BaseLogedinActivity;
import com.zam.sidik_padang.R;
import com.zam.sidik_padang.home.ppob.bayartagihan.BayarTagihanActivity;
import com.zam.sidik_padang.home.ppob.berita.BeritaActivity;
import com.zam.sidik_padang.home.ppob.game.VoucherGameActivity;
import com.zam.sidik_padang.home.ppob.history.HistoryActivity;
import com.zam.sidik_padang.home.ppob.iklanmerchant.IklanMerchantActivity;
import com.zam.sidik_padang.home.ppob.internet.PaketInternetActivity;
import com.zam.sidik_padang.home.ppob.isipulsa.IsiPulsaActivity;
import com.zam.sidik_padang.home.ppob.komisi.KoperasiActivity;
import com.zam.sidik_padang.home.ppob.settings.SettingsActivity;
import com.zam.sidik_padang.home.ppob.tambahsaldo.TambahSaldoActivity;
import com.zam.sidik_padang.home.ppob.tokenpln.TokenPlnActivity;
import com.zam.sidik_padang.home.ppob.transfersaldo.TransferSaldoActivity;
import com.zam.sidik_padang.util.Config;
import com.zam.sidik_padang.util.Util;
import com.zam.sidik_padang.util.VolleySingleton;
import com.zam.sidik_padang.util.VolleyStringRequest;
import com.zam.sidik_padang.util.listmenu.HomeMenuView;

public class PPOBActivity extends BaseLogedinActivity {
    private final String VOLLEY_TAG = getClass().getName();

    private TextView textViewSisaSaldo;
    private ImageView ivBarcode;

    private HomeMenuView homeMenuView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ppob_new);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        ivBarcode = (ImageView) findViewById(R.id.activity_ppob_ImageViewBarcode);
        ((TextView) findViewById(R.id.activity_ppob_TextViewNAmaMember)).setText(user.nama);
        textViewSisaSaldo = (TextView) findViewById(R.id.activity_ppob_TextViewSaldoMember);

        homeMenuView = findViewById(R.id.rv_ppob_menu);
        createMenu();
		/*
		findViewById(R.id.btn_isipulsa).setOnClickListener(this);
		findViewById(R.id.btn_token_pln).setOnClickListener(this);
		findViewById(R.id.btn_game).setOnClickListener(this);
		findViewById(R.id.btn_internet).setOnClickListener(this);
		findViewById(R.id.btn_history).setOnClickListener(this);
		findViewById(R.id.activity_ppob_ButtonIklnanMerchant).setOnClickListener(this);
		findViewById(R.id.activity_ppob_ButtonKomisiTrxDownline).setOnClickListener(this);
		findViewById(R.id.activity_ppob_ButtonTambahSaldo).setOnClickListener(this);
		findViewById(R.id.activity_ppob_ButtonTransferSaldo).setOnClickListener(this);
		findViewById(R.id.activity_ppob_ButtonSettings).setOnClickListener(this);
		findViewById(R.id.activity_ppob_ButtonInfoDanNews).setOnClickListener(this);
		findViewById(R.id.activity_ppob_ButtonPembayaranTagihan).setOnClickListener(this);
		 */
    }

    @Override
    protected void onStart() {
        super.onStart();
        ivBarcode.postDelayed(() -> new LoadBarcode(ivBarcode), 500);
        if (user != null) loadSisaSaldo();
    }

    public void onClick(String id) {
        Log.e("KLIK", id);
        switch (id) {
            case "1":
                startActivity(new Intent(this, IsiPulsaActivity.class));
                break;
            case "2":
                startActivity(new Intent(this, PaketInternetActivity.class));
                break;
            case "3":
                startActivity(new Intent(this, TokenPlnActivity.class));
                break;
            case "4":
                startActivity(new Intent(this, VoucherGameActivity.class));
                break;
            case "5":
                startActivity(new Intent(this, BayarTagihanActivity.class));
                break;
            case "6":
                startActivity(new Intent(this, BeritaActivity.class));
                break;
            case "7":
                startActivity(new Intent(this, TambahSaldoActivity.class));
                break;
            case "8":
                startActivity(new Intent(this, TransferSaldoActivity.class));
                break;
            case "9":
                startActivity(new Intent(this, KoperasiActivity.class));
                break;
            case "10":
                startActivity(new Intent(this, IklanMerchantActivity.class));
                break;
            case "11":
                startActivity(new Intent(this, HistoryActivity.class));
                break;
            case "12":
                startActivity(new Intent(this, SettingsActivity.class));
                break;
        }
		/*
		int id = v.getId();
		if (id == R.id.btn_isipulsa) {
			startActivity(new Intent(this, IsiPulsaActivity.class));
		} else if (id == R.id.btn_internet) {
			startActivity(new Intent(this, PaketInternetActivity.class));
		} else if (id == R.id.btn_token_pln) {
			startActivity(new Intent(this, TokenPlnActivity.class));
		} else if (id == R.id.activity_ppob_ButtonPembayaranTagihan) {
			startActivity(new Intent(this, BayarTagihanActivity.class));
		} else if (id == R.id.btn_game) {
			startActivity(new Intent(this, VoucherGameActivity.class));
		} else if (id == R.id.activity_ppob_ButtonInfoDanNews) {
			startActivity(new Intent(this, BeritaActivity.class));
		} else if (id == R.id.activity_ppob_ButtonKomisiTrxDownline) {
			startActivity(new Intent(this, KoperasiActivity.class));
		} else if (id == R.id.activity_ppob_ButtonIklnanMerchant) {
			startActivity(new Intent(this, IklanMerchantActivity.class));
		} else if (id == R.id.activity_ppob_ButtonSettings) {
			startActivity(new Intent(this, SettingsActivity.class));
		} else if (id == R.id.activity_ppob_ButtonTransferSaldo) {
			startActivity(new Intent(this, TransferSaldoActivity.class));
		} else if (id == R.id.activity_ppob_ButtonTambahSaldo) {
			startActivity(new Intent(this, TambahSaldoActivity.class));
		} else if (id == R.id.btn_history) {
			startActivity(new Intent(this, HistoryActivity.class));
		}
		 */
    }

    private void loadSisaSaldo() {
        if (!Util.isInternetAvailible(this)) return;
        String url = Config.URL_SALDO + "aksi=1&userid=" + user.userid;
        debug(getClass(), "Load saldo url=" + url);
        VolleyStringRequest request = new VolleyStringRequest(url, new VolleyStringRequest.Callback() {

            @Override
            public void onResponse(JsonObject jsonObject) {
                debug(getClass(), "Load saldo response=" + jsonObject);
                if (jsonObject.get("success").getAsBoolean()) {
                    textViewSisaSaldo.setText("Saldo Rp. " + NumberFormat.getInstance().format((jsonObject.get("saldo").getAsJsonArray().get(0).getAsJsonObject().get("saldo").getAsDouble())));
                }
					/*
					 {"saldo":[{"userid":"100002","saldo":69385}],
					 "success":true,"message":"Sukses"}
					*/

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

    private void createMenu() {
        List<HomeMenuView.ItemMenu> menus = new ArrayList<>();
        menus.add(new HomeMenuView.ItemMenu(R.mipmap.ic_action_phone, "Isi Pulsa", "1"));
        menus.add(new HomeMenuView.ItemMenu(R.mipmap.ic_internet, "Internet", "2"));
        menus.add(new HomeMenuView.ItemMenu(R.mipmap.ic_flash, "Token PLN", "3"));
        menus.add(new HomeMenuView.ItemMenu(R.mipmap.ic_videogame, "Game", "4"));
        menus.add(new HomeMenuView.ItemMenu(R.drawable.ic_money, "Bayar Tagihan", "5"));
        menus.add(new HomeMenuView.ItemMenu(R.drawable.ic_news, "Info & Berita", "6"));
        menus.add(new HomeMenuView.ItemMenu(R.mipmap.ic_action_wallet, "Tambah Saldo", "7"));
        menus.add(new HomeMenuView.ItemMenu(R.mipmap.ic_action_list, "Transfer Saldo", "8"));
        menus.add(new HomeMenuView.ItemMenu(R.drawable.ic_business, "Koperasi", "9"));
        menus.add(new HomeMenuView.ItemMenu(R.drawable.ic_store, "Iklan", "10"));
        menus.add(new HomeMenuView.ItemMenu(R.mipmap.ic_action_mutasi, "Histori", "11"));
        menus.add(new HomeMenuView.ItemMenu(R.drawable.ic_settings, "Setting", "12"));

        homeMenuView.setListMenu(menus).setListener((position, item) -> onClick(item.getId())).buildMenu();
    }

    private class LoadBarcode extends AsyncTask<Void, Void, Bitmap> {

        private ImageView imageView;
        private int viewWidth, viewHeight;

        LoadBarcode(ImageView imageView) {
            this.imageView = imageView;
            execute();
        }

        @Override
        protected void onPreExecute() {
            viewWidth = imageView.getWidth();
            viewHeight = imageView.getHeight();
        }

        @Override
        protected Bitmap doInBackground(Void... params) {
            try {
                return Util.encodeAsBitmap(user.userid, BarcodeFormat.QR_CODE, viewWidth, viewHeight);
            } catch (Exception e) {
                Log.e(getClass().getName(), e.getMessage());
                return null;
            }
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            if (bitmap != null) imageView.setImageBitmap(bitmap);
        }
    }

}
