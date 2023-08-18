package com.zam.sidik_padang.home.ppob.iklanmerchant.iklanumum;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.List;

import com.zam.sidik_padang.BaseLogedinActivity;
import com.zam.sidik_padang.R;
import com.zam.sidik_padang.home.ppob.iklanmerchant.komentar.Komentar;
import com.zam.sidik_padang.home.ppob.iklanmerchant.komentar.KomentarListAdapter;
import com.zam.sidik_padang.util.Config;
import com.zam.sidik_padang.util.Util;
import com.zam.sidik_padang.util.VolleySingleton;
import com.zam.sidik_padang.util.VolleyStringRequest;

import static com.zam.sidik_padang.home.ppob.iklanmerchant.iklanumum.IklanUmumListAdapter.EXTRA_IKLAN_UMUM;

/**
 * Created by supriyadi on 3/11/18.
 */

public class IklanUmumKomentarActivity extends BaseLogedinActivity {


    private static final String TAG = TrxMerchantActivity.class.getName();
    private IklanUmum iklanUmum;
    private View progressBarHorizontal, progressbarKirimKomentar;
    private View buttonKirimKomentar;
    private TextView textViewKomentar;
    private boolean isLoading = false;
    private List<Komentar> komentarList;
    private KomentarListAdapter komentarListAdapter;

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
        setContentView(R.layout.activity_komentar);
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
        progressBarHorizontal = findViewById(R.id.activity_komentar_Progressbar);
        progressbarKirimKomentar = findViewById(R.id.activity_komentar_ProgressbarKirimKomen);
        RecyclerView rv = (RecyclerView) findViewById(R.id.activity_komentar_RecyclerView);
        rv.setLayoutManager(new LinearLayoutManager(this));
        komentarList = new ArrayList<>();
        komentarListAdapter = new KomentarListAdapter(komentarList);
        rv.setAdapter(komentarListAdapter);

        textViewKomentar = (TextView) findViewById(R.id.activity_komentar_EditText);
        buttonKirimKomentar = findViewById(R.id.activity_komentar_ButtonSend);
        buttonKirimKomentar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                kirimKomentar();
            }
        });

        loadKomentar();
    }

    private void loadKomentar() {
        if (isLoading) return;
        if (!Util.isInternetAvailible(this)) {
            Toast.makeText(this, "Tidak ada akses internet", Toast.LENGTH_SHORT).show();
            return;
        }
        String link = Config.URL_MERCHANT_LIST + "aksi=4&userid=" + user.userid;
        link += "&id=" + iklanUmum.id;
        link += "&id_merchent=" + iklanUmum.id;
        isLoading = true;
        progressBarHorizontal.setVisibility(View.VISIBLE);
        VolleyStringRequest request = new VolleyStringRequest(link, new VolleyStringRequest.Callback() {
            @Override
            public void onResponse(JsonObject jsonObject) {
                debug(getClass(), "Load komentar response " + jsonObject);
                progressBarHorizontal.setVisibility(View.INVISIBLE);
                isLoading = false;
                KomentarResponse response = new Gson().fromJson(jsonObject, KomentarResponse.class);
                if (response == null) {

                    return;
                }
                if (response.success) {
                    komentarList.clear();
                    komentarList.addAll(response.komentar_merchent);
                    komentarListAdapter.notifyDataSetChanged();
                } else {
                    Toast.makeText(IklanUmumKomentarActivity.this, response.message, Toast.LENGTH_SHORT).show();
                }
            }
        });

        request.setTag(TAG);
        VolleySingleton.getInstance(this).getRequestQueue().add(request);
    }

    private void kirimKomentar() {
        if (!Util.isInternetAvailible(this)) {
            Toast.makeText(this, "Tidak ada akses internet", Toast.LENGTH_SHORT).show();
            return;
        }

        String komentar = textViewKomentar.getText().toString().trim();
        if (komentar.isEmpty()) {
            textViewKomentar.setError("Tuliskan komentar anda");
            textViewKomentar.requestFocus();
            return;
        }

        String link = Config.URL_MERCHANT_LIST + "aksi=5&userid=" + user.userid;
        link += "&id=" + iklanUmum.id;
        link += "&id_merchent=" + iklanUmum.id;
        link += "&komentar=" + Util.toUrlEncoded(komentar);

        debug(getClass(), "Tambah komentar link: " + link);
        progressbarKirimKomentar.setVisibility(View.VISIBLE);
        buttonKirimKomentar.setVisibility(View.INVISIBLE);
        VolleyStringRequest request = new VolleyStringRequest(link, new VolleyStringRequest.Callback() {
            @Override
            public void onResponse(JsonObject jsonObject) {
                debug(getClass(), "Tambah komentar response: " + jsonObject);
                buttonKirimKomentar.setVisibility(View.VISIBLE);
                progressbarKirimKomentar.setVisibility(View.INVISIBLE);
                if (jsonObject.get("success").getAsBoolean()) {
                    loadKomentar();
                    textViewKomentar.setText("");
                } else if (isResummed) new AlertDialog.Builder(IklanUmumKomentarActivity.this)
                        .setMessage(jsonObject.get("message").getAsString()).show();

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

    public static class KomentarResponse {
        public boolean success = false;
        public String message = "Terjadi kesalahan 7264287";
        public List<Komentar> komentar_merchent = new ArrayList<>();
    }

	/* {"komentar_merchent":
				[
					{"id":"3",
					 "foto":"IMG_20161211_101743.jpg",
					 "nama":"Supriyadi2",
					 "iduser":"MB1000005",
					 "komentar":"test koment",
					 "tanggal":"11 Mar 2018 11:54:31"
					}],
		"total_komentar":1,"success":true,"message":" berhasil"}*/
}
