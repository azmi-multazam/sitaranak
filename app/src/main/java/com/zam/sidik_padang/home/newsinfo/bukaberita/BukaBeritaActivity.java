package com.zam.sidik_padang.home.newsinfo.bukaberita;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.zam.sidik_padang.BaseLogedinActivity;
import com.zam.sidik_padang.R;
import com.zam.sidik_padang.home.memberhome.mvp.Berita;
import com.zam.sidik_padang.home.memberhome.mvp.BeritaResponse;
import com.zam.sidik_padang.home.newsinfo.BeritaListAdapter;
import com.zam.sidik_padang.home.newsinfo.NewsInfoActivity;
import com.zam.sidik_padang.home.newsinfo.bukaberita.komentar.KomentarActivity;
import com.zam.sidik_padang.home.newsinfo.bukaberita.komentar.KomentarResponse;
import com.zam.sidik_padang.home.newsinfo.bukaberita.mvp.BeritaContract;
import com.zam.sidik_padang.home.newsinfo.bukaberita.mvp.BeritaPresenter;
import com.zam.sidik_padang.home.newsinfo.bukaberita.mvp.BukaBeritaResponse;
import com.zam.sidik_padang.home.newsinfo.bukaberita.mvp.DetailBerita;
import com.zam.sidik_padang.util.Config;
import com.zam.sidik_padang.util.Util;
import com.zam.sidik_padang.util.VolleySingleton;
import io.paperdb.Paper;

import static com.zam.sidik_padang.home.newsinfo.NewsInfoActivity.PREF_RESPONSE_BERITA;

public class BukaBeritaActivity extends BaseLogedinActivity implements BeritaContract.BeritaView {

    private static final String VOLLEY_TAG = BukaBeritaActivity.class.getName();
    private static final String PREF_RESPONSE_ISI_BERITA = "response_isi_berita";
    private boolean resumed = false;
    private MenuItem shareMenuItem;
    private String shareLink = "";
    private Drawable drawableGambar;

    private ProgressBar progressbar;
    private List<Berita> listBeritaBacaJuaga, listBeritaTerkait;
    private BeritaListAdapter adapterBeritaTerkait, adapterBacaJuga;
    private StatusKomentar statusKomentar = StatusKomentar.NOT_LOADED;
    private String komentarResponse;
    private String idBerita;

    private BeritaContract.Presenter presenter;
    private LinearLayout prograssKomentar;
    private AppCompatButton btnKomentar;

    @Override
    protected void onResume() {
        super.onResume();
        resumed = true;
    }

    @Override
    protected void onPause() {
        resumed = false;
        super.onPause();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        if (idBerita != null) outState.putString(NewsInfoActivity.EXTRA_BERITA, idBerita);
        //if (berita != null) outState.putSerializable(NewsInfoActivity.EXTRA_BERITA, berita);
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        if (savedInstanceState != null && savedInstanceState.containsKey(NewsInfoActivity.EXTRA_BERITA)) {
            idBerita = savedInstanceState.getString(NewsInfoActivity.EXTRA_BERITA);
            //berita = (Berita) savedInstanceState.getSerializable(NewsInfoActivity.EXTRA_BERITA);
        }
        super.onRestoreInstanceState(savedInstanceState);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buka_berita);
        Intent intent = getIntent();
        if (!intent.hasExtra(NewsInfoActivity.EXTRA_BERITA) && idBerita == null) {
            Toast.makeText(this, "Terjadi kesalahan", Toast.LENGTH_SHORT).show();
            finish();
        }
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        presenter = new BeritaPresenter(this);
        progressbar = findViewById(R.id.activity_buka_berita_ProgressbarIsiBerita);
        prograssKomentar = findViewById(R.id.progressKomentar);
        btnKomentar = findViewById(R.id.activity_buka_berita_ButtonKomentar);

        RecyclerView rvBeritaTerkait = (RecyclerView) findViewById(R.id.activity_buka_berita_RecyclerViewBeritaTerkait);
        rvBeritaTerkait.setLayoutManager(new LinearLayoutManager(this));
        listBeritaTerkait = new ArrayList<>();
        adapterBeritaTerkait = new BeritaListAdapter(listBeritaTerkait);
        rvBeritaTerkait.setAdapter(adapterBeritaTerkait);

        RecyclerView rvBacaJuga = (RecyclerView) findViewById(R.id.activity_buka_berita_RecyclerViewBukaJuga);
        rvBacaJuga.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        listBeritaBacaJuaga = new ArrayList<>();
        adapterBacaJuga = new BacaJugaAdapter(listBeritaBacaJuaga);
        rvBacaJuga.setAdapter(adapterBacaJuga);

        idBerita = intent.getStringExtra(NewsInfoActivity.EXTRA_BERITA);
        BeritaResponse beritaResponse = Paper.book().read(PREF_RESPONSE_BERITA);
        Berita berita = getBeritaById(beritaResponse.getBerita(), idBerita);
        if (berita != null) {
            ((TextView) findViewById(R.id.activity_buka_berita_textViewJudulBerita)).setText(berita.getJudul());
            ((TextView) findViewById(R.id.activity_buka_berita_textViewTanggal)).setText(berita.getTanggal());
            if (berita.getGambar() != null && !berita.getGambar().isEmpty()) {
                final View progressBarGambar = findViewById(R.id.activity_buka_berita_ProgressbarGambar);
                progressBarGambar.setVisibility(View.VISIBLE);
                Glide.with(this).load(berita.getGambar()).listener(new RequestListener<String, GlideDrawable>() {
                    @Override
                    public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
                        progressBarGambar.setVisibility(View.INVISIBLE);
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                        progressBarGambar.setVisibility(View.INVISIBLE);
                        drawableGambar = resource;
                        return false;
                    }
                }).into((ImageView) findViewById(R.id.activity_buka_berita_ImageViewGambar));
            }
        }

        //String response = sharedPreferences.getString(PREF_RESPONSE_ISI_BERITA + berita.getId(), "");
        //if (!response.isEmpty() && response.startsWith("{")) bindView(new Gson().fromJson(response, JsonElement.class).getAsJsonObject());
        BukaBeritaResponse bukaBeritaResponse = Paper.book().read(PREF_RESPONSE_ISI_BERITA + idBerita);
        if (bukaBeritaResponse != null) bindView(bukaBeritaResponse);

        KomentarResponse komentarResponse = Paper.book().read("komentar_" + idBerita);
        if (komentarResponse != null) statusKomentar = StatusKomentar.LOADED;

        loadFromServer();
        btnKomentar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bukaKomentar();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.share, menu);
        boolean b = super.onCreateOptionsMenu(menu);
        shareMenuItem = menu.findItem(R.id.menu_share);
        return b;
    }

    private void bukaKomentar() {
		/*
		if (statusKomentar == StatusKomentar.LOADING) {
			new TungguLoadKomentar();
			return;
		} else if (statusKomentar == StatusKomentar.NOT_LOADED) {
			loadKomentar();
			new TungguLoadKomentar();
			return;
		}
		 */
        if (statusKomentar != StatusKomentar.LOADED) {
            Toast.makeText(this, "Terjadi kesalahan", Toast.LENGTH_SHORT).show();
            return;
        }
        Intent it = new Intent(this, KomentarActivity.class);
        //it.putExtra(KomentarActivity.EXTRA_KOMENTAR_RESPONSE, komentarResponse);
        //it.putExtra(NewsInfoActivity.EXTRA_BERITA, berita);
        it.putExtra(NewsInfoActivity.EXTRA_BERITA, idBerita);
        startActivity(it);
    }

    private void loadFromServer() {
        if (!Util.isInternetAvailible(this)) {
            Toast.makeText(this, "Tidak ada koneksi internet", Toast.LENGTH_SHORT).show();
            return;
        }

        //String url = Config.URL_BERITA + "?aksi=2&userid=" + user.userid;
        //url += "&id=" + idBerita;
        //debug(getClass(), "Load berita url=" + url);
        Map<String, String> params = new HashMap<>();
        params.put("aksi", "2");
        params.put("userid", user.userid);
        params.put("id", idBerita);
        presenter.loadBerita(params);
		/*
		progressbar.setVisibility(View.VISIBLE);
		VolleyStringRequest request = new VolleyStringRequest(url, new VolleyStringRequest.Callback() {
			@Override
			public void onResponse(JsonObject jsonObject) {
				debug(getClass(), "Load berita response=" + jsonObject);
				progressbar.setVisibility(View.INVISIBLE);
				if (jsonObject.get("success").getAsBoolean()) {
					sharedPreferences.edit().putString(PREF_RESPONSE_ISI_BERITA + berita.getId(), jsonObject.toString()).apply();
					bindView(jsonObject);
				} else if (isResummed)
					Util.showDialog(BukaBeritaActivity.this, jsonObject.get("message").getAsString());
				loadKomentar();
			}
		});
		request.setTag(VOLLEY_TAG);
		VolleySingleton.getInstance(this).getRequestQueue().add(request);
		 */
    }

    private void bindView(BukaBeritaResponse beritaResponse) {
        if (beritaResponse.getDetailBerita().size() == 0) return;

        DetailBerita detailBerita = beritaResponse.getDetailBerita().get(0);
        String isi = detailBerita.getIsiBerita();
        isi = isi.replace("font-size:12px", "").replace("font-family:Arial,Helvetica,sans-serif", "");
        String fontStyle = "<style type=\"text/css\">" +
                "@font-face {" +
                "font-family: 'nunito';" +
                "src: url(\"nunito_regular.ttf\")" +
                "}" +
                "a {" +
                "text-decoration: none;" +
                "color: #416E96;" +
                "}" +
                "</style>";
        String sb = "<!DOCTYPE html>" +
                "<html lang=\"en\"><head>" +
                "<meta name=\"viewport\" content=\"width=device-width, initial-scale=1\", maximum-scale=1>" +
                fontStyle + "</head><body>" +
                "<div id=\"content\"  style=\"font-family: 'nunito'; font-size: 12pt;line-height: 1.2;text-align:justify\">" +
                isi +
                "</div>" +
                "</body></html>";

        ((WebView) findViewById(R.id.activity_buka_berita_textViewisiBerita)).loadDataWithBaseURL("file:///android_asset/", sb, "text/html", "utf-8", null);

        String subJudul = detailBerita.getSubJudul();
        Log.d("subtitle", subJudul);
        Log.d("berita", sb);
        ((TextView) findViewById(R.id.activity_buka_berita_textViewSubJudul)).setText(subJudul);

        String link = detailBerita.getLink();
        boolean linkExist = false;
        if (link != null && !link.isEmpty()) {
            linkExist = true;
            shareLink = link;
        }

        final boolean b = linkExist;
        new Handler(Looper.getMainLooper()).postDelayed(() -> {
            if(shareMenuItem != null) shareMenuItem.setVisible(b);
        }, 100);

        List<Berita> beritaTerkaits = beritaResponse.getBeritaTerkait();
        listBeritaTerkait.clear();
        listBeritaTerkait.addAll(beritaTerkaits);
        adapterBeritaTerkait.notifyDataSetChanged();
        findViewById(R.id.activity_buka_berita_textViewBeritaTerkait)
                .setVisibility(listBeritaTerkait.size() > 0 ? View.VISIBLE : View.INVISIBLE);

        List<Berita> bacaJuga = beritaResponse.getBacaJuga();
        listBeritaBacaJuaga.clear();
        listBeritaBacaJuaga.addAll(bacaJuga);
        adapterBacaJuga.notifyDataSetChanged();
        findViewById(R.id.activity_buka_berita_textViewBacaJuga)
                .setVisibility(listBeritaTerkait.size() > 0 ? View.VISIBLE : View.INVISIBLE);
    }

    private void bindView(JsonObject jsonObject) {
        JsonElement detailElement = jsonObject.get("detail_berita");
        if (detailElement.isJsonArray()) {
            JsonArray detailArray = detailElement.getAsJsonArray();
            if (detailArray.size() > 0) {
                JsonElement e = detailArray.get(0);
                if (e.isJsonObject()) {
                    String isi = e.getAsJsonObject().get("isi_berita").getAsString();
					/*
					if (isi != null && !isi.isEmpty()) {
						Spanned spanned;
						if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
							spanned = Html.fromHtml(isi, Html.FROM_HTML_SEPARATOR_LINE_BREAK_PARAGRAPH);
						} else spanned = Html.fromHtml(isi);
						((TextView) findViewById(R.id.activity_buka_berita_textViewisiBerita))
								.setText(spanned);
					}
					 */
                    isi = isi.replace("font-size:12px", "").replace("font-family:Arial,Helvetica,sans-serif", "");
                    String fontStyle = "<style type=\"text/css\">" +
                            "@font-face {" +
                            "font-family: 'nunito';" +
                            "src: url(\"nunito_regular.ttf\")" +
                            "}" +
                            "a {" +
                            "text-decoration: none;" +
                            "color: #416E96;" +
                            "}" +
                            "</style>";
                    String sb = "<!DOCTYPE html>" +
                            "<html lang=\"en\"><head>" +
                            "<meta name=\"viewport\" content=\"width=device-width, initial-scale=1\", maximum-scale=1>" +
                            fontStyle + "</head><body>" +
                            "<div id=\"content\"  style=\"font-family: 'nunito'; font-size: 12pt;line-height: 1.2;text-align:justify\">" +
                            isi +
                            "</div>" +
                            "</body></html>";

                    ((WebView) findViewById(R.id.activity_buka_berita_textViewisiBerita)).loadDataWithBaseURL("file:///android_asset/", sb, "text/html", "utf-8", null);

                    String subJudul = e.getAsJsonObject().get("sub_judul").getAsString();
                    Log.d("subtitle", subJudul);
                    Log.d("berita", sb);
                    ((TextView) findViewById(R.id.activity_buka_berita_textViewSubJudul))
                            .setText(subJudul);
                    JsonElement linkElement = e.getAsJsonObject().get("link");
                    boolean linkExist = false;
                    if (linkElement != null) {
                        String linkOri = linkElement.getAsString();
                        if (linkOri != null && !linkOri.isEmpty()) {
                            linkExist = true;
                            shareLink = linkOri;
                        }
                    }
                    final boolean b = linkExist;
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            shareMenuItem.setVisible(b);
                        }
                    }, 100);
                }
            }
        }

        JsonElement terkaitElement = jsonObject.get("berita_terkait");
        if (terkaitElement.isJsonArray()) {
            JsonArray array = terkaitElement.getAsJsonArray();
            listBeritaTerkait.clear();
            Gson gson = new Gson();
            for (JsonElement je : array) listBeritaTerkait.add(gson.fromJson(je, Berita.class));
            adapterBeritaTerkait.notifyDataSetChanged();
            findViewById(R.id.activity_buka_berita_textViewBeritaTerkait)
                    .setVisibility(listBeritaTerkait.size() > 0 ? View.VISIBLE : View.INVISIBLE);
        }

        JsonElement bacaJugaElement = jsonObject.get("baca_juga");
        if (bacaJugaElement.isJsonArray()) {
            JsonArray array = bacaJugaElement.getAsJsonArray();
            listBeritaBacaJuaga.clear();
            Gson gson = new Gson();
            for (JsonElement je : array) listBeritaBacaJuaga.add(gson.fromJson(je, Berita.class));
            adapterBacaJuga.notifyDataSetChanged();
            findViewById(R.id.activity_buka_berita_textViewBacaJuga)
                    .setVisibility(listBeritaTerkait.size() > 0 ? View.VISIBLE : View.INVISIBLE);
        }
    }

    private void loadKomentar() {
        if (statusKomentar == StatusKomentar.LOADING) return;
        if (!Util.isInternetAvailible(this)) return;
        //String url = Config.URL_BERITA + "?aksi=4&userid=" + user.userid;
        //url += "&id=" + idBerita;
        //debug(getClass(), "Load komentar url=" + url);
        Map<String, String> params = new HashMap<>();
        params.put("aksi", "4");
        params.put("userid", user.userid);
        params.put("id", idBerita);
        presenter.ambilKomentar(params);
		/*
		statusKomentar = StatusKomentar.LOADING;
		VolleyStringRequest request = new VolleyStringRequest(url, new VolleyStringRequest.Callback() {
			@Override
			public void onResponse(JsonObject jsonObject) {
				debug(getClass(), "Load komentar response=" + jsonObject);
				if (jsonObject.get("success").getAsBoolean()) {
					statusKomentar = StatusKomentar.LOADED;
					komentarResponse = jsonObject.toString();
				} else statusKomentar = StatusKomentar.NOT_LOADED;
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

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        debug(getClass(), "onOptionItemselected id=" + item.getItemId());
        if (item.getItemId() == R.id.menu_share) {
            Intent intent = new Intent(Intent.ACTION_SEND);
            String text = shareLink;
            intent.setType("text/*");
            debug(getClass(), "shareLink=" + shareLink);
            if (shareLink.isEmpty()) {

                return true;
            }
            if (text.contains(" ")) text = text.replaceFirst(" ", " \n");
            intent.putExtra(Intent.EXTRA_TEXT, text);
            new Share(intent, drawableGambar).execute(this);
            return true;
        } else
            return super.onOptionsItemSelected(item);
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
    public void onAmbilKomentarStart() {
        statusKomentar = StatusKomentar.LOADING;
        prograssKomentar.setVisibility(View.VISIBLE);
        btnKomentar.setVisibility(View.GONE);
    }

    @Override
    public void onAmbilKomentarSuccess(KomentarResponse response) {
        Paper.book().write("komentar_" + idBerita, response);
        statusKomentar = StatusKomentar.LOADED;
        prograssKomentar.setVisibility(View.GONE);
        btnKomentar.setVisibility(View.VISIBLE);
        Log.d("onAmbilKomentarSuccess", new Gson().toJson(response));
    }

    @Override
    public void onAmbilKomentarError(String message) {
        statusKomentar = StatusKomentar.NOT_LOADED;
        prograssKomentar.setVisibility(View.GONE);
        btnKomentar.setVisibility(View.VISIBLE);
    }

    @Override
    public void onLoadBeritaStart() {
        progressbar.setVisibility(View.VISIBLE);
    }

    @Override
    public void onLoadBeritaSuccess(BukaBeritaResponse response) {
        Log.d("onLoadBeritaSuccess", new Gson().toJson(response));
        progressbar.setVisibility(View.INVISIBLE);
        Paper.book().write(PREF_RESPONSE_ISI_BERITA + idBerita, response);
        bindView(response);
        loadKomentar();
    }

    @Override
    public void onLoadBeritaError(String message) {
        progressbar.setVisibility(View.INVISIBLE);
        if (isResummed) Util.showDialog(BukaBeritaActivity.this, message);
    }

    private enum StatusKomentar {
        NOT_LOADED, LOADING, LOADED
    }

    private static class Share extends AsyncTask<Context, Void, Context> {
        Intent intent;
        Drawable drawable;

        public Share(Intent intent, Drawable drawable) {
            this.intent = intent;
            this.drawable = drawable;
        }

        @Override
        protected Context doInBackground(Context... contexts) {
            Context context = contexts[0];
            if (drawable == null) {
                Log.e(getClass().getName(), "Cannot create image, Drawable==null");
                return context;
            }
            File filesFolder = context.getFilesDir();
            File shareFolder = new File(filesFolder, "share");
            if (!shareFolder.exists()) shareFolder.mkdirs();
            File shareFile = new File(shareFolder, "gambar.png");
            try {
                if (!shareFile.exists()) shareFile.createNewFile();
                Bitmap bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
                Canvas c = new Canvas(bitmap);
                drawable.draw(c);
                int shorter = Math.min(bitmap.getHeight(), bitmap.getWidth());
                float scale = 1;
                float finalHeight = context.getResources().getDisplayMetrics().density * 100F;
                if (shorter > finalHeight) {
                    scale = finalHeight / shorter;
                }
//				Log.e(getClass().getName(), "oriBitmap W=" + bitmap.getWidth());
//				Log.e(getClass().getName(), "oriBitmap H=" + bitmap.getHeight());
//				Log.e(getClass().getName(), "oriBitmap scale=" + scale);
                Bitmap finalBitmap = Bitmap.createScaledBitmap(bitmap, (int) (bitmap.getWidth() * scale), (int) (bitmap.getHeight() * scale), false);
//				Log.e(getClass().getName(), "finalBitmap W=" + finalBitmap.getWidth());
//				Log.e(getClass().getName(), "finalBitmap H=" + finalBitmap.getHeight());
                FileOutputStream fos = new FileOutputStream(shareFile);
                finalBitmap.compress(Bitmap.CompressFormat.PNG, 100, fos);
                fos.flush();
                fos.close();
                intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                intent.setType("image/*");
                intent.putExtra(Intent.EXTRA_STREAM,
                        FileProvider.getUriForFile(context, context.getPackageName() + ".provider",
                                shareFile));
            } catch (IOException e) {
                Log.e(getClass().getName(), e.getMessage());
            }
            return context;
        }

        @Override
        protected void onPostExecute(Context context) {
            context.startActivity(Intent.createChooser(intent, "Bagikan dengan/"));
        }
    }

    private class TungguLoadKomentar extends AsyncTask<Void, Void, Boolean> {

        Dialog dialog;

        public TungguLoadKomentar() {
            dialog = ProgressDialog.show(BukaBeritaActivity.this, null, "Memuat komentar...", true, true);
            dialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
                @Override
                public void onCancel(DialogInterface dialog) {
                    cancel(true);
                }
            });
            execute();
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            if (statusKomentar == StatusKomentar.LOADED) return true;
            if (statusKomentar == StatusKomentar.NOT_LOADED) return false;
            while (statusKomentar == StatusKomentar.LOADING && !isCancelled()) {
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            return statusKomentar == StatusKomentar.LOADED;
        }

        @Override
        protected void onPostExecute(Boolean success) {
            if (dialog != null && dialog.isShowing()) dialog.dismiss();
            if (!isCancelled()) {
                if (success) bukaKomentar();
                else
                    Toast.makeText(BukaBeritaActivity.this, "Terjadi kesalahan", Toast.LENGTH_SHORT).show();
            }

        }
    }

}
