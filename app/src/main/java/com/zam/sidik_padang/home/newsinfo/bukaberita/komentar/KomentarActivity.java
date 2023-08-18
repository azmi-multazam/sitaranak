package com.zam.sidik_padang.home.newsinfo.bukaberita.komentar;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import com.zam.sidik_padang.BaseLogedinActivity;
import com.zam.sidik_padang.R;
import com.zam.sidik_padang.home.memberhome.mvp.Berita;
import com.zam.sidik_padang.home.memberhome.mvp.BeritaResponse;
import com.zam.sidik_padang.home.newsinfo.NewsInfoActivity;
import com.zam.sidik_padang.util.Config;
import com.zam.sidik_padang.util.Util;
import com.zam.sidik_padang.util.VolleySingleton;
import io.paperdb.Paper;

import static com.zam.sidik_padang.home.newsinfo.NewsInfoActivity.PREF_RESPONSE_BERITA;

public class KomentarActivity extends BaseLogedinActivity implements KomentarContract.KometarView {

    public static final String EXTRA_KOMENTAR_RESPONSE = "response_komentar";
    public static final String VOLLEY_TAG = KomentarActivity.class.getName();

    private KomentarResponse komentarResponse;
    private Berita berita;
    private Gson gson;
    //private JsonObject jsonObject;
    private List<KomentarBerita> list;
    private KomentarListAdapter adapter;
    private EditText editText;
    private View progressbar, progressbarKirim, buttonKirim;

    private KomentarContract.Presenter presenter;

    private String idBerita;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_komentar);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setSubtitleTextAppearance(this, R.style.AppTheme_ToolbarSubTitle);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        Intent it = getIntent();
        idBerita = it.getStringExtra(NewsInfoActivity.EXTRA_BERITA);
        komentarResponse = Paper.book().read("komentar_" + idBerita);
        if (komentarResponse == null) {
            new AlertDialog.Builder(this)
                    .setMessage("Terjadi kesalahan")
                    .setCancelable(false)
                    .setPositiveButton("Kembali", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            finish();
                        }
                    }).show();
            return;
        }
        presenter = new KomentarPresenter(this);
        BeritaResponse beritaResponse = Paper.book().read(PREF_RESPONSE_BERITA);
        berita = getBeritaById(beritaResponse.getBerita(), idBerita);
        //berita = (Berita) it.getSerializableExtra(NewsInfoActivity.EXTRA_BERITA);
        if (berita != null) {
            toolbar.setSubtitle(berita.getJudul());
        }

        RecyclerView rv = (RecyclerView) findViewById(R.id.activity_komentar_RecyclerView);
        rv.setLayoutManager(new LinearLayoutManager(this));

        //gson = new Gson();
        //jsonObject = gson.fromJson(it.getStringExtra(EXTRA_KOMENTAR_RESPONSE), JsonElement.class).getAsJsonObject();
        list = new ArrayList<>();
        adapter = new KomentarListAdapter(list);
        rv.setAdapter(adapter);
        editText = (EditText) findViewById(R.id.activity_komentar_EditText);
        buttonKirim = findViewById(R.id.activity_komentar_ButtonSend);
        buttonKirim.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                kirimKomentar();
            }
        });
        progressbar = findViewById(R.id.activity_komentar_Progressbar);
        progressbarKirim = findViewById(R.id.activity_komentar_ProgressbarKirimKomen);
        bindView();
    }

    private void kirimKomentar() {
        String coment = editText.getText().toString().trim();
        if (coment.isEmpty()) {
            editText.setError("Tulis komentar");
            editText.requestFocus();
            return;
        }
        if (!Util.isInternetAvailible(this)) {
            Toast.makeText(this, "Tidak ada koneksi internet", Toast.LENGTH_SHORT).show();
            return;
        }
        String url = Config.URL_BERITA + "?aksi=5&userid=" + user.userid;
        url += "&id=" + berita.getId();
        url += "&komentar=" + Util.toUrlEncoded(coment);
        debug(getClass(), "kirim komentar url=" + url);
        presenter.kirimKomentar(url);
		/*
		progressbarKirim.setVisibility(View.VISIBLE);
		buttonKirim.setVisibility(View.INVISIBLE);
		HttpUrlConnectionApi.get(url, new HttpUrlConnectionApi.Callback() {
			@Override
			public void onResponse(JsonObject jsonObject) {
				debug(getClass(), "Kirim koment response=" + jsonObject);
				progressbarKirim.setVisibility(View.INVISIBLE);
				buttonKirim.setVisibility(View.VISIBLE);
				if (jsonObject.get("success").getAsBoolean()) {
					editText.setText("");
					editText.clearFocus();
					loadKomentar();
				} else
					Toast.makeText(KomentarActivity.this, jsonObject.get("message").getAsString(), Toast.LENGTH_SHORT).show();
			}
		});
		 */
    }

    private void bindView() {
        if (komentarResponse == null) return;
        list.clear();
        list.addAll(komentarResponse.getKomentarBerita());
        adapter.notifyDataSetChanged();
        if (getSupportActionBar() != null)
            getSupportActionBar().setTitle(list.size() + " Komentar");
    }

    private void loadKomentar() {
        if (!Util.isInternetAvailible(this)) return;
        String url = Config.URL_BERITA + "?aksi=4&userid=" + user.userid;
        url += "&id=" + berita.getId();
        debug(getClass(), "Load komentar url=" + url);
        presenter.ambilKomentar(url);
		/*
		progressbar.setVisibility(View.VISIBLE);
		VolleyStringRequest request = new VolleyStringRequest(url, new VolleyStringRequest.Callback() {
			@Override
			public void onResponse(JsonObject jsonObject) {
				debug(getClass(), "Load komentar response=" + jsonObject);
				progressbar.setVisibility(View.INVISIBLE);
				if (jsonObject.get("success").getAsBoolean()) {
					KomentarActivity.this.jsonObject = jsonObject;
					bindView();
				} else
					Toast.makeText(KomentarActivity.this, jsonObject.get("message").getAsString(), Toast.LENGTH_SHORT).show();
			}
		});

		request.setTag(VOLLEY_TAG);
		VolleySingleton.getInstance(this).getRequestQueue().add(request);
		 */
    }

    @Override
    protected void onDestroy() {
        VolleySingleton.getInstance(this).getRequestQueue().cancelAll(VOLLEY_TAG);
        super.onDestroy();
    }

    private Berita getBeritaById(List<Berita> list, String idBerita) {
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).getId().equals(idBerita)) {
                return list.get(i);
            }
        }
        return null;
    }

    @Override
    public void onAmbilStart() {
        progressbar.setVisibility(View.VISIBLE);

    }

    @Override
    public void onAmbilSuccess(KomentarResponse response) {
        progressbar.setVisibility(View.INVISIBLE);
        this.komentarResponse = response;
        Paper.book().write("komentar_" + idBerita, response);
        bindView();
    }

    @Override
    public void onAmbilError(String message) {
        progressbar.setVisibility(View.INVISIBLE);

    }

    @Override
    public void onKirimStart() {
        progressbarKirim.setVisibility(View.VISIBLE);
        buttonKirim.setVisibility(View.INVISIBLE);

    }

    @Override
    public void onKirimSuccess(KomentarResponse response) {
        progressbarKirim.setVisibility(View.INVISIBLE);
        buttonKirim.setVisibility(View.VISIBLE);
        editText.setText("");
        editText.clearFocus();
        loadKomentar();
    }

    @Override
    public void onKirimError(String message) {
        progressbarKirim.setVisibility(View.INVISIBLE);
        buttonKirim.setVisibility(View.VISIBLE);
        Toast.makeText(KomentarActivity.this, message, Toast.LENGTH_SHORT).show();
    }
}
